package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.JSonUtils;
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
 * Linter to check if non JSONB type is used whereas JSON data is store in column
 * @author mbarre
 * @since 1.0.1
 */
public class LinterJsonContent extends BaseLinter {
	
    private static final Logger LOGGER = Logger.getLogger(LinterJsonContent.class.getName());

    /**
     * The lint that parses and test Json content
     */
    public LinterJsonContent () {
        setSeverity(LintSeverity.high);
    }

    /**
     * Get lint descrption
     * @return lint description
     */
    @Override
    public String getDescription() {
        return getSummary();
    }

    /**
     * Get lint Summary
     * @return lint Summary
     */
    @Override
    public String getSummary() {
        return "Should be JSON or JSONB type.";
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
            if("PostgreSQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName()) &&
                    "9.4".compareTo(connection.getMetaData().getDatabaseProductVersion()) <= 0){
               
                String sql;
                List<Column> columns = table.getColumns();
                for (Column column : columns) {
                    if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType())){
                        
                        LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                        
                        sql = "select \"" + column.getName() + "\" from \"" + table.getName() +"\"" ;
                        LOGGER.log(Level.CONFIG, "SQL : {0}", sql);
                        
                        ResultSet rs = stmt.executeQuery(sql);
                        boolean found = false;
                        while (rs.next() && !found) {
                            String data = rs.getString(column.getName());
                            
                            if(JSonUtils.isJsonContent(data)){
                                addLint(table, getDescription(), column.getFullName());
                                found = true;
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
