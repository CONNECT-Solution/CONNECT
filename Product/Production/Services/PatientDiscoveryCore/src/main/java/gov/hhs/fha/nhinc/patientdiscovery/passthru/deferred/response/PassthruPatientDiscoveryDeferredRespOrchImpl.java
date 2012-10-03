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
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseOrchestratable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * 
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespOrchImpl {

    private static Log log = LogFactory.getLog(PassthruPatientDiscoveryDeferredRespOrchImpl.class);

    /**
     * 
     * @param request
     * @param assertion
     * @param targetSystem
     * @return Patient Discovery Response Acknowledgement
     */
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion,
            NhinTargetSystemType targetSystem) {
        log.debug("Begin - proxyProcessPatientDiscoveryAsyncResp");

        auditRequestToNhin(request, assertion);
        
        OutboundPatientDiscoveryDeferredResponseDelegate pdRespDelegate = new OutboundPatientDiscoveryDeferredResponseDelegate();
        OutboundPatientDiscoveryDeferredResponseOrchestratable pdRespOrchestratable = new OutboundPatientDiscoveryDeferredResponseOrchestratable(
                pdRespDelegate);
        pdRespOrchestratable.setAssertion(assertion);
        pdRespOrchestratable.setRequest(request);
        pdRespOrchestratable.setTarget(targetSystem);
        MCCIIN000002UV01 response = ((OutboundPatientDiscoveryDeferredResponseOrchestratable) pdRespDelegate
                .process(pdRespOrchestratable)).getResponse();
        
        
        auditResponseFromNhin(response, assertion);
        
        log.debug("End - proxyProcessPatientDiscoveryAsyncResp");

        return response;
    }
    
    protected void auditRequestToNhin(PRPAIN201306UV02 request, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditNhinDeferred201306(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromNhin(MCCIIN000002UV01 resp, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }
}
