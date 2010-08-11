package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferredsecuredrequest.NhincProxyDocRetrieveDeferredRequestSecured;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferredsecuredrequest.NhincProxyDocRetrieveDeferredRequestSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqSecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean debugEnable = false;
    private static NhincProxyDocRetrieveDeferredRequestSecured service = null;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqSecuredWebServiceImpl() {
        log = createLogger();
        debugEnable = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     *
     * @return NhincProxyDocRetrieveDeferredRequestSecured
     */
    private NhincProxyDocRetrieveDeferredRequestSecured getWebService() {
        return new NhincProxyDocRetrieveDeferredRequestSecured();
    }

    /**
     * Creates logger instance
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnable) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST);
            NhincProxyDocRetrieveDeferredRequestSecuredPortType port = getPort(url, assertion);
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            req.setNhinTargetSystem(target);
            req.setRetrieveDocumentSetRequest(request);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            ack = port.crossGatewayRetrieveRequest(req);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_REQUEST);
            log.error(e.getMessage());
        }
        if (debugEnable) {
            log.debug("-- End NhincProxyDocRetrieveDeferredReqSecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }
        return ack;
    }

    /**
     *
     * @param url
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredRequestSecuredPortType
     */
    protected NhincProxyDocRetrieveDeferredRequestSecuredPortType getPort(String url, AssertionType assertion) {
        NhincProxyDocRetrieveDeferredRequestSecuredPortType port = service.getNhincProxyDocRetrieveDeferredRequestSecuredPortSoap();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        List<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        return port;
    }
}
