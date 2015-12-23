/**
 * 
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterXmlContent;
import org.apache.commons.io.output.StringBuilderWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.executable.Executable;
import schemacrawler.tools.executable.SchemaCrawlerExecutable;
import schemacrawler.tools.lint.LinterRegistry;
import schemacrawler.tools.lint.executable.LintOptionsBuilder;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.TextOutputFormat;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;


/**
 * Check columns with JSON content but not JSON or JSONB type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterXmlContentTest {

	private static final String CHANGE_LOG_XML_CHECK = "src/test/db/liquibase/xmlContentCheck/db.changelog.xml";
	private Logger LOGGER = LoggerFactory.getLogger(LinterXmlContentTest.class);
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_XML_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterXmlContent.class.getName()));

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
		options.setTableNamePattern("test_xml");
		
		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING, 
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
		
		final Executable executable = new SchemaCrawlerExecutable("lint");
		final Path linterConfigsFile = FileSystems.getDefault().getPath("", this.getClass().getClassLoader().getResource("schemacrawler-linter-configs-test.xml").getPath());
		final LintOptionsBuilder optionsBuilder = new LintOptionsBuilder();
		optionsBuilder.withLinterConfigs(linterConfigsFile.toString());
		executable.setAdditionalConfiguration(optionsBuilder.toConfig());
		
		try (StringBuilderWriter out = new StringBuilderWriter()) {
			OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json,out);
			executable.setOutputOptions(outputOptions);
			executable.setSchemaCrawlerOptions(options);
			executable.execute(connection);

			Assert.assertNotNull(out.toString());
			JSONObject json = new JSONObject(out.toString().substring(1, out.toString().length()-1)) ;
			Assert.assertNotNull(json.getJSONObject("table_lints"));
			Assert.assertEquals("test_xml", json.getJSONObject("table_lints").getString("name"));

			JSONArray lints = json.getJSONObject("table_lints").getJSONArray("lints");
            Assert.assertNotNull(lints);
            Assert.assertEquals(1,lints.length());
			Assert.assertEquals(LinterXmlContent.class.getName(), lints.getJSONObject(0).getString("id"));
			Assert.assertEquals("public.test_xml.content", lints.getJSONObject(0).getString("value").trim());
			Assert.assertEquals("should be XML type.", lints.getJSONObject(0).getString("description").trim());
			Assert.assertEquals("high", lints.getJSONObject(0).getString("severity").trim());
		}

	}

}
