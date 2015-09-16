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
package gov.hhs.fha.nhinc.docsubmission.inbound;

import java.lang.reflect.Method;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class PassthroughInboundDocSubmissionTest {

    @Test
    public void passthroughInboundDocSubmission() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        RegistryResponseType expectedResponse = new RegistryResponseType();

        AdapterDocSubmissionProxyObjectFactory adapterFactory = mock(AdapterDocSubmissionProxyObjectFactory.class);
        AdapterDocSubmissionProxy adapterProxy = mock(AdapterDocSubmissionProxy.class);
        DocSubmissionAuditLogger auditLogger = mock(DocSubmissionAuditLogger.class);
        DocSubmissionUtils dsUtils = mock(DocSubmissionUtils.class);
        Properties webContextProperties = new Properties();
        when(adapterFactory.getAdapterDocSubmissionProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetB(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
            auditLogger, dsUtils);

        RegistryResponseType actualResponse = passthroughDocSubmission
            .documentRepositoryProvideAndRegisterDocumentSetB(request, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(auditLogger).auditRequestMessage(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.FALSE), eq(webContextProperties), eq(NhincConstants.NHINC_XDR_SERVICE_NAME));
        verify(auditLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_SERVICE_NAME));
    }

    @Test
    public void convertDataToFileError() throws LargePayloadException {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();

        AdapterDocSubmissionProxyObjectFactory adapterFactory = mock(AdapterDocSubmissionProxyObjectFactory.class);
        DocSubmissionAuditLogger auditLogger = mock(DocSubmissionAuditLogger.class);
        DocSubmissionUtils dsUtils = mock(DocSubmissionUtils.class);
        Properties webContextProperties = new Properties();
        doThrow(new LargePayloadException()).when(dsUtils).convertDataToFileLocationIfEnabled(request);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
            auditLogger, dsUtils);

        RegistryResponseType actualResponse = passthroughDocSubmission
            .documentRepositoryProvideAndRegisterDocumentSetB(request, assertion, webContextProperties);

        assertEquals("XDSRegistryError", actualResponse.getRegistryErrorList().getRegistryError().get(0).getErrorCode());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getStatus());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getRegistryErrorList()
            .getRegistryError().get(0).getSeverity());

        verify(auditLogger).auditRequestMessage(eq(request), eq(assertion), isNull(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.FALSE), eq(webContextProperties), eq(NhincConstants.NHINC_XDR_SERVICE_NAME));
        verify(auditLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_SERVICE_NAME));
    }

}
