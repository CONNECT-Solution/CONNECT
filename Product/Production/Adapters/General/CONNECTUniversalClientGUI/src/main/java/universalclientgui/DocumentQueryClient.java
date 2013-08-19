/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
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

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docquery.entity.proxy.EntityDocQueryProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
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
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.apache.log4j.Logger;

/**
 * 
 * @author patlollav
 */
public class DocumentQueryClient {

    private static final Logger LOG = Logger.getLogger(DocumentQueryClient.class);
    private static final String HOME_ID = "urn:oid:2.16.840.1.113883.3.200";
    private static final String ID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    private static final String PATIENT_ID_SLOT_NAME = "$XDSDocumentEntryPatientId";
    private static final String DOCUMENT_STATUS_SLOT_NAME = "$XDSDocumentEntryStatus";
    private static final String CREATION_TIME_FROM_SLOT_NAME = "$XDSDocumentEntryCreationTimeFrom";
    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
    private static final String DOCUMENT_STATUS_APPROVED = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')";
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";

    private static final String SERVICE_NAME = NhincConstants.ENTITY_DOC_QUERY_PROXY_SERVICE_NAME;
   

    /**
     * 
     * @param patientSearchData
     * @param creationFromDate
     * @param creationToDate
     * @return
     */
    public List<DocumentInformation> retrieveDocumentsInformation(PatientSearchData patientSearchData) {

        String url;
        try {
            url = getUrl();
            if (NullChecker.isNotNullish(url)) {

                RespondingGatewayCrossGatewayQueryRequestType request = createAdhocQueryRequest(patientSearchData);
                NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
                request.setNhinTargetCommunities(target);
                EntityDocQueryProxyWebServiceUnsecuredImpl instance = new EntityDocQueryProxyWebServiceUnsecuredImpl();
                AdhocQueryResponse response = instance.respondingGatewayCrossGatewayQuery(
                        request.getAdhocQueryRequest(), request.getAssertion(), request.getNhinTargetCommunities());

                return convertAdhocQueryResponseToDocInfoBO(response);
            } else {
                LOG.error("Error getting URL for " + SERVICE_NAME);
            }
        } catch (Exception ex) {
            LOG.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 
     * @param patientSearchData
     * @param creationFromDate
     * @param creationToDate
     * @return
     */
    private RespondingGatewayCrossGatewayQueryRequestType createAdhocQueryRequest(PatientSearchData patientSearchData) {
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQuery.setHome(HOME_ID);
        adhocQuery.setId(ID);

        // Set patient id
        SlotType1 patientIDSlot = new SlotType1();
        patientIDSlot.setName(PATIENT_ID_SLOT_NAME);

        ValueListType valueList = new ValueListType();

        StringBuilder universalPatientID = new StringBuilder();
        universalPatientID.append(patientSearchData.getPatientId());
        universalPatientID.append("^^^&");
        universalPatientID.append(patientSearchData.getAssigningAuthorityID());
        universalPatientID.append("&ISO");

        valueList.getValue().add(universalPatientID.toString());

        // Populate $XDSDocumentEntryStatus slot to address Gateway-166
        patientIDSlot.setValueList(valueList);
        adhocQuery.getSlot().add(patientIDSlot);

        SlotType1 documentEntryStatusSlot = new SlotType1();
        documentEntryStatusSlot.setName(DOCUMENT_STATUS_SLOT_NAME);
        ValueListType valueEntryStatusList = new ValueListType();
        valueEntryStatusList.getValue().add(DOCUMENT_STATUS_APPROVED);
        documentEntryStatusSlot.setValueList(valueEntryStatusList);
        adhocQuery.getSlot().add(documentEntryStatusSlot);

        // Set Creation From Date
        SlotType1 creationStartTimeSlot = new SlotType1();
        creationStartTimeSlot.setName(CREATION_TIME_FROM_SLOT_NAME);

        ValueListType creationStartTimeValueList = new ValueListType();

       // creationStartTimeValueList.getValue().add(formatDate(creationFromDate, HL7_DATE_FORMAT));

        creationStartTimeSlot.setValueList(creationStartTimeValueList);
        adhocQuery.getSlot().add(creationStartTimeSlot);

        // Set Creation To Date
        SlotType1 creationEndTimeSlot = new SlotType1();
        creationEndTimeSlot.setName(CREATION_TIME_TO_SLOT_NAME);

        ValueListType creationEndTimeSlotValueList = new ValueListType();

       // creationEndTimeSlotValueList.getValue().add(formatDate(creationToDate, HL7_DATE_FORMAT));

        creationEndTimeSlot.setValueList(creationEndTimeSlotValueList);
        adhocQuery.getSlot().add(creationEndTimeSlot);

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
        
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        request.setNhinTargetCommunities(targetCommunities);
        
        return request;
    }
    
    protected String getUrl() throws ConnectionManagerException {
        return ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(SERVICE_NAME);
    }
   
    /**
     * 
     * @param response
     * @return
     */
    private List<DocumentInformation> convertAdhocQueryResponseToDocInfoBO(AdhocQueryResponse response) {

        List<DocumentInformation> documentInfoList = new ArrayList<DocumentInformation>();

        if (response == null) {
            LOG.debug("AdhocQueryResponse is null");
            return documentInfoList;
        }

        if (response.getRegistryObjectList() == null || response.getRegistryObjectList().getIdentifiable() == null) {
            LOG.debug("AdhocQueryResponse is null");
            return documentInfoList;
        }

        List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = response.getRegistryObjectList()
                .getIdentifiable();

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

    private String extractRespositoryUniqueID(ExtrinsicObjectType extrinsicObject) {
        return extractSingleSlotValue(extrinsicObject, "repositoryUniqueId");
    }

    private String extractDocumentType(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject,
                "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");

        String documentTypeCode = classification.getName().getLocalizedString().get(0).getValue();
        return documentTypeCode;
    }

    private String extractDocumentTitle(ExtrinsicObjectType extrinsicObject) {

        String documentTitle = null;

        if (extrinsicObject != null && extrinsicObject.getName() != null) {
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

        ExternalIdentifierType identifier = extractIndentifierType(extrinsicObject,
                "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");

        if (identifier != null) {
            documentID = identifier.getValue();
        }

        return documentID;
    }

    private String extractInstitution(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject,
                "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");

        String institution = null;

        if (classification != null && classification.getSlot() != null && !classification.getSlot().isEmpty()) {
            for (SlotType1 slot : classification.getSlot()) {
                if (slot != null && slot.getName().contentEquals("authorInstitution")) {
                    if (slot.getValueList() != null && slot.getValueList().getValue() != null
                            && !slot.getValueList().getValue().isEmpty()) {
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

    private ExternalIdentifierType extractIndentifierType(ExtrinsicObjectType extrinsicObject,
            String identificationScheme) {
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
            if (classificationItem != null
                    && classificationItem.getClassificationScheme().contentEquals(classificationScheme)) {
                classification = classificationItem;
                break;
            }
        }

        return classification;
    }

    /**
     * Format date in the String format using UTCDateUtil class.
     * 
     * @param dateString
     * @param outputFormat
     * @return
     */
    private String formatDate(String dateString, String outputFormat) {
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
