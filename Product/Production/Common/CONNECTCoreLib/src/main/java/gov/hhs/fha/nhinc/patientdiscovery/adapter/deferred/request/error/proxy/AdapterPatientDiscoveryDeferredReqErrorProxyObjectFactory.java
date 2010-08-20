/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author Jon Hoppesch
 */
public class AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "AdapterPatientDiscoveryAsyncReqErrorProxyConfig.xml";
    private static final String BEAN_NAME = "adapterpatientdiscoveryasyncreqerror";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterPatientDiscoveryDeferredReqErrorProxy getAdapterPatientDiscoveryDeferredReqErrorProxy() {
        return getBean(BEAN_NAME, AdapterPatientDiscoveryDeferredReqErrorProxy.class);
    }
}
