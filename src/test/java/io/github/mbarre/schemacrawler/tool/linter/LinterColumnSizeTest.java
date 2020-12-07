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
import java.sql.DriverManager;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import schemacrawler.schemacrawler.*;
import schemacrawler.tools.lint.LinterRegistry;


/**
 * Check columns with JSON content but not JSON or JSONB type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterColumnSizeTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_COLUMNSIZE_CHECK = "src/test/db/liquibase/LinterColumnSize/db.changelog.xml";
    private static PostgreSqlDatabase database;
    
    @BeforeClass
    public static void  init() {
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_COLUMNSIZE_CHECK);
    }
    
    @Test
    public void testLint_varchar() throws Exception{
        
        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterColumnSize.class.getName()));
        
        LimitOptionsBuilder limitOptionBuilder = LimitOptionsBuilder.builder().tableNamePattern("test_varchar");
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions().withLimitOptions(limitOptionBuilder.toOptions());

        Connection connection = DriverManager.getConnection(database.getConnectionString(),
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
        
        List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterColumnSize.class.getSimpleName(), options, connection);
        
        Assert.assertEquals(1, lints.size());
        int index = 0;
        Assert.assertEquals(LinterColumnSize.class.getName(), lints.get(index).getId());
        Assert.assertEquals("public.test_varchar.content_over", lints.get(index).getValue());
        Assert.assertEquals("Column is oversized (100 char.) regarding its content (max: 5 char.).", lints.get(index).getDescription());
        Assert.assertEquals("high", lints.get(index).getSeverity());
    }
    
    @Test
    public void testLint_getDescription () throws SchemaCrawlerException{
        final LinterRegistry registry = new LinterRegistry();
        LinterColumnSize linter = (LinterColumnSize)registry.newLinter(LinterColumnSize.class.getName());
        Assert.assertEquals("Column is oversized regarding its content", linter.getDescription());
    }
    
    @Test
    public void testLint_getSummary () throws SchemaCrawlerException{
        final LinterRegistry registry = new LinterRegistry();
        LinterColumnSize linter = (LinterColumnSize)registry.newLinter(LinterColumnSize.class.getName());
        Assert.assertEquals("oversized column", linter.getSummary());
    }
    
}
