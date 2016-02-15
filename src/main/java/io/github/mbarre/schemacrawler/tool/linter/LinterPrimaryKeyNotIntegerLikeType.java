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
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.linter.LinterTableSql;

/*
* Linter to check if primary key is integer like type
* Created by barmi83
*/

public class LinterPrimaryKeyNotIntegerLikeType extends LinterTableSql {
    private static final Logger LOGGER = Logger.getLogger(LinterPrimaryKeyNotIntegerLikeType.class.getName());
    
    
    
    /**
     * The lint that parses and test primary keys
     *
     */
    public LinterPrimaryKeyNotIntegerLikeType() {
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
        return "should be Integer like type or eventually char(1).";
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     */
    @Override
    protected void lint(final Table table, final Connection connection) {
        
   
        for (Column column : table.getColumns()) {
            if(column.isPartOfPrimaryKey()){
                if(!LintUtils.isSqlTypeIntegerBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType()) 
                        && !(column.getColumnDataType().getJavaSqlType().getJavaSqlType() ==  Types.CHAR && column.getSize() == 1)){
                    addLint(table, getDescription(), column.getFullName());
                    LOGGER.log(Level.INFO, "{0} is not integer like type nor char(1).", column.getFullName());
                }
            }
        }
    }
    
}
