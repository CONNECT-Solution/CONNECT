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
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author narendra.reddy
 */
public class AdapterDocRetrieveDeferredRequestQueueProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "AdapterDocRetrieveDeferredReqQueueProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocretrievedeferredreqqueue";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocRetrieveDeferredRequestQueueProxy getDocRetrieveDeferredReqQueueProxy() {
        return getBean(BEAN_NAME, AdapterDocRetrieveDeferredRequestQueueProxy.class);
    }
}
