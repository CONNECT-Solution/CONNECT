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

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public abstract class AbstractInboundDocSubmission implements InboundDocSubmission {

    abstract RegistryResponseType processDocSubmission(ProvideAndRegisterDocumentSetRequestType body,
            AssertionType assertion);

    private XDRAuditLogger auditLogger;
    private AdapterDocSubmissionProxyObjectFactory adapterFactory;
    
    public AbstractInboundDocSubmission(AdapterDocSubmissionProxyObjectFactory adapterFactory,
            XDRAuditLogger auditLogger) {
        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    @InboundProcessingEvent(beforeBuilder = DocSubmissionBaseEventDescriptionBuilder.class,
            afterReturningBuilder = DocSubmissionBaseEventDescriptionBuilder.class, serviceType = "Document Submission",
            version = "")
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
            ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {

        auditRequestFromNhin(body, assertion);
        
        RegistryResponseType response = processDocSubmission(body, assertion);

        auditResponseToNhin(response, assertion);

        return response;
    }
    
    protected RegistryResponseType sendToAdapter(ProvideAndRegisterDocumentSetRequestType request,
            AssertionType assertion) {
        AdapterDocSubmissionProxy proxy = adapterFactory.getAdapterDocSubmissionProxy();
        return proxy.provideAndRegisterDocumentSetB(request, assertion);
    }

    protected void auditRequestFromNhin(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        auditLogger.auditNhinXDR(request, assertion, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void auditResponseToNhin(RegistryResponseType response, AssertionType assertion) {
        auditLogger.auditNhinXDRResponse(response, assertion, null, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, false);
    }
    
    protected void auditRequestToAdapter(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        auditLogger.auditAdapterXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromAdapter(RegistryResponseType response, AssertionType assertion) {
        auditLogger.auditAdapterXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }
    
}
