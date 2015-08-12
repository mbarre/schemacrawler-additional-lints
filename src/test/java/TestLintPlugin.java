

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import schemacrawler.tools.lint.LinterRegistry;

public class TestLintPlugin
{

  @Test
  public void testLintPlugin() throws Exception
  {
    final LinterRegistry registry = new LinterRegistry();
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterTableWithNoPrimaryKey"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterTableWithNoRemark"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized"));
    
  }

}
