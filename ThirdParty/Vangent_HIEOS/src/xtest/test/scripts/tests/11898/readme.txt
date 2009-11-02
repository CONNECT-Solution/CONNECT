SQ FindSubmissionSets Stored Query

Stored Query must use SOAP version 1.2 

This test contains many many test steps each validating one 
aspect of the FindSubmissionSets stored query.  This test relies
on test 11890 to pre-load appropriate test data into the registry.

The test steps are:

simple
	Basic query using patient ID and status

other_sourceid
	Adds selection on non-existant sourceId

submissiontime_in
	Includes all 5 in submission time range

submissiontime_out
	Includes none in submission time range

submissiontime_no_start
	No start time specified - includes all 5

submissiontime_no_end
	No end time - includes all 5

author_all
	Select all 5 based on same author

author_none
	Select none based on different author
	
contenttype_all
	Select all 5 based on same content type

contenttype_none
	Select none based on different content type
