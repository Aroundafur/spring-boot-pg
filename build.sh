#!/bin/bash

mvn clean install

docker build -t dozza/spring-boot-pg:latest .

docker push dozza/spring-boot-pg:latest
