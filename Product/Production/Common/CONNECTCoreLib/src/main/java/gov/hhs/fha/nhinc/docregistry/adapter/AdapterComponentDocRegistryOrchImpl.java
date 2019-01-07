/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.docregistry.adapter;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocRegistryOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocRegistryOrchImpl.class);
    private UTCDateUtil utcDateUtil = null;

    /*
     * The following constants are the parameters defined by IHE for FindDocuments in section 3.16.4.1.4.1 of the ITI
     * TF-2a specification (revision 6.0 dated August 10, 2009)
     */
    private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private static final String EBXML_DOCENTRY_CLASS_CODE = "$XDSDocumentEntryClassCode";
    private static final String EBXML_DOCENTRY_CLASS_CODE_SCHEME = "$XDSDocumentEntryClassCodeScheme";
    private static final String EBXML_DOCENTRY_CREATION_TIME_FROM = "$XDSDocumentEntryCreationTimeFrom";
    private static final String EBXML_DOCENTRY_CREATION_TIME_TO = "$XDSDocumentEntryCreationTimeTo";
    private static final String EBXML_DOCENTRY_SERVICE_START_TIME_FROM = "$XDSDocumentEntryServiceStartTimeFrom";
    private static final String EBXML_DOCENTRY_SERVICE_START_TIME_TO = "$XDSDocumentEntryServiceStartTimeTo";
    private static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM = "$XDSDocumentEntryServiceStopTimeFrom";
    private static final String EBXML_DOCENTRY_SERVICE_STOP_TIME_TO = "$XDSDocumentEntryServiceStopTimeTo";
    private static final String EBXML_EVENT_CODE_LIST = "$XDSDocumentEntryEventCodeList";
    private static final String EBXML_EVENT_CODE_LIST_SCHEME = "$XDSDocumentEntryEventCodeListScheme";
    private static final String EBXML_DOCENTRY_STATUS = "$XDSDocumentEntryStatus";
    private static final String EBXML_DOCENTRY_ENTRY_TYPE = "$XDSDocumentEntryType";
    // -- End IHE defined FindDocuments parameters --

    private static final String NHINC_CUSTOM_DOCUMENT_ID = "$XDSDocumentEntryUniqueId";

    private static final String EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME = "repositoryUniqueId";
    private static final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME
        = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String EBXML_RESPONSE_DOCID_NAME = "XDSDocumentEntry.uniqueId";
    private static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME
        = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
    private static final String EBXML_RESPONSE_PATIENTID_NAME = "XDSDocumentEntry.patientId";
    private static final String EBXML_RESPONSE_AUTHOR_CLASS_SCHEME = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    private static final String EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME = "authorPerson";
    private static final String EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME = "authorInstitution";
    private static final String EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME = "authorRole";
    private static final String EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME = "authorSpecialty";
    private static final String EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    private static final String EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME
        = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    private static final String EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    private static final String EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    private static final String EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME
        = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    private static final String EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME
        = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    private static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    private static final String EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME = "codingScheme";
    private static final String EBXML_RESPONSE_CREATIONTIME_SLOTNAME = "creationTime";
    private static final String EBXML_RESPONSE_HASH_SLOTNAME = "hash";
    private static final String EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME = "urn:recipient:intendedRecipient";
    private static final String EBXML_RESPONSE_LANGUAGECODE_SLOTNAME = "languageCode";
    private static final String EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME = "legalAuthenticator";
    private static final String EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME = "serviceStartTime";
    private static final String EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME = "serviceStopTime";
    private static final String EBXML_RESPONSE_SIZE_SLOTNAME = "size";
    private static final String EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME = "sourcePatientId";
    private static final String EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME = "sourcePatientInfo";
    private static final String EBXML_RESPONSE_URI_SLOTNAME = "URI";
    private static final int EBXML_RESPONSE_URI_LINE_LENGTH = 128;

    private static final String XDS_QUERY_RESPONSE_STATUS_SUCCESS
        = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    private static final String EBXML_DOCENTRY_STABLE_DOCUMENTS_VALUE = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
    private static final String EBXML_DOCENTRY_ONDEMAND_DOCUMENTS_VALUE
        = "urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248";

    private static final String REPOSITORY_UNIQUE_ID = "1.1";

    // Properties file keys
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";

    private static final String EBXML_DOCQUERY_STORED_QUERY_ERROR = "XDSUnknownStoredQuery";
    private static final String EBXML_DOCQUERY_STORED_QUERY_MISSIN_PARAM = "XDSStoredQueryMissingParam";

    private static final String FINDDOCUMENTSQUERYID = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    private static final String EBXML_DOCQUERY_MULTIPLE_PATIENTID_SLOTS_ERROR = "XDSStoredQueryParamNumber";

    /**
     * Constructor.
     */
    public AdapterComponentDocRegistryOrchImpl() {
        utcDateUtil = createDateUtil();
    }

    /**
     * @return UTCDateUtil
     */
    protected UTCDateUtil createDateUtil() {
        return utcDateUtil != null ? utcDateUtil : new UTCDateUtil();
    }

    /**
     * @param request - AdhocQUeryRequest input Parameter.
     * @return response - This method outputs the AdhocQUeryResponse back.
     */
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        LOG.debug("Begin AdapterComponentDocRegistryOrchImpl.registryStoredQuery(...)");
        AdhocQueryResponse response;

        boolean registryIdPresent = false;
        DocumentQueryParams params = new DocumentQueryParams();
        if (request != null && getRegistryQueryId().contains(request.getAdhocQuery().getId())) {
            registryIdPresent = true;
        }
        boolean patientIdSlotPresent = false;
        if (registryIdPresent) {
            ObjectFactory queryObjFact = new ObjectFactory();
            response = queryObjFact.createAdhocQueryResponse();
            if (request != null) {

                List<SlotType1> slots = getSlotsFromAdhocQueryRequest(request);
                int patientIdSlotCount = 0;
                for (SlotType1 slot : slots) {
                    if (slot.getName().equals(EBXML_DOCENTRY_PATIENT_ID)) {
                        patientIdSlotCount++;
                    }
                }
                if (patientIdSlotCount > 1) {
                    response = createErrorResponse(EBXML_DOCQUERY_MULTIPLE_PATIENTID_SLOTS_ERROR,
                        "Multiple $XDSDocumentEntryPatientId Present in the Request");
                    return response;
                } else {

                    if (request.getAdhocQuery().getId().equals(FINDDOCUMENTSQUERYID)) {
                        List<String> slotValues;
                        for (SlotType1 slot : slots) {
                            if (slot.getName().equals(EBXML_DOCENTRY_PATIENT_ID)) {
                                patientIdSlotPresent = true;
                                slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
                                if (slotValues == null || slotValues.isEmpty()) {
                                    response = createErrorResponse(EBXML_DOCQUERY_STORED_QUERY_MISSIN_PARAM,
                                        "Required parameter XDSDocumentEntryPatientId, not present in query request");
                                    return response;
                                }
                            }
                        }
                        if (!patientIdSlotPresent) {
                            response = createErrorResponse(EBXML_DOCQUERY_STORED_QUERY_MISSIN_PARAM,
                                "Required parameter XDSDocumentEntryPatientId, not present in query request");
                            return response;
                        }
                    }
                    params = generateDocumentQueryParamsFromSlots(slots);
                }
            }

            DocumentService service = getDocumentService();

            List<DocumentMetadata> docs = new ArrayList<>();
            Boolean isOnDemand = params.getOnDemand();
            if (isOnDemand == null || !isOnDemand) {
                params.setOnDemandParams(Boolean.FALSE);
                docs.addAll(service.documentQuery(params));
            }

            if (isOnDemand == null || isOnDemand != null && isOnDemand) {
                params.setCreationTimeFrom(null);
                params.setCreationTimeTo(null);
                params.setOnDemandParams(Boolean.TRUE);
                docs.addAll(service.documentQuery(params));
            }

            LOG.debug("registryStoredQuery- docs.size: " + docs.size());
            loadResponseMessage(response, docs);
            LOG.debug("End AdapterComponentDocRegistryOrchImpl.registryStoredQuery(...)");
        } else {
            response = createErrorResponse(EBXML_DOCQUERY_STORED_QUERY_ERROR,
                "Unknown Stored Query query id =" + request.getAdhocQuery().getId());
        }
        return response;
    }

    /**
     * @param request - AdhocQUery Request i/p parameter.
     * @return SlotType1 - This method returns all the slots from Request.
     */
    protected List<SlotType1> getSlotsFromAdhocQueryRequest(AdhocQueryRequest request) {
        AdhocQueryType adhocQuery = request.getAdhocQuery();
        List<SlotType1> slots = null;
        if (adhocQuery != null) {
            slots = adhocQuery.getSlot();
        }

        return slots;
    }

    /**
     * @param slots - All the slots from request i/p Parameter.
     * @return DocumentQueryParams - This method outputs the DocumentQueryParams.
     */
    protected DocumentQueryParams generateDocumentQueryParamsFromSlots(List<SlotType1> slots) {
        String patientId = extractPatientIdentifier(slots);
        List<String> classCodeValues = extractClassCodes(slots);
        String classCodeScheme = extractClassCodeScheme(slots);
        Date creationTimeFrom = extractCreationTimeFrom(slots);
        Date creationTimeTo = extractCreationTimeTo(slots);
        Date serviceStartTimeFrom = extractServiceStartTimeFrom(slots);
        Date serviceStartTimeTo = extractServiceStartTimeTo(slots);
        Date serviceStopTimeFrom = extractServiceStopTimeFrom(slots);
        Date serviceStopTimeTo = extractServiceStopTimeTo(slots);
        List<String> statuses = extractStatuses(slots);
        List<String> documentUniqueIds = extractDocumentId(slots);
        List<String> eventCodeValues = extractEventCodeList(slots);
        List<String> eventCodeSchemeValues = extractEventCodeListSchemes(slots);
        DocumentQueryParams params = new DocumentQueryParams();
        params.setPatientId(patientId);
        params.setClassCodes(classCodeValues);
        params.setClassCodeScheme(classCodeScheme);
        params.setCreationTimeFrom(creationTimeFrom);
        params.setCreationTimeTo(creationTimeTo);
        params.setServiceStartTimeFrom(serviceStartTimeFrom);
        params.setServiceStartTimeTo(serviceStartTimeTo);
        params.setServiceStopTimeFrom(serviceStopTimeFrom);
        params.setServiceStopTimeTo(serviceStopTimeTo);
        params.setStatuses(statuses);
        params.setDocumentUniqueId(documentUniqueIds);
        params.setEventCodeParams(createEventCodeParameters(eventCodeValues, eventCodeSchemeValues));
        params.setSlots(slots);

        /*
         * params.setOnDemandParams(null) = both stable and on demand params.setOnDemandParams(false) = stable only
         * params.setOnDemandParams(true) = on demand only
         *
         * per the specification if no $XDSDocumentEntryType is in the query, the default behavior is stable only.
         */
        List<String> documentEntryTypes = extractDocumentEntryTypes(slots);
        if (documentEntryTypes == null || documentEntryTypes.isEmpty()) {
            // default case, stable only
            params.setOnDemandParams(false);
        } else {
            boolean onDemandFound = false;
            boolean stableFound = false;
            for (String s : documentEntryTypes) {
                if (StringUtils.contains(s, EBXML_DOCENTRY_ONDEMAND_DOCUMENTS_VALUE)) {
                    onDemandFound = true;
                }

                if (StringUtils.contains(s, EBXML_DOCENTRY_STABLE_DOCUMENTS_VALUE)) {
                    stableFound = true;
                }
            }

            if (onDemandFound && stableFound) {
                params.setOnDemandParams(null);
            } else {
                // if we found just one...
                params.setOnDemandParams(onDemandFound);
            }

        }
        return params;
    }

    /**
     * Guaranteed not to be null.
     *
     * @param slots
     * @return a list of strings with the types.
     */
    private List<String> extractDocumentEntryTypes(List<SlotType1> slots) {
        List<String> types = new ArrayList<>();
        for (SlotType1 slot : slots) {
            if (EBXML_DOCENTRY_ENTRY_TYPE.equals(slot.getName()) && slot.getValueList() != null) {
                types.addAll(slot.getValueList().getValue());
            }
        }
        return types;
    }

    /**
     * @return DocumentService
     */
    protected DocumentService getDocumentService() {
        return new DocumentService();
    }

    private List<EventCodeParam> createEventCodeParameters(List<String> eventCodeValues,
        List<String> eventCodeSchemeValues) {
        List<EventCodeParam> eventCodeParams = null;
        if (CollectionUtils.isNotEmpty(eventCodeValues)) {
            eventCodeParams = new ArrayList<>();
            boolean hasMatchingSchemes = CollectionUtils.isNotEmpty(eventCodeSchemeValues)
                && eventCodeValues.size() == eventCodeSchemeValues.size();
            for (int i = 0; i < eventCodeValues.size(); i++) {
                String eventCode = eventCodeValues.get(i);
                EventCodeParam eventCodeParam = new EventCodeParam();
                eventCodeParam.setEventCode(eventCode);
                if (hasMatchingSchemes) {
                    String eventCodeScheme = eventCodeSchemeValues.get(i);
                    eventCodeParam.setEventCodeScheme(eventCodeScheme);
                }
                eventCodeParams.add(eventCodeParam);
            }
        }

        return eventCodeParams;
    }

    private String extractPatientIdentifier(List<SlotType1> slots) {
        String patientId = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
        if (slotValues != null && !slotValues.isEmpty()) {
            String formattedPatientId = slotValues.get(0);
            patientId = PatientIdFormatUtil.stripQuotesFromPatientId(formattedPatientId);
            LOG.debug("extractPatientIdentifier - patientId: " + patientId);
        }
        return patientId;
    }

    private List<String> extractClassCodes(List<SlotType1> slots) {
        List<String> classCodes = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CLASS_CODE);
        if (slotValues != null && !slotValues.isEmpty()) {
            classCodes = new ArrayList<>();
            for (String slotValue : slotValues) {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodes;
    }

    private String extractClassCodeScheme(List<SlotType1> slots) {
        String classCodeScheme = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CLASS_CODE_SCHEME);
        if (slotValues != null && !slotValues.isEmpty()) {
            classCodeScheme = slotValues.get(0);
        }
        return classCodeScheme;
    }

    private Date extractCreationTimeFrom(List<SlotType1> slots) {
        Date creationTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CREATION_TIME_FROM);
        if (slotValues != null && !slotValues.isEmpty()) {
            creationTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return creationTimeFrom;
    }

    private Date extractCreationTimeTo(List<SlotType1> slots) {
        Date creationTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_CREATION_TIME_TO);
        if (slotValues != null && !slotValues.isEmpty()) {
            creationTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return creationTimeTo;
    }

    private Date extractServiceStartTimeFrom(List<SlotType1> slots) {
        Date serviceStartTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_START_TIME_FROM);
        if (slotValues != null && !slotValues.isEmpty()) {
            serviceStartTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStartTimeFrom;
    }

    private Date extractServiceStartTimeTo(List<SlotType1> slots) {
        Date serviceStartTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_START_TIME_TO);
        if (slotValues != null && !slotValues.isEmpty()) {
            serviceStartTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStartTimeTo;
    }

    private Date extractServiceStopTimeFrom(List<SlotType1> slots) {
        Date serviceStopTimeFrom = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM);
        if (slotValues != null && !slotValues.isEmpty()) {
            serviceStopTimeFrom = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStopTimeFrom;
    }

    private Date extractServiceStopTimeTo(List<SlotType1> slots) {
        Date serviceStopTimeTo = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_SERVICE_STOP_TIME_TO);
        if (slotValues != null && !slotValues.isEmpty()) {
            serviceStopTimeTo = utcDateUtil.parseUTCDateOptionalTimeZone(slotValues.get(0));
        }
        return serviceStopTimeTo;
    }

    private List<String> extractStatuses(List<SlotType1> slots) {
        List<String> statuses = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_STATUS);
        if (slotValues != null && !slotValues.isEmpty()) {
            statuses = new ArrayList<>();
            for (String slotValue : slotValues) {
                parseParamFormattedString(slotValue, statuses);
            }
        }
        return statuses;
    }

    /**
     * Extract the document ID from the slots, if it exists and put it into the array.
     *
     * @param slots The slots to be searched.
     * @return The document ID in a list if it is found.
     */
    private List<String> extractDocumentId(List<SlotType1> slots) {
        List<String> documentIds = null;
        String docId;
        List<String> slotValues = extractSlotValues(slots, NHINC_CUSTOM_DOCUMENT_ID);
        if (slotValues != null && !slotValues.isEmpty()) {
            // We should only have one - so use the first one.
            // -------------------------------------------------
            documentIds = new ArrayList<>();
            docId = StringUtil.extractStringFromTokens(slotValues.get(0).trim(), "'()");
            documentIds.add(docId);
        }
        return documentIds;
    }

    private List<String> extractEventCodeList(List<SlotType1> slots) {
        List<String> classCodes = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_EVENT_CODE_LIST);
        if (slotValues != null && !slotValues.isEmpty()) {
            classCodes = new ArrayList<>();
            for (String slotValue : slotValues) {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodes;
    }

    private List<String> extractEventCodeListSchemes(List<SlotType1> slots) {
        List<String> classCodes = null;
        List<String> slotValues = extractSlotValues(slots, EBXML_EVENT_CODE_LIST_SCHEME);
        if (slotValues != null && !slotValues.isEmpty()) {
            classCodes = new ArrayList<>();
            for (String slotValue : slotValues) {
                parseParamFormattedString(slotValue, classCodes);
            }
        }
        return classCodes;
    }

    /**
     * @param response - AdhocQueryResponse i/p Parameter.
     * @param docs - Documents i/p Parameter.
     */
    public void loadResponseMessage(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse response,
        List<DocumentMetadata> docs) {
        RegistryObjectListType regObjList = new RegistryObjectListType();
        response.setRegistryObjectList(regObjList);

        if (NullChecker.isNullish(docs)) {
            LOG.debug("loadResponseMessage - docs size: null");
            response.setStatus(XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        } else {
            LOG.debug("loadResponseMessage - docs size: " + docs.size());
            response.setStatus(XDS_QUERY_RESPONSE_STATUS_SUCCESS);

            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory
                = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            // Collect the home community id
            String homeCommunityId = retrieveHomeCommunityId();

            List<JAXBElement<? extends IdentifiableType>> olRegObjs = regObjList.getIdentifiable();

            // Save these so that theyu can be added in later after all of the other items..
            // ------------------------------------------------------------------------------
            ArrayList<JAXBElement<? extends IdentifiableType>> olObjRef = new ArrayList<>();
            ArrayList<JAXBElement<? extends IdentifiableType>> olAssoc = new ArrayList<>();

            for (DocumentMetadata doc : docs) {
                ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
                JAXBElement<? extends IdentifiableType> oJAXBExtObj = oRimObjectFactory.createExtrinsicObject(oExtObj);
                List<SlotType1> olSlot = oExtObj.getSlot();
                List<ClassificationType> olClassifications = oExtObj.getClassification();
                boolean bHaveData = false;

                oExtObj.setIsOpaque(Boolean.FALSE);
                if (doc.isOnDemand()) {
                    oExtObj.setObjectType(EBXML_DOCENTRY_ONDEMAND_DOCUMENTS_VALUE);
                } else {
                    oExtObj.setObjectType(EBXML_DOCENTRY_STABLE_DOCUMENTS_VALUE);
                }

                // Generate a UUID for the document
                String sDocumentUUID = generateId();
                oExtObj.setId(sDocumentUUID);

                // Document Unique ID
                // ------------------
                String sDocumentId; // need to keep a handle to this to be used later...
                if (NullChecker.isNotNullish(doc.getDocumentUniqueId())) {
                    sDocumentId = doc.getDocumentUniqueId();
                    ExternalIdentifierType oExtId = new ExternalIdentifierType();
                    oExtId.setId(generateId());
                    oExtObj.getExternalIdentifier().add(oExtId);
                    oExtId.setRegistryObject(sDocumentUUID);
                    oExtId.setValue(sDocumentId);
                    oExtId.setIdentificationScheme(EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME);
                    InternationalStringType oName = createSingleValueInternationalStringType(EBXML_RESPONSE_DOCID_NAME);
                    oExtId.setName(oName);
                    bHaveData = true;
                }

                // Author data
                boolean bHasAuthorData = false;
                ClassificationType oClassification = new ClassificationType();
                oClassification.setId(generateId());
                oClassification.setClassificationScheme(EBXML_RESPONSE_AUTHOR_CLASS_SCHEME);
                oClassification.setClassifiedObject(sDocumentUUID);
                oClassification.setNodeRepresentation("");
                List<SlotType1> olClassificationSlot = oClassification.getSlot();

                // AuthorPerson
                // -------------
                if (NullChecker.isNotNullish(doc.getAuthorPerson())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_AUTHOR_PERSON_SLOTNAME,
                        doc.getAuthorPerson());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }
                // AuthorInstitution
                // ------------------
                if (NullChecker.isNotNullish(doc.getAuthorInstitution())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_AUTHOR_INSTITUTION_SLOTNAME,
                        doc.getAuthorInstitution());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                // AuthorRole
                // ------------
                if (NullChecker.isNotNullish(doc.getAuthorRole())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_AUTHOR_ROLE_SLOTNAME, doc.getAuthorRole());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                // AuthorSpecialty
                // ----------------
                if (NullChecker.isNotNullish(doc.getAuthorSpecialty())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_AUTHOR_SPECIALTY_SLOTNAME,
                        doc.getAuthorSpecialty());
                    olClassificationSlot.add(oSlot);
                    bHasAuthorData = true;
                }

                if (bHasAuthorData) {
                    olClassifications.add(oClassification);
                    bHaveData = true;
                }

                // Availability Status
                // ---------------------
                if (NullChecker.isNotNullish(doc.getAvailablityStatus())) {
                    oExtObj.setStatus(doc.getAvailablityStatus());
                    bHaveData = true;
                }

                // Class Code
                // ------------
                ClassificationType classCodeClassification = createClassificationFromCodedData(doc.getClassCode(),
                    doc.getClassCodeScheme(), doc.getClassCodeDisplayName(), EBXML_RESPONSE_CLASSCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (classCodeClassification != null) {
                    olClassifications.add(classCodeClassification);
                    bHaveData = true;
                }

                // Comments
                // ---------
                if (NullChecker.isNotNullish(doc.getComments())) {
                    InternationalStringType oComments = createSingleValueInternationalStringType(doc.getComments());
                    oExtObj.setDescription(oComments);
                    bHaveData = true;
                }

                // Confidentiality Code
                // ---------------------
                ClassificationType confidentialityCodeClassification = createClassificationFromCodedData(
                    doc.getConfidentialityCode(), doc.getConfidentialityCodeScheme(),
                    doc.getConfidentialityCodeDisplayName(), EBXML_RESPONSE_CONFIDENTIALITYCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (confidentialityCodeClassification != null) {
                    olClassifications.add(confidentialityCodeClassification);
                    bHaveData = true;
                }

                // Creation Time
                // --------------
                if (doc.getCreationTime() != null) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_CREATIONTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getCreationTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Event Code List
                // ----------------
                if (doc.getEventCodes() != null && !doc.getEventCodes().isEmpty()) {
                    for (EventCode eventCode : doc.getEventCodes()) {
                        ClassificationType eventCodeClassification = createClassificationFromCodedData(
                            eventCode.getEventCode(), eventCode.getEventCodeScheme(),
                            eventCode.getEventCodeDisplayName(), EBXML_RESPONSE_EVENTCODE_CLASS_SCHEME, sDocumentUUID);
                        if (eventCodeClassification != null) {
                            olClassifications.add(eventCodeClassification);
                            bHaveData = true;
                        }
                    }
                }

                // Format Code
                // -------------
                ClassificationType formatCodeClassification = createClassificationFromCodedData(doc.getFormatCode(),
                    doc.getFormatCodeScheme(), doc.getFormatCodeDisplayName(), EBXML_RESPONSE_FORMATCODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (formatCodeClassification != null) {
                    olClassifications.add(formatCodeClassification);
                    bHaveData = true;
                }

                // Hash Code
                // ----------
                if (NullChecker.isNotNullish(doc.getHash())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_HASH_SLOTNAME, doc.getHash());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Healthcare Facility Type Code
                // ------------------------------
                ClassificationType healthcareFacilityTypeCodeClassification = createClassificationFromCodedData(
                    doc.getFacilityCode(), doc.getFacilityCodeScheme(), doc.getFacilityCodeDisplayName(),
                    EBXML_RESPONSE_HEALTHCAREFACILITYTYPE_CLASS_SCHEME, sDocumentUUID);
                if (healthcareFacilityTypeCodeClassification != null) {
                    olClassifications.add(healthcareFacilityTypeCodeClassification);
                    bHaveData = true;
                }

                // Intended Recipients
                // --------------------
                List<String> intendedRecipients = new ArrayList<>();
                if (NullChecker.isNotNullish(doc.getIntendedRecipientPerson())) {
                    intendedRecipients.add(doc.getIntendedRecipientPerson());
                } else if (NullChecker.isNotNullish(doc.getIntendedRecipientOrganization())) {
                    intendedRecipients.add(doc.getIntendedRecipientOrganization());
                }

                if (!intendedRecipients.isEmpty()) {
                    String[] intendedRecipientArray = intendedRecipients.toArray(new String[intendedRecipients.size()]);
                    SlotType1 oSlot = createMultiValueSlot(EBXML_RESPONSE_INTENDEDRECIPIENTS_SLOTNAME,
                        intendedRecipientArray);
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Language Code
                // ---------------
                if (NullChecker.isNotNullish(doc.getLanguageCode())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_LANGUAGECODE_SLOTNAME,
                        doc.getLanguageCode());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // LegalAuthenticator Code
                // ------------------------
                if (NullChecker.isNotNullish(doc.getLegalAuthenticator())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_LEGALAUTHENTICATOR_SLOTNAME,
                        doc.getLegalAuthenticator());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Mime Type
                // ----------
                if (NullChecker.isNotNullish(doc.getMimeType())) {
                    oExtObj.setMimeType(doc.getMimeType());
                    bHaveData = true;
                }

                // Patient ID
                // -----------
                if (NullChecker.isNotNullish(doc.getPatientId())) {
                    String formattedPatientId = doc.getPatientId();
                    ExternalIdentifierType oExtId = new ExternalIdentifierType();
                    oExtId.setId(generateId());
                    oExtId.setIdentificationScheme(EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME);
                    InternationalStringType oPatIdName = createSingleValueInternationalStringType(
                        EBXML_RESPONSE_PATIENTID_NAME);
                    oExtId.setName(oPatIdName);
                    oExtId.setRegistryObject(sDocumentUUID);
                    oExtId.setValue(formattedPatientId);
                    oExtObj.getExternalIdentifier().add(oExtId);
                    bHaveData = true;
                }

                // Practice Setting Code
                // ----------------------
                ClassificationType practiceSettingCodeClassification = createClassificationFromCodedData(
                    doc.getPracticeSetting(), doc.getPracticeSettingScheme(), doc.getPracticeSettingDisplayName(),
                    EBXML_RESPONSE_PRACTICESETTING_CLASS_SCHEME, sDocumentUUID);
                if (practiceSettingCodeClassification != null) {
                    olClassifications.add(practiceSettingCodeClassification);
                    bHaveData = true;
                }

                // Service Start Time
                // -------------------
                if (doc.getServiceStartTime() != null) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_SERVICESTARTTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getServiceStartTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Service Stop Time
                // ------------------
                if (doc.getServiceStopTime() != null) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_SERVICESTOPTIME_SLOTNAME,
                        utcDateUtil.formatUTCDate(doc.getServiceStopTime()));
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Size
                // -----
                if (doc.getSize() != null && doc.getSize() > 0) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_SIZE_SLOTNAME, doc.getSize().toString());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Source Patient Id
                // ------------------
                if (NullChecker.isNotNullish(doc.getSourcePatientId())) {
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_SOURCEPATIENTID_SLOTNAME,
                        doc.getSourcePatientId());
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Source Patient Info
                // --------------------
                List<String> sourcePatientInfoValues = new ArrayList<>();
                if (NullChecker.isNotNullish(doc.getPid3())) {
                    sourcePatientInfoValues.add("PID-3|" + doc.getPid3());
                }
                if (NullChecker.isNotNullish(doc.getPid5())) {
                    sourcePatientInfoValues.add("PID-5|" + doc.getPid5());
                }
                if (NullChecker.isNotNullish(doc.getPid7())) {
                    sourcePatientInfoValues.add("PID-7|" + doc.getPid7());
                }
                if (NullChecker.isNotNullish(doc.getPid8())) {
                    sourcePatientInfoValues.add("PID-8|" + doc.getPid8());
                }
                if (NullChecker.isNotNullish(doc.getPid11())) {
                    sourcePatientInfoValues.add("PID-11|" + doc.getPid11());
                }

                if (!sourcePatientInfoValues.isEmpty()) {
                    String[] sourcePatientInfoValuesArray = sourcePatientInfoValues
                        .toArray(new String[sourcePatientInfoValues.size()]);
                    SlotType1 oSlot = createMultiValueSlot(EBXML_RESPONSE_SOURCEPATIENTINFO_SLOTNAME,
                        sourcePatientInfoValuesArray);
                    olSlot.add(oSlot);
                    bHaveData = true;
                }

                // Title
                // -------
                if (NullChecker.isNotNullish(doc.getDocumentTitle())) {
                    InternationalStringType oTitle = createSingleValueInternationalStringType(doc.getDocumentTitle());
                    oExtObj.setName(oTitle);
                    bHaveData = true;
                }

                // Type Code
                // ----------
                ClassificationType typeCodeClassification = createClassificationFromCodedData(doc.getTypeCode(),
                    doc.getTypeCodeScheme(), doc.getTypeCodeDisplayName(), EBXML_RESPONSE_TYPECODE_CLASS_SCHEME,
                    sDocumentUUID);
                if (typeCodeClassification != null) {
                    olClassifications.add(typeCodeClassification);
                    bHaveData = true;
                }

                // URI
                // ----
                if (NullChecker.isNotNullish(doc.getDocumentUri())) {
                    SlotType1 oSlot;
                    String documentUri = doc.getDocumentUri();
                    if (documentUri.length() <= EBXML_RESPONSE_URI_LINE_LENGTH) {
                        oSlot = createSingleValueSlot(EBXML_RESPONSE_URI_SLOTNAME, documentUri);
                    } else {
                        int iStart = 0;
                        int iTotalLen = documentUri.length();
                        int iIndex = 1;
                        String[] saURIPart;

                        if (iTotalLen % EBXML_RESPONSE_URI_LINE_LENGTH == 0) {
                            saURIPart = new String[iTotalLen / EBXML_RESPONSE_URI_LINE_LENGTH];
                        } else {
                            saURIPart = new String[iTotalLen / EBXML_RESPONSE_URI_LINE_LENGTH + 1];
                        }
                        while (iStart < iTotalLen) {
                            if (iStart + EBXML_RESPONSE_URI_LINE_LENGTH > iTotalLen) {
                                saURIPart[iIndex - 1] = iIndex + "|" + documentUri.substring(iStart, iTotalLen);
                                iStart = iTotalLen;
                            } else {
                                saURIPart[iIndex - 1] = iIndex + "|"
                                    + documentUri.substring(iStart, iStart + EBXML_RESPONSE_URI_LINE_LENGTH);
                                iStart += EBXML_RESPONSE_URI_LINE_LENGTH;
                            }
                            iIndex++;
                        }

                        oSlot = createMultiValueSlot(EBXML_RESPONSE_URI_SLOTNAME, saURIPart);
                    } // else

                    if (oSlot != null) {
                        olSlot.add(oSlot);
                        bHaveData = true;
                    }
                }

                if (bHaveData) {
                    // Home community ID
                    // ------------------
                    oExtObj.setHome(homeCommunityId);

                    // Repository Unique ID
                    // ---------------------
                    SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_REPOSITORY_UNIQUE_ID_SLOTNAME,
                        getRepositoryUniqueId());
                    olSlot.add(oSlot);

                    olRegObjs.add(oJAXBExtObj);

                }
            }
            // if we have any Object References, add them in now.
            // ---------------------------------------------------
            if (CollectionUtils.isNotEmpty(olObjRef)) {
                olRegObjs.addAll(olObjRef);
            }

            // if we have any associations, add them in now.
            // ---------------------------------------------------
            if (CollectionUtils.isNotEmpty(olAssoc)) {
                olRegObjs.addAll(olAssoc);
            }

        }

    }

    /**
     * This method creates a classification from a coded item.
     *
     * @param oCoded The coded to be transformed.
     * @param sClassificationScheme The classification scheme value.
     * @param sDocumentId The document ID for the document associated with this classificaation.
     * @return The classification created based on the information in the coded.
     */
    private ClassificationType createClassificationFromCodedData(String code, String codeScheme, String codeDisplayName,
        String sClassificationScheme, String sDocumentId) {
        LOG.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- Begin");
        ClassificationType oClassification = new ClassificationType();
        oClassification.setId(generateId());
        boolean bHasCode = false;
        oClassification.setClassificationScheme(sClassificationScheme);
        oClassification.setClassifiedObject(sDocumentId);
        oClassification.setNodeRepresentation("");
        List<SlotType1> olClassificationSlot = oClassification.getSlot();

        // Code
        // -----
        if (NullChecker.isNotNullish(code)) {
            oClassification.setNodeRepresentation(code);
            bHasCode = true;
        }

        // Code System
        // ------------
        if (NullChecker.isNotNullish(codeScheme)) {
            SlotType1 oSlot = createSingleValueSlot(EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME, codeScheme);
            olClassificationSlot.add(oSlot);
            bHasCode = true;
        }

        // DisplayName
        // ------------
        if (NullChecker.isNotNullish(codeDisplayName)) {
            InternationalStringType oDisplayName = createSingleValueInternationalStringType(codeDisplayName);
            oClassification.setName(oDisplayName);
            bHasCode = true;
        }

        if (bHasCode) {
            return oClassification;
        } else {
            LOG.debug("DocumentRegistryHelper.CreateClassificationFromCodedData() -- End");
            return null;
        }
    }

    /**
     * @return homeCommunityId - Retrieve HomeCommunityId from gateway.properties
     */
    protected String retrieveHomeCommunityId() {
        String homeCommunityId = null;
        try {
            homeCommunityId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY,
                PROPERTY_FILE_KEY_HOME_COMMUNITY);
            if (homeCommunityId != null && !homeCommunityId.startsWith(NhincConstants.HCID_PREFIX)) {
                homeCommunityId = NhincConstants.HCID_PREFIX + homeCommunityId;
            }
        } catch (PropertyAccessException t) {
            LOG.error("Error retrieving the home community id: " + t.getMessage(), t);
        }
        return homeCommunityId;
    }

    private List<String> extractSlotValues(List<SlotType1> slots, String slotName) {
        List<String> returnValues;
        returnValues = new ArrayList<>();
        if (slots != null) {
            for (SlotType1 slot : slots) {
                if (StringUtils.isNotEmpty(slot.getName()) && slot.getValueList() != null
                    && CollectionUtils.isNotEmpty(slot.getValueList().getValue())) {

                    if (slot.getName().equals(slotName)) {
                        ValueListType valueListType = slot.getValueList();
                        List<String> slotValues = valueListType.getValue();
                        for (String slotValue : slotValues) {
                            returnValues.add(slotValue);
                        }
                    }
                }

            }
        }
        return returnValues;
    }

    /**
     * @param paramFormattedString - PatientIdFormattes String i/p Parameter.
     * @param resultCollection - Collection of PatientId's i/p Parameter.
     */
    public void parseParamFormattedString(String paramFormattedString, List<String> resultCollection) {
        if (paramFormattedString != null && resultCollection != null) {
            if (paramFormattedString.startsWith("(")) {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if (endIndex != -1) {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if (multiValueString != null) {
                    for (String element : multiValueString) {
                        String singleValue = element;
                        if (singleValue != null) {
                            singleValue = singleValue.trim();
                            if (singleValue.startsWith("'")) {
                                singleValue = singleValue.substring(1);
                                int endTickIndex = singleValue.indexOf("'");
                                if (endTickIndex != -1) {
                                    singleValue = singleValue.substring(0, endTickIndex);
                                }
                            }
                        }
                        resultCollection.add(singleValue);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Added single value: " + singleValue + " to query parameters");
                        }
                    }
                }
            } else {
                resultCollection.add(paramFormattedString);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
                }
            }
        }
    }

    /**
     * This method creates a Slot containing a single value.
     *
     * @param sSlotName The name of the slot.
     * @param sSlotValue The value for the slot.
     * @return The SlotType1 object containing the data passed in.
     */
    private SlotType1 createSingleValueSlot(String sSlotName, String sSlotValue) {
        LOG.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- Begin");
        String[] saSlotValue = new String[1];
        saSlotValue[0] = sSlotValue;
        LOG.debug("DocumentRegistryHelper.CreateSingleValueSlot() -- End");
        return createMultiValueSlot(sSlotName, saSlotValue);
    }

    /**
     * This method creates a Slot containing a single value.
     *
     * @param sSlotName The name of the slot.
     * @param saSlotValue The array of values for the slot.
     * @return The SlotType1 object containing the data passed in.
     */
    private SlotType1 createMultiValueSlot(String sSlotName, String[] saSlotValue) {
        LOG.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- Begin");
        SlotType1 oSlot = new SlotType1();
        oSlot.setName(sSlotName);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        List<String> olValue = oValueList.getValue();
        for (String element : saSlotValue) {
            olValue.add(element);
        }
        LOG.debug("DocumentRegistryHelper.CreateMultiValueSlot() -- End");
        return oSlot;
    }

    /**
     * This method creates an InternationalStringType with a single value.
     *
     * @param sLocStrValue The value to be placed in the string.
     * @return The InternationStringType that is being returned.
     */
    private InternationalStringType createSingleValueInternationalStringType(String sLocStrValue) {
        LOG.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- Begin");
        InternationalStringType oName = new InternationalStringType();
        List<LocalizedStringType> olLocStr = oName.getLocalizedString();
        LocalizedStringType oNameLocStr = new LocalizedStringType();
        olLocStr.add(oNameLocStr);
        oNameLocStr.setValue(sLocStrValue);
        LOG.debug("DocumentTransforms.CreateSingleValueInternationalStringType() -- End");
        return oName;
    }

    private oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse createErrorResponse(String errorCode,
        String codeContext) {
        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        response.setRegistryErrorList(regErrList);
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        RegistryObjectListType regObjectList = new RegistryObjectListType();
        response.setRegistryObjectList(regObjectList);

        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode(errorCode);
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        return response;
    }

    /**
     * @return registryQueryId - List of valid QueryId's defined in spec.
     */
    protected List<String> getRegistryQueryId() {
        List<String> registryQueryId = new ArrayList<>();
        registryQueryId.add("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        registryQueryId.add("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9");
        registryQueryId.add("urn:uuid:958f3006-baad-4929-a4de-ff1114824431");
        registryQueryId.add("urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3");
        registryQueryId.add("urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4");
        registryQueryId.add("urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4");
        registryQueryId.add("urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155");
        registryQueryId.add("urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a");
        registryQueryId.add("urn:uuid:51224314-5390-4169-9b91-b1980040715a");
        registryQueryId.add("urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83");
        registryQueryId.add("urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7");
        registryQueryId.add("urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578");
        registryQueryId.add("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6");
        return registryQueryId;
    }

    private static String generateId() {
        return MessageFormat.format("{0}{1}", NhincConstants.WS_SOAP_HEADER_MESSAGE_ID_PREFIX, UUID.randomUUID());
    }

    /**
     * Retrieve from adapter.properties. If it doesn't exist, return default value
     *
     * @return document repository ID from adapter properties
     */
    private static String getRepositoryUniqueId() {
        return PropertyAccessor.getInstance().getProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME,
            NhincConstants.XDS_REPOSITORY_ID, REPOSITORY_UNIQUE_ID);
    }
}
