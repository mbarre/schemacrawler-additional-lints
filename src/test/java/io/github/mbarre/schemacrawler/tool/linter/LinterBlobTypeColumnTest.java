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
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

/**
 *
 * @author barmi83
 */
public class LinterBlobTypeColumnTest extends BaseLintTest {

    private static final String CHANGE_LOG_BLOB_CHECK = "src/test/db/liquibase/blobCheck/db.changelog.xml";
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

            final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
            // Set what details are required in the schema - this affects the
            // time taken to crawl the schema
            options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
            options.setTableNamePattern("test_blob");

            Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                    PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

            List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);

            Assert.assertEquals(1,lints.size());
            Assert.assertEquals(LinterBlobTypeColumn.class.getName(), lints.get(0).getId());
            Assert.assertEquals("public.test_blob.content_blob", lints.get(0).getValue());
            Assert.assertEquals("BLOB should not be used", lints.get(0).getDescription());
            Assert.assertEquals("critical", lints.get(0).getSeverity());
        }
}
