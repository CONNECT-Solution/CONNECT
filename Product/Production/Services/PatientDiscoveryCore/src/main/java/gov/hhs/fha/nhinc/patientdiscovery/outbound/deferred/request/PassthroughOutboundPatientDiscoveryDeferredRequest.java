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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestOrchestratable;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class PassthroughOutboundPatientDiscoveryDeferredRequest extends AbstractOutboundPatientDiscoveryDeferredRequest {

    private static final MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    
    private final OutboundPatientDiscoveryDeferredRequestDelegate delegate;
    private final PatientDiscoveryAuditor auditLogger;
    
    /**
     * Constructor.
     */
    public PassthroughOutboundPatientDiscoveryDeferredRequest() {
        auditLogger = new PatientDiscoveryAuditLogger();
        delegate = new OutboundPatientDiscoveryDeferredRequestDelegate();
    }

    /**
     * Constructor.
     * 
     * @param auditLogger
     * @param delegate
     */
    public PassthroughOutboundPatientDiscoveryDeferredRequest(PatientDiscoveryAuditor auditLogger,
            OutboundPatientDiscoveryDeferredRequestDelegate delegate) {
        this.auditLogger = auditLogger;
        this.delegate = delegate;
    }

    /**
     * Processes and sends the request to the Nhin.
     * 
     * @param request
     * @param assertion
     * @param targets
     * @return the MCCIIN000002UV01 response from the Nhin
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class, 
            afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class,
            serviceType = "Patient Discovery Deferred Request", version = "1.0")
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion,
            NhinTargetCommunitiesType targets) {
        
        MCCIIN000002UV01 response = sendToNhin(request, assertion, targets);

        return response;
    }
    
    @Override
    PatientDiscoveryAuditor getPatientDiscoveryAuditor() {
        return auditLogger;
    }

    private MCCIIN000002UV01 sendToNhin(PRPAIN201305UV02 message, AssertionType assertion,
            NhinTargetCommunitiesType targets) {        
        NhinTargetSystemType target = msgUtils.convertFirstToNhinTargetSystemType(targets);

        OutboundPatientDiscoveryDeferredRequestOrchestratable orchestratable = new OutboundPatientDiscoveryDeferredRequestOrchestratable(
                delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(message);
        orchestratable.setTarget(target);
        return ((OutboundPatientDiscoveryDeferredRequestOrchestratable) delegate.process(orchestratable)).getResponse();
    }
}
