+++
title = "About Additional Lints"
menuTitle = "About"
date = 2020-11-16T13:59:22+11:00
weight = 15
+++


This project is a set of free basic database lints that come in addition to the ones embedded in Schemacrawler Lint.

Additional lint checks are :  

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterBlobTypeColumn*  
  Checks that no binary column type is used.   
  Default severity : critical.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterBooleanContent*  
  Checks if column type should be boolean. ex.: int column with only 1 and 0 values.   
  Default severity : high.
  
**Linter :** *io.github.mbarre.schemacrawler.tools.LinterByteaTypeColumn*  
  Checks that no binary column type is used. Postgres recommandation is to use OID.   
  Default severity : medium.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterColomnContentNotNormalized*  
  Checks wether foreign table should have been used.
  This linter is configurable thanks config file and following parameters:

     <linter id="io.github.mbarre.schemacrawler.tool.linter.LinterColumnContentNotNormalized">
      <run>true</run>
      <config>
        <property name="nbRepeatTolerance">2</property>
        <property name="minTextColumnSize">2</property>
      </config>  
     </linter>

  nbRepeatTolerance : Repeat tolerance, 1 is the most agressive, duplicates occur since there are at least two reps of the same value. Default value is set to 2.    
  minTextColumnSize : The minimal length of the text based column. We consider that a 1-length char based column is acceptable. Default value is set to 2.   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterColumnSize*  
  Checks that string like type column size is appropriated.
  This linter is configurable thanks config file and following parameter:

    <linter id="io.github.mbarre.schemacrawler.tool.linter.LinterColumnSize">
      <run>true</run>
      <config>
        <property name="minColumnSizePercent">20</property>
      </config>
    </linter>

  Default value is minColumnSizePercent = 20.   
  Default severity : high.
  
**Linter :** *io.github.mbarre.schemacrawler.tools.LinterCompressBlob*  
    If column is BLOB or BINARY type, checks if compression can be useful:
    This linter is configurable thanks config file and following parameter:

      <linter id="io.github.mbarre.schemacrawler.tool.linter.LinterCompressBlob">
        <run>true</run>
        <config>
          <property name="minCompressionPercent">30</property>
        </config>
      </linter>
  
    Default value is minCompressionPercent = 20, means if compressed file is more than 20% smaller than original one, you shoud compress it.   
    Default severity : high.


**Linter :** *io.github.mbarre.schemacrawler.tools.LinterForeignKeyMismatchLazy*  
  Check that a foreign key has the same type as the referenced primary key.   
  Default severity : critical.
  
**Linter :** *io.github.mbarre.schemacrawler.tools.LinterForeignKeyName*  
  If you need your foreign key name has a specific prefix or suffix, this linter can check it.
  Default severity : medium.
  Exemple : if you want your foreign key starts with 'id_'
  
  ```
  <linter id="io.github.mbarre.schemacrawler.tool.linter.LinterForeignKeyName">
    <run>true</run>
    <severity>medium</severity>
    <config>
        <property name="foreignKeyPrefix">id_</property>
    </config>
  </linter>
  ```    
 
**Linter :** *io.github.mbarre.schemacrawler.tools.LinterJsonContent*  
  Checks if column type should be JSON according to its content.   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterJsonTypeColumn*  
  Checks that JSONB type is used for JSON content. (PostgresSQL)   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterPrimarykeyNotIntegerLikeType*  
  Checks that primary is integer like type.   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterTableNameNotInLowerCase*  
  Checks that all tables and columns names are in lower case.   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterXmlContent*  
  Check that columns with XML content is XML type.   
  Default severity : high.

**Linter :** *io.github.mbarre.schemacrawler.tools.LinterTimeStampWithoutTimeZoneColumn*  
  Check if columns have any TimeStamp data type columns if so suggest to use TimeStamp with TimeZones - PostgreSQL reserved.
  Default severity : critical.