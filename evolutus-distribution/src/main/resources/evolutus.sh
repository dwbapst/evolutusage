#!/bin/bash

SCRIPTPATH="$(dirname "$(readlink -f "$0")")"

java -classpath "$SCRIPTPATH/lib/*" pl.edu.agh.evolutus.cmd.Application $@
