#!/bin/bash
cd /var/app/e911
source env.sh
export EXECDIR=$PWD
scp fullexport.csv voiceservicesftp@neon.bc.edu:
#
HOST="xenon2.bc.edu"
USERNAME="dsadmin"
PORT=22 # or your SFTP server's port
sftp -oPort=$PORT $USERNAME@$HOST <<EOF
cd /opt/ibm/is/isdata5/voiceservicesftp/inbound
put fullexport.csv
chmod 777 fullexport.csv
exit
EOF
