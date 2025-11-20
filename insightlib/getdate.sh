#!/bin/bash
#
YEAR=`date +%Y`
MONTH=`date +%m`
DAY=`date +%d`
DAYOFWEEK=`date +%w`
HOUR=`date +%H`
MINUTE=`date +%M`
HOSTNAME=`hostname`
echo ${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINUTE}
THEDATE=$YEAR$MONTH$DAY
