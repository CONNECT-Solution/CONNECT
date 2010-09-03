/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Adapter policy engine orchestrator proxy object factory. Used to obtain a proxy object for sending
 * messages to the adapter policy engine orchestration service.
 *
 * @author Les westberg
 */
public class AdapterPolicyEngineOrchProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterPolicyEngineOrchestratorProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR = "adapterpolicyengineorchestrator";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter policy engine orchestrator implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterpolicyengineorchestrator."
     *
     * @return AdapterPolicyEngineOrchestratorProxy instance
     */
    public AdapterPolicyEngineOrchProxy getAdapterPolicyEngineOrchProxy()
    {
        return getBean(BEAN_NAME_ADAPTER_POLICY_ENGINE_ORCHESTRATOR, AdapterPolicyEngineOrchProxy.class);
    }
}
