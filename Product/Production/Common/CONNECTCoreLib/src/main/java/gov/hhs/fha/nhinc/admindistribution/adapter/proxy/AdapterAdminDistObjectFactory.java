/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 *
 * @author dunnek
 */
public class AdapterAdminDistObjectFactory {
   private Log log = null;

    public AdapterAdminDistObjectFactory()
    {
        log = createLogger();
        log.debug("created logger");
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    private static final String CONFIG_FILE_NAME = "AdapterAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_ADMIN_DIST = "adapteradmindist";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }
    /**
     * Retrieve an adapter audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincadmindist."
     *
     * @return AdapterAuditQueryProxy instance
     */
    public AdapterAdminDistProxy getAdapterAdminDistProxy() {
        log.debug("Begin getAdapterAdminDistProxy()");

        AdapterAdminDistProxy result = null;
        
        log.debug("Getting bean");
        result = (AdapterAdminDistProxy) context.getBean(BEAN_NAME_ADAPTER_ADMIN_DIST);
        return result;
    }
}
