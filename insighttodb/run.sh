#!/bin/bash
#
date
cd /var/app/insighttodb
export EXECDIR=$PWD
source env.sh
export LOGDIR=$EXECDIR/logs
export LOGFILE=${LOGDIR}/${YEAR}${MONTH}${DAY}${HOUR}${MINUTE}.log
echo "$EXECDIR/getlogs.sh > $LOGFILE 2>&1"
$EXECDIR/getlogs.sh $1 > $LOGFILE 2>&1
LINES=`grep -i exception $LOGFILE | wc -l`
if [ $LINES -gt 0 ];
then
	$SENDMAIL/sendmail.sh "ed@bc.edu" "ed@bc.edu" "Error in ClearPass Log to DB Extraction ($HOSTNAME)." "`cat $LOGFILE`"
fi
