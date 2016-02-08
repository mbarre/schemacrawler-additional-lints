/**
 *
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * @author mbarre
 */
public class LinterTableNameNotInLowerCaseTest extends BaseLintTest {

	private static final String CHANGE_LOG_LOWERCASE_CHECK = "src/test/db/liquibase/lowerCaseCheck/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_LOWERCASE_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterTableNameNotInLowerCase.class.getName()));

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
			if(LinterTableNameNotInLowerCase.class.getName().equals(lint.getId())){
				if("TEST_CASE".equals(lint.getValue())){
					Assert.assertEquals("name should be in lower case", lint.getDescription());
					Assert.assertEquals("high", lint.getSeverity());
					lint1Detected = true;
				}
				else if("UPPERCASE_COLUMN_NAME".equals(lint.getValue())){
					Assert.assertEquals("name should be in lower case", lint.getDescription());
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

//	}

}
