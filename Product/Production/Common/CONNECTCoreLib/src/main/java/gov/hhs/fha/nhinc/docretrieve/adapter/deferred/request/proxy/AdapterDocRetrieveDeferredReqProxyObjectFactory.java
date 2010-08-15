package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:28:35 PM
 */
public class AdapterDocRetrieveDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocretrievedeferredrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocRetrieveDeferredReqProxy getAdapterDocRetrieveDeferredRequestProxy() {
        return getBean(BEAN_NAME, AdapterDocRetrieveDeferredReqProxy.class);
    }
    
}
