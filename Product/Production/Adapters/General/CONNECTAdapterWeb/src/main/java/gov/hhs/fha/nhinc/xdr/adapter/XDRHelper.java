/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.nhinc.xdr.routing.RoutingObjectFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;


/**
 *
 * @author dunnek
 */
public class XDRHelper {
    private static Log log = null;

    public static String XDR_EC_XDSMissingDocument = "XDSMissingDocument";
    public static String XDR_EC_XDSMissingDocumentMetadata = "XDSMissingDocumentMetadata";
    public static String XDR_EC_XDSNonIdenticalHash = "XDSNonIdenticalHash";
    public static String XDR_EC_XDSRegistryDuplicateUniqueIdInMessage = "XDSRegistryDuplicateUniqueIdInMessage";
    public static String XDR_EC_XDSRegistryBusy = "XDSRegistryBusy";
    public static String XDR_EC_XDSRegistryMetadataError = "XDSRegistryMetadataError";
    public static String XDR_EC_XDSUnknownPatientId = "XDSUnknownPatientId";
    public static String XDR_EC_XDSPatientIdDoesNotMatch = "XDSPatientIdDoesNotMatch";

    public static final String XDS_RETRIEVE_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_AVAILABLILTY_STATUS_APPROVED = "Active";
    public static final String XDS_STATUS = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
    public static final String XDS_STATUS_ONLINE = "Online";
    public static final String XDS_STATUS_OFFLINE = "Offline";
    public static final String XDS_NAME = "Name";
    public static final String XDS_CLASSIFIED_OBJECT = "classifiedObject";      //this is the reference to the extrinsicObject/document element
    public static final String XDS_NODE_REPRESENTATION = "nodeRepresentation";  //this the actual code in a classification element
    public static final String XDS_CLASSIFICATION_ID = "id";                    //this is the id of the classification element
    public static final String XDS_DOCUMENT_UNIQUE_ID = "XDSDocumentEntry.uniqueId";
    public static final String XDS_PATIENT_ID = "XDSDocumentEntry.patientId";
    public static final String XDS_CREATION_TIME_SLOT = "creationTime";
    public static final String XDS_START_TIME_SLOT = "serviceStartTime";
    public static final String XDS_STOP_TIME_SLOT = "serviceStopTime";
    public static final String XDS_SOURCE_PATIENT_ID_SLOT = "sourcePatientId";
    public static final String XDS_SOURCE_PATIENT_INFO_SLOT = "sourcePatientInfo";
    public static final String XDS_AUTHOR_PERSON_SLOT = "authorPerson";
    public static final String XDS_AUTHOR_INSTITUTION_SLOT = "authorInstitution";
    public static final String XDS_AUTHOR_ROLE_SLOT = "authorRole";
    public static final String XDS_AUTHOR_SPECIALITY_SLOT = "authorSpecialty";
    public static final String XDS_CODING_SCHEME_SLOT = "codingScheme";
    public static final String XDS_INTENDED_RECIPIENT_SLOT = "intendedRecipient";
    public static final String XDS_LANGUAGE_CODE_SLOT = "languageCode";
    public static final String XDS_LEGAL_AUTHENTICATOR_SLOT = "legalAuthenticator";
    public static final String XDS_SOURCE_PATIENT_INFO_PID3 = "PID-3";
    public static final String XDS_SOURCE_PATIENT_INFO_PID5 = "PID-5";
    public static final String XDS_SOURCE_PATIENT_INFO_PID7 = "PID-7";
    public static final String XDS_SOURCE_PATIENT_INFO_PID8 = "PID-8";
    public static final String XDS_SOURCE_PATIENT_INFO_PID11 = "PID-11";
    public static final String XDS_AUTHOR_CLASSIFICATION = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String XDS_CLASSCODE_CLASSIFICATION = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    public static final String XDS_CONTENT_TYPE_CODE_CLASSIFICATION = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";
    public static final String XDS_CONFIDENTIALITY_CODE_CLASSIFICATION = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    public static final String XDS_FORMAT_CODE_CLASSIFICATION = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    public static final String XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    public static final String XDS_PRACTICE_SETTING_CODE_CLASSIFICATION = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    public static final String XDS_EVENT_CODE_LIST_CLASSIFICATION = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    public static final String XDS_CODE_LIST_CLASSIFICATION = "urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5";
    public static final String XDS_TYPE_CODE_CLASSIFICATION = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    public static final String XDS_ERROR_CODE_MISSING_REQUEST_MESSAGE_DATA = "MISSING_DATA";
    public static final String XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA = "MISSING_METADATA";
    public static final String XDS_ERROR_CODE_REPOSITORY_ERROR = "REPOSITORY_ERROR";
    public static final String XDS_MISSING_REQUEST_MESSAGE_DATA = "The ProvideAndRegisterDocumentSetRequest message did not contain any data to operate on. No documents will be stored.";
    public static final String XDS_MISSING_DOCUMENT_METADATA = "A document exists in the submission with no corresponding document metadata. Document will not be stored.";
    public static final String XDS_REPOSITORY_ERROR = "An error occurred while storing a document to the repository.";
    public static final String XDS_ERROR_SEVERITY_WARNING = "WARNING";
    public static final String XDS_ERROR_SEVERITY_SEVERE = "SEVERE";
    public static final String XDS_ERROR_SEVERITY_ERROR = "ERROR";
    public static final String XDS_ASSOCIATION_TYPE_REPLACE = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";

    public XDRHelper()
    {
        log=createLogger();
    }
    
    public RegistryResponseType createErrorResponse(RegistryErrorList errorList)
    {
        RegistryResponseType result = new RegistryResponseType();
        log.debug("begin createErrorResponse()");
        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        result.setRegistryErrorList(errorList);

        return result;
    }
    public RegistryResponseType createPositiveAck()
    {
        RegistryResponseType result= new RegistryResponseType();

        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);

        return result;
    }
    public RegistryErrorList validateDocumentMetaData(ProvideAndRegisterDocumentSetRequestType body)
    {
        RegistryErrorList result = new RegistryErrorList();

        log.debug("begin validateDocumentMetaData()");
        if(body == null)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDS_ERROR_SEVERITY_ERROR, "ProvideAndRegisterDocumentSetRequestType was null");

            result.getRegistryError().add(error);

            //Request message was null, cannot continue. Return result.
            return processErrorList(result);
        }
        if(body.getDocument() == null)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDS_ERROR_SEVERITY_ERROR, "ProvideAndRegisterDocumentSetRequestType did not contain a DocumentList");
            result.getRegistryError().add(error);
        }
        else if(body.getDocument().size() == 0)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDS_ERROR_SEVERITY_ERROR, "DocumentList did not contain any documents");
            result.getRegistryError().add(error);
        }

        if (result.getRegistryError().size() > 0)
        {
            return processErrorList(result);
        }
        
        RegistryObjectListType regList = body.getSubmitObjectsRequest().getRegistryObjectList();

        ArrayList<String> metaDocIds = new ArrayList<String>();
        ArrayList<String> metaPatIds = new ArrayList<String>();

        for(int x = 0; x< regList.getIdentifiable().size(); x++)
        {
            if(regList.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class))
            {
                
                ExtrinsicObjectType extObj = (ExtrinsicObjectType) regList.getIdentifiable().get(x).getValue();
                String mimeType = extObj.getMimeType();
                if(isSupportedMimeType(mimeType) == false)
                {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocumentMetadata,XDS_ERROR_SEVERITY_ERROR, "Unsupported Mime Type: " + mimeType);
                    result.getRegistryError().add(error);                    
                }
                String docId= extObj.getId();
                metaDocIds.add(docId);
                
                if(isDocIdPresent(body.getDocument(), docId) == false)
                {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDS_ERROR_SEVERITY_ERROR, "Document Id: " + docId + " exists in metadata with no corresponding attached document");
                    result.getRegistryError().add(error);
                }
                String localPatId = getPatientId(extObj.getSlot());

                if(localPatId.isEmpty())
                {
                    RegistryError error = createRegistryError(XDR_EC_XDSUnknownPatientId,XDS_ERROR_SEVERITY_ERROR, "Patient ID referenced in metadata is not known to the Receiving NHIE");
                    result.getRegistryError().add(error);
                }
                metaPatIds.add(localPatId);
            }
        }

        if(patientIdsMatch(metaPatIds) == false)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSPatientIdDoesNotMatch,XDS_ERROR_SEVERITY_ERROR, "Patient Ids do not match");
            result.getRegistryError().add(error);
        }

        return processErrorList(result);



    }

    private String getXDSDocumentEntryPatientId(ExtrinsicObjectType extObj)
    {
        String result = "";

        for(ExternalIdentifierType extId : extObj.getExternalIdentifier())
        {
            String test = extId.getName().getLocalizedString().get(0).getValue();
            if(test.equals("XDSDocumentEntry.patientId"))
            {
                result = extId.getValue();
            }
        }

        return result;
    }
    public List<String> getIntendedRecepients(ProvideAndRegisterDocumentSetRequestType body)
    {

        List<String> result = new ArrayList<String>();

        log.debug("begin getIntendedRecepients()");
        if(body == null || body.getSubmitObjectsRequest() == null)
        {
            return null;
        }
        try
        {
            RegistryObjectListType regList = body.getSubmitObjectsRequest().getRegistryObjectList();
            
            for(int x = 0; x< regList.getIdentifiable().size(); x++)
            {
                if(regList.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class))
                {
                    ExtrinsicObjectType extObj = (ExtrinsicObjectType) regList.getIdentifiable().get(x).getValue();

                    SlotType1 recipSlot = getNamedSlotItem(extObj.getSlot(), XDS_INTENDED_RECIPIENT_SLOT);
                    if(recipSlot != null)
                    {
                        result = recipSlot.getValueList().getValue();
                    }

                }
            
            }           
        }
        catch (Exception ex)
        {
            log.error("Unable to pull intended recipients" + ex.getMessage());
        }


        log.debug("Found " + result.size() + " recipients");
        return result;
    }
    public List<String> getRoutingBeans(List<String> intendedRecipients)
    {
        ArrayList<String> result = new ArrayList<String>();

        ConfigurationManager configMgr = new ConfigurationManager();

        Config config = configMgr.loadConfiguration();

        for(String recipient : intendedRecipients)
        {
            //Loop through List of configured beans
            for(RoutingConfig rc : config.getRoutingInfo())
            {
                if(rc.getRecepient().equalsIgnoreCase(recipient))
                {
                    if (result.contains(rc.getBean()) == false)
                    {
                        result.add(rc.getBean());
                    }
                    break;
                }

            }
        }
        
        if(result.isEmpty())
        {
            result.add(RoutingObjectFactory.BEAN_REFERENCE_IMPLEMENTATION);
        }

        log.debug("Found " + result.size() + " beans");
        return result;
    }
    protected boolean checkIdsMatch()
    {
        boolean checkIds = false;

        try
        {
            checkIds = PropertyAccessor.getPropertyBoolean("adapter", "XDR.CheckPatientIdsMatch");
        }
        catch (Exception ex)
        {
            log.error("Unable to load XDR.CheckPatientIdsMatch");
        }

        return checkIds;
    }


    protected boolean isSupportedMimeType(String mimeType)
    {
        String[] mimeArray = getSupportedMimeTypes();
        boolean result = false;

        for(int x = 0; x<mimeArray.length;x++)
        {
            if(mimeArray[x].equalsIgnoreCase(mimeType))
            {
                result = true;
                break;
            }
        }

        return result;
    }
    protected  String[] getSupportedMimeTypes()
    {
         String[] mimeArray = null;

        try
        {
            String list = PropertyAccessor.getProperty("adapter", "XDR.SupportedMimeTypes");
            mimeArray = list.split(";");


        }
        catch(Exception ex)
        {

        }

        return mimeArray;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private boolean isDocIdPresent(List<ProvideAndRegisterDocumentSetRequestType.Document> documents, String docId)
    {
        boolean result = false;

        for(ProvideAndRegisterDocumentSetRequestType.Document doc : documents)
        {
            if(doc.getId().equals(docId))
            {
                result = true;
            }
        }

        return result;
    }
    private RegistryError createRegistryError(String errorCode, String severity, String codeContext)
    {
        RegistryError result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();

        result.setSeverity(severity);
        result.setCodeContext(codeContext);
        result.setErrorCode(errorCode);

        return result;
    }
    public String getSubmissionSetPatientId(ProvideAndRegisterDocumentSetRequestType body)
    {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for(int x= 0; x<object.getIdentifiable().size();x++)
        {
            System.out.println(object.getIdentifiable().get(x).getName());

            if(object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class))
            {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                System.out.println(registryPackage.getSlot().size());

                for(int y=0; y< registryPackage.getExternalIdentifier().size();y++)
                {
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0).getValue();
                    if(test.equals("XDSSubmissionSet.patientId"))
                    {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }


                }


            }
        }


        return result;
    }

    private String getXDSDocEntryPatientId(ProvideAndRegisterDocumentSetRequestType body)
    {
        String result = "";

        return result;
    }
    public String getSourcePatientId(ProvideAndRegisterDocumentSetRequestType body)
    {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for(int x= 0; x<object.getIdentifiable().size();x++)
        {
            System.out.println(object.getIdentifiable().get(x).getName());

            if(object.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class))
            {
                ExtrinsicObjectType extObj = (ExtrinsicObjectType) object.getIdentifiable().get(x).getValue();

                System.out.println(extObj.getSlot().size());

                SlotType1 slot = getNamedSlotItem(extObj.getSlot(), "sourcePatientId");

                if(slot != null)
                {
                    if(slot.getValueList() != null)
                    {
                        if(slot.getValueList().getValue().size() == 1)
                        {
                            result = slot.getValueList().getValue().get(0);
                        }
                    }
                }
                
            }
        }

        return result;
    }

    private String getPatientId(List<SlotType1> slots)
    {
        String result = "";
        SlotType1 patientIdSlot;

        patientIdSlot = getNamedSlotItem(slots, XDS_SOURCE_PATIENT_ID_SLOT);

        if(patientIdSlot != null)
        {
            if(patientIdSlot.getValueList().getValue().size() == 1)
            {
                result = patientIdSlot.getValueList().getValue().get(0);
            }
        }


        return result;

    }
    private SlotType1 getNamedSlotItem(List<SlotType1> slots, String name)
    {
        SlotType1 result = null;

        log.debug("begin getNamedSlotItem()");
        for(SlotType1 slot : slots)
        {
            if(slot.getName().equalsIgnoreCase(name))
            {
                result = slot;
                log.info("Slot=" + result.getName());
                break;
            }
        }

        return result;
    }
    private boolean patientIdsMatch(List<String> patIds)
    {
        boolean result = true;

        if(checkIdsMatch())
        {
            if (patIds.size() > 1)
            {
                //Get the first id
                String patId = patIds.get(0);
                //loop through all ids, make sure they all equal
                for(String id : patIds)
                {
                    if(id.equalsIgnoreCase(patId) == false)
                    {
                        result = false;
                        break;
                    }
                }
            }
        }


        return result;
    }
    private boolean metaDataPatientIdsMatch(ProvideAndRegisterDocumentSetRequestType body, String patId)
    {
        boolean result = true;

        String sourcePatId = getSourcePatientId(body);
        String submissionSetId = getSubmissionSetPatientId(body);

        return result;
    }
    private RegistryErrorList processErrorList(RegistryErrorList list)
    {
        int highestError = 0;

        if(list == null)
        {
            return null;
        }
        for(RegistryError error : list.getRegistryError())
        {
            int currentError = getErrorRanking(error.getSeverity());

            if(currentError > highestError)
            {
                highestError = currentError;
            }

        }

        list.setHighestSeverity(getErrorDescription(highestError));

        return list;
    }
    private String getErrorDescription(int rank)
    {
        String result = "";

        switch(rank)
        {
            case 0:
            {
                result = "";
                break;
            }
            case 1:
            {
                result = XDS_ERROR_SEVERITY_WARNING;
                break;
            }
            case 2:
            {
                result = XDS_ERROR_SEVERITY_SEVERE;
                break;
            }
            case 3:
            {
                result = XDS_ERROR_SEVERITY_ERROR;
                break;
            }
            default:
            {
                result = "";
                break;
            }
        }

        return result;
    }
    private int getErrorRanking(String severity)
    {
        int result;

        if(severity.equalsIgnoreCase(""))
        {
            result = 0;
        }
        else if(severity.equalsIgnoreCase(XDS_ERROR_SEVERITY_WARNING))
        {
            result = 1;
        }
        else if(severity.equalsIgnoreCase(XDS_ERROR_SEVERITY_SEVERE))
        {
            result = 2;
        }
        else if(severity.equalsIgnoreCase(XDS_ERROR_SEVERITY_ERROR))
        {
            result = 3;
        }
        else
        {
            result = -1;
        }

        return result;
    }

}
