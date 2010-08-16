package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 3:02:25 PM
 */
public class AdapterDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocretrievedeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocRetrieveDeferredRespProxy getAdapterDocRetrieveDeferredResponseProxy() {
        return getBean(BEAN_NAME, AdapterDocRetrieveDeferredRespProxy.class);
    }
}
