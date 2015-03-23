/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.COREX12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.OutboundCORE_X12DSRealTimeDelegate;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.OutboundCORE_X12DSRealTimeOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 * @author cmay
 *
 */
public class PassthroughOutboundCORE_X12DSRealTime implements OutboundCORE_X12DSRealTime {

    private OutboundCORE_X12DSRealTimeDelegate dsDelegate = new OutboundCORE_X12DSRealTimeDelegate();
    private COREX12AuditLogger auditLogger = new COREX12AuditLogger();

    public PassthroughOutboundCORE_X12DSRealTime() {
        super();
    }

    /**
     *
     * @param dsDelegate
     * @param auditLogger
     */
    public PassthroughOutboundCORE_X12DSRealTime(OutboundCORE_X12DSRealTimeDelegate dsDelegate, COREX12AuditLogger auditLogger) {
        this.dsDelegate = dsDelegate;
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param body
     * @param assertion
     * @param targets
     * @param urlInfo
     * @return COREEnvelopeRealTimeResponse
     */
    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest body, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(
            targets);
        this.auditRequestToNhin(body, assertion, targetSystem);
        OutboundCORE_X12DSRealTimeOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, body, targetSystem, assertion);
        COREEnvelopeRealTimeResponse response = ((OutboundCORE_X12DSRealTimeOrchestratable) dsDelegate.process(dsOrchestratable))
            .getResponse();
        this.auditResponseFromNhin(response, assertion, targetSystem);
        return response;
    }

    private OutboundCORE_X12DSRealTimeOrchestratable createOrchestratable(OutboundCORE_X12DSRealTimeDelegate delegate,
        COREEnvelopeRealTimeRequest request, NhinTargetSystemType targetSystem, AssertionType assertion) {

        OutboundCORE_X12DSRealTimeOrchestratable core_x12dsOrchestratable = new OutboundCORE_X12DSRealTimeOrchestratable(delegate);
        core_x12dsOrchestratable.setAssertion(assertion);
        core_x12dsOrchestratable.setRequest(request);
        core_x12dsOrchestratable.setTarget(targetSystem);

        return core_x12dsOrchestratable;
    }

    private void auditRequestToNhin(COREEnvelopeRealTimeRequest body, AssertionType assertion,
        NhinTargetSystemType targetSystem) {
        auditLogger.auditCoreX12RealtimeRequest(body, assertion, targetSystem, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void auditResponseFromNhin(COREEnvelopeRealTimeResponse body, AssertionType assertion,
        NhinTargetSystemType targetSystem) {
        auditLogger.auditCoreX12RealtimeRespponse(body, assertion, targetSystem, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, true);
    }
}
