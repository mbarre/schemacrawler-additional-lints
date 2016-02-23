/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterBlobTypeColumn;
import io.github.mbarre.schemacrawler.tool.linter.LinterClobTypeColumn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

/**
 *
 * @author barmi83
 */
public class LinterClobTypeColumnTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_CLOB_CHECK = "src/test/db/liquibase/clobCheck/db.changelog.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(LinterClobTypeColumnTest.class);
    private static PostgreSqlDatabase database;
    
    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_CLOB_CHECK);
    }
    
    @Test
    public void testLint() throws Exception{
            
            final LinterRegistry registry = new LinterRegistry();
            Assert.assertTrue(registry.hasLinter(LinterBlobTypeColumn.class.getName()));
            
            final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            // Set what details are required in the schema - this affects the
            // time taken to crawl the schema
            options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
            options.setTableNamePattern("test_clob");
            
            Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                    PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
            
            List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
            
            Assert.assertEquals(1,lints.size());
            Assert.assertEquals(LinterClobTypeColumn.class.getName(), lints.get(0).getId());
            Assert.assertEquals("content_clob", lints.get(0).getValue());
            Assert.assertEquals("CLOB/TEXT should not be used.", lints.get(0).getDescription());
            Assert.assertEquals("high", lints.get(0).getSeverity());
        }
}
