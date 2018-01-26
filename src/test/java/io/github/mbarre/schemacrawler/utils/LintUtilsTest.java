package io.github.mbarre.schemacrawler.utils;

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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.sql.Types;

/**
 *
 * @author salad74
 */
public class LintUtilsTest {
    
    public LintUtilsTest() {
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
    
     /**
     * Test of testIsSqlTypeNumericBased method, of class LintUtils.
     */
    @Test
    public void testIsSqlTypeBinaryBased() {
        
        Assert.assertTrue(LintUtils.isSqlTypeBinayBased(Types.BINARY));
        Assert.assertTrue(LintUtils.isSqlTypeBinayBased(Types.BLOB));
        Assert.assertTrue(LintUtils.isSqlTypeBinayBased(Types.LONGVARBINARY));
        Assert.assertTrue(LintUtils.isSqlTypeBinayBased(Types.VARBINARY));
        
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.BIGINT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.DECIMAL));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.NUMERIC));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.REAL));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.SMALLINT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.TINYINT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.DOUBLE));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.FLOAT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.INTEGER));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.NVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.LONGNVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.LONGVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.CHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.NCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.VARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.BIT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.ARRAY));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.BOOLEAN));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.CLOB));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.DATALINK));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.DATE));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.DISTINCT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.JAVA_OBJECT));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.NCLOB));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.NULL));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.OTHER));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.SQLXML));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.TIME));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.TIMESTAMP));
        Assert.assertFalse(LintUtils.isSqlTypeBinayBased(Types.TIMESTAMP_WITH_TIMEZONE));
        
    }

    /**
     * Test of testIsSqlTypeNumericBased method, of class LintUtils.
     */
    @Test
    public void testIsSqlTypeDateBased() {
        Assert.assertTrue(LintUtils.isSqlTypeDateBased(Types.DATE));
        Assert.assertTrue(LintUtils.isSqlTypeDateBased(Types.TIME));
        Assert.assertTrue(LintUtils.isSqlTypeDateBased(Types.TIME_WITH_TIMEZONE));
        Assert.assertTrue(LintUtils.isSqlTypeDateBased(Types.TIMESTAMP));
        Assert.assertTrue(LintUtils.isSqlTypeDateBased(Types.TIMESTAMP_WITH_TIMEZONE));

        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.BINARY));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.BLOB));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.LONGVARBINARY));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.VARBINARY));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.BIGINT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.DECIMAL));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.NUMERIC));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.REAL));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.SMALLINT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.TINYINT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.DOUBLE));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.FLOAT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.INTEGER));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.NVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.LONGNVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.LONGVARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.CHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.NCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.VARCHAR));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.BIT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.ARRAY));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.BOOLEAN));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.CLOB));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.DATALINK));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.DISTINCT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.JAVA_OBJECT));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.NCLOB));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.NULL));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.OTHER));
        Assert.assertFalse(LintUtils.isSqlTypeDateBased(Types.SQLXML));
    }
    
}
