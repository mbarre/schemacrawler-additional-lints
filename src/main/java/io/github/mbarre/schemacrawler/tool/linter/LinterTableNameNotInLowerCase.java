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
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;


/**
 * Check that objects (tables, columns) have name in lower case
 * @author mbarre
 */
public class LinterTableNameNotInLowerCase extends BaseLinter
{
    private static final Logger LOGGER = Logger.getLogger(LinterTableNameNotInLowerCase.class.getName());
    
    
    /**
     * The lint
     */
    public LinterTableNameNotInLowerCase() {
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
    
    /**
     * Get the lint summary
     * @return the lint summary
     */
    @Override
    public String getSummary()
    {
        return "name should be in lower case";
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection the connection
     */
    @Override
    protected void lint(final Table table, final Connection connection)
    {
        requireNonNull(table, "No table provided");
        
        List<String> names = findColumnsWithUpperCase(getColumns(table));
        if (!isLowerCaseName(table.getName()))
        {
            names.add(0,table.getName());
        }
        
        for (String name : names) {
            addLint(table, getDescription(), name);
        }
    }
    
    private boolean isLowerCaseName(final String name)
    {
        return Objects.equals(name.toLowerCase(), name);
    }
    
    private List<String> findColumnsWithUpperCase(List<Column> columns){
        List<String> names = new ArrayList<>();
        for (Column column : columns) {
            LOGGER.log(Level.INFO, "Checking {0}...", column.getFullName());
            if(!isLowerCaseName(column.getName())){
                names.add(column.getName());
            }
        }
        return names;
    }
    
}
