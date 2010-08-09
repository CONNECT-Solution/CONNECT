/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhinc.proxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author dunnek
 */
public class NhincAdminDistObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhincAdminDistProxyConfig.xml";
    private static final String BEAN_NAME = "nhincadmindist";
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
    public NhincAdminDistProxy getNhincAdminDistProxy() {
        NhincAdminDistProxy result = null;

        result = (NhincAdminDistProxy) context.getBean(BEAN_NAME);
        return result;
    }
}
