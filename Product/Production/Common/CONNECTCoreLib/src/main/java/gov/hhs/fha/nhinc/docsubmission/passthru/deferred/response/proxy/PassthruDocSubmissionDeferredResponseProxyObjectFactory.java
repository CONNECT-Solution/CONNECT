/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocSubmissionDeferredResponseProxyObjectFactory extends ComponentProxyObjectFactory
{
    private static final String CONFIG_FILE_NAME = "PassthruXDRAsyncRespProxyConfig.xml";
    private static final String BEAN_NAME = "passthruxdrasyncresp";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public PassthruDocSubmissionDeferredResponseProxy getPassthruDocSubmissionDeferredResponseProxy() {
        return getBean(BEAN_NAME, PassthruDocSubmissionDeferredResponseProxy.class);
    }
}
