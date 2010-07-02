/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class DocumentRetrieveClient {
    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(DocumentRetrieveClient.class);
    private static EntityDocRetrieve service = new EntityDocRetrieve();

    public String retriveDocument(DocumentInformation documentInformation){

        EntityDocRetrievePortType port = getPort(getEntityDocumentRetrieveProxyAddress());

        RespondingGatewayCrossGatewayRetrieveRequestType request = createCrossGatewayRetrieveRequest(documentInformation);

        RetrieveDocumentSetResponseType response = port.respondingGatewayCrossGatewayRetrieve(request);

        return extractDocument(response);
    }

    private String extractDocument(RetrieveDocumentSetResponseType response){
        String documentInXmlFormat = null;

        if (response == null)
        {
            return null;
        }
        List<DocumentResponse> documentResponseList = response.getDocumentResponse();

        if (documentResponseList != null && documentResponseList.size() > 0)
        {
            DocumentResponse documentResponse = documentResponseList.get(0);

            if (documentResponse != null && documentResponse.getDocument() != null)
            {
                documentInXmlFormat = new String(documentResponse.getDocument());
            }
        }
        
        //log.debug("Document: " + documentInXmlFormat);

        //convertXMLToHTML(documentInXmlFormat, null);

        return documentInXmlFormat;
    }

    /**
     * 
     * @return
     */
    private RespondingGatewayCrossGatewayRetrieveRequestType createCrossGatewayRetrieveRequest(DocumentInformation documentInformation){
        
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();

        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = new RetrieveDocumentSetRequestType();

        DocumentRequest docRequest = new DocumentRequest();
        //docRequest.setHomeCommunityId("urn:oid:2.2");
        //docRequest.setRepositoryUniqueId("1");
        docRequest.setHomeCommunityId(documentInformation.getHomeCommunityID());
        docRequest.setRepositoryUniqueId(documentInformation.getRepositoryUniqueID());
        docRequest.setDocumentUniqueId(documentInformation.getDocumentID());

        retrieveDocumentSetRequest.getDocumentRequest().add(docRequest);

        request.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);

        // Add Assertion
        AssertionCreator assertionCreator = new AssertionCreator();
        request.setAssertion(assertionCreator.createAssertion());

        return request;
    }

    /**
     *
     * @return
     */
    private String getEntityDocumentRetrieveProxyAddress() {
        String endpointAddress = null;

        try {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
            endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_DOC_RETRIEVE_PROXY_SERVICE_NAME);
            log.debug("Doc Retrive endpoint address: " + endpointAddress);
        } catch (PropertyAccessException pae) {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        } catch (ConnectionManagerException cme) {
            log.error("Exception encountered retrieving the entity doc query connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }

    /**
     *
     * @param url
     * @return
     */
    private EntityDocRetrievePortType getPort(String url) {
        if (service == null) {
            service = new EntityDocRetrieve();
        }
        EntityDocRetrievePortType port = service.getEntityDocRetrievePortSoap11();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

    /**
     * Retrieve the local home community id
     *
     * @return Local home community id
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }



}
