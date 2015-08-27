/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.tool.linter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.linter.LinterTableSql;

/**
 *
 * @author salad74
 */
public class LinterColumnContentNotNormalized extends LinterTableSql {

    public static final int NB_REPEAT_TOLERANCE = 2;
    public static final int MIN_TEXT_COLUMN_SIZE = 2;
    private static final Logger LOGGER = Logger.getLogger(LinterColumnContentNotNormalized.class.getName());

    public LinterColumnContentNotNormalized() {
        setSeverity(LintSeverity.high);
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public String getSummary() {
        return " should not have so many duplicates.";
    }

    /**
     * Tells wether a column is text based or not.
     * @param javaSqlType
     * @return
     */
    public static final boolean isSqlTypeTextBased(int javaSqlType) {
        return (javaSqlType == Types.NVARCHAR)
                || (javaSqlType == Types.LONGNVARCHAR)
                || (javaSqlType == Types.LONGVARCHAR)
                || (javaSqlType == Types.CHAR)
                || (javaSqlType == Types.NCHAR)
                || (javaSqlType == Types.VARCHAR);
        
    }
    
    /**
     * Tells wether a column is text based or not... and if minimal length
     * requirements are met to make normalization computations.
     *
     * @param javaSqlType
     * @param colSize
     * @return
     */
    public static final boolean mustColumnBeTested(int javaSqlType, int colSize) {
        if (LinterColumnContentNotNormalized.isSqlTypeTextBased(javaSqlType)) {
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

    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {

        try {
            String sql;
            Statement stmt;
            stmt = connection.createStatement();
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
            stmt.close();
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }

    }
}
