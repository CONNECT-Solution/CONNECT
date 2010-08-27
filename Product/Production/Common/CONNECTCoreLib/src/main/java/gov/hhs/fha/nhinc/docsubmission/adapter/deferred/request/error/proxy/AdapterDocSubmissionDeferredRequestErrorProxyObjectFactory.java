package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

public class AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestErrorProxyConfig.xml";
    private static final String BEAN_NAME = "adapterxdrrequesterror";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocSubmissionDeferredRequestErrorProxy getAdapterDocSubmissionDeferredRequestErrorProxy()
    {
        return getBean(BEAN_NAME, AdapterDocSubmissionDeferredRequestErrorProxy.class);
    }
}
