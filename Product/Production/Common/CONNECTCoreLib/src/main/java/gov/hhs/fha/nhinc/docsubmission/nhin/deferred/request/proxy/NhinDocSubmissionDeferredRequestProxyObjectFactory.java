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

package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredRequestProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRRequestProxyConfig.xml";
    private static final String BEAN_NAME = "nhinxdrrequest";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public NhinDocSubmissionDeferredRequestProxy getNhinDocSubmissionDeferredRequestProxy() {
        return getBean(BEAN_NAME, NhinDocSubmissionDeferredRequestProxy.class);
    }

}
