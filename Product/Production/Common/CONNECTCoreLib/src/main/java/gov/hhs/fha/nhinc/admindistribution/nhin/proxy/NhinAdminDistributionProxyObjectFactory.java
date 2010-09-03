/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;
/**
 *
 * @author dunnek
 */
public class NhinAdminDistributionProxyObjectFactory extends ComponentProxyObjectFactory{

    private static final String CONFIG_FILE_NAME = "NhinAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_NHIN_ADMIN_DIST = "nhinadmindist";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }

    /**
     * Retrieve an adapter audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "nhincadmindist."
     *
     * @return AdapterAuditQueryProxy instance
     */
    public NhinAdminDistributionProxy getNhinAdminDistProxy() {
        log.debug("Begin getNhinAdminDistProxy()");
        
        return getBean(BEAN_NAME_NHIN_ADMIN_DIST, NhinAdminDistributionProxy.class);
    }
}
