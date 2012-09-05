/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.patientdiscovery._10.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.PassthruPatientDiscoveryDeferredRequestOrchImpl;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

public class NhincProxyPatientDiscoveryDeferredRequestImpl {

    private Log log = null;

    public NhincProxyPatientDiscoveryDeferredRequestImpl() {
        log = createLogger();
    }

    public Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

  
    protected PassthruPatientDiscoveryDeferredRequestOrchImpl createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl() {
        return new PassthruPatientDiscoveryDeferredRequestOrchImpl();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncRequestSecured(
            ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        log.info("Begin processPatientDiscoveryAsyncRequestSecured(ProxyPRPAIN201305UVProxySecuredRequestType, WebServiceContext)");
        PassthruPatientDiscoveryDeferredRequestOrchImpl implOrch = createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try {
            if (request != null) {
                PRPAIN201305UV02 message = request.getPRPAIN201305UV02();
                NhinTargetSystemType targets = request.getNhinTargetSystem();
                response = implOrch.processPatientDiscoveryAsyncReq(message, extractAssertion(context),targets);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                            + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " + "Exception: "
                            + e.getMessage(), e);
        }
        return response;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncRequestUnsecured(ProxyPRPAIN201305UVProxyRequestType request,
            WebServiceContext context) {
        log.info("Begin processPatientDiscoveryAsyncRequestSecured(ProxyPRPAIN201305UVProxyRequestType, WebServiceContext)");
        PassthruPatientDiscoveryDeferredRequestOrchImpl implOrch = createPassthruProxyPatientDiscoveryDeferredRequestOrchImpl();
        MCCIIN000002UV01 response = null;

        try {
            if (request != null) {
                PRPAIN201305UV02 message = request.getPRPAIN201305UV02();
                NhinTargetSystemType targets = request.getNhinTargetSystem();
                AssertionType assertion = request.getAssertion();
                response = (MCCIIN000002UV01) implOrch.processPatientDiscoveryAsyncReq(message, assertion, targets);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".processPatientDiscoveryAsyncReq).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                            + ".processPatientDiscoveryAsyncReq).  An unexpected exception occurred.  " + "Exception: "
                            + e.getMessage(), e);
        }
        return response;
    }
    
    protected AssertionType extractAssertion(WebServiceContext context) {
        return SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
    }
}
