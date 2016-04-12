package io.github.mbarre.schemacrawler.tool.linter;

/*
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2016 github
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
import java.sql.Types;
import java.util.List;

/**
 * @author adriens
 */
public class LinterColumnContentNotNormalizedTest  extends BaseLintTest {

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
                    Assert.assertEquals("<4> repetitions of the same value <AAAA>", lint.getDescription());
                    Assert.assertEquals("high", lint.getSeverity());
                    lint1Dectected = true;
                }
            }
        }
        Assert.assertTrue("Normalization issue has been detected.", lint1Dectected);

    }

    @Test
    public void testMustColumnBeTested() {
        //text based but too short
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.VARCHAR, 
                LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE - 1, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        // Text based and good length
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.NVARCHAR, 
                LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        Assert.assertTrue(LinterColumnContentNotNormalized.mustColumnBeTested(Types.NCHAR, 
                LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE + 1, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        // not text based
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.BIT, 
                LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
        Assert.assertFalse(LinterColumnContentNotNormalized.mustColumnBeTested(Types.BIT, 
                LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE + 1, LinterColumnContentNotNormalized.MIN_TEXT_COLUMN_SIZE));
    }

    @Test
    public void testGetSummary() {
        LinterColumnContentNotNormalized test = new LinterColumnContentNotNormalized();
        Assert.assertEquals("too much duplicates", test.getSummary());
    }

    @Test
    public void testGetDescription() {
        LinterColumnContentNotNormalized test = new LinterColumnContentNotNormalized();
        Assert.assertEquals(test.getSummary(), test.getDescription());
    }

}
