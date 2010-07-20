/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request.adapter;

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
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterXDRRequestImpl {

    private static Log log = null;
    private static EntityXDRSecuredAsyncResponseService entityXDRSecuredResponseService = null;
    public static String INVALID_ENDPOINT_MESSAGE = "ERROR: entityXDRSecuredResponseEndPointURL is null";
    private static AdapterXDRService adapterXDRService = null;

    public AdapterXDRRequestImpl() {
        log = createLogger();
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        getLogger().debug("Begin provideAndRegisterDocumentSetBRequest()");

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body != null &&
                body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        RegistryResponseType registryResponse = callAdapterComponentXDR(body.getProvideAndRegisterDocumentSetRequest(), body.getAssertion());

        //TODO: Call the XDR Response service asynchronously
        //ihe.iti.xdr._2007.AcknowledgementType ack = sendXDRResponse(registryResponse, body.getAssertion());

        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        getLogger().debug("end provideAndRegisterDocumentSetBRequest()");
        return ack;
    }

    protected AdapterComponentXDRImpl getAdapterComponentXDRImpl() {
        return new AdapterComponentXDRImpl();
    }

    protected Log getLogger() {
        return log;
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
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
    public XDRAcknowledgementType sendXDRResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion) {

        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        String entityXDRSecuredResponseEndPointURL = null;

        XDRAcknowledgementType response = null;

        entityXDRSecuredResponseEndPointURL = getEntityXDRSecuredResponseEndPointURL();

        if (NullChecker.isNotNullish(entityXDRSecuredResponseEndPointURL)) {
            EntityXDRSecuredAsyncResponsePortType port = getEntityXDRSecuredResponsePort(entityXDRSecuredResponseEndPointURL);

            setRequestContext(assertion, entityXDRSecuredResponseEndPointURL, port);

            RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = createEntityXDRResponseSecuredRequest(body, assertion);

            response = port.provideAndRegisterDocumentSetBAsyncResponse(request);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME + " is null");
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        getLogger().debug("Existing provideAndRegisterDocumentSetBResponse");

        return response;

    }

    protected void setRequestContext(AssertionType assertion, String entityXDRSecuredResponseEndPointURL, EntityXDRSecuredAsyncResponsePortType port) {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, entityXDRSecuredResponseEndPointURL, NhincConstants.ENTITY_XDR_SECURED_RESPONSE_ACTION);

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
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ENTITY_XDR_RESPONSE_SECURED_SERVICE_NAME, ex);
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

        log.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
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
