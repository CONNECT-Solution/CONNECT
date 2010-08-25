package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "PassthruXDRAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "passthruxdrasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocSubmissionDeferredRequestProxy getPassthruDocSubmissionDeferredRequestProxy() {
        return getBean(BEAN_NAME, PassthruDocSubmissionDeferredRequestProxy.class);
    }

}
