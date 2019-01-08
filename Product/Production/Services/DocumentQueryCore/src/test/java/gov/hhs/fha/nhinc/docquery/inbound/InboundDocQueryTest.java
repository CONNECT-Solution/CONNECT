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
package gov.hhs.fha.nhinc.docquery.inbound;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.docquery.audit.transform.DocQueryAuditTransforms;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.lang.reflect.Method;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Common base class for InboundDocQuery Tests.
 */
public class InboundDocQueryTest {

    /**
     * Responding gateway home community id (from gateway.properties).
     */
    protected static final String RESPONDING_HCID_FORMATTED = "1.2";
    /**
     * Sending gateway home community id (from the assertion org/home community).
     */
    protected static final String SENDING_HCID_ORG = "urn:oid:7.2";
    protected static final String SENDING_HCID_ORG_FORMATTED = "7.2";
    protected static final String SENDING_HCID_HOME = "urn:oid:7.3";
    protected static final String SENDING_HCID_HOME_FORMATTED = "7.3";
    protected static final AdhocQueryRequest request = new AdhocQueryRequest();
    protected static final AdhocQueryResponse expectedResponse = new AdhocQueryResponse();
    protected static final DocQueryPolicyChecker policyChecker = mock(DocQueryPolicyChecker.class);
    protected AuditEJBLogger mockEJBLogger;

    @Before
    public void setUp() {
        mockEJBLogger = mock(AuditEJBLogger.class);
    }

    protected void hasInboundProcessingEvent(Class<? extends InboundDocQuery> clazz) throws Exception {
        Method method = clazz.getMethod("respondingGatewayCrossGatewayQuery", AdhocQueryRequest.class,
            AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(AdhocQueryRequestDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(AdhocQueryResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Query", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    protected void verifyInboundDocQuery(AssertionType assertion, String sendingHcid,
        InboundDocQuery inboundDocQuery, int adapterAuditInvocations) {
        Properties webContextProperties = new Properties();
        NhinTargetSystemType target = null;
        AdhocQueryResponse actualResponse = inboundDocQuery.respondingGatewayCrossGatewayQuery(request, assertion,
            webContextProperties);

        assertSame(expectedResponse, actualResponse);

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.FALSE), eq(webContextProperties), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));
    }

    protected AdapterDocQueryProxyObjectFactory getMockAdapterFactory(AssertionType assertion) {
        AdapterDocQueryProxyObjectFactory mockAdapterFactory = mock(AdapterDocQueryProxyObjectFactory.class);
        AdapterDocQueryProxy mockAdapterProxy = mock(AdapterDocQueryProxy.class);

        when(mockAdapterFactory.getAdapterDocQueryProxy()).thenReturn(mockAdapterProxy);
        when(mockAdapterProxy.respondingGatewayCrossGatewayQuery(request, assertion)).thenReturn(expectedResponse);

        return mockAdapterFactory;
    }

    protected AssertionType getMockAssertion(String hcid) {
        AssertionType assertion = mock(AssertionType.class);
        UserType user = mock(UserType.class);
        HomeCommunityType homeCommunity = mock(HomeCommunityType.class);

        when(assertion.getUserInfo()).thenReturn(user);
        when(user.getOrg()).thenReturn(homeCommunity);
        when(homeCommunity.getHomeCommunityId()).thenReturn(hcid);
        return assertion;
    }

    protected DocQueryAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new DocQueryAuditLogger() {
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
