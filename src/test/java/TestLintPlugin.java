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

import org.junit.Test;
import schemacrawler.tools.lint.LinterRegistry;

import static org.junit.Assert.assertTrue;

public class TestLintPlugin
{

  @Test
  public void testLintPlugin() throws Exception
  {
    final LinterRegistry registry = new LinterRegistry();
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterTableNameNotInLowerCase"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterJsonTypeColumn"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterJsonContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterXmlContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterBooleanContent"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterPrimaryKeyNotIntegerLikeType"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterBlobTypeColumn"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterColumnSize"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterByteaTypeColumn"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterPluralTableName"));
    assertTrue(registry.hasLinter("io.github.mbarre.schemacrawler.tool.linter.LinterLeftSpacePaddingTest"));
  }

}
