package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.policyengine.adapterpip.PatientConsentManager;
import gov.hhs.fha.nhinc.policyengine.adapterpip.QueryUtil;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class DocRetrieveResponseProcessor {

    private Log log = null;
    private String documentId;
    private String homeCommunityId;
    private String repositoryId;
    private static final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";

    /**
     * Constructor
     */
    public DocRetrieveResponseProcessor() {
        log = createLogger();
    }

    /**
     * logger creation called during DocRetrieveResponseProcessor creation
     * @return Log
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * This method filters the Document Retrieve results based on the Patient Preferences and creates a new Response to return
     * @param retrieveRequest
     * @param retrieveResponse
     * @return RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetReults(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse) {
        RetrieveDocumentSetResponseType response = null;
        if (null != retrieveResponse &&
                null != retrieveResponse.getDocumentResponse() &&
                retrieveResponse.getDocumentResponse().size() > 0) {
            PatientPreferencesType ptPreferences = null;
            response = new RetrieveDocumentSetResponseType();
            for (DocumentResponse eachResponse : retrieveResponse.getDocumentResponse()) {
                if (null != eachResponse) {
                    extractIdentifiers(eachResponse);
                    ptPreferences = getPatientConsentHelper().retrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId);
                    if (filterResults(eachResponse, ptPreferences)) {
                        response.getDocumentResponse().add(eachResponse);
                    }
                }
            }
        }
        return response;
    }

    /**
     * The 3 main identifiers used for Document Retrieve are extracted here for each Document Response and used for further filtering
     * @param retrieveResponse
     */
    protected void extractIdentifiers(DocumentResponse retrieveResponse)
    {
        if (null != retrieveResponse)
        {
            documentId = retrieveResponse.getDocumentUniqueId();
            homeCommunityId = retrieveResponse.getHomeCommunityId();
            repositoryId = retrieveResponse.getRepositoryUniqueId();
        } else {
            log.warn("Document Response is null");
        }
    }

    /**
     * This method filters the results for each Document Retrieve Response using Patient Preferences Document Type
     * @param retrieveResponse
     * @param patientPreferences
     * @return boolean
     */
    protected boolean filterResults(DocumentResponse retrieveResponse, PatientPreferencesType patientPreferences)
    {
        AdhocQueryResponse oResponse = null;
        String sDocTypeFromDocQueryResults = "";
        boolean resultFlag = false;
        if(null == retrieveResponse)
        {
            log.error("Unable to filter results retrieveResponse was null");
            return resultFlag;
        }
        if(null == patientPreferences)
        {
            log.error("Unable to filter results Patient Preferences was null");
            return resultFlag;
        }
        if(null != patientPreferences.getAssigningAuthority() &&
                    null != patientPreferences.getPatientId())
        {
            try {
                AdhocQueryRequest oRequest = createAdhocQueryRequest(patientPreferences.getPatientId(), patientPreferences.getAssigningAuthority());
                oResponse = getAdhocQueryResponse(oRequest);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            if (null != oResponse) {
                sDocTypeFromDocQueryResults = extractDocTypeFromDocQueryResults(oResponse, retrieveResponse);
            }
            if (null != sDocTypeFromDocQueryResults) {
                resultFlag = isDocTypeFound(sDocTypeFromDocQueryResults, patientPreferences);
            }
        }
        return resultFlag;
    }

    /**
     * This method returns true if the documentType from Document Retrieve results
     * @param sDocTypeResult
     * @param patientPreferences
     * @return boolean
     */
    protected boolean isDocTypeFound(String sDocTypeResult, PatientPreferencesType patientPreferences)
    {
        return getPatientConsentHelper().extractDocTypeFromPatPref(sDocTypeResult, patientPreferences);
    }
    
    /**
     * This method returns AdhocQueryResponse for a AdhocQueryRequest
     * @param oRequest
     * @return AdhocQueryResponse
     * @throws Exception
     */
    protected AdhocQueryResponse getAdhocQueryResponse(AdhocQueryRequest oRequest) throws Exception
    {
        return getDocumentRegistryPort().documentRegistryRegistryStoredQuery(oRequest);
    }
    
    /**
     * Adhoc Query Request is created based on the Patient Id and Assigning Authority
     * @param sPatId
     * @param sAA
     * @return AdhocQueryRequest
     */
    protected AdhocQueryRequest createAdhocQueryRequest(String sPatId, String sAA) {
        AdhocQueryRequest request = null;
        try {
            if (null != sPatId &&
                    null != sAA &&
                    !sPatId.equals("") &&
                    !sAA.equals("")) {
                QueryUtil util = new QueryUtil();
                request = util.createAdhocQueryRequest(sPatId, sAA);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return request;
    }

    /**
     * This method is the Super method for all other methods to extract Doc Type from AdhocQuery Responses for each matching DocRetrieve Response
     * @param oResponse
     * @param docResponse
     * @return String
     */
    protected String extractDocTypeFromDocQueryResults(AdhocQueryResponse oResponse, DocumentResponse docResponse)
    {
        String docType = null;
        ExtrinsicObjectType match = null;
        if (null != oResponse &&
                null != docResponse &&
                null != oResponse.getRegistryObjectList())
        {
            List<JAXBElement<? extends IdentifiableType>> objectList =
                    oResponse.getRegistryObjectList().getIdentifiable();
            ExtrinsicObjectType docExtrinsic = null;
            for (JAXBElement<? extends IdentifiableType> object : objectList)
            {
                IdentifiableType identifiableType = object.getValue();

                if (identifiableType instanceof ExtrinsicObjectType)
                {
                    docExtrinsic = (ExtrinsicObjectType) identifiableType;
                    List<ExternalIdentifierType> externalIdentifers = docExtrinsic.getExternalIdentifier();
                    if (null != externalIdentifers)
                    {
                        String uniqueIdIdentifier = getUniqueIdIdentifier(externalIdentifers);
                        if (null != uniqueIdIdentifier &&
                                uniqueIdIdentifier.equals(docResponse.getDocumentUniqueId()))
                        {
                            match = docExtrinsic;
                            break;
                        }
                    }
                }
            }

            if (null != match) {
                docType = extractDocTypeFromMetaData(match);
            }

        }
        return docType;
    }

    /**
     * This method extracts Document Type from AdhocQuery Metadata for identified Extrinsic Object for a particular Document
     * @param documentMetaData
     * @return String
     */
    protected String extractDocTypeFromMetaData(ExtrinsicObjectType documentMetaData) {
        String value = null;
        if (null != documentMetaData &&
                null != documentMetaData.getClassification() &&
                documentMetaData.getClassification().size() > 0) {
            List<ClassificationType> classificationList = documentMetaData.getClassification();
            for (ClassificationType classification : classificationList) {
                if (null != classification &&
                        null != classification.getClassificationScheme() &&
                        classification.getClassificationScheme().contentEquals(EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME)) {
                    value = parseInternationalType(classification.getName());
                }
            }
        }
        return value;
    }

    /**
     * This method returns the value of Classification Code Schema from International String Type
     * @param str
     * @return String
     */
    protected String parseInternationalType(InternationalStringType str) {
        String value = null;
        if (null != str) {
            List<LocalizedStringType> localStr = str.getLocalizedString();
            if (null != localStr &&
                    localStr.size() > 0 &&
                    null != localStr.get(0)) {
                value = localStr.get(0).getValue();
            }
        }
        return value;
    }

    /**
     * This method returns Document Unique Id from ExternalIdentifier Object
     * @param externalIdentifierList
     * @return String String
     */
    protected String getUniqueIdIdentifier(List<ExternalIdentifierType> externalIdentifierList) {
        String aUniqueIdIdentifier = null;
        if (null != externalIdentifierList &&
                externalIdentifierList.size() > 0) {
            for (ExternalIdentifierType externalIdentifier : externalIdentifierList) {
                if (null != externalIdentifier &&
                        null != externalIdentifier.getIdentificationScheme() &&
                        externalIdentifier.getIdentificationScheme().contentEquals(EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME)) {
                    aUniqueIdIdentifier = externalIdentifier.getValue();
                }
            }
        }
        return aUniqueIdIdentifier;
    }

    /**
     * Return a handle to the document registry port.
     *
     * @return The handle to the document registry port web service.
     */
    protected DocumentRegistryPortType getDocumentRegistryPort()
            throws Exception
    {
        return getPatientConsentManager().getDocumentRegistryPort();
    }

    /**
     * This method returns PatientConsentManager instance
     * @return PatientConsentManager
     */
    protected PatientConsentManager getPatientConsentManager()
    {
        return new PatientConsentManager();
    }

    /**
     * This method returns a PatientConsentHelper Object
     * @return PatientConsentHelper
     */
    protected PatientConsentHelper getPatientConsentHelper()
    {
        return new PatientConsentHelper();
    }
    
    /**
     * Returns Document Id of each Document Response
     * @return String
     */
    protected String getDocumentId() {
        return documentId;
    }

    /**
     * Returns Home Community Id of each Document Response
     * @return String
     */
    protected String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Returns Repository Id of each Document Response
     * @return String
     */
    protected String getRepositoryId() {
        return repositoryId;
    }
}
