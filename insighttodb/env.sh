#!/bin/bash
#
source /var/app/insightlib/env.sh
source $LIBDIR/getdate.sh
export EXECDIR=`pwd`
export PGPASSFILE=/home/monman/.pgpass
export CLASSPATH=$CLASSPATH:$EXECDIR
echo $CLASSPATH
