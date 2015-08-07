# schemacrawler-additionnallints 
[![Build Status](https://travis-ci.org/mbarre/schemacrawler-additionnallints.svg?branch=master)](https://travis-ci.org/mbarre/schemacrawler-additionnallints) [![Coverage Status](https://coveralls.io/repos/mbarre/schemacrawler-additionnallints/badge.png?branch=master&service=github)](https://coveralls.io/github/mbarre/schemacrawler-additionnallints?branch=master)

Some additionnal lints for [Schemacrawler](http://sualeh.github.io/SchemaCrawler/)

# Build and install lint

**Notice that this version is designed to run on schemacrawler `12.06.03`.**

For now the jar is not available on maven central repo, so you'll have to build it yourself :

    git clone https://github.com/mbarre/schemacrawler-additionnallints.git schemacrawler-additionnallints
    cd schemacrawler-additionnallints

Build without testing as a local postgres install is required to test.

    mvn install -Dmaven.test.skip=true
    export LINT_VERSION=1.0
    cp target/schemacrawler-additionnallints-${LINT_VERSION}.jar $SCHEMACRAWLER_HOME/lib

... and you're done, you just have to pass your
normal  [schemacrawler lint](http://sualeh.github.io/SchemaCrawler/lint.html)
command and enjoy.
