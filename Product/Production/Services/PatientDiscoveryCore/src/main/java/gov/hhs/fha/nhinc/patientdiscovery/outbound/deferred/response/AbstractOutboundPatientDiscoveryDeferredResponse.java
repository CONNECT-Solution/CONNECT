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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseOrchestratable;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 *
 */
public abstract class AbstractOutboundPatientDiscoveryDeferredResponse implements
    OutboundPatientDiscoveryDeferredResponse {

    public abstract PatientDiscoveryDeferredResponseAuditLogger getAuditLogger();

    public abstract MCCIIN000002UV01 process(PRPAIN201306UV02 request, AssertionType assertion,
        NhinTargetCommunitiesType target);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.OutboundPatientDiscoveryDeferredResponse#
     * processPatientDiscoveryAsyncResp(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType,
     * gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType)
     */
    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion,
        NhinTargetCommunitiesType target) {

        return process(request, PDMessageGeneratorUtils.getInstance().generateMessageId(assertion),
            target);
    }

    protected MCCIIN000002UV01 sendToNhin(OutboundPatientDiscoveryDeferredResponseDelegate delegate,
        PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        OutboundPatientDiscoveryDeferredResponseOrchestratable orchestratable
            = new OutboundPatientDiscoveryDeferredResponseOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request);
        orchestratable.setTarget(target);
        return ((OutboundPatientDiscoveryDeferredResponseOrchestratable) delegate.process(orchestratable))
            .getResponse();
    }

    public void auditRequest(PRPAIN201306UV02 message, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        getAuditLogger().auditRequestMessage(message, assertion,
            PDMessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE,
            null, NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
    }

    protected NhinTargetSystemType convertToNhinTargetSystemType(NhinTargetCommunitiesType targets) {
        return PDMessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
    }
}
