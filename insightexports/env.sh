#!/bin/bash
#
export EXECDIR=/var/app/insightexports
source /var/app/insightlib/env.sh
export PGPASSFILE=/home/monman/.pgpass
export CLASSPATH=$CLASSPATH:$EXECPATH
echo $CLASSPATH
java -version
