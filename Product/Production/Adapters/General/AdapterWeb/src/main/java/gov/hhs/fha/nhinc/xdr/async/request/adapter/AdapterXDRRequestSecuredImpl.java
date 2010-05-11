/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRPortType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRService;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponseService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.adapter.AdapterComponentXDRImpl;
import ihe.iti.xdr._2007.AcknowledgementType;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.Handler;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestSecuredImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequestSecuredImpl.class);
    private static EntityXDRSecuredAsyncResponseService entityXDRSecuredResponseService = null;
    public static String INVALID_ENDPOINT_MESSAGE = "ERROR: entityXDRSecuredResponseEndPointURL is null";
    private static AdapterXDRService adapterXDRService = null;

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        // Call AdapterComponent implementation to process the request.
        AssertionType assertion = createAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        RegistryResponseType registryResponse = callAdapterComponentXDR(body, assertion);

        getLogger().debug("Registry Response from AdapterXDRComponentImpl: " + registryResponse);

        // Call the XDR Response service
        ihe.iti.xdr._2007.AcknowledgementType ack = sendXDRResponse(registryResponse, assertion);

        getLogger().debug("Exiting provideAndRegisterDocumentSetBRequest");

        return ack;
    }

    protected AdapterComponentXDRImpl getAdapterComponentXDRImpl() {
        return new AdapterComponentXDRImpl();
    }

    protected Log getLogger() {
        return logger;
    }

    protected AssertionType createAssertion(WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return assertion;
    }

    protected RegistryResponseType callAdapterComponentXDR(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {

        getLogger().debug("Calling AdapterComponentXDRImpl");

        AdapterProvideAndRegisterDocumentSetRequestType adapterComponentXDRRequest = new AdapterProvideAndRegisterDocumentSetRequestType();

        adapterComponentXDRRequest.setAssertion(assertion);
        adapterComponentXDRRequest.setProvideAndRegisterDocumentSetRequest(body);

        RegistryResponseType registryResponse = null;

        String adapterComponentXDRUrl = getAdapterComponentXDRUrl();

        if (NullChecker.isNotNullish(adapterComponentXDRUrl)) {
            AdapterXDRPortType port = getAdapterXDRPort(adapterComponentXDRUrl);

            registryResponse = port.provideAndRegisterDocumentSetb(adapterComponentXDRRequest);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_XDR_SERVICE_NAME + " is null");
        }

        return registryResponse;

    }

    /**
     *
     * @param body
     * @param assertion
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType sendXDRResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion) {

        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        String entityXDRSecuredResponseEndPointURL = null;

        ihe.iti.xdr._2007.AcknowledgementType response = null;

        entityXDRSecuredResponseEndPointURL = getEntityXDRSecuredResponseEndPointURL();

        if (NullChecker.isNotNullish(entityXDRSecuredResponseEndPointURL)) {
            EntityXDRSecuredAsyncResponsePortType port = getEntityXDRSecuredResponsePort(entityXDRSecuredResponseEndPointURL);

            setRequestContext(assertion, entityXDRSecuredResponseEndPointURL, port);

            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = createEntityXDRResponseSecuredRequest(body, assertion);

            response = port.provideAndRegisterDocumentSetBAsyncResponse(request);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage(INVALID_ENDPOINT_MESSAGE);
        }

        getLogger().debug("Existing provideAndRegisterDocumentSetBResponse");

        return response;

    }

    protected void setRequestContext(AssertionType assertion, String entityXDRSecuredResponseEndPointURL, EntityXDRSecuredAsyncResponsePortType port) {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, entityXDRSecuredResponseEndPointURL, NhincConstants.ENTITY_XDR_SECURED_RESPONSE_ACTION);
        
        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForRelatesTo(assertion));

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    /**
     *
     * @return
     */
    protected String getEntityXDRSecuredResponseEndPointURL() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            logger.error("Error: Failed to retrieve url for service: " + NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME, ex);
        }

        return url;
    }

    /**
     *
     * @param url
     * @return
     */
    protected EntityXDRSecuredAsyncResponsePortType getEntityXDRSecuredResponsePort(String url) {
        EntityXDRSecuredAsyncResponsePortType port = getEntityXDRSecuredResponseService().getEntityXDRSecuredAsyncResponsePort();

        logger.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    /**
     *
     * @return
     */
    protected EntityXDRSecuredAsyncResponseService getEntityXDRSecuredResponseService() {
        if (entityXDRSecuredResponseService == null) {
            return new EntityXDRSecuredAsyncResponseService();
        } else {
            return entityXDRSecuredResponseService;
        }

    }

    protected RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType createEntityXDRResponseSecuredRequest(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion) {
        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        request.setRegistryResponse(body);
        request.setNhinTargetCommunities(createNhinTargetCommunities(assertion));

        return request;
    }

    /**
     * This will be removed once the broadcast PD is implented
     */
    private NhinTargetCommunitiesType createNhinTargetCommunities(AssertionType assertion) {

        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        target.setHomeCommunity(assertion.getHomeCommunity());
        targets.getNhinTargetCommunity().add(target);

        return targets;
    }

    /**
     * 
     * @return
     */
    protected String getAdapterComponentXDRUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            getLogger().error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_SERVICE_NAME, ex);
        }

        return url;
    }

    protected AdapterXDRPortType getAdapterXDRPort(String url) {

        AdapterXDRPortType port = getAdapterXDRService().getAdapterXDRPort();

        getLogger().info("Setting endpoint address to Adapter XDR Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected AdapterXDRService getAdapterXDRService() {
        if (adapterXDRService == null) {
            return new AdapterXDRService();
        } else {
            return adapterXDRService;
        }

    }
}
