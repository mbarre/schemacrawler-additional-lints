package io.github.mbarre.schemacrawler.tool.linter;

import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;

import java.sql.Connection;

public class LinterNoSpaceInNames extends BaseLinter {

    @Override
    protected void lint(Table table, Connection connection) throws SchemaCrawlerException {

    }

    @Override
    public String getSummary() {
        return "No space in names (tables, columns)";
    }
}
