#!/bin/bash
LINTS_VERSION=${TRAVIS_TAG}

echo "** Publishing SchemaCrawler Additional Lints $LINTS_VERSION Docker image"

# Deploy image to Docker Hub
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker push mbarre/schemacrawler-additional-lints
docker logout
