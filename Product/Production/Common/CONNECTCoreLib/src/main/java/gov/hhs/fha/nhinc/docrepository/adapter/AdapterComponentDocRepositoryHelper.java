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
package gov.hhs.fha.nhinc.docrepository.adapter;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.StringUtil;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdapterComponentDocRepositoryHelper {

	private static final String VALUE_LIST_SEPERATOR = "~";
	private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocRepositoryHelper.class);

	RegistryError setRegistryError(String codeContext, String location, String errorCode,
    		String value){
    	RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
    	error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not " + codeContext);
    	error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> "
                + "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet" + location);
    	error.setErrorCode(errorCode);
    	error.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        error.setValue(value);

        LOG.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity()
                + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: "
                + error.getCodeContext());

        return error;
    }

	HashMap<String, DataHandler> getDocumentMap(
    		ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
    	// retrieve the documents (base64encoded representation)
        List<ProvideAndRegisterDocumentSetRequestType.Document> binaryDocs = body.getDocument();
        LOG.debug("There are " + binaryDocs.size() + " binary documents in this request.");

        // loop through binaryDocs list and put them into a hashmap for later use
        // when looping through the metadata - we need to associate the metadata
        // with the document (this is done by looking at the XDS Document id attribute).
        HashMap<String, DataHandler> docMap = new HashMap<>();

        for (ProvideAndRegisterDocumentSetRequestType.Document tempDoc : binaryDocs) {
            docMap.put(tempDoc.getId(), tempDoc.getValue());
        }

        return docMap;
    }

	long queryRepositoryByPatientId(String sPatId, String sDocId, String sClassCode, String sStatus,
            DocumentService docService) {
        long nhincDocRepositoryDocId = 0;

        // query for the doc unique id
        DocumentQueryParams params = new DocumentQueryParams();
        params.setPatientId(sPatId);
        List<String> lClassCodeList = new ArrayList<>();
        lClassCodeList.add(sClassCode);
        params.setClassCodes(lClassCodeList);
        List<String> lStatus = new ArrayList<>();
        lStatus.add(sStatus);
        params.setStatuses(lStatus);

        List<Document> documents = docService.documentQuery(params);
        if (NullChecker.isNotNullish(documents)) {
            LOG.debug("queryRepositoryByPatientId " + documents.size() + " documents for patient: " + sPatId);
            for (Document doc : documents) {
                LOG.debug("queryRepositoryByPatientId - sDocId: " + sDocId);
                if (sDocId.equals(doc.getDocumentUniqueId())) {
                    nhincDocRepositoryDocId = doc.getDocumentid();
                    break;
                }
            }
        }

        return nhincDocRepositoryDocId;
    }

	/**
     * This method extracts metadata from the XDS classification element given the slotname of the metadata item.
     *
     * @param classifications A list of classifications to search through.
     * @param classificationSchemeUUID The classification scheme idendifier to search for.
     * @param slotName The name of the metadata item within the classification element.
     * @param valueIndex In case there are multiple values for the metadata item, the option to choose a single value or
     *            all values (i.e. -1).
     * @return Returns the value of the metadata item found in the XDS classification element given the slotname.
     */
    String extractClassificationMetadata(
            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications,
            String classificationSchemeUUID, String slotName, int valueIndex) {
        String classificationValue = null;

        // loop through the classifications looking for the desired classification uuid
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications) {
            String classificationSchemeName = classification.getClassificationScheme();
            if (classificationSchemeUUID.equals(classificationSchemeName)) {
                classificationValue = extractMetadataFromSlots(classification.getSlot(), slotName, valueIndex);
                break;
            }
        }

        return classificationValue;
    }

    /**
     * This method extracts classification element metadata for non-slot values given the classification code uuid. The
     * following will be retrieved depending on the value of the classificationValueName: - the classification code
     * value (nodeRepresentation) - the representation of the code (nodeRepresentationName) - the id of the
     * extrinsicObject referenced by the given classification (classificationObject) - the id of the
     * classificationObject element in the request (id)
     *
     * @param classifications A list of classifications to search through.
     * @param classificationSchemeUUID The classification scheme idendifier to search for.
     * @param classificationValueName A string value indicating whether this method should return the classification
     *            code representation, the code itself, the id of the classification element, or the id of the
     *            extrinsicObject element that the classification refers to.
     * @return Returns the value of the metadata item found in the XDS classification element given the classification
     *         scheme and the name of the desired metadata element.
     */
    String extractClassificationMetadata(
            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications,
            String classificationSchemeUUID, String classificationValueName) {
        String classificationValue = null;

        LOG.debug("Looking for classificationScheme=" + classificationSchemeUUID);
        LOG.debug("Looking for classificationValueName=" + classificationValueName);
        // loop through the classifications looking for the desired classification uuid
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications) {
            String classificationSchemeName = classification.getClassificationScheme();
            LOG.debug("Found classificationScheme=" + classificationSchemeName);

            if (classificationSchemeUUID.equals(classificationSchemeName)) {
                if (classificationValueName.equals(DocRepoConstants.XDS_NAME)) {
                    classificationValue = classification.getName().getLocalizedString().get(0).getValue();
                } else if (classificationValueName.equals(DocRepoConstants.XDS_NODE_REPRESENTATION)) {
                    classificationValue = classification.getNodeRepresentation();
                } else if (classificationValueName.equals(DocRepoConstants.XDS_CLASSIFIED_OBJECT)) {
                    classificationValue = classification.getClassifiedObject();
                } else if (classificationValueName.equals(DocRepoConstants.XDS_CLASSIFICATION_ID)) {
                    classificationValue = classification.getClassifiedObject();
                }
                break; // found desired classification, have values, exit loop
            } // if (classificationSchemeUUID.equals(classificationSchemeName))
        }
        if (classificationValue != null && !classificationValue.isEmpty()) {
            classificationValue = StringUtil.extractStringFromTokens(classificationValue, "'()");
        }
        LOG.debug(classificationValueName + ": " + classificationValue);
        return classificationValue;
    }

    /**
     * Extracts the valueIndex value from an XDS request slot for a given metadata name.
     *
     * @param documentSlots A list of XDS metadata slots
     * @param slotName The name of the slot containing the desired metadata item
     * @param valueIndex For slot multivalued possibilities, the index value desired. If the value is < 0 then all
     *            values in the value list are returned in a '~' delimited list.
     * @return Returns the value of the first metadata value with the given metadata name. Null if not present.
     */
    String extractMetadataFromSlots(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots,
            String slotName, int valueIndex) {
        LOG.debug("extractMetadataFromSlots slotname: " + slotName + "; index: " + valueIndex);
        String slotValue = null;
        StringBuffer slotValues = null;
        boolean returnAllValues = false;
        if (valueIndex < 0) {
            returnAllValues = true;
            slotValues = new StringBuffer();
        }
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (slotName.equals(slot.getName())) {
                LOG.debug("Found " + slotName + ": " + slot.getValueList().getValue());
                if (returnAllValues) {
                    int listSize = slot.getValueList().getValue().size();
                    int counter = 0;
                    Iterator<String> iter = slot.getValueList().getValue().iterator();
                    while (iter.hasNext()) {
                        String value = iter.next();
                        slotValues.append(value);
                        counter++;
                        if (counter < listSize) {
                            slotValues.append(VALUE_LIST_SEPERATOR);
                        }
                    }

                } else {
                    if (slot.getValueList() != null && slot.getValueList().getValue() != null
                            && slot.getValueList().getValue().size() > 0) {
                        slotValue = slot.getValueList().getValue().get(valueIndex);
                    } else {
                        slotValue = "";
                    }
                }
                break; // found desired slot, have values, exit loop
            } // if (slotName.equals(slot.getName()))
        } // for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)
        if (returnAllValues) {
            slotValue = slotValues.toString();
        }

        LOG.debug(slotName + ": " + slotValue);
        return slotValue;
    }

    /**
     * Extracts the sourcePatientInfo pid value from an XDS request slot for the sourcePatientInfo element.
     *
     * @param documentSlots A list of XDS metadata slots
     * @param patientInfoName The name of the sourcePatientInfo pid containing the desired metadata item
     * @return Returns the value of the first metadata value with the given metadata name. Null if not present.
     */
    String extractPatientInfo(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots,
            String patientInfoName) {
        String slotValue = null;

        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (DocRepoConstants.XDS_SOURCE_PATIENT_INFO_SLOT.equals(slot.getName())) {
                Iterator<String> iter = slot.getValueList().getValue().iterator();
                while (iter.hasNext()) {
                    String nextSlotValue = iter.next();
                    if (nextSlotValue.startsWith(patientInfoName)) {
                        slotValue = nextSlotValue.substring(patientInfoName.length() + 1);
                        LOG.debug(patientInfoName + " extractionValue: " + slotValue);
                    }
                }
            } // if (XDS_SOURCE_PATIENT_INFO_SLOT.equals(slot.getName()))
        } // for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)

        return slotValue;
    }

}
