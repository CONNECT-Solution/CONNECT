package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:28:35 PM
 */
public class AdapterComponentDocRetrieveDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adaptercomponentdocretrievedeferredrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterComponentDocRetrieveDeferredReqProxy getAdapterDocRetrieveDeferredRequestProxy() {
        return getBean(BEAN_NAME, AdapterComponentDocRetrieveDeferredReqProxy.class);
    }

}
