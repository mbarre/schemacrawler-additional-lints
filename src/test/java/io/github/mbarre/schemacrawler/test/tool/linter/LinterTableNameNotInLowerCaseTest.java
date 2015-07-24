/**
 * 
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.io.output.StringBuilderWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.tools.executable.Executable;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.lint.Linter;
import schemacrawler.tools.lint.LinterRegistry;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.TextOutputFormat;

/**
 * @author mbarre
 */
public class LinterTableNameNotInLowerCaseTest {

	Logger logger = LoggerFactory.getLogger(LinterTableNameNotInLowerCaseTest.class);
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(PostgreSqlDatabase.CHANGE_LOG_LOWERCASE_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Linter linter = registry.lookupLinter(LinterTableNameNotInLowerCase.class.getName());

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevel.minimum());
		options.setTableNamePattern("TEST");

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING, 
				PostgreSqlDatabase.USER_NAME, PostgreSqlDatabase.PASSWORD);


		final Executable executable = new SchemaCrawlerExecutable("lint");
		try (StringBuilderWriter out = new StringBuilderWriter()) {
			OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json,out);
			executable.setOutputOptions(outputOptions);
			executable.setSchemaCrawlerOptions(options);
			executable.execute(connection);

			System.out.println("----");
			Assert.assertNotNull(out.toString());
			JSONObject json = new JSONObject(out.toString().substring(1, out.toString().length()-1)) ;
			Assert.assertNotNull(json.getJSONObject("table_lints"));
			Assert.assertEquals("TEST", json.getJSONObject("table_lints").getString("name"));

			JSONArray lints = json.getJSONObject("table_lints").getJSONArray("lints");

			boolean lintDectected = false;
			for (int i=0; i < lints.length(); i++) {
				if(LinterTableNameNotInLowerCase.class.getName().equals(lints.getJSONObject(i).getString("id"))){
					Assert.assertEquals("name should be in lower case", lints.getJSONObject(i).getString("description").trim());
					Assert.assertEquals("TEST", lints.getJSONObject(i).getString("value").trim());
					Assert.assertEquals("high", lints.getJSONObject(i).getString("severity").trim());
					lintDectected = true;
				}
			}

			Assert.assertTrue(lintDectected);
		}

	}

}
