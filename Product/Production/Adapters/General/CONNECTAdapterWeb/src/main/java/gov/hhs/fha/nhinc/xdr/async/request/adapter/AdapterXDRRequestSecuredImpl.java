/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy.AdapterComponentDocSubmissionRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy.AdapterComponentDocSubmissionRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.AdapterComponentDocSubmissionImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestSecuredImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequestSecuredImpl.class);

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

    protected AdapterComponentDocSubmissionImpl getAdapterComponentXDRImpl() {
        return new AdapterComponentDocSubmissionImpl();
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

        XDRAcknowledgementType ack = null;

        AdapterComponentDocSubmissionRequestProxyObjectFactory oFactory = new AdapterComponentDocSubmissionRequestProxyObjectFactory();
        AdapterComponentDocSubmissionRequestProxy oProxy = oFactory.getAdapterComponentDocSubmissionRequestProxy();
        ack = oProxy.provideAndRegisterDocumentSetBRequest(body, assertion, "");

        return ack;

    }

}
