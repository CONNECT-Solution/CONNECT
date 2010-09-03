/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;


public class EntityDocQueryProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentQueryProxyConfig.xml";
    private static final String BEAN_NAME = "entitydocquery";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityDocQueryProxy getEntityDocQueryProxy() {
        return getBean(BEAN_NAME, EntityDocQueryProxy.class);
    }

}
