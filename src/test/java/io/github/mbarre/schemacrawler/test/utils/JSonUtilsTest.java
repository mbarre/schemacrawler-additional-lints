/**
 * 
 */
package io.github.mbarre.schemacrawler.test.utils;

import io.github.mbarre.schemacrawler.utils.JSonUtils;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author barmi83
 * @since 
 */
public class JSonUtilsTest {

	private Logger LOGGER = LoggerFactory.getLogger(JSonUtilsTest.class);

	
	@Test
	public void testUtils_success() throws Exception{

		String data = "{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": {\"menuitem\": [{\"value\": \"New\", "
				+ "\"onclick\": \"CreateNewDoc()\"},{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},"
				+ " {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}";
		
		Assert.assertTrue(JSonUtils.isJsonContent(data));

	}
	
	@Test
	public void testUtils_fails() throws Exception{

		String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
				+ " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure "
				+ "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non "
				+ "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		Assert.assertFalse(JSonUtils.isJsonContent(data));

	}
}
