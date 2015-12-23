package io.github.mbarre.schemacrawler.tool.linter;


import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Check that objects (tables, columns) have a remark
 * @author mbarre
 */
public class LinterTableWithNoRemark extends BaseLinter
{

    /**
     * The lint
     */
    public LinterTableWithNoRemark() {
		setSeverity(LintSeverity.low);
	}

    /**
     * Get lint description
     * @return lint description
     */
    @Override
	public String getDescription()
	{
		return getSummary();
	}

    /**
     * lint summary
     * @return summary
     */
    @Override
	public String getSummary()
	{
		return " should have a remark";
	}

    /**
     * The lint that does the job
     * @param table table
     */
    @Override
	protected void lint(final Table table, Connection connection)
	{
		requireNonNull(table, "No table provided");

		List<String> names = findColumnsWithNoRemark(table.getColumns());
		if (!table.hasRemarks())
		{
			names.add(0,table.getName());
		}
		for (String name : names) {
			addLint(table, getDescription(), name);
		}
	}
	
	private List<String> findColumnsWithNoRemark(List<Column> columns){
		List<String> names = new ArrayList<>();
		for (Column column : columns) {
			if(!column.hasRemarks()){
				names.add(column.getName());
			}
		}
		return names;
	}
	

}
