R.b Folder lastUpdateTime

This test verifies that the registry properly sets the XDSFolder.lastUpdateTime
attribute.

Steps

no_time - submit folder with no lastUpdateTime attribute

has_time - submit folder with lastUpdateTime attribute (old date)

verify_no_submission_time - use GetFolder stored query to retrieve
folder from no_time step.  Verify the XDSFolder.lastUpdateTime
attribute shows today's date.

verify_has_submission_time - use GetFolder stored query to retrieve
folder from has_time step.  Verify the XDSFolder.lastUpdateTime
attribute shows today's date.

add_to_folder - submit a new DocumentEntry adding it to the folder
submitted in step has_time

verify_time_updated - use GetFolder stored query to verify that 
XDSFolder.lastUpdateTime was updated by the add_to_folder step.  Your
ability to pass this step depends on how you test and the precision 
of XDSFolder.lastUpdateTime in your implementation.  The XD* profiles do not specify the time precision. If you only support time precision 
to the minute and you run step add_to_folder a few seconds after step 
has_time then XDSFolder.lastUpdateTime will not show any change.  You will 
have to wait a minute or two before running this step to show that your 
Registry manages this attribute correctly.  If you support a 1 second 
granularity or better then the automatic running of this test will probably 
work fine for you.

rplc - replace document submitted in step add_to_folder.  This must update
the lastUpdateTime on the folder

verify_time_updated_by_rplc - verify the rplc step did update
lastUpdateTime on the folder

