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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12BatchSubmissionAuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.adapter.proxy.AdapterCORE_X12DGenericBatchResponseProxy;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.response.adapter.proxy.AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.corex12.docsubmission.utils.CORE_X12DSLargePayloadUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
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
    private CORE_X12BatchSubmissionAuditLogger auditLogger;

    /**
     *
     * @param adapterFactory
     * @param auditLogger
     */
    public AbstractInboundCORE_X12DSGenericBatchResponse(
        AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory adapterFactory,
        CORE_X12BatchSubmissionAuditLogger auditLogger) {
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
     * @param webContextProperties
     *
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
            AdapterCORE_X12DGenericBatchResponseProxy oProxy
                = getProxyObjectFactory().getAdapterCORE_X12DocSubmissionProxy();
            oResponse = oProxy.batchSubmitTransaction(msg, assertion);
            CORE_X12DSLargePayloadUtils.convertFileLocationToDataIfEnabled(oResponse);
        } catch (LargePayloadException e) {
            LOG.error("Failed to retrieve data from the file uri in the payload.", e);
        }
        return oResponse;
    }

    protected void auditResponseToNhin(COREEnvelopeBatchSubmission request,
        COREEnvelopeBatchSubmissionResponse oResponse, AssertionType assertion, Properties webContextProperties) {
        getAuditLogger().auditResponseMessage(request, oResponse, assertion, null,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME);
    }

    protected CORE_X12BatchSubmissionAuditLogger getAuditLogger() {
        if (auditLogger == null) {
            auditLogger = new CORE_X12BatchSubmissionAuditLogger();
        }
        return auditLogger;
    }

    protected AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory getProxyObjectFactory() {
        if (oAdapterFactory == null) {
            oAdapterFactory = new AdapterCORE_X12DSGenericBatchResponseProxyObjectFactory();
        }
        return oAdapterFactory;
    }
}
