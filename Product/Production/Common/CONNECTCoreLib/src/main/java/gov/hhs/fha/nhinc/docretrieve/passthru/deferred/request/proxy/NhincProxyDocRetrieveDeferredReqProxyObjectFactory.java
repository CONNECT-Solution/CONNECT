package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class NhincProxyDocRetrieveDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_REQUEST = "nhincproxydocretrievedeferredreq";

    /**
     * Retrieve an Nhinc proxy Document Retrieve Deferred Request implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincproxydocretrievedeferredreq".
     * @return NhincProxyDocRetrieveDeferredReqProxy
     */
    public NhincProxyDocRetrieveDeferredReqProxy getNhincProxyDocRetrieveDeferredReqProxy() {
        return getBean(BEAN_NAME_NHINCPROXY_DOCRETRIEVE_DEFERRED_REQUEST, NhincProxyDocRetrieveDeferredReqProxy.class);
    }

    /**
     * 
     * @return String
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }
}
