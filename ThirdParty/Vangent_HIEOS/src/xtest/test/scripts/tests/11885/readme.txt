R.a Duplicate Submission

Submit directory

submit - Submit a Submission Set containing a single document

resubmit_doc - Resubmit same with document having same XDSDocumentEntry.uniqueId and XDSDocumentEntry.hash.  The Register 
transaction must be successful.

resubmit_doc_diff_hash - Resubmit again with document having a 
different value in XDSDocumentEntry.hash but same value in XDSDocumentEntry.uniqueId.  This must return errorCode 
XDSNonIdenticalHash.


eval directory

Use SQ to validate registry contents.