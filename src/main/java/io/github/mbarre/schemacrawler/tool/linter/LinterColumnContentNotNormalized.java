package io.github.mbarre.schemacrawler.tool.linter;

/*
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2016 github
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.lint.LinterConfig;
import schemacrawler.tools.options.Config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test column contents normalization. Detect wether foreign table should have
 * be used.
 * @author salad74
 */
public class LinterColumnContentNotNormalized extends BaseLinter {
    
    private static final Logger LOGGER = Logger.getLogger(LinterColumnContentNotNormalized.class.getName());
    private static final String NB_REPEAT_TOLERANCE_CONFIG = "nbRepeatTolerance";
    private static final String MIN_TEXT_COLUMN_SIZE_CONFIG = "minTextColumnSize";
    private Integer sampleSize;
    
    /**
     * Repeat tolerance : 1 is the most agressive, duplicates occur since there
     * are at least tow reps of the same value.
     */
    public static final int NB_REPEAT_TOLERANCE = 2;
    
    /**
     * The minimal length of the text based column. We consider that a 1-length
     * char based column is acceptable.
     */
    public static final int MIN_TEXT_COLUMN_SIZE = 2;
    
    private int nbRepeatTolerance;
    private int minTextColumnSize;
    
    
    /**
     * Build the lint
     */
    public LinterColumnContentNotNormalized() {
        setSeverity(LintSeverity.medium);
        nbRepeatTolerance = NB_REPEAT_TOLERANCE;
        minTextColumnSize = MIN_TEXT_COLUMN_SIZE;
        sampleSize = 1000;
    }
    
    /**
     * Get the lint descrption
     * @return The Lint description
     */
    @Override
    public String getDescription() {
        return getSummary();
    }
    
    /**
     * Get the Summary
     * @return The lint summary
     */
    @Override
    public String getSummary() {
        return "too much duplicates";
    }

    @Override
    public void configure(LinterConfig config) {
        nbRepeatTolerance = Integer.valueOf(config.getProperties().get(NB_REPEAT_TOLERANCE_CONFIG));
        minTextColumnSize = Integer.valueOf(config.getProperties().get(MIN_TEXT_COLUMN_SIZE_CONFIG));
    }
        
    /**
     * Tells wether a column is text based or not... and if minimal length
     * requirements are met to make normalization computations.
     *
     * @param javaSqlType javaSqlType
     * @param colSize colSize
     * @param minColumnSize minColSize
     * @return mustColumnBeTested
     */
    public static final boolean mustColumnBeTested(int javaSqlType, int colSize, int minColumnSize) {
        if (LintUtils.isSqlTypeTextBased(javaSqlType)) {
            // test minimal size to pass test
            if(colSize > minColumnSize){
                LOGGER.log(Level.INFO, "Column min size requirement are met : <{0}> is greater than <{1}>", new Object[]{colSize, minColumnSize});
                return true;
            }
            else{
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection the db connection
     * @throws SchemaCrawlerException SchemaCrawlerException
     */
    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {
        
        LOGGER.log(Level.CONFIG, "<nbRepeatTolerance> parameter set to {0}",nbRepeatTolerance);
        LOGGER.log(Level.CONFIG, "<minTextColumnSize> parameter set to {0}",minTextColumnSize);
        
        try (Statement stmt = connection.createStatement()){
            String sql;
            String tableName = table.getName().replaceAll("\"", "");
            List<Column> columns = getColumns(table);

            Long totalRows = 0L;
            totalRows = LintUtils.getTableSize(stmt, tableName);
            LOGGER.log(Level.INFO, "totalrows=" + totalRows);
            Set<Long> sampleIndexes = LintUtils.generateSample(sampleSize, totalRows);
            LOGGER.log(Level.INFO, "sampleIndexes=" + sampleIndexes);

            for (Column column : columns) {
                if (LinterColumnContentNotNormalized.mustColumnBeTested(column.getColumnDataType().getJavaSqlType().getVendorTypeNumber(), column.getSize(), minTextColumnSize)) {
                    // test based column, perform test
                    LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                    String columnName = column.getName().replaceAll("\"", "");
                    sql = "select \"" + columnName + "\", count(*)  as counter from \"" + tableName + "\" where \"" + columnName + "\" is not null group by \"" + columnName + "\" having count(*) > " + nbRepeatTolerance + " order by count(*) desc";
                    LOGGER.log(Level.CONFIG, "SQL : {0}", sql);

                    try(ResultSet rs = stmt.executeQuery(sql)){
                        Long i = 0L;
                        while (rs.next()) {
                            if (sampleIndexes.contains(i)) {
                                int nbRepeats = rs.getInt("counter");
                                LOGGER.log(Level.CONFIG, "Found <{0}> repetitions of the same value <{1}> in <{2}>", new Object[]{nbRepeats, rs.getString(1), column});
                                if (nbRepeats > nbRepeatTolerance) {
                                    LOGGER.log(Level.CONFIG, "Adding lint as nbRepeats exceeds tolerance ({0} > {1} )", new Object[]{nbRepeats, nbRepeatTolerance});
                                    addTableLint(table, "<" + nbRepeats + "> repetitions of the same value <" + rs.getString(1) + ">", column.getFullName());
                                }
                            }
                            i++;
                        }
                    } catch (SQLException ex) {
                        LOGGER.severe(ex.getMessage());
                        throw new SchemaCrawlerException(ex.getMessage(), ex);
                    }
                } else {
                    // no text based column, skip test
                    LOGGER.log(Level.CONFIG, "<{0}> is not text based : normalize test will be skipped.", column);
                }
                
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
        
    }
}
