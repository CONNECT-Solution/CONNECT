/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestOrchestratable;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class PassthroughOutboundPatientDiscoveryDeferredRequest extends AbstractOutboundPatientDiscoveryDeferredRequest {

    private static final PDMessageGeneratorUtils msgUtils = PDMessageGeneratorUtils.getInstance();

    private final OutboundPatientDiscoveryDeferredRequestDelegate delegate;
    private final PatientDiscoveryDeferredRequestAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public PassthroughOutboundPatientDiscoveryDeferredRequest() {
        auditLogger = new PatientDiscoveryDeferredRequestAuditLogger();
        delegate = new OutboundPatientDiscoveryDeferredRequestDelegate();
    }

    /**
     * Constructor.
     *
     * @param auditLogger
     * @param delegate
     */
    public PassthroughOutboundPatientDiscoveryDeferredRequest(PatientDiscoveryDeferredRequestAuditLogger auditLogger,
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
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        logInfoServiceProcess(this.getClass());

        auditRequest(request, PDMessageGeneratorUtils.getInstance().generateMessageId(assertion),
            msgUtils.convertFirstToNhinTargetSystemType(targets));
        return sendToNhin(request, assertion, targets);
    }

    @Override
    public PatientDiscoveryDeferredRequestAuditLogger getPatientDiscoveryDeferredAuditLogger() {
        return auditLogger;
    }

    private MCCIIN000002UV01 sendToNhin(PRPAIN201305UV02 message, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        NhinTargetSystemType target = msgUtils.convertFirstToNhinTargetSystemType(targets);

        OutboundPatientDiscoveryDeferredRequestOrchestratable orchestratable
        = new OutboundPatientDiscoveryDeferredRequestOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(message);
        orchestratable.setTarget(target);
        return ((OutboundPatientDiscoveryDeferredRequestOrchestratable) delegate.process(orchestratable)).getResponse();
    }
}
