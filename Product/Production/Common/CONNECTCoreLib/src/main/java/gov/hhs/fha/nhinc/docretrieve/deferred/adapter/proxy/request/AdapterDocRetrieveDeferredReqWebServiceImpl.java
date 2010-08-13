package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.request;

import gov.hhs.fha.nhinc.adapterdocretrievedeferredreq.AdapterDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:37:22 PM
 */
public class AdapterDocRetrieveDeferredReqWebServiceImpl implements AdapterDocRetrieveDeferredReqProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreq";
    private static final String SERVICE_LOCAL_PART = "AdapterDocRetrieveDeferredRequest";
    private static final String PORT_LOCAL_PART = "AdapterDocRetrieveDeferredRequestPortSoap";
    private static final String WSDL_FILE = "AdapterDocRetrieveDeferredReq.wsdl";
    private Log log = null;

    public AdapterDocRetrieveDeferredReqWebServiceImpl() {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body,
                                                        AssertionType assertion) {
        RespondingGatewayCrossGatewayRetrieveRequestType        unsecureBody = new RespondingGatewayCrossGatewayRetrieveRequestType();

        unsecureBody.setAssertion(assertion);
        unsecureBody.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());

        return sendToAdapter(unsecureBody, assertion);
    }

    public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveRequestType body, AssertionType assertion) {
        String url = null;
        DocRetrieveAcknowledgementType result = new DocRetrieveAcknowledgementType();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " +
                    NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AdapterDocRetrieveDeferredRequestPortType port = getPort(url);

//            SamlTokenCreator tokenCreator = new SamlTokenCreator();
//            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVEDEFERRED_REQUEST_ACTION);
//
//            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            result = port.crossGatewayRetrieveRequest(body);
        }

        return result;
    }

    protected AdapterDocRetrieveDeferredRequestPortType getPort(String url) {

        AdapterDocRetrieveDeferredRequestPortType port = null;
        Service service = getService();
        if(service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocRetrieveDeferredRequestPortType.class);
            setEndpointAddress(port, url);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }


    protected Service getService()
    {
        if(cachedService == null)
        {
            try
            {
                cachedService = new ServiceUtil().createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch(Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }


    protected void setEndpointAddress(AdapterDocRetrieveDeferredRequestPortType port, String url)
    {
        if(port == null)
        {
            log.error("Port was null - not setting endpoint address.");
        }
        else if((url == null) || (url.length() < 1))
        {
            log.error("URL was null or empty - not setting endpoint address.");
        }
        else
        {
            log.info("Setting endpoint address to Document Retrieve Request Secure Service to " + url);
            ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

}
