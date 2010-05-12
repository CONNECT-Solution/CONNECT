/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredAsyncRequestPortType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredAsyncRequestService;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruXDRAsyncReqWebServiceProxy implements PassthruXDRAsyncReqProxy {
    private static Log log = LogFactory.getLog(PassthruXDRAsyncReqWebServiceProxy.class);
    private static ProxyXDRSecuredAsyncRequestService service = new ProxyXDRSecuredAsyncRequestService();

    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType request) {
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType securedRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        securedRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());


        // Get the URL to the Pass-through XDR Async Req
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            ProxyXDRSecuredAsyncRequestPortType port = getPort(url, request.getAssertion());
            response = port.provideAndRegisterDocumentSetBAsyncRequest(securedRequest);
        }

        return response;
    }

    private ProxyXDRSecuredAsyncRequestPortType getPort(String url, AssertionType assertion)
    {
        ProxyXDRSecuredAsyncRequestPortType port = service.getProxyXDRSecuredAsyncRequestPort();

        log.info("Setting endpoint address to Pass through XDR Async Request Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.XDR_REQUEST_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
