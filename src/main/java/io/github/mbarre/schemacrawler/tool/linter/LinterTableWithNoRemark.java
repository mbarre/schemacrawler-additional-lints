package io.github.mbarre.schemacrawler.tool.linter;


import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 * Check that objects (tables, columns) have a remark
 * @author mbarre
 */
public class LinterTableWithNoRemark extends BaseLinter
{

	public LinterTableWithNoRemark() {
		setSeverity(LintSeverity.low);
	}

	@Override
	public String getDescription()
	{
		return getSummary();
	}

	@Override
	public String getSummary()
	{
		return " should have a remark";
	}

	@Override
	protected void lint(final Table table)
	{
		requireNonNull(table, "No table provided");

		List<String> names = findColumnsWithNoRemark(table.getColumns());
		if (!table.hasRemarks())
		{
			names.add(0,table.getName());
		}
		for (String name : names) {
			addLint(table, getSummary(), name);
		}
	}
	
	private List<String> findColumnsWithNoRemark(List<Column> columns){
		List<String> names = new ArrayList<String>();
		for (Column column : columns) {
			if(!column.hasRemarks()){
				names.add(column.getName());
			}
		}
		return names;
	}
	

}
