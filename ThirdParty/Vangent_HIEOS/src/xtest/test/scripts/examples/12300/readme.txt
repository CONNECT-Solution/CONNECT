XGQ FindDocuments for LeafClass

Initiate a FindDocuments Cross-Gateway Query (XGQ) to the Public
Registry server's Responding Gateway for a pre-determined Patient ID. Request LeafClass (full metadata) be returned.

The metadata contained in the file testplan.xml is an example of the request.  The content of element 
/TestPlan/TestStep/XCQTransaction/Metadata is the actual request. 
To run this example you must first run test 12318 which establishes
some test data to query.

Instead if you want to use the test data I have already installed,  
see http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Kit_2007-2008_Test_Descriptions#12300 
for the Patient ID and Web Service Endpoint currently being used. To 
use this, edit the testplan, inserting the patient id for $patient_id$ 
and remove the UseId and Assertions elements.




