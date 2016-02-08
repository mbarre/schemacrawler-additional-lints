/**
 *
 */
package io.github.mbarre.schemacrawler.test.utils;

import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe utilitaire pour l'initialisation de la base de test
 *
 * @author mbarre
 */
public class PostgreSqlDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSqlDatabase.class);
    
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/sc_lint_test";
    /* Leave to postgres as the default user as described on travis
    (http://docs.travis-ci.com/user/database-setup/)
    
    The default user for accessing the local PostgreSQL server is postgres
    and doesn’t have a password set up.
    
    */
    public static final String USER_NAME = "postgres";
    public static final String DEFAULT_PASSWORD = "";
    
    
    private Liquibase liquibase;
    private Properties properties;
    private String postgresPassword;
    
    public void setUp(String changelog) {
        try {
            setProperties();
            createTables(changelog);
            
        } catch (Exception ex) {
            LOG.error("Error during database initialization", ex);
            throw new RuntimeException("Error during database initialization", ex);
        }
    }
    
    public String getDbVersion(){
        setProperties();
        String version;
        try {
            Connection connection = DriverManager.getConnection(PostgreSqlDatabase.CONNECTION_STRING,
                    PostgreSqlDatabase.USER_NAME, getPostgresPassword());
            version = connection.getMetaData().getDatabaseProductVersion();
            connection.close();
        } catch (Exception ex) {
            LOG.error("Error during database initialization", ex);
            throw new RuntimeException("Error during database initialization", ex);
        }
        return version;
    }
    
    public void setProperties(){
        this.properties = new Properties();
        InputStream input;
        LOG.info("Looking for <test/resources/test.properties> file, with key <postgres.password> ...");
        try{
            
            input = getClass().getClassLoader().getResourceAsStream("test.properties");
            if(input == null){
                LOG.info("Not able to open <test/resources/test.properties> file,"
                        + " skipping and setting to default password");
                setPostgresPassword(DEFAULT_PASSWORD);
                return;
            }
            
            
            properties.load(input);
            LOG.info("Found <test/resources/test.properties> file");
            LOG.info("Found postgres password : <" + properties.getProperty("postgres.password") +">");
            if(properties.getProperty("postgres.password") != null){
                LOG.info("<postgres.password> found...");
                setPostgresPassword(properties.getProperty("postgres.password"));
            }
            else{
                LOG.info("<postgres.password> nof found : default empty value will be used.");
                setPostgresPassword(DEFAULT_PASSWORD);
            }
        }
        catch(FileNotFoundException ex){
            LOG.error("Could not find <test.properties> conf file... will use default", ex);
            this.properties = null;
            setPostgresPassword(DEFAULT_PASSWORD);
        }
        catch(IOException ex){
            LOG.error("Could not find <test.properties> conf file... will use default", ex);
            this.properties = null;
            setPostgresPassword(DEFAULT_PASSWORD);
        }
    }
    
    
    private Connection getConnectionImpl(String user, String password) throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, user, password);
    }
    
    // Create tables...
    private void createTables(String changelog) {
        
        Connection holdingConnection;
        try {
            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
            
            holdingConnection = getConnectionImpl(USER_NAME, getPostgresPassword());
            JdbcConnection conn = new JdbcConnection(holdingConnection);
            
            PostgresDatabase database = new PostgresDatabase();
            database.setDefaultSchemaName("public");
            database.setConnection(conn);
            
            liquibase = new Liquibase(changelog, resourceAccessor, database);
            liquibase.dropAll();
            liquibase.update("test");
            
            conn.close();
            
        } catch (SQLException | LiquibaseException ex) {
            LOG.error("Error during createTable step", ex);
            throw new RuntimeException("Error during createTable step", ex);
        }
        
    }
    
    /**
     * @return the postgresPassword
     */
    public String getPostgresPassword() {
        return postgresPassword;
    }
    
    /**
     * @param postgresPassword the postgresPassword to set
     */
    public void setPostgresPassword(String postgresPassword){
        this.postgresPassword = postgresPassword;
        LOG.info("PG password set to <" + postgresPassword + ">");
    }
    
}
