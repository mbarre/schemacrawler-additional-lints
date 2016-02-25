package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import schemacrawler.tools.lint.BaseLinter;

/**
 * Linter to check if numeric column is used instead of boolean column
 * Created by barmi83 on 28/12/15.
 */
public class LinterBooleanContent extends BaseLinter {
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
        return "Should be boolean type.";
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
        
        try (Statement stmt = connection.createStatement()){
            
            String sql;
            List<Column> columns = table.getColumns();
            String columnName;
            String tableName = table.getName().replaceAll("\"", "");
            
            for (Column column : columns) {
                columnName = column.getName().replaceAll("\"", "");
                
                int columnDataType = column.getColumnDataType().getJavaSqlType().getJavaSqlType();
                if(LintUtils.isSqlTypeNumericBased(columnDataType)){
                    LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                    
                    sql = "select count (distinct \"" + columnName + "\") as countRow from \"" + tableName +"\"";
                    LOGGER.log(Level.CONFIG, "SQL : {0}", sql);
                    
                    ResultSet rs = stmt.executeQuery(sql);
                    boolean found = false;
                    if(rs.next()){
                        int count = rs.getInt("countRow");
                        
                        if(count == 2){
                            sql = "select distinct \"" + columnName + "\" from \"" + tableName + "\"" ;
                            LOGGER.log(Level.CONFIG, "SQL : {0}", sql);
                            
                            rs = stmt.executeQuery(sql);
                            boolean trueFound = false;
                            boolean falseFound = false;
                            while (rs.next()) {
                                if(rs.getObject(columnName) != null) {
                                    int data = ((Number) rs.getObject(columnName)).intValue();
                                    if(data == 1)
                                        trueFound = true;
                                    else if(data == 0)
                                        falseFound = true;
                                }
                            }
                            
                            if(trueFound && falseFound){
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
