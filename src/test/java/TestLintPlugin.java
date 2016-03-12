

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
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterJsonTypeColumn"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterJsonContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterXmlContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterBooleanContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterPrimaryKeyNotIntegerLikeType"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterBlobTypeColumn"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterColumnSize"));
  }

}
