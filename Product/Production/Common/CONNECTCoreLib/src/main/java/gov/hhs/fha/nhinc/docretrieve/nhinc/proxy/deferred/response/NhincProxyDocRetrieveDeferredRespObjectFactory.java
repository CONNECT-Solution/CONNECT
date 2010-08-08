package gov.hhs.fha.nhinc.docretrieve.nhinc.proxy.deferred.response;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredRespObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_RESPONSE = "nhincproxydocretrievedeferredresp";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve an Nhinc proxy Document Retrieve Deferred Response implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincproxydocretrievedeferredresp".
     * @return NhincProxyDocRetrieveDeferredRespProxy
     */
    public NhincProxyDocRetrieveDeferredRespProxy getNhincProxyDocRetrieveDeferredRespProxy() {
        NhincProxyDocRetrieveDeferredRespProxy nhincProxyDocRetrieveDeferredRespProxy = null;
        if (null != context) {
            nhincProxyDocRetrieveDeferredRespProxy = (NhincProxyDocRetrieveDeferredRespProxy) context.getBean(BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_RESPONSE);
        }
        return nhincProxyDocRetrieveDeferredRespProxy;
    }
}
