/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author Jon Hoppesch
 */
public class AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory extends ComponentProxyObjectFactory implements GenericFactory<AdapterPatientDiscoveryDeferredReqErrorProxy> {
    private static final String CONFIG_FILE_NAME = "AdapterPatientDiscoveryAsyncReqErrorProxyConfig.xml";
    private static final String BEAN_NAME = "adapterpatientdiscoveryasyncreqerror";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

	@Override
	public AdapterPatientDiscoveryDeferredReqErrorProxy create() {
		return getBean(BEAN_NAME, AdapterPatientDiscoveryDeferredReqErrorProxy.class);
	}
}
