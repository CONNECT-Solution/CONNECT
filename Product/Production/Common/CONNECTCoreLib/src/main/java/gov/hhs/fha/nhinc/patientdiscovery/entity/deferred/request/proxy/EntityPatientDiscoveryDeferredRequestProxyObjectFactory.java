package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;


public class EntityPatientDiscoveryDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "EntityPatientDiscoveryAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "entitypatientdiscoveryasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityPatientDiscoveryDeferredRequestProxy getEntityPatientDiscoveryDeferredRequestProxy() {
        return getBean(BEAN_NAME, EntityPatientDiscoveryDeferredRequestProxy.class);
    }

}
