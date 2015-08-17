/**
 * 
 */
package io.github.mbarre.schemacrawler.tool.linter;

import static java.util.Objects.requireNonNull;
import schemacrawler.schema.Table;
import schemacrawler.tools.lint.BaseLinter;
import schemacrawler.tools.lint.LintSeverity;

/**
 * Check that tables have primary key
 * @author mabrre
 */
public class LinterTableWithNoPrimaryKey extends BaseLinter {

	public LinterTableWithNoPrimaryKey(){
		setSeverity(LintSeverity.high);
	}

	@Override
	public String getDescription() {
		return getSummary();
	}

	@Override
	public String getSummary() {
		return " table should have a primary key";
	}

	@Override
	protected void lint(Table table) {

		requireNonNull(table, "No table provided");
		
		if(table.getPrimaryKey() == null){
			addLint(table, getDescription(), table.getName());
		}
	}

}
