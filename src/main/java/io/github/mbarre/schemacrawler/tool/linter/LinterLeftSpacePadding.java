package io.github.mbarre.schemacrawler.tool.linter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.mbarre.schemacrawler.utils.LintUtils;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.lint.BaseLinter;

public class LinterLeftSpacePadding extends BaseLinter {
	private static final Logger LOGGER = Logger.getLogger(LinterLeftSpacePadding.class.getName());

	@Override
	protected void lint(Table table, Connection connection) throws SchemaCrawlerException {

		try (Statement stmt = connection.createStatement()){

			String sql;
			String columnName;
			String valueFound = "";
			String tableName = table.getName().replaceAll("\"", "");

			List<Column> columns = getColumns(table);
			for (Column column : columns) {
				if(LintUtils.isSqlTypeTextBased(column.getColumnDataType().getJavaSqlType().getVendorTypeNumber())){

					columnName = column.getName().replaceAll("\"", "");
					sql = "select distinct \"" + columnName + "\" from \"" + tableName + "\"" ;
					LOGGER.log(Level.CONFIG, "SQL : {0}", sql);

					try(ResultSet rs = stmt.executeQuery(sql)){

						while (rs.next()) {
							if(rs.getObject(columnName) != null) {
								valueFound = rs.getObject(columnName).toString();
							}
						}

						if(valueFound.length() > 0 && Character.isWhitespace(valueFound.charAt(0))){
							addLint(table, getDescription(), column.getFullName());
						}

					}catch (SQLException ex) {
						LOGGER.severe(ex.getMessage());
						throw new SchemaCrawlerException(ex.getMessage(), ex);
					}

				}
			}

		}catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			throw new SchemaCrawlerException(ex.getMessage(), ex);
		}
	}

	@Override
	public String getSummary() {
		return "Column with left space padding";
	}
}
