package io.github.mbarre.schemacrawler.tool.linter;

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

        if (table != null && !(table instanceof View))
        {
            for (final ForeignKey foreignKey: table.getImportedForeignKeys())
            {
                for (final ForeignKeyColumnReference columnReference: foreignKey)
                {
                    final Column fkColumn = columnReference.getForeignKeyColumn();
                    if (!fkColumn.getName().startsWith(prefix))
                    {
                        addTableLint(table, "Foreign key does not start with prefix \"" + prefix +"\"", foreignKey);

                    }
                    if (!fkColumn.getName().endsWith(suffix))
                    {
                        addTableLint(table, "Foreign key does not ends with suffix \"" + suffix +"\"", foreignKey);
                    }
                }
            }
        }
    }

}
