package io.github.mbarre.schemacrawler.tool.linter;

/*
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2016 github
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import io.github.mbarre.schemacrawler.utils.LintUtils;
import java.sql.Connection;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/*
* Linter to check if primary key is integer like type
* Created by barmi83
*/

public class LinterPrimaryKeyNotIntegerLikeType extends BaseLinter {
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
        return "Should be Integer like type or eventually char(1).";
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
                LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                if(!LintUtils.isSqlTypeIntegerBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType()) 
                        && !(column.getColumnDataType().getJavaSqlType().getJavaSqlType() ==  Types.CHAR && column.getSize() == 1)){
                    addLint(table, getDescription(), column.getFullName());
                }
            }
        }
    }
    
}
