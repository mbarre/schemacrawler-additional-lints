/**
 * 
 */
package io.github.mbarre.schemacrawler.tool.linter;

import static java.util.Objects.requireNonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.LintSeverity;
import schemacrawler.tools.linter.LinterTableSql;

/**
 * Linter to check if JSON type is used instead of JSONB - PostgreSQL reserved lint
 * @author mbarre
 * @since 1.0.1
 */
public class LinterJsonTypeColumn extends LinterTableSql {
	private static final Logger LOGGER = Logger.getLogger(LinterJsonTypeColumn.class.getName());
	
	public LinterJsonTypeColumn() {
		setSeverity(LintSeverity.high);
	}

	@Override
	public String getDescription()
	{
		return getSummary();
	}

	@Override
	public String getSummary()
	{
		return " \"jsonb\" type should be used instead of \"json\" to store JSON data.";
	}

	@Override
	protected void lint(final Table table, Connection connection)
	{
		requireNonNull(table, "No table provided");
		
		try {
			System.out.println(connection.getMetaData().getDatabaseProductVersion());
			if("PostgreSQL".equalsIgnoreCase(connection.getMetaData().getDatabaseProductName()) &&
					"9.4".compareTo(connection.getMetaData().getDatabaseProductVersion()) <= 0){
				List<String> names = findJsonTypeColumn(table.getColumns());
				for (String name : names) {
					addLint(table, getDescription(), name);
				}
			}
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to get database product name info. Lint will not be executed.");
		}
	}

	private List<String> findJsonTypeColumn(List<Column> columns){
		List<String> names = new ArrayList<String>();
		for (Column column : columns) {
			if("json".equalsIgnoreCase(column.getColumnDataType().getDatabaseSpecificTypeName())){
				names.add(column.getName());
				 LOGGER.log(Level.INFO, column.getFullName() + " is json type.");
			}
		}
		return names;
	}

}
