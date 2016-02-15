package io.github.mbarre.schemacrawler.utils;

import java.sql.Types;

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


}
