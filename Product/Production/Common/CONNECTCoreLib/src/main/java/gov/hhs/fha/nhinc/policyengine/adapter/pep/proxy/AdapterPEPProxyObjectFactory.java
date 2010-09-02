/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pep.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Adapter PEP Proxy object factory. Used to obtain a proxy object for sending
 * messages to the adapter PEP service.
 *
 */
public class AdapterPEPProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterPEPConfig.xml";
    private static final String BEAN_NAME_ADAPTER_PEP = "adapterpep";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter PEP implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterpep."
     *
     * @return AdapterPEPProxy instance
     */
    public AdapterPEPProxy getAdapterPEPProxy()
    {
        return getBean(BEAN_NAME_ADAPTER_PEP, AdapterPEPProxy.class);
    }
}
