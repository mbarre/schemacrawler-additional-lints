#  Schemacrawler Additional Lints
[![Project Website](https://img.shields.io/badge/Project%20Website-Additional%20Lints-7f3692.svg)](http://mbarre.github.io/schemacrawler-additional-lints/)
[![Build Status](https://travis-ci.org/mbarre/schemacrawler-additional-lints.svg?branch=master)](https://travis-ci.org/mbarre/schemacrawler-additional-lints)
[![Release](https://jitpack.io/v/mbarre/schemacrawler-additional-lints.svg)](https://jitpack.io/mbarre/schemacrawler-additional-lints)
[![Coverage Status](https://coveralls.io/repos/mbarre/schemacrawler-additional-lints/badge.svg?branch=master&service=github)](https://coveralls.io/github/mbarre/schemacrawler-additional-lints?branch=master)
[![Join the chat at https://gitter.im/mbarre/schemacrawler-additionallints](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mbarre/schemacrawler-additional-lints?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
![Docker Pulls](https://img.shields.io/docker/pulls/mbarre/schemacrawler-additional-lints)

Some additional lints for [Schemacrawler](http://sualeh.github.io/SchemaCrawler/)

# Main purpose

The main purpose of this lint library is to enhance native schemacrawler lints
with some more hardcore constraints and some specific postgres types. That's
why a postgreSQL database instance is required to test.

This project has been created to be used at our former office
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

To be able to test locally, assuming you have a locally postgreSQL instance
up and running with the proper `superuser` account, then run the following
commands

    dropdb --if-exists sc_lint_test
    createdb sc_lint_test
    mvn install
    cp target/schemacrawler-additional-lints-${LINT_VERSION}.jar $SCHEMACRAWLER_HOME/lib
    
Or, install [Docker Compose](https://docs.docker.com/compose/install/) to deploy PostgreSQL and the database

    docker-compose -f docker/postgresql.yml up
    mvn install
    cp target/schemacrawler-additional-lints-${LINT_VERSION}.jar $SCHEMACRAWLER_HOME/lib
    
# Docker Image

Schemacrawler Additional Lints is now released with an image on Docker Hub. This is an extension of the [SchemaCrawler Docker Image](https://www.schemacrawler.com/docker-image.html).

You can run docker container like this :

    docker run --name schemacrawler-additional-lints --rm -i -t --entrypoint=/bin/bash --net=host mbarre/schemacrawler-additional-lints

--net=host option will allow you to work on database deployed on your host.

Use the Schemacrawler Interactive Shell

    schemacrawler --shell
    
Use the following script from within the shell to launch the lints analysis :

    connect --server=your_db_server --database=your_db --user=your_user
    load --info-level=minimum
    execute --command lint

Or, simplier...
    
    schemacrawler --server=your_db_server --database=your_db --user=your_user --info-level=minimum  --command lint

The docker image also includes the command which export lints in csv files, for more information see [adriens/schemacrawler-additional-command-lints-as-csv](https://github.com/adriens/schemacrawler-additional-command-lints-as-csv)

You can try it this way :

    schemacrawler --server=your_db_server --database=your_db --user=your_user --info-level=minimum  --command csv


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

<a href="https://www.jetbrains.com/idea/?fromMenu"><img src="https://raw.githubusercontent.com/mbarre/schemacrawler-additional-lints/master/src/site/img/logo.png" height="42" width="42"></a>
To Jetbrains for offering an [IntelliJ IDEA](https://www.jetbrains.com/idea/?fromMenu) Free Open Source License to this project contributors.
