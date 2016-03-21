### About Additional SchemaCrawler Lints

This project is a set of free basic database lints that come in addition to the ones embedded in Schemacrawler Lint.

Additionnal lint checks are :  
**Linter :** *io.github.mbarre.schemacrawler.tools.LinterBlobTypeColumn*  
  Checks that no binary column type is used.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterBooleanContent*  
  Checks if column type should be boolean. ex.: int column with only 1 and 0 values.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterColomnContentNotNormalized*  
  Checks wether foreign table should have been used.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterColumnSize*  
  Checks that string like type column size is appropriated.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterForeignKeyMismatchLazy*  
  Check that a foreign key has the same type as the referenced primary key.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterJsonContent*  
  Checks if column type should be JSON according to its content.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterJsonTypeColumn*  
  Checks that JSONB type is used for JSON content. (PostgresSQL)

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterPrimarykeyNotIntegerLikeType*  
  Checks that primary is integer like type.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterTableNameNotInLowerCase*  
  Checks that all tables and columns names are in lower case.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterXmlContent*  
  Check that columns with XML content is XML type.
