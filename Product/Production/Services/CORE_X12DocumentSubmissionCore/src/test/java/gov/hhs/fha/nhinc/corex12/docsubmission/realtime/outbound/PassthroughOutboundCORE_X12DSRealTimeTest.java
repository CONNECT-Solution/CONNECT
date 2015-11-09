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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.outbound;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.audit.ejb.impl.AuditEJBLoggerImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditLogger;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform.COREX12RealTimeAuditTransforms;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.OutboundCORE_X12DSRealTimeDelegate;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.entity.OutboundCORE_X12DSRealTimeOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author tjafri
 */
public class PassthroughOutboundCORE_X12DSRealTimeTest {

    private final AuditEJBLoggerImpl mockEJBLogger = mock(AuditEJBLoggerImpl.class);

    @Test
    public void auditLoggingOnForOutboundRealTime() {
        COREEnvelopeRealTimeRequest request = new COREEnvelopeRealTimeRequest();
        AssertionType assertion = new AssertionType();
        UrlInfoType urlInfo = new UrlInfoType();
        OutboundCORE_X12DSRealTimeOrchestratable mockOrch = mock(OutboundCORE_X12DSRealTimeOrchestratable.class);
        OutboundCORE_X12DSRealTimeDelegate mockDelegate = mock(OutboundCORE_X12DSRealTimeDelegate.class);
        COREEnvelopeRealTimeResponse expectedResponse = new COREEnvelopeRealTimeResponse();
        PassthroughOutboundCORE_X12DSRealTime realTime = new PassthroughOutboundCORE_X12DSRealTime(
            mockDelegate, getAuditLogger(true));
        when(mockDelegate.process(any(OutboundOrchestratable.class))).thenReturn(mockOrch);
        when(mockOrch.getResponse()).thenReturn(expectedResponse);
        COREEnvelopeRealTimeResponse actualResponse = realTime.realTimeTransaction(request, assertion,
            createNhinTargetCommunities(), urlInfo);
        assertEquals("Actual and Expected Response differ", expectedResponse, actualResponse);
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME),
            any(COREX12RealTimeAuditTransforms.class));
    }

    @Test
    public void auditLoggingOffForOutboundRealTime() {
        COREEnvelopeRealTimeRequest request = new COREEnvelopeRealTimeRequest();
        AssertionType assertion = new AssertionType();
        UrlInfoType urlInfo = new UrlInfoType();
        OutboundCORE_X12DSRealTimeOrchestratable mockOrch = mock(OutboundCORE_X12DSRealTimeOrchestratable.class);
        OutboundCORE_X12DSRealTimeDelegate mockDelegate = mock(OutboundCORE_X12DSRealTimeDelegate.class);
        COREEnvelopeRealTimeResponse expectedResponse = new COREEnvelopeRealTimeResponse();
        PassthroughOutboundCORE_X12DSRealTime realTime = new PassthroughOutboundCORE_X12DSRealTime(
            mockDelegate, getAuditLogger(false));
        when(mockDelegate.process(any(OutboundOrchestratable.class))).thenReturn(mockOrch);
        when(mockOrch.getResponse()).thenReturn(expectedResponse);
        COREEnvelopeRealTimeResponse actualResponse = realTime.realTimeTransaction(request, assertion,
            createNhinTargetCommunities(), urlInfo);
        assertEquals("Actual and Expected Response differ", expectedResponse, actualResponse);
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME),
            any(COREX12RealTimeAuditTransforms.class));
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

    private NhinTargetCommunitiesType createNhinTargetCommunities() {
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("1.1");
        target.setHomeCommunity(home);
        targets.getNhinTargetCommunity().add(target);
        return targets;
    }

}
