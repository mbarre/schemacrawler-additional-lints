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

import io.github.mbarre.schemacrawler.utils.JSonUtils;
import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.Config;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Linter to check if non JSONB type is used whereas JSON data is store in column
 * @author mbarre
 * @since 1.0.1
 */
public class LinterJsonContent extends BaseLinter {

    private static final Logger LOGGER = Logger.getLogger(LinterJsonContent.class.getName());

    public static final String SAMPLE_SIZE_PARAM = "sampleSize";

    private Integer sampleSize;

    /**
     * The lint that parses and test Json content
     */
    public LinterJsonContent () {
        sampleSize = 1000;
        setSeverity(LintSeverity.high);
    }

    @Override
    public void configure(Config config) {
        sampleSize = config.getIntegerValue(SAMPLE_SIZE_PARAM, 1000);
    }

    /**
     * Get lint descrption
     * @return lint description
     */
    @Override
    public String getDescription() {
        return getSummary();
    }

    /**
     * Get lint Summary
     * @return lint Summary
     */
    @Override
    public String getSummary() {
        return "should be JSON or JSONB type";
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

        try (Statement stmt = connection.createStatement()){
            if("PostgreSQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName()) &&
                    "9.4".compareTo(connection.getMetaData().getDatabaseProductVersion()) <= 0){

                LOGGER.log(Level.CONFIG, "<"+SAMPLE_SIZE_PARAM+"> parameter set to {0}%", sampleSize);

                String sql;
                List<Column> columns = getColumns(table);
                String columnName;
                String tableName = table.getName().replaceAll("\"", "");
                for (Column column : columns) {
                    if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getJavaSqlType())){
                        columnName = column.getName().replaceAll("\"", "");
                        LOGGER.log(Level.INFO, "Checking {0}...",columnName);



                        sql = "select \"" + columnName + "\" from \"" + tableName +"\" ORDER BY random()" ;
                        stmt.setMaxRows(sampleSize);
                        LOGGER.log(Level.CONFIG, "SQL : {0}", sql);

                        try(ResultSet rs = stmt.executeQuery(sql)){
                            boolean found = false;
                            while (rs.next() && !found) {
                                String data = rs.getString(columnName);

                                if(JSonUtils.isJsonContent(data)){
                                    addLint(table, getDescription(), column.getFullName());
                                    found = true;
                                }
                            }
                        }catch (SQLException ex) {
                            LOGGER.severe(ex.getMessage());
                            throw new SchemaCrawlerException(ex.getMessage(), ex);
                        }
                    }
                }
            }

        }catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new SchemaCrawlerException(ex.getMessage(), ex);
        }
    }
}
