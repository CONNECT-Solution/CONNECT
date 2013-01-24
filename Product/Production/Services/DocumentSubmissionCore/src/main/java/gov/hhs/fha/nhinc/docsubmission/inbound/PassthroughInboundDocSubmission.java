/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.log4j.Logger;

/**
 * @author akong
 * 
 */
public class PassthroughInboundDocSubmission extends AbstractInboundDocSubmission {

    private static final Logger LOG = Logger.getLogger(PassthroughInboundDocSubmission.class);
    private MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private DocSubmissionUtils dsUtils;
    
    /**
     * Constructor.
     */
    public PassthroughInboundDocSubmission() {
        this(new AdapterDocSubmissionProxyObjectFactory(), new XDRAuditLogger(), DocSubmissionUtils.getInstance());
    }

    /**
     * Constructor with dependency injection of strategy components.
     * 
     * @param adapterFactory
     * @param auditLogger
     * @param dsUtils
     */
    public PassthroughInboundDocSubmission(AdapterDocSubmissionProxyObjectFactory adapterFactory,
            XDRAuditLogger auditLogger, DocSubmissionUtils dsUtils) {
        super(adapterFactory, auditLogger);
        this.dsUtils = dsUtils;
    }

    @Override
    RegistryResponseType processDocSubmission(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        RegistryResponseType response = null;

        try {
            dsUtils.convertDataToFileLocationIfEnabled(body);
            response = sendToAdapter(body, assertion);
        } catch (LargePayloadException lpe) {
            LOG.error("Failed to retrieve payload document.", lpe);
            response = msgUtils.createRegistryErrorResponse();
        }

        return response;
    }

}
