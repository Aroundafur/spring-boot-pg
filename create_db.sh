#!/bin/bash

docker stop postgres

docker rm postgres

docker run --name postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:alpine

sleep 5

docker exec -it postgres psql postgres -U postgres -c "CREATE DATABASE demodb"
