/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistObjectFactory {
    private static final String CONFIG_FILE_NAME = "EntityAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_AUDIT_REPOSITORY = "entityadmindist";
    private static ApplicationContext context = null;

    static {
        context = new FileSystemXmlApplicationContext(PropertyAccessor.getPropertyFileURL() + CONFIG_FILE_NAME);
    }
    /**
     * Retrieve an adapter audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterauditquery."
     *
     * @return AdapterAuditQueryProxy instance
     */
    public EntityAdminDistProxy getAdapterAuditQueryProxy() {
        EntityAdminDistProxy result = null;
        if (context != null) {
            result = (EntityAdminDistProxy) context.getBean(BEAN_NAME_AUDIT_REPOSITORY);
        }
        return result;
    }
}
