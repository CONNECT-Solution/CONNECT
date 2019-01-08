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
package gov.hhs.fha.nhinc.docsubmission.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public abstract class AbstractInboundDocSubmission implements InboundDocSubmission {

    public abstract RegistryResponseType processDocSubmission(ProvideAndRegisterDocumentSetRequestType body,
        AssertionType assertion, Properties webContextProperties);

    private DocSubmissionAuditLogger auditLogger = null;
    private AdapterDocSubmissionProxyObjectFactory adapterFactory = null;

    public AbstractInboundDocSubmission(AdapterDocSubmissionProxyObjectFactory adapterFactory,
        DocSubmissionAuditLogger auditLogger) {

        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    @Override
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
        ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, Properties webContextProperties) {
        RegistryResponseType response = null;
        try {
             response = processDocSubmission(body, assertion, webContextProperties);
        }
        catch (ErrorEventException e) {
            // only doing this for audit response.
            response = (RegistryResponseType) e.getReturnOverride();
            throw e;
        } finally {
            auditResponse(body, response, assertion, webContextProperties);
        }

        return response;
    }

    protected RegistryResponseType sendToAdapter(ProvideAndRegisterDocumentSetRequestType request,
        AssertionType assertion) {
        AdapterDocSubmissionProxy proxy = adapterFactory.getAdapterDocSubmissionProxy();
        return proxy.provideAndRegisterDocumentSetB(request, assertion);
    }

    protected void auditResponse(ProvideAndRegisterDocumentSetRequestType request, RegistryResponseType response,
        AssertionType assertion, Properties webContextProperties) {
        auditLogger.auditResponseMessage(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.NHINC_XDR_SERVICE_NAME);
    }
}
