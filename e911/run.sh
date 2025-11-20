#!/bin/bash
cd /var/app/e911
source env.sh
export EXECDIR=$PWD
if [ "$1" == "" ];
then
  LOGFILE="${YEAR}${MONTH}${DAY}.log"
else
  LOGFILE="${YEAR}${MONTH}${DAY}${HOUR}${MINUTE}.log"
fi
$EXECDIR/voip.sh $1 > $EXECDIR/logs/$LOGFILE 2>&1
$SENDMAIL/sendmail.sh "ed@bc.edu" "ed@bc.edu" "e911 log ${YEAR}${MONTH}${DAY} ($HOSTNAME)" "`cat $EXECDIR/logs/$LOGFILE`"
