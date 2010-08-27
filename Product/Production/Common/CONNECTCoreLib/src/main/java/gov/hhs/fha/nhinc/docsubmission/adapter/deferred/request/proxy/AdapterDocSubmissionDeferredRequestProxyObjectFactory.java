package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocSubmissionDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "AdapterXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME = "adapterxdrrequest";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocSubmissionDeferredRequestProxy getAdapterDocSubmissionDeferredRequestProxy()
    {
        return getBean(BEAN_NAME, AdapterDocSubmissionDeferredRequestProxy.class);
    }
}


