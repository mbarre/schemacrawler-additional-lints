+++
title = "docker"
menuTitle = "Docker Image"
weight = 15
+++

Schemacrawler Additional Lints is now released with an [image](https://hub.docker.com/r/mbarre/schemacrawler-additional-lints) on Docker Hub. This is an extension of the [SchemaCrawler Docker Image](https://www.schemacrawler.com/docker-image.html).

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

A default linter config file is available in the schemacrawler container home directory : **schemacrawler-linter-config.xml**.
You can use it to include or exclude tables, change the lints severity, by adding **--linter-configs=schemacrawler-linter-config.xml** to lint and csv commands.

Enjoy !