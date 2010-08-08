package gov.hhs.fha.nhinc.docretrieve.nhinc.proxy.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequest;
import gov.hhs.fha.nhinc.nhincproxydocretrievedeferredrequest.NhincProxyDocRetrieveDeferredRequestPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.handler.Handler;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredReqProxy {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     * 
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
        if (debugEnabled) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            NhincProxyDocRetrieveDeferredRequestPortType port = getPort(url, assertion);
            RespondingGatewayCrossGatewayRetrieveRequestType req = new RespondingGatewayCrossGatewayRetrieveRequestType();
            req.setAssertion(assertion);
            req.setNhinTargetSystem(target);
            req.setRetrieveDocumentSetRequest(request);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ack = port.crossGatewayRetrieveRequest(req);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_REQUEST);
            log.error(e.getMessage());
        }
        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredReqUnsecuredWebServiceImpl.crossGatewayRetrieveRequest() --");
        }

        return ack;
    }

    /**
     * 
     * @param url
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredRequestPortType
     */
    protected NhincProxyDocRetrieveDeferredRequestPortType getPort(String url, AssertionType assertion) {
        NhincProxyDocRetrieveDeferredRequest service = new NhincProxyDocRetrieveDeferredRequest();
        NhincProxyDocRetrieveDeferredRequestPortType port = service.getNhincProxyDocRetrieveDeferredRequestPortSoap();
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
