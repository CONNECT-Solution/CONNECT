/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Object factory to retrieve an AdapterPDPProxy instance.
 * 
 * @author Neil Webb
 */
public class AdapterPDPProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterPDPConfig.xml";
    private static final String BEAN_NAME = "adapterpdp";

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory#getConfigFileName()
	 */
	@Override
	protected String getConfigFileName()
	{
		return CONFIG_FILE_NAME;
	}

    /**
     * Retrieve an adapter PDP implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterpdp."
     *
     * @return AdapterPDPProxy instance
     */
    public AdapterPDPProxy getAdapterPDPProxy()
    {
        return getBean(BEAN_NAME, AdapterPDPProxy.class);
    }
}
