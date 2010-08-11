package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrieveresponse.NhincProxyDocRetrieveDeferredResponse;
import gov.hhs.fha.nhinc.nhincproxydocretrieveresponse.NhincProxyDocRetrieveDeferredResponsePortType;
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
public class NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl implements NhincProxyDocRetrieveDeferredRespProxy {

    private Log log = null;
    private boolean debugEnabled = false;
    private static NhincProxyDocRetrieveDeferredResponse service = null;

    /**
     * default constructor
     */
    public NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
        service = getWebService();
    }

    /**
     *
     * @return NhincProxyDocRetrieveResponse
     */
    private NhincProxyDocRetrieveDeferredResponse getWebService() {
        return new NhincProxyDocRetrieveDeferredResponse();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
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
            log.debug("-- Begin NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        String url = getUrl();
        if (NullChecker.isNotNullish(url)) {
            NhincProxyDocRetrieveDeferredResponsePortType port = getPort(url, assertion);
            RespondingGatewayCrossGatewayRetrieveResponseType resp = new RespondingGatewayCrossGatewayRetrieveResponseType();
            resp.setAssertion(assertion);
            resp.setNhinTargetSystem(target);
            resp.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
            ack = port.crossGatewayRetrieveResponse(resp);
        }
        if (debugEnabled) {
            log.debug("-- End NhincProxyDocRetrieveDeferredRespUnsecuredWebServiceImpl.crossGatewayRetrieveResponse(...) --");
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE);
        } catch (Exception e) {
            log.error("Error: Failed to retrieve url for service: '" + NhincConstants.NHINCPROXY_DOCRETRIEVE_DEFERRED_UNSECURED_RESPONSE + "'");
            log.error(e.getMessage());
        }
        return url;
    }

    /**
     *
     * @param url
     * @param assertion
     * @return NhincProxyDocRetrieveDeferredResponsePortType
     */
    private NhincProxyDocRetrieveDeferredResponsePortType getPort(String url, AssertionType assertion) {

        NhincProxyDocRetrieveDeferredResponsePortType port = service.getNhincProxyDocRetrieveDeferredResponsePortSoap();
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
