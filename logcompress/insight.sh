#!/bin/bash
#
export EXECDIR=/var/app/logcompress
export CLASSPATH=$EXECDIR
date
# insight log extracts from ares.
java ProcessFiles /var/app/insightexports/insightlogs/ares 190
# insight logs from the execution of the program.
java ProcessFiles /var/app/insightexports/logs 30
# insight logs from the execution of the program.
java ProcessFiles /var/app/insighttodb/logs 15
