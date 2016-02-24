 /**
  *
  */
package io.github.mbarre.schemacrawler.test.tool.linter;

import io.github.mbarre.schemacrawler.test.utils.LintWrapper;
import io.github.mbarre.schemacrawler.test.utils.PostgreSqlDatabase;
import io.github.mbarre.schemacrawler.tool.linter.LinterColumnSize;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.tools.lint.LinterRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


/**
 * Check columns with JSON content but not JSON or JSONB type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterColumnSizeTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_COLUMNSIZE_CHECK = "src/test/db/liquibase/columnSizeCheck/db.changelog.xml";
    private static PostgreSqlDatabase database;
    
    @BeforeClass
    public static void  init() throws SQLException{
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_COLUMNSIZE_CHECK);
    }
    
    @Test
    public void testLint_varchar() throws Exception{
        
        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterColumnSize.class.getName()));
        
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        options.setTableNamePattern("test_varchar");
        
        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
        
        List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
        
        Assert.assertEquals(1, lints.size());
        Assert.assertEquals(LinterColumnSize.class.getName(), lints.get(0).getId());
        Assert.assertEquals("public.test_varchar.content_over", lints.get(0).getValue());
        Assert.assertEquals("column is oversized regarding its content.", lints.get(0).getDescription());
        Assert.assertEquals("high", lints.get(0).getSeverity());
    }
    
//    @Test
//    public void testLint_clob() throws Exception{
//        
//            final LinterRegistry registry = new LinterRegistry();
//            Assert.assertTrue(registry.hasLinter(LinterColumnSize.class.getName()));
//            
//            final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
//            options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
//            options.setTableNamePattern("test_clob");
//            
//            Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
//                    PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
//            
//            List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
//            
//            Assert.assertEquals(1, lints.size());
//            Assert.assertEquals(LinterColumnSize.class.getName(), lints.get(0).getId());
//            Assert.assertEquals("public.test_clob.content_over", lints.get(0).getValue());
//            Assert.assertEquals("column is oversized regarding its content.", lints.get(0).getDescription());
//            Assert.assertEquals("high", lints.get(0).getSeverity());
//        }
    
    
    
}
