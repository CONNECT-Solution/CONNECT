/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.document;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
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

    /** Property constants. */
    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";
    public static final String INBOUND_DOCUMENT_REPOSITORY_ID_PROP = "inboundDocumentRepositoryId";
    public static final String POLICY_REPOSITORY_ID_PROP = "policyRepositoryId";
    public static final String DOCUMENT_UNIQUE_OID_PROP = "documentUniqueOID";

    /** Registry and Repository web service. */
    public static final String DYNDOC_REGISTRY_ENDPOINT =
        "http://localhost:8080/DocumentRegistry/DocumentRegistry_Service";
    public static final String DYNDOC_REPOSITORY_ENDPOINT =
        "http://localhost:8080/DocumentRepository/DocumentRepository_Service";
    public static final String INBOUND_REGISTRY_ENDPOINT = DYNDOC_REGISTRY_ENDPOINT;
    public static final String INBOUND_REPOSITORY_ENDPOINT = DYNDOC_REPOSITORY_ENDPOINT;
    public static final String POLICY_REGISTRY_ENDPOINT = DYNDOC_REGISTRY_ENDPOINT;
    public static final String POLICY_REPOSITORY_ENDPOINT = DYNDOC_REPOSITORY_ENDPOINT;

    /** Value for archive field in metadata */
    public static final String XDS_ARCHIVE_SLOT = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";

    /** Repository id field for segmenting the data. */
    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";

    /** Error values */
    public static final String XDS_FAILED_STATUS
       = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_ERROR_CODE = "DOCUMENT_MANAGER_ERROR";
    public static final String XDS_ERROR_SEVERITY = "ERROR";

    /** Date format for XDS */
    public static final DateFormat XDS_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private static Log log = LogFactory.getLog(DocumentManagerImpl.class);

    ////////////////////////////////////////////////////////////////////////////
    //Interface implementation
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Query dynamic document archive.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryDynamicDocumentArchive(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Querying dynamic document archive.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doQuery(body, DYNDOC_REGISTRY_ENDPOINT, repositoryId);
    }

    /**
     * Retrieve dynamic document.
     * 
     * @param body
     * @return
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveDynamicDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Retrieving dynamic document.");
        return doRetrieve(body, DYNDOC_REPOSITORY_ENDPOINT);
    }

    /**
     * Store dynamic document.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreDynamicDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Storing dynamic document.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doStore(body, DYNDOC_REPOSITORY_ENDPOINT, repositoryId);
    }

    /**
     * The dynamic document is archived by first querying for the metadata, then querying for
     * the document itself.  With this information, we can re-store the document as a replacement
     * of the original (updating the hasBeenAccessed flag).  The hasBeenAccessed flag is our
     * archiving flag.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerArchiveDynamicDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Archiving dynamic document.");

        RegistryResponseType result = null;
        String repositoryId = null;
        String documentUniqueId = null;

        try {
            //Pull out parameters
            if (body.getDocumentRequest().isEmpty()) {
                throw new Exception("Empty request.");
            }

            DocumentRequest docRequest = body.getDocumentRequest().get(0);
            repositoryId = docRequest.getRepositoryUniqueId();
            documentUniqueId = docRequest.getDocumentUniqueId();

            if ((repositoryId == null) || (documentUniqueId == null)) {
                throw new Exception("Either repositoryId or documentUniqueId is missing.");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            result = new RegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new RegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not archive document.");
            error.setLocation("DocumentManagerImpl.archiveDynamicDocument");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);
            return result;
        }

        //Create metadata query
        AdhocQueryRequest metaRequest = createQuery(
            new String[] {
                "$XDSRepositoryUniqueId",
                "$XDSDocumentEntryUniqueId",
            },
            new String[] {
                repositoryId,
                documentUniqueId,
            }
        );

        //Perform query for metadata
        AdhocQueryResponse queryResponse = documentManagerQueryDynamicDocumentArchive(metaRequest);

        //Retrieve document
        RetrieveDocumentSetResponseType docResponse  = documentManagerRetrieveDynamicDocument(body);

        ProvideAndRegisterDocumentSetRequestType replaceRequest = null;
        try {
            //Create replacement
            replaceRequest = createReplaceRequest(queryResponse, docResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
            result = new RegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new RegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not archive document.");
            error.setLocation("DocumentManagerImpl.archiveDynamicDocument");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);
            return result;
        }

        //Do store with metdata for "accessed" set
        result =  documentManagerStoreDynamicDocument(replaceRequest);

        return result;
    }

    /**
     * Query inbound repository.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryInboundRepository(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Querying inbound repository.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + INBOUND_DOCUMENT_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doQuery(body, INBOUND_REGISTRY_ENDPOINT, repositoryId);
    }

    /**
     * Retrieve the inbound document.
     * 
     * @param body
     * @return
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveInboundDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Retrieving inbound document.");
        return doRetrieve(body, INBOUND_REPOSITORY_ENDPOINT);
    }

    /**
     * Store inbound document.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreInboundDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Storing inbound document.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + INBOUND_DOCUMENT_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doStore(body, INBOUND_REPOSITORY_ENDPOINT, repositoryId);
    }

    /**
     * Query policy repository.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryPolicyRepository(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Querying policy repository.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, POLICY_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + POLICY_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doQuery(body, POLICY_REGISTRY_ENDPOINT, repositoryId);
    }

    /**
     * Store policy.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStorePolicy(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Storing policy.");
        String repositoryId = null;

        try {
            repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, POLICY_REPOSITORY_ID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + POLICY_REPOSITORY_ID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        return doStore(body, POLICY_REPOSITORY_ENDPOINT, repositoryId);
    }

    /**
     * Retrieve policy.
     * 
     * @param body
     * @return
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrievePolicy(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Retrieving policy.");
        return doRetrieve(body, POLICY_REPOSITORY_ENDPOINT);
    }

    /**
     * Generate unique Id that can be used for document unique ids.
     * 
     * @param request
     * @return
     */
    public gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType generateUniqueId(gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdRequestType request) {
        gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType response  = new gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType();
        String oid = "1.1.1.1.1.1";

        try {
            oid = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DOCUMENT_UNIQUE_OID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + DOCUMENT_UNIQUE_OID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        //OID^extension format
        response.setUniqueId(oid + "^" + new Date().getTime());

        return response;
    }

    /**
     * Perform the actual query.
     * 
     * Before we do, we add a repository Id to the request.  This will tell the
     * repository to filter the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     * 
     * @param request
     * @param endpoint
     * @param repositoryId
     * @return
     */
    private AdhocQueryResponse doQuery(AdhocQueryRequest request, String endpoint, String repositoryId) {

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse result = null;

        try { // Call Web Service Operation
            DocumentRegistryService service = new DocumentRegistryService();
            DocumentRegistryPortType port = service.getDocumentRegistryPortSoap();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                   javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                   endpoint);

            //Insert repository id for query
            insertRepositoryIdQuery(request, repositoryId);

            result = port.documentRegistryRegistryStoredQuery(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Perform the actual store.  A unique Id is inserted if one hasn't already
     * been created.
     * 
     * Before we do, we add a repository Id to the request.  This will tell the
     * repository to store the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     * 
     * @param request
     * @param endpoint
     * @param repositoryId
     * @return
     */
    private RegistryResponseType doStore(
            ProvideAndRegisterDocumentSetRequestType request,
            String endpoint,
            String repositoryId) {

        oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType result = null;

        try { // Call Web Service Operation
            DocumentRepositoryService service = new DocumentRepositoryService();
            DocumentRepositoryPortType port = service.getDocumentRepositoryPortSoap();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                   javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                   endpoint);

            //Insert doc unique id if one does not exist
            if (!createDocumentUniqueId(request))
                throw new Exception("Failed to create document unique Id");

            //Insert repository id
            insertRepositoryId(request, repositoryId);

            result = port.documentRepositoryProvideAndRegisterDocumentSetB(request);
        } catch (Exception e) {
            e.printStackTrace();
            result = new RegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new RegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not store document.");
            error.setLocation("DocumentManagerImpl.doStore");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);
        }

        return result;
    }

    /**
     * Perform the actual retrieve.
     * 
     * Here we can just forward the request to the repository.  For a real XDS
     * server, we would need to call the appropriate repository for the document.
     * 
     * @param request
     * @param endpoint
     * @return
     */
    private RetrieveDocumentSetResponseType doRetrieve(
            RetrieveDocumentSetRequestType request,
            String endpoint) {

        RetrieveDocumentSetResponseType result = null;

        try { // Call Web Service Operation
            DocumentRepositoryService service = new DocumentRepositoryService();
            DocumentRepositoryPortType port = service.getDocumentRepositoryPortSoap();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                   javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                   endpoint);

            result = port.documentRepositoryRetrieveDocumentSet(request);
        } catch (Exception e) {
            e.printStackTrace();
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

    private AdhocQueryRequest createQuery(String [] names, String [] values) {
        if ((names == null) || (values == null) || (names.length != values.length))
            return null;

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

    private ProvideAndRegisterDocumentSetRequestType createReplaceRequest(
            AdhocQueryResponse queryResponse,
            RetrieveDocumentSetResponseType docResponse)
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
        SlotType1 archiveSlot = findSlot(extrinsic, XDS_ARCHIVE_SLOT);
        //Create if it doesn't exist
        if (archiveSlot == null) {
            archiveSlot = new SlotType1();
            archiveSlot.setName(XDS_ARCHIVE_SLOT);
            extrinsic.getSlot().add(archiveSlot);
        }
        ValueListType valList = new ValueListType();
        valList.getValue().add("true");
        archiveSlot.setValueList(valList);

        /**The Submission set is actually ignored by the reference implemenation.*/
        //Create submission set
        RegistryPackageType registryPackage = new RegistryPackageType();
        registryPackage.setId("SubmissionSet01");
        registryPackage.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        //Submission time
        Date now = new Date();
        addSlot(registryPackage, "submissionTime", new String [] { XDS_DATE_FORMAT.format(now) });

        //Add submission author classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d", //scheme
            "", //node representation
            "id_20", //id
			null, //name
            new String [] {
                "authorPerson",
				"authorInstitution",
				"authorRole",
				"authorSpecialty",
            }, //slot names
            new String [][] {
                new String [] { "^DocumentManager^Automated^^^" },
                new String [] {
                    "LocalMHS",
                },
                new String [] { "Automated" },
                new String [] { "Automated" },
            } //slot values
        );

        //Add submission content type classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", //scheme
			"contentTypeCode", //node representation
            "id_21", //id
			"contentTypeDisplayName", //name
            new String [] {
                "codingScheme",
            }, //slot names
            new String [][] {
                new String [] { "Connect-a-thon contentTypeCodes" },
            } //slot values
        );

        //Add submission uniqueId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
            "id_22", //id
            "XDSSubmissionSet.uniqueId", //name
            new java.rmi.server.UID().toString()
        );

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
        addSlot(association, "SubmissionSetStatus", new String [] { "Original" });

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
     * Add slot to submission object.
     *
     * @param registry - submission object
     * @param name - slot name
     * @param values - slot values
     */
    private static void addSlot(RegistryObjectType registry,
            String name, String [] values) {

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
            String [] slotNames,
            String [][] slotValues) {

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
                    if (XDS_REPOSITORY_ID.equals(slot.getName())) {
                        repositoryIdSlot = slot;
                        break;
                    }
                }
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(extrinsic, XDS_REPOSITORY_ID, new String[] { repositoryId });
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
            addSlot(request.getAdhocQuery(), XDS_REPOSITORY_ID_QUERY, new String[] { repositoryId });
            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
    }
}
