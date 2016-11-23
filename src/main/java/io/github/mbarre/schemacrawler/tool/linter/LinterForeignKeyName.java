package io.github.mbarre.schemacrawler.tool.linter;

/*-
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

import schemacrawler.schema.*;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 *  Check that foreign key name has right prefix or suffix name
 * Created by barmi83 on 12/09/16.
 */

public class LinterForeignKeyName  extends BaseLinter {

    private static final Logger LOGGER = Logger.getLogger(LinterForeignKeyName.class.getName());

    public static final String PREFIX_CONFIG_PARAM = "foreignKeyPrefix";
    public static final String SUFFIX_CONFIG_PARAM = "foreignKeySuffix";

    private String prefix;
    private String suffix;

    public LinterForeignKeyName() {
        prefix = "";
        suffix = "";
        setSeverity(LintSeverity.medium);
    }

    @Override
    public String getSummary() {
        return "FK name does not contain required prefix or suffix.";
    }

    /**
     * Get the description
     * @return the description
     */
    @Override
    public String getDescription() {
        return "Foreign key data type does not match Primary key";
    }

    @Override
    public void configure(Config config) {
        prefix = config.getStringValue(PREFIX_CONFIG_PARAM, "");
        suffix = config.getStringValue(SUFFIX_CONFIG_PARAM, "");
    }

    @Override
    protected void lint(Table table, Connection connection) throws SchemaCrawlerException {

        LOGGER.log(Level.CONFIG, "<"+PREFIX_CONFIG_PARAM+"> parameter set to {0}%", prefix);
        LOGGER.log(Level.CONFIG, "<"+SUFFIX_CONFIG_PARAM+"> parameter set to {0}%", suffix);

        requireNonNull(table, "No table provided");
        findMismatchedForeignKeys(table);

    }

    private void findMismatchedForeignKeys(final Table table){

        if(prefix.isEmpty() && suffix.isEmpty())
            return;

        if (table != null && !(table instanceof View))
        {
            for (final ForeignKey foreignKey: table.getImportedForeignKeys())
            {
                for (final ForeignKeyColumnReference columnReference: foreignKey)
                {
                    final Column fkColumn = columnReference.getForeignKeyColumn();
                    if (fkColumn != null && !fkColumn.getName().startsWith(prefix))
                    {
                        addTableLint(table, "Foreign key does not start with prefix \"" + prefix +"\"", foreignKey);

                    }
                    if (fkColumn != null && !fkColumn.getName().endsWith(suffix))
                    {
                        addTableLint(table, "Foreign key does not ends with suffix \"" + suffix +"\"", foreignKey);
                    }
                }
            }
        }
    }

}
