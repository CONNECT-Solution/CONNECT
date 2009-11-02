XGQ FindDocuments for LeafClass

This test depends on test 12318 to initialize test data.

There is a single test step.

LeafClass indicates that the entire XML description of the document of interest will be returned.

Assertions test the following conditions:

ExtrinsicObjectCount - 2 ExtrinsicObject returned

ExtrinsicObjectsApproved - both ExtrinsicObject have status of Approved

homeMatch1 - home attribute on first ExtrinsicObject matches configuration
 in xdstest2tool/mgmt/default.xml

homeMatch2 - home attribute on second ExtrinsicObject matches 
configuration in xdstest2tool/mgmt/default.xml

Implicit Assertions:

Metadata validates against Schema

Metadata validates against XDS Metadata Validator


