/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import java.sql.Connection;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 *
 * @author barmi83
 */
public class LinterClobTypeColumn extends BaseLinter {
    private static final Logger LOGGER = Logger.getLogger(LinterClobTypeColumn.class.getName());
    
    
    
    /**
     * The lint that parses and test primary keys
     *
     */
    public LinterClobTypeColumn() {
        super();
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
        return "CLOB/TEXT should not be used.";
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     */
    @Override
    protected void lint(final Table table, final Connection connection) {
        
        System.out.println(Types.CLOB);
        
        for (Column column : table.getColumns()) {
                if(LintUtils.isSqlTypeLargeTextBased(column.getColumnDataType())){
                    addLint(table, getDescription(), column.getName());
                    LOGGER.log(Level.INFO, "{0} is CLOB/TEXT type.", column.getFullName());
                }
            }
    }
    
}
