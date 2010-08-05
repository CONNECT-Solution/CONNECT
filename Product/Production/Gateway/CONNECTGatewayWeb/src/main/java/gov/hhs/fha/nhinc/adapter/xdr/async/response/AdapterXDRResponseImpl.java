/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.response;

import gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy.AdapterXDRResponseProxy;
import gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy.AdapterXDRResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseImpl {
    private static final Log logger = LogFactory.getLog(AdapterXDRResponseImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body != null &&
                body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        AdapterXDRResponseProxyObjectFactory factory = new AdapterXDRResponseProxyObjectFactory();

        AdapterXDRResponseProxy proxy = factory.getAdapterXDRResponseProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBResponse(body.getRegistryResponse(), body.getAssertion());

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return response;
    }

    protected Log getLogger(){
        return logger;
    }

}