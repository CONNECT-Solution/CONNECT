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
package gov.hhs.fha.nhinc.docquery.inbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * @author akong
 * 
 */
public class StandardInboundDocQueryTest {
    
    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundDocQuery> clazz = StandardInboundDocQuery.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayQuery", AdhocQueryRequest.class,
                AssertionType.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(AdhocQueryRequestDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(AdhocQueryResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Query", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void standardInboundDocQuery() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        AdhocQueryResponse expectedResponse = new AdhocQueryResponse();

        AdapterDocQueryProxyObjectFactory mockAdapterFactory = mock(AdapterDocQueryProxyObjectFactory.class);
        AdapterDocQueryProxy mockAdapterProxy = mock(AdapterDocQueryProxy.class);
        DocQueryAuditLog mockAuditLogger = mock(DocQueryAuditLog.class);
        DocQueryPolicyChecker policyChecker = mock(DocQueryPolicyChecker.class);

        when(mockAdapterFactory.getAdapterDocQueryProxy()).thenReturn(mockAdapterProxy);

        when(mockAdapterProxy.respondingGatewayCrossGatewayQuery(request, assertion)).thenReturn(expectedResponse);

        when(policyChecker.checkIncomingPolicy(request, assertion)).thenReturn(true);

        PassthroughInboundDocQuery passthroughDocQuery = new PassthroughInboundDocQuery(mockAdapterFactory,
                mockAuditLogger);

        StandardInboundDocQuery standardDocQuery = new StandardInboundDocQuery(policyChecker, passthroughDocQuery,
                mockAuditLogger);
        AdhocQueryResponse actualResponse = standardDocQuery.respondingGatewayCrossGatewayQuery(request, assertion);

        assertSame(expectedResponse, actualResponse);

        verify(mockAuditLogger).auditDQRequest(eq(request), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                any(String.class));

        verify(mockAuditLogger).auditDQRequest(eq(request), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                any(String.class));

        verify(mockAuditLogger).auditDQResponse(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                any(String.class));

        verify(mockAuditLogger).auditDQResponse(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                any(String.class));
    }

    @Test
    public void failedPolicy() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();

        DocQueryAuditLog mockAuditLogger = mock(DocQueryAuditLog.class);
        DocQueryPolicyChecker policyChecker = mock(DocQueryPolicyChecker.class);
        PassthroughInboundDocQuery passthroughDocQuery = mock(PassthroughInboundDocQuery.class);

        when(policyChecker.checkIncomingPolicy(request, assertion)).thenReturn(false);

        StandardInboundDocQuery standardDocQuery = new StandardInboundDocQuery(policyChecker, passthroughDocQuery,
                mockAuditLogger);
        AdhocQueryResponse actualResponse = standardDocQuery.respondingGatewayCrossGatewayQuery(request, assertion);

        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, actualResponse.getStatus());
        assertEquals(DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR, actualResponse.getRegistryErrorList()
                .getRegistryError().get(0).getErrorCode());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, actualResponse.getRegistryErrorList()
                .getRegistryError().get(0).getSeverity());

    }
}
