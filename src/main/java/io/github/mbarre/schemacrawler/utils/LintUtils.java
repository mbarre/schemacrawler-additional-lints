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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import schemacrawler.schema.ColumnDataType;
import schemacrawler.schemacrawler.SchemaCrawlerException;

/**
 *
 * @author salad74
 */
public class LintUtils {
	private static final Logger LOGGER = Logger.getLogger(LintUtils.class.getName());

	private LintUtils(){
		throw new IllegalAccessError("Utility class.");
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
	 * @return is the sqlType is text based or not
	 */
	public static final boolean isSqlTypeLargeTextBased(ColumnDataType dataType) {
		return dataType.getJavaSqlType().getVendorTypeNumber() == Types.CLOB
                        || dataType.getJavaSqlType().getVendorTypeNumber() == Types.LONGVARCHAR
                        || "text".equalsIgnoreCase(dataType.getDatabaseSpecificTypeName());
	}

	/**
	 * Tells wether a column is date like type or not.
	 * @param javaSqlType the dataType
	 * @return is the sqlType is date based or not
	 */
	public static final boolean isSqlTypeDateBased(int javaSqlType) {
		return javaSqlType == Types.DATE
				|| javaSqlType == Types.TIME
				|| javaSqlType == Types.TIME_WITH_TIMEZONE
				|| javaSqlType == Types.TIMESTAMP
				|| javaSqlType == Types.TIMESTAMP_WITH_TIMEZONE;
	}

	public static Set<Long> generateSample(int sampleSize, Long totalRows){
		Set<Long> sampleIndex = new HashSet<>();
		UniformRandomProvider rng = RandomSource.create(RandomSource.MT);
		if(totalRows < sampleSize)
			sampleSize = totalRows.intValue();
		while (sampleIndex.size() < sampleSize){
			Long value = rng.nextLong(totalRows);
			LOGGER.log(Level.INFO, "value="+value);
			if(value.compareTo(totalRows) <= 0) {
				sampleIndex.add(value);
			}
		}
		return sampleIndex;
	}

	public static Long getTableSize(Statement stmt, String tableName) throws SchemaCrawlerException {
		Long totalRows;
		try (ResultSet rs0 = stmt.executeQuery("select count(*) from \"" + tableName + "\"")) {
			rs0.next();
			totalRows = rs0.getLong(1);
		} catch (SQLException ex) {
			LOGGER.severe(ex.getMessage());
			throw new SchemaCrawlerException(ex.getMessage(), ex);
		}
		return totalRows;
	}

}
