package io.github.mbarre.schemacrawler.test.utils;



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

import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire pour l'initialisation de la base de test
 *
 * @author mbarre
 */
public class PostgreSqlDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSqlDatabase.class);
    
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost";
    public static final String DB_NAME = "sc_lint_test";
    /* Leave to postgres as the default user as described on travis
    (http://docs.travis-ci.com/user/database-setup/)
    
    The default user for accessing the local PostgreSQL server is postgres
    and doesn’t have a password set up.
    
    */
    public static final String USER_NAME = "postgres";
    public static final String DEFAULT_PASSWORD = "";
    public static final String DEFAULT_PORT = "5432";
    
    
    private Liquibase liquibase;
    private Properties properties;
    private String postgresPassword = "";
    private String postgresPort = "";
    private String connectionString = "";
    
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
            Connection connection = DriverManager.getConnection(getConnectionString(),
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
        LOG.info("Looking for <test/resources/test.properties> file...");
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

            if(properties.getProperty("postgres.password") != null){
                LOG.info("<postgres.password> found...");
                LOG.info("Found postgres password : <" + properties.getProperty("postgres.password") +">");
                setPostgresPassword(properties.getProperty("postgres.password"));
            }
            else{
                LOG.info("<postgres.password> not found : default empty value will be used.");
                setPostgresPassword(DEFAULT_PASSWORD);
            }

            if(properties.getProperty("postgres.port") != null){
                LOG.info("<postgres.port> found...");
                LOG.info("Found postgres port : <" + properties.getProperty("postgres.port") +">");
                setPostgresPort(properties.getProperty("postgres.port"));
            }
            else{
                LOG.info("<postgres.port> not found : default value will be used.");
                setPostgresPort(DEFAULT_PASSWORD);
            }
        }
        catch(IOException ex){
            LOG.error("Could not find <test.properties> conf file... will use default", ex);
            this.properties = null;
            setPostgresPassword(DEFAULT_PASSWORD);
            setPostgresPort(DEFAULT_PASSWORD);
        }

        setConnectionString(CONNECTION_STRING + (getPostgresPort().isEmpty() ? "" : (":" + getPostgresPort())) + "/" + DB_NAME);
    }
    
    
    private Connection getConnectionImpl(String user, String password) throws SQLException {
        LOG.info("Try to connect with user " + user +", pwd : '"+ password + "' to " + getConnectionString());
        return DriverManager.getConnection(getConnectionString(), user, password);
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

    public String getPostgresPort() {
        return postgresPort;
    }

    public void setPostgresPort(String postgresPort) {
        this.postgresPort = postgresPort;
        LOG.info("PG port set to <" + postgresPort + ">");
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
