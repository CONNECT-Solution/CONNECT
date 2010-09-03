/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:28:35 PM
 */
public class NhinDocRetrieveDeferredReqProxyObjectFactory extends ComponentProxyObjectFactory {

    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "nhindocretrievedeferredrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public NhinDocRetrieveDeferredReqProxy getNhinDocRetrieveDeferredRequestProxy() {
        return getBean(BEAN_NAME, NhinDocRetrieveDeferredReqProxy.class);
    }
}
