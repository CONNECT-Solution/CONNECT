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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12BatchSubmissionAuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy.AdapterCORE_X12DGenericBatchRequestProxy;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy.AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSLargePayloadUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author svalluripalli
 */
public abstract class AbstractInboundCORE_X12DSGenericBatchRequest implements InboundCORE_X12DSGenericBatchRequest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractInboundCORE_X12DSGenericBatchRequest.class);
    private AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory oAdapterFactory;
    private CORE_X12BatchSubmissionAuditLogger auditLogger;

    /**
     *
     * @param adapterFactory
     */
    public AbstractInboundCORE_X12DSGenericBatchRequest(AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory adapterFactory, CORE_X12BatchSubmissionAuditLogger auditLogger) {
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
        AssertionType assertion, Properties webContextProperties) {
        COREEnvelopeBatchSubmissionResponse oResponse = processGenericBatchSubmitTransaction(msg, assertion);
        auditResponseToNhin(msg, oResponse, assertion, webContextProperties);
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
            AdapterCORE_X12DGenericBatchRequestProxy oProxy = oAdapterFactory.getAdapterCORE_X12DocSubmissionProxy();
            oResponse = oProxy.batchSubmitTransaction(msg, assertion);
            CORE_X12DSLargePayloadUtils.convertFileLocationToDataIfEnabled(oResponse);
        } catch (LargePayloadException e) {
            LOG.error("Failed to retrieve data from the file uri in the payload.", e);
        }
        return oResponse;
    }

    protected void auditResponseToNhin(COREEnvelopeBatchSubmission request,
        COREEnvelopeBatchSubmissionResponse oResponsse, AssertionType assertion, Properties webContextProperties) {
        auditLogger.auditResponseMessage(request, oResponsse, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME);
    }
}
