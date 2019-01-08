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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.audit.transform.DocSubmissionAuditTransforms;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PassthroughInboundDocSubmissionTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final AdapterDocSubmissionProxyObjectFactory adapterFactory = mock(AdapterDocSubmissionProxyObjectFactory.class);
    private final DocSubmissionUtils dsUtils = mock(DocSubmissionUtils.class);
    private final Properties webContextProperties = new Properties();
    private final ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
    private final AssertionType assertion = new AssertionType();

    @Test
    public void passthroughInboundDocSubmission() {

        RegistryResponseType expectedResponse = new RegistryResponseType();
        AdapterDocSubmissionProxy adapterProxy = mock(AdapterDocSubmissionProxy.class);

        when(adapterFactory.getAdapterDocSubmissionProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetB(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
            getAuditLogger(true), dsUtils);

        RegistryResponseType actualResponse = passthroughDocSubmission
            .documentRepositoryProvideAndRegisterDocumentSetB(request, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_SERVICE_NAME), any(DocSubmissionAuditTransforms.class));
    }

    @Test
    public void convertDataToFileError() throws LargePayloadException {

        doThrow(new LargePayloadException()).when(dsUtils).convertDataToFileLocationIfEnabled(request);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
            getAuditLogger(true), dsUtils);
        RegistryResponseType actualResponse = null;
        try {
            passthroughDocSubmission.documentRepositoryProvideAndRegisterDocumentSetB(request, assertion, webContextProperties);
            Assert.fail("Didnt throw exception!");
        } catch (ErrorEventException e) {
            actualResponse = (RegistryResponseType) e.getReturnOverride();
            assertEquals("XDSRegistryError", actualResponse.getRegistryErrorList().getRegistryError().get(0).getErrorCode());
            assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", actualResponse.getStatus());
            assertEquals("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error", actualResponse.getRegistryErrorList()
                .getRegistryError().get(0).getSeverity());


        }
        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_SERVICE_NAME), any(DocSubmissionAuditTransforms.class));
    }

    @Test
    public void testAuditLoggingOffForInboundDS() {

        RegistryResponseType expectedResponse = new RegistryResponseType();
        AdapterDocSubmissionProxy adapterProxy = mock(AdapterDocSubmissionProxy.class);

        when(adapterFactory.getAdapterDocSubmissionProxy()).thenReturn(adapterProxy);

        when(adapterProxy.provideAndRegisterDocumentSetB(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocSubmission passthroughDocSubmission = new PassthroughInboundDocSubmission(adapterFactory,
            getAuditLogger(false), dsUtils);

        RegistryResponseType actualResponse = passthroughDocSubmission
            .documentRepositoryProvideAndRegisterDocumentSetB(request, assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.NHINC_XDR_SERVICE_NAME), any(DocSubmissionAuditTransforms.class));
    }

    private DocSubmissionAuditLogger getAuditLogger(final boolean isAuditOn) {
        return new DocSubmissionAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isAuditOn;
            }
        };
    }
}
