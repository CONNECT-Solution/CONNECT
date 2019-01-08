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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.request.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12BatchAuditLogger;
import gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.adapter.proxy.AdapterX12BatchProxyObjectFactory;
import gov.hhs.fha.nhinc.corex12.ds.utils.X12LargePayloadUtils;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay, svalluripalli
 */
public abstract class AbstractInboundX12Batch implements InboundX12Batch {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractInboundX12Batch.class);
    private final AdapterX12BatchProxyObjectFactory adapterFactory;
    private final X12BatchAuditLogger auditLogger;

    /**
     *
     * @param adapterFactory
     * @param auditLogger
     */
    public AbstractInboundX12Batch(AdapterX12BatchProxyObjectFactory adapterFactory,
        X12BatchAuditLogger auditLogger) {

        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    protected abstract COREEnvelopeBatchSubmissionResponse processGenericBatchSubmitTransaction(
        COREEnvelopeBatchSubmission msg, AssertionType assertion);

    /**
     *
     * @param msg
     * @param assertion
     * @param webContextProperties
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    public COREEnvelopeBatchSubmissionResponse batchSubmitTransaction(COREEnvelopeBatchSubmission msg,
        AssertionType assertion, Properties webContextProperties) {

        COREEnvelopeBatchSubmissionResponse response = processGenericBatchSubmitTransaction(msg, assertion);
        auditResponseToNhin(msg, response, assertion, webContextProperties);
        return response;
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    public COREEnvelopeBatchSubmissionResponse sendToAdapter(COREEnvelopeBatchSubmission msg,
        AssertionType assertion) {

        COREEnvelopeBatchSubmissionResponse response = null;
        try {
            X12LargePayloadUtils.convertDataToFileLocationIfEnabled(msg);
            response = adapterFactory.getAdapterCOREX12DocSubmissionProxy().batchSubmitTransaction(msg, assertion);
            X12LargePayloadUtils.convertFileLocationToDataIfEnabled(response);
        } catch (LargePayloadException e) {
            LOG.error("Failed to retrieve data from the file uri in the payload: {}", e.getLocalizedMessage(), e);
        }
        return response;
    }

    protected void auditResponseToNhin(COREEnvelopeBatchSubmission request,
        COREEnvelopeBatchSubmissionResponse response, AssertionType assertion, Properties webContextProperties) {

        auditLogger.auditResponseMessage(request, response, assertion, null, getDirection(), getInterfaceName(),
            Boolean.FALSE, webContextProperties, getServiceName());
    }

    protected final String getDirection() {
        return NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
    }

    protected final String getInterfaceName() {
        return NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
    }

    protected abstract String getServiceName();
}
