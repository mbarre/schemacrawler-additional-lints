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

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.JavaSqlType;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by barmi83 on 3/10/17.
 */
public class LinterByteaTypeColumn  extends BaseLinter {
    private static final Logger LOGGER = Logger.getLogger(LinterByteaTypeColumn.class.getName());

    public LinterByteaTypeColumn() {
        super();
        setSeverity(LintSeverity.high);
    }

    /**
     * Get lint descrption
     * @return lint description
     */
    @Override
    public String getDescription() {
        return "OID should be used instead of BYTEA";
    }

    /**
     * Get lint Summary
     * @return lint Summary
     */
    @Override
    public String getSummary() {
        return "BYTEA should not be used";
    }

    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     */
    @Override
    protected void lint(final Table table, final Connection connection) {
        List<Column> columns = getColumns(table);
        for (Column column : columns) {
            LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName() + " - " + column.getColumnDataType().getDatabaseSpecificTypeName());
            if(Objects.equals("bytea", column.getColumnDataType().getDatabaseSpecificTypeName())){
                addLint(table, getDescription(), column.getFullName());
            }
        }
    }
}
