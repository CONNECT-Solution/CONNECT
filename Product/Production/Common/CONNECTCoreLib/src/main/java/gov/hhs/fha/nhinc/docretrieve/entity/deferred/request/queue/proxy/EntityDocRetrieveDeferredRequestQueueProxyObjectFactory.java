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
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author narendra.reddy
 */
public class EntityDocRetrieveDeferredRequestQueueProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "EntityDocRetrieveDeferredReqQueueProxyConfig.xml";
    private static final String BEAN_NAME = "entitydocretrievedeferredreqqueue";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityDocRetrieveDeferredRequestQueueProxy getDocRetrieveDeferredReqQueueProxy() {
        return getBean(BEAN_NAME, EntityDocRetrieveDeferredRequestQueueProxy.class);
    }
}
