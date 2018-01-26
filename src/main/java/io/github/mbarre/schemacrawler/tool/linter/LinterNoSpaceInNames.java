package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.utils.JSonUtils;
import io.github.mbarre.schemacrawler.utils.LintUtils;
import org.apache.commons.lang3.StringUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

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


    @Override
    protected void lint(Table table, Connection connection) throws SchemaCrawlerException {
        String sql;
        List<Column> columns = getColumns(table);
        String columnName;
        String tableName = table.getName().replaceAll("\"", "");
        if(tableName.contains(" ")) {
            addLint(table, getDescription(), table.getFullName());
        }

        for (Column column : columns) {
            if(column.getName().contains(" "))
                addLint(table, getDescription(), column.getFullName());
        }
    }

    @Override
    public String getSummary() {
        return "Space should not be used in table or column names";
    }
}
