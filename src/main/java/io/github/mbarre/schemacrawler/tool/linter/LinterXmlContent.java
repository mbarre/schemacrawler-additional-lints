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
import io.github.mbarre.schemacrawler.utils.XmlUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.options.Config;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Linter to check if non XML type is used whereas XML data is store in column
 * @author mbarre
 * @since 1.0.1
 */
public class LinterXmlContent extends BaseLinter {

    private static final Logger LOGGER = Logger.getLogger(LinterXmlContent.class.getName());

    public static final String SAMPLE_SIZE_PARAM = "sampleSize";

    private Integer sampleSize;

    /**
     * The lint
     */
    public LinterXmlContent () {
        sampleSize = 1000;
        setSeverity(LintSeverity.high);
    }

    @Override
    public void configure(Config config) {
        sampleSize = config.getIntegerValue(SAMPLE_SIZE_PARAM, 1000);
    }

    /**
     * Get lint description
     * @return lint description
     */
    @Override
    public String getDescription() {
        return getSummary();
    }

    /**
     * Get lint summary
     * @return summary
     */
    @Override
    public String getSummary() {
        return "should be XML type";
    }

    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     * @throws SchemaCrawlerException SchemaCrawlerException
     */
    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {

        LOGGER.log(Level.CONFIG, "<"+SAMPLE_SIZE_PARAM+"> parameter set to {0}%", sampleSize);

        try (Statement stmt = connection.createStatement()){
            String sql;
            String columnName;
            String tableName = table.getName().replaceAll("\"", "");
            List<Column> columns = getColumns(table);

            Long totalRows = 0L;
			totalRows = LintUtils.getTableSize(stmt, tableName);
            LOGGER.log(Level.INFO, "totalrows="+totalRows);
            Set<Long> sampleIndexes = LintUtils.generateSample(sampleSize, totalRows);
            LOGGER.log(Level.INFO, "sampleIndexes="+sampleIndexes);
            for (Column column : columns) {

                if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getVendorTypeNumber())){

                    columnName = column.getName().replaceAll("\"", "");

                    sql = "select \"" + columnName + "\" from \"" + tableName +"\" where \"" + columnName + "\" like '<%>'" ;
                    LOGGER.log(Level.CONFIG, "SQL : {0}", sql);
                    LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());

                    try(ResultSet rs = stmt.executeQuery(sql)){
                        boolean found = false;
                        Long i = 0L;
                        while (rs.next() && !found) {
                            if(sampleIndexes.contains(i)) {
                                String data = rs.getString(column.getName());
                                if (XmlUtils.isXmlContent(data)) {
                                    addTableLint(table, getDescription(), column.getFullName());
                                    found = true;
                                }
                            }
                            i++;
                        }
                    }catch (SQLException ex) {
                        LOGGER.severe(ex.getMessage());
                        throw new SchemaCrawlerException(ex.getMessage(), ex);
                    }
                }
                else if(column.getColumnDataType().getJavaSqlType().getVendorTypeNumber() == Types.CLOB){
                    //TODO voir comment gérer ce cas
                }
            }

        }catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
    }

}
