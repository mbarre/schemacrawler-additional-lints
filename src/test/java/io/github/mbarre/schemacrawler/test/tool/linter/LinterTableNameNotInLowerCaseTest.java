/**
 * 
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mbarre
 */
public class LinterTableNameNotInLowerCaseTest {
	
	Logger logger = LoggerFactory.getLogger(LinterTableNameNotInLowerCaseTest.class);
    private static PostgreSqlDatabase database;
	
	@BeforeClass
	public void init(){
		database = new PostgreSqlDatabase();
		database.setUp(PostgreSqlDatabase.CHANGE_LOG_LOWERCASE_CHECK);
	}
	
	@Test
	public void testLint(){
		logger.info("test!");
		Assert.assertTrue(true);
	}

}
