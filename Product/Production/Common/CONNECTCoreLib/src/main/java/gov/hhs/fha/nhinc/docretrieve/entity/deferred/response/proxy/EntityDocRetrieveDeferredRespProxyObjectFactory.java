package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE = "entitydocretrievedeferredresp";

    /**
     * Retrieve an Entity Document Retrieve Deferred Response implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "entitydocretrievedeferredresp".
     * @return EntityDocRetrieveDeferredRespProxy
     */
    public EntityDocRetrieveDeferredRespProxy getEntityDocRetrieveDeferredRespProxy()
    {
            return getBean(BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE, EntityDocRetrieveDeferredRespProxy.class);
    }

    /**
     * 
     * @return String
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }
    
}
