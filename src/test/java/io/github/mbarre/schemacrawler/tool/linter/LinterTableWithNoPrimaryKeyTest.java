 /**
  *
  */
package io.github.mbarre.schemacrawler.tool.linter;

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
import java.util.List;

/**
 * @author mbarre
 */
public class LinterTableWithNoPrimaryKeyTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_PRIMARY_KEY_CHECK = "src/test/db/liquibase/primaryKeyCheck/db.changelog.xml";
    private static PostgreSqlDatabase database;
    
    @BeforeClass
    public static void  init(){
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_PRIMARY_KEY_CHECK);
    }
    
    @Test
    public void testLint() throws Exception{
        
        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterTableWithNoPrimaryKey.class.getName()));
        
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        options.setTableNamePattern("test_primary_key");
        
        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
        
        List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
        boolean lintDetected = false;
        for (LintWrapper lint : lints) {
            if(LinterTableWithNoPrimaryKey.class.getName().equals(lint.getId())){
                if(lint.getValue().contains("test_primary_key")){
                    Assert.assertEquals("Table should have a primary key", lint.getDescription());
                    Assert.assertEquals("critical", lint.getSeverity());
                    lintDetected = true;
                }
                else{
                    Assert.fail("Not expected error detected :"+lint.getValue());
                }
            }
        }
        
        Assert.assertTrue("Some expected errors have not been detected.", lintDetected);
    }
}
