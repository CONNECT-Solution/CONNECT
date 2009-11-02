R.b Accept Document Replace, Document in Folder

A Document Registry, when accepting a Document Replace, must find all
Folders the original document is a member of and add the replacement
document to those folders.

Steps are:

submit_doc_w_fol - submit a document in a folder

verify_submission -  Use GetSubmissionSetAndContents SQ to verify the
submission

rplc - replace the document from step submit_doc_w_fol

verify_folder_contents - verify both the original and the replacement
documents are members of the folder
