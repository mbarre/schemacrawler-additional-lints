package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import io.github.mbarre.schemacrawler.utils.XmlUtils;

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
 * Linter to check if non XML type is used whereas XML data is store in column
 * @author mbarre
 * @since 1.0.1
 */
public class LinterXmlContent extends LinterTableSql {
	
    private static final Logger LOGGER = Logger.getLogger(LinterXmlContent.class.getName());


	public LinterXmlContent () {
        setSeverity(LintSeverity.high);
    }

    @Override
    public String getDescription() {
        return getSummary();
    }

    @Override
    public String getSummary() {
        return " should be XML type.";
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
                        
                        if(XmlUtils.isXmlContent(data)){
                            LOGGER.log(Level.INFO, "Adding lint as data is XML but column type is not XML.");
                            addLint(table, getDescription(), column.getFullName());
                            found = true;
                        }
                    }            		
            	}
            	else if(column.getColumnDataType().getJavaSqlType().getJavaSqlType() == Types.CLOB){
            		//TODO voir comment gérer ce cas pour ne pas que ca explose !
            	}
            }
            
        }catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }
}
