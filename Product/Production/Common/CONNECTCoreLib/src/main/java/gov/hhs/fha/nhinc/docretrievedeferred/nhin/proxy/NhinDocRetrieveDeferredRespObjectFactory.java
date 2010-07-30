package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy;

import gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.AdapterDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:00:19 PM
 */
public class NhinDocRetrieveDeferredRespObjectFactory {
    private Log log = null;

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_RESPONSE = "nhindocretrievedeferredresponse";
    private static ApplicationContext context = null;

     public NhinDocRetrieveDeferredRespObjectFactory()
     {
         log = LogFactory.getLog(getClass());

         context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
     }

     /**
      * Retrieve an adapter audit query implementation using the IOC framework.
      * This method retrieves the object from the framework that has an
      * identifier of "nhindocretrievedeferredresponse."
      *
      * @return AdapterAuditQueryProxy instance
      */
     public AdapterDocRetrieveDeferredRespProxy getDocumentDeferredResponseProxy() {

         AdapterDocRetrieveDeferredRespProxy result = null;

         if (context != null) {
             result = (AdapterDocRetrieveDeferredRespProxy) context.getBean(BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_RESPONSE);
         }
         else {
             log.error("Could not get context "+ BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_RESPONSE + " from config file " +
                     PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
         }

         return result;
     }

}
