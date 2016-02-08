package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.linter.LinterTableSql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Linter to check if numeric column is used instead of boolean column
 * Created by barmi83 on 28/12/15.
 */
public class LinterBooleanContent extends LinterTableSql {
    private static final Logger LOGGER = Logger.getLogger(LinterBooleanContent.class.getName());
    
    /**
     * The lint that parses and test numeric content
     *
     */
    public LinterBooleanContent() {
        super();
        setSeverity(LintSeverity.high);
    }
    
    /**
     * Get the description
     * @return the description
     */
    @Override
    public String getDescription()
    {
        return getSummary();
    }
    
    /**
     * Get lint Summary
     * @return lint Summary
     */
    @Override
    public String getSummary() {
        return " should be boolean type.";
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
        
        LOGGER.log(Level.INFO, "lint boolean");
        try (Statement stmt = connection.createStatement()){
            
            String sql;
            List<Column> columns = table.getColumns();
            String columnName;
            for (Column column : columns) {
                columnName = column.getName().replaceAll("\"", "");
                
                int columnDataType = column.getColumnDataType().getJavaSqlType().getJavaSqlType();
                if(LintUtils.isSqlTypeNumericBased(columnDataType)){
                    
                    sql = "select count (distinct \"" + columnName + "\") as countRow from \"" + table.getName() +"\"";
                    LOGGER.log(Level.INFO, "SQL : {0}", sql);
                    
                    ResultSet rs = stmt.executeQuery(sql);
                    boolean found = false;
                    if(rs.next()){
                        int count = rs.getInt("countRow");
                        
                        if(count == 2){
                            sql = "select distinct \"" + columnName + "\" from \"" + table.getName() + "\"" ;
                            LOGGER.log(Level.INFO, "SQL : {0}", sql);
                            
                            rs = stmt.executeQuery(sql);
                            boolean trueFound = false;
                            boolean falseFound = false;
                            while (rs.next()) {
                                int data = ((Number) rs.getObject(columnName)).intValue();
                                if(data == 1)
                                    trueFound = true;
                                else if(data == 0)
                                    falseFound = true;
                            }
                            
                            if(trueFound && falseFound){
                                LOGGER.log(Level.INFO, "Adding lint as data is boolean but column type is " + column.getColumnDataType());
                                addLint(table, getDescription(), column.getFullName());
                            }
                        }
                        
                        
                    }
                    
                }
            }
            
        }catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
    }
}
