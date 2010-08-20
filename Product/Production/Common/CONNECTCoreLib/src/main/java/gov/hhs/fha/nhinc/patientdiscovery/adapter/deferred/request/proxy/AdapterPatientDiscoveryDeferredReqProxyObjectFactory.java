/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author Jon Hoppesch
 */
public class AdapterPatientDiscoveryDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "AdapterPatientDiscoveryAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "adapterpatientdiscoveryasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterPatientDiscoveryDeferredReqProxy getAdapterPatientDiscoveryDeferredReqProxy() {
        return getBean(BEAN_NAME, AdapterPatientDiscoveryDeferredReqProxy.class);
    }
}
