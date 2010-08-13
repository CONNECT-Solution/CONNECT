package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response;

//import gov.hhs.fha.nhinc.adapterdocretrievedeferredresp.AdapterDocRetrieveDeferredResponsePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhindocretrievedeferredrequest.RespondingGatewayDeferredRequestRetrievePortType;
import gov.hhs.fha.nhinc.nhindocretrievedeferredresponse.RespondingGatewayDeferredResponseRetrievePortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceContext;
import java.util.Map;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:16:36 PM
 */
public class NhinDocRetrieveDeferredRespWebServiceImpl implements NhinDocRetrieveDeferredRespProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhindocretrievedeferredresponse";
    private static final String SERVICE_LOCAL_PART = "RespondingGatewayDeferredResponse_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGatewayDeferredResponse_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieveDeferredResp.wsdl";
    private Log log = null;

    public NhinDocRetrieveDeferredRespWebServiceImpl()
    {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body,
                                                                  AssertionType assertion)
    {
            String url = null;
            DocRetrieveAcknowledgementType result = new DocRetrieveAcknowledgementType();

            try {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_RESPONSE);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " +
                        NhincConstants.ADAPTER_DOC_RETRIEVE_DEFERRED_REQUEST_SERVICE_NAME + " for local home community");
                log.error(ex.getMessage());
            }

            if (NullChecker.isNotNullish(url)) {
                RespondingGatewayDeferredResponseRetrievePortType port = getPort(url);

                SamlTokenCreator tokenCreator = new SamlTokenCreator();
                Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVEDEFERRED_RESPONSE_ACTION);

                ((BindingProvider) port).getRequestContext().putAll(requestContext);

                result = port.respondingGatewayDeferredResponseCrossGatewayRetrieve(body.getRetrieveDocumentSetResponse());
            }

            return result;
        }

        protected RespondingGatewayDeferredResponseRetrievePortType getPort(String url) {

            RespondingGatewayDeferredResponseRetrievePortType port = null;
            Service service = getService();
            if(service != null)
            {
                log.debug("Obtained service - creating port.");
                port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayDeferredResponseRetrievePortType.class);
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


        protected void setEndpointAddress(RespondingGatewayDeferredResponseRetrievePortType port, String url)
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
