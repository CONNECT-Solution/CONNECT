XGQ GetDocuments Cross Gateway Query

This test is dependent on test 12309 which does a FindDocuments query returning ObjectRefs.  
This test takes those ObjectRefs and retrieves (really query) the full metadata from the 
Receiving Gateway. This approach to the use of metadata is important in case the original 
FindDocuments query returned a large quantity of ObjectRefs then the GetDocuments query 
would be used to retreive the full metadata a bunch at a time. 

BTW, test 12309 is dependent on test 12318 to load test data.


There are several test steps:

uuid1 - issues a GetDocuments XGQ for a single document based on its UUID

uuid2 - same query but asking for 2 documents

Each test step references the log.xml file of test 12309 
(FindDocuments XGQ for ObjectRefs) to get the starting ObjectRefs.

Each test uses the following assertions:

ExtrinsicObjectCount - Correct # of ExtrinsicObjects returned

ExtrinsicObjectsApproved - both ExtrinsicObject have status of Approved

homeMatch* - home attribute on ExtrinsicObject matches configuration
 in xdstest2tool/mgmt/default.xml


Implicit Assertions:

Metadata validates against Schema

Metadata validates against XDS Metadata Validator

