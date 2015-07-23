/**
 * 
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.Linter;
import schemacrawler.tools.lint.LinterRegistry;

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
	public void testLint() throws SchemaCrawlerException{
		
		final LinterRegistry registry = new LinterRegistry();
		Linter linter = registry.lookupLinter(LinterTableNameNotInLowerCase.class.getName());
		
		Assert.assertNotNull(linter);

	}

}
