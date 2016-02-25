/**
 *
 */
package io.github.mbarre.schemacrawler.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
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
public class LinterTableWithNoRemarkTest extends BaseLintTest {

	private static final String CHANGE_LOG_REMARK_CHECK = "src/test/db/liquibase/remarkCheck/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_REMARK_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterTableWithNoRemark.class.getName()));

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
		options.setTableNamePattern("test_remark");

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

		List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
		boolean lint1Detected = false;
		boolean lint2Detected = false;
		for (LintWrapper lint : lints) {
			if(LinterTableWithNoRemark.class.getName().equals(lint.getId())){
				if("test_remark".equals(lint.getValue())){
					Assert.assertEquals("should have a remark", lint.getDescription());
					Assert.assertEquals("low", lint.getSeverity());
					lint1Detected = true;
				}
				else if("column_without_remark".equals(lint.getValue())){
					Assert.assertEquals("should have a remark", lint.getDescription());
					Assert.assertEquals("low", lint.getSeverity());
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
