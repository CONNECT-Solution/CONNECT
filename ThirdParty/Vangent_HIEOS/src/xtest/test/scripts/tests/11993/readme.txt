R.b Accept Document Addendum

submit
	submit  - submit a document
	submit_copy - submit a second copy of the document

rplc
	rplc - replace document from step submit_copy

apnd
	apnd - add ammendment to document from step submit
	apnd_rplc - add ammendment to document from step submit_copy - must fail

eval
	validate_original - verify document from step submit has status Approved (apnd didn't change it)
	validate_apnd - verify document from step apnd is present and has status Approved
	no_validate_rplc_apnd - verify metadata from step apnd_rplc was not stored

