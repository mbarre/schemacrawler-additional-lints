package io.github.mbarre.schemacrawler.tool.linter;

/*-
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2018 github
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

import java.sql.Connection;
import java.util.List;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

public class LinterNoSpaceInNames extends BaseLinter {


    /**
     * The lint that test if the table and column names contain spaces
     */
    public LinterNoSpaceInNames() {
        setSeverity(LintSeverity.high);
    }

    /**
     * Get the lint description
     * @return lint description
     */
    @Override
    public String getDescription()
    {
        return getSummary();
    }


    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     */
    @Override
    protected void lint(Table table, Connection connection) {
        List<Column> columns = getColumns(table);
        String tableName = table.getName().replaceAll("\"", "");
        if(tableName.contains(" ")) {
            addLint(table, getDescription(), table.getFullName());
        }

        for (Column column : columns) {
            if(column.getName().contains(" "))
                addLint(table, getDescription(), column.getFullName());
        }
    }


    /**
     * Get the summary
     * @return the summary
     */
    @Override
    public String getSummary() {
        return "Space should not be used in table or column names";
    }
}
