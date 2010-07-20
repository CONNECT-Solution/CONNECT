package gov.hhs.fha.nhinc.adapters.general.adapterpolicyenginetransform.adapterpolicyengine;

import java.util.List;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToCppAQRRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformAQRToCppRDSRRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPatientOptInRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformXACMLRequestToAQRForPatientIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.TransformPatientIdAQRToCppXACMLRequestType;

import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class does the work of transforming messages from one format 
 * to another.
 * 
 * @author Les Westberg
 */
public class AdapterPolicyEngineTransformHelper
{
    // Constants used in this class
    //-----------------------------
    private static final String XACML_POLICY_PATIENT_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    private static final String LEAF_CLASS = "LeafClass";
    private static final String SLOT_NAME_CPP_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private static final String SLOT_NAME_DOCUMENT_CLASS_CODE = "$XDSDocumentEntryClassCode";
    private static final String SLOT_NAME_DOCUMENT_CLASS_CODE_VALUE = "('XNHIN-CONSENT')";
    private static final String SLOT_NAME_REPOSITORY_UNIQUE_ID = "repositoryUniqueId";
    private static final String SLOT_NAME_SOURCE_PATIENT_ID = "sourcePatientId";
    private static final String DOCUMENT_ID_IDENT_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String XACML_DOCUMENT_REPOSITORY_ID = "urn:gov:hhs:fha:nhinc:document-repository-id";
    private static final String XACML_DOCUMENT_ID = "urn:gov:hhs:fha:nhinc:document-id";
    private static final String SLOT_NAME_DOC_RETRIEVE_REPOSITORY_UNIQUE_ID = "$XDSRepositoryUniqueId";
    private static final String SLOT_NAME_DOC_RETRIEVE_DOCUMENT_ID = "$XDSDocumentEntryUniqueId";
    private static final String SLOT_NAME_DOC_QUERY_ENTRY_STATUS = "$XDSDocumentEntryStatus";
    private static final String SLOT_NAME_DOC_QUERY_ENTRY_STATUS_VALUE="('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')";
    private static final String ADHOC_QUERY__WITH_DOCUMENTID_UUID = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
    private static final String ADHOC_QUERY__WITH_PATIENTID_UUID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    private static Log log = LogFactory.getLog(AdapterPolicyEngineTransformHelper.class);

    /**
     * This message transforms a XACML request message to an AdhocQueryRequest 
     * message that can be used to find the CPP (Consumer Preferences Profile) 
     * document for the patient.
     * 
     * @param transformXACMLRequestToCppAQRRequest The XACML request to be 
     *        transformed.
     * 
     * @return The AdhocQueryRequest to get the CPP document.
     */
    public static TransformXACMLRequestToCppAQRResponseType transformXACMLRequestToCppAQR(TransformXACMLRequestToCppAQRRequestType transformXACMLRequestToCppAQRRequest)
        throws AdapterPolicyEngineTransformException
    {
        TransformXACMLRequestToCppAQRResponseType oResponse = new TransformXACMLRequestToCppAQRResponseType();
        
        String sPatientId = "";
        
        if ((transformXACMLRequestToCppAQRRequest != null) &&
            (transformXACMLRequestToCppAQRRequest.getRequest() != null) &&
            (transformXACMLRequestToCppAQRRequest.getRequest().getResource() != null) &&
            (transformXACMLRequestToCppAQRRequest.getRequest().getResource().size() > 0))
        {
            List<ResourceType> olResource = transformXACMLRequestToCppAQRRequest.getRequest().getResource();
            for (ResourceType oResource : olResource)
            {
                if ((oResource.getAttribute() != null) &&
                    (oResource.getAttribute().size() > 0))
                {
                    List<AttributeType> olAttribute = oResource.getAttribute();
                    for (AttributeType oAttribute : olAttribute)
                    {
                        if ((oAttribute.getAttributeId() != null) &&
                            (oAttribute.getAttributeId().equals(XACML_POLICY_PATIENT_ID)) &&
                            (oAttribute.getAttributeValue() != null) &&
                            (oAttribute.getAttributeValue().size() > 0) &&
                            (oAttribute.getAttributeValue().get(0) != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent() != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent().size() > 0))
                        {
                            List<Object> olObjectValue = oAttribute.getAttributeValue().get(0).getContent();
                            for (Object oObjectValue : olObjectValue)
                            {
                                if ((oObjectValue instanceof String) &&
                                    (((String) oObjectValue).length() > 0))
                                {
                                    sPatientId = ((String) oObjectValue).trim();
                                    break;          // get out of the for loop
                                }
                            }   //for (Object oObjectValue : olObjectValue)
                            
                            // If we found what we wanted get out of the for loop
                            //---------------------------------------------------
                            if (sPatientId.length() > 0)
                            {
                                break;
                            }
                        }   // if ((oAttribute.getAttributeId() != null) &&
                    }   // for (AttributeType oAttribute : olAttribute)
                    
                    // If we found what we wanted get out of the for loop
                    //---------------------------------------------------
                    if (sPatientId.length() > 0)
                    {
                        break;
                    }
                }   // if ((oResource.getAttribute() != null) &&
            }   // for (ResourceType oResource : olResource)
            
            // If we found the patient ID, create the request to get the CPP
            // document.
            //---------------------------------------------------------------
            if (sPatientId.length() > 0)
            {
                AdhocQueryRequest oAQR = new AdhocQueryRequest();
                oResponse.setAdhocQueryRequest(oAQR);
                
                // ResponseOption
                //----------------
                ResponseOptionType oResponseOption = new ResponseOptionType();
                oAQR.setResponseOption(oResponseOption);
                oResponseOption.setReturnComposedObjects(true);
                oResponseOption.setReturnType(LEAF_CLASS);
                
                AdhocQueryType oAQ = new AdhocQueryType();
                oAQR.setAdhocQuery(oAQ);
                oAQ.setId(ADHOC_QUERY__WITH_PATIENTID_UUID);
                
                List<SlotType1> olSlot = oAQ.getSlot();

                // Patient ID
                //------------
                SlotType1 oSlot = new SlotType1();
                olSlot.add(oSlot);
                oSlot.setName(SLOT_NAME_CPP_PATIENT_ID);
                ValueListType oValueList = new ValueListType();
                oSlot.setValueList(oValueList);
                sPatientId = addQuoteToString(sPatientId);
                oValueList.getValue().add(sPatientId);
                
                // Document Class Code
                //--------------------
                oSlot = new SlotType1();
                olSlot.add(oSlot);
                oSlot.setName(SLOT_NAME_DOCUMENT_CLASS_CODE);
                oValueList = new ValueListType();
                oSlot.setValueList(oValueList);
                oValueList.getValue().add(SLOT_NAME_DOCUMENT_CLASS_CODE_VALUE);

                //Document Entry Status Code
                //-------------------------
                oSlot = new SlotType1();
                olSlot.add(oSlot);
                oSlot.setName(SLOT_NAME_DOC_QUERY_ENTRY_STATUS);
                oValueList = new ValueListType();
                oSlot.setValueList(oValueList);
                oValueList.getValue().add(SLOT_NAME_DOC_QUERY_ENTRY_STATUS_VALUE);
            }
        }   // if ((transformXACMLRequestToCppAQRRequest != null) &&

        return oResponse;
    }

    /**
     * This method formats the input string in between two single quotes.
     * @param inString
     * @return String
     */
    public static String addQuoteToString(String inString)
    {
        String outString ="";
        if(inString != null && !inString.equals(""))
        {
            if(!inString.startsWith("'"))
            {
                outString = "'"+inString+"'";
            }
        }
        if(outString.equals(""))
        {
            outString = inString;
        }
        return outString;
    }

    /**
     * This method transforms a message from a CPP AdhocQueryResponse message to 
     * a RetrieveDocumentSetRequest message that can be used to retrieve the
     * CPP document.
     * 
     * @param transformAQRToCppRDSRRequest The AdhocQueryResponse message that
     *        was returned form the Document Query to get the CPP document.
     * @return The RetrieveDocumentSetRequest that can be used to retrieve
     *         the CPP document.
     */
    public static TransformAQRToCppRDSRResponseType transformAQRToCppRDSR(TransformAQRToCppRDSRRequestType transformAQRToCppRDSRRequest)
        throws AdapterPolicyEngineTransformException
    {
        TransformAQRToCppRDSRResponseType oResponse = new TransformAQRToCppRDSRResponseType();
        
        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentId = "";

        if ((transformAQRToCppRDSRRequest != null) &&
            (transformAQRToCppRDSRRequest.getAdhocQueryResponse() != null) &&
            (transformAQRToCppRDSRRequest.getAdhocQueryResponse().getRegistryObjectList() != null) &&
            (transformAQRToCppRDSRRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable() != null) &&
            (transformAQRToCppRDSRRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size() > 0))
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = transformAQRToCppRDSRRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable();
            
            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    
                    // Home Community ID
                    //-------------------
                    if ((oExtObj != null) &&
                        (oExtObj.getHome() != null) &&
                        (oExtObj.getHome().length() > 0))
                    {
                        sHomeCommunityId = oExtObj.getHome().trim();
                    }
                    
                    // Repository ID
                    //---------------
                    if ((oExtObj.getSlot() != null) &&
                        (oExtObj.getSlot().size() > 0))
                    {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot)
                        {
                            if ((oSlot.getName() != null) &&
                                (oSlot.getName().equals(SLOT_NAME_REPOSITORY_UNIQUE_ID)) &&
                                (oSlot.getValueList() != null) &&
                                (oSlot.getValueList().getValue() != null) &&
                                (oSlot.getValueList().getValue().size() > 0) &&
                                (oSlot.getValueList().getValue().get(0).length() > 0))
                            {
                                sRepositoryId = oSlot.getValueList().getValue().get(0).trim();
                            }
                        }   // for (SlotType1 oSlot : olSlot)
                    }   // if ((oExtObj.getSlot() != null) && ...
                    
                    // Document Unique ID
                    //-------------------
                    if ((oExtObj.getExternalIdentifier() != null) &&
                        (oExtObj.getExternalIdentifier().size() > 0))
                    {
                        List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
                        for (ExternalIdentifierType oExtId : olExtId)
                        {
                            if ((oExtId.getIdentificationScheme() != null) &&
                                (oExtId.getIdentificationScheme().equals(DOCUMENT_ID_IDENT_SCHEME)) &&
                                (oExtId.getValue() != null) &&
                                (oExtId.getValue().length() > 0))
                            {
                                sDocumentId = oExtId.getValue().trim();
                            }
                        }   // for (ExternalIdentifierType oExtid : olExtId)
                    }   // if ((oExtObj.getExternalIdentifier() != null) &&
                }   // if ((oJAXBObj != null) &&
            }   // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            
            // Now lets create the response...
            //---------------------------------
            RetrieveDocumentSetRequestType oRDSR = new RetrieveDocumentSetRequestType();
            oResponse.setRetrieveDocumentSetRequest(oRDSR);
            List<DocumentRequest> olDocRequest = oRDSR.getDocumentRequest();
            DocumentRequest oDocRequest = new DocumentRequest();
            olDocRequest.add(oDocRequest);
            oDocRequest.setHomeCommunityId(sHomeCommunityId);
            oDocRequest.setRepositoryUniqueId(sRepositoryId);
            oDocRequest.setDocumentUniqueId(sDocumentId);
        }
        
        return oResponse;
    }

    /**
     * This method takes the RetrieveDocumentSetResponse message from the 
     * document repository that contains the CPP document and looks at the
     * CPP document and returns TRUE if the patient has opted in and false if
     * they have not.  If the document does not exist or the input parameter
     * is null, then it means that the patient has opted out and false is 
     * returned.
     * 
     * @param checkPatientOptInRequest The RetrieveDocumentSetResponse containing
     *        the CPP document.
     * @return TRUE if the patient has opted in and false if they have not.
     */
    public static CheckPatientOptInResponseType checkPatientOptIn(CheckPatientOptInRequestType checkPatientOptInRequest)
        throws AdapterPolicyEngineTransformException
    {
        CheckPatientOptInResponseType oResponse = new CheckPatientOptInResponseType();
        oResponse.setPatientOptedIn(false);     // default is opt out.
        
        if ((checkPatientOptInRequest != null) &&
            (checkPatientOptInRequest.getRetrieveDocumentSetResponse() != null) &&
            (checkPatientOptInRequest.getRetrieveDocumentSetResponse().getDocumentResponse() != null) &&
            (checkPatientOptInRequest.getRetrieveDocumentSetResponse().getDocumentResponse().size() > 0))
        {
            // A patient should only have one document - so if there are more than one, we
            // will use the first one...
            //-----------------------------------------------------------------------------
            DocumentResponse oDocResponse = checkPatientOptInRequest.getRetrieveDocumentSetResponse().getDocumentResponse().get(0);

            if ((oDocResponse.getDocument() != null) &&
                (oDocResponse.getDocument().length > 0))
            {
                String sCpp = new String(oDocResponse.getDocument());
                log.error("CPP Document: '" + sCpp + "'");

                // The CPP defined by NHIN does not appear to be compatible with the XACML 2.0
                // I cannot use that to unmarshal the XML to an object.  We are just going to
                // have to search for the piece we want - since we created this document - at
                // least for now, we will look for exactly the match on the string that we want.
                //------------------------------------------------------------------------------
                
                // Search for <Rule, then Search for the word "Effect="  use that value...
                //-------------------------------------------------------------------------
                int iRuleIdx = sCpp.indexOf("<Rule");
                if (iRuleIdx > 0)
                {
                    log.error("iRuleIdx: " + iRuleIdx);
                    int iEffectIdx = sCpp.indexOf("Effect=\"", iRuleIdx);
                    if (iEffectIdx >= 0)
                    {
                        log.error("iEffectIdx: " + iRuleIdx);
                        String sRestOfString = sCpp.substring(iEffectIdx + 8);
                        log.error("sRestOfString: " + sRestOfString);
                        if (sRestOfString.startsWith("Permit"))
                        {
                            oResponse.setPatientOptedIn(true);
                        }   // if (sRestOfString.startsWith("Permit"))
                    }   // if (iEffectIdx >= 0)
                }   // if (iRuleIdx > 0)
            }   // if ((oDocResponse.getDocument() != null) &&
        }   // if ((checkPatientOptInRequest != null) &&
        
        return oResponse;
    }
    
    /**
     * This method transforms a XACML request to the information needed to
     * retrienve the patient ID from the Document Registry based on the
     * document information.   This will return an AdhocQueryRequest.
     * 
     * @param transformXACMLRequestToAQRForPatientIdRequest The XACML information
     *        that contains the document identifiers.
     * @return The AdhocQueryRequest to retrieve the patient ID 
     *         information based on the document identifiers.
     */
    public static TransformXACMLRequestToAQRForPatientIdResponseType transformXACMLRequestToAQRForPatientId(TransformXACMLRequestToAQRForPatientIdRequestType transformXACMLRequestToAQRForPatientIdRequest)
        throws AdapterPolicyEngineTransformException
    {
        TransformXACMLRequestToAQRForPatientIdResponseType oResponse = new TransformXACMLRequestToAQRForPatientIdResponseType();
        
        String sRepositoryId = "";
        String sDocumentId = "";
        
        if ((transformXACMLRequestToAQRForPatientIdRequest != null) &&
            (transformXACMLRequestToAQRForPatientIdRequest.getRequest() != null) &&
            (transformXACMLRequestToAQRForPatientIdRequest.getRequest().getResource() != null) &&
            (transformXACMLRequestToAQRForPatientIdRequest.getRequest().getResource().size() > 0))
        {
            List<ResourceType> olResource = transformXACMLRequestToAQRForPatientIdRequest.getRequest().getResource();
            for (ResourceType oResource : olResource)
            {
                if ((oResource.getAttribute() != null) &&
                    (oResource.getAttribute().size() > 0))
                {
                    List<AttributeType> olAttribute = oResource.getAttribute();
                    for (AttributeType oAttribute : olAttribute)
                    {
                        if ((oAttribute.getAttributeId() != null) &&
                            (oAttribute.getAttributeId().equals(XACML_DOCUMENT_ID)) &&
                            (oAttribute.getAttributeValue() != null) &&
                            (oAttribute.getAttributeValue().size() > 0) &&
                            (oAttribute.getAttributeValue().get(0) != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent() != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent().size() > 0))
                        {
                            List<Object> olObjectValue = oAttribute.getAttributeValue().get(0).getContent();
                            for (Object oObjectValue : olObjectValue)
                            {
                                if ((oObjectValue instanceof String) &&
                                    (((String) oObjectValue).length() > 0))
                                {
                                    sDocumentId = ((String) oObjectValue).trim();
                                    break;          // get out of the for loop
                                }
                            }   //for (Object oObjectValue : olObjectValue)
                        }   // if ((oAttribute.getAttributeId() != null) &&
                        else if ((oAttribute.getAttributeId() != null) &&
                            (oAttribute.getAttributeId().equals(XACML_DOCUMENT_REPOSITORY_ID)) &&
                            (oAttribute.getAttributeValue() != null) &&
                            (oAttribute.getAttributeValue().size() > 0) &&
                            (oAttribute.getAttributeValue().get(0) != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent() != null) &&
                            (oAttribute.getAttributeValue().get(0).getContent().size() > 0))
                        {
                            List<Object> olObjectValue = oAttribute.getAttributeValue().get(0).getContent();
                            for (Object oObjectValue : olObjectValue)
                            {
                                if ((oObjectValue instanceof String) &&
                                    (((String) oObjectValue).length() > 0))
                                {
                                    sRepositoryId = ((String) oObjectValue).trim();
                                    break;          // get out of the for loop
                                }
                            }   //for (Object oObjectValue : olObjectValue)
                        }   // if ((oAttribute.getAttributeId() != null) &&
                        
                        
                        // If we found what we wanted get out of the for loop
                        //---------------------------------------------------
                        if ((sDocumentId.length() > 0) && (sRepositoryId.length() > 0))
                        {
                            break;
                        }
                        
                    }   // for (AttributeType oAttribute : olAttribute)
                    
                    // If we found what we wanted get out of the for loop
                    //---------------------------------------------------
                    if ((sDocumentId.length() > 0) && (sRepositoryId.length() > 0))
                    {
                        break;
                    }
                }   // if ((oResource.getAttribute() != null) &&
            }   // for (ResourceType oResource : olResource)
            
            // If we found what we are looking for, create the request to 
            //---------------------------------------------------------------
            if ((sDocumentId.length() > 0) || (sRepositoryId.length() > 0))
            {
                AdhocQueryRequest oAQR = new AdhocQueryRequest();
                oResponse.setAdhocQueryRequest(oAQR);
                
                // ResponseOption
                //----------------
                ResponseOptionType oResponseOption = new ResponseOptionType();
                oAQR.setResponseOption(oResponseOption);
                oResponseOption.setReturnComposedObjects(true);
                oResponseOption.setReturnType(LEAF_CLASS);
                
                AdhocQueryType oAQ = new AdhocQueryType();
                oAQR.setAdhocQuery(oAQ);
                oAQ.setId(ADHOC_QUERY__WITH_DOCUMENTID_UUID);
                
                List<SlotType1> olSlot = oAQ.getSlot();

                // Document ID
                //------------
                if (sDocumentId.length() > 0)
                {
                    SlotType1 oSlot = new SlotType1();
                    olSlot.add(oSlot);
                    oSlot.setName(SLOT_NAME_DOC_RETRIEVE_DOCUMENT_ID);
                    ValueListType oValueList = new ValueListType();
                    oSlot.setValueList(oValueList);
                    sDocumentId = "('"+sDocumentId+"')";
                    oValueList.getValue().add(sDocumentId);
                }

                // Repository ID
                //--------------
                if (sRepositoryId.length() > 0)
                {
                    SlotType1 oSlot = new SlotType1();
                    olSlot.add(oSlot);
                    oSlot.setName(SLOT_NAME_DOC_RETRIEVE_REPOSITORY_UNIQUE_ID);
                    ValueListType oValueList = new ValueListType();
                    oSlot.setValueList(oValueList);
                    sRepositoryId = "'"+sRepositoryId+"'";
                    oValueList.getValue().add(sRepositoryId);
                }
            }   // if ((sDocumentId.length() > 0) || (sRepositoryId.length() > 0))
        }   // if ((transformXACMLRequestToAQRForPatientIdRequest != null) &&

        return oResponse;
    }
    
    /**
     * This method takes the AdhocQueryResponse containing the document
     * meta data including the Patient ID and it will create a XACML request
     * containing the patient ID so that it can be passed into the 
     * CheckPolicyPatientOptIn method to determine if the patient has opted in.
     * 
     * @param transformPatientIdAQRToCppXACMLRequest The AdhocQueryResponse
     *        containing the patient ID for the patient associated with the document.
     * @return The XACML policy containing the patient ID.
     * 
     */
    public static TransformPatientIdAQRToCppXACMLResponseType transformPatientIdAQRToCppXACML(TransformPatientIdAQRToCppXACMLRequestType transformPatientIdAQRToCppXACMLRequest)
        throws AdapterPolicyEngineTransformException
    {
        TransformPatientIdAQRToCppXACMLResponseType oResponse = new TransformPatientIdAQRToCppXACMLResponseType();
        
        // If we are given a basis of our XACML, use it and put the PatientId into it.  Otherwise, 
        // we will have to create it from scratch.
        //----------------------------------------------------------------------------------------
        if ((transformPatientIdAQRToCppXACMLRequest != null) &&
            (transformPatientIdAQRToCppXACMLRequest.getRequest() != null))
        {
            oResponse.setRequest(transformPatientIdAQRToCppXACMLRequest.getRequest());
        }
        else
        {
            RequestType oRequest = new RequestType();
            oResponse.setRequest(oRequest);
        }
        
        String sPatientId = "";
        
        // Find the Patient ID in the response...
        //---------------------------------------
        if ((transformPatientIdAQRToCppXACMLRequest != null) &&
            (transformPatientIdAQRToCppXACMLRequest.getAdhocQueryResponse() != null) &&
            (transformPatientIdAQRToCppXACMLRequest.getAdhocQueryResponse().getRegistryObjectList() != null) &&
            (transformPatientIdAQRToCppXACMLRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable() != null) &&
            (transformPatientIdAQRToCppXACMLRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size() > 0))
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = transformPatientIdAQRToCppXACMLRequest.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable();
            
            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    
                    // Patient ID
                    //-----------
                    if ((oExtObj.getSlot() != null) &&
                        (oExtObj.getSlot().size() > 0))
                    {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot)
                        {
                            if ((oSlot.getName() != null) &&
                                (oSlot.getName().equals(SLOT_NAME_SOURCE_PATIENT_ID)) &&
                                (oSlot.getValueList() != null) &&
                                (oSlot.getValueList().getValue() != null) &&
                                (oSlot.getValueList().getValue().size() > 0) &&
                                (oSlot.getValueList().getValue().get(0).length() > 0))
                            {
                                sPatientId = oSlot.getValueList().getValue().get(0).trim();
                                break;          // Get out of the loop - we found what we want.
                            }
                        }   // for (SlotType1 oSlot : olSlot)
                    }   // if ((oExtObj.getSlot() != null) && ...
                }   // if ((oJAXBObj != null) &&
            }   // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
        }   // if ((transformPatientIdAQRToCppXACMLRequest != null) &&
        
        // Now lets get the patient ID and put it in the XACML request.  If 
        // there is already a node in there with a patient ID, then this will
        // replace it, otherwise it will add in a new node with the patient ID.
        //----------------------------------------------------------------------
        if (sPatientId.length() > 0)
        {
            RequestType oRequest = oResponse.getRequest();
            List<ResourceType> olResource = oRequest.getResource();
            AttributeType oPatientIdAttribute = null;
            ResourceType oPatientIdResource = null;
            
            if (olResource.size() > 0)
            {

                // See if the patient ID is already in here...
                //--------------------------------------------
                for (ResourceType oResource : olResource)
                {
                    List<AttributeType> olAttribute = oResource.getAttribute();
                    for (AttributeType oAttribute : olAttribute)
                    {
                        if ((oAttribute.getAttributeId() != null) &&
                            (oAttribute.getAttributeId().equals(XACML_POLICY_PATIENT_ID)))
                        {
                            oPatientIdResource = oResource;
                            oPatientIdAttribute = oAttribute;
                            break;
                        }
                    }   // for (AttributeType oAttribute : olAttribute)

                    if (oPatientIdAttribute != null)
                    {
                        break;
                    }
                }   // for (ResourceType oResource : olResource)
            }   // if (oRequest.getResource().size() > 0)
            else
            {
                oPatientIdResource = new ResourceType();
                olResource.add(oPatientIdResource);
            }
            
            // If we did not find it, then we need to create the attribute and fill
            // it in.
            //----------------------------------------------------------------------
            if (oPatientIdAttribute == null)
            {
                oPatientIdAttribute = new AttributeType();
                oPatientIdAttribute.setAttributeId(XACML_POLICY_PATIENT_ID);
                oPatientIdResource.getAttribute().add(oPatientIdAttribute);
            }
            
            // Now lets set the value...
            //---------------------------
            oPatientIdAttribute.getAttributeValue().clear();
            AttributeValueType oAttributeValue = new AttributeValueType();
            oPatientIdAttribute.getAttributeValue().add(oAttributeValue);
            oAttributeValue.getContent().add(sPatientId);
        }   // if (sPatientId.length() > 0)
        
        return oResponse;
    }
    
}
