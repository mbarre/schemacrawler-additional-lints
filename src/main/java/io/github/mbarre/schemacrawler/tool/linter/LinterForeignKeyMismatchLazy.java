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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import schemacrawler.schema.Column;
import schemacrawler.schema.ForeignKey;
import schemacrawler.schema.ForeignKeyColumnReference;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 * Check that foreign key has the same type as the associated primary key, using the Java sql type instead of jdbc type.
 * @see schemacrawler.tools.linter.LinterForeignKeyMismatch
 * @author Sualeh Fatehi, mbarre
 */
public class LinterForeignKeyMismatchLazy extends BaseLinter {
    
    public LinterForeignKeyMismatchLazy() {
        setSeverity(LintSeverity.critical);
    }
    
    /**
     * Get the description
     * @return the description
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
        return "Foreign key data type does not match Primary key.";
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
        
        requireNonNull(table, "No table provided");
        List<ForeignKey> mismatchedForeignKeys = findMismatchedForeignKeys(table);
        for (final ForeignKey foreignKey: mismatchedForeignKeys)
        {
            addTableLint(table, getSummary(), foreignKey);
        }
    }
    
    private List<ForeignKey> findMismatchedForeignKeys(final Table table){
        final List<ForeignKey> mismatchedForeignKeys = new ArrayList<>();
        if (table != null && !(table instanceof View))
        {
            for (final ForeignKey foreignKey: table.getImportedForeignKeys())
            {
                for (final ForeignKeyColumnReference columnReference: foreignKey)
                {
                    final Column pkColumn = columnReference.getPrimaryKeyColumn();
                    final Column fkColumn = columnReference.getForeignKeyColumn();
                    if (!pkColumn.getColumnDataType().getJavaSqlType().getJavaSqlTypeName().equals(fkColumn.getColumnDataType().getJavaSqlType().getJavaSqlTypeName())
                            || pkColumn.getSize() != fkColumn.getSize())
                    {
                        mismatchedForeignKeys.add(foreignKey);
                        break;
                    }
                }
            }
        }
        return mismatchedForeignKeys;
    }
    
}
