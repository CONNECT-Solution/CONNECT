/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

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
    private static final String DOCUMENT_STATUS_SLOT_NAME = "('$XDSDocumentEntryStatus')";
    private static final String CREATION_TIME_FROM_SLOT_NAME = "$XDSDocumentEntryCreationTimeFrom";
    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
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
    public List<DocumentInformation> retrieveDocumentsInformation(PatientSearchData patientSearchData, Date creationFromDate, Date creationToDate) {

        EntityDocQueryPortType port = getPort(getEntityDocumentQueryProxyAddress());

        RespondingGatewayCrossGatewayQueryRequestType request = createAdhocQueryRequest(patientSearchData, creationFromDate, creationToDate);

        AdhocQueryResponse response = port.respondingGatewayCrossGatewayQuery(request);

        return convertAdhocQueryResponseToDocInfoBO(response);
    }

    private SlotType1 createStatusTypeSlot()
    {
        SlotType1 slot = new SlotType1();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Approved");

        ValueListType valueList = new ValueListType();

        valueList.getValue().add("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Approved");

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
    private RespondingGatewayCrossGatewayQueryRequestType createAdhocQueryRequest(PatientSearchData patientSearchData, Date creationFromDate, Date creationToDate) {
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQuery.setHome(HOME_ID);
        adhocQuery.setId(ID);



        // Set patient id
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName(PATIENT_ID_SLOT_NAME);

        ValueListType valueList = new ValueListType();

        StringBuffer universalPatientID = new StringBuffer();
        universalPatientID.append(patientSearchData.getPatientId());
        universalPatientID.append("^^^&");
        universalPatientID.append(patientSearchData.getAssigningAuthorityID());
        universalPatientID.append("&ISO");

        valueList.getValue().add(universalPatientID.toString());

        //valueList.getValue().add("D123401^^^&1.1&ISO");

        patientIDSlot.setValueList(valueList);
        adhocQuery.getSlot().add(patientIDSlot);

        //Set Creation From Date
        SlotType1 creationStartTimeSlot = new SlotType1();
        creationStartTimeSlot.setName(CREATION_TIME_FROM_SLOT_NAME);

        ValueListType creationStartTimeValueList = new ValueListType();

        creationStartTimeValueList.getValue().add(formatDate(creationFromDate, HL7_DATE_FORMAT));

        creationStartTimeSlot.setValueList(creationStartTimeValueList);
        adhocQuery.getSlot().add(creationStartTimeSlot);

        // Set Creation To Date
        SlotType1 creationEndTimeSlot = new SlotType1();
        creationEndTimeSlot.setName(CREATION_TIME_TO_SLOT_NAME);

        ValueListType creationEndTimeSlotValueList = new ValueListType();

        creationEndTimeSlotValueList.getValue().add(formatDate(creationToDate, HL7_DATE_FORMAT));

        creationEndTimeSlot.setValueList(creationEndTimeSlotValueList);
        adhocQuery.getSlot().add(creationEndTimeSlot);

        adhocQuery.getSlot().add(createStatusTypeSlot());

        ResponseOptionType responseOption = new ResponseOptionType();
        responseOption.setReturnType("LeafClass");
        responseOption.setReturnComposedObjects(Boolean.FALSE);

        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        adhocQueryRequest.setResponseOption(responseOption);

        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);

        AssertionCreator assertionCreator = new AssertionCreator();

        request.setAssertion(assertionCreator.createAssertion());

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
            endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME);
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
        return PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
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