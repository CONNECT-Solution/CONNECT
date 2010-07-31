package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.response;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE = "entitydocretrievedeferredresp";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve an Entity Document Retrieve Deferred Response implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "entitydocretrievedeferredresp".
     * @return EntityDocRetrieveDeferredRespProxy
     */
    public EntityDocRetrieveDeferredRespProxy getEntityDocRetrieveDeferredRespProxy()
    {
        EntityDocRetrieveDeferredRespProxy entityDocRetrieveDeferredRespProxy = null;
        if(null != context)
        {
            entityDocRetrieveDeferredRespProxy = (EntityDocRetrieveDeferredRespProxy)context.getBean(BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_RESPONSE);
        }
        return entityDocRetrieveDeferredRespProxy;
    }
    
}
