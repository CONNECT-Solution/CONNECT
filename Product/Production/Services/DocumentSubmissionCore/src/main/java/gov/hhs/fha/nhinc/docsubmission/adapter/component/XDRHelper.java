/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.docsubmission.adapter.component.routing.RoutingObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class XDRHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XDRHelper.class);

    public static final String XDR_EC_XDSMissingDocument = "XDSMissingDocument";
    public static final String XDR_EC_XDSMissingDocumentMetadata = "XDSMissingDocumentMetadata";
    public static final String XDR_EC_XDSUnknownPatientId = "XDSUnknownPatientId";
    public static final String XDR_EC_XDSPatientIdDoesNotMatch = "XDSPatientIdDoesNotMatch";

    public static final String XDS_RETRIEVE_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_SOURCE_PATIENT_ID_SLOT = "sourcePatientId";
    public static final String XDS_INTENDED_RECIPIENT_SLOT = "intendedRecipient";

    public static RegistryResponseType createErrorResponse(RegistryErrorList errorList) {
        RegistryResponseType result = new RegistryResponseType();
        LOG.debug("begin createErrorResponse()");
        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
        result.setRegistryErrorList(errorList);

        return result;
    }

    public static RegistryResponseType createPositiveAck() {
        RegistryResponseType result = new RegistryResponseType();

        result.setStatus(XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);

        return result;
    }


    public RegistryErrorList validateDocumentMetaData(ProvideAndRegisterDocumentSetRequestType body) {
        RegistryErrorList result = new RegistryErrorList();

        LOG.debug("begin validateDocumentMetaData()");
        if (body == null) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,
                NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, "ProvideAndRegisterDocumentSetRequestType was null");

            result.getRegistryError().add(error);

            // Request message was null, cannot continue. Return result.
            return processErrorList(result);
        }

        if (validateDocumentData(body, result)) {
            return processErrorList(result);
        }

        validateSubmittedObjectsRequest(body.getSubmitObjectsRequest(), result, body.getDocument());

        return processErrorList(result);

    }

    /**
     * Validate SubmittedObjectRequest on a Document Submission or Document Data Submission. If docListToCheck is NULL
     * then there will be no validation done to see if the document id is present in the payload.
     *
     * Any RegistryErrors found will be added to the errorList parameter and the method will return true. If no
     * RegistryErrors are created, this method will return false.
     *
     * @param body
     * @param errorList
     * @param docListToCheck - Optional, may be NULL if this is a DDS call.
     */
    private boolean validateSubmittedObjectsRequest(SubmitObjectsRequest submittedObject,
        RegistryErrorList errorList, List<Document> docListToCheck) {

        boolean added = false;

        RegistryObjectListType regList = submittedObject.getRegistryObjectList();
        ArrayList<String> metaPatIds = new ArrayList<>();
        for (JAXBElement<? extends IdentifiableType> item : regList.getIdentifiable()) {
            if (item.getDeclaredType().equals(ExtrinsicObjectType.class)) {

                ExtrinsicObjectType extObj = (ExtrinsicObjectType) item.getValue();
                String mimeType = extObj.getMimeType();
                if (!isSupportedMimeType(mimeType)) {
                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocumentMetadata,
                        NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, "Unsupported Mime Type: " + mimeType);
                    errorList.getRegistryError().add(error);
                    added = true;
                }

                // Explicit check for NULL here. If docListToCheck is null, under the assumption this is a DSS
                // submission and not a DS submission. If it is not null, check if the id is present.
                // An empty list should enter this branch and add a registry error.
                String docId = extObj.getId();
                if (docListToCheck != null && !isDocIdPresent(docListToCheck, docId)) {

                    RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,
                        NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                        "Document Id: " + docId + " exists in metadata with no corresponding attached document");
                    errorList.getRegistryError().add(error);
                    added = true;
                }

                String localPatId = getPatientId(extObj.getSlot());
                if (localPatId.isEmpty()) {
                    RegistryError error = createRegistryError(XDR_EC_XDSUnknownPatientId,
                        NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                        "Patient ID referenced in metadata is not known to the Receiving NHIE");
                    errorList.getRegistryError().add(error);
                    added = true;
                }
                metaPatIds.add(localPatId);
            }
        }

        if (!patientIdsMatch(metaPatIds)) {
            RegistryError error = createRegistryError(XDR_EC_XDSPatientIdDoesNotMatch,
                NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, "Patient Ids do not match");
            errorList.getRegistryError().add(error);
            added = true;
        }

        return added;
    }

    /**
     * Validates that the body has a document attached to it. If it does not or if it is empty, a RegistryError will be
     * added to the error list.
     *
     * If there was an error added to the list, this method will return true. Otherwise this method will return false
     *
     * @param body
     * @param errorList
     */
    private static boolean validateDocumentData(ProvideAndRegisterDocumentSetRequestType body,
        RegistryErrorList errorList) {
        boolean result = false;
        if (body.getDocument() == null) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,
                NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR,
                "ProvideAndRegisterDocumentSetRequestType did not contain a DocumentList");
            errorList.getRegistryError().add(error);
            result = true;
        } else if (body.getDocument().isEmpty()) {
            RegistryError error = createRegistryError(XDR_EC_XDSMissingDocument,
                NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, "DocumentList did not contain any documents");
            errorList.getRegistryError().add(error);
            result = true;
        }

        return result;
    }

    public static List<String> getIntendedRecepients(ProvideAndRegisterDocumentSetRequestType body) {

        List<String> result = new ArrayList<>();

        LOG.debug("begin getIntendedRecepients()");
        if (body != null && body.getSubmitObjectsRequest() != null) {
            RegistryObjectListType regList = body.getSubmitObjectsRequest().getRegistryObjectList();

            for (int x = 0; x < regList.getIdentifiable().size(); x++) {
                if (regList.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class)) {
                    ExtrinsicObjectType extObj = (ExtrinsicObjectType) regList.getIdentifiable().get(x).getValue();

                    SlotType1 recipSlot = getNamedSlotItem(extObj.getSlot(), XDS_INTENDED_RECIPIENT_SLOT);
                    if (recipSlot != null) {
                        result = recipSlot.getValueList().getValue();
                        LOG.info("Intended Recipients: {}", result);
                    }

                }

            }
        }

        LOG.debug("Found {} recipients", result.size());
        return result;
    }

    /**
     * Grabs the intended recipient bean names as defined in XDRConfiguration.xml and attempts to load them as beans
     * defined in DocumentSubmissionProxyConfig.xml
     *
     * If no intended recipients are found, "reference" will be added by default.
     *
     * @param List of recipients. Possible values defined in XDRConfiguration.xml
     * @return List of corresponding bean names for the given recipient list
     */
    public static List<String> getRoutingBeans(List<String> intendedRecipients) {
        ArrayList<String> result = new ArrayList<>();

        ConfigurationManager configMgr = new ConfigurationManager();

        Config config = configMgr.loadConfiguration();

        for (String recipient : intendedRecipients) {
            // Loop through List of configured beans
            for (RoutingConfig rc : config.getRoutingInfo()) {
                if (rc.getRecepient().equalsIgnoreCase(recipient)) {
                    if (!result.contains(rc.getBean())) {
                        result.add(rc.getBean());
                    }
                    break;
                }
            }
        }

        if (result.isEmpty()) {
            result.add(RoutingObjectFactory.BEAN_REFERENCE_IMPLEMENTATION);
        }

        LOG.debug("Found {} beans", result.size());
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

        for (String element : mimeArray) {
            if (element.equalsIgnoreCase(mimeType)) {
                result = true;
                break;
            }
        }

        return result;
    }

    protected static String[] getSupportedMimeTypes() {
        String[] mimeArray = new String[0];

        try {
            String list = PropertyAccessor.getInstance().getProperty("adapter", "XDR.SupportedMimeTypes");
            mimeArray = list.split(";");

        } catch (Exception ex) {
            LOG.trace("Error getting supported mime types: {}", ex.getLocalizedMessage(), ex);
        }

        return mimeArray;
    }

    private static boolean isDocIdPresent(List<Document> documents, String docId) {
        boolean result = false;

        if (CollectionUtils.isEmpty(documents)) {
            return false;
        } else {
            for (Document doc : documents) {
                if (doc.getId().equals(docId)) {
                    result = true;
                }
            }
            return result;
        }
    }

    private static RegistryError createRegistryError(String errorCode, String severity, String codeContext) {
        RegistryError result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();

        result.setSeverity(severity);
        result.setCodeContext(codeContext);
        result.setErrorCode(errorCode);

        return result;
    }

    public static String getSubmissionSetPatientId(ProvideAndRegisterDocumentSetRequestType body) {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            LOG.debug("List of Identifiable Registry Object On Submission Patient ID {}",
                object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class)) {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();

                LOG.debug("Slot(s) in registry Package is {}", registryPackage.getSlot().size());

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

    public static String getSourcePatientId(ProvideAndRegisterDocumentSetRequestType body) {
        String result = "";

        RegistryObjectListType object = body.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            LOG.debug("List of Identifiable Registry Object On Source Patient ID {}",
                object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(ExtrinsicObjectType.class)) {
                ExtrinsicObjectType extObj = (ExtrinsicObjectType) object.getIdentifiable().get(x).getValue();

                LOG.debug("Slot(s) in registry Package is {}", extObj.getSlot().size());

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

    private static String getPatientId(List<SlotType1> slots) {
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

    private static SlotType1 getNamedSlotItem(List<SlotType1> slots, String name) {
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

    private static RegistryErrorList processErrorList(RegistryErrorList list) {
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

    private static String getErrorDescription(int rank) {
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

    private static int getErrorRanking(String severity) {
        int result;

        if (severity.isEmpty()) {
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
