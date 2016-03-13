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
import java.sql.SQLException;
import java.util.List;


/**
 * Check columns with JSON content but not JSON or JSONB type
 * @author mbarre
 * @since 1.0.1
 */
public class LinterForeignKeyMismatchLazyTest extends BaseLintTest {
    
    private static final String CHANGE_LOG_FK_MISMATCH_CHECK = "src/test/db/liquibase/primaryKeyMismatchCheck/db.changelog.xml";
    private static PostgreSqlDatabase database;
    
    @BeforeClass
    public static void  init() throws SQLException{
        database = new PostgreSqlDatabase();
        database.setUp(CHANGE_LOG_FK_MISMATCH_CHECK);
    }
    
    @Test
    public void testLint() throws Exception{
        
        final LinterRegistry registry = new LinterRegistry();
        Assert.assertTrue(registry.hasLinter(LinterForeignKeyMismatchLazy.class.getName()));
        
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        
        Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                PostgreSqlDatabase.USER_NAME, database.getPostgresPassword());
        
        List<LintWrapper> lints = executeToJsonAndConvertToLintList(options, connection);
        Assert.assertEquals(1,lints.size());
        Assert.assertEquals(LinterForeignKeyMismatchLazy.class.getName(), lints.get(0).getId());
        Assert.assertEquals("fk_test_2", lints.get(0).getValue());
        Assert.assertEquals("Foreign key data type does not match Primary key.", lints.get(0).getDescription());
        Assert.assertEquals("critical", lints.get(0).getSeverity());
       
    }
    
   
    
}
