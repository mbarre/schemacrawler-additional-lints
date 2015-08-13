/*
TODO : implement sample option

SELECT * FROM myTable
WHERE attribute = 'myValue'
ORDER BY random()
LIMIT 1000;
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

    /**
     * The repeat tolerance : the lint is throwing an alert
     * is a chain/value has more than NB_REPEAT_TOLERANCE occurrences
     */
    public static final int NB_REPEAT_TOLERANCE = 2;

    /**
     * Defines the minimal text based column on which to apply the lint. This
     * is the most agressive parameter
     */
    public static final int MIN_TEXT_COLUMN_SIZE = 2;
    
    //TODO : implement the sample parameter so the whole table is not scanned, only a random subset
    
    
    private static final Logger LOGGER = Logger.getLogger(LinterColumnContentNotNormalized.class.getName());

    /**
     * Lint that tests column content normalization by detecting repetition
     * of text based values instead of a numeric or CHAR(1) values.
     */
    public LinterColumnContentNotNormalized() {
        setSeverity(LintSeverity.high);
    }

    /**
     * Returns the lint description
     * @return
     */
    @Override
    public String getDescription() {
        return getSummary();
    }

    /**
     * Returns the Lint summary
     * @return
     */
    @Override
    public String getSummary() {
        return " should not have so many duplicates.";
    }

    /**
     * Tells wether a column is text based or not... and if minimal length
     * requirements are met to make normalization computations.
     *
     * @param column
     * @return
     */
    public static final boolean mustColumnBeTested(Column column) {
        if ((column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.NVARCHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.LONGNVARCHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.LONGVARCHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.CHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.NCHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.NVARCHAR)
                || (column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.VARCHAR)) {
            // test minimal size to pass test
            LOGGER.log(Level.INFO, "<{0}> is text based.", column);
            if(column.getSize() > MIN_TEXT_COLUMN_SIZE){
                LOGGER.log(Level.INFO, "Column min size requirement are met : <{0}> is greater than <{1}>", new Object[]{column.getSize(), MIN_TEXT_COLUMN_SIZE});
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
     *
     * @param table
     * @param connection
     * @throws SchemaCrawlerException
     */
    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {

        try {
            String sql;
            Statement stmt;
            stmt = connection.createStatement();
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                if (LinterColumnContentNotNormalized.mustColumnBeTested(column)) {
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
