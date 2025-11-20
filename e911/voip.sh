#!/bin/bash
source env.sh
export EXECDIR=$PWD
java phonePorts $1
java export $1
if [ -f "email.eml" ]; 
then
  echo Sending email...
  $SENDMAIL/sendHTMLemail.sh "itsstaff.collab@bc.edu" "itsstaff.voip.alerts@bc.edu" "VoIP Phone Move Report" "$EXECDIR/email.eml"
  rm $EXECDIR/email.eml
fi
if [ "$1" == "" ];
then
  wc -l fullexport.csv
  cp fullexport.csv ${EXECDIR}/reports/${YEAR}${MONTH}${DAY}0000-fullexport.csv
# dev datastage server
#  scp fullexport.csv voiceservicesftp@genesis.bc.edu:
# prod datastage server
  scp fullexport.csv voiceservicesftp@neon.bc.edu:
  ssh voiceservicesftp@neon "~/fixpermissions.sh"
  if [ $? != 0 ]; then
    echo "FOCAL: Error copying e911 data to NEON."
    $SENDMAIL/sendHTMLemail.sh "itsstaff.collab@bc.edu" "ed@bc.edu" "FOCAL: Error copying e911 data to NEON" "Error."
  fi
# new datastage server
HOST="xenon2.bc.edu"
USERNAME="dsadmin"
PORT=22 # or your SFTP server's port
sftp -oPort=$PORT $USERNAME@$HOST <<EOF
cd /opt/ibm/is/isdata5/voiceservicesftp/inbound
put fullexport.csv
chmod 777 fullexport.csv
exit
EOF
#
else
  cp $EXECDIR/export.csv ${EXECDIR}/reports/${YEAR}${MONTH}${DAY}${HOUR}${MINUTE}-export.csv
fi
