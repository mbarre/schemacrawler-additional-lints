package io.github.mbarre.schemacrawler.tool.linter;

/*-
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2018 github
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
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

public class LinterLeftSpacePaddingTest  extends BaseLintTest {

	private static final String CHANGE_LOG_LEFT_PADDING_CHECK = "src/test/db/liquibase/LinterLeftSpacePadding/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_LEFT_PADDING_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterLeftSpacePadding.class.getName()));

		final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.builder().toOptions();
		//fixme
//		final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.builder().withSchemaInfoLevel(SchemaInfoLevelBuilder.standard()).toOptions();

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

		List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterLeftSpacePadding.class.getSimpleName(), options, connection);
		Assert.assertEquals(6, lints.size());

		Assert.assertTrue(contains(lints, "public.test_char.char_left_padding"));
		Assert.assertTrue(contains(lints, "public.test_longnvarchar.longnvarchar_left_padding"));
		Assert.assertTrue(contains(lints, "public.test_longvarchar.longvarchar_left_padding"));
		Assert.assertTrue(contains(lints, "public.test_nchar.nchar_left_padding"));
		Assert.assertTrue(contains(lints, "public.test_nvarchar.nvarchar_left_padding"));
		Assert.assertTrue(contains(lints, "public.test_varchar.varchar_left_padding"));
	}

	private boolean contains(List<LintWrapper> lints, String columnName){
		return lints.stream().anyMatch(lint -> Objects.equals(lint.getValue(), columnName));
	}
}
