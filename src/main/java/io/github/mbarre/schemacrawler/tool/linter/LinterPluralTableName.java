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

import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;
import static java.util.Objects.requireNonNull;
import java.sql.Connection;

/**
 * Linter to check if a table has its name plural ou singular
 * Table represents a collection of entities, there is no need for plural names.
 * @author mbarre
 */
public class LinterPluralTableName extends BaseLinter {

    public LinterPluralTableName() {
        setSeverity(LintSeverity.medium);
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
        return "Don't use plural for table name.";
    }

    /**
     * The lint that does the job
     * @param table table
     * @param connection
     */
    @Override
    protected void lint(final Table table, final Connection connection){
        requireNonNull(table, "No table provided");

            if(table.getName().endsWith("s"))
                addTableLint(table, getDescription(), table.getName());
    }

}
