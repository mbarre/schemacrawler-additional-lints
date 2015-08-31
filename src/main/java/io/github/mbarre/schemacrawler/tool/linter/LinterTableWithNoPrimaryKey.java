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

    /**
     * The lint
     */
    public LinterTableWithNoPrimaryKey(){
		setSeverity(LintSeverity.high);
	}

    /**
     * Get lint description
     * @return lint description
     */
    @Override
	public String getDescription() {
		return getSummary();
	}

    /**
     * lint summary
     * @return lint summary
     */
    @Override
	public String getSummary() {
		return " table should have a primary key";
	}

    /**
     * The lint that does the job
     * @param table table
     */
    @Override
	protected void lint(Table table) {

		requireNonNull(table, "No table provided");
		
		if(table.getPrimaryKey() == null){
			addLint(table, getDescription(), table.getName());
		}
	}

}
