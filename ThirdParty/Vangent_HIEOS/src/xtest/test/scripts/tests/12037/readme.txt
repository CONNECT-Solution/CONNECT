Ret.b from Integrated Src/Rep Accept retrieve document set - two documents 

Instructions:

Run test 12038 first to establish your repository's ability to perform a Retreive Document Set transaction.  This test will reference the test data created in that test.

Re-run the Retrieve Document Set transaction specified in 12038 but with two 
documents specified.

To do this, you will have to edit testplan.xml (from test 12038) and create a
second <DocumentUniqueId/> element containing the XDSDocumentEntry.uniqueId attribute of the other document submitted as part of 12038.

The logging requirements are the same as 12038.
