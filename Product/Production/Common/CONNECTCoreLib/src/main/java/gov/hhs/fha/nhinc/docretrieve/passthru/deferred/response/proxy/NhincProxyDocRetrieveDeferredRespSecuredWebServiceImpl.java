package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecuredresponse.NhincProxyDocRetrieveDeferredResponseSecured;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecuredresponse.NhincProxyDocRetrieveDeferredResponseSecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredRespProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private static NhincProxyDocRetrieveDeferredResponseSecured service = null;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     *
     * @return NhincProxyDocRetrieveResponseSecured
     */
    private NhincProxyDocRetrieveDeferredResponseSecured getWebService() {
        return new NhincProxyDocRetrieveDeferredResponseSecured();
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
     * @param retrieveDocumentSetResponse
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType target) {
        if (debugEnabled) {
            log.debug("-- Begin NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse() --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = getUrl();
        if (NullChecker.isNotNullish(url)) {
            NhincProxyDocRetrieveDeferredResponseSecuredPortType port = getPort(url, assertion);
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType resp = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
            resp.setNhinTargetSystem(target);
            resp.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVE_DEFERRED_ACTION);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            ack = port.crossGatewayRetrieveResponse(resp);
        }
        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredRespSecuredWebServiceImpl.crossGatewayRetrieveResponse() --");
        }
        return ack;
    }

    /**
     *
     * @return String
     */
    private String getUrl() {
        String url = null;
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_SECURED_RESPONSE + "'");
            log.error(e.getMessage());
        }
        return url;
    }

    /**
     *
     * @param url
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredResponseSecuredPortType
     */
    private NhincProxyDocRetrieveDeferredResponseSecuredPortType getPort(String url, AssertionType assertion) {

        NhincProxyDocRetrieveDeferredResponseSecuredPortType port = service.getNhincProxyDocRetrieveDeferredResponseSecuredPortSoap();
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
