/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.entity.OutboundCORE_X12DSGenericBatchResponseDelegate;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.entity.OutboundCORE_X12DSGenericBatchResponseOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 * @author svalluripalli
 *
 */
public class PassthroughOutboundCORE_X12DSGenericBatchResponse implements OutboundCORE_X12DSGenericBatchResponse {

    private final OutboundCORE_X12DSGenericBatchResponseDelegate dsDelegate = new OutboundCORE_X12DSGenericBatchResponseDelegate();
    private CORE_X12AuditLogger auditLogger = new CORE_X12AuditLogger();

    /**
     *
     * @param msg
     * @param assertion
     * @param targets
     * @param urlInfo
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg, AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {
        NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(targets);
        this.auditRequestToNhin(msg, assertion, targetSystem);
        COREEnvelopeBatchSubmissionResponse oResponse;
        OutboundCORE_X12DSGenericBatchResponseOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, msg, targetSystem, assertion);
        oResponse = ((OutboundCORE_X12DSGenericBatchResponseOrchestratable) dsDelegate.process(dsOrchestratable)).getResponse();
        this.auditResponseFromNhin(oResponse, assertion, targetSystem);
        return oResponse;
    }

    /**
     *
     * @param delegate
     * @param request
     * @param targetSystem
     * @param assertion
     * @return OutboundCORE_X12DSGenericBatchRequestOrchestratable
     */
    public OutboundCORE_X12DSGenericBatchResponseOrchestratable createOrchestratable(OutboundCORE_X12DSGenericBatchResponseDelegate delegate,
        COREEnvelopeBatchSubmission request,
        NhinTargetSystemType targetSystem,
        AssertionType assertion) {

        OutboundCORE_X12DSGenericBatchResponseOrchestratable core_x12dsOrchestratable = new OutboundCORE_X12DSGenericBatchResponseOrchestratable(delegate);
        core_x12dsOrchestratable.setAssertion(assertion);
        core_x12dsOrchestratable.setRequest(request);
        core_x12dsOrchestratable.setTarget(targetSystem);
        return core_x12dsOrchestratable;
    }

    private void auditRequestToNhin(COREEnvelopeBatchSubmission body, AssertionType assertion,
        NhinTargetSystemType targetSystem) {
        auditLogger.auditNhinCoreX12BatchMessage(body, assertion, targetSystem, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, Boolean.TRUE, null,NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME);
    }

    private void auditResponseFromNhin(COREEnvelopeBatchSubmissionResponse body, AssertionType assertion,
        NhinTargetSystemType targetSystem) {
        auditLogger.auditNhinCoreX12BatchMessage(body, assertion, targetSystem, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, Boolean.TRUE, null,NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME);
    }
}
