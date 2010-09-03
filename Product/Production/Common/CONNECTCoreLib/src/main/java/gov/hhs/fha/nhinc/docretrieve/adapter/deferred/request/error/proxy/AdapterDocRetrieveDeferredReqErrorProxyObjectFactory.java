/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:28:35 PM
 */
public class AdapterDocRetrieveDeferredReqErrorProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "DocumentRetrieveDeferredProxyConfig.xml";
    private static final String BEAN_NAME = "adapterdocretrievedeferredrequesterror";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public AdapterDocRetrieveDeferredReqErrorProxy getAdapterDocRetrieveDeferredRequestErrorProxy() {
        return getBean(BEAN_NAME, AdapterDocRetrieveDeferredReqErrorProxy.class);
    }
}
