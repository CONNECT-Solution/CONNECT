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
package gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class PassthroughInboundDocSubmissionDeferredRequest extends AbstractInboundDocSubmissionDeferredRequest {

    private static final Logger LOG = LoggerFactory.getLogger(PassthroughInboundDocSubmissionDeferredRequest.class);

    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private DocSubmissionUtils dsUtils;

    /**
     * Constructor.
     */
    public PassthroughInboundDocSubmissionDeferredRequest() {
        this(new AdapterDocSubmissionDeferredRequestProxyObjectFactory(), new DocSubmissionDeferredRequestAuditLogger(),
            DocSubmissionUtils.getInstance());
    }

    /**
     * Constructor with dependency injection of strategy components.
     *
     * @param adapterFactory
     * @param auditLogger
     * @param dsUtils
     */
    public PassthroughInboundDocSubmissionDeferredRequest(
        AdapterDocSubmissionDeferredRequestProxyObjectFactory adapterFactory,
        DocSubmissionDeferredRequestAuditLogger auditLogger,
        DocSubmissionUtils dsUtils) {
        super(adapterFactory, auditLogger);
        this.dsUtils = dsUtils;
    }

    @Override
    public XDRAcknowledgementType processDocSubmissionRequest(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion) {
        logInfoServiceProcess(this.getClass());

        XDRAcknowledgementType response;

        try {
            dsUtils.convertDataToFileLocationIfEnabled(body);
            response = sendToAdapter(body, assertion);
        } catch (LargePayloadException lpe) {
            LOG.error("Failed to retrieve payload document. " + lpe.getLocalizedMessage(), lpe);
            response = msgUtils.createXDRAckWithRegistryErrorResponse();
        }

        return response;
    }
}
