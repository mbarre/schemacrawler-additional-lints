# Schemacrawler Additional Lints
[![Project Website](https://img.shields.io/badge/Project%20Website-Additional%20Lints-7f3692.svg)](http://mbarre.github.io/schemacrawler-additional-lints/)
[![Build Status](https://travis-ci.org/mbarre/schemacrawler-additional-lints.svg?branch=master)](https://travis-ci.org/mbarre/schemacrawler-additional-lints)
[![Release](https://jitpack.io/v/mbarre/schemacrawler-additional-lints.svg)](https://jitpack.io/mbarre/schemacrawler-additional-lints)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=io.github.mbarre%3Aschemacrawler-additional-lints)](https://sonarqube.com/overview?id=io.github.mbarre%3Aschemacrawler-additional-lints)
[![Coverage Status](https://coveralls.io/repos/mbarre/schemacrawler-additional-lints/badge.svg?branch=master&service=github)](https://coveralls.io/github/mbarre/schemacrawler-additional-lints?branch=master)
[![Join the chat at https://gitter.im/mbarre/schemacrawler-additionallints](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mbarre/schemacrawler-additional-lints?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Dependency Status](https://www.versioneye.com/user/projects/57b3946ab4fa6e004171f956/badge.svg?style=flat)](https://www.versioneye.com/user/projects/57b3946ab4fa6e004171f956) 

<a href="https://www.jetbrains.com/idea/?fromMenu"><img src="./src/site/img/logo.svg"></a>

<a href="https://zenhub.com"><img src="https://raw.githubusercontent.com/ZenHubIO/support/master/zenhub-badge.png"></a>

Some additional lints for [Schemacrawler](http://sualeh.github.io/SchemaCrawler/)

# Main purpose

The main purpose of this lint library is to enhance native schemacrawler lints
with some more hardcore constraints and some specific postgres types. That's
why a postgreSQL database instance is required to test.

This project has been created to be used at our office
[Mairie de la Ville de Nouméa](http://www.noumea.nc/) to test our database
schema quality in a more easy way and make it possible for our partners to
apply our database quality policy the easy way.


# Build and install lint

**Notice that this version is designed to run on schemacrawler `14.x.x`...
and hence is requiring JDK-1.8 to be built and run.**

For now the jar is not available on maven central repo, so you'll have to build it yourself :

    git clone https://github.com/mbarre/schemacrawler-additional-lints.git schemacrawler-additional-lints
    cd schemacrawler-additional-lints
    export LINT_VERSION=1.02.02

Build without testing as a local postgres install is required to test.

    mvn install -Dmaven.test.skip=true
    cp target/schemacrawler-additional-lints-${LINT_VERSION}.jar $SCHEMACRAWLER_HOME/lib

... and you're done, you just have to pass your
normal  [schemacrawler lint](http://sualeh.github.io/SchemaCrawler/lint.html)
command and enjoy.

# Build and test

To ba able to test locally, assuming you have a locally postgreSQL instance
up and running with the proper `superuser` account, then run the following
commands

    dropdb --if-exists sc_lint_test
    createdb sc_lint_test
    mvn install
    cp target/schemacrawler-additional-lints-${LINT_VERSION}.jar $SCHEMACRAWLER_HOME/lib

# Pre-release tasks

Check that dependencies are up-to-date :

    mvn versions:display-dependency-updates

Check that plugins are up-to-date :

    mvn versions:display-plugin-updates

# Command line examples

    schemacrawler -server=postgresql -host=localhost -port=5432 -database=pdc -user=pdc_adm -password=pdc_adm -infolevel=maximum -schemas=pdc_adm -loglevel=INFO -c=lint --outputfile=lint_pdc.html -outputformat=html

# Donate


If you like this lint library, you can donate :

* Stars on github
* Ideas for new lints
* Code for new lints
* Code to increase code coverage
* [Create issue](https://github.com/mbarre/schemacrawler-additional-lints/issues)
* A nice logo
* Feebdack on this library
* Anything you think that could make us happy to go on developing this library,
including just some kind words on our gitter

# Thanks

Thanks to Jetbrains for offering me an [IntelliJ IDEA](https://www.jetbrains.com/idea/?fromMenu) Free Open Source License for this project. 
