package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy;

import gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:28:35 PM
 */
public class NhinDocRetrieveDeferredReqObjectFactory {
    private Log log = null;

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_REQUEST = "nhindocretrievedeferredrequest";
    private static ApplicationContext context = null;

     public NhinDocRetrieveDeferredReqObjectFactory()
     {
         log = LogFactory.getLog(getClass());

         context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
     }

     /**
      * Retrieve an adapter audit query implementation using the IOC framework.
      * This method retrieves the object from the framework that has an
      * identifier of "nhindocretrievedeferredrequest."
      *
      * @return AdapterAuditQueryProxy instance
      */
     public NhinDocRetrieveDeferredReqProxy getDocumentDeferredRequestProxy() {

         NhinDocRetrieveDeferredReqProxy result = null;

         if (context != null) {
             result = (NhinDocRetrieveDeferredReqProxy) context.getBean(BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_REQUEST);
         }
         else {
             log.error("Could not get context "+ BEAN_NAME_NHIN_DOC_RETRIEVE_DEFERRED_REQUEST + " from config file " +
                     PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
         }

         return result;
     }
}
