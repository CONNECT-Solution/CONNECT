/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocRepositoryOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterComponentDocRepositoryOrchImpl.class);
    private static final String REPOSITORY_UNIQUE_ID = "1";
    private static final String XDS_DOCUMENT_UNIQUE_ID_ERROR = "XDSDocumentUniqueIdError";
    private UTCDateUtil utcDateUtil = null;
    private AdapterComponentDocRepositoryHelper docRepoHelper = null;

    public AdapterComponentDocRepositoryOrchImpl() {
        docRepoHelper = getHelper();
    }

    protected AdapterComponentDocRepositoryHelper getHelper() {
        return docRepoHelper != null ? docRepoHelper : new AdapterComponentDocRepositoryHelper();
    }

    public DocumentService getDocumentService() {
        return new DocumentService();
    }

    public LargeFileUtils getLargeFileUtils() {
        return LargeFileUtils.getInstance();
    }

    public UTCDateUtil getDateUtil() {
        return utcDateUtil != null ? utcDateUtil : new UTCDateUtil();
    }

    /**
     * Perform a document retrieve on the document repository.
     *
     * @param body Message containing document retrieve parameters
     * @return Document retrieve response message.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        String docUniqueId;
        String reposUniqueId;
        RegistryResponseType regResponse = new RegistryResponseType();
        regResponse.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
        response.setRegistryResponse(regResponse);
        RegistryErrorList regerrList = new RegistryErrorList();

        if (body != null && body.getDocumentRequest() != null && !body.getDocumentRequest().isEmpty()) {
            String homeCommunityId = null;
            List<String> documentUniqueIds = new ArrayList<>();
            List<String> repositoryUniqueIds = new ArrayList<>();
            List<DocumentRequest> olDocRequest = body.getDocumentRequest();
            Iterator<DocumentRequest> iterDocRequest = olDocRequest.iterator();

            while (iterDocRequest.hasNext()) {
                DocumentRequest oDocRequest = iterDocRequest.next();
                // Home Community
                // ----------------
                if (homeCommunityId == null && oDocRequest.getHomeCommunityId() != null
                    && oDocRequest.getHomeCommunityId().length() > 0) {
                    homeCommunityId = oDocRequest.getHomeCommunityId();
                }

                // Document Uniqiue ID
                // --------------------
                if (oDocRequest.getDocumentUniqueId() != null && oDocRequest.getDocumentUniqueId().length() > 0
                    && !oDocRequest.getDocumentUniqueId().isEmpty()) {
                    docUniqueId = StringUtil.extractStringFromTokens(oDocRequest.getDocumentUniqueId(), "'()");
                    documentUniqueIds.add(docUniqueId);

                } else {
                    if (regResponse.getRegistryErrorList() == null) {
                        regResponse.setRegistryErrorList(regerrList);
                    }
                    RegistryError regErr = docRepoHelper.setRegistryError("find Document id.", "",
                        XDS_DOCUMENT_UNIQUE_ID_ERROR, XDS_DOCUMENT_UNIQUE_ID_ERROR + " Document Id is empty.");
                    regerrList.getRegistryError().add(regErr);
                    regResponse.setStatus(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
                }

                // Repository Unique ID
                // ----------------------
                if (oDocRequest.getRepositoryUniqueId() != null && oDocRequest.getRepositoryUniqueId().length() > 0
                    && !oDocRequest.getRepositoryUniqueId().isEmpty()) {
                    reposUniqueId = StringUtil.extractStringFromTokens(oDocRequest.getRepositoryUniqueId(), "'()");
                    repositoryUniqueIds.add(reposUniqueId);
                } else {
                    LOG.debug("RepositoryId not found");
                }

            }

            if (!documentUniqueIds.isEmpty() && !repositoryUniqueIds.isEmpty()) {
                boolean repositoryIdMatched = true;
                for (String repositoryUniqueId : repositoryUniqueIds) {
                    if (!REPOSITORY_UNIQUE_ID.equals(repositoryUniqueId)) {
                        repositoryIdMatched = false;
                        LOG.warn("Document repository message not processed due to repository "
                            + " unique id mismatch. Expected: " + REPOSITORY_UNIQUE_ID + ", found: "
                            + repositoryUniqueId);
                    }
                }
                retrieveDocuments(repositoryIdMatched, documentUniqueIds, response, homeCommunityId, regerrList);
            }
        }

        return response;
    }

    protected void retrieveDocuments(boolean repositoryIdMatched, List<String> documentUniqueIds,
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response, String homeCommunityId,
        RegistryErrorList regerrList) {
        if (repositoryIdMatched) {
            DocumentQueryParams params = new DocumentQueryParams();
            params.setDocumentUniqueId(documentUniqueIds);
            DocumentService service = getDocumentService();
            List<DocumentMetadata> docs = service.documentQuery(params);
            loadDocumentResponses(response, docs, homeCommunityId, documentUniqueIds, regerrList);
        }
    }

    protected void loadDocumentResponses(ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
        List<DocumentMetadata> docs, String homeCommunityId, List<String> documentUniqueId, RegistryErrorList regerrList) {
        if (response != null) {
            String responseStatus = DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
            List<DocumentResponse> olDocResponse = response.getDocumentResponse();

            if (docs != null && !docs.isEmpty()) {
                for (String documentId : documentUniqueId) {
                    boolean documentIdPresent = false;
                    for (DocumentMetadata doc : docs) {
                        if (doc.getDocumentUniqueId().equals(documentId)) {
                            documentIdPresent = true;
                        }
                    }
                    if (!documentIdPresent) {
                        if (response.getRegistryResponse().getRegistryErrorList() == null) {
                            response.getRegistryResponse().setRegistryErrorList(regerrList);
                        }
                        RegistryError regErr = docRepoHelper.setRegistryError("find Document id.", "",
                            XDS_DOCUMENT_UNIQUE_ID_ERROR, XDS_DOCUMENT_UNIQUE_ID_ERROR + " Document Id is empty.");
                        response.getRegistryResponse().getRegistryErrorList().getRegistryError().add(regErr);
                        response.getRegistryResponse().setStatus(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
                    }
                }

                for (DocumentMetadata doc : docs) {
                    DocumentResponse oDocResponse = new DocumentResponse();
                    boolean bHasData;

                    // Home Community Id
                    // -------------------
                    oDocResponse.setHomeCommunityId(homeCommunityId);

                    // Repository Unique Id
                    // ----------------------
                    oDocResponse.setRepositoryUniqueId(REPOSITORY_UNIQUE_ID);

                    // Document Unique ID
                    // --------------------
                    if (NullChecker.isNotNullish(doc.getDocumentUniqueId())) {
                        oDocResponse.setDocumentUniqueId(doc.getDocumentUniqueId());
                        LOG.debug("Document unique id found ");
                    }

                    // Mime Type
                    // ----------
                    if (NullChecker.isNotNullish(doc.getMimeType())) {
                        oDocResponse.setMimeType(doc.getMimeType());
                        LOG.debug("Mime type Identified ");
                    }

                    bHasData = setDocumentResponse(doc, oDocResponse);

                    // On-Demand document
                    if (doc.isOnDemand()) {
                        oDocResponse.setNewDocumentUniqueId(doc.getNewDocumentUniqueId());
                        oDocResponse.setNewRepositoryUniqueId(doc.getNewRepositoryUniqueId());
                    }

                    if (bHasData) {
                        olDocResponse.add(oDocResponse);
                    }
                }

            } else {
                response.getRegistryResponse().setStatus(responseStatus);
                RegistryErrorList regErrList = new RegistryErrorList();
                response.getRegistryResponse().setRegistryErrorList(regErrList);
                RegistryError regErr = docRepoHelper.setRegistryError("find Document id.", "",
                    XDS_DOCUMENT_UNIQUE_ID_ERROR, XDS_DOCUMENT_UNIQUE_ID_ERROR + " Document Id is empty.");
                regErrList.getRegistryError().add(regErr);
            }
            if (response.getRegistryResponse().getStatus().equals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE)
                && CollectionUtils.isNotEmpty(response.getDocumentResponse())) {
                response.getRegistryResponse().setStatus(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS);
            } else if (response.getRegistryResponse().getStatus()
                .equals(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS)
                && response.getRegistryResponse().getRegistryErrorList() != null
                && response.getRegistryResponse().getRegistryErrorList().getRegistryError() != null) {
                response.getRegistryResponse().setStatus(DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
            }
        } else {
            LOG.info("loadDocumentResponses - response object was null");
        }
    }

    protected boolean setDocumentResponse(DocumentMetadata doc, DocumentResponse oDocResponse) {
        boolean bHasData = false;
        if (doc.getRawData().length > 0) {
            try {
                String url = StringUtil.convertToStringUTF8(doc.getRawData());
                LOG.debug("Raw Data not null");
                URI uri = new URI(url);
                File sourceFile = new File(uri);
                try {
                    DataHandler dh = getLargeFileUtils().convertToDataHandler(sourceFile);
                    oDocResponse.setDocument(dh);
                    bHasData = true;
                } catch (IOException ex) {
                    LOG.error("Failed to read contents of the file {}: {}", sourceFile.getName(),
                        ex.getLocalizedMessage(), ex);
                    bHasData = false;
                }
            } catch (URISyntaxException | UnsupportedEncodingException ue) {
                LOG.trace("Exception reading file: {}", ue.getLocalizedMessage(), ue);
                DataHandler dh = getLargeFileUtils().convertToDataHandler(doc.getRawData());

                oDocResponse.setDocument(dh);
                bHasData = true;
            }
        }
        return bHasData;
    }

    /**
     *
     * This method extracts the metadata and binary document from the request and stores them in the NHINC document
     * repository.
     *
     * NOTE: This method is NOT compliant to the XDS specification.
     *
     * @param body The ProvideAndRequestDocumentSet request to parse and store metadata and documents.
     * @return Returns an XDS successful or failure response message.
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSet(
        ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        LOG.debug("Entering docRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet method.");

        RegistryResponseType registryResponse = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory()
            .createRegistryResponseType();
        RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList();

        // convert input XDS message to internal message
        if (body == null) {
            RegistryError error = docRepoHelper.setRegistryError("find a required element", "",
                DocRepoConstants.XDS_ERROR_CODE_MISSING_REQUEST_MESSAGE_DATA,
                DocRepoConstants.XDS_MISSING_REQUEST_MESSAGE_DATA
                + " ProvideAndRegisterDocumentSetRequestType element is null.");
            errorList.getRegistryError().add(error);

        } else {
            LOG.trace("ProvideAndRegisterDocumentSetRequestType element is not null.");

            HashMap<String, DataHandler> docMap = docRepoHelper.getDocumentMap(body);

            // retrieve the document metadata and store each doc in the request
            SubmitObjectsRequest submitObjectsRequest = body.getSubmitObjectsRequest();
            RegistryObjectListType regObjectList = submitObjectsRequest.getRegistryObjectList();
            List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList = regObjectList
                .getIdentifiable();
            LOG.debug("There is/are " + identifiableObjectList.size()
            + " identifiableObject(s) in this registryObjectsList.");

            boolean requestHasReplacementAssociation = checkForReplacementAssociation(identifiableObjectList,
                errorList);

            for (int i = 0; i < identifiableObjectList.size(); i++) {
                setDocument(identifiableObjectList, errorList, i, docMap, requestHasReplacementAssociation);
            }
        }

        // return the correct response based on the results of the query.
        String responseStatus;
        if (errorList.getRegistryError().isEmpty()) {
            responseStatus = DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS;
        } else {
            responseStatus = DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
            registryResponse.setRegistryErrorList(errorList);
        }

        registryResponse.setStatus(responseStatus);
        return registryResponse;
    }

    protected DocumentMetadata setDocument(
        List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
        RegistryErrorList errorList, int i, HashMap<String, DataHandler> docMap,
        boolean requestHasReplacementAssociation) {
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType extrinsicObject;

        logDeclaredType(identifiableObjectList, i);

        // the getValue method will return the non-JAXBElement<? extends...> object
        Object tempObj = getExtrinsicObjectValue(identifiableObjectList, i);
        if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType) {
            extrinsicObject = (ExtrinsicObjectType) tempObj;
            LOG.trace("extrinsicObject successfully populated");

            // get the externalIdentifiers so that we can get the docId and patientId
            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers = extrinsicObject
                .getExternalIdentifier();

            if (externalIdentifiers == null || externalIdentifiers.isEmpty()) {
                RegistryError error = docRepoHelper.setRegistryError("find a required element", "",
                    DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
                    DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA
                    + " extrinsicObject.getExternalIdentifier() element is null or empty.");
                errorList.getRegistryError().add(error);
                return null;
            }

            // prepare for the translation to the NHINC doc repository
            DocumentMetadata doc = new DocumentMetadata();

            // extract the docId
            String documentUniqueId = extractMetadataFromExternalIdentifiers(externalIdentifiers,
                DocRepoConstants.XDS_DOCUMENT_UNIQUE_ID);
            if (documentUniqueId != null) {
                LOG.debug("DocumentUniqueId for ExtrinsicObject " + i + ": " + documentUniqueId);
                doc.setDocumentUniqueId(documentUniqueId);
            } else {
                RegistryError error = docRepoHelper.setRegistryError("find a required element", " extractDocumentId",
                    DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
                    DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " DocumentUniqueId was missing.");
                errorList.getRegistryError().add(error);
            }

            // extract the patientId
            String patientId = extractMetadataFromExternalIdentifiers(externalIdentifiers,
                DocRepoConstants.XDS_PATIENT_ID);
            if (patientId != null) {
                // remove the assigning authority value
                LOG.debug("patientId for ExtrinsicObject " + i + ": " + patientId);
                String patientIdReformatted = PatientIdFormatUtil.stripQuotesFromPatientId(patientId);
                LOG.debug("Reformatted patientId for ExtrinsicObject " + i + ": " + patientIdReformatted);
                doc.setPatientId(patientIdReformatted);
            } else {
                RegistryError error = docRepoHelper.setRegistryError("find a required element", " extractPatientId",
                    DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
                    DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " PatientId was missing.");

                errorList.getRegistryError().add(error);
            }

            // extract the document title
            InternationalStringType docTitle = extrinsicObject.getName();
            if (docTitle != null) {
                String docTitleValue = docTitle.getLocalizedString().get(0).getValue();
                LOG.debug("DocumentTitle for ExtrinsicObject " + i + ": " + docTitleValue);
                doc.setDocumentTitle(docTitleValue);
            }

            // extract the document comments
            InternationalStringType docComments = extrinsicObject.getDescription();
            if (docComments != null) {
                String docCommentsValue = docComments.getLocalizedString().get(0).getValue();
                LOG.debug("DocumentComments for ExtrinsicObject " + i + ": " + docCommentsValue);
                doc.setComments(docCommentsValue);
            }

            // extract mimeType
            LOG.debug("Document mimeType for ExtrinsicObject " + i + ": " + extrinsicObject.getMimeType());
            doc.setMimeType(extrinsicObject.getMimeType());

            // there are many metadata items at the extrinsicObject/document
            // level that we need to translate to the NHINC format
            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots = extrinsicObject.getSlot();

            // extract intendedRecipient - format: organization|person
            String intendedRecipientValue = docRepoHelper.extractMetadataFromSlots(documentSlots,
                DocRepoConstants.XDS_INTENDED_RECIPIENT_SLOT, 0);
            if (intendedRecipientValue != null) {
                String intendedRecipientPerson;
                String intendedRecipientOrganization;
                if (intendedRecipientValue.indexOf("|") != -1) {
                    intendedRecipientOrganization = intendedRecipientValue.substring(0,
                        intendedRecipientValue.indexOf("|"));
                    intendedRecipientPerson = intendedRecipientValue.substring(intendedRecipientValue.indexOf("|") + 1,
                        intendedRecipientValue.length());
                } else {
                    intendedRecipientPerson = intendedRecipientValue;
                    intendedRecipientOrganization = intendedRecipientValue;
                }
                LOG.debug("Document intendedRecipientPerson for ExtrinsicObject " + i + ": " + intendedRecipientPerson);
                LOG.debug("Document intendedRecipientOrganization for ExtrinsicObject " + i + ": "
                    + intendedRecipientOrganization);
                doc.setIntendedRecipientPerson(intendedRecipientPerson);
                doc.setIntendedRecipientOrganization(intendedRecipientOrganization);
            }

            // extract languageCode
            doc.setLanguageCode(
                docRepoHelper.extractMetadataFromSlots(documentSlots, DocRepoConstants.XDS_LANGUAGE_CODE_SLOT, 0));
            LOG.debug("Document LanguageCode for ExtrinsicObject " + i + ": " + doc.getLanguageCode());

            // extract legalAuthenticator
            doc.setLegalAuthenticator(docRepoHelper.extractMetadataFromSlots(documentSlots,
                DocRepoConstants.XDS_LEGAL_AUTHENTICATOR_SLOT, 0));
            LOG.debug("Document LegalAuthenticator for ExtrinsicObject " + i + ": " + doc.getLegalAuthenticator());

            // extract Date fields
            String creationTime = docRepoHelper.extractMetadataFromSlots(documentSlots,
                DocRepoConstants.XDS_CREATION_TIME_SLOT, 0);
            LOG.debug("Document creationTime for ExtrinsicObject " + i + ": " + creationTime);
            // TODO add an error code for invalid date format
            doc.setCreationTime(getDateUtil().parseUTCDateOptionalTimeZone(creationTime));

            String startTime = docRepoHelper.extractMetadataFromSlots(documentSlots,
                DocRepoConstants.XDS_START_TIME_SLOT, 0);
            LOG.debug("Document startTime for ExtrinsicObject " + i + ": " + startTime);
            doc.setServiceStartTime(getDateUtil().parseUTCDateOptionalTimeZone(startTime));

            String stopTime = docRepoHelper.extractMetadataFromSlots(documentSlots, DocRepoConstants.XDS_STOP_TIME_SLOT,
                0);
            LOG.debug("Document stopTime for ExtrinsicObject " + i + ": " + stopTime);
            doc.setServiceStopTime(getDateUtil().parseUTCDateOptionalTimeZone(stopTime));

            // extract sourcePatientInfo metadata
            String sourcePatientId = docRepoHelper.extractMetadataFromSlots(documentSlots,
                DocRepoConstants.XDS_SOURCE_PATIENT_ID_SLOT, 0);
            LOG.debug("sourcePatientid: " + sourcePatientId);
            if (sourcePatientId != null) {
                // remove the assigning authority value
                String sourcePatientIdReformatted = PatientIdFormatUtil.stripQuotesFromPatientId(sourcePatientId);
                LOG.debug("Reformatted sourcePatientId for ExtrinsicObject " + i + ": " + sourcePatientIdReformatted);
                doc.setSourcePatientId(sourcePatientIdReformatted);
            }

            setDocumentPidObjects(doc, documentSlots);

            // extract classification metadata items
            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications = extrinsicObject
                .getClassification();

            setDocumentObjectsFromClassifications(doc, classifications);

            // extract eventCodes
            extractEventCodes(classifications, doc);

            // get the document byte array from the hashmap populated earlier
            byte[] rawData = new byte[0];
            try {
                DataHandler dh = docMap.get(extrinsicObject.getId());
                rawData = getLargeFileUtils().convertToBytes(dh);
                Document document = new Document(doc);
                document.setRawData(rawData);
            } catch (IOException ioe) {
                LOG.error("Failed to retrieve document from the message.  Will not be able to save to repository: {}",
                    ioe.getLocalizedMessage(), ioe);
            }

            String availabilityStatus = extrinsicObject.getStatus();
            LOG.debug("Availability status received in message: " + availabilityStatus);
            // Use default if no value was provided
            if (NullChecker.isNullish(availabilityStatus)) {
                availabilityStatus = DocRepoConstants.XDS_AVAILABLILTY_STATUS_APPROVED;
            }
            doc.setAvailablityStatus(availabilityStatus);

            // default value for new documents
            // TODO implement logic for the replacement of a document - it means
            // changing the status of the
            // referenced document in the submission set association element
            doc.setStatus(DocRepoConstants.XDS_STATUS);

            doc.setSize(rawData.length);

            // TODO concatenate the adapter server's uri to the document unique
            // id
            doc.setDocumentUri(documentUniqueId);

            saveDocument(doc, requestHasReplacementAssociation, documentUniqueId, errorList);

            return doc;
        }
        return null;
    }

    protected void saveDocument(DocumentMetadata doc, boolean requestHasReplacementAssociation, String documentUniqueId,
        RegistryErrorList errorList) {

        DocumentService docService = getDocumentService();

        if (requestHasReplacementAssociation) {
            // query for the documentId using the documentUniqueId
            long documentid = docRepoHelper.queryRepositoryByPatientId(doc.getPatientId(), doc.getDocumentUniqueId(),
                doc.getClassCode(), doc.getStatus(), docService);
            doc.setDocumentid(documentid);
        }

        docService.saveDocument(doc);
        LOG.debug("doc.documentId: " + doc.getDocumentid());
        // log.debug("document.isPersistent: " + doc.isPersistent()); //TODO need a better way to
        // determine if the doc was actually persisted.

        // determine if the save was successful - Hibernate will generate
        // a documentId for the record and populate this value in the
        // document object if the save was successful.
        if (doc.getDocumentid() == null || doc.getDocumentid() < 1) {
            RegistryError error = docRepoHelper.setRegistryError("store a document.", " storeDocument",
                DocRepoConstants.XDS_ERROR_CODE_REPOSITORY_ERROR,
                DocRepoConstants.XDS_REPOSITORY_ERROR + " DocumentUniqueId: " + documentUniqueId);
            errorList.getRegistryError().add(error);
        }
    }

    protected Object getExtrinsicObjectValue(
        List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
        int i) {
        return identifiableObjectList.get(i).getValue();
    }

    protected void logDeclaredType(
        List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
        int i) {
        LOG.debug("Item " + i + " identifiableObject is of DeclaredType: "
            + identifiableObjectList.get(i).getDeclaredType());
    }

    protected void setDocumentPidObjects(DocumentMetadata doc,
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots) {
        String pid3 = docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID3);
        doc.setPid3(pid3);

        String pid5 = docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID5);
        doc.setPid5(pid5);

        String pid7 = docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID7);
        doc.setPid7(pid7);

        String pid8 = docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID8);
        doc.setPid8(pid8);

        String pid11 = docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID11);
        doc.setPid11(pid11);

        LOG.debug("pid3: " + pid3 + ", pid5: " + pid5 + ", pid7: " + pid7 + ", pid8: " + pid8 + ", pid11: " + pid11
            + ".");
    }

    protected void setDocumentObjectsFromClassifications(DocumentMetadata doc,
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications) {
        // extract the document's author info
        String authorPerson = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_AUTHOR_CLASSIFICATION, DocRepoConstants.XDS_AUTHOR_PERSON_SLOT, -1);
        doc.setAuthorPerson(authorPerson);

        String authorInstitution = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_AUTHOR_CLASSIFICATION, DocRepoConstants.XDS_AUTHOR_INSTITUTION_SLOT, -1);
        doc.setAuthorInstitution(authorInstitution);

        String authorRole = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_AUTHOR_CLASSIFICATION, DocRepoConstants.XDS_AUTHOR_ROLE_SLOT, -1);
        doc.setAuthorRole(authorRole);

        String authorSpeciality = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_AUTHOR_CLASSIFICATION, DocRepoConstants.XDS_AUTHOR_SPECIALITY_SLOT, -1);
        doc.setAuthorSpecialty(authorSpeciality);

        // extract classCode
        String classCode = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CLASSCODE_CLASSIFICATION, DocRepoConstants.XDS_NODE_REPRESENTATION);
        doc.setClassCode(classCode);

        String classCodeScheme = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CLASSCODE_CLASSIFICATION, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0);
        doc.setClassCodeScheme(classCodeScheme);

        String classCodeDisplayName = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CLASSCODE_CLASSIFICATION, DocRepoConstants.XDS_NAME);
        doc.setClassCodeDisplayName(classCodeDisplayName);

        // extract confidentialityCode
        String confidentialityCode = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, DocRepoConstants.XDS_NODE_REPRESENTATION);
        doc.setConfidentialityCode(confidentialityCode);

        String confidentialityCodeScheme = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0);
        doc.setConfidentialityCodeScheme(confidentialityCodeScheme);

        String confidentialityCodeDisplayName = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, DocRepoConstants.XDS_NAME);
        doc.setConfidentialityCodeDisplayName(confidentialityCodeDisplayName);

        // extract formatCode
        String formatCode = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_FORMAT_CODE_CLASSIFICATION, DocRepoConstants.XDS_NODE_REPRESENTATION);
        doc.setFormatCode(formatCode);

        String formatCodeScheme = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_FORMAT_CODE_CLASSIFICATION, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0);
        doc.setFormatCodeScheme(formatCodeScheme);

        String formatCodeDisplayName = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_FORMAT_CODE_CLASSIFICATION, DocRepoConstants.XDS_NAME);
        doc.setFormatCodeDisplayName(formatCodeDisplayName);

        // extract healthcareFacilityTypeCode
        doc.setFacilityCode(docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION,
            DocRepoConstants.XDS_NODE_REPRESENTATION));
        doc.setFacilityCodeScheme(docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION,
            DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0));
        doc.setFacilityCodeDisplayName(docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION, DocRepoConstants.XDS_NAME));

        // extract practiceSettingCode
        String practiceSetting = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, DocRepoConstants.XDS_NODE_REPRESENTATION);
        doc.setPracticeSetting(practiceSetting);

        String practiceSettingScheme = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0);
        doc.setPracticeSettingScheme(practiceSettingScheme);

        String practiceSettingDisplayName = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, DocRepoConstants.XDS_NAME);
        doc.setPracticeSettingDisplayName(practiceSettingDisplayName);

        // extract typeCode
        String typeCode = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_TYPE_CODE_CLASSIFICATION, DocRepoConstants.XDS_NODE_REPRESENTATION);
        doc.setTypeCode(typeCode);

        String typeCodeScheme = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_TYPE_CODE_CLASSIFICATION, DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0);
        doc.setTypeCodeScheme(typeCodeScheme);

        String typeCodeDisplayName = docRepoHelper.extractClassificationMetadata(classifications,
            DocRepoConstants.XDS_TYPE_CODE_CLASSIFICATION, DocRepoConstants.XDS_NAME);
        doc.setTypeCodeDisplayName(typeCodeDisplayName);

        LOG.debug("authorPerson: " + authorPerson + "\n, authorInstitution: " + authorInstitution + "\n, authorRole: "
            + authorRole + "\n, authorSpeciality: " + authorSpeciality + "\n, classCode: " + classCode
            + "\n, classCodeScheme: " + classCodeScheme + "\n, classCodeDisplayName: " + classCodeDisplayName
            + "\n, confidentialityCode: " + confidentialityCode + "\n, confidentialityCodeDisplayName: "
            + confidentialityCodeDisplayName + "\n, confidentialityCodeScheme: " + confidentialityCodeScheme
            + "\n, formatCode: " + formatCode + "\n, typeCode: " + typeCode + "\n, formatCodeScheme: "
            + formatCodeScheme + "\n, formatCodeDisplayName: " + formatCodeDisplayName + "\n, practiceSetting: "
            + practiceSetting + "\n, practiceSettingScheme: " + practiceSettingScheme
            + "\n, practiceSettingDisplayName: " + practiceSettingDisplayName + "\n, typeCodeScheme: "
            + typeCodeScheme + "\n, typeCodeDisplayName: " + typeCodeDisplayName);
    }

    protected boolean checkForReplacementAssociation(
        List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
        RegistryErrorList errorList) {
        boolean replacementAssociationExists = false;

        for (int i = 0; i < identifiableObjectList.size(); i++) {

            // the getValue method will return the non-JAXBElement<? extends...> object
            Object tempObj = getIdentifiableObjectValue(identifiableObjectList, i);

            if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1) {
                // TODO logic for the replacement of a document - it means changing the status of the referenced
                // document in the submission set association element
                // WARNING: The following logic is NOT XDS compliant - we are assuming that the document in the request
                // is already persisted and that we are only updating
                // that document. If there is another document that actually replaces/deprecates the old, then this
                // logic will not work.
                AssociationType1 associationObj = (AssociationType1) tempObj;

                LOG.debug("associationType object present");

                if (NullChecker.isNullish(associationObj.getAssociationType())) {
                    RegistryError error = docRepoHelper.setRegistryError("", "",
                        DocRepoConstants.XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA,
                        DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " associationType element is null.");
                    errorList.getRegistryError().add(error);
                } else {
                    // check to see if the associationType is rplc
                    String associationType = associationObj.getAssociationType();
                    LOG.debug("Association element associationType = " + associationType);
                    if (DocRepoConstants.XDS_ASSOCIATION_TYPE_REPLACE.equalsIgnoreCase(associationType)) {
                        replacementAssociationExists = true;
                        break;
                    } else {
                        replacementAssociationExists = false;
                    }

                }
            }
        }

        LOG.debug("replacementAssociationExists = " + replacementAssociationExists);
        return replacementAssociationExists;
    }

    protected Object getIdentifiableObjectValue(
        List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
        int i) {
        LOG.debug("Item " + i + " identifiableObject is of DeclaredType: "
            + identifiableObjectList.get(i).getDeclaredType());
        return identifiableObjectList.get(i).getValue();
    }

    /**
     * This method extracts the value of a metadata item of a document from a list of XDS externalIdentifier objects
     * given the name of the metadata item.
     *
     * @param externalIdentifiers List of externalIdentifier objects which may contain the metadata item
     * @return Returns the string representation of the metadata item. Returns null if not present.
     */
    private String extractMetadataFromExternalIdentifiers(
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers,
        String metadataItemName) {
        String metadataItemValue = null;

        LOG.debug("extractMetadataFromExternalIdentifiers metadataItemName: " + metadataItemName);

        // loop through the externalIdentifiers looking for the for the desired name
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType externalIdentifier : externalIdentifiers) {
            String externalIdentifierName = externalIdentifier.getName().getLocalizedString().get(0).getValue();
            LOG.debug("externalIdentifierName: " + externalIdentifierName);
            if (metadataItemName.equalsIgnoreCase(externalIdentifierName)) {
                metadataItemValue = externalIdentifier.getValue();
                break;
            }
        }

        return metadataItemValue;
    }

    /**
     * This method extracts the list of event codes and prepares them for persistence into the doc NHINC repository
     *
     * @param classifications The list of metadata classification objects for the document
     * @param doc The NHINC document object to be persisted.
     */
    protected void extractEventCodes(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications,
        gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata doc) {
        LOG.trace("Begin extractEventCodes");
        HashSet<EventCode> eventCodes = new HashSet<>();
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications) {
            String classificationSchemeName = classification.getClassificationScheme();
            if (DocRepoConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION.equals(classificationSchemeName)) {
                LOG.debug(
                    "Found event code classification entry. Event code: " + classification.getNodeRepresentation());
                EventCode eventCode = new EventCode();
                eventCode.setDocument(doc);

                eventCode.setEventCode(classification.getNodeRepresentation());
                eventCode.setEventCodeScheme(docRepoHelper.extractMetadataFromSlots(classification.getSlot(),
                    DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0));
                eventCode.setEventCodeDisplayName(classification.getName().getLocalizedString().get(0).getValue());

                eventCodes.add(eventCode);
            }

        }

        doc.setEventCodes(eventCodes);
        LOG.trace("End extractEventCodes");
    }
}
