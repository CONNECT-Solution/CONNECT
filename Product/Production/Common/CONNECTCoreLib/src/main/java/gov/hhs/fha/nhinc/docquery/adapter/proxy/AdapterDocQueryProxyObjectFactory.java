/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * @author svalluripalli
 */
public class AdapterDocQueryProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "AdapterDocQueryProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocquery";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocQueryProxy getAdapterDocQueryProxy() {
        return getBean(BEAN_NAME, AdapterDocQueryProxy.class);
    }

}
