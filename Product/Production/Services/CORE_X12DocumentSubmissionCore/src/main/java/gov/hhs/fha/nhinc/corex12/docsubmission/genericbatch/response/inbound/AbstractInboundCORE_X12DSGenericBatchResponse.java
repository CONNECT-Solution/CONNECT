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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.COREX12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.adapter.proxy.AdapterCORE_X12DGenericBatchResponseProxy;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.adapter.proxy.AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSLargePayloadUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.log4j.Logger;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author svalluripalli
 */
public abstract class AbstractInboundCORE_X12DSGenericBatchResponse implements InboundCORE_X12DSGenericBatchResponse {

    private static final Logger LOG = Logger.getLogger(AbstractInboundCORE_X12DSGenericBatchResponse.class);
    private AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory oAdapterFactory;
    private COREX12AuditLogger auditLogger;

    /**
     *
     * @param adapterFactory
     */
    public AbstractInboundCORE_X12DSGenericBatchResponse(AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory adapterFactory, COREX12AuditLogger auditLogger) {
        oAdapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    abstract COREEnvelopeBatchSubmissionResponse processGenericBatchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion);

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion) {
        auditRequestFromNhin(msg, assertion);
        COREEnvelopeBatchSubmissionResponse oResponse = processGenericBatchSubmitTransaction(msg, assertion);
        auditResponseToNhin(oResponse, assertion);
        return oResponse;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    public COREEnvelopeBatchSubmissionResponse sendToAdapter(COREEnvelopeBatchSubmission msg,
        AssertionType assertion) {
        COREEnvelopeBatchSubmissionResponse oResponse = null;
        try {
            CORE_X12DSLargePayloadUtils.convertDataToFileLocationIfEnabled(msg);
            AdapterCORE_X12DGenericBatchResponseProxy oProxy = oAdapterFactory.getAdapterCORE_X12DocSubmissionProxy();
            oResponse = oProxy.batchSubmitTransaction(msg, assertion);
            CORE_X12DSLargePayloadUtils.convertFileLocationToDataIfEnabled(oResponse);
        } catch (LargePayloadException e) {
            LOG.error("Failed to retrieve data from the file uri in the payload.", e);
        }
        return oResponse;
    }

    protected void auditRequestFromNhin(COREEnvelopeBatchSubmission request, AssertionType assertion) {

        auditLogger.auditNhinCoreX12BatchRequest(request, assertion, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void auditResponseToNhin(COREEnvelopeBatchSubmissionResponse oResponsse, AssertionType assertion) {
        auditLogger.auditNhinCoreX12BatchRespponse(oResponsse, assertion, null, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, false);
    }

    protected void auditRequestToAdapter(COREEnvelopeBatchSubmission request, AssertionType assertion) {
        auditLogger.auditAdapterCoreX12BatchRequest(request, assertion, null, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromAdapter(COREEnvelopeBatchSubmissionResponse oResponsse, AssertionType assertion) {
        auditLogger.auditAdapterCoreX12BatchRespponse(oResponsse, assertion, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, true);
    }
}
