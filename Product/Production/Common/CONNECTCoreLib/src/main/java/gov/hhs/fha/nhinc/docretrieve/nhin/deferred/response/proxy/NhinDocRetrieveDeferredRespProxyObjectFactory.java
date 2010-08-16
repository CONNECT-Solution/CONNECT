package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:00:19 PM
 */
public class NhinDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "nhindocretrievedeferredresponse";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public NhinDocRetrieveDeferredRespProxy getNhinDocRetrieveDeferredResponseProxy() {
        return getBean(BEAN_NAME, NhinDocRetrieveDeferredRespProxy.class);
    }
}
