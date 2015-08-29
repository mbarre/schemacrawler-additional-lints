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
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.linter.LinterTableSql;

/**
 * Linter to check if non JSONB type is used whereas JSON data is store in column
 * @author mbarre
 * @since 1.0.1
 */
public class LinterJsonContent extends LinterTableSql {
	
    private static final Logger LOGGER = Logger.getLogger(LinterJsonContent.class.getName());


	public LinterJsonContent () {
        setSeverity(LintSeverity.high);
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public String getSummary() {
        return " should be JSON or JSONB type.";
    }
    
    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {

        try (Statement stmt = connection.createStatement()){
            String sql;
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
            	if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType())){
            		
            		sql = "select " + column.getName() + " from " + table.getName() ;
                    LOGGER.log(Level.INFO, "SQL : {0}", sql);
                    
                    ResultSet rs = stmt.executeQuery(sql);
                    boolean found = false;
                    while (rs.next() && !found) {
                        String data = rs.getString(column.getName());
                        
                        if(JSonUtils.isJsonContent(data)){
                            LOGGER.log(Level.INFO, "Adding lint as data is JSON but column type is not JSONB or JSON.");
                            addLint(table, getDescription(), column.getFullName());
                            found = true;
                        }
                    }            		
            	}
            }
            
        }catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
}
