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

package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;
/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyObjectFactory  extends ComponentProxyObjectFactory{


    private static final String CONFIG_FILE_NAME = "AdapterAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_ADAPTER_ADMIN_DIST = "adapteradmindist";


    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }
    public AdapterAdminDistributionProxy getAdapterAdminDistProxy() {
        log.debug("Begin getAdapterAdminDistProxy()");

        AdapterAdminDistributionProxy result = null;
        
        log.debug("Getting bean");
        return getBean(BEAN_NAME_ADAPTER_ADMIN_DIST, AdapterAdminDistributionProxy.class);
        
    }
}
