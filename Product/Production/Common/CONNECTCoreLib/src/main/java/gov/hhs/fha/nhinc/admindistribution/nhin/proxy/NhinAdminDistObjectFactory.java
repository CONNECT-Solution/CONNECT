/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
/**
 *
 * @author dunnek
 */
public class NhinAdminDistObjectFactory {
   private Log log = null;

    public NhinAdminDistObjectFactory()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    private static final String CONFIG_FILE_NAME = "NhinAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_AUDIT_REPOSITORY = "nhinadmindist";
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
    public NhinAdminDistProxy getAdapterAuditQueryProxy() {
        NhinAdminDistProxy result = null;
        if (context != null) {
            result = (NhinAdminDistProxy) context.getBean(BEAN_NAME_AUDIT_REPOSITORY);
        }
        return result;
    }
}
