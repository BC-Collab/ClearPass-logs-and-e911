#!/bin/bash
#
source env.sh
if [ "$1" != "" ]; then
  THEDATE=$1
fi
export PARAM="$THEDATE $2"
echo -------------------------------------
echo java insightToDB $PARAM
java insightToDB $PARAM
