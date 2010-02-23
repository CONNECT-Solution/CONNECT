/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;


/**
 *
 * @author dunnek
 */
public class XDRHelper {
    private static Log log = null;

    public static String XDR_SEVERITY_ERROR = "Error";
    public static String XDR_EC_XDSMissingDocument = "XDSMissingDocument";
    public static String XDR_EC_XDSMissingDocumentMetadata = "XDSMissingDocumentMetadata";
    public static String XDR_EC_XDSNonIdenticalHash = "XDSNonIdenticalHash";
    public static String XDR_EC_XDSRegistryDuplicateUniqueIdInMessage = "XDSRegistryDuplicateUniqueIdInMessage";
    public static String XDR_EC_XDSRegistryBusy = "XDSRegistryBusy";
    public static String XDR_EC_XDSRegistryMetadataError = "XDSRegistryMetadataError";
    public static String XDR_EC_XDSUnknownPatientId = "XDSUnknownPatientId";
    public static String XDR_EC_XDSPatientIdDoesNotMatch = "XDSPatientIdDoesNotMatch";



    public XDRHelper()
    {
        log=createLogger();
    }
    
    public RegistryResponseType createErrorResponse(RegistryErrorList errorList)
    {
        RegistryResponseType result = new RegistryResponseType();
        log.debug("begin createErrorResponse()");
        result.setStatus("Failure");
        result.setRegistryErrorList(errorList);

        return result;
    }
    public RegistryErrorList validateDocumentMetaData(ProvideAndRegisterDocumentSetRequestType body)
    {
        RegistryErrorList result = new RegistryErrorList();

        log.debug("begin validateDocumentMetaData()");
        if(body == null)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDR_SEVERITY_ERROR, "ProvideAndRegisterDocumentSetRequestType was null");

            result.getRegistryError().add(error);

            //Request message was null, cannot continue. Return result.
            return result;
        }
        if(body.getDocument() == null)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDR_SEVERITY_ERROR, "ProvideAndRegisterDocumentSetRequestType did not contain a DocumentList");
            result.getRegistryError().add(error);
        }
        else if(body.getDocument().size() == 0)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDR_SEVERITY_ERROR, "DocumentList did not contain any documents");
            result.getRegistryError().add(error);
        }

        if (result.getRegistryError().size() > 0)
        {
            return result;
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

                    RegistryError error = createRegistryError(XDR_EC_XDSRegistryMetadataError,XDR_SEVERITY_ERROR, "Unsupported Mime Type: " + mimeType);
                    result.getRegistryError().add(error);                    
                }
                String docId= extObj.getId();
                metaDocIds.add(docId);

                if(isDocIdPresent(body.getDocument(), docId) == false)
                {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,XDR_SEVERITY_ERROR, "Document Id: " + docId + " exists in metadata with no corresponding attached document");
                    result.getRegistryError().add(error);
                }
                String localPatId = getPatientId(extObj.getSlot());
                metaPatIds.add(localPatId);
                
            }
        }
        if(patientIdsMatch(metaPatIds) == false)
        {
            RegistryError error = createRegistryError(XDR_EC_XDSPatientIdDoesNotMatch,XDR_SEVERITY_ERROR, "Patient Ids do not match");
            result.getRegistryError().add(error);
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
    private String getPatientId(List<SlotType1> slots)
    {
        String result = "";
        SlotType1 patientIdSlot;

        patientIdSlot = getNamedSlotItem(slots, "sourcePatientId");

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

        for(SlotType1 slot : slots)
        {
            if(slot.getName().equalsIgnoreCase(name))
            {
                result = slot;
                break;
            }
        }

        return result;
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

    private RegistryError createRegistryError(String errorCode, String severity, String codeContext)
    {
        RegistryError result = new RegistryError();

        result.setSeverity(severity);
        result.setCodeContext(codeContext);
        result.setErrorCode(errorCode);

        return result;
    }
}
