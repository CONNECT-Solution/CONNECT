Ret.a Accept Retrieve Document by Integrated Src/Rep

1) Create/identify a document in your Integrated Source/Repository to use for 
this test.

2) Generate a Register.a transaction for each to the Public Registry at endpoint 
http://ihexds.nist.gov:PORT/EVENT/services/xdsregistrya

See 
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Management#Configure_Public_Registry_Endpoint
for an explanation of the PORT and EVENT parts of this URI

3) With your tools identify from your system OR using the Test Log
Browser to look inside the Public Registry, extract the unique ID 
(XDSDocumentEntry.uniqueId) attribute for the document you just 
submitted. 

4) Edit the testplan.xml file found in testkit/tests/12045/query and
replace the string $uid$ with the uniqueId found in the last step.

5) Run this testplan against the Public Registry with the command line:
xdstest -s pub -t 12045 query -run -err.  This will extract your
metadata from the Public Registry. If successful continue.

6) Run the testplan testkit/tests/12045/retrieve. This will use the above metadata
to locate you document in your Integrated Source/Repository and 
retrieve it.  After retrieval it will compare the document contents 
against the size and hash and mimeType recorded in the Public 
Registry. 

7) Submit to Kudu the log files from the query and retrieve steps via instructions found at
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Management#Tests_run_with_xdstest_tool