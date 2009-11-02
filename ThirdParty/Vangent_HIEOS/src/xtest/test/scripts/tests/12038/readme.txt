Ret.b from Integrated Src/Rep Accept retrieve document set - single document

Instructions:

1) Create a document in your Integrated Src/Rep

2) Note the XDSDocumentEntry.uniqueId for the document

3) Generate a Register transaction for each to the Public Registry at endpoint 
http://ihexds.nist.gov:PORT/EVENT/services/xdsregistryb

See 
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Management#Configure_Public_Registry_Endpoint
for an explanation of the PORT and EVENT parts of this URI

4) Find the submissions in the Test Log for the Public Registry and report it to Kudu. See step 10 for Kudu instructions. Instructions for retrieving the Test Log results are found at
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Management#Tests_run_against_the_Public_Registry

5) Working in the testkit/tests/12038 directory of the testkit...

6) Edit testplan.xml replacing:
	a) ENDPOINT with the WS Endpoint where your Repository will accept
a Retrieve Document Set transaction
	b) DOCUID with the XdsDocumentEntry.uniqueId of one of the documents you created
	c) REPID with the repositoryUniqueId of your Repository

8) Issue a Retrieve Document Set transaction to your Repository by running xdstest with this modified testplan

9) log.xml must show successful completion in the root element.

10) Submit the resulting log.xml file and the EVS file from step 4 to Kudu. Instructions for uploading results files to Kudu are found at
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Management#Tests_run_against_the_Public_Registry


