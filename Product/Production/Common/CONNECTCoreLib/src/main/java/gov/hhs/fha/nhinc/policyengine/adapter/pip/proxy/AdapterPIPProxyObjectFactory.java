/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Adapter PIP Proxy object factory. Used to obtain a proxy object for sending
 * messages to the adapter PIP service.
 *
 * @author Les westberg
 */
public class AdapterPIPProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterPIPConfig.xml";
    private static final String BEAN_NAME_ADAPTER_PIP = "adapterpip";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter PIP implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterpip."
     *
     * @return AdapterPIPProxy instance
     */
    public AdapterPIPProxy getAdapterPIPProxy()
    {
        return getBean(BEAN_NAME_ADAPTER_PIP, AdapterPIPProxy.class);
    }
}
