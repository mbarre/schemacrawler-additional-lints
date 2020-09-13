FROM schemacrawler/schemacrawler

USER schcrwlr
WORKDIR /home/schcrwlr

# Copy over any additional local jar files, such as proprietary JDBC drivers
COPY --chown=schcrwlr:schcrwlr schemacrawler-additional-lints-1.05.03.jar /opt/schemacrawler/lib/

MAINTAINER Michèle Barré <michele.barre@gmail.com>
