/**
 * 
 */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.apache.commons.io.output.StringBuilderWriter;
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
import schemacrawler.tools.lint.Linter;
import schemacrawler.tools.lint.LinterRegistry;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.options.TextOutputFormat;

/**
 * @author adriens
 */

/*
add a dependency to the SchemaCrawler SQLite project. Then I would create
a SQLite database (file) with the problem your lint is trying to find. Then
you can use SchemaCrawler to load the metadata for the SQLite database,
and run your custom lint against it to validate. Please also take a look at
the SchemaCrawler lint unit tests for examples. Thanks.

*/
public class LinterColumnContentNotNormalizedTest {

	Logger logger = LoggerFactory.getLogger(LinterColumnContentNotNormalizedTest.class);
	private static PostgreSqlDatabase database;

	@BeforeClass
	public static void  init(){
		database = new PostgreSqlDatabase();
		database.setUp(PostgreSqlDatabase.CHANGE_LOG_NORMALIZE_CHECK);
	}

	@Test
	public void testLint() throws Exception{

		final LinterRegistry registry = new LinterRegistry();
		Linter linter = registry.lookupLinter(LinterColumnContentNotNormalized.class.getName());
		Assert.assertNotNull(linter);
                
                

		final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
                
		// Set what details are required in the schema - this affects the
		// time taken to crawl the schema
		options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
                
                
                //final Catalog catalog = getCatalog(options);
                
		
		Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING, 
				PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
                
                
		
		final Executable executable = new SchemaCrawlerExecutable("lint");
                try{
                    Path out = Paths.get("target/test_lint_normalized.json");
                    OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json, out);
                        outputOptions.setOutputFile(Paths.get("target/test_lint_normalized.json"));
			executable.setOutputOptions(outputOptions);
                        
			executable.setSchemaCrawlerOptions(options);
			executable.execute(connection);
                        
                        //Assert.assertNotNull(out.g);
                        File output = new File(out.toString());
                        Assert.assertTrue("Lint json output must be generated.", output.exists());
                        // now, only grab the lints i'm interested in (id : io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized)
                        Assert.assertNotNull(IOUtils.toString(new FileInputStream(output)));
                        JSONObject json = new JSONObject(out.toString().substring(1, out.toString().length()-1)) ;
                        Assert.assertNotNull(json.getJSONObject("table_lints"));
                        
                        JSONArray lints = json.getJSONObject("table_lints").getJSONArray("lints");
                        // now we have the json array, let's filter only the one we want in our lint
                        // lint id : [io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized]
                        boolean lint1Dectected = false;
                        for (int i=0; i < lints.length(); i++) {
                            // be sure we are on the right lint
                            if(LinterColumnContentNotNormalized.class.getName().equals(lints.getJSONObject(i).getString("id"))){
                                if("public.test_normalized.content".equals(lints.getJSONObject(i).getString("value").trim())){
						Assert.assertEquals("Found <4> repetitions of the same value <AAAA> in <public.test_normalized.content>", lints.getJSONObject(i).getString("description").trim());
						Assert.assertEquals("high", lints.getJSONObject(i).getString("severity").trim());
						lint1Dectected = true;
					}
                            }
                        }
                        Assert.assertTrue("Normalization issue has been detected.", lint1Dectected);
                        
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                       
		/*try (Path out = new StringBuilderWriter()) {
			OutputOptions outputOptions = new OutputOptions(TextOutputFormat.json,);
                        outputOptions.setOutputFile(Paths.get("target/test_lint_normalized.json"));
			executable.setOutputOptions(outputOptions);
                        
			executable.setSchemaCrawlerOptions(options);
			executable.execute(connection);
                        
                        Assert.assertNotNull(out.toString());
			JSONObject json = new JSONObject(out.toString().substring(1, out.toString().length()-1)) ;
			Assert.assertNotNull(json.getJSONObject("table_lints"));
		}*/

	}

}
