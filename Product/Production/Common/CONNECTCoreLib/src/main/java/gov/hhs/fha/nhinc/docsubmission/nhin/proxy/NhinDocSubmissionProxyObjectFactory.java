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

package gov.hhs.fha.nhinc.docsubmission.nhin.proxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;
/**
 *
 * @author dunnek
 */
public class NhinDocSubmissionProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "NhinXDRProxyConfig.xml";
    private static final String BEAN_NAME = "nhinxdr";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public NhinDocSubmissionProxy getNhinDocSubmissionProxy() {
        return getBean(BEAN_NAME, NhinDocSubmissionProxy.class);
    }

}
