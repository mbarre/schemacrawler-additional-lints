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

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author barmi83
 */
public class LinterColumnSize extends BaseLinter {
    private static final Logger LOGGER = Logger.getLogger(LinterColumnSize.class.getName());
    
    private static final int MIN_COLUMN_SIZE_PERCENT = 10;
    public static final String COLUMNSIZE_CONFIG_PARAM = "minColumnSizePercent";
    
    private double minColumnSizePercent;
    
    
    /**
     * The lint that parses and test numeric content
     *
     */
    public LinterColumnSize() {
        setSeverity(LintSeverity.high);
        minColumnSizePercent = MIN_COLUMN_SIZE_PERCENT;
    }
    
    /**
     * Get the description
     * @return the description
     */
    @Override
    public String getDescription()
    {
        return "Column is oversized regarding its content";
    }
    
    /**
     * Get lint Summary
     * @return lint Summary
     */
    @Override
    public String getSummary() {
        return "oversized column";
    }

    @Override
    public void configure(Config config) {
        minColumnSizePercent = config.getIntegerValue(COLUMNSIZE_CONFIG_PARAM, MIN_COLUMN_SIZE_PERCENT);
    }
    
    /**
     * The lint that does the job
     * @param table table
     * @param connection connection
     * @throws SchemaCrawlerException SchemaCrawlerException
     */
    @Override
    protected void lint(final Table table, final Connection connection)
            throws SchemaCrawlerException {

        LOGGER.log(Level.CONFIG, "<minColumnSizePercent> parameter set to {0}%", minColumnSizePercent);

        try (Statement stmt = connection.createStatement()){
            
            String sql;
            String columnName;
            String tableName = table.getName().replaceAll("\"", "");
            
            List<Column> columns = getColumns(table);
            for (Column column : columns) {
                
                if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType())
                        || LintUtils.isSqlTypeLargeTextBased(column.getColumnDataType())){
                    
                    LOGGER.log(Level.INFO,"Checking {0}...", column.getFullName());
                    columnName = column.getName().replaceAll("\"", "");
                    
                    sql = "select max(length(\"" + columnName + "\")) as max from \"" + tableName +"\"" ;
                    LOGGER.log(Level.CONFIG,"SQL : {0}", sql);
                    
                    try(ResultSet rs = stmt.executeQuery(sql)){
                        while (rs.next()) {
                            int maxLength = rs.getInt(1);
                            if(maxLength > 0 && (maxLength*100/column.getSize() < minColumnSizePercent)){
                                addLint(table, "Column is oversized ("+column.getSize()+" char.) regarding its content (max: "+maxLength+" char.).", column.getFullName());
                            }
                        }
                    }   catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, ex.getMessage());
                        throw new SchemaCrawlerException(ex.getMessage(), ex);
                    }
                }
            }
            
        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
    }
}
