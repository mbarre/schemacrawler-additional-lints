/**
 *
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterJsonTypeColumn;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;


/**
 * Check that columns with JSON content use data type jsonb
 * @author mbarre
 */
public class LinterJsonTypeColumnTest extends BaseLintTest {

    private static final String CHANGE_LOG_JSON_CHECK = "src/test/db/liquibase/jsonbCheck/db.changelog.xml";
    private Logger LOGGER = LoggerFactory.getLogger(LinterJsonTypeColumnTest.class);
    private static PostgreSqlDatabase database;

    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_JSON_CHECK);
    }

    @Test
    public void testLint() throws Exception{

        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterJsonTypeColumn.class.getName()));

        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        // Set what details are required in the schema - this affects the
        // time taken to crawl the schema
        options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        options.setTableNamePattern("test_json");

        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

        List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);

        Assert.assertEquals(1,lints.size());
        Assert.assertEquals(LinterJsonTypeColumn.class.getName(), lints.get(0).getId());
        Assert.assertEquals("content_json", lints.get(0).getValue());
        Assert.assertEquals("\"jsonb\" type should be used instead of \"json\" to store JSON data.", lints.get(0).getDescription());
        Assert.assertEquals("high", lints.get(0).getSeverity());
    }


}
