package io.github.mbarre.schemacrawler.tool.linter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

public class LinterRightSpacePaddingTest  extends BaseLintTest {

	private static final String CHANGE_LOG_RIGHT_PADDING_CHECK = "src/test/db/liquibase/LinterRightSpacePadding/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_RIGHT_PADDING_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterRightSpacePadding.class.getName()));

		final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.builder().withSchemaInfoLevel(SchemaInfoLevelBuilder.standard()).toOptions();

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

		List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterRightSpacePadding.class.getSimpleName(), options, connection);
		Assert.assertEquals(6, lints.size());

		Assert.assertTrue(contains(lints, "public.test_char.char_right_padding"));
		Assert.assertTrue(contains(lints, "public.test_longnvarchar.longnvarchar_right_padding"));
		Assert.assertTrue(contains(lints, "public.test_longvarchar.longvarchar_right_padding"));
		Assert.assertTrue(contains(lints, "public.test_nchar.nchar_right_padding"));
		Assert.assertTrue(contains(lints, "public.test_nvarchar.nvarchar_right_padding"));
		Assert.assertTrue(contains(lints, "public.test_varchar.varchar_right_padding"));
	}

	private boolean contains(List<LintWrapper> lints, String columnName){
		return lints.stream().anyMatch(lint -> Objects.equals(lint.getValue(), columnName));
	}
}
