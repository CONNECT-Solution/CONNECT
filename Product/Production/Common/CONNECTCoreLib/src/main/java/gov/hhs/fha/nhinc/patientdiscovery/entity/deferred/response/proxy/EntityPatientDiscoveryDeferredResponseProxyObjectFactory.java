/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Les Westberg
 */
public class EntityPatientDiscoveryDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory
{

    private static final String CONFIG_FILE_NAME = "EntityPatientDiscoveryAsyncRespProxyConfig.xml";
    private static final String BEAN_NAME = "entitypatientdiscoveryasyncresp";

    /**
     * Returns the name of the config file.
     *
     * @return The name of the config file.
     */
    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Return an instance of the NhinPatientDiscoveryProxy class.
     *
     * @return An instance of the NhinPatientDiscoveryProxy class.
     */
    public EntityPatientDiscoveryDeferredResponseProxy getNhincPatientDiscoveryProxy()
    {

        return getBean(BEAN_NAME, EntityPatientDiscoveryDeferredResponseProxy.class);
    }
}
