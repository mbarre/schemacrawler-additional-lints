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
import java.util.regex.Pattern;


/**
 * Check columns with boolean content but numeric type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterBooleanContentTest extends BaseLintTest {

	private static final String CHANGE_LOG_BOOLEAN_CHECK = "src/test/db/liquibase/booleanContentCheck/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_BOOLEAN_CHECK);
	}

	@Test
	public void testLint_int() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterBooleanContent.class.getName()));

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
		
		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING, 
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
		
		List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
		Pattern p = Pattern.compile("test_(.*)_boolean");
		Assert.assertEquals(8, lints.size());
		for (LintWrapper lint :	lints) {
			Assert.assertEquals(LinterBooleanContent.class.getName(), lint.getId());
			Assert.assertEquals("Should be boolean type.", lint.getDescription());
			Assert.assertEquals("high", lint.getSeverity());
			Assert.assertTrue(p.matcher(lint.getTableName()).matches());
		}
	}

}
