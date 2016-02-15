/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.utils;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Types;

/**
 *
 * @author salad74
 */
public class LintUtilsTest {
    
    public LintUtilsTest() {
        LintUtils test = new LintUtils();
        Assert.assertTrue(true);
    }
    


    /**
     * Test of isSqlTypeTextBased method, of class LintUtils.
     */
    @Test
    public void testIsSqlTypeTextBased() {
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.NVARCHAR));
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.LONGNVARCHAR));
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.LONGVARCHAR));
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.CHAR));
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.NCHAR));
        Assert.assertTrue(LintUtils.isSqlTypeTextBased(Types.VARCHAR));

        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.BIGINT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.BINARY));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.BIT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.ARRAY));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.BLOB));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.BOOLEAN));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.CLOB));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.DATALINK));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.DATE));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.DECIMAL));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.DISTINCT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.DOUBLE));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.FLOAT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.INTEGER));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.JAVA_OBJECT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.LONGVARBINARY));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.NCLOB));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.NULL));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.NUMERIC));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.OTHER));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.REAL));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.SMALLINT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.SQLXML));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.TIME));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.TIMESTAMP));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.TIMESTAMP_WITH_TIMEZONE));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.TINYINT));
        Assert.assertFalse(LintUtils.isSqlTypeTextBased(Types.VARBINARY));
    }
    
    /**
     * Test of testIsSqlTypeNumericBased method, of class LintUtils.
     */
    @Test
    public void testIsSqlTypeNumericBased() {
        
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.BIGINT));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.DECIMAL));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.NUMERIC));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.REAL));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.SMALLINT));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.TINYINT));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.DOUBLE));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.FLOAT));
        Assert.assertTrue(LintUtils.isSqlTypeNumericBased(Types.INTEGER));
        
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.NVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.LONGNVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.LONGVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.CHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.NCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.VARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.BINARY));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.BIT));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.ARRAY));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.BLOB));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.BOOLEAN));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.CLOB));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.DATALINK));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.DATE));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.DISTINCT));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.JAVA_OBJECT));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.LONGVARBINARY));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.NCLOB));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.NULL));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.OTHER));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.SQLXML));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.TIME));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.TIMESTAMP));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.TIMESTAMP_WITH_TIMEZONE));
        Assert.assertFalse(LintUtils.isSqlTypeNumericBased(Types.VARBINARY));
    }
    
     /**
     * Test of testIsSqlTypeNumericBased method, of class LintUtils.
     */
    @Test
    public void testIsSqlTypeIntegerBased() {
        
        Assert.assertTrue(LintUtils.isSqlTypeIntegerBased(Types.BIGINT));
        Assert.assertTrue(LintUtils.isSqlTypeIntegerBased(Types.INTEGER));
        Assert.assertTrue(LintUtils.isSqlTypeIntegerBased(Types.SMALLINT));
        Assert.assertTrue(LintUtils.isSqlTypeIntegerBased(Types.TINYINT));

        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.BINARY));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.NVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.LONGNVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.LONGVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.CHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.NCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.VARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.BIT));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.ARRAY));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.BLOB));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.BOOLEAN));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.CLOB));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.DATALINK));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.DATE));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.DECIMAL));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.DISTINCT));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.DOUBLE));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.FLOAT));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.JAVA_OBJECT));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.LONGVARBINARY));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.NCLOB));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.NULL));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.NUMERIC));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.OTHER));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.REAL));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.SQLXML));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.TIME));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.TIMESTAMP));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.TIMESTAMP_WITH_TIMEZONE));
        Assert.assertFalse(LintUtils.isSqlTypeIntegerBased(Types.VARBINARY));
    }
    
}
