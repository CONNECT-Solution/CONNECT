/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Adapter policy engine proxy object factory. Used to obtain a proxy object for sending
 * messages to the adapter policy engine service.
 */
public class PolicyEngineProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "PolicyEngineProxyConfig.xml";
    private static final String BEAN_NAME_POLICY_ENGINE = "policyengine";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve a policy engine implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "policyengine."
     *
     * @return PolicyEngineProxy instance
     */
    public PolicyEngineProxy getPolicyEngineProxy()
    {
        return getBean(BEAN_NAME_POLICY_ENGINE, PolicyEngineProxy.class);
    }
}
