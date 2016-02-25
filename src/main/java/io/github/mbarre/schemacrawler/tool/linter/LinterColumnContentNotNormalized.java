/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 * Test column contents normalization. Detect wether foreign table should have
 * be used.
 * @author salad74
 */
public class LinterColumnContentNotNormalized extends BaseLinter {
    
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
    private static final Logger LOGGER = Logger.getLogger(LinterColumnContentNotNormalized.class.getName());
    
    /**
     * Build the lint
     */
    public LinterColumnContentNotNormalized() {
        setSeverity(LintSeverity.high);
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
        return " should not have so many duplicates.";
    }
    
    
    /**
     * Tells wether a column is text based or not... and if minimal length
     * requirements are met to make normalization computations.
     *
     * @param javaSqlType javaSqlType
     * @param colSize colSize
     * @return mustColumnBeTested
     */
    public static final boolean mustColumnBeTested(int javaSqlType, int colSize) {
        if (LintUtils.isSqlTypeTextBased(javaSqlType)) {
            // test minimal size to pass test
            if(colSize > MIN_TEXT_COLUMN_SIZE){
                LOGGER.log(Level.INFO, "Column min size requirement are met : <{0}> is greater than <{1}>", new Object[]{colSize, MIN_TEXT_COLUMN_SIZE});
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
        
        try (Statement stmt = connection.createStatement()){
            String sql;
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                if (LinterColumnContentNotNormalized.mustColumnBeTested(column.getColumnDataType().getJavaSqlType().getJavaSqlType(), column.getSize())) {
                    // test based column, perform test
                    LOGGER.log(Level.INFO, "Analyzing colum <{0}>", column);
                    sql = "select \"" + column.getName() + "\", count(*)  as counter from \"" + table.getName() + "\" where \"" + column.getName() + "\" is not null group by \"" + column.getName() + "\" having count(*) > " + NB_REPEAT_TOLERANCE + " order by count(*) desc";
                    LOGGER.log(Level.INFO, "SQL : {0}", sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        int nbRepeats = rs.getInt("counter");
                        LOGGER.log(Level.INFO, "Found <{0}> repetitions of the same value <{1}> in <{2}>", new Object[]{nbRepeats, rs.getString(1), column});
                        if(nbRepeats > NB_REPEAT_TOLERANCE){
                            LOGGER.log(Level.INFO, "Adding lint as nbRepeats exceeds tolerance ({0} > {1} )", new Object[]{nbRepeats, NB_REPEAT_TOLERANCE});
                            addLint(table, "Found <" + nbRepeats + "> repetitions of the same value <" + rs.getString(1) + "> in <" + column + ">", column.getFullName());
                        }
                    }
                } else {
                    // no text based column, skip test
                    LOGGER.log(Level.INFO, "<{0}> is not text based : normalize test will be skipped.", column);
                }
                
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }
        
    }
}
