#!/usr/bin/env bash

export IMAGE_NAME=$1
docker-compose -f docker-compose.yaml up --detach
echo "all containers are successfully up and running..."