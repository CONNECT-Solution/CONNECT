/*
 * Copyright (c) 2013, United States Government, as represented by the Secretary of Health and Human Services.
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryUnitTestUtil;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.lang.reflect.Method;
import java.net.URISyntaxException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.junit.Test;

/**
 * @author akong
 * 
 */
public class PassthroughInboundDocQueryTest {
        
    /**
     * Compared with value in the gateway.properties file.
     */
    private static final String RESPONDING_HCID_FORMATTED = "1.2";
    
    
    /**
     * Compared with value in the assertion.
     */
    private static final String SENDING_HCID_ORG = "urn:oid:7.2";
    private static final String SENDING_HCID_ORG_FORMATTED = "7.2";
    private static final String SENDING_HCID_HOME = "urn:oid:7.3";
    private static final String SENDING_HCID_HOME_FORMATTED = "7.3";
    
    
    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<PassthroughInboundDocQuery> clazz = PassthroughInboundDocQuery.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayQuery", AdhocQueryRequest.class,
                AssertionType.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(AdhocQueryRequestDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(AdhocQueryResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Query", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    /**
     * Test {@link PassthroughInboundDocQuery#respondingGatewayCrossGatewayQuery(AdhocQueryRequest, AssertionType)}
     * when sending hcid comes from org part of the assertion.
     * @throws URISyntaxException
     */
    @Test
    public void passthroughInboundDocQueryOrgHcid() throws URISyntaxException {
        passthroughInboundDocQuery(getMockAssertion(SENDING_HCID_ORG), SENDING_HCID_ORG_FORMATTED);
    }
    
    /**
     * Test {@link PassthroughInboundDocQuery#respondingGatewayCrossGatewayQuery(AdhocQueryRequest, AssertionType)}
     * when sending hcid comes from home community part of the assertion.
     * @throws URISyntaxException
     */
    @Test
    public void passthroughInboundDocQueryHomeHcid() throws URISyntaxException {
        passthroughInboundDocQuery(getMockAssertion(SENDING_HCID_HOME), SENDING_HCID_HOME_FORMATTED);
    }

    private void passthroughInboundDocQuery(AssertionType assertion, String sendingHcid) throws URISyntaxException {
        
        System.setProperty("nhinc.properties.dir", "" + DocQueryUnitTestUtil.getClassPath());
        
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryResponse expectedResponse = new AdhocQueryResponse();

        AdapterDocQueryProxyObjectFactory mockAdapterFactory = mock(AdapterDocQueryProxyObjectFactory.class);
        AdapterDocQueryProxy mockAdapterProxy = mock(AdapterDocQueryProxy.class);
        DocQueryAuditLog mockAuditLogger = mock(DocQueryAuditLog.class);

        when(mockAdapterFactory.getAdapterDocQueryProxy()).thenReturn(mockAdapterProxy);
        when(mockAdapterProxy.respondingGatewayCrossGatewayQuery(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundDocQuery passthroughDocQuery = new PassthroughInboundDocQuery(mockAdapterFactory,
                mockAuditLogger);
        AdhocQueryResponse actualResponse = passthroughDocQuery.respondingGatewayCrossGatewayQuery(request, assertion);
        
        assertSame(expectedResponse, actualResponse);

        verify(mockAuditLogger).auditDQRequest(eq(request), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                eq(sendingHcid));

        verify(mockAuditLogger).auditDQRequest(eq(request), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                eq(RESPONDING_HCID_FORMATTED));

        verify(mockAuditLogger).auditDQResponse(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE),
                eq(RESPONDING_HCID_FORMATTED));

        verify(mockAuditLogger).auditDQResponse(eq(actualResponse), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
                eq(sendingHcid));
    }
   
    private AssertionType getMockAssertion(String hcid) {
        AssertionType assertion = mock(AssertionType.class);
        UserType user = mock(UserType.class);
        HomeCommunityType homeCommunity = mock(HomeCommunityType.class);
        
        when(assertion.getUserInfo()).thenReturn(user);
        when(user.getOrg()).thenReturn(homeCommunity);        
        when(homeCommunity.getHomeCommunityId()).thenReturn(hcid);
        return assertion; 
    }


}
