/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author Jon Hoppesch
 */
public class NhinPatientDiscoveryDeferredRespProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinPatientDiscoveryAsyncRespProxyConfig.xml";
    private static final String BEAN_NAME = "nhinpatientdiscoveryasyncresp";

    /**
     * Returns the name of the config file.
     *
     * @return The name of the config file.
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     * Return an instance of the NhinPatientDiscoveryProxy class.
     *
     * @return An instance of the NhinPatientDiscoveryProxy class.
     */
    public NhinPatientDiscoveryDeferredRespProxy getNhinPatientDiscoveryAsyncRespProxy() {
        return getBean(BEAN_NAME, NhinPatientDiscoveryDeferredRespProxy.class);
    }
}
