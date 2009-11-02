R.a Accept Document Transformation

submit
	submit  - submit a document
	submit_copy - submit a second copy of the document

rplc
	rplc - replace document from step submit_copy

xfrm
	xfrm - add transformation to document from step submit
	xfrm_rplc - add transformation to document from step submit_copy - must fail

eval
	validate_original - verify document from step submit has status Approved (apnd didn't change it)
	validate_xfrm - verify document from step apnd is present and has status Approved
	no_validate_rplc_xfrm - verify metadata from step xfrm_rplc was not stored

