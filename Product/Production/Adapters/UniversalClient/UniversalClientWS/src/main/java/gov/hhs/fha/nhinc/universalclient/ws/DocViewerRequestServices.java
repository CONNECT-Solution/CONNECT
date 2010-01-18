/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.universalclient.ws;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Query;
import gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdRequestType;
import gov.hhs.fha.nhinc.common.docmgr.GenerateUniqueIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQuery;
import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.netbeans.xml.schema.docviewer.RetrievedDocumentDisplayObject;

/**
 *
 * @author Duane DeCouteau
 */
@WebService(serviceName = "DocViewerRequestServicesService", portName = "DocViewerRequestServicesPort", endpointInterface = "gov.hhs.fha.nhinc.universalclient.ws.DocViewerRequestServicesPortType", targetNamespace = "http://ws.universalclient.nhinc.fha.hhs.gov/", wsdlLocation = "WEB-INF/wsdl/DocViewerRequestServices/DocViewerRequestServicesService.wsdl")
public class DocViewerRequestServices implements DocViewerRequestServicesPortType {
    private AdhocQueryResponse nhinResults = null;
    private AdhocQueryResponse localResults = null;
    private static Log log = LogFactory.getLog(DocViewerRequestServices.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    private String SERVICE_NAME_ENTITY_DOC_QUERY_SERVICE = "entitydocumentquery";
    private String SERVICE_NAME_ENTITY_DOC_RETRIEVE_SERVICE = "entitydocumentretrieve";
    private String SERVICE_NAME_DOCUMENT_MANAGER_SERVICE = "documentmanager";
    private String patientId;
    private String homeCommunityId;
    private AdhocQueryRequest origAdhocQuery;
    private AssertionType origAssertion;
    private String requestingUser;
    private static final String HL7_DATE_FORMAT = "yyyyMMddHHmmssZ";
    private static final String REGULAR_DATE_FORMAT = "MM/dd/yyyy";
    private static final String CREATION_TIME_TO_SLOT_NAME = "$XDSDocumentEntryCreationTimeTo";
    private static final String PATIENT_EXTERNAL_IDENTITY_SCHEME = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";


    //DB4Objects to handle management of display object based on requesting user and patient
    private static ObjectServer db4server = null;
    private static ObjectContainer db = null;
    private static String dbFileName = "UniversalClientWSdbo";
    private static String dbFilePath = "./";  //put objectmanagement in config directory of glassfish

    private void getDB4Server() {
        try {
            if (db4server == null) {
                boolean dbexists = false;
                String dbFile = dbFilePath + dbFileName;
                try {
                    File dbtest = new File(dbFile);
                    if (dbtest.exists()) {
                        dbexists = true;
                    }
                }
                catch (Exception fx) {
                    fx.printStackTrace();
                }
                try {
                    Configuration conf = Db4o.newConfiguration();
                    conf.automaticShutDown(true);
                    conf.disableCommitRecovery();
                    conf.lockDatabaseFile(false);
                    db4server = Db4o.openServer(conf, dbFile, 0);
                }
                catch (Exception ex) {
                    System.out.println("DB4object is open");
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public org.netbeans.xml.schema.docviewer.DocViewerResponseType requestAllNHINDocuments(org.netbeans.xml.schema.docviewer.DocViewerRequestType docRequest) {
        getDB4Server();
        addInitialStatusRecord(docRequest);
        this.patientId = docRequest.getPatientId();
        this.homeCommunityId = docRequest.getHomeCommunityId();
        this.origAdhocQuery = docRequest.getAdhocQueryRequest();
        this.origAssertion = docRequest.getAssertion();
        this.requestingUser = docRequest.getUserId();
        org.netbeans.xml.schema.docviewer.DocViewerResponseType res = new org.netbeans.xml.schema.docviewer.DocViewerResponseType();
        log.debug("Entering DocViewRequestServices.findDocuments");
        List<WebServiceFeature> wsfeatures = new ArrayList<WebServiceFeature>();
        wsfeatures.add(new MTOMFeature(0));
        WebServiceFeature[] wsfeaturearray = wsfeatures.toArray(new WebServiceFeature[0]);
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the entity doc query service
            //------------------------------------------
            EntityDocQuery service = new EntityDocQuery();
            EntityDocQueryPortType port =  service.getEntityDocQueryPortSoap11(wsfeaturearray);

            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_ENTITY_DOC_QUERY_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_ENTITY_DOC_QUERY_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 9080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9080/NhinConnect/EntityDocQuery");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_ENTITY_DOC_QUERY_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:9080/NhinConnect/EntityDocQuery'");
            }

            RespondingGatewayCrossGatewayQueryRequestType gateway = new RespondingGatewayCrossGatewayQueryRequestType();
            gateway.setAdhocQueryRequest(origAdhocQuery);
            gateway.setAssertion(origAssertion);

            nhinResults = port.respondingGatewayCrossGatewayQuery(gateway);
            if (nhinResults.getTotalResultCount() != null) {
                log.debug("DocViewerRequestService NHIN RESULT COUNT "+nhinResults.getTotalResultCount().toString());
            }
            addNewSearchToDisplayList();
            res.setStatusCode(0);
            res.setStatusDesc("Request Successfully Processed");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("DocViewerRequestServices.findDocuments "+ex.getMessage());
            res.setStatusCode(1);
            res.setStatusDesc("DocViewerRequestAll failed "+ex.getMessage());
        }
        
        log.debug("Exiting DocViewerRequestService.findDocuments");
        localResults = queryLocalRespository(origAdhocQuery);
        log.debug("DocViewerRequestServices : queryLocalRepository status"+localResults.getStatus());
        if (localResults != null) {
            String localStatus = localResults.getStatus();
            if (!"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure".equals(localStatus)) {
                compareAndStore();
            }
            else {
                storeAll();
            }
        }
        else {
            storeAll();
        }
        
        log.debug("Exiting DocViewerRequestService.requestAllNHINDocuments");
        return res;
    }

    public org.netbeans.xml.schema.docviewer.DocViewerStatusResponseType getNHINRequestStatus(org.netbeans.xml.schema.docviewer.DocViewerStatusRequestType statusRequest) {
        org.netbeans.xml.schema.docviewer.DocViewerStatusResponseType res = new org.netbeans.xml.schema.docviewer.DocViewerStatusResponseType();
        List<org.netbeans.xml.schema.docviewer.RetrievedDocumentDisplayObject> objs = new ArrayList();
        getDB4Server();
        ObjectContainer client = null;
        System.out.println("DocViewerRequestService.getNHINRequestStatus PatientId "+statusRequest.getPatientId()+" Requesting User "+statusRequest.getUserId());
        try {
            client = db4server.openClient();
            Query query = client.query();
            query.constrain(RetrievedDocumentDisplayObject.class);
            query.descend("patientId").constrain(statusRequest.getPatientId());
            ObjectSet set = query.execute();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)iter.next();
                if (obj.getRequestingUser().equals(statusRequest.getUserId())) {
                    objs.add(obj);
                }
            }
            client.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.close();
        }
        res.getDisplayObjects().addAll(objs);
        return res;
    }
    private void addNewSearchToDisplayList() {
        //remove old for requesting user
        log.debug("Entering DocViewerRequestServices.addNewSearchToDisplayList");

        try {
             List<JAXBElement<? extends IdentifiableType>> objectList = nhinResults.getRegistryObjectList().getIdentifiable();
             List<RetrievedDocumentDisplayObject> docArray = new ArrayList();

            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();


                RetrievedDocumentDisplayObject docInfo = new RetrievedDocumentDisplayObject();

                if (identifiableType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType)identifiableType;

                    if (extrinsicObject != null) {
                        try { docInfo.setDocumentTitle(extractDocumentTitle(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setDocumentType(extractDocumentType(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        String creationTime = "";
                        try { creationTime = extractCreationTime(extrinsicObject); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setCreationDate(formatDate(creationTime, HL7_DATE_FORMAT, REGULAR_DATE_FORMAT)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setOrganizationName(extractInstitution(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setUniqueDocumentId(extractDocumentID(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setRepositoryId(extractRespositoryUniqueID(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setOrgId(extrinsicObject.getHome()); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setOrigDocumentId(extractDocumentID(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setOrigRespositoryId(extractRespositoryUniqueID(extrinsicObject)); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        try { docInfo.setOrigHomeCommunityId(extrinsicObject.getHome()); } catch (Exception ex) { System.out.println("DocViewerRequestService ERROR "+ex.getMessage()); }
                        docInfo.setSelected(false);
                        docInfo.setAvailableInLocalStore(false);  //change this back to false
                        docInfo.setDocumentStatus("Pending"); //change this back to pending
                        docInfo.setPatientId(patientId);
                        docInfo.setRequestingUser(requestingUser);
                        docArray.add(docInfo);
                    }
                }
                else {
                    System.out.println("DocViewerRequestService Failed to Add Status NULL");
                }
            }
            deleteRetrievedDocumentDisplayObject(patientId, requestingUser);
            addRetrievedDocumentDisplayObject(docArray);
        }
        catch (Exception ex) {
            log.debug("DocViewerRequestServices.addNewSearchToDisplayList "+ex.getMessage());
        }
        log.debug("Exiting DocViewerRequestServices.addNewSearchToDisplayList");
    }

    private AdhocQueryResponse queryLocalRespository(AdhocQueryRequest request) {
        log.debug("Entering DocRequestRequestService.queryLocalRespository");
        AdhocQueryResponse res = null;
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the document manager service
            //------------------------------------------
            ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_DOCUMENT_MANAGER_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 8080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/DocumentManager_Service/DocumentManagerService");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:8080/DocumentManager_Service/DocumentManagerService'");
            }

            res = port.documentManagerQueryInboundRepository(request);
        }
        catch (Exception ex) {
            log.error("DocViewerReqeustService.queryLocalRespository "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocRequestViewerService.queryLocalRepository");
        return res;
    }

    private RetrieveDocumentSetResponseType getNHINDocumentSet(RetrieveDocumentSetRequestType request) {
        log.debug("Entering DocViewerRequestServices");
        List<WebServiceFeature> wsfeatures = new ArrayList<WebServiceFeature>();
        wsfeatures.add(new MTOMFeature(0));
        WebServiceFeature[] wsfeaturearray = wsfeatures.toArray(new WebServiceFeature[0]);
        RetrieveDocumentSetResponseType response = null;
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the entity doc query service
            //------------------------------------------
            EntityDocRetrieve service = new EntityDocRetrieve();
            EntityDocRetrievePortType port = service.getEntityDocRetrievePortSoap11(wsfeaturearray);
            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_ENTITY_DOC_RETRIEVE_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_ENTITY_DOC_RETRIEVE_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 9080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:9080/NhinConnect/EntityDocRetrieve");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_ENTITY_DOC_RETRIEVE_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:9080/NhinConnect/EntityDocRetrieve'");
            }

            RespondingGatewayCrossGatewayRetrieveRequestType gateway = new RespondingGatewayCrossGatewayRetrieveRequestType();
            gateway.setAssertion(origAssertion);
            gateway.setRetrieveDocumentSetRequest(request);


            response = port.respondingGatewayCrossGatewayRetrieve(gateway);

        }
        catch (Exception ex) {
            log.error("DocViewerRequestServices.retrieveNHINDocument "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocViewerRequestServices.retrieveNHINDocument ");
        return response;
    }

    private RegistryResponseType storeInboundDocumentToRespository(RetrieveDocumentSetResponseType document,
                                                                   ExtrinsicObjectType extrinsicType, ClassificationType classType,
                                                                   AssociationType1 assocType, RegistryPackageType regPackageType) {
        RegistryResponseType response = null;
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the document manager service
            //------------------------------------------
            ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_DOCUMENT_MANAGER_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 8080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/DocumentManager_Service/DocumentManagerService");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:8080/DocumentManager_Service/DocumentManagerService'");
            }

            ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType req = new ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType();

            // original document info
            if (document == null) {
                log.debug("DocViewerRequestService.storeInbound DocumentSet is Null");
                return null;
            }

            byte[] rawxml = document.getDocumentResponse().get(0).getDocument();
            String origDocId = document.getDocumentResponse().get(0).getDocumentUniqueId();
            String origRepositoryId = document.getDocumentResponse().get(0).getRepositoryUniqueId();
            String origHomeCommunity = document.getDocumentResponse().get(0).getHomeCommunityId();
            String origMIMEType = document.getDocumentResponse().get(0).getMimeType();

            System.out.println("DocViewerRequestService Orig Doc Id "+origDocId);
            System.out.println("DocViewerRequestService Orig Repository Id "+origRepositoryId);
            System.out.println("DocViewerRequestService Orig Home Community "+origHomeCommunity);
            System.out.println("DocViewerRequestService Orig MIME Type "+origMIMEType);

            GenerateUniqueIdResponseType uId = getUniqueDocumentId();
            String newDocId = uId.getUniqueId();
            System.out.println("DocViewerRequestService NEW DOCUMENT ID "+newDocId);

            document.getDocumentResponse().get(0).setDocumentUniqueId(newDocId);
            document.getDocumentResponse().get(0).setHomeCommunityId(homeCommunityId);
            document.getDocumentResponse().get(0).setRepositoryUniqueId(newDocId);
            document.getDocumentResponse().get(0).setMimeType(origMIMEType);

            //set new values
            SlotType1 slot1 = new SlotType1();
            slot1.setName("urn:gov:hhs:fha:nhinc:xds:OrigDocumentUniqueId");
            ValueListType valList = new ValueListType();
            valList.getValue().add(origDocId);
            slot1.setValueList(valList);

            SlotType1 slot2 = new SlotType1();
            slot2.setName("urn:gov:hhs:fha:nhinc:xds:OrigHomeCommunityId");
            valList = new ValueListType();
            valList.getValue().add(origHomeCommunity);
            slot2.setValueList(valList);

            SlotType1 slot3 = new SlotType1();
            slot3.setName("urn:gov:hhs:fha:nhinc:xds:OrigRepositoryUniqueId");
            valList = new ValueListType();
            valList.getValue().add(origRepositoryId);
            slot3.setValueList(valList);

            SlotType1 slot4 = new SlotType1();
            slot4.setName("urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed");
            valList = new ValueListType();
            valList.getValue().add("false");
            slot4.setValueList(valList);

            extrinsicType.getSlot().add(slot1);
            extrinsicType.getSlot().add(slot2);
            extrinsicType.getSlot().add(slot3);
            extrinsicType.getSlot().add(slot4);
            //extrinsic.getSlot().add(slot5);

            //update xdspatientid
            try {
                StringBuffer sb2 = new StringBuffer();
                sb2.append(patientId);
                sb2.append("^^^&");
                sb2.append(homeCommunityId);
                sb2.append("&ISO");

                for (ExternalIdentifierType identifierItem : extrinsicType.getExternalIdentifier()) {
                    if (identifierItem != null && identifierItem.getIdentificationScheme().contentEquals(PATIENT_EXTERNAL_IDENTITY_SCHEME)) {
                        System.out.println("DocViewerRequestService ExternalIdentifier "+identifierItem.getName().getLocalizedString().get(0).getValue());
                        if (identifierItem.getName().getLocalizedString().get(0).getValue().equals("XDSDocumentEntry.patientId")) {
                            System.out.println("DocViewerRequestService ExternalIdentifier Found");
                            identifierItem.setValue(sb2.toString());
                            break;
                        }
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

            //need to update patient source value
            SlotType1 patientslot = new SlotType1();
            patientslot.setName("sourcePatientId");
            ValueListType patientvalList = new ValueListType();
            StringBuffer sb = new StringBuffer();
            sb.append(patientId);
            sb.append("^^^&");
            sb.append(homeCommunityId);
            sb.append("&ISO");
            patientvalList.getValue().add(sb.toString());
            patientslot.setValueList(patientvalList);

            //remove existing SourcePatientId before replacing it
            for (SlotType1 mSlot : extrinsicType.getSlot()) {
                if (mSlot != null && mSlot.getName().contentEquals("sourcePatientId")) {
                    extrinsicType.getSlot().remove(mSlot);
                    break;
                }                
            }
            extrinsicType.getSlot().add(patientslot);


            extrinsicType.setId(newDocId);
            assocType.setId(newDocId);
            regPackageType.setId(newDocId);
            classType.setId(newDocId);

            ObjectFactory rimObjectFactory = new ObjectFactory();
            JAXBElement<ExtrinsicObjectType> extrinsicMetadata = rimObjectFactory.createExtrinsicObject(extrinsicType);
            JAXBElement<RegistryPackageType> submission = rimObjectFactory.createRegistryPackage(regPackageType);
            JAXBElement<AssociationType1> associationObject = rimObjectFactory.createAssociation(assocType);
            JAXBElement<ClassificationType> classificationObject = rimObjectFactory.createClassification(classType);
            RegistryObjectListType registryList = new RegistryObjectListType();
            registryList.getIdentifiable().add(extrinsicMetadata);
            registryList.getIdentifiable().add(submission);
            registryList.getIdentifiable().add(associationObject);
            registryList.getIdentifiable().add(classificationObject);


            ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document regdocument = new ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document();
            regdocument.setId(newDocId);
            regdocument.setValue(rawxml);


            //Add request to body for submission
            SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
            submitObjects.setRegistryObjectList(registryList);
            req.setSubmitObjectsRequest(submitObjects);
            req.getDocument().add(regdocument);


            response = port.documentManagerStoreInboundDocument(req);
            if (response.getRegistryErrorList() != null) {
                List<RegistryError> errorList = response.getRegistryErrorList().getRegistryError();
                for (RegistryError err : errorList) {
                    System.out.println("DocViewerRequestService Error "+err.getErrorCode()+" codeContext "+err.getCodeContext()+" Severity "+err.getSeverity()+" location "+err.getLocation()+" value "+err.getValue());
                }
            }
            System.out.println("DocViewerRequestService Result = "+response.getStatus());
        }
        catch (Exception ex) {
            log.error("DocViewerRequestService storeInboundRespository "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocViewerRequestService storeInboundRespository");
        return response;

    }

    private void compareAndStore() {
        log.debug("Entering DocViewerRequestService compareAndStore");
        try {
             List<JAXBElement<? extends IdentifiableType>> objectList = nhinResults.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();

                RetrieveDocumentSetRequestType reqType = new RetrieveDocumentSetRequestType();
                DocumentRequest docReq = new DocumentRequest();
                String homeId = "";
                String respId = "";
                String docId = "";

                //because standard is dumb
                ClassificationType classType = new ClassificationType();
                ExtrinsicObjectType extrinsicType = new ExtrinsicObjectType();
                AssociationType1 assocType = new AssociationType1();
                RegistryPackageType regPackageType = new RegistryPackageType();



                if (identifiableType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType slottype = (ExtrinsicObjectType)identifiableType;
                    docReq.setHomeCommunityId(slottype.getHome());
                    docReq.setDocumentUniqueId(extractDocumentID(slottype));
                    docReq.setRepositoryUniqueId(extractRespositoryUniqueID(slottype));
                    extrinsicType = slottype;
                }
                if (identifiableType instanceof ClassificationType) {
                    classType = (ClassificationType)identifiableType;
                }
                if (identifiableType instanceof AssociationType1) {
                    assocType = (AssociationType1)identifiableType;
                }
                if (identifiableType instanceof RegistryPackageType) {
                    regPackageType = (RegistryPackageType)identifiableType;
                }

                if (!existsInRepository(docReq.getHomeCommunityId(), docReq.getRepositoryUniqueId(), docReq.getDocumentUniqueId())) {
                    reqType.getDocumentRequest().add(docReq);
                    updateRetrievedDocumentDisplayObjectState(docReq, "Downloading");
                    RetrieveDocumentSetResponseType respType = getNHINDocumentSet(reqType);
                    RegistryResponseType regResponse = storeInboundDocumentToRespository(respType, extrinsicType, classType, assocType, regPackageType);
                }
            }
            refreshLocalRespositoryQuery();
        }
        catch (Exception ex) {
            log.error("DocViewerRequestServices compareAndStore "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocViewerRequestService compareAndStore");
    }

    private void storeAll() {
        log.debug("Entering DocViewerRequestService storeAll");
        try {

             List<JAXBElement<? extends IdentifiableType>> objectList = nhinResults.getRegistryObjectList().getIdentifiable();

            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();

                RetrieveDocumentSetRequestType reqType = new RetrieveDocumentSetRequestType();
                DocumentRequest docReq = new DocumentRequest();
                ClassificationType classType = new ClassificationType();
                ExtrinsicObjectType extrinsicType = new ExtrinsicObjectType();
                AssociationType1 assocType = new AssociationType1();
                RegistryPackageType regPackageType = new RegistryPackageType();

                if (identifiableType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType slottype = (ExtrinsicObjectType)identifiableType;
                    docReq.setHomeCommunityId(slottype.getHome());
                    docReq.setDocumentUniqueId(extractDocumentID(slottype));
                    docReq.setRepositoryUniqueId(extractRespositoryUniqueID(slottype));
                    extrinsicType = slottype;
                }
                if (identifiableType instanceof ClassificationType) {
                    classType = (ClassificationType)identifiableType;
                }
                if (identifiableType instanceof AssociationType1) {
                    assocType = (AssociationType1)identifiableType;
                }
                if (identifiableType instanceof RegistryPackageType) {
                    regPackageType = (RegistryPackageType)identifiableType;
                }

                reqType.getDocumentRequest().add(docReq);
                updateRetrievedDocumentDisplayObjectState(docReq, "Downloading");
                RetrieveDocumentSetResponseType respType = getNHINDocumentSet(reqType);
                RegistryResponseType regResponse = storeInboundDocumentToRespository(respType, extrinsicType, classType, assocType, regPackageType);
            }
            refreshLocalRespositoryQuery();
        }
        catch (Exception ex) {
            log.error("DocViewerRequestService StoreAll "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocViewerRequestServices storeAll");
    }

    private boolean existsInRepository(String homeId, String respositoryId, String docId) {
        boolean res = false;
        try {
             List<JAXBElement<? extends IdentifiableType>> objectList = localResults.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();

                RetrieveDocumentSetRequestType reqType = new RetrieveDocumentSetRequestType();
                DocumentRequest docReq = new DocumentRequest();
                String home = "";
                String doc = "";
                String resp = "";

                if (identifiableType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType slottype = (ExtrinsicObjectType)identifiableType;
                    for (SlotType1 returnSlot : slottype.getSlot()) {
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigDocumentUniqueId".equals(returnSlot.getName())) {
                            doc = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigHomeCommunityId".equals(returnSlot.getName())) {
                            home = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigRepositoryUniqueId".equals(returnSlot.getName())) {
                            resp = returnSlot.getValueList().getValue().get(0);
                        }

                    }
                }
                System.out.println("DocViewerRequestService COMPARE HC "+home+" "+homeId);
                System.out.println("DocViewerRequestService COMPARE DOCID "+doc+" "+docId);
                System.out.println("DocViewerRequestService COMPARE REPID "+resp+" "+respositoryId);
                if (home.equals(homeId) && resp.equals(respositoryId) && doc.equals(docId)) {
                    //document exists
                    res = true;
                    break;
                }
            }
        }
        catch (Exception ex) {
            log.error("DocViewerRequestServices.existInRespository "+ex.getMessage());
            ex.printStackTrace();
        }
        return res;
    }

    private String extractRespositoryUniqueID(ExtrinsicObjectType extrinsicObject)
    {
        return extractSingleSlotValue(extrinsicObject, "repositoryUniqueId");
    }

    private String extractDocumentType(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a");

        String documentTypeCode = classification.getName().getLocalizedString().get(0).getValue();
        return documentTypeCode;
    }

    private String extractDocumentTitle(ExtrinsicObjectType extrinsicObject) {

        String documentTitle = null;

        if (extrinsicObject != null &&
                extrinsicObject.getName() != null) {
            List<LocalizedStringType> localizedString = extrinsicObject.getName().getLocalizedString();

            if (localizedString != null && localizedString.size() > 0) {
                documentTitle = localizedString.get(0).getValue();
            }
        }

        return documentTitle;
    }

    private String extractCreationTime(ExtrinsicObjectType extrinsicObject) {
        return extractSingleSlotValue(extrinsicObject, "creationTime");
    }

    private String extractDocumentID(ExtrinsicObjectType extrinsicObject) {
        String documentID = null;

        ExternalIdentifierType identifier = extractIdentifierType(extrinsicObject, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");

        if (identifier != null) {
            documentID = identifier.getValue();
        }

        return documentID;
    }

    private String extractInstitution(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classification = extractClassification(extrinsicObject, "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");

        String institution = null;

        if (classification != null && classification.getSlot() != null && !classification.getSlot().isEmpty()) {
            for (SlotType1 slot : classification.getSlot()) {
                if (slot != null && slot.getName().contentEquals("authorInstitution")) {
                    if (slot.getValueList() != null && slot.getValueList().getValue() != null && !slot.getValueList().getValue().isEmpty()) {
                        institution = slot.getValueList().getValue().get(0);
                        break;
                    }
                }
            }
        }

        return institution;
    }

    private String extractSingleSlotValue(ExtrinsicObjectType extrinsicObject, String slotName) {
        String slotValue = null;
        for (SlotType1 slot : extrinsicObject.getSlot()) {
            if (slot != null && slot.getName().contentEquals(slotName)) {
                if (slot.getValueList().getValue().size() > 0) {
                    slotValue = slot.getValueList().getValue().get(0);
                    break;
                }
            }
        }
        return slotValue;
    }

    private ExternalIdentifierType extractIdentifierType(ExtrinsicObjectType extrinsicObject, String identificationScheme) {
        ExternalIdentifierType identifier = null;

        for (ExternalIdentifierType identifierItem : extrinsicObject.getExternalIdentifier()) {
            if (identifierItem != null && identifierItem.getIdentificationScheme().contentEquals(identificationScheme)) {
                identifier = identifierItem;
                break;
            }
        }

        return identifier;
    }
    private ClassificationType extractClassification(ExtrinsicObjectType extrinsicObject, String classificationScheme) {
        ClassificationType classification = null;

        for (ClassificationType classificationItem : extrinsicObject.getClassification()) {
            if (classificationItem != null && classificationItem.getClassificationScheme().contentEquals(classificationScheme)) {
                classification = classificationItem;
                break;
            }
        }

        return classification;
    }

    private String formatDate(String dateString, String inputFormat, String outputFormat) {
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
        Date date = null;

        try {
            date = inputFormatter.parse(dateString);
        } catch (ParseException ex) {
            log.error(ex);
        }

        return outputFormatter.format(date);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    private void addInitialStatusRecord(org.netbeans.xml.schema.docviewer.DocViewerRequestType addRequest) {
        log.debug("Entering DocViewerRequestService.addInitalStatus");
        try {
            deleteRetrievedDocumentDisplayObject(addRequest.getPatientId(), addRequest.getUserId());

            RetrievedDocumentDisplayObject docInfo = new RetrievedDocumentDisplayObject();
            docInfo.setDocumentTitle("Summary of Episode");
            docInfo.setDocumentType("C32");
            docInfo.setCreationDate(formatDate(new Date(), REGULAR_DATE_FORMAT));
            docInfo.setOrganizationName("Searching All Organizations");
            docInfo.setUniqueDocumentId("999999");
            docInfo.setRepositoryId("999999");
            docInfo.setOrgId(addRequest.getHomeCommunityId());
            docInfo.setSelected(false);
            docInfo.setAvailableInLocalStore(false);
            docInfo.setDocumentStatus("Pending");
            docInfo.setPatientId(addRequest.getPatientId());
            docInfo.setRequestingUser(addRequest.getUserId());
            docInfo.setOrigDocumentId("999999");
            docInfo.setOrigHomeCommunityId(addRequest.getHomeCommunityId());
            docInfo.setOrigRespositoryId("999999");
            List<RetrievedDocumentDisplayObject> docArray = new ArrayList();
            docArray.add(docInfo);
            addRetrievedDocumentDisplayObject(docArray);
        }
        catch (Exception ex) {
            log.error("DocViewerRequestService.addInitialStatusRecord "+ex.getMessage());
        }
        log.debug("Exiting DocViewerRequestService.addInitialStatus");
    }

    private void addRetrievedDocumentDisplayObject(List<RetrievedDocumentDisplayObject> objL) {
        getDB4Server();
        ObjectContainer client = null;
        try {
            client = db4server.openClient();
            Iterator iter = objL.iterator();
            while (iter.hasNext()) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)iter.next();
                client.store(obj);
            }
            client.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.close();
        }
    }

    private void deleteRetrievedDocumentDisplayObject(String pId, String uId) {
        getDB4Server();
        ObjectContainer client = null;
        try {
            client = db4server.openClient();
            Query query = client.query();
            query.constrain(RetrievedDocumentDisplayObject.class);
            query.descend("patientId").constrain(patientId);
            ObjectSet set = query.execute();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)iter.next();
                if (requestingUser.equals(obj.getRequestingUser())) {
                    client.delete(obj);
                }
            }
            client.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.close();
        }
    }

    private void updateRetrievedDocumentDisplayObjectState(DocumentRequest d, String stateupdate) {
        getDB4Server();
        ObjectContainer client = null;
        String docId = d.getDocumentUniqueId();
        String repId = d.getRepositoryUniqueId();
        String homeId = d.getHomeCommunityId();
        try {
            client = db4server.openClient();
            Query query = client.query();
            query.constrain(RetrievedDocumentDisplayObject.class);
            query.descend("patientId").constrain(patientId);
            ObjectSet set = query.execute();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)iter.next();
                String rUser = obj.getRequestingUser();
                String origDocId = obj.getOrigDocumentId();
                String origRepId = obj.getOrigRespositoryId();
                String origHC = obj.getOrigHomeCommunityId();
                if (rUser.equals(requestingUser) && origDocId.equals(docId) && origRepId.equals(repId) && origHC.equals(homeId)) {
                    obj.setDocumentStatus(stateupdate);
                    client.store(obj);
                }
            }
            client.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.close();
        }
    }

    private void refreshLocalRespositoryQuery() {
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the document manager service
            //------------------------------------------
            ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_DOCUMENT_MANAGER_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 8080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/DocumentManager_Service/DocumentManagerService");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:8080/DocumentManager_Service/DocumentManagerService'");
            }

            AdhocQueryResponse res = port.documentManagerQueryInboundRepository(origAdhocQuery);  //patientid and community should be same so just use the original query

             List<JAXBElement<? extends IdentifiableType>> objectList = res.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();

                String docId = "";
                String repId = "";
                String homeId = "";

                String origHome = "";
                String origDoc = "";
                String origRep = "";


                if (identifiableType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType slottype = (ExtrinsicObjectType)identifiableType;
                    docId = extractDocumentID(slottype);
                    repId = extractRespositoryUniqueID(slottype);
                    homeId = slottype.getHome();
                    for (SlotType1 returnSlot : slottype.getSlot()) {
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigDocumentUniqueId".equals(returnSlot.getName())) {
                            origDoc = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigHomeCommunityId".equals(returnSlot.getName())) {
                            origHome = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigRepositoryUniqueId".equals(returnSlot.getName())) {
                            origRep = returnSlot.getValueList().getValue().get(0);
                        }
                    }
                }
                //update retrieved docs status to available
                updateRetrievedDocumentDisplayObjectToAvailable(docId, repId, homeId, origDoc, origRep, origHome);
            }
        }
        catch (Exception ex) {
            log.error("DocViewerReqeustService.refreshQueryLocalRespository "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateRetrievedDocumentDisplayObjectToAvailable(String docId, String repId, String homeId, String oDocId, String oRepId, String oHomeId) {
        log.debug("Entering DocViewerRequestService updateRetrievedDocumentDisplayObject");
        getDB4Server();
        ObjectContainer client = null;
        try {
            client = db4server.openClient();
            Query query = client.query();
            query.constrain(RetrievedDocumentDisplayObject.class);
            query.descend("patientId").constrain(patientId);
            ObjectSet set = query.execute();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                RetrievedDocumentDisplayObject obj = (RetrievedDocumentDisplayObject)iter.next();
                String rUser = obj.getRequestingUser();
                String origDocId = obj.getOrigDocumentId();
                String origRepId = obj.getOrigRespositoryId();
                String origHC = obj.getOrigHomeCommunityId();
                if (rUser.equals(requestingUser) && origDocId.equals(oDocId) && origRepId.equals(oRepId) && origHC.equals(oHomeId)) {
                    obj.setUniqueDocumentId(docId);
                    obj.setRepositoryId(repId);
                    obj.setOrgId(homeId);
                    obj.setDocumentStatus("Available");
                    obj.setAvailableInLocalStore(true);
                    client.store(obj);
                }
            }
            client.commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.close();
        }
        log.debug("Exiting DocViewerRequestService updateRetrievedDocumentDisplayObject");
    }

    private GenerateUniqueIdResponseType getUniqueDocumentId() {
        log.debug("Entering DocViewerRequestService.getUniqueDocumentId");
        GenerateUniqueIdResponseType res = null;
        GenerateUniqueIdRequestType request = new GenerateUniqueIdRequestType();
        try {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the document manager service
            //------------------------------------------
            ihe.iti.xds_b._2007.DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
            ihe.iti.xds_b._2007.DocumentManagerPortType port = service.getDocumentManagerPortSoap();
            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_DOCUMENT_MANAGER_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 8080
                //-------------------------------------------------------------------------
                ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/DocumentManager_Service/DocumentManagerService");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_DOCUMENT_MANAGER_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:8080/DocumentManager_Service/DocumentManagerService'");
            }

            res = port.generateUniqueId(request);
        }
        catch (Exception ex) {
            log.error("DocViewerReqeustService.getUniqueDocumentId "+ex.getMessage());
            ex.printStackTrace();
        }
        log.debug("Exiting DocViewerRequestService.getUniqueDocumentId");
        return res;
    }


}
