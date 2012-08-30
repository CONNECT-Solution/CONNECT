/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuery;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.saml.creation.SAMLAssertionCreator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class DocumentQueryClient {

    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(DocumentQueryClient.class);
    private static EntityDocQuery service = new EntityDocQuery();
    private static final String HOME_ID = "urn:oid:2.16.840.1.113883.3.200";
    private static final String ID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    private static final String PATIENT_ID_SLOT_NAME = "$XDSDocumentEntryPatientId";
    private static final String DOCUMENT_STATUS_SLOT_NAME = "$XDSDocumentEntryStatus";
//    private static final String CREATION_TIME_FROM_SLOT_NAME = "$XDSDocumentEntryCreationTimeFrom";
//    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
    public static final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
    public static final String EBXML_DOCENTRY_SERVICE_START_TIME_TO = "$XDSDocumentEntryServiceStartTimeTo";
    public static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM = "$XDSDocumentEntryServiceStopTimeFrom";
    public static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
     public static final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
    //private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmssZ";
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";

    /**
     * 
     * @param patientSearchData
     * @param creationFromDate
     * @param creationToDate
     * @return
     */
    public List<DocumentInformation> retrieveDocumentsInformation(PatientSearchData patientSearchData, Date serviceTimeFromDate, Date serviceTimeToDate, List<String> targetCommunities,  List<String> docTypes) {

        EntityDocQueryPortType port = getPort(getEntityDocumentQueryProxyAddress());

        RespondingGatewayCrossGatewayQueryRequestType request = createAdhocQueryRequest(patientSearchData, serviceTimeFromDate, serviceTimeToDate, docTypes);


        //check to see if user specified a targeted gateway for the PD request
        if (targetCommunities != null && !targetCommunities.isEmpty())
        {

            log.debug("A target community has been requested.  Sending Doc Query request to the following communities: ");

            //decalare target communities parameter
            NhinTargetCommunitiesType targetType = null;

            //set parameter equal to the oid value entered by user
            Page2 pageTwoInstance = new Page2();
            targetType = pageTwoInstance.createNhinTargetCommunities(targetCommunities);

            //set the Target Communities value in the Entitiy DQ request and target only that gateway
            request.setNhinTargetCommunities(targetType);
        }
        
        AdhocQueryResponse response = port.respondingGatewayCrossGatewayQuery(request);

        return convertAdhocQueryResponseToDocInfoBO(response);
    }

    private SlotType1 createStatusTypeSlot()
    {
        SlotType1 slot = new SlotType1();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved','urn:ihe:iti:2010:StatusType:DeferredCreation')");

        ValueListType valueList = new ValueListType();

        valueList.getValue().add("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved','urn:ihe:iti:2010:StatusType:DeferredCreation')");

        slot.setName(DOCUMENT_STATUS_SLOT_NAME);
        slot.setValueList(valueList);


        return slot;
    }
    /**
     * 
     * @param patientSearchData
     * @param creationFromDate
     * @param creationToDate
     * @return
     */
    private RespondingGatewayCrossGatewayQueryRequestType createAdhocQueryRequest(PatientSearchData patientSearchData, Date serviceTimeFromDate, Date serviceTimeToDate, List<String> docTypes) {
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQuery.setHome(HOME_ID);
        adhocQuery.setId(ID);



        // Set patient id
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName(PATIENT_ID_SLOT_NAME);

        ValueListType valueList = new ValueListType();

        String uniquePatientId = patientSearchData.getPatientId() +"^^^&" + patientSearchData.getAssigningAuthorityID() + "&ISO";

        valueList.getValue().add(uniquePatientId);

        //valueList.getValue().add("D123401^^^&1.1&ISO");

        patientIDSlot.setValueList(valueList);
        adhocQuery.getSlot().add(patientIDSlot);

        //check to see if values from the calendar controls are null
        if (serviceTimeFromDate != null && serviceTimeToDate !=null)
        {
            //Set Service Start Time FROM Value
            SlotType1 serviceStartTimeFromSlot = new SlotType1();
            serviceStartTimeFromSlot.setName(EBXML_DOCENTRY_SERVICE_START_TIME_FROM);

            ValueListType serviceStartTimeFromValueList = new ValueListType();

            serviceStartTimeFromValueList.getValue().add(formatDate(serviceTimeFromDate, HL7_DATE_FORMAT));
            serviceStartTimeFromSlot.setValueList(serviceStartTimeFromValueList);
            adhocQuery.getSlot().add(serviceStartTimeFromSlot);

            //Set Service Start Time TO Value
            SlotType1 serviceStartTimeToSlot = new SlotType1();
            serviceStartTimeToSlot.setName(EBXML_DOCENTRY_SERVICE_START_TIME_TO);

            ValueListType serviceStartTimeToValueList = new ValueListType();

            serviceStartTimeToValueList.getValue().add(formatDate(serviceTimeToDate, HL7_DATE_FORMAT));
            serviceStartTimeToSlot.setValueList(serviceStartTimeToValueList);
            adhocQuery.getSlot().add(serviceStartTimeToSlot);
        }

        

        //if user selected doc types, create class code slot
        if (!docTypes.isEmpty())
        {
            
            //Set Entry Class Codes (document types)
            SlotType1 classCodeSlot = new SlotType1();
            classCodeSlot.setName(EBXML_DOCENTRY_CLASS_CODE);

            ValueListType classCodeValueList = new ValueListType();

            //loop through list of doc types and build slot value
            StringBuffer docTypeBuffer = new StringBuffer();
            docTypeBuffer.append("(");

            for (int x=0; x<docTypes.size();x++)
            {
                log.debug("Splitting entry #" + String.valueOf(x) + ": " + (docTypes.get(x).trim().split("\\s"))[0].trim());

                docTypeBuffer.append("'" + (docTypes.get(x).trim().split("\\s"))[0].trim() + "^^2.16.840.1.113883.6.1" + "'");

                if(x!=docTypes.size() - 1)
                    docTypeBuffer.append(",");

            }

            docTypeBuffer.append(")");

            log.debug("Class Code value to be added to Adhoc Query = " + docTypeBuffer.toString());

            classCodeValueList.getValue().add(docTypeBuffer.toString());
            classCodeSlot.setValueList(classCodeValueList);
            adhocQuery.getSlot().add(classCodeSlot);
        }

        //Set Creation From Date
  //      SlotType1 creationStartTimeSlot = new SlotType1();
   //     creationStartTimeSlot.setName(CREATION_TIME_FROM_SLOT_NAME);

 //       ValueListType creationStartTimeValueList = new ValueListType();

 //       creationStartTimeValueList.getValue().add(formatDate(creationFromDate, HL7_DATE_FORMAT));

//        creationStartTimeSlot.setValueList(creationStartTimeValueList);
 //       adhocQuery.getSlot().add(creationStartTimeSlot);

        // Set Creation To Date
   //     SlotType1 creationEndTimeSlot = new SlotType1();
   //     creationEndTimeSlot.setName(CREATION_TIME_TO_SLOT_NAME);

 //       ValueListType creationEndTimeSlotValueList = new ValueListType();

  //      creationEndTimeSlotValueList.getValue().add(formatDate(creationToDate, HL7_DATE_FORMAT));

  //      creationEndTimeSlot.setValueList(creationEndTimeSlotValueList);
  //      adhocQuery.getSlot().add(creationEndTimeSlot);

        adhocQuery.getSlot().add(createStatusTypeSlot());

        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(Boolean.FALSE);

        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        adhocQueryRequest.setResponseOption(responseOption);

        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);

        // Add Assertions
        AuthenticatedUserInfo authenticationInfo = Page1.getAuthenticationInfo();
        AssertionType assertions = authenticationInfo.getAssertions();
        request.setAssertion(assertions);

        return request;
    }

    /**
     *
     * @return
     */
    private String getEntityDocumentQueryProxyAddress() {
        String endpointAddress = null;

        try {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
        //    endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME);
            endpointAddress = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME);
            log.debug("Doc Query endpoint address: " + endpointAddress);
        } catch (PropertyAccessException pae) {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        } catch (ConnectionManagerException cme) {
            log.error("Exception encountered retrieving the entity doc query connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }

    /**
     * 
     * @param url
     * @return
     */
    private EntityDocQueryPortType getPort(String url) {
        if (service == null) {
            service = new EntityDocQuery();
        }

        EntityDocQueryPortType port = service.getEntityDocQueryPortSoap();

        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);


        return port;
    }

    /**
     * Retrieve the local home community id
     *
     * @return Local home community id
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }

    /**
     * 
     * @param response
     * @return
     */
    private List<DocumentInformation> convertAdhocQueryResponseToDocInfoBO(AdhocQueryResponse response) {

        List<DocumentInformation> documentInfoList = new ArrayList<DocumentInformation>();

        if (response == null) {
            log.debug("AdhocQueryResponse is null");
            return documentInfoList;
        }

        if (response.getRegistryObjectList() == null ||
                response.getRegistryObjectList().getIdentifiable() == null) {
            log.debug("AdhocQueryResponse is null");
            return documentInfoList;
        }

        List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = response.getRegistryObjectList().getIdentifiable();

        if (extrinsicObjects != null && extrinsicObjects.size() > 0) {
            for (JAXBElement<? extends IdentifiableType> jaxb : extrinsicObjects) {
                DocumentInformation docInfo = new DocumentInformation();

                if (jaxb.getValue() instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType) jaxb.getValue();

                    if (extrinsicObject != null) {
                        docInfo.setTitle(extractDocumentTitle(extrinsicObject));
                        docInfo.setDocumentType(extractDocumentType(extrinsicObject));
                        docInfo.setDocumentLOINC(extractDocumentLOINC(extrinsicObject));
                        String creationTime = extractCreationTime(extrinsicObject);
                        docInfo.setCreationDate(formatDate(creationTime, REGULAR_DATE_FORMAT));
                        docInfo.setInstitution(extractInstitution(extrinsicObject));
                        docInfo.setDocumentID(extractDocumentID(extrinsicObject));
                        docInfo.setRepositoryUniqueID(extractRespositoryUniqueID(extrinsicObject));
                        docInfo.setHomeCommunityID(extrinsicObject.getHome());
                        documentInfoList.add(docInfo);
                    }
                }
            }
        }

        return documentInfoList;
    }

    private String extractRespositoryUniqueID(ExtrinsicObjectType extrinsicObject)
    {
        return extractSingleSlotValue(extrinsicObject, "repositoryUniqueId");
    }

    private String extractDocumentType(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");

        String documentTypeCode = classification.getName().getLocalizedString().get(0).getValue();
        return documentTypeCode;
    }

    private String extractDocumentLOINC(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");

        String documentLoincCode = classification.getNodeRepresentation();
        return documentLoincCode;
    }

    private String extractDocumentTitle(ExtrinsicObjectType extrinsicObject) {

        String documentTitle = null;

        if (extrinsicObject != null &&
                extrinsicObject.getName() != null) {
            List<LocalizedStringType> localizedString = extrinsicObject.getName().getLocalizedString();

            if (localizedString != null && localizedString.size() > 0) {
                documentTitle = localizedString.get(0).getValue();
            }
        }

        return documentTitle;
    }

    private String extractCreationTime(ExtrinsicObjectType extrinsicObject) {
        return extractSingleSlotValue(extrinsicObject, "creationTime");
    }

    private String extractDocumentID(ExtrinsicObjectType extrinsicObject) {
        String documentID = null;

        ExternalIdentifierType identifier = extractIndentifierType(extrinsicObject, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");

        if (identifier != null) {
            documentID = identifier.getValue();
        }

        return documentID;
    }

    private String extractInstitution(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");

        String institution = null;

        if (classification != null && classification.getSlot() != null && !classification.getSlot().isEmpty()) {
            for (SlotType1 slot : classification.getSlot()) {
                if (slot != null && slot.getName().contentEquals("authorInstitution")) {
                    if (slot.getValueList() != null && slot.getValueList().getValue() != null && !slot.getValueList().getValue().isEmpty()) {
                        institution = slot.getValueList().getValue().get(0);
                        break;
                    }
                }
            }
        }

        return institution;
    }

    private String extractSingleSlotValue(ExtrinsicObjectType extrinsicObject, String slotName) {
        String slotValue = null;
        for (SlotType1 slot : extrinsicObject.getSlot()) {
            if (slot != null && slot.getName().contentEquals(slotName)) {
                if (slot.getValueList().getValue().size() > 0) {
                    slotValue = slot.getValueList().getValue().get(0);
                    break;
                }
            }
        }
        return slotValue;
    }

    private ExternalIdentifierType extractIndentifierType(ExtrinsicObjectType extrinsicObject, String identificationScheme) {
        ExternalIdentifierType identifier = null;

        for (ExternalIdentifierType identifierItem : extrinsicObject.getExternalIdentifier()) {
            if (identifierItem != null && identifierItem.getIdentificationScheme().contentEquals(identificationScheme)) {
                identifier = identifierItem;
                break;
            }
        }

        return identifier;
    }

    private ClassificationType extractClassification(ExtrinsicObjectType extrinsicObject, String classificationScheme) {
        ClassificationType classification = null;

        for (ClassificationType classificationItem : extrinsicObject.getClassification()) {
            if (classificationItem != null && classificationItem.getClassificationScheme().contentEquals(classificationScheme)) {
                classification = classificationItem;
                break;
            }
        }

        return classification;
    }

    /**
     * 
     * @param dateString
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    
//    private String formatDate(String dateString, String inputFormat, String outputFormat) {
//        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
//        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
//
//        Date date = null;
//
//        try {
//            date = inputFormatter.parse(dateString);
//        } catch (ParseException ex) {
//            Logger.getLogger(DocumentQueryClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return outputFormatter.format(date);
//    }

    /**
     * Format date in the String format using UTCDateUtil class.
     *
     * @param dateString
     * @param outputFormat
     * @return
     */
    private String formatDate(String dateString, String outputFormat){
        UTCDateUtil dateUtil = new UTCDateUtil();

        Date date = dateUtil.parseUTCDateOptionalTimeZone(dateString);

        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

        return outputFormatter.format(date);
    }

    private String formatDate(Date date, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
