package io.github.mbarre.schemacrawler.tool.linter;

/*-
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2017 github
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

import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import static java.util.Objects.requireNonNull;

/**
 * Linter to check if a table has foreign key or is referenced in any other table.
 * A table with no relation is suspicious.
 * @author mbarre
 * @since 1.0.1
 */
public class LinterOrphanTable extends BaseLinter {

    public LinterOrphanTable() {
        setSeverity(LintSeverity.critical);
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
     * Get the lint summary
     * @return the lint summary
     */
    @Override
    public String getSummary()
    {
        return "table has no relation with any other table.";
    }

    /**
     * The lint that does the job
     * @param table table
     * @param connection
     */
    @Override
    protected void lint(final Table table, final Connection connection)
    {
        requireNonNull(table, "No table provided");

        if(table.getExportedForeignKeys().isEmpty() && table.getForeignKeys().isEmpty())
            addTableLint(table, getDescription(), table.getName());
    }
}
