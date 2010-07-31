package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.request;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_REQUEST = "entitydocretrievedeferredreq";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }

    /**
     * Retrieve an Entity Document Retrieve Deferred Request implementation
     * using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "entitydocretrievedeferredreq".
     * @return EntityDocRetrieveDeferredReqProxy
     */
    public EntityDocRetrieveDeferredReqProxy getEntityDocRetrieveDeferredReqProxy()
    {
        EntityDocRetrieveDeferredReqProxy entityDocRetrieveDeferredReqProxy = null;
        if(null != context)
        {
            entityDocRetrieveDeferredReqProxy = (EntityDocRetrieveDeferredReqProxy) context.getBean(BEAN_NAME_ENTITY_DOCRETRIEVE_DEFERRED_REQUEST);
        }
        return entityDocRetrieveDeferredReqProxy;
    }
}
