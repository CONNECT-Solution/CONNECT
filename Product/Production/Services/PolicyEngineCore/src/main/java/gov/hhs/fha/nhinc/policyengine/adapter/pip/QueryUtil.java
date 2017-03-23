/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility Methods for AdhocQuery requests and responses
 *
 * @author Neil Webb
 */
public class QueryUtil {

    private static final Logger LOG = LoggerFactory.getLogger(QueryUtil.class);

    /**
     * This method extracts the patient ID from the AdhocQueryResponse message and returns it.
     *
     * @param oResponse The AdhocQueryResponse message from the repository.
     * @return The patient ID in the response message.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This is thrown if any issues occur.
     */
    public String extractPatientId(AdhocQueryResponse oResponse) throws AdapterPIPException {
        String sPatientId = "";

        // Find the Patient ID in the response...
        // ---------------------------------------
        if ((oResponse != null) && (oResponse.getRegistryObjectList() != null)
                && (oResponse.getRegistryObjectList().getIdentifiable() != null)
                && (oResponse.getRegistryObjectList().getIdentifiable().size() > 0)) {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList()
                    .getIdentifiable();

            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs) {
                if ((oJAXBObj != null)
                        && (oJAXBObj.getDeclaredType() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName()
                                .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType"))
                        && (oJAXBObj.getValue() != null)) {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();

                    // Patient ID
                    // -----------
                    if ((oExtObj.getSlot() != null) && (oExtObj.getSlot().size() > 0)) {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot) {
                            if ((oSlot.getName() != null)
                                    && (oSlot.getName().equals(CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID))
                                    && (oSlot.getValueList() != null) && (oSlot.getValueList().getValue() != null)
                                    && (oSlot.getValueList().getValue().size() > 0)
                                    && (oSlot.getValueList().getValue().get(0).length() > 0)) {
                                sPatientId = oSlot.getValueList().getValue().get(0).trim();
                                break; // Get out of the loop - we found what we want.
                            }
                        } // for (SlotType1 oSlot : olSlot)
                    } // if ((oExtObj.getSlot() != null) && ...
                } // if ((oJAXBObj != null) &&
            } // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
        } // if ((oResponse != null) && ...

        return sPatientId;
    }

    /**
     * This method takes the parameters and creates an AdhocQueryRequest message that can be used to retrieve the meta
     * data about this document.
     *
     * @param sDocumentUniqueId The unique ID of the document.
     * @param sRepositoryId The repository ID where the document is stored.
     * @return The AdhocQueryRequest containing the search information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This is thrown if there is an error.
     */
    public AdhocQueryRequest createPatientIdQuery(String sDocumentUniqueId, String sRepositoryId)
            throws AdapterPIPException {
        AdhocQueryRequest oRequest = new AdhocQueryRequest();

        // ResponseOption
        // ----------------
        ResponseOptionType oResponseOption = new ResponseOptionType();
        oRequest.setResponseOption(oResponseOption);
        oResponseOption.setReturnComposedObjects(true);
        oResponseOption.setReturnType(CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS);

        AdhocQueryType oAQ = new AdhocQueryType();
        oRequest.setAdhocQuery(oAQ);
        oAQ.setId(CDAConstants.ADHOC_QUERY_REQUEST_BY_DOCUMENT_ID_UUID);

        List<SlotType1> olSlot = oAQ.getSlot();

        // Document ID
        // ------------
        if (sDocumentUniqueId.length() > 0) {
            SlotType1 oSlot = new SlotType1();
            olSlot.add(oSlot);
            oSlot.setName(CDAConstants.SLOT_NAME_DOC_RETRIEVE_DOCUMENT_ID);
            ValueListType oValueList = new ValueListType();
            oSlot.setValueList(oValueList);
            String sDocId = "('" + sDocumentUniqueId + "')"; // Formatted for NIST XDS.b
            oValueList.getValue().add(sDocId);
        }

        // Repository ID
        // --------------
        if (sRepositoryId.length() > 0) {
            SlotType1 oSlot = new SlotType1();
            olSlot.add(oSlot);
            oSlot.setName(CDAConstants.SLOT_NAME_DOC_RETRIEVE_REPOSITORY_UNIQUE_ID);
            ValueListType oValueList = new ValueListType();
            oSlot.setValueList(oValueList);
            oValueList.getValue().add(sRepositoryId);
        }

        // Status
        // -------
        SlotType1 oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.STATUS_APPROVED_QUERY_VALUE);

        return oRequest;
    }

    /**
     * This method will create the DocumentRequests (Document IDs) from the AdhocQueryResponse object.
     *
     * @param oResponse The AdhocQueryResponse that contains the document ids.
     * @return The list of DocumentRequests containing the document ids, note list may be empty if no DocumentRequests
     *         are built
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException Any error in the process of conversion.
     */
    public List<DocumentRequest> createDocumentRequest(AdhocQueryResponse oResponse) throws AdapterPIPException {
        List<DocumentRequest> olDocReq = new ArrayList<>();

        if ((oResponse != null) && (oResponse.getRegistryObjectList() != null)
                && (oResponse.getRegistryObjectList().getIdentifiable() != null)
                && (oResponse.getRegistryObjectList().getIdentifiable().size() > 0)) {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList()
                    .getIdentifiable();

            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs) {
                if ((oJAXBObj != null)
                        && (oJAXBObj.getDeclaredType() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                        && (oJAXBObj.getDeclaredType().getCanonicalName()
                                .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType"))
                        && (oJAXBObj.getValue() != null)) {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();

                    String sHomeCommunityId = "";
                    String sRepositoryId = "";
                    String sDocumentId = "";
                    String sHL7PatientId;

                    if (oExtObj != null) {
                        // Home Community ID
                        // -------------------
                        if (oExtObj.getHome() != null && oExtObj.getHome().length() > 0) {
                            sHomeCommunityId = oExtObj.getHome().trim();
                        } else if (oExtObj.getSlot() != null && oExtObj.getSlot().size() > 0) {
                            List<SlotType1> olSlot = oExtObj.getSlot();
                            for (SlotType1 oSlot : olSlot) {
                                if (oSlot.getName() != null
                                    && oSlot.getName().equals(CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID)
                                    && oSlot.getValueList() != null
                                    && oSlot.getValueList().getValue() != null
                                    && oSlot.getValueList().getValue().size() > 0
                                    && oSlot.getValueList().getValue().get(0).length() > 0) {

                                    sHL7PatientId = oSlot.getValueList().getValue().get(0).trim();
                                    sHomeCommunityId = PatientIdFormatUtil.parseCommunityId(sHL7PatientId);
                                }
                            } // for (SlotType1 oSlot : olSlot)
                        } // if ((oExtObj.getSlot() != null) && ...

                        // Repository ID
                        // ---------------
                        if (oExtObj.getSlot() != null && oExtObj.getSlot().size() > 0) {
                            List<SlotType1> olSlot = oExtObj.getSlot();
                            for (SlotType1 oSlot : olSlot) {
                                if (oSlot.getName() != null
                                    && oSlot.getName().equals(CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID)
                                    && oSlot.getValueList() != null
                                    && oSlot.getValueList().getValue() != null
                                    && oSlot.getValueList().getValue().size() > 0
                                    && oSlot.getValueList().getValue().get(0).length() > 0) {
                                    sRepositoryId = oSlot.getValueList().getValue().get(0).trim();
                                }
                            } // for (SlotType1 oSlot : olSlot)
                        } // if ((oExtObj.getSlot() != null) && ...

                        // Document Unique ID
                        // -------------------
                        if ((oExtObj.getExternalIdentifier() != null) && (oExtObj.getExternalIdentifier().size() > 0)) {
                            List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
                            for (ExternalIdentifierType oExtId : olExtId) {
                                if ((oExtId.getIdentificationScheme() != null)
                                    && (oExtId.getIdentificationScheme().equals(CDAConstants.DOCUMENT_ID_IDENT_SCHEME))
                                    && (oExtId.getValue() != null) && (oExtId.getValue().length() > 0)) {
                                    sDocumentId = oExtId.getValue().trim();
                                }
                            } // for (ExternalIdentifierType oExtid : olExtId)
                        } // if ((oExtObj.getExternalIdentifier() != null) &&
                    }

                    DocumentRequest oDocRequest = new DocumentRequest();
                    oDocRequest.setHomeCommunityId(sHomeCommunityId);
                    oDocRequest.setRepositoryUniqueId(sRepositoryId);
                    oDocRequest.setDocumentUniqueId(sDocumentId);
                    olDocReq.add(oDocRequest);

                } // if ((oJAXBObj != null) &&
            } // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
        }

        return olDocReq;
    }

    /**
     * This method creates the AdhocQueryRequest that is used to retreive the meta data about the CPP document in the
     * repository.
     *
     * @param sPatientId The patient ID.
     * @param sAssigningAuthority The assigning authority
     * @return The AdhocQueryRequest used to retrieve the document meta data.
     * @throws gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException This exception is thrown if there is an
       *             error.
     */
    public AdhocQueryRequest createAdhocQueryRequest(String sPatientId, String sAssigningAuthority)
            throws AdapterPIPException {
        LOG.debug("In createAdhocQueryRequest");

        AdhocQueryRequest oRequest = new AdhocQueryRequest();
        String sHL7PatId;
        // ResponseOption
        // ----------------
        ResponseOptionType oResponseOption = new ResponseOptionType();
        oRequest.setResponseOption(oResponseOption);
        oResponseOption.setReturnComposedObjects(true);
        oResponseOption.setReturnType(CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS);

        AdhocQueryType oAQ = new AdhocQueryType();
        oRequest.setAdhocQuery(oAQ);
        oAQ.setId(CDAConstants.ADHOC_QUERY_REQUEST_BY_PATIENT_ID_UUID);

        List<SlotType1> olSlot = oAQ.getSlot();

        // Patient ID
        // ------------
        SlotType1 oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_CPP_PATIENT_ID);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        if (sPatientId != null && !sPatientId.contains("&ISO")) {
            sHL7PatId = PatientIdFormatUtil.hl7EncodePatientId(sPatientId, sAssigningAuthority);
        } else {
            if (sPatientId != null && !sPatientId.isEmpty() && !sPatientId.startsWith("'")) {
                sPatientId = "'" + sPatientId + "'";
            }
            sHL7PatId = sPatientId;
        }
        oValueList.getValue().add(sHL7PatId);

        // Document Class Code
        // --------------------
        oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_DOCUMENT_CLASS_CODE);
        oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.ADHOC_QUERY_CLASS_CODE);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Set class code for query to: " + CDAConstants.ADHOC_QUERY_CLASS_CODE);
        }

        // Status
        // -------
        oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);
        oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.STATUS_APPROVED_QUERY_VALUE);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Set status for query to: " + CDAConstants.STATUS_APPROVED_QUERY_VALUE);
        }

        return oRequest;
    }
}
