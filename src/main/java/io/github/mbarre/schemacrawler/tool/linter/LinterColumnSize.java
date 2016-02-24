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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 *
 * @author barmi83
 */
public class LinterColumnSize extends BaseLinter {
    
    private static Logger LOGGER = LoggerFactory.getLogger(LinterColumnSize.class);
    private static final int MIN_COLUMN_SIZE_PERCENT = 10;
    public static final String COLUMNSIZE_CONFIG_PARAM = "minColumnSizePercent";
    
    private double minColumnSizePercent;
   
    
    /**
     * The lint that parses and test numeric content
     *
     */
    public LinterColumnSize() {
        setSeverity(LintSeverity.high);
        minColumnSizePercent = MIN_COLUMN_SIZE_PERCENT;
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
        return "column is oversized regarding its content.";
    }
    
    @Override
    protected void configure(final Config config){
        int value = config.getIntegerValue(COLUMNSIZE_CONFIG_PARAM, MIN_COLUMN_SIZE_PERCENT);
        minColumnSizePercent = Double.valueOf(value);
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
        
        LOGGER.info("Min. content size set to : {0}% of declared column size.", minColumnSizePercent);
        
        try (Statement stmt = connection.createStatement()){
            
            String sql;
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                
                if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType())
                        || LintUtils.isSqlTypeLargeTextBased(column.getColumnDataType())){
                    
                    sql = "select max(length(\"" + column.getName() + "\")) as max from \"" + table.getName() +"\"" ;
                    LOGGER.info("SQL : {0}", sql);
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        int maxLength = rs.getInt(1);
                        if(maxLength > 0 && (maxLength*100/column.getSize() < minColumnSizePercent)){
                            addLint(table, getDescription(), column.getFullName());
                        }
                    }
                }
            }
            
        }catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
    }
}
