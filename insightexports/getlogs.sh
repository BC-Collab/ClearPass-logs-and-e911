#!/bin/bash
#
source env.sh
export CLEARPASSLOGS=${EXECDIR}/insightlogs/ares
if [ "$1" != "" ]; then
  THEDATE=$1
fi
export PARAM="$THEDATE $2"
echo -------------------------------------
echo $CLASSPATH
echo java insightLogs $CLEARPASSLOGS $PARAM $2
java insightLogs $CLEARPASSLOGS $PARAM
