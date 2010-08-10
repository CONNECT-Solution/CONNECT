/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType;
import gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponseService;
import gov.hhs.fha.nhinc.async.AsyncMessageHandler;
import gov.hhs.fha.nhinc.async.AsyncMessageIdCreator;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImpl
{

    private static final Log logger = LogFactory.getLog(AdapterXDRResponseSecuredImpl.class);
    private static AdapterComponentXDRResponseService adapterXDRService = new AdapterComponentXDRResponseService();

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context)
    {

        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");
        WebServiceHelper oHelper = new WebServiceHelper();
        XDRAcknowledgementType ack = null;
        try
        {
            if (body != null)
            {
                ack = (XDRAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(this, this.getClass(), "callAdapterComponentXDR", body, context);
            } else
            {
                logger.error("Failed to call the web orchestration (" + this.getClass() + ".callAdapterComponentXDR).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            logger.error("Failed to call the web orchestration (" + this.getClass() + ".callAdapterComponentXDR).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected Log getLogger()
    {
        return logger;
    }

    protected XDRAcknowledgementType callAdapterComponentXDR(RegistryResponseType body, AssertionType assertion)
    {

        getLogger().debug("Calling AdapterComponentXDRImpl");

        XDRAcknowledgementType ack = null;
        AdapterRegistryResponseType msg = new AdapterRegistryResponseType();
        msg.setAssertion(assertion);
        msg.setRegistryResponse(body);

        String adapterComponentXDRUrl = getAdapterComponentXDRUrl();

        if (NullChecker.isNotNullish(adapterComponentXDRUrl))
        {
            AdapterComponentXDRResponsePortType port = getAdapterXDRPort(adapterComponentXDRUrl, assertion);

            ack = port.provideAndRegisterDocumentSetBResponse(msg);

        } else
        {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME + " is null");
        }

        return ack;

    }

    /**
     *
     * @return
     */
    protected String getAdapterComponentXDRUrl()
    {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            getLogger().error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME, ex);
        }

        return url;
    }

    protected AdapterComponentXDRResponsePortType getAdapterXDRPort(String url, AssertionType assertion)
    {

        AdapterComponentXDRResponsePortType port = adapterXDRService.getAdapterComponentXDRResponsePort();

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
