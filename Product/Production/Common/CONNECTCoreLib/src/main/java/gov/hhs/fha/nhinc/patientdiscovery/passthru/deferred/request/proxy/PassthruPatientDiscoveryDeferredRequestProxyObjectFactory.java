/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;


public class PassthruPatientDiscoveryDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "PassthruPatientDiscoveryAsyncReqProxyConfig.xml";
    private static final String BEAN_NAME = "passthrupatientdiscoveryasyncreq";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruPatientDiscoveryDeferredRequestProxy getPassthruPatientDiscoveryDeferredRequestProxy() {
        return getBean(BEAN_NAME, PassthruPatientDiscoveryDeferredRequestProxy.class);
    }

}
