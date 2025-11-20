#!/bin/bash
#
export DIRPATH=/var/app
export LIBDIR=$DIRPATH/insightlib
export SENDMAIL=$LIBDIR
source $LIBDIR/getdate.sh
export CLASSPATH=$LIBDIR/javax.mail.jar:$LIBDIR/activation.jar:$LIBDIR/postgresql-42.6.0.jar:$LIBDIR/mysql-connector-j-8.2.0.jar:$LIBDIR
