/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.QueryUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Webb
 */
public class DocRetrieveResponseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DocRetrieveResponseProcessor.class);
    private String documentId;
    private String homeCommunityId;
    private String repositoryId;
    private static final String EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String EBXML_RESPONSE_TYPECODE_CLASS_SCHEME = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";

    /**
     * This method filters the Document Retrieve results based on the Patient Preferences and creates a new Response to
     * return
     *
     * @param retrieveRequest
     * @param retrieveResponse
     * @return RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetReults(
        RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse) {
        LOG.debug("Begin filterRetrieveDocumentSetReults");
        RetrieveDocumentSetResponseType response = null;
        if (null != retrieveResponse && null != retrieveResponse.getDocumentResponse()
            && retrieveResponse.getDocumentResponse().size() > 0) {
            PatientPreferencesType ptPreferences;
            response = new RetrieveDocumentSetResponseType();
            response.setRegistryResponse(retrieveResponse.getRegistryResponse());
            for (DocumentResponse eachResponse : retrieveResponse.getDocumentResponse()) {
                LOG.debug("Processing a document response.");
                if (null != eachResponse) {
                    extractIdentifiers(eachResponse);
                    ptPreferences = getPatientConsentHelper().retrievePatientConsentbyDocumentId(homeCommunityId,
                        repositoryId, documentId);
                    if (allowDocumentSharing(eachResponse, ptPreferences)) {
                        LOG.debug("Document not filtered. Adding to response.");
                        response.getDocumentResponse().add(eachResponse);
                    }
                }
            }
        } else {
            LOG.debug("No document responses to filter.");
        }
        LOG.debug("End filterRetrieveDocumentSetReults");
        return response;
    }

    /**
     * The 3 main identifiers used for Document Retrieve are extracted here for each Document Response and used for
     * further filtering
     *
     * @param retrieveResponse
     */
    protected void extractIdentifiers(DocumentResponse retrieveResponse) {
        LOG.debug("Begin extractIdentifiers");
        if (null != retrieveResponse) {
            documentId = retrieveResponse.getDocumentUniqueId();
            homeCommunityId = retrieveResponse.getHomeCommunityId();
            repositoryId = retrieveResponse.getRepositoryUniqueId();
        } else {
            LOG.warn("Document Response is null");
        }
        LOG.debug("End extractIdentifiers - document id: " + documentId + ", home community id: " + homeCommunityId
            + ", repository id: " + repositoryId);
    }

    /**
     * This method filters the results for each Document Retrieve Response using Patient Preferences Document Type
     *
     * @param retrieveResponse
     * @param patientPreferences
     * @return boolean
     */
    protected boolean allowDocumentSharing(DocumentResponse retrieveResponse, PatientPreferencesType patientPreferences) {
        LOG.debug("Begin allowDocumentSharing");
        AdhocQueryResponse oResponse = null;
        String sDocTypeFromDocQueryResults = "";
        boolean allowSharing = false;
        if (null == retrieveResponse) {
            LOG.error("Unable to filter results retrieveResponse was null");
            return allowSharing;
        }
        if (null == patientPreferences) {
            LOG.error("Unable to filter results Patient Preferences was null");
            return allowSharing;
        }
        if (null != patientPreferences.getAssigningAuthority() && null != patientPreferences.getPatientId()) {

            try {
                // AdhocQueryRequest oRequest = createAdhocQueryRequest(patientPreferences.getPatientId(),
                // patientPreferences.getAssigningAuthority());
                AdhocQueryRequest oRequest = createAdhocQueryRequestByDocumentId(
                    retrieveResponse.getDocumentUniqueId(), retrieveResponse.getRepositoryUniqueId());
                oResponse = getAdhocQueryResponse(oRequest);
            } catch (Exception ex) {
                LOG.error("Error retrieving the document type for a document retrieve response: {}",
                    ex.getLocalizedMessage(), ex);
            }
            if (null != oResponse) {
                sDocTypeFromDocQueryResults = extractDocTypeFromDocQueryResults(oResponse, retrieveResponse);
                LOG.debug("Doc type retrieved from doc query: " + sDocTypeFromDocQueryResults);
            } else {
                LOG.debug("Adhoc query response for document metadata was null.");
            }
            if (null != sDocTypeFromDocQueryResults) {
                allowSharing = patientPrefAllowsSharing(sDocTypeFromDocQueryResults, patientPreferences);
            } else {
                LOG.debug("Document type from query for metadata response was null.");
            }
        } else {
            LOG.debug("Assigning authority or patient id was null.");
        }
        LOG.debug("End allowDocumentSharing - result: " + allowSharing);
        return allowSharing;
    }

    /**
     * This method returns true if the documentType from Document Retrieve results
     *
     * @param sDocTypeResult
     * @param patientPreferences
     * @return boolean
     */
    protected boolean patientPrefAllowsSharing(String sDocTypeResult, PatientPreferencesType patientPreferences) {
        return getPatientConsentHelper().documentSharingAllowed(sDocTypeResult, patientPreferences);
    }

    /**
     * This method returns AdhocQueryResponse for a AdhocQueryRequest
     *
     * @param oRequest
     * @return AdhocQueryResponse
     * @throws Exception
     */
    protected AdhocQueryResponse getAdhocQueryResponse(AdhocQueryRequest oRequest) throws Exception {
        AdapterComponentDocRegistryProxyObjectFactory factory = new AdapterComponentDocRegistryProxyObjectFactory();
        AdapterComponentDocRegistryProxy proxy = factory.getAdapterComponentDocRegistryProxy();

        return proxy.registryStoredQuery(oRequest, null);
    }

    /**
     * Adhoc Query Request is created based on the Patient Id and Assigning Authority
     *
     * @param sPatId
     * @param sAA
     * @return AdhocQueryRequest
     */
    protected AdhocQueryRequest createAdhocQueryRequest(String sPatId, String sAA) {
        LOG.debug("Begin createAdhocQueryRequest");
        AdhocQueryRequest request = null;
        try {
            if (null != sPatId && null != sAA && !sPatId.isEmpty() && !sAA.isEmpty()) {
                QueryUtil util = new QueryUtil();
                request = util.createAdhocQueryRequest(sPatId, sAA);
            }
        } catch (Exception e) {
            LOG.error("Error creating Adhoc Query Request: {}", e.getLocalizedMessage(), e);
        }
        LOG.debug("End createAdhocQueryRequest");
        return request;
    }

    /**
     * Adhoc Query Request is created based on the Patient Id and Assigning Authority
     *
     * @param documentId
     * @param repositoryId
     * @return AdhocQueryRequest
     */
    protected AdhocQueryRequest createAdhocQueryRequestByDocumentId(String documentId, String repositoryId) {
        LOG.debug("Begin createAdhocQueryRequestByDocumentId");
        AdhocQueryRequest request = null;
        try {
            if ((documentId != null) && (repositoryId != null)) {
                QueryUtil util = new QueryUtil();
                request = util.createPatientIdQuery(documentId, repositoryId);
            }
        } catch (Exception e) {
            LOG.error("Error creating query by document id message: {}", e.getLocalizedMessage(), e);
        }
        LOG.debug("End createAdhocQueryRequestByDocumentId");
        return request;
    }

    /**
     * This method is the Super method for all other methods to extract Doc Type from AdhocQuery Responses for each
     * matching DocRetrieve Response
     *
     * @param oResponse
     * @param docResponse
     * @return String
     */
    protected String extractDocTypeFromDocQueryResults(AdhocQueryResponse oResponse, DocumentResponse docResponse) {
        LOG.debug("Begin extractDocTypeFromDocQueryResults");
        String docType = null;
        ExtrinsicObjectType match = null;
        if (null != oResponse && null != docResponse && null != oResponse.getRegistryObjectList()) {
            List<JAXBElement<? extends IdentifiableType>> objectList = oResponse.getRegistryObjectList()
                .getIdentifiable();
            ExtrinsicObjectType docExtrinsic;
            LOG.debug("Identifiable list size: " + objectList.size());
            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();

                if (identifiableType instanceof ExtrinsicObjectType) {
                    LOG.debug("Identifiable item was ExtrinsicObjectType - processing");
                    docExtrinsic = (ExtrinsicObjectType) identifiableType;
                    List<ExternalIdentifierType> externalIdentifers = docExtrinsic.getExternalIdentifier();
                    if (null != externalIdentifers) {
                        String uniqueIdIdentifier = getUniqueIdIdentifier(externalIdentifers);
                        LOG.debug("Comapring identifier from document (" + uniqueIdIdentifier + ") to ("
                            + docResponse.getDocumentUniqueId() + ")");
                        if (null != uniqueIdIdentifier && uniqueIdIdentifier.equals(docResponse.getDocumentUniqueId())) {
                            match = docExtrinsic;
                            LOG.debug("Found match: " + match);
                            break;
                        }
                    }
                } else {
                    LOG.debug("Identifiable item was not ExtrinsicObjectType - was: "
                        + identifiableType.getClass().getName());
                }
            }

            if (null != match) {
                docType = extractDocTypeFromMetaData(match);
                LOG.debug("Doc type extracted from match: " + docType);
            } else {
                LOG.debug("Match was null");
            }
        } else {
            LOG.debug("Document response was null or empty");
        }
        LOG.debug("End extractDocTypeFromDocQueryResults - result: " + docType);
        return docType;
    }

    /**
     * This method extracts Document Type from AdhocQuery Metadata for identified Extrinsic Object for a particular
     * Document
     *
     * @param documentMetaData
     * @return String
     */
    protected String extractDocTypeFromMetaData(ExtrinsicObjectType documentMetaData) {
        LOG.debug("Begin extractDocTypeFromMetaData");
        String value = null;
        if (null != documentMetaData && null != documentMetaData.getClassification()
            && documentMetaData.getClassification().size() > 0) {
            LOG.debug("Classification size: " + documentMetaData.getClassification().size());
            List<ClassificationType> classificationList = documentMetaData.getClassification();
            for (ClassificationType classification : classificationList) {
                if (null != classification && null != classification.getClassificationScheme()
                    && classification.getClassificationScheme().contentEquals(EBXML_RESPONSE_TYPECODE_CLASS_SCHEME)) {
                    LOG.debug("Looking at classification scheme (" + classification.getClassificationScheme()
                        + ") compared to (" + EBXML_RESPONSE_TYPECODE_CLASS_SCHEME + ")");
                    value = classification.getNodeRepresentation();
                    // value = parseInternationalType(classification.getName());
                    LOG.debug("Value extracted from classification: " + value);
                }
            }
        }
        LOG.debug("End extractDocTypeFromMetaData - result: " + value);
        return value;
    }

    /**
     * This method returns the value of Classification Code Schema from International String Type
     *
     * @param str
     * @return String
     */
    protected String parseInternationalType(InternationalStringType str) {
        String value = null;
        if (null != str) {
            List<LocalizedStringType> localStr = str.getLocalizedString();
            if (null != localStr && localStr.size() > 0 && null != localStr.get(0)) {
                value = localStr.get(0).getValue();
            }
        }
        return value;
    }

    /**
     * This method returns Document Unique Id from ExternalIdentifier Object
     *
     * @param externalIdentifierList
     * @return String String
     */
    protected String getUniqueIdIdentifier(List<ExternalIdentifierType> externalIdentifierList) {
        LOG.debug("Begin getUniqueIdIdentifier");
        String aUniqueIdIdentifier = null;
        if (null != externalIdentifierList && externalIdentifierList.size() > 0) {
            for (ExternalIdentifierType externalIdentifier : externalIdentifierList) {
                if (null != externalIdentifier
                    && null != externalIdentifier.getIdentificationScheme()
                    && externalIdentifier.getIdentificationScheme().contentEquals(
                        EBXML_RESPONSE_DOCID_IDENTIFICATION_SCHEME)) {
                    aUniqueIdIdentifier = externalIdentifier.getValue();
                }
            }
        }
        LOG.debug("End getUniqueIdIdentifier - result: " + aUniqueIdIdentifier);
        return aUniqueIdIdentifier;
    }

    /**
     * This method returns a PatientConsentHelper Object
     *
     * @return PatientConsentHelper
     */
    protected PatientConsentHelper getPatientConsentHelper() {
        return new PatientConsentHelper();
    }

    /**
     * Returns Document Id of each Document Response
     *
     * @return String
     */
    protected String getDocumentId() {
        return documentId;
    }

    /**
     * Returns Home Community Id of each Document Response
     *
     * @return String
     */
    protected String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Returns Repository Id of each Document Response
     *
     * @return String
     */
    protected String getRepositoryId() {
        return repositoryId;
    }
}
