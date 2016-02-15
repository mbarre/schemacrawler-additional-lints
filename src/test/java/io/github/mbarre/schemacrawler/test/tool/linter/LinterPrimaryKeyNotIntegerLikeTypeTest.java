/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterPrimaryKeyNotIntegerLikeType;
import io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

/**
 *
 * @author barmi83
 */
public class LinterPrimaryKeyNotIntegerLikeTypeTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_PKTYPE_CHECK = "src/test/db/liquibase/primaryKeyTypeCheck/db.changelog.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(LinterPrimaryKeyNotIntegerLikeTypeTest.class);
    private static PostgreSqlDatabase database;
    
    
    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_PKTYPE_CHECK);
    }
    
    @Test
    public void testLint() throws Exception {
        final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterPrimaryKeyNotIntegerLikeType.class.getName()));

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
		//options.setTableNamePattern("\"TEST_CASE\"");

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

		List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);

		boolean lint1Detected = false;
		boolean lint2Detected = false;

		for (LintWrapper lint : lints) {
			if(LinterPrimaryKeyNotIntegerLikeType.class.getName().equals(lint.getId())){
				if("public.test_pk_char.id".equals(lint.getValue())){
					Assert.assertEquals("should be Integer like type or eventually char(1).", lint.getDescription());
					Assert.assertEquals("high", lint.getSeverity());
					lint1Detected = true;
				}
				else if("public.test_pk_timestamp.id".equals(lint.getValue())){
					Assert.assertEquals("should be Integer like type or eventually char(1).", lint.getDescription());
					Assert.assertEquals("high", lint.getSeverity());
					lint2Detected = true;
				}
				else{
					Assert.fail("Not expected error detected :"+lint.getValue());
				}
			}
		}

		Assert.assertTrue("Some expected errors have not been detected.", lint1Detected && lint2Detected);
    }
    
}
