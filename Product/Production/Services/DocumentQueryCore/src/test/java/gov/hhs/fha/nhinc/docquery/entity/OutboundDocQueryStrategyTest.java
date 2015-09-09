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
package gov.hhs.fha.nhinc.docquery.entity;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author akong
 *
 */
public class OutboundDocQueryStrategyTest {

    final AdhocQueryRequest mockRequest = mock(AdhocQueryRequest.class);
    final AdhocQueryResponse mockResponse = mock(AdhocQueryResponse.class);
    final AssertionType mockAssertion = mock(AssertionType.class);

    private static final String TARGET_HCID = "urn:oid:3.4";
    private static final String TARGET_HCID_FORMATTED = "3.4";

    @Test
    public void errorHandlingUsesMessageUtils() {
        MessageGeneratorUtils mockUtils = mock(MessageGeneratorUtils.class);
        when(mockUtils.createRepositoryErrorResponse(any(String.class))).thenReturn(mockResponse);

        OutboundDocQueryStrategy strategy = mock(OutboundDocQueryStrategy.class, Mockito.CALLS_REAL_METHODS);
        strategy.setMessageGeneratorUtils(mockUtils);

        Exception exception = mock(Exception.class);
        OutboundDocQueryOrchestratable message = mock(OutboundDocQueryOrchestratable.class);
        strategy.handleError(message, exception);

        verify(mockUtils).createRepositoryErrorResponse(any(String.class));
        verify(message).setResponse(mockResponse);
    }

    /**
     * Test {@link OutboundDocQueryStrategy#execute(OutboundDocQueryOrchestratable)} uses the target hcid for audit repo
     * logging.
     *
     * @throws Exception on exception.
     */
    @Test
    public void willAuditRepoLogHcidFromTargetAllStrategies() throws Exception {
        willAuditRepoLogHcidFromTarget(getOutboundDocQueryStrategyG0());
        willAuditRepoLogHcidFromTarget(getOutboundDocQueryStrategyG1());
    }

    private void willAuditRepoLogHcidFromTarget(final OutboundDocQueryStrategy strategy) throws Exception {

        NhinTargetSystemType nhinTargetSystem = getMockNhinTargetSystem();
        Properties properties = null;
        OutboundDocQueryOrchestratable message = getMockOutboundDocQueryOrchestratable(nhinTargetSystem);
        when(message.getRequest()).thenReturn(mockRequest);
        when(message.getResponse()).thenReturn(mockResponse);
        when(message.getAssertion()).thenReturn(mockAssertion);

        strategy.execute(message);

        verify(strategy.getAuditLogger()).auditRequestMessage(eq(mockRequest), eq(mockAssertion), eq(nhinTargetSystem),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), eq(properties), eq(NhincConstants.DOC_QUERY_SERVICE_NAME));
        verify(strategy.getAuditLogger()).auditResponseMessage(eq(mockRequest), eq(mockResponse), eq(mockAssertion),
            eq(nhinTargetSystem), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), eq(properties),
            eq(NhincConstants.DOC_QUERY_SERVICE_NAME));
    }

    private OutboundDocQueryOrchestratable getMockOutboundDocQueryOrchestratable(NhinTargetSystemType nhinTargetSystem) {
        OutboundDocQueryOrchestratable message = mock(OutboundDocQueryOrchestratable.class);
        when(message.getTarget()).thenReturn(nhinTargetSystem);
        return message;
    }

    private NhinTargetSystemType getMockNhinTargetSystem() {
        NhinTargetSystemType nhinTargetSystem = mock(NhinTargetSystemType.class);
        HomeCommunityType homeCommunity = getMockTargetHomeCommunity();
        when(nhinTargetSystem.getHomeCommunity()).thenReturn(homeCommunity);
        return nhinTargetSystem;
    }

    private HomeCommunityType getMockTargetHomeCommunity() {
        HomeCommunityType homeCommunity = mock(HomeCommunityType.class);
        when(homeCommunity.getHomeCommunityId()).thenReturn(TARGET_HCID);
        return homeCommunity;
    }

    private OutboundDocQueryStrategy getOutboundDocQueryStrategyG0() {
        final DocQueryAuditLogger mockAuditLogger = mock(DocQueryAuditLogger.class);
        return new OutboundDocQueryStrategyImpl_g0() {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return mockAuditLogger;
            }
        };
    }

    private OutboundDocQueryStrategy getOutboundDocQueryStrategyG1() {
        final DocQueryAuditLogger mockAuditLogger = mock(DocQueryAuditLogger.class);
        return new OutboundDocQueryStrategyImpl_g1() {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return mockAuditLogger;
            }
        };
    }

}
