#!/bin/bash
#
cd /var/app/insightlib
source ./env.sh
export CLASSPATH=$CLASSPATH:$PWD
echo $CLASSPATH
# SendMail from to subject messagefile
echo "java sendHTMLemail \"$1\" \"$2\" \"$3\" \"$4\""
java sendHTMLemail "$1" "$2" "$3" "$4"
