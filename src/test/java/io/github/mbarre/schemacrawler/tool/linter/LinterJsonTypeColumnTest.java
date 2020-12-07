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
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;


/**
 * Check that columns with JSON content use data type jsonb
 * @author mbarre
 */
public class LinterJsonTypeColumnTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_JSON_CHECK = "src/test/db/liquibase/LinterJsonTypeColumn/db.changelog.xml";
    private static PostgreSqlDatabase database;
    private static boolean jsonbSupport = false;
    
    
    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        if("9.4".compareTo(database.getDbVersion()) <= 0){
            jsonbSupport = true;
            database.setUp(CHANGE_LOG_JSON_CHECK);
        }
    }
    
    @Test
    public void testLint() throws Exception{
        
        if(jsonbSupport){
            
            final LinterRegistry registry = new LinterRegistry();
            Assert.assertTrue(registry.hasLinter(LinterJsonTypeColumn.class.getName()));
            
            final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions();

            Connection connection = DriverManager.getConnection(database.getConnectionString(),
                    PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
            
            List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterJsonTypeColumn.class.getSimpleName(), options, connection);
            
            Assert.assertEquals(1,lints.size());
            int index = 0;
            Assert.assertEquals(LinterJsonTypeColumn.class.getName(), lints.get(index).getId());
            Assert.assertEquals("content_json", lints.get(index).getValue());
            Assert.assertEquals("\"JSONB\" type should be used instead of \"JSON\" to store JSON data", lints.get(index).getDescription());
            Assert.assertEquals("high", lints.get(index).getSeverity());
        }
    }
    
    
}
