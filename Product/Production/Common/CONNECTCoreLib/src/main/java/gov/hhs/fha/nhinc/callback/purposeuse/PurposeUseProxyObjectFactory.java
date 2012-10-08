/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented
 * by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Purpose Use proxy object factory. Used to obtain a proxy object for
 * creating a purposeOfUse or purposeForUse based on specifications
 */
public class PurposeUseProxyObjectFactory extends ComponentProxyObjectFactory {
    
    private static final String CONFIG_FILE_NAME = "PurposeUseProxyConfig.xml";
    private static final String BEAN_NAME_PURPOSE_USE = "purposeuse";

    /**
     * Returns the name of the config file.
     * @return the config file name
     */
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve a purpose use engine implementation.
     * This method retrieves the object from the framework that has an
     * identifier of "purposeuse."
     *
     * @return PurposeUseProxy instance
     */
    public PurposeUseProxy getPurposeUseProxy() {
        return getBean(BEAN_NAME_PURPOSE_USE, PurposeUseProxy.class);
    }
}
