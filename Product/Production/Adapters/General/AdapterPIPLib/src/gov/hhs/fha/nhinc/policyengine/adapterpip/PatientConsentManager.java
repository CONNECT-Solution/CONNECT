package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;

/**
 * This class manages the patient consent form.  It stores or retrieves the patient
 * consent document from the repository.
 *
 * @author Les Westberg
 */
public class PatientConsentManager
{
    private static Log log = LogFactory.getLog(PatientConsentManager.class);
    private static JAXBContext oJaxbContextHL7 = null;
    private static Marshaller oHL7Marshaller = null;
    private static Unmarshaller oHL7Unmarshaller = null;
    private static DocumentRegistryService oDocRegService = null;
    private static DocumentRepositoryService oDocRepService = null;
    private static final String ADAPTER_PROPFILE_NAME = "adapter";
    private static final String XDS_HC_VALUE = "XDSbHomeCommunityId";
    
    static
    {
        try
        {
            oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            oHL7Marshaller = oJaxbContextHL7.createMarshaller();
            oHL7Unmarshaller = oJaxbContextHL7.createUnmarshaller();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to create HL7 JAXB Context and Marshaller.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    /**
     * Return a handle to the document registry port.
     *
     * @return The handle to the document registry port web service.
     */
    private DocumentRegistryPortType getDocumentRegistryPort()
        throws AdapterPIPException
    {
        DocumentRegistryPortType oDocRegistryPort = null;

        try
        {
            if (oDocRegService == null)
            {
                oDocRegService = new DocumentRegistryService();
            }

            oDocRegistryPort = oDocRegService.getDocumentRegistryPortSoap();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = "";
            String xdsHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPFILE_NAME, XDS_HC_VALUE);
            if(xdsHomeCommunityId!=null &&
                    !xdsHomeCommunityId.equals(""))
            {
                sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(xdsHomeCommunityId, CDAConstants.DOC_REGISTRY_SERVICE_NAME);
            } else {
                sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(CDAConstants.DOC_REGISTRY_SERVICE_NAME);
            }
            

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = CDAConstants.DOC_REGISTRY_SERVICE_DEFAULT_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       CDAConstants.DOC_REGISTRY_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
            ((javax.xml.ws.BindingProvider)oDocRegistryPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Document Registry web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oDocRegistryPort;
    }

    /**
     * Return a handle to the document repository port.
     *
     * @return The handle to the document registry port web service.
     */
    private DocumentRepositoryPortType getDocumentRepositoryPort()
        throws AdapterPIPException
    {
        DocumentRepositoryPortType oDocRepositoryPort = null;

        try
        {
            if (oDocRepService == null)
            {
                oDocRepService = new DocumentRepositoryService();
            }

            oDocRepositoryPort = oDocRepService.getDocumentRepositoryPortSoap();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = "";
            String xdsHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPFILE_NAME, XDS_HC_VALUE);
            if(xdsHomeCommunityId!=null &&
                    !xdsHomeCommunityId.equals(""))
            {
                sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(xdsHomeCommunityId, CDAConstants.DOC_REPOSITORY_SERVICE_NAME);
            } else {
                sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(CDAConstants.DOC_REPOSITORY_SERVICE_NAME);
            }


            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = CDAConstants.DOC_REPOSITORY_SERVICE_DEFAULT_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       CDAConstants.DOC_REPOSITORY_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
            ((javax.xml.ws.BindingProvider)oDocRepositoryPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Document Repository web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oDocRepositoryPort;
    }





//    /**
//     * This method fills in the structured portion of the CDA document.  It was used in CONNECT
//     * Release 2.1
//     *
//     * @param oDOMDocument The DOM Document to create elements to.
//     * @param bOptIn TRUE if the user has opted in.
//     * @return The structured body node to be placed in the CDA document.
//     */
//    private POCDMT000040StructuredBody fillInCDAStructuredBody(Document oDOMDocument, boolean bOptIn)
//    {
//        // Structured Body
//        //----------------
//        POCDMT000040StructuredBody oStructuredBody = new POCDMT000040StructuredBody();
//
//        // Structured Body Component
//        //---------------------------
//        POCDMT000040Component3 oSBComponent = new POCDMT000040Component3();
//        oStructuredBody.getComponent().add(oSBComponent);
//
//        // Section
//        //---------
//        POCDMT000040Section oSection = new POCDMT000040Section();
//        oSBComponent.setSection(oSection);
//
//        // Section Code
//        //-------------
//        CE oCode = new CE();
//        oSection.setCode(oCode);
//        if (bOptIn)
//        {
//            oCode.setCode(CDAConstants.CONSENT_CODE_YES);
//            oCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
//        }
//        else
//        {
//            oCode.setCode(CDAConstants.CONSENT_CODE_NO);
//            oCode.setDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
//        }
//        oCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
//        oCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);
//
//        // Section Title
//        //---------------
//        Element oSectionTitle = null;
//        oSectionTitle = oDOMDocument.createElement(CDAConstants.TITLE_TAG);
//        if (bOptIn)
//        {
//            oSectionTitle.setTextContent(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
//        }
//        else
//        {
//            oSectionTitle.setTextContent(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
//        }
//        oSection.setTitle(oSectionTitle);
//
//        // Section Text
//        //-------------
//        Element oSectionText = oDOMDocument.createElement(CDAConstants.TEXT_TAG);
//        if (bOptIn)
//        {
//            oSectionText.setTextContent(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
//        }
//        else
//        {
//            oSectionText.setTextContent(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
//        }
//        oSection.setText(oSectionText);
//
//        // Section Entry
//        //--------------
//        POCDMT000040Entry oSectionEntry = new POCDMT000040Entry();
//        oSection.getEntry().add(oSectionEntry);
//
//        // Entry Act
//        //-----------
//        POCDMT000040Act oEntryAct = new POCDMT000040Act();
//        oSectionEntry.setAct(oEntryAct);
//
//        // Entry Act Class & Mood Code
//        //----------------------------
//        oEntryAct.setClassCode(XActClassDocumentEntryAct.ACT);
//        oEntryAct.setMoodCode(XDocumentActMood.EVN);
//
//        // Entry Act Code
//        //----------------
//        oCode = new CE();
//        oEntryAct.setCode(oCode);
//        if (bOptIn)
//        {
//            oCode.setCode(CDAConstants.CONSENT_CODE_YES);
//            oCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
//        }
//        else
//        {
//            oCode.setCode(CDAConstants.CONSENT_CODE_NO);
//            oCode.setDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
//        }
//        oCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
//        oCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);
//
//        return oStructuredBody;
//    }


    /**
     * This method takes in an object representation of the clinical document
     * and serializes it to a text string representation of the document.
     *
     * @param oPrefCDA The object representation of the clinical document.
     * @return The textual string representation of the clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    private String serializeConsentCDADoc(POCDMT000040ClinicalDocument oPrefCDA)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextHL7 == null)
            {
                oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            }

            if (oHL7Marshaller == null)
            {
                oHL7Marshaller = oJaxbContextHL7.createMarshaller();
            }

            StringWriter swXML = new StringWriter();

            org.hl7.v3.ObjectFactory oHL7ObjectFactory = new org.hl7.v3.ObjectFactory();
            JAXBElement oJaxbElement = oHL7ObjectFactory.createClinicalDocument(oPrefCDA);

            oHL7Marshaller.marshal(oJaxbElement, swXML);
            sPrefCDA = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the CDA document to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sPrefCDA;
    }


    /**
     * This method takes a string version of the Patient Pref document and
     * creates the JAXB object version of the same document.
     *
     * @param sPrefCDA The string version of the patient preference CDA document.
     * @return The JAXB object version of the patient preferences CDA document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    private POCDMT000040ClinicalDocument deserializeConsentCDADoc(String sPrefCDA)
        throws AdapterPIPException
    {
        POCDMT000040ClinicalDocument oPrefCDA = null;

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextHL7 == null)
            {
                oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            }

            if (oHL7Unmarshaller == null)
            {
                oHL7Unmarshaller = oJaxbContextHL7.createUnmarshaller();
            }

            StringReader srXML = new StringReader(sPrefCDA);

            JAXBElement oJAXBElementPrefCDA = (JAXBElement) oHL7Unmarshaller.unmarshal(srXML);
            if (oJAXBElementPrefCDA.getValue() instanceof POCDMT000040ClinicalDocument)
            {
                oPrefCDA = (POCDMT000040ClinicalDocument) oJAXBElementPrefCDA.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the CDA string: " + sPrefCDA + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oPrefCDA;
    }


    /**
     * This operation returns the Patient CPP document in XML form that it
     * extracts from the Response.
     *
     * @param oResponse The response that was recieved from the repository.
     * @return The XML form of the patient CPP.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there are any errors.
     */
    private String extractPrefCDA(RetrieveDocumentSetResponseType oResponse)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        if ((oResponse != null) &&
            (oResponse.getDocumentResponse() != null) &&
            (oResponse.getDocumentResponse().size() > 0))
        {
            // A patient should only have one document - so if there are more than one, we
            // will use the first one...
            //-----------------------------------------------------------------------------
            DocumentResponse oDocResponse = oResponse.getDocumentResponse().get(0);
            if ((oDocResponse.getDocument() != null) &&
                (oDocResponse.getDocument().length > 0))
            {
                sPrefCDA = new String(oDocResponse.getDocument());
            }
        }   // if ((oResponse != null) &&

        return sPrefCDA;
    }

    /**
     * This method retrieves the document identifier information from the
     * repository for the Consumer Preferences document for this patient.
     *
     * @param sPatientId The patient ID of the patient.
     * @param sAssigningAuthority The assigning authority
     * @return The document identifiers for the CPP document
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if any problem occurs getting the data.
     */
    public DocumentRequest retrieveCPPDocIdentifiers(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        QueryUtil queryUtil = new QueryUtil();
        DocumentRequest oDocRequest = new DocumentRequest();

        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = queryUtil.createAdhocQueryRequest(sPatientId, sAssigningAuthority);

        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);

        oDocRequest = queryUtil.createDocumentRequest(oResponse);
        
        return oDocRequest;
    }

    /**
     * This method takes the document identifiers for the CPP document and
     * retrieves the document from the repository.
     *
     * @param oDocRequest The document identifiers.
     * @return The XML CPP document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there are any errors.
     */
    private String retrieveCPPDoc(DocumentRequest oDocRequest)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        DocumentRepositoryPortType oDocRepositoryPort = getDocumentRepositoryPort();

        RetrieveDocumentSetRequestType oRequest = new RetrieveDocumentSetRequestType();
        oRequest.getDocumentRequest().add(oDocRequest);

        RetrieveDocumentSetResponseType oResponse = null;
        oResponse = oDocRepositoryPort.documentRepositoryRetrieveDocumentSet(oRequest);

        sPrefCDA = extractPrefCDA(oResponse);

        return sPrefCDA;

    }

    /**
     * This operation retrieves the currently stored patient consent
     * information from the repository.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The Assigning authority
     * @return The document information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         The error if one occurs.
     */
    private CPPDocumentInfo retrieveCPPFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        CPPDocumentInfo oCPPDocInfo = new CPPDocumentInfo();


        DocumentRequest oDocRequest = retrieveCPPDocIdentifiers(sPatientId, sAssigningAuthority);

        if (oDocRequest != null)
        {
            if (oDocRequest.getHomeCommunityId() != null)
            {
                oCPPDocInfo.sHomeCommunityId = oDocRequest.getHomeCommunityId();
            }

            if (oDocRequest.getRepositoryUniqueId() != null)
            {
                oCPPDocInfo.sRepositoryId = oDocRequest.getRepositoryUniqueId();
            }

            if (oDocRequest.getDocumentUniqueId() != null)
            {
                oCPPDocInfo.sDocumentUniqueId = oDocRequest.getDocumentUniqueId();
            }

            String sPrefCDA = retrieveCPPDoc(oDocRequest);

            if ((sPrefCDA != null) &&
                (sPrefCDA.length() > 0))
            {
                oCPPDocInfo.sPrefCDA = sPrefCDA;

                POCDMT000040ClinicalDocument oPrefCDA = deserializeConsentCDADoc(sPrefCDA);
                oCPPDocInfo.oPrefCDA = oPrefCDA;
            }
        }

        return oCPPDocInfo;
    }

    
//    /**
//     * This operation retrieves the currently stored patient consent
//     * information from the repository.
//     *
//     * @param sPatientId The ID of the patient.
//     * @return The document information.
//     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
//     *         The error if one occurs.
//     */
//    private CPPDocumentInfo retrieveCPPFromRepositoryUsingDocumentService(String sPatientId)
//        throws AdapterPIPException
//    {
//        CPPDocumentInfo oCPPDocInfo = new CPPDocumentInfo();
//
//        gov.hhs.fha.nhinc.repository.model.Document oDoc = new gov.hhs.fha.nhinc.repository.model.Document();
//        DocumentService oDocService = new DocumentService();
//        DocumentQueryParams oParams = new DocumentQueryParams();
//
//
//        LinkedList<String> oaClassCode = new LinkedList<String>();
//        List<gov.hhs.fha.nhinc.repository.model.Document> oDocs;
//
//        oaClassCode.add(CDAConstants.METADATA_CLASS_CODE);
//
//        oParams.setClassCodes(oaClassCode);
//        oParams.setPatientId(sPatientId);
//
//        oDocs = oDocService.documentQuery(oParams);
//
//        // We should only have one document.  If we found more than one, throw
//        // an exception.
//        //---------------------------------------------------------------------
//        if (oDocs.size() == 1)
//        {
//            oDoc = oDocs.get(0);
//
//            log.debug("Retrieved patient preferences for patient: " + sPatientId +
//                      "Document: " + new String(oDoc.getRawData()));
//        }
//        else if (oDocs.size() > 1)
//        {
//            String sErrorMessage = "Found more than one Consent document in the repository.  " +
//                                   "There should have been only 1.";
//            log.error(sErrorMessage);
//            throw new AdapterPIPException(sErrorMessage);
//        }
//
//        if (oDoc != null)
//        {
//            if (oDoc.getDocumentid() != null)
//            {
//                oCPPDocInfo.lDocumentId = oDoc.getDocumentid();
//            }
//
//            if (oDoc.getDocumentUniqueId() != null)
//            {
//                oCPPDocInfo.sDocumentUniqueId = oDoc.getDocumentUniqueId();
//            }
//
//            if ((oDoc.getRawData() != null) &&
//                (oDoc.getRawData().length > 0))
//            {
//                oCPPDocInfo.sPrefCDA = new String(oDoc.getRawData());
//
//                oCPPDocInfo.oPrefCDA = deserializeConsentCDADoc(oCPPDocInfo.sPrefCDA);
//            }
//        }
//
//        return oCPPDocInfo;
//
//    }

     /**
     * This method stores the patient preference CDA document to the repository.
     *
     * @param oPtPref The patient preference information.
     * @param sPrefCDA The CDA document form of the patient preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if there is an error.
     */
    private void storeCPPToRepositoryUsingXDSb(PatientPreferencesType oPtPref, String sPrefCDA)
        throws AdapterPIPException, PropertyAccessException
    {
        log.info("------ Begin PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
        if ((oPtPref == null) ||
            (oPtPref.getPatientId() == null) ||
            (oPtPref.getPatientId().trim().length() <= 0))
        {
            String sErrorMessage = "failed to store patient consent.  The patient ID was null or blank.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }
        
        saveCPPDoc(sPrefCDA, oPtPref);
        log.info("------ End PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
    }

//    /**
//     * This method stores the patient preference CDA document to the repository.
//     *
//     * @param oPtPref The patient preference information.
//     * @param sPrefCDA The CDA document form of the patient preferences.
//     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
//     *         This exception is thrown if there is an error.
//     */
//    private void storeCPPToRepository(PatientPreferencesType oPtPref, String sPrefCDA)
//        throws AdapterPIPException
//    {
//        // Note that right now we are storing using the Document repository API
//        // As soon as the XDS.b document storage operations are completed,
//        // this code should be changed to call that web service.
//        //----------------------------------------------------------------------
//        if ((oPtPref == null) ||
//            (oPtPref.getPatientId() == null) ||
//            (oPtPref.getPatientId().trim().length() <= 0))
//        {
//            String sErrorMessage = "failed to store patient consent.  The patient ID was null or blank.";
//            log.error(sErrorMessage);
//            throw new AdapterPIPException(sErrorMessage);
//        }
//
//        CPPDocumentInfo oCPPDocInfo = retrieveCPPFromRepositoryUsingDocumentService(oPtPref.getPatientId());
//
//        gov.hhs.fha.nhinc.repository.model.Document oDoc = new gov.hhs.fha.nhinc.repository.model.Document();
//
//        // Document ID - If it exists - then the previous one will be overwritten
//        //-----------------------------------------------------------------------
//        if ((oCPPDocInfo != null) &&
//            (oCPPDocInfo.lDocumentId > 0))
//        {
//            oDoc.setDocumentid(oCPPDocInfo.lDocumentId);
//        }
//
//        // Class Code
//        //-----------
//        oDoc.setClassCode(CDAConstants.METADATA_CLASS_CODE);
//        oDoc.setClassCodeDisplayName(CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
//
//        // Event Code
//        //-----------
//        HashSet<EventCode> oaEventCode = new HashSet<EventCode>();
//        EventCode oEventCode = new EventCode();
//        oaEventCode.add(oEventCode);
//        oEventCode.setDocument(oDoc);       // This must be set for foreign key
//        if (oPtPref.isOptIn())
//        {
//            oEventCode.setEventCode(CDAConstants.CONSENT_CODE_YES);
//            oEventCode.setEventCodeDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
//        }
//        else
//        {
//            oEventCode.setEventCode(CDAConstants.CONSENT_CODE_NO);
//            oEventCode.setEventCodeDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
//        }
//        oEventCode.setEventCodeScheme(CDAConstants.SNOMED_CT_CODE_SYSTEM);
//        oDoc.setEventCodes(oaEventCode);
//
//        // Format Code
//        //-------------
//        oDoc.setFormatCode(CDAConstants.METADATA_FORMAT_CODE);
//        oDoc.setFormatCodeScheme(CDAConstants.METADATA_FORMAT_CODE_SYSTEM);
//
//        // CDA Consent XML
//        //-----------------
//        oDoc.setRawData(sPrefCDA.getBytes());
//
//        // Document Title
//        //---------------
//        oDoc.setDocumentTitle(CDAConstants.TITLE);
//
//        // Patient ID
//        //------------
//        oDoc.setPatientId(oPtPref.getPatientId());
//
//        // Unique ID - Note that this Unique ID would normally be set by
//        // the repository (I assume).  If that is the case then we need to
//        // allow it to create this..  Right now we will make one up...  (This
//        // may be an issue with the XDS.b interface.
//        //--------------------------------------------------------------------
//        oDoc.setDocumentUniqueId(oPtPref.getPatientId()+'-'+CDAConstants.METADATA_CLASS_CODE);
//
//        // Status Code
//        //-------------
//        oDoc.setStatus(CDAConstants.STATUS_APPROVED_STORE_VALUE);
//
//        DocumentService oDocService = new DocumentService();
//        oDocService.saveDocument(oDoc);
//
//        log.debug("Stored CPP to repository for patient ID: " + oPtPref.getPatientId() +
//                  "Document: " + sPrefCDA);
//
//    }

    /**
     * This method takes the XML Patient preference CDA document and it populates
     * the PtPref with the information it finds in it.
     *
     * @param oDocInfo  The CPP document information.
     * @return The patient prefernces from the document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    private PatientPreferencesType populateConsentInfo(CPPDocumentInfo oDocInfo)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        // if we do not have an XML string, then set it to opt out and get out of here.
        //-----------------------------------------------------------------------------
        if ((oDocInfo == null) ||
            (oDocInfo.oPrefCDA == null))
        {
            oPtPref.setOptIn(false);
            return oPtPref;         // Get out of here...  Nothing more to do
        }

        POCDMT000040ClinicalDocument oPrefCDA = oDocInfo.oPrefCDA;

        if ((oPrefCDA != null) &&
            (oPrefCDA.getComponent() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent().length() > 0))
        {
            String sConsentXACML = StringUtil.unwrapCdata(oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent());

            if ((sConsentXACML != null) &&
                (sConsentXACML.trim().length() > 0))
            {
                XACMLSerializer oSerializer = new XACMLSerializer();
                PolicyType oConsentXACML = oSerializer.deserializeConsentXACMLDoc(sConsentXACML);
                if (oConsentXACML != null)
                {
                    XACMLExtractor oExtractor = new XACMLExtractor();
                    oPtPref = oExtractor.extractPatientPreferences(oConsentXACML);
                }   // if (oConsentXACML != null)
            }   // if ((sConsentXACML != null) &&
        }   // if ((oPrefCDA != null) &&
        

        return oPtPref;
    }

    /**
     * This method takes the given Document ID and does a query against the repository
     * to find out the patient ID for this patient.  It returns that patient ID.
     *
     * @param sDocumentUniqueId The document ID of an existing document in the repository.
     * @param sRepositoryId The repository ID of the repository where the document is stored.
     * @return The patient ID of the patient.
     * @throws AdapterPIPException - This is thrown if there is an error.
     */
    private String retrievePtIdFromDocumentId(String sDocumentUniqueId, String sRepositoryId)
        throws AdapterPIPException
    {
        String sPatientId = "";

        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = new QueryUtil().createPatientIdQuery(sDocumentUniqueId, sRepositoryId);

        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);

        sPatientId = new QueryUtil().extractPatientId(oResponse);

        return sPatientId;
    }

    /**
     * This method saves the patient consent information to the document repository.
     * It will overwrite anything that is currently there.
     *
     * @param oPtPref The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This
     *         exception is thrown if there is an error storing.
     */
    public void storePatientConsent(PatientPreferencesType oPtPref)
        throws AdapterPIPException, PropertyAccessException
    {
        POCDMT000040ClinicalDocument oPrefCDA = null;
        PolicyType oConsentXACML = null;

        XACMLCreator oCreator = new XACMLCreator();
        oConsentXACML = oCreator.createConsentXACMLDoc(oPtPref);

        XACMLSerializer oSerializer = new XACMLSerializer();
        String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);
        
        oPrefCDA = new ConsentDocumentCreator().createConsentCDADoc(oPtPref, sConsentXACML);
        String sPrefCDA = serializeConsentCDADoc(oPrefCDA);

        //Uses Repository Services
        storeCPPToRepositoryUsingXDSb(oPtPref, sPrefCDA);
        //storeCPPToRepository(oPtPref, sPrefCDA);

    }

    /**
     * This method retrieves the patient consent information from the repository
     * based on patient Id.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The assigning authority associated with the patient ID.
     *                            Currently it is not really used, but here if it is needed.
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if there are any issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByPatientId(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sPatientId == null) ||
            (sPatientId.trim().length() <= 0))
        {
            String sErrorMessage = "Failed to retrieve patient consent.  The patient ID was either null or an empty string.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        CPPDocumentInfo oDocInfo = retrieveCPPFromRepositoryUsingXDSb(sPatientId, sAssigningAuthority);

        if (oDocInfo != null)
        {
            oPtPref = populateConsentInfo(oDocInfo);
        }

        return oPtPref;
    }

    /**
     * This method retrieves the patient consent information from the repository
     * based on a document Id.  It does this by first using the document ID to
     * retrieve the patient ID.  Then it uses the patient ID to retrieve the consent
     * information.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The assigning authority associated with the patient ID.
     *                            Currently it is not really used, but here if it is needed.
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if there are any issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByDocId(String sHomeCommunityId,
                                                                String sRepositoryId,
                                                                String sDocumentUniqueId)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sDocumentUniqueId == null) ||
            (sDocumentUniqueId.trim().length() <= 0))
        {
            String sErrorMessage = "Failed to retrieve patient consent.  The document unique ID was either null or an empty string.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        String sPatientId = retrievePtIdFromDocumentId(sDocumentUniqueId, sRepositoryId);

        if ((sPatientId != null) &&
            (sPatientId.length() >= 0))
        {
            oPtPref = retrievePatientConsentByPatientId(sPatientId, "");
        }

        return oPtPref;
    }


    /**
     * This class is an inner class used to hold the document along with its
     * set of identifiers.  For internal purposes.
     */
    private class CPPDocumentInfo
    {
        long lDocumentId = 0;           // note this is only used by our internal document repository service
        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentUniqueId = "";
        String sPrefCDA = "";
        POCDMT000040ClinicalDocument oPrefCDA = null;
    }

    /**
     * 
     * @param sPrefCDA
     * @param oPtPref
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    private void saveCPPDoc(String sPrefCDA, PatientPreferencesType oPtPref)
            throws AdapterPIPException, PropertyAccessException
    {
       log.info("------ Begin PatientConsentManager.saveCPPDoc ------");
       PatientConsentDocumentBuilderHelper oPatConsentDocBuilderHelper = new PatientConsentDocumentBuilderHelper();
       DocumentRepositoryPortType oDocRepositoryPort = getDocumentRepositoryPort();
       String sHomeCommunityId = PropertyAccessor.getProperty("gateway", "localHomeCommunityId");
       // Should the document unique id be pulled from the policyOID field?
       String sDocumentUniqueId = oPatConsentDocBuilderHelper.getOidFromProperty("documentuniqueid");
       ProvideAndRegisterDocumentSetRequestType oRequest = new ProvideAndRegisterDocumentSetRequestType();
       ProvideAndRegisterDocumentSetRequestType.Document oDoc = createDocumentRawData(sPrefCDA, sDocumentUniqueId);
       oRequest.getDocument().add(oDoc);
       String sTargetObject = checkCPPMetaFromRepositoryUsingXDSb(oPtPref.getPatientId(), oPtPref.getAssigningAuthority());
       SubmitObjectsRequest oSubmitObjectRequest = oPatConsentDocBuilderHelper.createSubmitObjectRequest(sTargetObject, sHomeCommunityId, sDocumentUniqueId, oPtPref);
       oRequest.setSubmitObjectsRequest(oSubmitObjectRequest);
       RegistryResponseType oRegistryResponse = oDocRepositoryPort.documentRepositoryProvideAndRegisterDocumentSetB(oRequest);
       if(oRegistryResponse != null &&
               oRegistryResponse.getStatus() != null &&
               !oRegistryResponse.getStatus().equals(""))
       {
           log.info("Patient Consent Document saved to repository Successfully");
       }
       else
       {
           throw new AdapterPIPException("Unable to Save patient Consent Document"+oRegistryResponse.getRegistryErrorList().getRegistryError().get(0).getValue());
       }
       log.info("------ End PatientConsentManager.saveCPPDoc ------");
    }

    /**
     * This method is used internal by saveCPPDoc creates Document with rawData and document unique id
     * @param sPrefCDA
     * @param sDocUniqueId
     * @return ProvideAndRegisterDocumentSetRequestType.Document
     */
    private ProvideAndRegisterDocumentSetRequestType.Document createDocumentRawData(String sPrefCDA, String sDocUniqueId)
    {
        log.info("------ Begin PatientConsentManager.createDocumentRawData ------");
        ProvideAndRegisterDocumentSetRequestType.Document oDoc = new ProvideAndRegisterDocumentSetRequestType.Document();
        oDoc.setId(sDocUniqueId);
        oDoc.setValue(sPrefCDA.getBytes());
        log.info("------ End PatientConsentManager.createDocumentRawData ------");
        return oDoc;
    }
    
   /**
     * To verify a XAML Document already persists in database
     * @param sPatientId The Patient identifier
    *  @param sAssigningAuthority The assigning authority
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    public String checkCPPMetaFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority)
            throws AdapterPIPException
    {
        log.info("--------------- Begin checkCPPMetaFromRepositoryUsingXDSb ---------------");
        String sTargetObject = "";
        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = new QueryUtil().createAdhocQueryRequest(sPatientId, sAssigningAuthority);
        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);
        
        if(oResponse != null &&
                oResponse.getRegistryObjectList() != null &&
                oResponse.getRegistryObjectList().getIdentifiable() != null &&
                oResponse.getRegistryObjectList().getIdentifiable().size() > 0)
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    sTargetObject = oExtObj.getId();
                }
            }
        }
        return sTargetObject;
    }   
}
