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

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinterForbiddenPrimaryKeyType extends BaseLinter
{
    private static final Logger LOGGER = Logger.getLogger(LinterForbiddenPrimaryKeyType.class.getName());

    /**
     * The lint that parses and test primary keys
     *
     */
    public LinterForbiddenPrimaryKeyType() {
        super();
        setSeverity(LintSeverity.high);
    }

    @Override
    protected void lint(Table table, Connection connection) throws SchemaCrawlerException {
        List<Column> columns = getColumns(table);
        for (Column column : columns) {
            if(column.isPartOfPrimaryKey()){
                LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                if(LintUtils.isSqlTypeDateBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType()))
                    addLint(table, getDescription(), column.getFullName());
            }
        }
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

    @Override
    public String getSummary() {
        return "Current primary Key type is forbidden.";
    }
}
