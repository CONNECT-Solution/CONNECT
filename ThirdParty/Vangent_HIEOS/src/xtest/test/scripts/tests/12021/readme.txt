Ret.b Accept retrieve document set - two documents

submit - Send a Provide and Register.b transaction to your Repository which
forwards the metadata to the Public Registry. Your Repository must
be properly configured (see wiki at
http://ihewiki.wustl.edu/wiki/index.php/XDS_Test_Kit_2007-2008_Test_Descriptions#12021).

query - Use Stored Query to fetch the repositoryUniqueId and document
uniqueId for each document. Make sure to have xdstest configured to 
query from the Public Registry.

retrieve - Use the above parameters to generate a Retrieve Document
Set transaction for both documents.

