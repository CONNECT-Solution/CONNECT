SQ GetSubmissionSetAndContents Stored Query

Stored Query must use SOAP version 1.2 

uniqueid
	Query based on uniqueId

folder_and_docs
	Query based on uniqueId. Submission Set includes multiple documents and a folder.

uuid
	Query based on uuid

format_code
	Filter the documents via Format code

conf_code
	Filter the documents via Confidentialitiy code

In the folder_and_docs, format_code,  and conf_code sections, one 
of two DocumentEntries are returned. So, the SQ results include the SS, the folder, one DocumentEntry, and Associations between:
	SS and Folder
	SS and DocumentEntry
	Folder and DocumentEntry
