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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.inbound;

/**
 *
 * @author tjafri
 */
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.audit.ejb.impl.AuditEJBLoggerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform.COREX12RealTimeAuditTransforms;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter.proxy.AdapterCORE_X12DSRealTimeProxy;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.adapter.proxy.AdapterCORE_X12DSRealTimeProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import org.junit.Test;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PassthroughInboundCORE_X12DSRealTimeTest {

    private final AuditEJBLoggerImpl mockEJBLogger = mock(AuditEJBLoggerImpl.class);
    private final AdapterCORE_X12DSRealTimeProxyObjectFactory mockFactory
        = mock(AdapterCORE_X12DSRealTimeProxyObjectFactory.class);
    private final AdapterCORE_X12DSRealTimeProxy mockAdapterProxy = mock(AdapterCORE_X12DSRealTimeProxy.class);
    private final COREEnvelopeRealTimeRequest request = new COREEnvelopeRealTimeRequest();
    private final AssertionType assertion = new AssertionType();
    private final Properties webContextProperties = new Properties();

    @Test
    public void auditLoggingOnForInboundRealTime() {
        PassthroughInboundCORE_X12DSRealTime realTimeX12
            = new PassthroughInboundCORE_X12DSRealTime(mockFactory, getAuditLogger(true));
        when(mockFactory.getAdapterCORE_X12DocSubmissionProxy()).thenReturn(mockAdapterProxy);
        COREEnvelopeRealTimeResponse expectedResponse = new COREEnvelopeRealTimeResponse();
        when(mockAdapterProxy.realTimeTransaction(request, assertion)).thenReturn(expectedResponse);
        COREEnvelopeRealTimeResponse actualResponse = realTimeX12.realTimeTransaction(request, assertion,
            webContextProperties);
        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME), any(COREX12RealTimeAuditTransforms.class));
    }

    @Test
    public void auditLoggingOffForInboundRealTime() {
        PassthroughInboundCORE_X12DSRealTime realTimeX12
            = new PassthroughInboundCORE_X12DSRealTime(mockFactory, getAuditLogger(false));

        when(mockFactory.getAdapterCORE_X12DocSubmissionProxy()).thenReturn(mockAdapterProxy);
        COREEnvelopeRealTimeResponse expectedResponse = new COREEnvelopeRealTimeResponse();
        when(mockAdapterProxy.realTimeTransaction(request, assertion)).thenReturn(expectedResponse);
        COREEnvelopeRealTimeResponse actualResponse = realTimeX12.realTimeTransaction(request, assertion,
            webContextProperties);
        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME), any(COREX12RealTimeAuditTransforms.class));
    }

    private CORE_X12AuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new CORE_X12AuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }
}
