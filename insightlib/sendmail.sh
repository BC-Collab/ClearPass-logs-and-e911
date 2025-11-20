#!/bin/bash
#
cd /var/app/insightlib
source env.sh
export CLASSPATH=$CLASSPATH:${PWD}
echo $CLASSPATH
# SendMail from to subject message
echo "java SendMail \"$1\" \"$2\" \"$3\" \"$4\""
java SendMail "$1" "$2" "$3" "$4"
