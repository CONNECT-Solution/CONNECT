/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;
/**
 *
 * @author dunnek
 */
public class NhinAdminDistObjectFactory extends ComponentProxyObjectFactory{

    private static final String CONFIG_FILE_NAME = "NhinAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_NHIN_ADMIN_DIST = "nhinadmindist";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincadmindist."
     *
     * @return AdapterAuditQueryProxy instance
     */
    public NhinAdminDistProxy getNhinAdminDistProxy() {
        log.debug("Begin getNhinAdminDistProxy()");
        
        return getBean(BEAN_NAME_NHIN_ADMIN_DIST, NhinAdminDistProxy.class);
    }
}
