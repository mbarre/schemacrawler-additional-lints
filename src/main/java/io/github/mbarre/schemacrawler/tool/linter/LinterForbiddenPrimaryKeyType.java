package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinterPrimaryKeyType extends BaseLinter
{
    private static final Logger LOGGER = Logger.getLogger(LinterPrimaryKeyType.class.getName());

    /**
     * The lint that parses and test primary keys
     *
     */
    public LinterPrimaryKeyType() {
        super();
        setSeverity(LintSeverity.high);
    }

    @Override
    protected void lint(Table table, Connection connection) throws SchemaCrawlerException {
        List<Column> columns = getColumns(table);
        for (Column column : columns) {
            if(column.isPartOfPrimaryKey()){
                LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
                if(LintUtils.isSqlTypeDateBased(column.getColumnDataType()))
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
