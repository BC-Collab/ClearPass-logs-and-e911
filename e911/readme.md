This program reads from a postgres database table that contains RADIUS log events.
It determines if any VoIP phones have moved in the last period of time and generates
a report.  The information is written into another database table which then can
be displayed or extracted.

There are 2 modes for this program.  If no parameters are specified, the program
creates a report for the previous day (from 0:00 to 23:59) and writes that
to a CSV file to be transfered to a DataStage server to be ingensted into
an APEX app that keeps track of VoIP phone locations.  This report contains
all phones and their locations.
If a parameter is specified, the report is made for that many hours.
That is to say, if you run the program and pass it a "6", it will generate 
a report for the last 6 hour period.  If that report contains any moves, an
email report with just those moves are generated and emailed out.  If no moves
are recorded, no report is sent.
