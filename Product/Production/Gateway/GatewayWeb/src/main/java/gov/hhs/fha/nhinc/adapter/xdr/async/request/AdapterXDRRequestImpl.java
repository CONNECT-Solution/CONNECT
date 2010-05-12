/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request;

import gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy.AdapterXDRRequestProxy;
import gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy.AdapterXDRRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequestImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {

        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");
        AdapterXDRRequestProxyObjectFactory factory = new AdapterXDRRequestProxyObjectFactory();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body != null &&
                body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        AdapterXDRRequestProxy proxy = factory.getAdapterXDRRequestProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(body.getProvideAndRegisterDocumentSetRequest(), body.getAssertion());

        getLogger().debug("Exiting provideAndRegisterDocumentSetBRequest");

        return response;
    }

    protected Log getLogger(){
        return logger;
    }
}