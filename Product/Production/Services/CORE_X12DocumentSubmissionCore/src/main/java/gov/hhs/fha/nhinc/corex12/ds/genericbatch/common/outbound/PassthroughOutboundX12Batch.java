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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12BatchAuditLogger;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.entity.OutboundX12BatchDelegate;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.entity.OutboundX12BatchOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author cmay, svalluripalli
 */
public abstract class PassthroughOutboundX12Batch implements OutboundX12Batch {

    private X12BatchAuditLogger auditLogger = null;
    private OutboundX12BatchDelegate dsDelegate = null;

    public PassthroughOutboundX12Batch() {
        this.auditLogger = new X12BatchAuditLogger();
    }

    public PassthroughOutboundX12Batch(OutboundX12BatchDelegate dsDelegate) {
        this();
        this.dsDelegate = dsDelegate;
    }

    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        NhinTargetSystemType targetSystem = MessageGeneratorUtils.getInstance()
            .convertFirstToNhinTargetSystemType(targets);
        assertion = MessageGeneratorUtils.getInstance().generateMessageId(assertion);
        auditRequestToNhin(msg, assertion, targetSystem);

        OutboundX12BatchOrchestratable dsOrchestratable = createOrchestratable(getDelegate(), msg,
            targetSystem, assertion);

        return ((OutboundX12BatchOrchestratable) getDelegate().process(dsOrchestratable))
            .getResponse();
    }

    /**
     *
     * @param delegate
     * @param request
     * @param targetSystem
     * @param assertion
     * @return OutboundCORE_X12DSGenericBatchRequestOrchestratable
     */
    protected OutboundX12BatchOrchestratable createOrchestratable(OutboundX12BatchDelegate delegate,
        COREEnvelopeBatchSubmission request, NhinTargetSystemType targetSystem, AssertionType assertion) {

        OutboundX12BatchOrchestratable orchestratable = getOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request);
        orchestratable.setTarget(targetSystem);

        return orchestratable;
    }

    protected final void auditRequestToNhin(COREEnvelopeBatchSubmission body, AssertionType assertion,
        NhinTargetSystemType targetSystem) {

        getAuditLogger().auditRequestMessage(body, assertion, targetSystem, getMessageDirection(), getInterfaceName(),
            Boolean.TRUE, null, getServiceName());
    }

    protected X12BatchAuditLogger getAuditLogger() {
        return auditLogger;
    }

    protected final String getMessageDirection() {
        return NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
    }

    protected final String getInterfaceName() {
        return NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
    }

    protected OutboundX12BatchDelegate getDelegate() {
        return dsDelegate;
    }

    protected abstract OutboundX12BatchOrchestratable getOrchestratable(
        OutboundX12BatchDelegate delegate);

    protected abstract String getServiceName();
}
