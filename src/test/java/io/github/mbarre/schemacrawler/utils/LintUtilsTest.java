/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.utils;

import java.sql.Types;
import org.junit.Assert;
import org.junit.Test;

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
    
}
