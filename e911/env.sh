#!/bin/bash
#
source /var/app/insightlib/env.sh
source $LIBDIR/getdate.sh
export PGPASSFILE=/home/monman/.pgpass
export EXECDIR=$PWD
export CLASSPATH=$CLASSPATH:$EXECDIR
