# ClearPass-logs-and-e911
Programs to collect logs from ClearPass via the insight database and report on VoIP phones for e911

These directories are a set of programs to pull log information from ClearPass.
There are two different programs that are pulling log information:

1: ClearPass RADIUS text logs for security

2: ClearPass RADIUS authentications to track VoIP phones.

Insight Export
This program will pull RADIUS log information from the ClearPass insight database.  This information is stored in CEF log format in text files containing 1 hour's worth of data.  A total of 9 months are recorded on disk and compressed.  The files are stored on an NFS mount that is also mounted on BC Security computer to store the information in their log databases (Arcsight & OpenSearch).

Insight to DB
This program will pull RADIUS log information from the ClearPass insight database.  This information is then written to a postgres database.  14 days worth of data is kept in the database table.  This information is used to produce 2 types of reports.

e911 Report
Report 1 is generated every 6 hours sending a report message with informaiton about any VoIP phone that may have changed switch ports.
Report 2 is a report of all the phone information is generated once a day at night that is copied to the Data Stage server to be loaded into the APEX application for the e911 phone data.

InsightLib
This is a common directory for programs and libraries shared by all these programs.

LogCompress
this is a program that will compress any new files and delete any old files that need to be culled.

