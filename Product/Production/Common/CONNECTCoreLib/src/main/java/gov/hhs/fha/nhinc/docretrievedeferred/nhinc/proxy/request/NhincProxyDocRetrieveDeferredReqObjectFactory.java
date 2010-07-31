package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.request;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_REQUEST = "nhincproxydocretrievedeferredreq";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve an Nhinc proxy Document Retrieve Deferred Request implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincproxydocretrievedeferredreq".
     * @return NhincProxyDocRetrieveDeferredReqProxy
     */
    public NhincProxyDocRetrieveDeferredReqProxy getNhincProxyDocRetrieveDeferredReqProxy()
    {
        NhincProxyDocRetrieveDeferredReqProxy nhincProxyDocRetrieveDeferredReqProxy = null;
        if(null != context)
        {
            nhincProxyDocRetrieveDeferredReqProxy = (NhincProxyDocRetrieveDeferredReqProxy) context.getBean(BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_REQUEST);
        }
        return nhincProxyDocRetrieveDeferredReqProxy;
    }
}
