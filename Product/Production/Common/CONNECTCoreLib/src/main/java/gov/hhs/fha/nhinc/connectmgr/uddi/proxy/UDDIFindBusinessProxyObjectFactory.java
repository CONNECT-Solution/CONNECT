/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author richard.ettema
 */
public class UDDIFindBusinessProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "UDDIFindBusinessProxyConfig.xml";
    private static final String BEAN_NAME = "uddifindbusiness";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     *
     * @return instance of <code>UDDIFindBusinessProxy</code>
     */
    public UDDIFindBusinessProxy getUDDIBusinessInfoProxy() {
        return getBean(BEAN_NAME, UDDIFindBusinessProxy.class);
    }

}
