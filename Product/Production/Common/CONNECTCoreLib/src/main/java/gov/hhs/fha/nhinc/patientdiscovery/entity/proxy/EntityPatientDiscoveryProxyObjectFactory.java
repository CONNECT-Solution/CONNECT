/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Entity patient discovery proxy object factory. Used to obtain a proxy object for sending
 * patient discovery messages to the gateway using the entity interface.
 * 
 * @author Neil Webb
 */
public class EntityPatientDiscoveryProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "EntityPatientDiscoveryProxyConfig.xml";
    private static final String BEAN_NAME_ENTITY_PATIENT_DISCOVERY = "entitypatientdiscovery";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    public EntityPatientDiscoveryProxy getEntityPatientDiscoveryProxy()
    {
        return getBean(BEAN_NAME_ENTITY_PATIENT_DISCOVERY, EntityPatientDiscoveryProxy.class);
   }

}
