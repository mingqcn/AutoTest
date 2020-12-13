#!/bin/bash
git pull
mvn clean test site -Dooad.group=$0 -Dooad.testdir=$1 -Dmanagement.gate=$2 -Dmall.gate=$3
