package io.github.mbarre.schemacrawler.utils;

import java.sql.Types;
import schemacrawler.schema.ColumnDataType;

/**
 *
 * @author salad74
 */
public class LintUtils {

        public LintUtils(){
            
        }
	/**
	 * Tells wether a column is text based or not.
	 * @param javaSqlType the javaSqlType
	 * @return is the sqlType is test based or not
	 */
	public static final boolean isSqlTypeTextBased(int javaSqlType) {
		return (javaSqlType == Types.NVARCHAR)
				|| (javaSqlType == Types.LONGNVARCHAR)
				|| (javaSqlType == Types.LONGVARCHAR)
				|| (javaSqlType == Types.CHAR)
				|| (javaSqlType == Types.NCHAR)
				|| (javaSqlType == Types.VARCHAR);
	}

	/**
	 * Tells wether a column is numeric based or not.
	 * @param javaSqlType the javaSqlType
	 * @return is the sqlType is numeric based or not
	 */
	public static final boolean isSqlTypeNumericBased(int javaSqlType) {
		return (javaSqlType == Types.BIGINT)
				|| (javaSqlType == Types.DECIMAL)
				|| (javaSqlType == Types.FLOAT)
				|| (javaSqlType == Types.DOUBLE)
				|| (javaSqlType == Types.INTEGER)
				|| (javaSqlType == Types.NUMERIC)
                                || (javaSqlType == Types.SMALLINT)
                                || (javaSqlType == Types.REAL)
                                || (javaSqlType == Types.TINYINT);
	}

        /**
	 * Tells wether a column is integer like type or not.
	 * @param javaSqlType the javaSqlType
	 * @return is the sqlType is numeric based or not
	 */
	public static final boolean isSqlTypeIntegerBased(int javaSqlType) {
		return (javaSqlType == Types.BIGINT)
				|| (javaSqlType == Types.INTEGER)
                                || (javaSqlType == Types.SMALLINT)
                                || (javaSqlType == Types.TINYINT);
	}
        
         /**
	 * Tells wether a column is binary like type or not.
	 * @param javaSqlType the javaSqlType
	 * @return is the sqlType is binary based or not
	 */
	public static final boolean isSqlTypeBinayBased(int javaSqlType) {
		return (javaSqlType == Types.BINARY) 
                        || (javaSqlType == Types.BLOB)
                        || (javaSqlType == Types.VARBINARY)
                        || (javaSqlType == Types.LONGVARBINARY);
	}
        
        /**
	 * Tells wether a column is large text like type or not.
	 * @param dataType the dataType
	 * @return is the sqlType is binary based or not
	 */
	public static final boolean isSqlTypeLargeTextBased(ColumnDataType dataType) {
		return (dataType.getJavaSqlType().getJavaSqlType() == Types.CLOB
                        || dataType.getJavaSqlType().getJavaSqlType() == Types.LONGVARCHAR
                        || "text".equalsIgnoreCase(dataType.getDatabaseSpecificTypeName()));
	}


}
