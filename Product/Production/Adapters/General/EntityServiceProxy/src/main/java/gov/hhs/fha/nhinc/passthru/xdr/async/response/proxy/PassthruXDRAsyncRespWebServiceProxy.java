/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredAsyncResponsePortType;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredAsyncResponseService;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PassthruXDRAsyncRespWebServiceProxy implements PassthruXDRAsyncRespProxy {
    private static Log log = LogFactory.getLog(PassthruXDRAsyncRespWebServiceProxy.class);
    private static ProxyXDRSecuredAsyncResponseService service = new ProxyXDRSecuredAsyncResponseService();

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request) {
        AcknowledgementType response = new AcknowledgementType();
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType securedRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        securedRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        securedRequest.setRegistryResponse(request.getRegistryResponse());


        // Get the URL to the Pass-through XDR Async Response
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            ProxyXDRSecuredAsyncResponsePortType port = getPort(url, request.getAssertion());
            response = port.provideAndRegisterDocumentSetBAsyncResponse(securedRequest);
        }

        return response;
    }

    private ProxyXDRSecuredAsyncResponsePortType getPort(String url, AssertionType assertion)
    {
        ProxyXDRSecuredAsyncResponsePortType port = service.getProxyXDRSecuredAsyncResponsePort();

        log.info("Setting endpoint address to Pass through XDR Async Request Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.XDR_RESPONSE_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForRelatesTo(assertion));

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_RESPONSE_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.NHINC_PROXY_XDR_RESPONSE_SECURED_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

}
