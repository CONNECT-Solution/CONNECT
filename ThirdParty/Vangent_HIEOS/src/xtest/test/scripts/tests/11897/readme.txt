SQ FindDocuments Stored Query

Stored Query must be run over SOAP 1.2.  

This testplan contains many many test steps each validating 
a feature of the FindDocuments stored query.  This test relies
on test 11890 to pre-load the Registry with known test data.

The test steps are:


leafclass: 
   queries for:
      all approved documents, DocE is deprecated so not returned
   returns LeafClass   
   must return: DocA, DocB, DocC, DocD, DocF

object_refs: 
   queries for:
      all approved documents, DocE is deprecated so not returned
   returns ObjectRefs   
   must return: DocA, DocB, DocC, DocD, DocF

no_matching_classcode:
   queries for class code not contained in test data
   must return: none

classcode_one: 
   queries for: 
      classCode = 'Consult'
      status = 'Approved'
   must return: DocD

classcode_two: 
   queries for: 
      classCode = 'Consult' or 'History and Physical'
      status = 'Approved'
   must return: DocD, DocE, DocA

classcode_scheme: 
   queries for:
     classCode = 'Communication'
     classCodeScheme = 'Connect-a-thon classCodes'
     status = 'Approved'
   must return: DocB

classcode_scheme_2: 
   queries for:
     classCode = ('Communication', 'Communication')
     classCodeScheme = ('Connect-a-thon classCodes','Connect-a-thon classCodes 2')
     status = 'Approved'
   must return: DocB, DocC

classcode_scheme_mismatch:
  queries for:
      classCode = 'Communication'
      classCodeScheme = ('Connect-a-thon classCodes', 'a') 
      status = 'Approved'
  must return: none (mismatch on codes and scheme)

practicesetting:
  queries for:
    practiceSettingCode = 'Dialysis'
    status = 'Approved'
  must return: DocA, DocF, DocD

practicesetting_scheme:
  queries for:
    practiceSettingCode = 'Dialysis'
    practiceSettingCodeScheme = 'Connect-a-thon practiceSettingCodes'
    status = 'Approved'
  must return: DocA, DocD

practicesetting_two:
  contains duplicate $XDSDocumentEntryPracticeSettingCode
  must return: error

classcode_practicesetting:
  queries for:
    classCode = 'Communication'
    practiceSettingCode = 'Cardiology'
    status = 'Approved'
  must return: DocB

creationtime_between:
  queries for:
    creationTimeFrom: 20040101
    creationTimeTo:  20050101
    status = 'Approved'
  must return: DocB

creationtime_left_edge:
  queries for:
    creationTimeFrom: 20041224
    creationTimeTo:  20050101
    status = 'Approved'
  must return: DocB

creationtime_right_edge:
  queries for:
    creationTimeFrom: 20041124
    creationTimeTo:  20041224
    status = 'Approved'
  must return: none

creationtime_practicesetting:
  queries for: 
    creation time:  20020101 thru 20060101
    practiceSettingCode: 'Dialysis'
    status = 'Approved'
  must return: DocD

servicestarttime:
  queries for:
    serviceStartTimeFrom: 2005
    serviceStartTimeTo: 2006
    status = 'Approved' 
  must return: DocC, DocD

servicestoptime:
  queries for:
    serviceStopTimeFrom: 2005
    serviceStopTimeTo: 2006
    status = 'Approved' 
  must return: DocC, DocD

hcftc:
  queries for:
    healthcareFacilty: Outpatient
    status: 'Approved'
  must return: DocA, DocF, DocD

hcftc_scheme:
  queries for:
    healthcareFacilty: Outpatient
    healthcareFaciltyScheme: 'Connect-a-thon healthcareFacilityTypeCodes'
    status: 'Approved'
  must return: DocF, DocD

eventcode:
  queries for:
    eventCodeList = 'Colonoscopy'
    status = 'Approved'
  must return: DocB

eventcode_scheme:
  queries for:
    eventCodeList = 'Colonoscopy'
    eventCodeList = 'Connect-a-thon eventCodeList'
    status = 'Approved'
  must return: DocB

confcode:
  queries for:
    confidentialityCode = ('1.3.6.1.4.1.21367.2006.7.101',
                                    '1.3.6.1.4.1.21367.2006.7.103')
    status = 'Approved'
  must return: DocB, DocC

formatcode:
  queries for:
    formatCode = 'CDAR2/IHE 1.0'
    status = 'Approved'
  must return: DocA, DocB, DocF, DocC

deprecated:
  queries for:
    status = 'Deprecated'
  must return: DocE
