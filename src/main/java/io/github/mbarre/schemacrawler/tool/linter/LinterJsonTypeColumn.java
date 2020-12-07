package io.github.mbarre.schemacrawler.tool.linter;

/*
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

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Linter to check if JSON type is used instead of JSONB - PostgreSQL reserved lint
 * @author mbarre
 * @since 1.0.1
 */
public class LinterJsonTypeColumn extends BaseLinter {
    private static final Logger LOGGER = Logger.getLogger(LinterJsonTypeColumn.class.getName());
    
    /**
     * The lint that test if the proper jsonb type has been used.
     *
     */
    public LinterJsonTypeColumn() {
        setSeverity(LintSeverity.high);
    }
    
    /**
     * Get the description
     * @return the description
     */
    @Override
    public String getDescription()
    {
        return "\"JSONB\" type should be used instead of \"JSON\" to store JSON data";
    }
    
    /**
     * Get the summaru
     * @return the summaru
     */
    @Override
    public String getSummary()
    {
        return "\"JSONB\" type should be used instead of \"JSON\"";
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     */
    @Override
    protected void lint(final Table table, Connection connection) throws SchemaCrawlerException
    {
        requireNonNull(table, "No table provided");
        
        try {
            if("PostgreSQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName()) &&
                    "9.4".compareTo(connection.getMetaData().getDatabaseProductVersion()) <= 0){
                List<String> names = findJsonTypeColumn(getColumns(table));
                for (String name : names) {
                    addTableLint(table, getDescription(), name);
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Unable to get database product name info. Lint will not be executed.");
            throw new SchemaCrawlerException(e.getMessage(), e);
        }
    }
    
    private List<String> findJsonTypeColumn(List<Column> columns){
        List<String> names = new ArrayList<>();
        for (Column column : columns) {
            LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
            if("json".equalsIgnoreCase(column.getColumnDataType().getDatabaseSpecificTypeName())){
                names.add(column.getName());
            }
        }
        return names;
    }
    
}
