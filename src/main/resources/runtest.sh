#!/bin/bash
git pull
mvn clean test site -Dooad.group=$0 -Dooad.testdor=$0 -Dmanagement.gate=$1 -Dmall.gate=$2
