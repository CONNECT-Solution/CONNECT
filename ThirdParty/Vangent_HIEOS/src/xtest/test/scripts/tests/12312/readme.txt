XGR XCA Retrieve single document

Generate request for the retrieval of a single document based 
on metadata returned in test 12311.

Based on the XDSDocument.uniqueId, repositoryUniqueId, and homeCommunityId returned 
in test 12311, issue a RetrieveDocumentSet transaction to retrieve a document.  

When successful, perform the following validations:

* Single document returned

* RepositoryUniqueId matches request

* DocumentUniqueId matches request

* MimeType matches metadata from test 12311

* Document hash, as calculated after the retrieve, matches value in metadata from test 12311

* HomeCommunityId matches configuration
