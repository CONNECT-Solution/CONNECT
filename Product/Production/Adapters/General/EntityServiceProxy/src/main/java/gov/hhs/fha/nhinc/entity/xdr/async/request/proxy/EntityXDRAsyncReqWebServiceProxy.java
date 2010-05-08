/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredAsyncRequestPortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.request.EntityXDRSecuredAsyncRequestService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
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
public class EntityXDRAsyncReqWebServiceProxy implements EntityXDRAsyncReqProxy {
    private static Log log = LogFactory.getLog(EntityXDRAsyncReqWebServiceProxy.class);
    private static EntityXDRSecuredAsyncRequestService service = new EntityXDRSecuredAsyncRequestService();

    public AcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType request) {
        AcknowledgementType response = new AcknowledgementType();
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType securedRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        securedRequest.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());
        securedRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());

        // Get the URL to the Entity XDR Async Req Service
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null)) {
            EntityXDRSecuredAsyncRequestPortType port = getPort(url, request.getAssertion());

            response = port.provideAndRegisterDocumentSetBAsyncRequest(securedRequest);
        }

        return response;
    }

    private EntityXDRSecuredAsyncRequestPortType getPort(String url, AssertionType assertion) {
        EntityXDRSecuredAsyncRequestPortType port = service.getEntityXDRSecuredAsyncRequestPort();

        log.info("Setting endpoint address to Entity XDR Async Request Secured Service to " + url);
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

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ENTITY_XDR_REQUEST_SECURED_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

}
