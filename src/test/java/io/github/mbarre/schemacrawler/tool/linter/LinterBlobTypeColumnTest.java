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

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import schemacrawler.schemacrawler.*;
import schemacrawler.tools.lint.LinterRegistry;

/**
 *
 * @author barmi83
 */
public class LinterBlobTypeColumnTest extends BaseLintTest {

    private static final String CHANGE_LOG_BLOB_CHECK = "src/test/db/liquibase/LinterBlobTypeColumn/db.changelog.xml";
    private static PostgreSqlDatabase database;

    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_BLOB_CHECK);
    }

    @Test
    public void testLint() throws Exception{

        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterBlobTypeColumn.class.getName()));

        //final SchemaCrawlerOptionsBuilder optionsBuilder = SchemaCrawlerOptionsBuilder.builder();
        LimitOptionsBuilder limitOptionBuilder = LimitOptionsBuilder.builder().tableNamePattern("test_blob");
        SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions().withLimitOptions(limitOptionBuilder.toOptions());

        Connection connection = DriverManager.getConnection(database.getConnectionString(),
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

        List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterBlobTypeColumn.class.getSimpleName(), options, connection);

        Assert.assertEquals(1,lints.size());
        boolean lint1Dectected = false;
        boolean lint2Dectected = false;
        for (LintWrapper lint : lints) {
            // be sure we are on the right lint
            if (Objects.equals(LinterBlobTypeColumn.class.getName(), lint.getId())) {
                if (Objects.equals("public.test_blob.content_blob", lint.getValue())) {
                    Assert.assertEquals("public.test_blob.content_blob", lint.getValue());
                    Assert.assertEquals("BLOB should not be used", lint.getDescription());
                    Assert.assertEquals("critical", lint.getSeverity());
                    lint1Dectected = true;
                }
            } else if (Objects.equals(LinterByteaTypeColumn.class.getName(), lint.getId())){
                if (Objects.equals("public.test_blob.content_blob", lint.getValue())) {
                    Assert.assertEquals("OID should be used instead of BYTEA", lint.getDescription());
                    Assert.assertEquals("high", lint.getSeverity());
                    lint2Dectected = true;
                }
            }
            else if (Objects.equals(LinterByteaTypeColumn.class.getName(), lint.getId())){
                //ignore
            }
        }
        Assert.assertTrue(lint1Dectected || lint2Dectected);
    }
}
