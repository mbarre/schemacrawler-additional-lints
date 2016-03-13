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

import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;

import static java.util.Objects.requireNonNull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Check that tables have primary key
 * @author mabrre
 */
public class LinterTableWithNoPrimaryKey extends BaseLinter {
    
    private static final Logger LOGGER = Logger.getLogger(LinterTableWithNoPrimaryKey.class.getName());
    
    /**
     * The lint
     */
    public LinterTableWithNoPrimaryKey(){
        setSeverity(LintSeverity.critical);
    }
    
    /**
     * Get lint description
     * @return lint description
     */
    @Override
    public String getDescription() {
        return getSummary();
    }
    
    /**
     * lint summary
     * @return lint summary
     */
    @Override
    public String getSummary() {
        return "Table should have a primary key";
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection
     */
    @Override
    protected void lint(final Table table, Connection connection){
        requireNonNull(table, "No table provided");
        LOGGER.log(Level.INFO, "Checking {0}....", table.getFullName());
        
        if(table.getPrimaryKey() == null){
            addLint(table, getDescription(), table.getFullName());
        }
    }
    
}
