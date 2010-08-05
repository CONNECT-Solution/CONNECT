/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import gov.hhs.fha.nhinc.adaptercomponentxdrrequest.AdapterComponentXDRRequestPortType;
import gov.hhs.fha.nhinc.adaptercomponentxdrrequest.AdapterComponentXDRRequestService;
import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.adapter.AdapterComponentXDRImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestSecuredImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequestSecuredImpl.class);
    private static AdapterComponentXDRRequestService adapterXDRService = new AdapterComponentXDRRequestService();

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        // Call AdapterComponent implementation to process the request.
        AssertionType assertion = createAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        XDRAcknowledgementType ack = callAdapterComponentXDR(body.getProvideAndRegisterDocumentSetRequest(), assertion);

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

    protected XDRAcknowledgementType callAdapterComponentXDR(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {

        getLogger().debug("Calling AdapterComponentXDRImpl");

        AdapterProvideAndRegisterDocumentSetRequestType adapterComponentXDRRequest = new AdapterProvideAndRegisterDocumentSetRequestType();

        adapterComponentXDRRequest.setAssertion(assertion);
        adapterComponentXDRRequest.setProvideAndRegisterDocumentSetRequest(body);

        XDRAcknowledgementType ack = null;

        String adapterComponentXDRUrl = getAdapterComponentXDRUrl();

        if (NullChecker.isNotNullish(adapterComponentXDRUrl)) {
            AdapterComponentXDRRequestPortType port = getAdapterXDRPort(adapterComponentXDRUrl, assertion);

            ack = port.provideAndRegisterDocumentSetBRequest(adapterComponentXDRRequest);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_COMPONENT_XDR_REQUEST_SERVICE_NAME + " is null");
        }

        return ack;

    }

    /**
     * 
     * @return
     */
    protected String getAdapterComponentXDRUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_COMPONENT_XDR_REQUEST_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            getLogger().error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_COMPONENT_XDR_REQUEST_SERVICE_NAME, ex);
        }

        return url;
    }

    protected AdapterComponentXDRRequestPortType getAdapterXDRPort(String url, AssertionType assertion) {

        AdapterComponentXDRRequestPortType port = adapterXDRService.getAdapterComponentXDRRequestPort();

        getLogger().info("Setting endpoint address to Adapter XDR Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        Map requestContext = new HashMap();

        ArrayList<Handler> handlerSetUp = new ArrayList<Handler>();
        AsyncMessageHandler msgHandler = new AsyncMessageHandler();
        handlerSetUp.add(msgHandler);
        ((javax.xml.ws.BindingProvider) port).getBinding().setHandlerChain(handlerSetUp);

        AsyncMessageIdCreator msgIdCreator = new AsyncMessageIdCreator();
        requestContext.putAll(msgIdCreator.CreateRequestContextForMessageId(assertion));

        ((BindingProvider) port).getRequestContext().putAll(requestContext);

        return port;
    }
}
