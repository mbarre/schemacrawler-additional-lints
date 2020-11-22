package io.github.mbarre.schemacrawler.tool.linter;

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

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.lint.LinterConfig;
import schemacrawler.tools.options.Config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Linter to check if text column has length(rtrim(column)) == length(column)
 */
public class LinterRightSpacePadding extends BaseLinter {
	public static final String SAMPLE_SIZE_PARAM = "sampleSize";
	private static final Logger LOGGER = Logger.getLogger(LinterRightSpacePadding.class.getName());
	private Integer sampleSize;

	/**
	 * The lint that parses and test Json content
	 */
	public LinterRightSpacePadding() {
		sampleSize = 1000;
		setSeverity(LintSeverity.high);
	}

	@Override
	public void configure(LinterConfig config) {
		sampleSize = Integer.valueOf(config.getProperties().get(SAMPLE_SIZE_PARAM));
	}

	@Override
	public String getSummary() {
		return "Column with right space padding";
	}

	@Override
	protected void lint(final Table table, final Connection connection) throws SchemaCrawlerException {
		try (Statement stmt = connection.createStatement()) {

			LOGGER.log(Level.CONFIG, "<" + SAMPLE_SIZE_PARAM + "> parameter set to {0}%", sampleSize);

			String sql;
			String columnName;
			String valueFound = "";
			String tableName = table.getName().replaceAll("\"", "");

			List<Column> columns = getColumns(table);

			Long totalRows = 0L;
			totalRows = LintUtils.getTableSize(stmt, tableName);
			LOGGER.log(Level.INFO, "totalrows=" + totalRows);
			Set<Long> sampleIndexes = LintUtils.generateSample(sampleSize, totalRows);
			LOGGER.log(Level.INFO, "sampleIndexes=" + sampleIndexes);

			for (Column column : columns) {
				if (LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getVendorTypeNumber())) {

					columnName = column.getName().replaceAll("\"", "");
					sql = "select distinct \"" + columnName + "\" from \"" + tableName + "\"";

					LOGGER.log(Level.CONFIG, "SQL : {0}", sql);

					try (ResultSet rs = stmt.executeQuery(sql)) {
						Long i = 0L;
						while (rs.next()) {
							if (sampleIndexes.contains(i)) {
								if (rs.getObject(columnName) != null) {
									valueFound = rs.getObject(columnName).toString();
								}

								if (valueFound.length() > 0 && Character.isWhitespace(valueFound.charAt(valueFound.length() - 1))) {
									addTableLint(table, getDescription(), column.getFullName());
								}
							}
							i++;
						}
					} catch (SQLException ex) {
						LOGGER.severe(ex.getMessage());
						throw new SchemaCrawlerException(ex.getMessage(), ex);
					}

				}
			}

		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			throw new SchemaCrawlerException(ex.getMessage(), ex);
		}

	}
}
