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
package gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.COREX12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.genericbatch.request.adapter.proxy.AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 * @author svalluripalli
 *
 */
public class PassthroughInboundCORE_X12DSGenericBatchRequest extends AbstractInboundCORE_X12DSGenericBatchRequest {

    /**
     * Default constructor.
     */
    public PassthroughInboundCORE_X12DSGenericBatchRequest() {
        this(new AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory(), new COREX12AuditLogger());
    }

    /**
     * Constructor with dependency injection of strategy components.
     *
     * @param adapterFactory
     */
    public PassthroughInboundCORE_X12DSGenericBatchRequest(AdapterCORE_X12DSGenericBatchRequestProxyObjectFactory adapterFactory, COREX12AuditLogger auditLogger) {
        super(adapterFactory, auditLogger);
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return COREEnvelopeBatchSubmissionResponse
     */
    @Override
    COREEnvelopeBatchSubmissionResponse processGenericBatchSubmitTransaction(COREEnvelopeBatchSubmission msg, AssertionType assertion) {
        return sendToAdapter(msg, assertion);
    }

}
