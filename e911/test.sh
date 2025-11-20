#!/bin/bash
cd /var/app/e911
source env.sh
export EXECDIR=$PWD
scp test.txt voiceservicesftp@neon.bc.edu:
RETURNCODE="$?"
if [ "$RETURNCODE" -gt 0 ];
then
  echo "Error copying file to NEON"
  $SENDMAIL/sendHTMLemail.sh "itsstaff.collab@bc.edu" "ed@bc.edu" "FOCAL: Error copying e911 data to NEON" "Could not copy test file to NEON."
else
  echo "No errors copying file."
fi
