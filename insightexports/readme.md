Aruba ClearPass Policy Manager Insight Log Exporter
---------------------------------------------------
This program will connect directly to a ClearPass Insight Database and export RADIUS
log information in CEF format.

This program is deisgned to connect directly to a ClearPass insight log server every 
2 minutes and export 2 minutes worth of data from 7 minutes ago.  The data is stored
into text files named per hour.  It should be scheduled to run every 2 minutes.

If a date and time are specified on the command line:

# ./run.sh "2024-04-01" "04:00"

all log events will be exported starting from that time until the current time.
