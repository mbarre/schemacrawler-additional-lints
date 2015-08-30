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


}
