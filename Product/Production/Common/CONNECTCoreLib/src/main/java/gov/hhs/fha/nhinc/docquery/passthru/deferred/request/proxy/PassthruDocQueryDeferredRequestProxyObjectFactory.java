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
package gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentQueryDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "passthrudocquerydeferredrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocQueryDeferredRequestProxy getPassthruDocQueryDeferredRequestProxy() {
        return getBean(BEAN_NAME, PassthruDocQueryDeferredRequestProxy.class);
    }
}
