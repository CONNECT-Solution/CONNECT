/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author narendra.reddy
 */
public class AdapterDocQueryDeferredRequestQueueProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "AdapterDocQueryDeferredReqQueueProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocquerydeferredreqqueue";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocQueryDeferredRequestQueueProxy getDocQueryDeferredReqQueueProxy() {
        return getBean(BEAN_NAME, AdapterDocQueryDeferredRequestQueueProxy.class);
    }

}