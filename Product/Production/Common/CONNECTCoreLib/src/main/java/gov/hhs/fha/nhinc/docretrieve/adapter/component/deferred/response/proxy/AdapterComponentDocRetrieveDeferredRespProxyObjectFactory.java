package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Aug 2, 2010
 * Time: 3:02:25 PM
 */
public class AdapterComponentDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adaptercomponentdocretrievedeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocRetrieveDeferredRespProxy getAdapterDocRetrieveDeferredResponseProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocRetrieveDeferredRespProxy.class);
    }
}
