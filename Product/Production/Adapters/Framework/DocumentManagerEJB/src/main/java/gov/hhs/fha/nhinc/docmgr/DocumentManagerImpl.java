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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.docmgr.repository.util.BaseRequestParameters;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the DocumentManager web serivce.
 *
 * @author cmatser
 */
public class DocumentManagerImpl {

    /** Infobutton xds slot for query result. */
    public static final String INFOBUTTON_QUERY_SLOT = "$InfoButtonQuery";
    public static final String INFOBUTTON_RETRIEVERESULT_SLOT = "urn:infobuttonSearchResult";
    /** Property constants. */
    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";
    public static final String INBOUND_DOCUMENT_REPOSITORY_ID_PROP = "inboundDocumentRepositoryId";
    public static final String INBOUND_DOCUMENT_MIRTH_WSDL = "mirthChannel";
    public static final String POLICY_REPOSITORY_ID_PROP = "policyRepositoryId";
    public static final String DOCUMENT_UNIQUE_OID_PROP = "documentUniqueOID";
    public static final String DOCMGR_QUEUE = "documentManager.queue";
    public static final String DOCMGR_QUEUE_FACTORY = "documentManager.queueFactory";
    public static final String NHINDOCQUERY_ENDPOINT_PROP = "nhinDocQuery";
    public static final String NHINDOCRETRIEVE_ENDPOINT_PROP = "nhinDocRetrieve";
    public static final String INFOBUTTON_COUNT_PROP = "infobutton.count";
    public static final String INFOBUTTON_PREFIX = "infobutton.";
    public static final String INFOBUTTON_NAME_SUFFIX = ".name";
    public static final String INFOBUTTON_OID_SUFFIX = ".oid";
    /** InfoButton Assertion properties. */
    public static final String IB_SIGNATURE_DATE_PROP = "ib.assertion.signature_date";
    public static final String IB_EXPIRATION_DATE_PROP = "ib.assertion.expiration_date";
    public static final String IB_ROLE_NAME_PROP = "ib.assertion.role_name";
    public static final String IB_ROLE_CODE_PROP = "ib.assertion.role_code";
    public static final String IB_ROLE_CODE_SYSTEM_PROP = "ib.assertion.role_code_system";
    public static final String IB_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.role_code_system_name";
    public static final String IB_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.role_code_system_version";
    public static final String IB_USER_DOD_EXTENSION_PROP = "ib.assertion.user_dod_extension";
    public static final String IB_DOD_ROLE_NAME_PROP = "ib.assertion.dod_role_name";
    public static final String IB_DOD_ROLE_CODE_PROP = "ib.assertion.dod_role_code";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_PROP = "ib.assertion.dod_role_code_system";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.dod_role_code_system_name";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.dod_role_code_system_version";
    public static final String IB_PURPOSE_OF_USE_ROLE_NAME_PROP = "ib.assertion.purpose_of_use_role_name";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_PROP = "ib.assertion.purpose_of_use_role_code";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_PROP = "ib.assertion.purpose_of_use_role_code_system";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.purpose_of_use_role_code_system_name";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.purpose_of_use_role_code_system_version";
    public static final String IB_CLAIM_FORM_REF_PROP = "ib.assertion.claim_form_ref";
    public static final String IB_CLAIM_FORM_STRING_PROP = "ib.assertion.claim_form_string";
    /** Date precision. */
    public static final int XDS_DATE_QUERY_FROM_PRECISION = 8;
    public static final int XDS_DATE_QUERY_TO_PRECISION = 14;
    public static final int ASSERTION_DOB_PRECISION = 12;
    public static final String XDS_DATE_FORMAT_FULL = "yyyyMMddHHmmssZ";
    /** Value for archive field in metadata */
    public static final String XDS_ARCHIVE_SLOT = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";
    /** Repository id field for segmenting the data. */
    public static final String XDS_REPOSITORY_ID_RETRIEVE = "repositoryUniqueId";
    public static final String XDS_DOCUMENT_UNIQUE_ID_RETRIEVE = "documentUniqueId";
    public static final String XDS_HOME_COMMUNITY_ID_RETRIEVE = "homeCommunityId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";
    public static final String XDS_REPOSITORY_DOCUMENT_UNIQUE_ID = "$XDSDocumentEntryUniqueId";
    public final static String XDSHasBeenAccessed = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";
    public static final String INFOBUTTON_PATIENTID_SLOT = "$XDSDocumentEntryPatientId";
    /** Error values */
    public static final String XDS_SUCCESS_STATUS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_FAILED_STATUS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_ERROR_CODE = "DOCUMENT_MANAGER_ERROR";
    public static final String XDS_ERROR_SEVERITY = "ERROR";
    public static final int INFOBUTTON_ERROR = -1;
    /** Message constants. */
    public static final String MESSAGE_START_SUCCESS = "Success";
    public static final String MESSAGE_START_FAILURE = "Failure";
    public static final String FAILURE_TICKET = "-1";
    /** Date format for XDS */
    public static final DateFormat XDS_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    /** Logging. */
    private static Log log = LogFactory.getLog(DocumentManagerImpl.class);

    ////////////////////////////////////////////////////////////////////////////
    //Interface implementation
    ////////////////////////////////////////////////////////////////////////////

    /*
     * RegistryError
     */
    private RegistryResponseType formatRegistryErrorResponse(String statusCode, String errorCode, String severityCode,
        String codeContext, String location) {
        RegistryResponseType result = null;
        result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
        result.setStatus(statusCode);
        RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryErrorList();
        RegistryError error = new RegistryError();
        error.setErrorCode(errorCode);
        error.setSeverity(severityCode);
        error.setCodeContext(codeContext);
        error.setLocation(location);
        errorList.getRegistryError().add(error);
        result.setRegistryErrorList(errorList);
        return result;
    }

    /*
     * Check the basic parameters documentId, repositoryId and homeCommunityId
     *
     * When these values are NULL, that indicates they are not expected to be passed to the given function
     */
    private void analyzeBaseParameters(BaseRequestParameters baseValues) {
        String message = "Request message contained these key values: ";

        // WARN on no valid repository id in query
        String repositoryId = baseValues.getRepositoryId();
        if (repositoryId != null && repositoryId.equals("")) {
            log.warn("Request did not contain a repository id.");
        } else {
            if (!getValidRepositories().contains(repositoryId)) {
                //throw new Exception("Repository id not valid: " + repositoryId);
                log.warn("Request contained an invalid repository id=" + repositoryId);
            }
            message += " repositoryId = " + repositoryId;
        }
        String documentId = baseValues.getDocumentId();
        if (documentId != null && documentId.equals("")) {
            log.warn("Request did not conain a doumentId");
        } else {
            message += " documentId = " + documentId;
        }
        String homeCommunityId = baseValues.getHomeCommunityId();
        if (homeCommunityId != null && homeCommunityId.equals("")) {
            log.warn("Request did not conain a homeCommunityId");
        } else {
            message += " homeCommunityId = " + homeCommunityId;
        }
        log.debug(message);
    }

    /**
     * Query for document.
     *
     * Before we do, ensure repository Id is in the request.  This will tell the
     * repository to filter the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryForDocument(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Querying document archive.");

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse result = null;

        try { // Call DocumentRepository

            // WARN on no valid repository id in query
            BaseRequestParameters baseValues = pullBaseRequestParameters(body);
            analyzeBaseParameters(baseValues);

            result = new DocumentRegistryHelper().documentRegistryRegistryStoredQuery(body);
        } catch (Exception e) {
            result = new oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse();
            result.setStatus(XDS_FAILED_STATUS);
            result.setRegistryObjectList(new RegistryObjectListType());

            log.error("Error querying for document.", e);
        }

        return result;
    }

    /**
     * Retrieve document.
     * 
     * Before we do, ensure repository Id is in the request.
     *
     * Here we can just forward the request to the repository.  For a real XDS
     * server, we would need to call the appropriate repository for the document.
     *
     * @param body
     * @return
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Retrieving document.");

        RetrieveDocumentSetResponseType result = null;

        try { // Call Web Service Operation

            //Ensure valid repository id in query
            BaseRequestParameters baseValues = pullBaseRequestParameters(body);
            analyzeBaseParameters(baseValues);
            result = new DocumentRepositoryHelper().documentRepositoryRetrieveDocumentSet(body);
        } catch (Exception e) {
            RegistryResponseType response = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            response.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList();
            RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not retrieve document.");
            error.setLocation("DocumentManagerImpl.retrieveDocument");
            errorList.getRegistryError().add(error);
            response.setRegistryErrorList(errorList);
            result.setRegistryResponse(response);

            log.error("Error retrieving document.", e);
        }

        return result;
    }

    /**
     * Store document.
     * 
     * Perform the actual store.  A unique Id is inserted if one hasn't already
     * been created.
     *
     * Before we do, we ensure a valid repository Id is in the request.  This will tell the
     * repository to store the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     *
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Storing document.");

        oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType result = null;

        try { // Call Web Service Operation

            //Insert doc unique id if one does not exist
            if (!createDocumentUniqueId(body)) {
                throw new Exception("Failed to create document unique Id");
            }

            //Ensure valid repository id in query
            BaseRequestParameters baseValues = pullBaseRequestParameters(body);
            analyzeBaseParameters(baseValues);

            PropertyAccessor oProp = PropertyAccessor.getInstance();

            String inboundRepositoryId = oProp.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);
            if (baseValues.getRepositoryId().equals(inboundRepositoryId)) {
                try {
                    String mirthWSDL = oProp.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_MIRTH_WSDL);
                    if ((mirthWSDL != null) && !mirthWSDL.isEmpty()) {
                        String msgBody = null;
                        if ((body.getDocument() != null) && (body.getDocument().size() > 0)) {
                            msgBody = new String(body.getDocument().get(0).getValue());
                        }

                        if (msgBody == null) {
                            throw new Exception("Message body not found in request.");
                        }
                    }
                } catch (Exception e) {
                    log.error("Error sending inbound document to Mirth channel", e);
                    //continue to store locally
                }
            }
            result = new DocumentRepositoryHelper().documentRepositoryProvideAndRegisterDocumentSet(body);
        } catch (Exception e) {
            result = formatRegistryErrorResponse(XDS_FAILED_STATUS, XDS_ERROR_CODE, XDS_ERROR_SEVERITY,
                "Could not store document.", "DocumentManagerImpl.storeDocument");
            log.error("Error storing document.", e);
        }

        return result;
    }

    /**
     * The document is archived by first querying for the metadata, then querying for
     * the document itself.  With this information, we can re-store the document as a replacement
     * of the original (updating the hasBeenAccessed flag).  The hasBeenAccessed flag is our
     * archiving flag.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerArchiveDocument(gov.hhs.fha.nhinc.common.docmgr.ArchiveDocumentRequestType body) {
        log.debug("Archiving document.");

        RegistryResponseType result = null;
        String homeCommunityId = null;
        String repositoryId = null;
        String documentUniqueId = null;

        try {
            //Pull out parameters
            homeCommunityId = body.getHomeCommunityId();
            repositoryId = body.getRepositoryUniqueId();
            documentUniqueId = body.getDocumentUniqueId();

            BaseRequestParameters baseValues = new BaseRequestParameters(repositoryId, documentUniqueId, homeCommunityId);
            analyzeBaseParameters(baseValues);

            //Ensure valid repository id in query
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }
            ArchiveDocument ad = new ArchiveDocument();
            result = ad.setArchiveMetaData(repositoryId, documentUniqueId, homeCommunityId);
        } catch (Exception e) {
            result = formatRegistryErrorResponse(XDS_FAILED_STATUS, XDS_ERROR_CODE, XDS_ERROR_SEVERITY,
                "Could not archive document.", "DocumentManagerImpl.archiveDocument");
            log.error("Error archving document.", e);

            return result;
        }

        if (result != null) {
            result.setStatus(XDS_SUCCESS_STATUS);
        } else {
            result = formatRegistryErrorResponse(XDS_FAILED_STATUS, XDS_ERROR_CODE, XDS_ERROR_SEVERITY,
                "Could not archive document. Null returned.", "DocumentManagerImpl.archiveDocument");
        }
        return result;
    }

    /**
     * Update document slot.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerUpdateDocumentSlot(gov.hhs.fha.nhinc.common.docmgr.UpdateDocumentSlotRequestType body) {
        log.debug("Updating document slot.");

        RegistryResponseType result = null;
        String homeCommunityId = null;
        String repositoryId = null;
        String documentUniqueId = null;

        try {
            //Pull out parameters
            homeCommunityId = body.getHomeCommunityId();
            repositoryId = body.getRepositoryUniqueId();
            documentUniqueId = body.getDocumentUniqueId();

            BaseRequestParameters baseValues = new BaseRequestParameters(repositoryId, documentUniqueId, homeCommunityId);
            analyzeBaseParameters(baseValues);

            //Ensure valid repository id in query
            if (!getValidRepositories().contains(repositoryId)) {
                log.error("DocumentManagerUpdateDocumentSlot: Invalid repository id provided. Id = " + repositoryId);
            }

            //Handle update
            doUpdateSlot(repositoryId, documentUniqueId, homeCommunityId, body.getSlotName(), body.getSlotValueList());

            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_SUCCESS_STATUS);
        } catch (Exception e) {
            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not update document slot.");
            error.setLocation("DocumentManagerImpl.updateDocumentSlot");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);

            log.error("Error updating document slot.", e);

            return result;
        }

        return result;
    }

    /**
     * Generate unique Id that can be used for document unique ids.
     * 
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType generateUniqueId(gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdRequestType request) {
        gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType response = new gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType();
        String oid = "1.1.1.1.1.1";

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        try {
            oid = oProp.getProperty(REPOSITORY_PROPERTY_FILE, DOCUMENT_UNIQUE_OID_PROP);
        } catch (PropertyAccessException e) {
            log.error("Error accessing property:" + DOCUMENT_UNIQUE_OID_PROP + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        //OID^extension format
        response.setUniqueId(oid + "^" + new Date().getTime());

        return response;
    }

    /**
     * Internal method to handle document slot update.
     * 
     * @param repositoryId
     * @param documentUniqueId
     * @param slotName
     * @param slotValueList
     * @throws Exception
     */
    private void doUpdateSlot(String repositoryId, String documentUniqueId, String homeCommunityId,
        String slotName, List<String> slotValueList)
        throws Exception {

        try {
            log.debug("Querying document to update slot.");

            //Create metadata query
            AdhocQueryRequest metaRequest = createQuery(
                new String[]{
                    "$XDSRepositoryUniqueId",
                    "$XDSDocumentEntryUniqueId",},
                new String[]{
                    repositoryId,
                    documentUniqueId,});

            //Perform query for metadata
            AdhocQueryResponse queryResponse = documentManagerQueryForDocument(metaRequest);

            log.debug("Retrieving document to update slot.");

            //Create document retrieve
            RetrieveDocumentSetRequestType docRequest = createRetrieve(
                repositoryId, documentUniqueId, homeCommunityId);

            //Retrieve document
            RetrieveDocumentSetResponseType docResponse = documentManagerRetrieveDocument(docRequest);

            log.debug("Replacing document to update slot.");

            //Create document retrieve
            //Create replacement
            ProvideAndRegisterDocumentSetRequestType replaceRequest = createReplaceRequest(
                queryResponse, docResponse, slotName, slotValueList);

            //Do store with updated metdata
            documentManagerStoreDocument(replaceRequest);
        } catch (Exception e) {
            log.error("Error performing slot update.", e);
            throw new Exception("Error performing slot update.", e);
        }

    }

    /**
     * Internal method to create query used in document update slot.
     *
     * @param names
     * @param values
     * @return
     */
    private AdhocQueryRequest createQuery(String[] names, String[] values) {
        if ((names == null) || (values == null) || (names.length != values.length)) {
            return null;
        }

        AdhocQueryRequest request = new AdhocQueryRequest();

        //Create FindDocuments query
        AdhocQueryType query = new AdhocQueryType();
        query.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

        for (int i = 0; i < names.length; i++) {
            SlotType1 slot = new SlotType1();
            slot.setName(names[i]);
            ValueListType valList = new ValueListType();
            valList.getValue().add(values[i]);
            slot.setValueList(valList);
            query.getSlot().add(slot);
        }

        request.setAdhocQuery(query);

        ResponseOptionType option = new ResponseOptionType();
        option.setReturnComposedObjects(true);
        option.setReturnType("LeafClass");
        request.setResponseOption(option);

        return request;
    }

    /**
     * Internal method to create retrieve request used by doument update slot.
     *
     * @param repositoryId
     * @param documentUniqueId
     * @return
     */
    private RetrieveDocumentSetRequestType createRetrieve(String repositoryId, String documentUniqueId, String homeCommunityId) {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();

        //Create retrieve request
        DocumentRequest retrieve = new DocumentRequest();
        retrieve.setRepositoryUniqueId(repositoryId);
        retrieve.setDocumentUniqueId(documentUniqueId);
        retrieve.setHomeCommunityId(homeCommunityId);
        request.getDocumentRequest().add(retrieve);

        return request;
    }

    /**
     * Internal method to create store request used by document update slot.
     * @param queryResponse
     * @param docResponse
     * @return
     * @throws Exception
     */
    private ProvideAndRegisterDocumentSetRequestType createReplaceRequest(
        AdhocQueryResponse queryResponse,
        RetrieveDocumentSetResponseType docResponse,
        String slotName,
        List<String> slotValueList)
        throws Exception {

        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();

        //Check for document metadata
        ExtrinsicObjectType extrinsic = null;
        if (queryResponse.getRegistryObjectList() == null) {
            throw new Exception("No document metadata returned.");
        }

        //Find document metadata
        List<JAXBElement<? extends IdentifiableType>> objectList = queryResponse.getRegistryObjectList().getIdentifiable();
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;
                break;
            }
        }

        //Check if metadata found
        if (extrinsic == null) {
            throw new Exception("Document metadata not found in query response.");
        }

        //Check for document data
        if (docResponse.getDocumentResponse().isEmpty()) {
            throw new Exception("No document metadata returned.");
        }

        //Update metadata
        SlotType1 updateSlot = findSlot(extrinsic, slotName);
        //Create if it doesn't exist
        if (updateSlot == null) {
            updateSlot = new SlotType1();
            updateSlot.setName(slotName);
            extrinsic.getSlot().add(updateSlot);
        }
        ValueListType valList = new ValueListType();
        valList.getValue().addAll(slotValueList);
        updateSlot.setValueList(valList);

        /**The Submission set is actually ignored by the reference implemenation.*/
        //Create submission set
        RegistryPackageType registryPackage = new RegistryPackageType();
        registryPackage.setId("SubmissionSet01");
        registryPackage.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        //Submission time
        Date now = new Date();
        addSlot(registryPackage, "submissionTime", new String[]{DocumentManagerImpl.XDS_DATE_FORMAT.format(now)});

        //Add submission author classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d", //scheme
            "", //node representation
            "id_20", //id
            null, //name
            new String[]{
                "authorPerson",
                "authorInstitution",
                "authorRole",
                "authorSpecialty",}, //slot names
            new String[][]{
                new String[]{"^DocumentManager^Automated^^^"},
                new String[]{
                    "LocalMHS",},
                new String[]{"Automated"},
                new String[]{"Automated"},} //slot values
            );

        //Add submission content type classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", //scheme
            "contentTypeCode", //node representation
            "id_21", //id
            "contentTypeDisplayName", //name
            new String[]{
                "codingScheme",}, //slot names
            new String[][]{
                new String[]{"Connect-a-thon contentTypeCodes"},} //slot values
            );

        //Add submission uniqueId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
            "id_22", //id
            "XDSSubmissionSet.uniqueId", //name
            new java.rmi.server.UID().toString());

        //Add submission sourceId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832", //identificationScheme
            "id_23", //id
            "XDSSubmissionSet.sourceId", //name
            "1.1.1.1" //value
            );

        //Add submission patientId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", //identificationScheme
            "id_24", //id
            "XDSSubmissionSet.patientId", //name
            "IGNORED" //value
            );

        //Build association
        AssociationType1 association = new AssociationType1();
        association.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC");
        association.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
        association.setId("ID_25276323_1");
        association.setSourceObject("SubmissionSet01");
        association.setTargetObject(extrinsic.getId());

        //Add submission status to assocation
        addSlot(association, "SubmissionSetStatus", new String[]{"Original"});

        //Build classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationNode("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setClassifiedObject("SubmissionSet01");
        classification.setId("ID_25276323_3");

        //Build registry object
        ObjectFactory rimObjectFactory = new ObjectFactory();
        JAXBElement<ExtrinsicObjectType> extrinsicMetadata = rimObjectFactory.createExtrinsicObject(extrinsic);
        JAXBElement<RegistryPackageType> submission = rimObjectFactory.createRegistryPackage(registryPackage);
        JAXBElement<AssociationType1> associationObject = rimObjectFactory.createAssociation(association);
        JAXBElement<ClassificationType> classificationObject = rimObjectFactory.createClassification(classification);
        RegistryObjectListType registryList = new RegistryObjectListType();
        registryList.getIdentifiable().add(extrinsicMetadata);
        registryList.getIdentifiable().add(submission);
        registryList.getIdentifiable().add(associationObject);
        registryList.getIdentifiable().add(classificationObject);

        //Build document object
        Document document = new Document();
        document.setId(extrinsic.getId());
        document.setValue(docResponse.getDocumentResponse().get(0).getDocument());

        //Add request to body for submission
        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
        submitObjects.setRegistryObjectList(registryList);
        request.setSubmitObjectsRequest(submitObjects);
        request.getDocument().add(document);

        return request;
    }

    /**
     * Add classification to submission object.
     *
     * @param registry
     * @param classifiedObject
     * @param classificationScheme
     * @param nodeRepresentation
     * @param id
     * @param name
     * @param slotNames
     * @param slotValues
     */
    private static void addClassification(
        RegistryObjectType registry,
        String classifiedObject,
        String classificationScheme,
        String nodeRepresentation,
        String id,
        String name,
        String[] slotNames,
        String[][] slotValues) {

        //Create classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationScheme(classificationScheme);
        classification.setNodeRepresentation(nodeRepresentation);
        classification.setClassifiedObject(classifiedObject);
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setId(id);

        //Classification name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            classification.setName(intlName);
        }

        //Slots
        for (int i = 0; i < slotNames.length; i++) {
            addSlot(classification, slotNames[i], slotValues[i]);
        }

        //Add classification
        registry.getClassification().add(classification);
    }

    /**
     * Add external identifier on submission object.
     *
     * @param registry
     * @param registryObject
     * @param identificationScheme
     * @param id
     * @param name
     * @param value
     */
    private static void addExternalIdentifier(
        RegistryObjectType registry,
        String registryObject,
        String identificationScheme,
        String id,
        String name,
        String value) {

        ExternalIdentifierType externalId = new ExternalIdentifierType();
        externalId.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        externalId.setRegistryObject(registryObject);
        externalId.setIdentificationScheme(identificationScheme);
        externalId.setId(id);
        externalId.setValue(value);

        //Identifier name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            externalId.setName(intlName);
        }

        //Add classification
        registry.getExternalIdentifier().add(externalId);
    }

    /**
     * Finds a slot in the document metadata.
     *
     * @param registry
     * @param slotName
     * @return
     */
    private SlotType1 findSlot(RegistryObjectType registry, String slotName) {
        SlotType1 result = null;

        for (SlotType1 slot : registry.getSlot()) {
            if (slot.getName().equals(slotName)) {
                result = slot;
                break;
            }
        }

        return result;
    }

    /**
     * Return a list of valid repository ids.
     * 
     * @return
     */
    private List<String> getValidRepositories() {
        List<String> repoIds = new LinkedList<String>();

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        try {
            repoIds.add(oProp.getProperty(REPOSITORY_PROPERTY_FILE, DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP));
            repoIds.add(oProp.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP));
            repoIds.add(oProp.getProperty(REPOSITORY_PROPERTY_FILE, POLICY_REPOSITORY_ID_PROP));
        } catch (PropertyAccessException e) {
            log.error("Error accessing repository id properties.", e);
        }

        return repoIds;
    }

    /**
     * Pull repository id out of query request.
     * 
     * @param body
     * @return
     */
    private String pullRepositoryId(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        String repositoryIdValue = null;

        for (SlotType1 slot : body.getAdhocQuery().getSlot()) {
            if (XDS_REPOSITORY_ID_QUERY.equals(slot.getName())) {
                repositoryIdValue = slot.getValueList().getValue().get(0);
                break;
            }
        }

        return repositoryIdValue;
    }

    /**
     * Pull Query Values out of query request.
     * 
     * @param body
     * @return
     */
    private BaseRequestParameters pullBaseRequestParameters(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        String repositoryIdValue = null;
        String documentIdValue = null;

        for (SlotType1 slot : body.getAdhocQuery().getSlot()) {
            if (XDS_REPOSITORY_ID_QUERY.equals(slot.getName())) {
                repositoryIdValue = slot.getValueList().getValue().get(0);
            }
            if (XDS_REPOSITORY_DOCUMENT_UNIQUE_ID.equalsIgnoreCase(slot.getName())) {
                documentIdValue = slot.getValueList().getValue().get(0);
            }
        }
        return new BaseRequestParameters(repositoryIdValue, documentIdValue, null);
    }

    /**
     * Pull query values out of retrieve request.
     * 
     * @param body
     * @return
     */
    private BaseRequestParameters pullBaseRequestParameters(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        String repositoryIdValue = null;
        String documentIdValue = null;
        String homeCommunityIdValue = null;

        if (!body.getDocumentRequest().isEmpty()) {
            repositoryIdValue = body.getDocumentRequest().get(0).getRepositoryUniqueId();
            documentIdValue = body.getDocumentRequest().get(0).getDocumentUniqueId();
            homeCommunityIdValue = body.getDocumentRequest().get(0).getHomeCommunityId();
        }

        return new BaseRequestParameters(repositoryIdValue, documentIdValue, homeCommunityIdValue);
    }

    /**
     * Pull query va;ies out of document store request.
     * 
     * @param body
     * @return
     */
    public BaseRequestParameters pullBaseRequestParameters(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        ExtrinsicObjectType extrinsic = null;
        String repositoryIdValue = null;
        String documentIdValue = null;
        String homeCommunityIdValue = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find repositoryl id (if present)
                for (SlotType1 slot : extrinsic.getSlot()) {
                    if (XDS_REPOSITORY_ID_RETRIEVE.equalsIgnoreCase(slot.getName())) {
                        repositoryIdValue = slot.getValueList().getValue().get(0);
                        break;
                    }
                    if (XDS_HOME_COMMUNITY_ID_RETRIEVE.equalsIgnoreCase(slot.getName())) {
                        homeCommunityIdValue = slot.getValueList().getValue().get(0);
                        break;
                    }
                    if (XDS_DOCUMENT_UNIQUE_ID_RETRIEVE.equalsIgnoreCase(slot.getName())) {
                        documentIdValue = slot.getValueList().getValue().get(0);
                        break;
                    }
                }
            }
        }

        return new BaseRequestParameters(repositoryIdValue, documentIdValue, homeCommunityIdValue);
    }

    /**
     * Sends message to Document Manager queue.
     * 
     * @param msg
     * @return ticket, detail
     */
    private String[] sendJMSMessage(java.io.Serializable jmsMsg) {
        String result[] = new String[2];
        String docMgrMsgQ = null;
        QueueConnection queueConnection = null;

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        try {
            //Get task queue name & queue factory
            docMgrMsgQ = oProp.getProperty(REPOSITORY_PROPERTY_FILE, DOCMGR_QUEUE);
            String docMgrQFactory = oProp.getProperty(REPOSITORY_PROPERTY_FILE, DOCMGR_QUEUE_FACTORY);

            //Get queue connection
            Context jndiContext = new InitialContext();
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup(docMgrQFactory);
            Queue queue = (Queue) jndiContext.lookup(docMgrMsgQ);

            //Create connection session
            queueConnection = queueConnectionFactory.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false,
                Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = queueSession.createSender(queue);

            ObjectMessage message = queueSession.createObjectMessage(jmsMsg);

            //Send message
            queueSender.send(message);

            //Set response info
            result[0] = message.getJMSMessageID();
            result[1] = MESSAGE_START_SUCCESS;
        } catch (PropertyAccessException pae) {
            String msg = MESSAGE_START_FAILURE + ": error accessing properties in file:" + REPOSITORY_PROPERTY_FILE + ".";
            log.error(msg, pae);
            result[0] = FAILURE_TICKET;
            result[1] = msg;
        } catch (NamingException ne) {
            String msg = MESSAGE_START_FAILURE + ": error creating connection to queue: " + docMgrMsgQ + ".";
            log.error(msg, ne);
            result[0] = FAILURE_TICKET;
            result[1] = msg;
        } catch (JMSException jmse) {
            String msg = MESSAGE_START_FAILURE + ": error occurred trying to send message to queue: " + docMgrMsgQ + ".";
            log.error(msg, jmse);
            result[0] = FAILURE_TICKET;
            result[1] = msg;
        } catch (Throwable t) {
            String msg = MESSAGE_START_FAILURE + ": error occurred trying to start NHIN query process.";
            log.error(msg, t);
            result[0] = FAILURE_TICKET;
            result[1] = msg;
        } finally {
            //Close queue
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                }
            }
        }

        return result;
    }

    /**
     * Insert a document unique Id if one does not already exist.  External
     * Identifier object should at least be in place.
     *
     * @param request
     * @return true if successful, false otherwise
     */
    private boolean createDocumentUniqueId(ProvideAndRegisterDocumentSetRequestType request) {

        String docUniqueId = null;
        ExternalIdentifierType externalId = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find doc unique identifier
                for (ExternalIdentifierType extId : extrinsic.getExternalIdentifier()) {
                    if ("XDSDocumentEntry.uniqueId".equals(extId.getName().getLocalizedString().get(0).getValue())) {
                        externalId = extId;
                        docUniqueId = extId.getValue();
                    }
                }
            }
        }

        //Check if external Id was found
        if (externalId == null) {
            return false;
        }

        //Check if docUniqueId needs filling
        if ((docUniqueId == null) || docUniqueId.isEmpty()) {
            docUniqueId = generateUniqueId(null).getUniqueId();
            externalId.setValue(docUniqueId);
        }

        return true;
    }

    /**
     * Add slot to submission object.
     *
     * @param registry - submission object
     * @param name - slot name
     * @param values - slot values
     */
    private static void addSlot(RegistryObjectType registry,
        String name, String[] values) {

        SlotType1 slot = new SlotType1();
        slot.setName(name);

        ValueListType valList = new ValueListType();
        for (String value : values) {
            valList.getValue().add(value);
        }

        slot.setValueList(valList);
        registry.getSlot().add(slot);
    }

    /**
     * Before storing, ensure that repositoryID is present.
     * 
     * @param request
     * @param repositoryId
     */
    private void insertRepositoryId(
        ProvideAndRegisterDocumentSetRequestType request,
        String repositoryId) {

        ExtrinsicObjectType extrinsic = null;
        SlotType1 repositoryIdSlot = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find repositoryl id (if present)
                for (SlotType1 slot : extrinsic.getSlot()) {
                    if (XDS_REPOSITORY_ID_RETRIEVE.equals(slot.getName())) {
                        repositoryIdSlot = slot;
                        break;
                    }
                }
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(extrinsic, XDS_REPOSITORY_ID_RETRIEVE, new String[]{repositoryId});
            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
    }

    /**
     * Before query, ensure repository id present.
     *
     * @param request
     * @param repositoryId
     */
    private void insertRepositoryIdQuery(
        AdhocQueryRequest request,
        String repositoryId) {

        SlotType1 repositoryIdSlot = null;

        //Find repositoryl id (if present)
        for (SlotType1 slot : request.getAdhocQuery().getSlot()) {
            if (XDS_REPOSITORY_ID_QUERY.equals(slot.getName())) {
                repositoryIdSlot = slot;
                break;
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(request.getAdhocQuery(), XDS_REPOSITORY_ID_QUERY, new String[]{repositoryId});
            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
    }

    /**
     * Create the nhin query for infoButton request.
     *
     * @param request
     * @return
     */
    private AdhocQueryRequest createInfoButtonQuery(gov.hhs.fha.nhinc.common.docmgr.InfoButtonQueryRequestType request) {
        AdhocQueryRequest retVal = new AdhocQueryRequest();
        retVal.setFederated(false);
        retVal.setStartIndex(BigInteger.valueOf(0));
        retVal.setMaxResults(BigInteger.valueOf(-1));

        ResponseOptionType resp = new ResponseOptionType();
        resp.setReturnComposedObjects(true);
        resp.setReturnType("LeafClass");
        retVal.setResponseOption(resp);

        AdhocQueryType queryType = new AdhocQueryType();
        queryType.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

        addSlot(queryType, INFOBUTTON_PATIENTID_SLOT,
            new String[]{request.getAssertionInfo().getPatientUnitNumber() + "^^^&" + request.getAssertionInfo().getHomeCommunityId() + "&ISO"});

        addSlot(queryType, INFOBUTTON_QUERY_SLOT,
            new String[]{request.getQuery()});

        retVal.setAdhocQuery(queryType);

        return retVal;
    }

    /**
     * Create gateway assertion object based on the passed parameters and some fixed values.
     *
     * @param msg
     * @return
     */
    private AssertionType createInfoButtonAssertion(gov.hhs.fha.nhinc.common.docmgr.AssertionInfoType assertionInfo)
        throws Exception {

        AssertionType assertion = new AssertionType();
        assertion.setDateOfBirth(formatXDSDate(assertionInfo.getPatientDOB().toGregorianCalendar().getTime(), ASSERTION_DOB_PRECISION));

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        //SAMLAuthStatementType is new for CONNECT v3.1
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        SamlAuthzDecisionStatementType samlAuthzDecisionStatement = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceType samlAuthzDecisionStatementEvidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType samlAuthzDecisionStatementAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceConditionsType samlAuthzDecisionStatementEvidenceConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_EXPIRATION_DATE_PROP));
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_SIGNATURE_DATE_PROP));
        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementAssertion.getAccessConsentPolicy().add(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_CLAIM_FORM_REF_PROP));
        samlAuthzDecisionStatementEvidence.setAssertion(samlAuthzDecisionStatementAssertion);
        samlAuthzDecisionStatement.setEvidence(samlAuthzDecisionStatementEvidence);
        assertion.setSamlAuthzDecisionStatement(samlAuthzDecisionStatement);
        assertion.setSamlAuthnStatement(samlAuthnStatement);

        PersonNameType pName = new PersonNameType();
        pName.setFamilyName(assertionInfo.getPatientLastName());
        pName.setGivenName(assertionInfo.getPatientFirstName());
        pName.setSecondNameOrInitials(assertionInfo.getPatientMiddleName());
        assertion.setPersonName(pName);

        HomeCommunityType hc = new HomeCommunityType();
        hc.setDescription(assertionInfo.getHomeCommunityDescription());
        hc.setHomeCommunityId(assertionInfo.getHomeCommunityId());
        hc.setName(assertionInfo.getHomeCommunityName());

        UserType muser = new UserType();
        CeType roleType = new CeType();
        roleType.setCode(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_CODE_PROP));
        roleType.setCodeSystem(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_CODE_SYSTEM_PROP));
        roleType.setCodeSystemName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_CODE_SYSTEM_NAME_PROP));
        roleType.setCodeSystemVersion(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_CODE_SYSTEM_VERSION_PROP));
        roleType.setDisplayName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_NAME_PROP));
        roleType.setOriginalText(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_ROLE_NAME_PROP));
        muser.setOrg(hc);
        muser.setRoleCoded(roleType);

        //VA DoD Requirement
        String uPlusDoD = assertionInfo.getUsername() + oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_USER_DOD_EXTENSION_PROP);
        muser.setUserName(uPlusDoD);
        PersonNameType uName = new PersonNameType();
        CeType nType = new CeType();
        nType.setCode(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_CODE_PROP));
        nType.setCodeSystem(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_CODE_SYSTEM_PROP));
        nType.setCodeSystemName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_CODE_SYSTEM_NAME_PROP));
        nType.setCodeSystemVersion(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_CODE_SYSTEM_VERSION_PROP));
        nType.setDisplayName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_NAME_PROP));
        nType.setOriginalText(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_DOD_ROLE_NAME_PROP));

        uName.setNameType(nType);
        uName.setFamilyName(assertionInfo.getProviderLastName());
        uName.setGivenName(assertionInfo.getProviderFirstName());
        uName.setSecondNameOrInitials(assertionInfo.getProviderMiddleName());
        muser.setPersonName(uName);
        assertion.setUserInfo(muser);

        assertion.setHomeCommunity(hc);
        CeType pouType = new CeType();
        pouType.setCode(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_CODE_PROP));
        pouType.setCodeSystem(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_PROP));
        pouType.setCodeSystemName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_NAME_PROP));
        pouType.setCodeSystemVersion(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_VERSION_PROP));
        pouType.setDisplayName(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_NAME_PROP));
        pouType.setOriginalText(oProp.getProperty(REPOSITORY_PROPERTY_FILE, IB_PURPOSE_OF_USE_ROLE_NAME_PROP));
        assertion.setPurposeOfDisclosureCoded(pouType);

        return assertion;
    }

    /**
     * 
     * @param request
     * @return
     */
    private NhinTargetCommunitiesType createTargetCommunities(List<String> targetNames)
        throws Exception {
        NhinTargetCommunitiesType targetCommunities = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory().createNhinTargetCommunitiesType();

        for (String target : targetNames) {
            String targetOID = getInfoButtonCommunityTargets().get(target);

            if (targetOID != null) {
                NhinTargetCommunityType targetType = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory().createNhinTargetCommunityType();
                HomeCommunityType homeType = new HomeCommunityType();
                homeType.setHomeCommunityId(targetOID);
                targetType.setHomeCommunity(homeType);
                targetCommunities.getNhinTargetCommunity().add(targetType);
            } else {
                throw new Exception("Infobutton target community not found: " + target);
            }
        }

        if (targetCommunities.getNhinTargetCommunity().size() == 0) {
            throw new Exception("Infobutton target community is undefined.");
        }

        return targetCommunities;
    }

    /**
     * Format XDS date using scaling precision (as according to XDS Spec).
     *
     * @param date
     * @param precision
     * @return
     */
    private String formatXDSDate(Date date, int precision) {
        DateFormat xdsFormat = new SimpleDateFormat(XDS_DATE_FORMAT_FULL.substring(0, precision));
        return xdsFormat.format(date);
    }

    /**
     * Return a hash of infobutton target communitites.
     *
     * @return
     */
    private Hashtable<String, String> getInfoButtonCommunityTargets() {
        Hashtable<String, String> targets = new Hashtable<String, String>();

        PropertyAccessor oProp = PropertyAccessor.getInstance();

        try {
            Long targetCount = oProp.getPropertyLong(REPOSITORY_PROPERTY_FILE, INFOBUTTON_COUNT_PROP);

            if (targetCount == null) {
                throw new PropertyAccessException("Property not found: " + INFOBUTTON_COUNT_PROP);
            }

            for (int i = 1; targets.size() < targetCount; i++) {
                String targetName =
                    oProp.getProperty(REPOSITORY_PROPERTY_FILE, INFOBUTTON_PREFIX + i + INFOBUTTON_NAME_SUFFIX);
                String targetOID =
                    oProp.getProperty(REPOSITORY_PROPERTY_FILE, INFOBUTTON_PREFIX + i + INFOBUTTON_OID_SUFFIX);

                targets.put(targetName, targetOID);
            }
        } catch (PropertyAccessException e) {
            log.error("Error accessing InfoButton Community Target properties.", e);
        }

        return targets;
    }
}
