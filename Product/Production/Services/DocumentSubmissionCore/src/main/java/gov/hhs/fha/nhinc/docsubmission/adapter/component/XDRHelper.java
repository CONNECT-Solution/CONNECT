/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.fha.nhinc.docsubmission.adapter.component.routing.RoutingObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author dunnek
 */
public class XDRHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XDRHelper.class);

    public static final String XDR_EC_XDSMissingDocument = "XDSMissingDocument";
    public static final String XDR_EC_XDSMissingDocumentMetadata = "XDSMissingDocumentMetadata";
    public static final String XDR_EC_XDSNonIdenticalHash = "XDSNonIdenticalHash";
    public static final String XDR_EC_XDSRegistryDuplicateUniqueIdInMessage = "XDSRegistryDuplicateUniqueIdInMessage";
    public static final String XDR_EC_XDSRegistryBusy = "XDSRegistryBusy";
    public static final String XDR_EC_XDSRegistryMetadataError = "XDSRegistryMetadataError";
    public static final String XDR_EC_XDSUnknownPatientId = "XDSUnknownPatientId";
    public static final String XDR_EC_XDSPatientIdDoesNotMatch = "XDSPatientIdDoesNotMatch";

    public static final String XDS_RETRIEVE_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_AVAILABLILTY_STATUS_APPROVED = "Active";
    public static final String XDS_STATUS = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
    public static final String XDS_STATUS_ONLINE = "Online";
    public static final String XDS_STATUS_OFFLINE = "Offline";
    public static final String XDS_NAME = "Name";
    public static final String XDS_CLASSIFIED_OBJECT = "classifiedObject"; // this is the reference to the
    // extrinsicObject/document element
    public static final String XDS_NODE_REPRESENTATION = "nodeRepresentation"; // this the actual code in a
    // classification element
    public static final String XDS_CLASSIFICATION_ID = "id"; // this is the id of the classification element
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
    public static final String XDS_ASSOCIATION_TYPE_REPLACE = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";

    public RegistryResponseType createErrorResponse(RegistryErrorList errorList) {
        RegistryResponseType result = new RegistryResponseType();
        LOG.debug("begin createErrorResponse()");
        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        result.setRegistryErrorList(errorList);

        return result;
    }

    public RegistryResponseType createPositiveAck() {
        RegistryResponseType result = new RegistryResponseType();

        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);

        return result;
    }

    public RegistryErrorList validateDocumentMetaData(ProvideAndRegisterDocumentSetRequestType body) {
        RegistryErrorList result = new RegistryErrorList();

        LOG.debug("begin validateDocumentMetaData()");
        if (body == null) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                "ProvideAndRegisterDocumentSetRequestType was null");

            result.getRegistryError().add(error);

            // Request message was null, cannot continue. Return result.
            return processErrorList(result);
        }
        if (body.getDocument() == null) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                "ProvideAndRegisterDocumentSetRequestType did not contain a DocumentList");
            result.getRegistryError().add(error);
        } else if (body.getDocument().size() == 0) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                "DocumentList did not contain any documents");
            result.getRegistryError().add(error);
        }

        if (result.getRegistryError().size() > 0) {
            return processErrorList(result);
        }

        RegistryObjectListType regList = body.getSubmitObjectsRequest().getRegistryObjectList();

        ArrayList<String> metaDocIds = new ArrayList<String>();
        ArrayList<String> metaPatIds = new ArrayList<String>();

        for (int x = 0; x < regList.getIdentifiable().size(); x++) {
            if (regList.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class)) {

                ExtrinsicObjectType extObj = (ExtrinsicObjectType) regList.getIdentifiable().get(x).getValue();
                String mimeType = extObj.getMimeType();
                if (isSupportedMimeType(mimeType) == false) {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocumentMetadata,
                        NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, "Unsupported Mime Type: " + mimeType);
                    result.getRegistryError().add(error);
                }
                String docId = extObj.getId();
                metaDocIds.add(docId);

                if (isDocIdPresent(body.getDocument(), docId) == false) {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                        "Document Id: " + docId + " exists in metadata with no corresponding attached document");
                    result.getRegistryError().add(error);
                }
                String localPatId = getPatientId(extObj.getSlot());

                if (localPatId.isEmpty()) {
                    RegistryError error = createRegistryError(XDR_EC_XDSUnknownPatientId, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                        "Patient ID referenced in metadata is not known to the Receiving NHIE");
                    result.getRegistryError().add(error);
                }
                metaPatIds.add(localPatId);
            }
        }

        if (patientIdsMatch(metaPatIds) == false) {
            RegistryError error = createRegistryError(XDR_EC_XDSPatientIdDoesNotMatch, NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                "Patient Ids do not match");
            result.getRegistryError().add(error);
        }

        return processErrorList(result);

    }

    public List<String> getIntendedRecepients(ProvideAndRegisterDocumentSetRequestType body) {

        List<String> result = new ArrayList<String>();

        LOG.debug("begin getIntendedRecepients()");
        if (body == null || body.getSubmitObjectsRequest() == null) {
            return null;
        }
        try {
            RegistryObjectListType regList = body.getSubmitObjectsRequest().getRegistryObjectList();

            for (int x = 0; x < regList.getIdentifiable().size(); x++) {
                if (regList.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class)) {
                    ExtrinsicObjectType extObj = (ExtrinsicObjectType) regList.getIdentifiable().get(x).getValue();

                    SlotType1 recipSlot = getNamedSlotItem(extObj.getSlot(), XDS_INTENDED_RECIPIENT_SLOT);
                    if (recipSlot != null) {
                        result = recipSlot.getValueList().getValue();
                    }

                }

            }
        } catch (Exception ex) {
            LOG.error("Unable to pull intended recipients: {}", ex.getLocalizedMessage(), ex);
        }

        LOG.debug("Found " + result.size() + " recipients");
        return result;
    }

    public List<String> getRoutingBeans(List<String> intendedRecipients) {
        ArrayList<String> result = new ArrayList<String>();

        ConfigurationManager configMgr = new ConfigurationManager();

        Config config = configMgr.loadConfiguration();

        for (String recipient : intendedRecipients) {
            // Loop through List of configured beans
            for (RoutingConfig rc : config.getRoutingInfo()) {
                if (rc.getRecepient().equalsIgnoreCase(recipient)) {
                    if (result.contains(rc.getBean()) == false) {
                        result.add(rc.getBean());
                    }
                    break;
                }

            }
        }

        if (result.isEmpty()) {
            result.add(RoutingObjectFactory.BEAN_REFERENCE_IMPLEMENTATION);
        }

        LOG.debug("Found " + result.size() + " beans");
        return result;
    }

    protected boolean checkIdsMatch() {
        boolean checkIds = false;

        try {
            checkIds = PropertyAccessor.getInstance().getPropertyBoolean("adapter", "XDR.CheckPatientIdsMatch");
        } catch (Exception ex) {
            LOG.error("Unable to load XDR.CheckPatientIdsMatch: {}", ex.getLocalizedMessage(), ex);
        }

        return checkIds;
    }

    protected boolean isSupportedMimeType(String mimeType) {
        String[] mimeArray = getSupportedMimeTypes();
        boolean result = false;

        for (int x = 0; x < mimeArray.length; x++) {
            if (mimeArray[x].equalsIgnoreCase(mimeType)) {
                result = true;
                break;
            }
        }

        return result;
    }

    protected String[] getSupportedMimeTypes() {
        String[] mimeArray = new String[0];

        try {
            String list = PropertyAccessor.getInstance().getProperty("adapter", "XDR.SupportedMimeTypes");
            mimeArray = list.split(";");

        } catch (Exception ex) {
            LOG.trace("Error getting supported mime types: {}", ex.getLocalizedMessage(), ex);
        }

        return mimeArray;
    }

    private boolean isDocIdPresent(List<ProvideAndRegisterDocumentSetRequestType.Document> documents, String docId) {
        boolean result = false;

        for (ProvideAndRegisterDocumentSetRequestType.Document doc : documents) {
            if (doc.getId().equals(docId)) {
                result = true;
            }
        }

        return result;
    }

    private RegistryError createRegistryError(String errorCode, String severity, String codeContext) {
        RegistryError result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();

        result.setSeverity(severity);
        result.setCodeContext(codeContext);
        result.setErrorCode(errorCode);

        return result;
    }

    public String getSubmissionSetPatientId(ProvideAndRegisterDocumentSetRequestType body) {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            System.out.println(object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class)) {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                System.out.println(registryPackage.getSlot().size());

                for (int y = 0; y < registryPackage.getExternalIdentifier().size(); y++) {
                    String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0)
                        .getValue();
                    if (test.equals("XDSSubmissionSet.patientId")) {
                        result = registryPackage.getExternalIdentifier().get(y).getValue();
                    }

                }

            }
        }

        return result;
    }

    public String getSourcePatientId(ProvideAndRegisterDocumentSetRequestType body) {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            System.out.println(object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class)) {
                ExtrinsicObjectType extObj = (ExtrinsicObjectType) object.getIdentifiable().get(x).getValue();

                System.out.println(extObj.getSlot().size());

                SlotType1 slot = getNamedSlotItem(extObj.getSlot(), "sourcePatientId");

                if (slot != null) {
                    if (slot.getValueList() != null) {
                        if (slot.getValueList().getValue().size() == 1) {
                            result = slot.getValueList().getValue().get(0);
                        }
                    }
                }

            }
        }

        return result;
    }

    private String getPatientId(List<SlotType1> slots) {
        String result = "";
        SlotType1 patientIdSlot;

        patientIdSlot = getNamedSlotItem(slots, XDS_SOURCE_PATIENT_ID_SLOT);

        if (patientIdSlot != null) {
            if (patientIdSlot.getValueList().getValue().size() == 1) {
                result = patientIdSlot.getValueList().getValue().get(0);
            }
        }

        return result;

    }

    private SlotType1 getNamedSlotItem(List<SlotType1> slots, String name) {
        SlotType1 result = null;

        LOG.debug("begin getNamedSlotItem()");
        for (SlotType1 slot : slots) {
            if (slot.getName().equalsIgnoreCase(name)) {
                result = slot;
                LOG.info("Slot=" + result.getName());
                break;
            }
        }

        return result;
    }

    private boolean patientIdsMatch(List<String> patIds) {
        boolean result = true;

        if (checkIdsMatch()) {
            if (patIds.size() > 1) {
                // Get the first id
                String patId = patIds.get(0);
                // loop through all ids, make sure they all equal
                for (String id : patIds) {
                    if (id.equalsIgnoreCase(patId) == false) {
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private RegistryErrorList processErrorList(RegistryErrorList list) {
        int highestError = 0;

        if (list == null) {
            return null;
        }
        for (RegistryError error : list.getRegistryError()) {
            int currentError = getErrorRanking(error.getSeverity());

            if (currentError > highestError) {
                highestError = currentError;
            }

        }

        list.setHighestSeverity(getErrorDescription(highestError));

        return list;
    }

    private String getErrorDescription(int rank) {
        String result;

        switch (rank) {
            case 0: {
                result = "";
                break;
            }
            case 1: {
                result = NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_WARNING;
                break;
            }
            case 2:
            case 3: {
                result = NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR;
                break;
            }
            default: {
                result = "";
                break;
            }
        }

        return result;
    }

    private int getErrorRanking(String severity) {
        int result;

        if (severity.equalsIgnoreCase("")) {
            result = 0;
        } else if (severity.equalsIgnoreCase(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_WARNING)) {
            result = 1;
        } else if (severity.equalsIgnoreCase(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR)) {
            result = 2;
        } else {
            result = -1;
        }

        return result;
    }

}
