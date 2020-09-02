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
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;


/**
 * Check columns with boolean content but numeric type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterBooleanContentTest extends BaseLintTest {

	private static final String CHANGE_LOG_BOOLEAN_CHECK = "src/test/db/liquibase/LinterBooleanContentColumn/db.changelog.xml";
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(CHANGE_LOG_BOOLEAN_CHECK);
	}

	@Test
	public void testLint_int() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Assert.assertTrue(registry.hasLinter(LinterBooleanContent.class.getName()));

		final SchemaCrawlerOptions options = SchemaCrawlerOptionsBuilder.builder().toOptions();

		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());

		List<LintWrapper> lints = executeToJsonAndConvertToLintList(LinterBooleanContent.class.getSimpleName(), options, connection);
		Assert.assertEquals(8, lints.size());
		Assert.assertTrue(contains(lints, "public.test_bigint_boolean.bigint_boolean"));
		Assert.assertTrue(contains(lints, "public.test_double_boolean.double_boolean"));
		Assert.assertTrue(contains(lints, "public.test_float_boolean.float_boolean"));
		Assert.assertTrue(contains(lints, "public.test_int_boolean.int_boolean"));
		Assert.assertTrue(contains(lints, "public.test_integer_boolean.integer_boolean"));
		Assert.assertTrue(contains(lints, "public.test_number_boolean.number_boolean"));
		Assert.assertTrue(contains(lints, "public.test_smallint_boolean.smallint_boolean"));
		Assert.assertTrue(contains(lints, "public.test_tinyint_boolean.tinyint_boolean"));

	}

	private boolean contains(List<LintWrapper> lints, String columnName){
		return lints.stream().anyMatch(lint -> Objects.equals(lint.getValue(), columnName));
	}

}
