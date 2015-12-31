package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized;
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
import java.sql.Types;
import java.util.List;

/**
 * @author adriens
 */
public class LinterColumnContentNotNormalizedTest  extends BaseLintTest {

    Logger logger = LoggerFactory.getLogger(LinterColumnContentNotNormalizedTest.class);
    private static PostgreSqlDatabase database;
    private static final String CHANGE_LOG_NORMALIZE_CHECK = "src/test/db/liquibase/normalizeCheck/db.changelog.xml";


    @BeforeClass
    public static void init() {
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_NORMALIZE_CHECK);
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

        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

        List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);

        // now we have the json array, let's filter only the one we want in our lint
        // lint id : [io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized]
        boolean lint1Dectected = false;
        for (LintWrapper lint : lints) {
            // be sure we are on the right lint
            if (LinterColumnContentNotNormalized.class.getName().equals(lint.getId())) {
                if ("public.test_normalized.content".equals(lint.getValue())) {
                    Assert.assertEquals("Found <4> repetitions of the same value <AAAA> in <public.test_normalized.content>", lint.getDescription());
                    Assert.assertEquals("high", lint.getSeverity());
                    lint1Dectected = true;
                }
            }
        }
        Assert.assertTrue("Normalization issue has been detected.", lint1Dectected);

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
