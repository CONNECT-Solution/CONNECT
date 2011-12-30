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
package gov.hhs.fha.nhinc.patientdiscovery.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredImpl {

    private static Log log = LogFactory.getLog(NhincProxyPatientDiscoverySecuredImpl.class);

    protected NhincPatientDiscoveryOrchImpl getNhincPatientDiscoveryProcessor() {
        return new NhincPatientDiscoveryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
        // TODO: Extract message ID from the web service context for logging.
    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
        if (processor != null) {
            try {

                AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
                loadAssertion(assertion, context);

                response = processor.proxyPRPAIN201305UV(request, assertion);
            } catch (Exception ex) {
                String message = "Error occurred calling NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV. Error: " +
                        ex.getMessage();
                log.error(message, ex);
                throw new RuntimeException(message, ex);
            }
        } else {
            log.warn("NhincPatientDiscoveryProcessor was null.");
        }

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        return response;
    }
}
