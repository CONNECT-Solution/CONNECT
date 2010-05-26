package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredService;
import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterXDRRequestWebServiceProxy implements AdapterXDRRequestProxy {

    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(AdapterXDRRequestWebServiceProxy.class);
    private static AdapterXDRRequestSecuredService securedAdapterService = null;

    /**
     *
     * @param body
     * @param assertion
     * @return
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body, AssertionType assertion) {

        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        String adapterXDRRequestSecuredEndPointURL = null;

        XDRAcknowledgementType response = null;

        adapterXDRRequestSecuredEndPointURL = getAdapterXDRRequestSecuredUrl();

        if (NullChecker.isNotNullish(adapterXDRRequestSecuredEndPointURL)) {
            AdapterXDRRequestSecuredPortType port = getAdapterXDRRequestSecuredPort(adapterXDRRequestSecuredEndPointURL);

            setRequestContext(assertion, adapterXDRRequestSecuredEndPointURL, port);

            response = port.provideAndRegisterDocumentSetBRequest(body);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
            response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        getLogger().debug("Existing provideAndRegisterDocumentSetBRequest");

        return response;

    }

    protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, AdapterXDRRequestSecuredPortType port) {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, adapterXDRRequestSecuredEndPointURL, NhincConstants.ADAPTER_XDRREQUEST_SECURED_ACTION);

        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    /**
     *
     * @return
     */
    protected String getAdapterXDRRequestSecuredUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            logger.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME, ex);
        }

        return url;
    }

    /**
     *
     * @param url
     * @return
     */
    protected AdapterXDRRequestSecuredPortType getAdapterXDRRequestSecuredPort(String url) {
        AdapterXDRRequestSecuredPortType port = getAdapterXDRRequestSecuredService().getAdapterXDRRequestSecuredPortSoap();

        logger.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    protected Log getLogger() {
        return logger;
    }

    /**
     *
     * @return
     */
    protected AdapterXDRRequestSecuredService getAdapterXDRRequestSecuredService() {
        if (securedAdapterService == null) {
            return new AdapterXDRRequestSecuredService();
        } else {
            return securedAdapterService;
        }

    }
}
