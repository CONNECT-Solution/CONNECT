/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.docmgr.repository.model.Document;
import gov.hhs.fha.nhinc.docmgr.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docmgr.repository.model.EventCode;
import gov.hhs.fha.nhinc.docmgr.repository.model.ExtraSlot;
import gov.hhs.fha.nhinc.docmgr.repository.service.DocumentService;
import gov.hhs.fha.nhinc.docmgr.repository.util.DocumentLoadUtil;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;

/**
 * Helper class for the document repository service.

 */
public class DocumentRepositoryHelper {
    // Properties file keys

    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String PROPERTY_FILE_NAME_REPOSITORY = "repository";
    private static final String PROPERTY_FILE_KEY_LOCAL_REPOSITORY_IDS = "localRepositoryIds";
    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DEFAULT_REPOSITORY_ID_PROP = "defaultRepositoryId";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_PARTIAL_SUCCESS = "urn:ihe:iti:2007:ResponseStatusType:PartialSuccess";
    public static final String XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_AVAILABLILTY_STATUS_APPROVED = "Active";
    public static final String XDS_STATUS = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";
    public static final String XDS_STATUS_ONLINE = "Online";
    public static final String XDS_STATUS_OFFLINE = "Offline";
    public static final String XDS_NAME = "Name";
    public static final String XDS_CLASSIFIED_OBJECT = "classifiedObject";      //this is the reference to the extrinsicObject/document element
    public static final String XDS_NODE_REPRESENTATION = "nodeRepresentation";  //this the actual code in a classification element
    public static final String XDS_CLASSIFICATION_ID = "id";                    //this is the id of the classification element
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
    public static final String XDS_REPOSITORY_ID_SLOT = "repositoryUniqueId";
    public static final String XDS_SOURCE_PATIENT_INFO_PID3 = "PID-3";
    public static final String XDS_SOURCE_PATIENT_INFO_PID5 = "PID-5";
    public static final String XDS_SOURCE_PATIENT_INFO_PID7 = "PID-7";
    public static final String XDS_SOURCE_PATIENT_INFO_PID8 = "PID-8";
    public static final String XDS_SOURCE_PATIENT_INFO_PID11 = "PID-11";
    public static final String XDS_EXTRA_METADATA_START = "urn:";
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
    public static final String XDS_ERROR_SEVERITY_SEVERE = "SEVERE";
    public static final String XDS_ERROR_SEVERITY_ERROR = "ERROR";
    public static final String XDS_ERROR_REGISTRY_SEVERITY_ERROR = "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error";
    public static final String XDS_DOCUMENT_ERROR_CODE_GENERAL = "XDSRepositoryError";
    public static final String XDS_DOCUMENT_ERROR_CODE_REPOSITORY_BUSY = "XDSRepositoryBusy";
    public static final String XDS_DOCUMENT_ERROR_CODE_REPOSITORY_OUT_OF_RESOURCES = "XDSRepositoryOutOfResources";
    public static final String XDS_DOCUMENT_ERROR_CODE_UNKNOWN_REPOSITORY_ID = "XDSUnknownRepositoryId";
    public static final String XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR = "XDSDocumentUniqueIdError";
    public static final String XDS_DOCUMENT_ERROR_CODE_UNKNOWN_COMMUNITY = "XDSUnknownCommunity";
    public static final String XDS_DOCUMENT_ERROR_CODE_MISSING_HOME_COMMUNITY_ID = "XDSMissingHomeCommunityId";
    public static final String XDS_ASSOCIATION_TYPE_REPLACE = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";
    private static final String DATE_FORMAT_STRING = "yyyyMMddhhmmss";
    private static final String VALUE_LIST_SEPERATOR = "~";
    private static Log log = LogFactory.getLog(DocumentRepositoryHelper.class);
    private static List<String> localRepositoryIds;

    static {
        List<String> repositoryIds = new ArrayList();
        String propertyRepositoryIds = null;
        String[] localIds = null;

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        try {
            propertyRepositoryIds = oProp.getProperty(PROPERTY_FILE_NAME_REPOSITORY, PROPERTY_FILE_KEY_LOCAL_REPOSITORY_IDS);


            // Process the string from the property file
            if (propertyRepositoryIds != null) {
                localIds = propertyRepositoryIds.split(";");
            }

            for (String repId : localIds) {
                repositoryIds.add(repId.trim());
            }
        } catch (Throwable t) {
            log.error("Error retrieving the home community id: " + t.getMessage(), t);
        }
        localRepositoryIds = repositoryIds;
    }

    /**
     * Perform a document retrieve on the document repository.
     *
     * @param body Message containing docurment retrieve parameters
     * @return Document retrieve response message.
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Entering docRepositoryHelper.documentRepositoryRetrieveDocumentSet method.");
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        RegistryResponseType registryResponse = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
        response.setRegistryResponse(registryResponse);
        RegistryErrorList errorList = new RegistryErrorList();
        registryResponse.setRegistryErrorList(errorList);
        response.getRegistryResponse().setStatus(XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);

        String docUniqueId = "";
        String reposUniqueId = "";
        if ((body != null) &&
            (body.getDocumentRequest() != null) &&
            (!body.getDocumentRequest().isEmpty())) {
            String homeCommunityId = null;
            List<String> documentUniqueIds = new ArrayList<String>();
            List<String> repositoryUniqueIds = new ArrayList<String>();
            List<DocumentRequest> olDocRequest = body.getDocumentRequest();
            Iterator<DocumentRequest> iterDocRequest = olDocRequest.iterator();

            // Collect the Components of the Request
            while (iterDocRequest.hasNext()) {
                DocumentRequest oDocRequest = iterDocRequest.next();

                // Home Community
                //----------------
                if ((homeCommunityId == null) &&
                    (oDocRequest.getHomeCommunityId() != null) &&
                    (oDocRequest.getHomeCommunityId().length() > 0)) {
                    homeCommunityId = oDocRequest.getHomeCommunityId();
                }

                // Document Uniqiue ID
                //--------------------
                if ((oDocRequest.getDocumentUniqueId() != null) &&
                    (oDocRequest.getDocumentUniqueId().length() > 0)) {
                    docUniqueId = StringUtil.extractStringFromTokens(oDocRequest.getDocumentUniqueId(), "'()");
                    documentUniqueIds.add(docUniqueId);
                }

                // Repository Unique ID
                //----------------------
                if ((oDocRequest.getRepositoryUniqueId() != null) &&
                    (oDocRequest.getRepositoryUniqueId().length() > 0)) {
                    reposUniqueId = StringUtil.extractStringFromTokens(oDocRequest.getRepositoryUniqueId(), "'()");
                    repositoryUniqueIds.add(reposUniqueId);
                }
            } // while (iterDocRequest.hasNext())

            // Evaluate the quality of the query parameters
            if (repositoryUniqueIds.isEmpty()) {
                log.info("Document Retrieve did not specify a repository id");
                InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNKNOWN_REPOSITORY_ID, "Repository Id is missing");
            }

            if (documentUniqueIds.isEmpty()) {
                log.error("Document unique id missing");
                InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR, "Document Unique Id is missing");
            }

            int numDocs = documentUniqueIds.size();
            if ((!documentUniqueIds.isEmpty()) && (!repositoryUniqueIds.isEmpty())) {
                boolean repositoryIdValid = true;

                for (String repositoryUniqueId : repositoryUniqueIds) {
                    // Repository Unique Id
                    //---------------------
                    if (repositoryUniqueId == null) {
                        log.info("Document Retrieve Request did not specify a repository id");
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNKNOWN_REPOSITORY_ID, "Repository Id is invalid");
                        repositoryIdValid = false;
                        break;
                    }
                    if (!localRepositoryIds.contains(repositoryUniqueId)) {
                        log.info("Document Retrieve specified an invalid reporitory id. Found " + repositoryUniqueId);
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNKNOWN_REPOSITORY_ID, "Repository Id is invalid");
                        repositoryIdValid = false;
                        log.warn("Document repository message not processed due to unknown repository " +
                            " unique id " + repositoryUniqueId);
                    }
                }

                if (repositoryIdValid) {
                    DocumentQueryParams params = new DocumentQueryParams();
                    params.setDocumentUniqueId(documentUniqueIds);
                    params.setRepositoryIds(repositoryUniqueIds.subList(0, 1));
                    DocumentService service = new DocumentService();
                    List<Document> docs = service.documentQuery(params);
                    List<String> foundDocumentIds = new ArrayList<String>();
                    for (Document doc : docs) {
                        foundDocumentIds.add(doc.getDocumentUniqueId());
                    }

                    for (String docId : documentUniqueIds) {
                        if (foundDocumentIds.contains(docId)) {
                            continue;
                        } else {
                            log.error("Document unique error. No such document in the repository");
                            InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR, "Document with specified Unique Id cannot be found");
                        }
                    }

                    loadDocumentResponses(response, docs, homeCommunityId);
                    String returnedStatus = response.getRegistryResponse().getStatus();
                    if (docs.size() != numDocs && returnedStatus.equalsIgnoreCase(XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS)) {
                        /* We had an error BEFORE we called loadDocumentResponse
                         * but there were no errors for other documents
                         * return partial success
                         */
                        response.getRegistryResponse().setStatus(XDS_RETRIEVE_RESPONSE_STATUS_PARTIAL_SUCCESS);
                    }
                    if (errorList.getRegistryError().isEmpty()) {
                        registryResponse.setRegistryErrorList(null);
                    }

                }
            }
        } else {
            InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR, "Document Unique Id is missing");
        }
        return response;
    }

    private void loadDocumentResponses(ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response, List<Document> docs, String homeCommunityId) {
        log.debug("Entering docRepositoryHelper.loadDocumentResponses method.");
        if (response != null) {
            String responseStatus = XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
            List<DocumentResponse> olDocResponse = response.getDocumentResponse();
            RegistryResponseType registryResponse = response.getRegistryResponse();
            RegistryErrorList errorList = response.getRegistryResponse().getRegistryErrorList();
            if ((docs != null) && (!docs.isEmpty())) {
                for (Document doc : docs) {
                    DocumentResponse oDocResponse = new DocumentResponse();
                    boolean bHasData = false;

                    // Home Community Id
                    //-------------------
                    //Test if this gateway supports the given HCID

                    if (homeCommunityId == null || homeCommunityId.length() == 0) {
                        log.error("No Home Community specified");
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_MISSING_HOME_COMMUNITY_ID, "Home Community Id is missing");
                        break;
                    }
                    String prefix = "urn:oid:";
                    String strippedHCID = null;
                    if (homeCommunityId.startsWith(prefix)) {
                        strippedHCID = homeCommunityId.substring(prefix.length());
                        if (strippedHCID == null || strippedHCID.length() == 0) {
                            log.error("No Home Community specified");
                            InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_MISSING_HOME_COMMUNITY_ID, "Home Community Id is missing");
                            break;
                        }
                    }

                    String myHomeCommunityId = retrieveHomeCommunityId();
                    if (!(myHomeCommunityId.equals(homeCommunityId))) {
                        log.error("Incorrect Home Community Id. Found: " + homeCommunityId + " expected " + myHomeCommunityId);
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNKNOWN_COMMUNITY, "Home Community Id cannot be serviced");
                        break;
                    }
                    oDocResponse.setHomeCommunityId(homeCommunityId);

                    // Repository Unique Id
                    //----------------------
                    String repositoryId = doc.getRepositoryId();

                    if (repositoryId == null) {
                        log.info("Document Retrieve specified a reporitory = " + repositoryId);
                    } else {
                        oDocResponse.setRepositoryUniqueId(doc.getRepositoryId());
                    }

                    // Document Unique ID
                    //--------------------
                    if (NullChecker.isNotNullish(doc.getDocumentUniqueId())) {
                        oDocResponse.setDocumentUniqueId(doc.getDocumentUniqueId());
                        log.info("Document unique id: " + doc.getDocumentUniqueId());
                        bHasData = true;
                    } else {
                        log.error("Document unique id missing");
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR, "Document Unique Id is missing");
                        break;
                    }

                    // Mime Type
                    //----------
                    if (NullChecker.isNotNullish(doc.getMimeType())) {
                        oDocResponse.setMimeType(doc.getMimeType());
                        log.info("Mime type: " + doc.getMimeType());
                        bHasData = true;
                    }

                    // Document
                    //---------
                    if ((doc.getRawData() != null) && (doc.getRawData().length > 0)) {
                        oDocResponse.setDocument(doc.getRawData());
                        log.info("Document data: " + doc.getRawData());
                        bHasData = true;
                    } else {
                        log.error("Document unique error. No such document in the repository");
                        InsertResponseError(errorList, XDS_DOCUMENT_ERROR_CODE_UNIQUE_ID_ERROR, "Document with specified Unique Id cannot be found");
                        break;
                    }

                    if (bHasData) {
                        olDocResponse.add(oDocResponse);
                        responseStatus = XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS;
                    }
                }
            } else {
                log.info("loadDocumentResponses - no response messages returned.");
            }

            registryResponse.setStatus(responseStatus);
        } else {
            log.info("loadDocumentResponses - response object was null");
        }
    }

    /**
     *
     * This method extracts the metadata and binary document from the request and
     * stores them in the NHINC document repository.
     *
     * NOTE: This method is NOT compliant to the XDS specification.
     *
     * @param body The ProvideAndRequestDocumentSet request to parse and store metadata and documents.
     * @return Returns an XDS successful or failure response message.
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentRepositoryProvideAndRegisterDocumentSet(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Entering docRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet method.");

        RegistryResponseType registryResponse =
            new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
        RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList();

        //convert input XDS message to internal message
        if (body == null) {
            RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
            error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not find a required element");
            error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet; "); //kludgy?
            error.setErrorCode(XDS_ERROR_CODE_MISSING_REQUEST_MESSAGE_DATA);
            error.setSeverity(XDS_ERROR_SEVERITY_SEVERE);
            error.setValue(XDS_MISSING_REQUEST_MESSAGE_DATA + "ProvideAndRegisterDocumentSetRequestType element is null.");


            errorList.getRegistryError().add(error);

            log.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity() + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: " + error.getCodeContext());

        } else {
            log.debug("ProvideAndRegisterDocumentSetRequestType element is not null.");

            //retrieve the documents (base64encoded representation)
            List<ProvideAndRegisterDocumentSetRequestType.Document> binaryDocs = body.getDocument();
            int numDocsInRequest = binaryDocs.size();
            log.debug("There are " + numDocsInRequest + " binary documents in this request.");

            //loop through binaryDocs list and put them into a hashmap for later use
            //when looping through the metadata - we need to associate the metadata
            //with the document (this is done by looking at the XDS Document id attribute).
            HashMap docMap = new HashMap();
            for (int docNum = 0; docNum < numDocsInRequest; docNum++) {
                String storeRequestId = binaryDocs.get(docNum).getId();
                byte[] storeRequestValue = binaryDocs.get(docNum).getValue();
                docMap.put(storeRequestId, storeRequestValue);
                log.debug("Found Document with id = " + storeRequestId + " with size = " + storeRequestValue.length);

                //retrieve the document metadata and store each doc in the request
                SubmitObjectsRequest submitObjectsRequest = body.getSubmitObjectsRequest();
                RegistryObjectListType regObjectList = submitObjectsRequest.getRegistryObjectList();
                List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList = regObjectList.getIdentifiable();
                log.debug("There is/are " + identifiableObjectList.size() + " identifiableObject(s) in this registryObjectsList.");

                boolean requestHasReplacementAssociation = checkForReplacementAssociation(identifiableObjectList, errorList);

                for (int i = 0; i < identifiableObjectList.size(); i++) {
                    oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType extrinsicObject = null;
                    log.debug("Item " + i + " identifiableObject is of DeclaredType: " + identifiableObjectList.get(i).getDeclaredType());

                    //the getValue method will return the non-JAXBElement<? extends...> object
                    Object tempObj = identifiableObjectList.get(i).getValue();
                    if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType) {
                        extrinsicObject = (ExtrinsicObjectType) tempObj;
                        log.debug("extrinsicObject successfully populated");

                        if (extrinsicObject == null) {
                            RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
                            error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not find a required element");
                            error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                                "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet"); //kludgy?
                            error.setErrorCode(XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA);
                            error.setSeverity(XDS_ERROR_SEVERITY_SEVERE);
                            error.setValue(XDS_MISSING_DOCUMENT_METADATA + "extrinsicObject element is null.");

                            errorList.getRegistryError().add(error);

                            log.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity() + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: " + error.getCodeContext());

                        } else {
                            //prepare for the translation to the NHINC doc repository
                            gov.hhs.fha.nhinc.docmgr.repository.model.Document doc =
                                new gov.hhs.fha.nhinc.docmgr.repository.model.Document();

                            //get the externalIdentifiers so that we can get the docId and patientId
                            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers = extrinsicObject.getExternalIdentifier();

                            //extract the docId
                            String documentUniqueId = extractMetadataFromExternalIdentifiers(externalIdentifiers, XDS_DOCUMENT_UNIQUE_ID);
                            if (documentUniqueId != null) {
                                log.debug("DocumentUniqueId for ExtrinsicObject " + i + ": " + documentUniqueId);
                                doc.setDocumentUniqueId(documentUniqueId);
                            } else {
                                RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
                                error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not find a required element");
                                error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                                    "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet extractDocumentId"); //kludgy?
                                error.setErrorCode(XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA);
                                error.setSeverity(XDS_ERROR_SEVERITY_SEVERE);
                                error.setValue(XDS_MISSING_DOCUMENT_METADATA + "DocumentUniqueId was missing.");

                                errorList.getRegistryError().add(error);

                                log.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity() + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: " + error.getCodeContext());
                            }

                            //extract the patientId
                            String patientId = extractMetadataFromExternalIdentifiers(externalIdentifiers, XDS_PATIENT_ID);
                            if (patientId != null) {
                                //remove the assigning authority value
                                log.debug("patientId for ExtrinsicObject " + i + ": " + patientId);
                                String patientIdReformatted = PatientIdFormatUtil.parsePatientId(patientId);
                                log.debug("Reformatted patientId for ExtrinsicObject " + i + ": " + patientIdReformatted);
                                doc.setPatientId(patientIdReformatted);
                            } else {
                                RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
                                error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not find a required element");
                                error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                                    "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet extractPatientId"); //kludgy?
                                error.setErrorCode(XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA);
                                error.setSeverity(XDS_ERROR_SEVERITY_SEVERE);
                                error.setValue(XDS_MISSING_DOCUMENT_METADATA + "PatientId was missing.");

                                errorList.getRegistryError().add(error);

                                log.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity() + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: " + error.getCodeContext());
                            }

                            //extract the document title
                            InternationalStringType docTitle = extrinsicObject.getName();
                            if (docTitle != null) {
                                log.debug("DocumentTitle for ExtrinsicObject " + i + ": " + docTitle.getLocalizedString().get(0).getValue());
                                doc.setDocumentTitle(docTitle.getLocalizedString().get(0).getValue());
                            }

                            //extract the document comments
                            InternationalStringType docComments = extrinsicObject.getDescription();
                            if (docComments != null) {
                                log.debug("DocumentComments for ExtrinsicObject " + i + ": " + docComments.getLocalizedString().get(0).getValue());
                                doc.setComments(docComments.getLocalizedString().get(0).getValue());
                            }

                            //extract mimeType
                            log.debug("Document mimeType for ExtrinsicObject " + i + ": " + extrinsicObject.getMimeType());
                            doc.setMimeType(extrinsicObject.getMimeType());


                            //there are many metadata items at the extrinsicObject/document
                            //level that we need to translate to the NHINC format
                            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots = extrinsicObject.getSlot();

                            //extract intendedRecipient - format: organization|person
                            String intendedRecipientValue = extractMetadataFromSlots(documentSlots, XDS_INTENDED_RECIPIENT_SLOT, 0);
                            if (intendedRecipientValue != null) {
                                String intendedRecipientPerson = "";
                                String intendedRecipientOrganization = "";
                                int separatorIdx = intendedRecipientValue.indexOf("|");
                                if (separatorIdx != -1) {
                                    intendedRecipientPerson = intendedRecipientValue.substring(separatorIdx + 1);
                                    intendedRecipientOrganization = intendedRecipientValue.substring(0, separatorIdx);
                                } else {
                                    intendedRecipientOrganization = intendedRecipientValue;
                                }

                                log.debug("Document intendedRecipientPerson for ExtrinsicObject " + i + ": " + intendedRecipientPerson);
                                log.debug("Document intendedRecipientOrganization for ExtrinsicObject " + i + ": " + intendedRecipientOrganization);
                                doc.setIntendedRecipientPerson(intendedRecipientPerson);
                                doc.setIntendedRecipientOrganization(intendedRecipientOrganization);
                            }

                            //extract languageCode
                            doc.setLanguageCode(extractMetadataFromSlots(documentSlots, XDS_LANGUAGE_CODE_SLOT, 0));
                            log.debug("Document LanguageCode for ExtrinsicObject " + i + ": " + doc.getLanguageCode());

                            //extract legalAuthenticator
                            doc.setLegalAuthenticator(extractMetadataFromSlots(documentSlots, XDS_LEGAL_AUTHENTICATOR_SLOT, 0));
                            log.debug("Document LegalAuthenticator for ExtrinsicObject " + i + ": " + doc.getLegalAuthenticator());

                            //extract repositoryId
                            doc.setRepositoryId(extractMetadataFromSlots(documentSlots, XDS_REPOSITORY_ID_SLOT, 0));
                            log.debug("Document repositoryId for ExtrinsicObject " + i + ": " + doc.getRepositoryId());
                            if (doc.getRepositoryId() == null) {
                                try {
                                    PropertyAccessor oProp = PropertyAccessor.getInstance();
                                    doc.setRepositoryId(oProp.getProperty(REPOSITORY_PROPERTY_FILE, DEFAULT_REPOSITORY_ID_PROP));
                                } catch (PropertyAccessException e) {
                                    log.error("Could not find property:" + DEFAULT_REPOSITORY_ID_PROP + " in file:" + REPOSITORY_PROPERTY_FILE, e);
                                }
                                log.debug("Document repositoryId for ExtrinsicObject set to default: " + doc.getRepositoryId());
                            }

                            //extract Date fields
                            String creationTime = extractMetadataFromSlots(documentSlots, XDS_CREATION_TIME_SLOT, 0);

                            log.debug("Creation Time from Request = " + creationTime);
                            log.debug("Rolling back creation time 24 hours to prevent stuck documents...");

                            /*  This was put in to relieve the issue of stuck
                             * documents.  Now it is being taken out due to issues
                             * where the creation date in the database (not in the
                             * document) and in the GUI keeps getting rolled back
                             * 24 hours.
                            try
                            {
                            //manipulate date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                            java.util.Date dt = sdf.parse(creationTime);

                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            // Substract 1 days from the calendar
                            cal.setTime(dt);
                            cal.add(java.util.Calendar.DATE, -1);
                            log.debug("1 days ago: " + cal.getTime());

                            creationTime = sdf.format(cal.getTime());
                            }
                            catch(Exception e)
                            {
                            log.error("Could not manipulate date: " + e);
                            }*/

                            log.debug("Document creationTime for ExtrinsicObject " + i + ": " + creationTime);
                            //TODO add an error code for invalid date format
                            doc.setCreationTime(parseDate(creationTime, DATE_FORMAT_STRING));

                            String startTime = extractMetadataFromSlots(documentSlots, XDS_START_TIME_SLOT, 0);
                            log.debug("Document startTime for ExtrinsicObject " + i + ": " + startTime);
                            doc.setServiceStartTime(parseDate(startTime, DATE_FORMAT_STRING));

                            String stopTime = extractMetadataFromSlots(documentSlots, XDS_STOP_TIME_SLOT, 0);
                            log.debug("Document stopTime for ExtrinsicObject " + i + ": " + stopTime);
                            doc.setServiceStopTime(parseDate(stopTime, DATE_FORMAT_STRING));

                            //extract sourcePatientInfo metadata
                            String sourcePatientId = extractMetadataFromSlots(documentSlots, XDS_SOURCE_PATIENT_ID_SLOT, 0);
                            log.debug("sourcePatientid: " + sourcePatientId);
                            if (sourcePatientId != null) {
                                //remove the assigning authority value
                                String sourcePatientIdReformatted = PatientIdFormatUtil.parsePatientId(sourcePatientId);
                                log.debug("Reformatted sourcePatientId for ExtrinsicObject " + i + ": " + sourcePatientIdReformatted);
                                doc.setSourcePatientId(sourcePatientIdReformatted);
                            }

                            String pid3 = extractPatientInfo(documentSlots, XDS_SOURCE_PATIENT_INFO_PID3);
                            log.debug("pid3: " + pid3);
                            doc.setPid3(pid3);

                            String pid5 = extractPatientInfo(documentSlots, XDS_SOURCE_PATIENT_INFO_PID5);
                            log.debug("pid5: " + pid5);
                            doc.setPid5(pid5);

                            String pid7 = extractPatientInfo(documentSlots, XDS_SOURCE_PATIENT_INFO_PID7);
                            log.debug("pid7: " + pid7);
                            doc.setPid7(pid7);

                            String pid8 = extractPatientInfo(documentSlots, XDS_SOURCE_PATIENT_INFO_PID8);
                            log.debug("pid8: " + pid8);
                            doc.setPid8(pid8);

                            String pid11 = extractPatientInfo(documentSlots, XDS_SOURCE_PATIENT_INFO_PID11);
                            log.debug("pid11: " + pid11);
                            doc.setPid11(pid11);

                            //extract extra metadata
                            extractExtraMetadataFromSlots(documentSlots, doc);

                            //extract classification metadata items
                            List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications = extrinsicObject.getClassification();

                            //extract the document's author info
                            String authorPerson = extractClassificationMetadata(classifications, XDS_AUTHOR_CLASSIFICATION, XDS_AUTHOR_PERSON_SLOT, -1);
                            log.debug("authorPerson: " + authorPerson);
                            doc.setAuthorPerson(authorPerson);

                            String authorInstitution = extractClassificationMetadata(classifications, XDS_AUTHOR_CLASSIFICATION, XDS_AUTHOR_INSTITUTION_SLOT, -1);
                            log.debug("authorInstitution: " + authorInstitution);
                            doc.setAuthorInstitution(authorInstitution);

                            String authorRole = extractClassificationMetadata(classifications, XDS_AUTHOR_CLASSIFICATION, XDS_AUTHOR_ROLE_SLOT, -1);
                            log.debug("authorRole: " + authorRole);
                            doc.setAuthorRole(authorRole);

                            String authorSpeciality = extractClassificationMetadata(classifications, XDS_AUTHOR_CLASSIFICATION, XDS_AUTHOR_SPECIALITY_SLOT, -1);
                            log.debug("authorSpeciality: " + authorSpeciality);
                            doc.setAuthorSpecialty(authorSpeciality);

                            //extract classCode
                            String classCode = extractClassificationMetadata(classifications, XDS_CLASSCODE_CLASSIFICATION, XDS_NODE_REPRESENTATION);
                            log.debug("classCode: " + classCode);
                            doc.setClassCode(classCode);

                            String classCodeScheme = extractClassificationMetadata(classifications, XDS_CLASSCODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0);
                            log.debug("classCodeScheme: " + classCodeScheme);
                            doc.setClassCodeScheme(classCodeScheme);

                            String classCodeDisplayName = extractClassificationMetadata(classifications, XDS_CLASSCODE_CLASSIFICATION, XDS_NAME);
                            log.debug("classCodeDisplayName: " + classCodeDisplayName);
                            doc.setClassCodeDisplayName(classCodeDisplayName);

                            //extract confidentialityCode
                            String confidentialityCode = extractClassificationMetadata(classifications, XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, XDS_NODE_REPRESENTATION);
                            log.debug("confidentialityCode: " + confidentialityCode);
                            doc.setConfidentialityCode(confidentialityCode);

                            String confidentialityCodeScheme = extractClassificationMetadata(classifications, XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0);
                            log.debug("confidentialityCodeScheme: " + confidentialityCodeScheme);
                            doc.setConfidentialityCodeScheme(confidentialityCodeScheme);

                            String confidentialityCodeDisplayName = extractClassificationMetadata(classifications, XDS_CONFIDENTIALITY_CODE_CLASSIFICATION, XDS_NAME);
                            log.debug("confidentialityCodeDisplayName: " + confidentialityCodeDisplayName);
                            doc.setConfidentialityCodeDisplayName(confidentialityCodeDisplayName);

                            //extract formatCode
                            String formatCode = extractClassificationMetadata(classifications, XDS_FORMAT_CODE_CLASSIFICATION, XDS_NODE_REPRESENTATION);
                            log.debug("formatCode: " + formatCode);
                            doc.setFormatCode(formatCode);

                            String formatCodeScheme = extractClassificationMetadata(classifications, XDS_FORMAT_CODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0);
                            log.debug("formatCodeScheme: " + formatCodeScheme);
                            doc.setFormatCodeScheme(formatCodeScheme);

                            String formatCodeDisplayName = extractClassificationMetadata(classifications, XDS_FORMAT_CODE_CLASSIFICATION, XDS_NAME);
                            log.debug("formatCodeDisplayName: " + formatCodeDisplayName);
                            doc.setFormatCodeDisplayName(formatCodeDisplayName);

                            //extract healthcareFacilityTypeCode
                            doc.setFacilityCode(extractClassificationMetadata(classifications, XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION, XDS_NODE_REPRESENTATION));
                            doc.setFacilityCodeScheme(extractClassificationMetadata(classifications, XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0));
                            doc.setFacilityCodeDisplayName(extractClassificationMetadata(classifications, XDS_HEALTHCARE_FACILITY_TYPE_CODE_CLASSIFICATION, XDS_NAME));

                            //extract practiceSettingCode
                            String practiceSetting = extractClassificationMetadata(classifications, XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, XDS_NODE_REPRESENTATION);
                            log.debug("practiceSetting: " + practiceSetting);
                            doc.setPracticeSetting(practiceSetting);

                            String practiceSettingScheme = extractClassificationMetadata(classifications, XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0);
                            log.debug("practiceSettingScheme: " + practiceSettingScheme);
                            doc.setPracticeSettingScheme(practiceSettingScheme);

                            String practiceSettingDisplayName = extractClassificationMetadata(classifications, XDS_PRACTICE_SETTING_CODE_CLASSIFICATION, XDS_NAME);
                            log.debug("practiceSettingDisplayName: " + practiceSettingDisplayName);
                            doc.setPracticeSettingDisplayName(practiceSettingDisplayName);

                            //extract typeCode
                            String typeCode = extractClassificationMetadata(classifications, XDS_TYPE_CODE_CLASSIFICATION, XDS_NODE_REPRESENTATION);
                            log.debug("typeCode: " + typeCode);
                            doc.setTypeCode(typeCode);

                            String typeCodeScheme = extractClassificationMetadata(classifications, XDS_TYPE_CODE_CLASSIFICATION, XDS_CODING_SCHEME_SLOT, 0);
                            log.debug("typeCodeScheme: " + typeCodeScheme);
                            doc.setTypeCodeScheme(typeCodeScheme);

                            String typeCodeDisplayName = extractClassificationMetadata(classifications, XDS_TYPE_CODE_CLASSIFICATION, XDS_NAME);
                            log.debug("typeCodeDisplayName: " + typeCodeDisplayName);
                            doc.setTypeCodeDisplayName(typeCodeDisplayName);

                            //extract eventCodes
                            extractEventCodes(classifications, doc);

                            //get the document byte array from the hashmap populated earlier
                            doc.setRawData(storeRequestValue);

                            //default value for new documents
                            //TODO implement logic for the replacement of a document - it means changing the status of the referenced document in the submission set association element
                            doc.setStatus(XDS_STATUS);
                            doc.setAvailablityStatus(XDS_AVAILABLILTY_STATUS_APPROVED);
                            //parent document id and relationship values deal with document replacements
                            //                        doc.setParentDocumentId(getChildElementStringValue(documentElement, "parentDocumentId"));
                            //                        doc.setParentDocumentRelationship(getChildElementStringValue(documentElement, "parentDocumentRelationship"));

                            //TODO verify that this size logic is correct - it seems kludgy
                            doc.setSize((long) storeRequestValue.length);

                            //TODO concatenate the adapter server's uri to the document unique id
                            doc.setDocumentUri(documentUniqueId);

                            DocumentService docService = new DocumentService();

                            //set the NHINC repository documentId value
                            if (requestHasReplacementAssociation) {
                                //query for the documentId using the documentUniqueId
                                long documentid = queryRepositoryByPatientId(doc.getPatientId(), doc.getClassCode(), doc.getStatus(), doc.getDocumentUniqueId(), docService);
                                doc.setDocumentid(documentid);
                            }
                            //call the DocumentService.save method
                            docService.saveDocument(doc);
                            log.debug("doc.documentId: " + doc.getDocumentid());
                            //                        log.debug("document.isPersistent: " + doc.isPersistent()); //TODO need a better way to determine if the doc was actually persisted.

                            //determine if the save was successful - Hibernate will generate
                            //a documentId for the record and populate this value in the
                            //document object if the save was successful.
                            if (doc.getDocumentid() < 1) {
                                RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
                                error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not store a document.");
                                error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                                    "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet storeDocument"); //kludgy?
                                error.setErrorCode(XDS_ERROR_CODE_REPOSITORY_ERROR);
                                error.setSeverity(XDS_ERROR_SEVERITY_ERROR);
                                error.setValue(XDS_REPOSITORY_ERROR + "\n" + "DocumentUniqueId: " + documentUniqueId);

                                errorList.getRegistryError().add(error);
                            }

                        } //if (extrinsicObject != null)
                    } //if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType)

                } //for (JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType> identifiableObject : identifiableObjectList)
            }
        } //if body is not null


        //return the correct response based on the results of the query.
        String responseStatus = null;
        if ((errorList.getRegistryError().isEmpty()) && (errorList.getRegistryError().size() == 0)) {
            responseStatus = XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS;
        } else {
            responseStatus = XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
            registryResponse.setRegistryErrorList(errorList);
        }

        registryResponse.setStatus(responseStatus);
        return registryResponse;
    }

    private boolean checkForReplacementAssociation(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList, RegistryErrorList errorList) {
        boolean replacementAssociationExists = false;

        for (int i = 0; i < identifiableObjectList.size(); i++) {
            log.debug("Item " + i + " identifiableObject is of DeclaredType: " + identifiableObjectList.get(i).getDeclaredType());

            //the getValue method will return the non-JAXBElement<? extends...> object
            Object tempObj = identifiableObjectList.get(i).getValue();

            if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1) {
                //TODO logic for the replacement of a document - it means changing the status of the referenced document in the submission set association element
                //WARNING: The following logic is NOT XDS compliant - we are assuming that the document in the request is already persisted and that we are only updating
                //that document. If there is another document that actually replaces/deprecates the old, then this logic will not work.
                AssociationType1 associationObj = (AssociationType1) tempObj;

                log.debug("associationType object present");

                if (associationObj == null) {
                    RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
                    error.setCodeContext("ProvideAndRegisterDocumentSetRequest message handler did not find a required element");
                    error.setLocation("DocumentRepositoryService.documentRepositoryProvideAndRegisterDocumentSetB -> " +
                        "DocumentRepositoryHelper.documentRepositoryProvideAndRegisterDocumentSet"); //kludgy?
                    error.setErrorCode(XDS_ERROR_CODE_MISSING_DOCUMENT_METADATA);
                    error.setSeverity(XDS_ERROR_SEVERITY_SEVERE);
                    error.setValue(XDS_MISSING_DOCUMENT_METADATA + "associationType element is null.");

                    errorList.getRegistryError().add(error);

                    log.error("Error Location: " + error.getLocation() + "; \n" + "Error Severity: " + error.getSeverity() + "; \n" + "Error ErrorCode: " + error.getErrorCode() + "; \n" + "Error CodeContext: " + error.getCodeContext());

                } else {
                    //check to see if the associationType is rplc
                    String associationType = associationObj.getAssociationType();
                    log.debug("Association element associationType = " + associationType);
                    if (XDS_ASSOCIATION_TYPE_REPLACE.equalsIgnoreCase(associationType)) {
                        replacementAssociationExists = true;
                        break;
                    } //if (XDS_ASSOCIATION_TYPE_REPLACE.equalsIgnoreCase(associationType))
                    else {
                        replacementAssociationExists = false;
                    }

                } //else associationObj is not null
            } //if (tempObj instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1)
        } //for (int i = 0; i < identifiableObjectList.size(); i++)

        log.debug("replacementAssociationExists = " + replacementAssociationExists);
        return replacementAssociationExists;
    }

    private long queryRepositoryByPatientId(String sPatId, String sClassCode, String sStatus, String sDocUniqueId, DocumentService docService) {
        long nhincDocRepositoryDocId = 0;

        //query for the doc unique id
        DocumentQueryParams params = new DocumentQueryParams();
        params.setPatientId(sPatId);
        List<String> lClassCodeList = new ArrayList();
        lClassCodeList.add(sClassCode);
        params.setClassCodes(lClassCodeList);
        List<String> lStatus = new ArrayList();
        lStatus.add(sStatus);
        params.setStatuses(lStatus);
        List<String> lUniqueId = new ArrayList();
        lUniqueId.add(sDocUniqueId);
        params.setDocumentUniqueId(lUniqueId);


        List<Document> documents = docService.documentQuery(params);
        if (documents != null && !documents.isEmpty()) {
            //the returned list should only have one document because the documentUniqueId should be unique in the repository.
            nhincDocRepositoryDocId = documents.get(0).getDocumentid();
        }

        return nhincDocRepositoryDocId;
    }

    /**
     * This method extracts the value of a metadata item of a document from a
     * list of XDS externalIdentifier objects given the name of the metadata item.
     * @param externalIdentifiers List of externalIdentifier objects which may
     * contain the metadata item
     * @return Returns the string representation of the metadata item. Returns
     * null if not present.
     */
    private String extractMetadataFromExternalIdentifiers(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType> externalIdentifiers,
        String metadataItemName) {
        String metadataItemValue = null;
        log.debug("extractMetadataFromExternalIdentifiers metadataItemName: " + metadataItemName);

        //loop through the externalIdentifiers looking for the for the desired name
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType externalIdentifier : externalIdentifiers) {
            String externalIdentifierName = externalIdentifier.getName().getLocalizedString().get(0).getValue();
            log.debug("externalIdentifierName: " + externalIdentifierName);
            if (metadataItemName.equalsIgnoreCase(externalIdentifierName)) {
                metadataItemValue = externalIdentifier.getValue();
                break;
            }
        }

        return metadataItemValue;
    }

    /**
     * This method extracts metadata from the XDS classification element given
     * the slotname of the metadata item.
     * @param classifications A list of classifications to search through.
     * @param classificationSchemeUUID The classification scheme idendifier to search for.
     * @param slotName The name of the metadata item within the classification element.
     * @param valueIndex In case there are multiple values for the metadata item,
     *                   the option to choose a single value or all values (i.e. -1).
     * @return Returns the value of the metadata item found in the XDS classification
     *         element given the slotname.
     */
    private String extractClassificationMetadata(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications, String classificationSchemeUUID, String slotName, int valueIndex) {
        String classificationValue = null;

        //loop through the classifications looking for the desired classification uuid
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
     * This method extracts classification element metadata for non-slot values
     * given the classification code uuid.
     * The following will be retrieved depending on the value of the classificationValueName:
     *  - the classification code value (nodeRepresentation)
     *  - the representation of the code (nodeRepresentationName)
     *  - the id of the extrinsicObject referenced by the given classification (classificationObject)
     *  - the id of the classificationObject element in the request (id)
     * @param classifications A list of classifications to search through.
     * @param classificationSchemeUUID The classification scheme idendifier to search for.
     * @param classificationValueName A string value indicating whether this method
     *                                 should return the classification code
     *                                 representation, the code itself, the id of
     *                                 the classification element, or the id of
     *                                 the extrinsicObject element that the classification
     *                                 refers to.
     * @return Returns the value of the metadata item found in the XDS classification
     *         element given the classification scheme and the name of the desired
     *         metadata element.
     */
    private String extractClassificationMetadata(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications, String classificationSchemeUUID, String classificationValueName) {
        String classificationValue = null;

        log.debug("Looking for classificationScheme=" + classificationSchemeUUID);
        log.debug("Looking for classificationValueName=" + classificationValueName);
        //loop through the classifications looking for the desired classification uuid
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications) {
            String classificationSchemeName = classification.getClassificationScheme();
            log.debug("Found classificationScheme=" + classificationSchemeName);

            if (classificationSchemeUUID.equals(classificationSchemeName)) {
                if (classificationValueName.equals(XDS_NAME)) {
                    classificationValue = classification.getName().getLocalizedString().get(0).getValue();
                } else if (classificationValueName.equals(XDS_NODE_REPRESENTATION)) {
                    classificationValue = classification.getNodeRepresentation();
                } else if (classificationValueName.equals(XDS_CLASSIFIED_OBJECT)) {
                    classificationValue = classification.getClassifiedObject();
                } else if (classificationValueName.equals(XDS_CLASSIFICATION_ID)) {
                    classificationValue = classification.getClassifiedObject();
                }
                break; //found desired classification, have values, exit loop
            } //if (classificationSchemeUUID.equals(classificationSchemeName))
        }
        if (classificationValue != null &&
            !classificationValue.equals("")) {
            classificationValue = StringUtil.extractStringFromTokens(classificationValue, "'()");
        }
        log.debug(classificationValueName + ": " + classificationValue);
        return classificationValue;
    }

    /**
     * Extracts the valueIndex value from an XDS request slot for a given metadata name.
     * @param documentSlots A list of XDS metadata slots
     * @param slotName The name of the slot containing the desired metadata item
     * @param valueIndex For slot multivalued possibilities, the index value desired.
     *                   If the value is < 0 then all values in the value list are
     *                   returned in a '~' delimited list.
     * @return Returns the value of the first metadata value with the given metadata
     * name. Null if not present.
     */
    private String extractMetadataFromSlots(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots, String slotName, int valueIndex) {
        log.debug("extractMetadataFromSlots slotname: " + slotName + "; index: " + valueIndex);
        String slotValue = null;
        StringBuffer slotValues = null;
        boolean returnAllValues = false;
        if (valueIndex < 0) {
            returnAllValues = true;
            slotValues = new StringBuffer();
        }
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (slotName.equals(slot.getName())) {
                log.debug("Found " + slotName + ": " + slot.getValueList().getValue());
                if (returnAllValues) {
                    int listSize = slot.getValueList().getValue().size();
                    int counter = 0;
                    Iterator iter = slot.getValueList().getValue().iterator();
                    while (iter.hasNext()) {
                        String value = (String) iter.next();
                        slotValues.append(value);
                        counter++;
                        if (counter < listSize) {
                            slotValues.append(VALUE_LIST_SEPERATOR);
                        }
                    }

                } else {
                    slotValue = slot.getValueList().getValue().get(valueIndex);
                }
                break; //found desired slot, have values, exit loop
            } //if (slotName.equals(slot.getName()))
        } //for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)
        if (returnAllValues) {
            slotValue = slotValues.toString();
        }

        log.debug(slotName + ": " + slotValue);
        return slotValue;
    }

    /**
     * Extracts the extra metadata elements from an XDS request slot
     * and prepares them for persistence into the doc NHINC repository.
     * @param documentSlots A list of XDS metadata slots
     * @param doc The NHINC document object to be persisted.
     */
    private void extractExtraMetadataFromSlots(
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots,
        gov.hhs.fha.nhinc.docmgr.repository.model.Document doc) {
        log.debug("extractExtraMetadataFromSlots");
        Set<ExtraSlot> extraSlots = new HashSet<ExtraSlot>();
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (slot.getName().startsWith(XDS_EXTRA_METADATA_START)) {
                log.debug("Found " + slot.getName() + ": " + slot.getValueList().getValue());

                StringBuffer slotValues = new StringBuffer();
                int listSize = slot.getValueList().getValue().size();
                int counter = 0;
                Iterator iter = slot.getValueList().getValue().iterator();
                while (iter.hasNext()) {
                    String value = (String) iter.next();
                    slotValues.append(value);
                    counter++;
                    if (counter < listSize) {
                        slotValues.append(VALUE_LIST_SEPERATOR);
                    }
                }

                ExtraSlot extraSlot = new ExtraSlot();
                extraSlot.setDocument(doc);
                extraSlot.setExtraSlotName(slot.getName());
                extraSlot.setExtraSlotValue(slotValues.toString());
                extraSlots.add(extraSlot);
            } //if (slot.getName().startsWith(XDS_EXTRA_METADATA_START))

        } //for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)

        doc.setExtraSlots(extraSlots);
        log.debug("Found " + extraSlots.size() + "extraSlots.");
    }

    /**
     * Extracts the sourcePatientInfo pid value from an XDS request slot for the sourcePatientInfo element.
     * @param documentSlots A list of XDS metadata slots
     * @param patientInfoName The name of the sourcePatientInfo pid containing the desired metadata item
     * @return Returns the value of the first metadata value with the given metadata
     * name. Null if not present.
     */
    private String extractPatientInfo(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots, String patientInfoName) {
        String slotValue = null;

        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots) {
            if (XDS_SOURCE_PATIENT_INFO_SLOT.equals(slot.getName())) {
                Iterator iter = slot.getValueList().getValue().iterator();
                while (iter.hasNext()) {
                    String nextSlotValue = (String) iter.next();
                    if (nextSlotValue.startsWith(patientInfoName)) {
                        slotValue = nextSlotValue.substring(patientInfoName.length() + 1);
                        log.debug(patientInfoName + " extractionValue: " + slotValue);
                    }
                }
            } //if (XDS_SOURCE_PATIENT_INFO_SLOT.equals(slot.getName()))
        } //for (oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1 slot : documentSlots)

        return slotValue;
    }

    /**
     * Parses a string and returns a Date object having the given dateformat
     * @param dateString String to be parsed containing a date
     * @param dateFormat Format of the date to be parsed
     * @return Returns the Date object for the given string and format.
     */
    private Date parseDate(String dateString, String dateFormat) {
        Date parsed = null;
        if ((dateString != null) && (dateFormat != null)) {
            try {
                String formatString = DocumentLoadUtil.prepareDateFormatString(dateFormat, dateString);
                parsed = new SimpleDateFormat(formatString).parse(dateString);
            } catch (Throwable t) {
                log.warn("Error parsing '" + dateString + "' using format: '" + dateFormat + "'", t);
            }
        }
        return parsed;
    }

    /**
     * This method extracts the list of event codes and prepares them for persistence into the doc NHINC repository
     * @param classifications The list of metadata classification objects for the document
     * @param doc The NHINC document object to be persisted.
     */
    private void extractEventCodes(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications, gov.hhs.fha.nhinc.docmgr.repository.model.Document doc) {
        Set<EventCode> eventCodes = new HashSet<EventCode>();
        for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications) {
            String classificationSchemeName = classification.getClassificationScheme();
            if (XDS_EVENT_CODE_LIST_CLASSIFICATION.equals(classificationSchemeName)) {
                EventCode eventCode = new EventCode();
                eventCode.setDocument(doc);

                //eventCode.setEventCodeId(getChildElementLongValue(eventCodeElement, "codeId"));
                eventCode.setEventCode(classification.getNodeRepresentation());
                eventCode.setEventCodeScheme(extractMetadataFromSlots(classification.getSlot(), XDS_CODING_SCHEME_SLOT, 0));
                eventCode.setEventCodeDisplayName(classification.getName().getLocalizedString().get(0).getValue());

                eventCodes.add(eventCode);
            }

        } //for (oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType classification : classifications)

        doc.setEventCodes(eventCodes);
    }

    /*
     * Retrieve the localHomeCommunityId from the Gateway Property File
     */
    private String retrieveHomeCommunityId() {
        String homeCommunityId = null;
        try {
            PropertyAccessor oProp = PropertyAccessor.getInstance();
            homeCommunityId = "urn:oid:" + oProp.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_HOME_COMMUNITY);
        } catch (Throwable t) {
            log.error("Error retrieving the home community id: " + t.getMessage(), t);
        }
        return homeCommunityId;
    }

    private void InsertResponseError(RegistryErrorList list, String XDSDocumentErrorCode, String context) {
        RegistryError err = new RegistryError();
        err.setErrorCode(XDSDocumentErrorCode);
        err.setCodeContext(context);
        err.setSeverity(XDS_ERROR_REGISTRY_SEVERITY_ERROR);
        list.getRegistryError().add(err);
    }
}
