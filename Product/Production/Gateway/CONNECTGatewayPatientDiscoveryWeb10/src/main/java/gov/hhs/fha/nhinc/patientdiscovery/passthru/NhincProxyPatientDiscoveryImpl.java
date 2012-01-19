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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.service.WebServiceHelper;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoveryImpl extends WebServiceHelper {

    private Log log = null;

    public NhincProxyPatientDiscoveryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected NhincPatientDiscoveryOrchImpl getNhincPatientDiscoveryProcessor() {
        return new NhincPatientDiscoveryOrchImpl();
    }

    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception {
    	String messageId = getMessageId(wsContext);
    	populateAssertionWithMessageId(assertion,  messageId);
    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxyRequestType request, WebServiceContext context) {
        log.info("Entering NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV");
        PRPAIN201306UV02 response = null;

        if (request == null) {
            log.warn("request was null.");
        } else {
            NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
            if (processor != null) {
                try {
                    AssertionType assertion = request.getAssertion();
                    loadAssertion(assertion, context);
                    ProxyPRPAIN201305UVProxySecuredRequestType secureRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
                    secureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
                    secureRequest.setNhinTargetSystem(request.getNhinTargetSystem());

                    response = processor.proxyPRPAIN201305UV(secureRequest, assertion);
                } catch (Exception ex) {
                    String message = "Error occurred calling NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV. Error: " +
                            ex.getMessage();
                    log.error(message, ex);
                    throw new RuntimeException(message, ex);
                }
            } else {
                log.warn("NhincPatientDiscoveryProcessor was null.");
            }
        }

        log.info("Exiting NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV");
        return response;
    }
    
    
    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
        if (processor != null) {
            try {

                AssertionType assertion = getSamlAssertion(context);
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
