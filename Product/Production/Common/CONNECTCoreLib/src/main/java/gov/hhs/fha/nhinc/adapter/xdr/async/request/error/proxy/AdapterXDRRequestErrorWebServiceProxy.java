/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.adapterxdrrequesterrorsecured.AdapterXDRRequestErrorSecuredService;
import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import gov.hhs.fha.nhinc.adapterxdrrequesterrorsecured.AdapterXDRRequestErrorSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRRequestErrorWebServiceProxy implements AdapterXDRRequestErrorProxy {
    private static Log log = LogFactory.getLog(AdapterXDRRequestErrorWebServiceProxy.class);
    private static AdapterXDRRequestErrorSecuredService service = new AdapterXDRRequestErrorSecuredService();

   public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestErrorType body) {
       XDRAcknowledgementType response = new XDRAcknowledgementType();
        AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType securedRequest = new AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType();
        securedRequest.setProvideAndRegisterDocumentSetRequest(body.getProvideAndRegisterDocumentSetRequest());
        securedRequest.setErrorMsg(body.getErrorMsg());

        // Get the URL to the Adapter Patient Discovery Async Req
        String url = getUrl();

        if (NullChecker.isNotNullish(url))
        {
            AdapterXDRRequestErrorSecuredPortType port = getPort(url, body.getAssertion());
            response = port.provideAndRegisterDocumentSetBRequestError(securedRequest);
        }

        return response;
   }

   private AdapterXDRRequestErrorSecuredPortType getPort(String url, AssertionType assertion)
    {
        AdapterXDRRequestErrorSecuredPortType port = service.getAdapterXDRRequestErrorSecuredPortSoap();

        log.info("Setting endpoint address to Adapter XDR Async Request Secured Error Service to " + url);
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
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_SECURED_ASYNC_REQ_ERROR_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }
}
