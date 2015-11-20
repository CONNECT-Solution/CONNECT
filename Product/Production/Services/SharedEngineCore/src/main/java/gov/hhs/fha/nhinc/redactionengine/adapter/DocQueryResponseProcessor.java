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
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.CDAConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Webb
 */
public class DocQueryResponseProcessor {
    private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
    private static final Logger LOG = LoggerFactory.getLogger(DocQueryResponseProcessor.class);
    private String patientId;
    private String assigningAuthorityId;
    private String homeCommunityId;

    protected PatientConsentHelper getPatientConsentHelper() {
        return new PatientConsentHelper();
    }

    protected void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    protected String getPatientId() {
        return patientId;
    }

    protected void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    protected String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    protected void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    protected String getHomeCommunityId() {
        return homeCommunityId;
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest,
            AdhocQueryResponse adhocQueryResponse) {
        LOG.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        if (adhocQueryRequest == null) {
            LOG.warn("AdhocQueryRequest was null.");
        } else if (adhocQueryResponse == null) {
            LOG.warn("AdhocQueryResponse was null.");

        } else {
            extractIdentifiers(adhocQueryRequest);
            if ((patientId != null) && (!patientId.isEmpty())) {
                PatientConsentHelper patientConsentHelper = getPatientConsentHelper();
                if (patientConsentHelper == null) {
                    LOG.warn("PatientConsentHelper was null.");
                } else {
                    PatientPreferencesType patientPreferences = patientConsentHelper.retrievePatientConsentbyPatientId(
                            patientId, assigningAuthorityId);
                    if (patientPreferences == null) {
                        LOG.warn("PatientPreferences was null.");
                    } else {
                        response = filterResults(adhocQueryResponse, patientPreferences);
                    }
                }
            } else {
                LOG.info("Not a patient-centric query.");
                response = filterResultsNonPatientCentric(adhocQueryResponse);
            }
        }
        LOG.debug("End filterAdhocQueryResults");
        return response;
    }

    protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest) {
        LOG.debug("Begin extractIdentifiers");
        if (adhocQueryRequest == null) {
            LOG.warn("AdhocQueryRequest was null.");
        } else {
            AdhocQueryType adhocQuery = adhocQueryRequest.getAdhocQuery();
            if (adhocQuery != null) {
                homeCommunityId = HomeCommunityMap.getCommunityId(adhocQuery);

                List<SlotType1> slots = null;
                if (adhocQuery != null) {
                    slots = adhocQuery.getSlot();
                    List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
                    if ((slotValues != null) && (!slotValues.isEmpty())) {
                        String formattedPatientId = slotValues.get(0);
                        patientId = PatientIdFormatUtil.parsePatientId(formattedPatientId);
                        assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(formattedPatientId);
                    }
                }
            }
        }

        LOG.debug("End extractIdentifiers");
    }

    protected List<String> extractSlotValues(List<SlotType1> slots, String slotName) {
        LOG.debug("Begin extractSlotValues");
        List<String> returnValues = null;
        if (slots != null) {
            for (SlotType1 slot : slots) {
                if ((slot.getName() != null) && (slot.getName().length() > 0) && (slot.getValueList() != null)
                        && (slot.getValueList().getValue() != null) && (slot.getValueList().getValue().size() > 0)) {

                    if (slot.getName().equals(slotName)) {
                        ValueListType valueListType = slot.getValueList();
                        List<String> slotValues = valueListType.getValue();
                        returnValues = new ArrayList<String>();
                        for (String slotValue : slotValues) {
                            returnValues.add(slotValue);
                        }
                    }
                }

            }
        }
        LOG.debug("End extractSlotValues");
        return returnValues;
    }

    protected AdhocQueryResponse filterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse) {
        LOG.debug("In filterResultsNonPatientCentric");
        return filterResults(adhocQueryResponse, null);
    }

    protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse,
            PatientPreferencesType patientPreferences) {
        LOG.debug("Begin filterResults");
        AdhocQueryResponse response = null;
        if (adhocQueryResponse == null) {
            LOG.warn("AdhocQueryResponse was null.");
        } else {
            ObjectFactory rimObjectFactory = new ObjectFactory();
            response = new AdhocQueryResponse();
            response.setRegistryErrorList(adhocQueryResponse.getRegistryErrorList());
            response.setStatus(adhocQueryResponse.getStatus());
            response.setRegistryObjectList(new RegistryObjectListType());
            long docCount = 0;

            RegistryObjectListType sourceRegistryObjectList = adhocQueryResponse.getRegistryObjectList();
            if (sourceRegistryObjectList != null) {
                List<JAXBElement<? extends IdentifiableType>> olRegObjs = sourceRegistryObjectList.getIdentifiable();
                for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs) {
                    if ((oJAXBObj != null)
                            && (oJAXBObj.getDeclaredType() != null)
                            && (oJAXBObj.getDeclaredType().getCanonicalName() != null)
                            && (oJAXBObj.getDeclaredType().getCanonicalName()
                                    .equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType"))
                            && (oJAXBObj.getValue() != null)) {
                        ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                        PatientPreferencesType workingPatientPreferences = null;
                        if (patientPreferences == null) {
                            workingPatientPreferences = retrievePatientPreferencesForDocument(oExtObj);
                        } else {
                            workingPatientPreferences = patientPreferences;
                        }
                        if (documentAllowed(oExtObj, workingPatientPreferences)) {
                            LOG.debug("Adding document query response to the list.");
                            response.getRegistryObjectList().getIdentifiable()
                                    .add(rimObjectFactory.createExtrinsicObject(oExtObj));
                            docCount++;
                        } else {
                            LOG.debug("Skipping document");
                        }
                    }
                }
                response.setTotalResultCount(BigInteger.valueOf(docCount));
            } else {
                LOG.info("RegistryObjectList was null.");
            }

        }
        LOG.debug("End filterResults");
        return response;
    }

    protected PatientPreferencesType retrievePatientPreferencesForDocument(ExtrinsicObjectType oExtObj) {
        PatientPreferencesType patientPreferences = null;
        if (oExtObj == null) {
            LOG.error("Extrinsic Object was null.");
        } else {
            String documentId = extractDocumentId(oExtObj);
            String repositoryId = extractRepositoryId(oExtObj);
            patientPreferences = getPatientConsentHelper().retrievePatientConsentbyDocumentId(homeCommunityId,
                    repositoryId, documentId);
        }
        return patientPreferences;
    }

    protected String extractDocumentId(ExtrinsicObjectType oExtObj) {
        LOG.debug("Begin extractDocumentId");
        String documentId = null;
        if (!oExtObj.getExternalIdentifier().isEmpty()) {
            List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
            for (ExternalIdentifierType oExtId : olExtId) {
                if ((oExtId.getIdentificationScheme() != null)
                        && (oExtId.getIdentificationScheme().equals(CDAConstants.DOCUMENT_ID_IDENT_SCHEME))
                        && (oExtId.getValue() != null) && (oExtId.getValue().length() > 0)) {
                    documentId = oExtId.getValue().trim();
                }
            }
        }
        LOG.debug("End extractDocumentId - returning: " + documentId);
        return documentId;
    }

    protected String extractRepositoryId(ExtrinsicObjectType oExtObj) {
        LOG.debug("Begin extractRepositoryId");
        String repositoryId = null;
        if (!oExtObj.getSlot().isEmpty()) {
            List<SlotType1> slots = oExtObj.getSlot();
            for (SlotType1 slot : slots) {
                if ((slot != null) && (CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID.equals(slot.getName()))
                        && (slot.getValueList() != null) && (!slot.getValueList().getValue().isEmpty())) {
                    repositoryId = slot.getValueList().getValue().get(0);
                    if (repositoryId != null) {
                        repositoryId = repositoryId.trim();
                        break;
                    }
                }
            }
        }
        LOG.debug("End extractRepositoryId - returning: " + repositoryId);
        return repositoryId;
    }

    protected String extractDocumentType(ExtrinsicObjectType oExtObj) {
        LOG.debug("Begin extractDocumentType");
        String documentType = null;
        if (!oExtObj.getClassification().isEmpty()) {
            List<ClassificationType> classifications = oExtObj.getClassification();
            for (ClassificationType classification : classifications) {
                if ((classification != null)
                        && (EBXML_RESPONSE_TYPECODE_CLASS_SCHEME.equals(classification.getClassificationScheme()))) {
                    documentType = classification.getNodeRepresentation();
                    break;
                }
            }
        }
        LOG.debug("End extractDocumentType - returning: " + documentType);
        return documentType;
    }

    protected boolean documentAllowed(ExtrinsicObjectType extObject, PatientPreferencesType patientPreferences) {
        LOG.debug("Begin documentAllowed");
        boolean allowed = false;
        String documentTypeCode = extractDocumentType(extObject);
        allowed = getPatientConsentHelper().documentSharingAllowed(documentTypeCode, patientPreferences);
        LOG.debug("End documentAllowed - response: " + allowed);
        return allowed;
    }

}
