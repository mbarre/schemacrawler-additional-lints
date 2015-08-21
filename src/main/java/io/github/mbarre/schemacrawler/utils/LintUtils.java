package io.github.mbarre.schemacrawler.utils;

import java.sql.Types;

public class LintUtils {

	/**
     * Tells wether a column is text based or not.
     * @param javaSqlType
     * @return
     */
    public static final boolean isSqlTypeTextBased(int javaSqlType) {
        return (javaSqlType == Types.NVARCHAR)
                || (javaSqlType == Types.LONGNVARCHAR)
                || (javaSqlType == Types.LONGVARCHAR)
                || (javaSqlType == Types.CHAR)
                || (javaSqlType == Types.NCHAR)
                || (javaSqlType == Types.NVARCHAR)
                || (javaSqlType == Types.VARCHAR);
        
    }
}
