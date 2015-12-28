package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized;
import org.apache.commons.io.IOUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;

/**
 * @author adriens
 */
public class LinterColumnContentNotNormalizedTest  {

    Logger logger = LoggerFactory.getLogger(LinterColumnContentNotNormalizedTest.class);
    private static PostgreSqlDatabase database;

    @BeforeClass
    public static void init() {
        database = new PostgreSqlDatabase();
        database.setUp(PostgreSqlDatabase.CHANGE_LOG_NORMALIZE_CHECK);
        System.out.println("LinterColumnContentNotNormalizedTest1 running...");
    }

    @Test
    public void testLint() throws Exception {

        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterColumnContentNotNormalized.class.getName()));

        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();

		// Set what details are required in the schema - this affects the
        // time taken to crawl the schema
        options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());

        //final Catalog catalog = getCatalog(options);
        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

        final Executable executable = new SchemaCrawlerExecutable("lint");
        final Path linterConfigsFile = FileSystems.getDefault().getPath("", this.getClass().getClassLoader().getResource("schemacrawler-linter-configs-test.xml").getPath());
        final LintOptionsBuilder optionsBuilder = new LintOptionsBuilder();
        optionsBuilder.withLinterConfigs(linterConfigsFile.toString());
        executable.setAdditionalConfiguration(optionsBuilder.toConfig());

        try {
            Path out = Paths.get("target/test_lint_normalized.json");
            OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json, out);
            outputOptions.setOutputFile(Paths.get("target/test_lint_normalized.json"));
            executable.setOutputOptions(outputOptions);

            executable.setSchemaCrawlerOptions(options);
            executable.execute(connection);

            File output = new File(out.toString());
            Assert.assertTrue("Lint json output must be generated.", output.exists());
            // now, only grab the lints i'm interested in (id : io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized)
            Assert.assertNotNull(IOUtils.toString(new FileInputStream(output)));
            System.out.println(out.toString().substring(1, out.toString().length() - 1));
            JSONObject json = new JSONObject(out.toString().substring(1, out.toString().length() - 1));
            Assert.assertNotNull(json.getJSONObject("table_lints"));

            JSONArray lints = json.getJSONObject("table_lints").getJSONArray("lints");
            // now we have the json array, let's filter only the one we want in our lint
            // lint id : [io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized]
            boolean lint1Dectected = false;
            for (int i = 0; i < lints.length(); i++) {
                // be sure we are on the right lint
                if (LinterColumnContentNotNormalized.class.getName().equals(lints.getJSONObject(i).getString("id"))) {
                    if ("public.test_normalized.content".equals(lints.getJSONObject(i).getString("value").trim())) {
                        Assert.assertEquals("Found <4> repetitions of the same value <AAAA> in <public.test_normalized.content>", lints.getJSONObject(i).getString("description").trim());
                        Assert.assertEquals("high", lints.getJSONObject(i).getString("severity").trim());
                        lint1Dectected = true;
                    }
                }
            }
            Assert.assertTrue("Normalization issue has been detected.", lint1Dectected);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testIsSqlTypeTextBased() {
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.BIGINT));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.BIT));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.BOOLEAN));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.CHAR));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.DATE));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.DECIMAL));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.DOUBLE));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.FLOAT));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.INTEGER));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.JAVA_OBJECT));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.LONGNVARCHAR));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.LONGVARBINARY));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.LONGVARCHAR));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.NCHAR));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.NULL));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.NUMERIC));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.NVARCHAR));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.OTHER));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.REAL));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.SMALLINT));
        Assert.assertFalse(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.TIME));
        Assert.assertTrue(LinterColumnContentNotNormalized.isSqlTypeTextBased(Types.VARCHAR));

    }

    @Test
    public void testMustColumnBeTested() {
        //text based but too short
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.VARCHAR, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE - 1));
        // Text based and good length
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.NVARCHAR, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        Assert.assertTrue(LinterColumnContentNotNormalized.mustColumnBeTested(Types.NCHAR, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE + 1));
        // not text based
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.BIT, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.BIT, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE + 1));
    }

    @Test
    public void testGetSummary() {
        LinterColumnContentNotNormalized test = new LinterColumnContentNotNormalized();
        Assert.assertEquals(" should not have so many duplicates.", test.getSummary());
    }
    
    @Test
    public void testGetDescription() {
        LinterColumnContentNotNormalized test = new LinterColumnContentNotNormalized();
        Assert.assertEquals(test.getDescription(), test.getSummary());
    }

}
