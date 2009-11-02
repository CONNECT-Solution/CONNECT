R.b Document Resubmission

Test Purpose

 The creation of a document and the submission of a document are different events.  A document created within an EHR could be forwarded as reference to multiple parties.  A lab report is an example.  Each party could then submit this document as evidence to support an action taken.  Now, as a second issue, this report is based on the CDA format and the Content Profile requires that the XDSDocumentEntry.uniqueId be the value of attribute x in the CDA.  Now, if both parties are truly submitting the same document (hash matches) then this should not cause a conflict. They cannot allocate a new uniqueId for one of the copies because the Content Profile stipulates taking the value from the document header.  This test validates:

1) That resubmission does not generate an error
2) That an apparent resubmission (same uniqueId) but different hash (different content) is rejected
3) Both copies are available in query

submit
	Submit a Submission Set containing a single document

resubmit_doc
	Resubmit same with document having same XDSDocumentEntry.uniqueId and
XDSDocumentEntry.hash.  The Register must be successful.

resubmit_doc_diff_hash
	Resubmit again with document having a different value in 
XDSDocumentEntry.hash but same value in XDSDocumentEntry.uniqueId.  This must 
return errorCode XDSDuplicateUniqueIdInRegistry.
