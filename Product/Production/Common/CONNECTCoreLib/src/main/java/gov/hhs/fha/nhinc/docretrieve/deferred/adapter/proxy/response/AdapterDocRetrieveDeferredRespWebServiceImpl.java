package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.response;

import gov.hhs.fha.nhinc.adapterdocretrievedeferredresp.AdapterDocRetrieveDeferredResponsePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
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

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 1:53:02 PM
 */
public class AdapterDocRetrieveDeferredRespWebServiceImpl implements AdapterDocRetrieveDeferredRespProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrievedeferredreq";
    private static final String SERVICE_LOCAL_PART = "AdapterDocRetrieveDeferredResponse";
    private static final String PORT_LOCAL_PART = "AdapterDocRetrieveDeferredResponsePortSoap";
    private static final String WSDL_FILE = "AdapterDocRetrieveDeferredResp.wsdl";
    private Log log = null;

    public AdapterDocRetrieveDeferredRespWebServiceImpl() {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body,
                                                             AssertionType assertion) {
        RespondingGatewayCrossGatewayRetrieveResponseType       unsecureBody = new RespondingGatewayCrossGatewayRetrieveResponseType();

        unsecureBody.setAssertion(assertion);
        unsecureBody.setRetrieveDocumentSetResponse(body.getRetrieveDocumentSetResponse());

        return receiveFromAdapter(unsecureBody, assertion);
    }

    protected DocRetrieveAcknowledgementType receiveFromAdapter(RespondingGatewayCrossGatewayRetrieveResponseType body, AssertionType assertion) {
        String url = null;
        DocRetrieveAcknowledgementType result = new DocRetrieveAcknowledgementType();

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_RESPONSE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " +
                    NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_RESPONSE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            AdapterDocRetrieveDeferredResponsePortType port = getPort(url);

            result = port.crossGatewayRetrieveResponse(body);
        }

        return result;
    }

    protected AdapterDocRetrieveDeferredResponsePortType getPort(String url) {

        AdapterDocRetrieveDeferredResponsePortType port = null;
        Service service = getService();
        if(service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocRetrieveDeferredResponsePortType.class);
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


    protected void setEndpointAddress(AdapterDocRetrieveDeferredResponsePortType port, String url)
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
            log.info("Setting endpoint address to Document Retrieve Response Secure Service to " + url);
            ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

}
