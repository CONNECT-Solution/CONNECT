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
package gov.hhs.fha.nhinc.docquery.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.AdhocQueryResponseAsserter;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditLogger;
import gov.hhs.fha.nhinc.docquery.audit.transform.DocQueryAuditTransforms;
import gov.hhs.fha.nhinc.docquery.entity.Aggregate;
import gov.hhs.fha.nhinc.docquery.entity.AggregationService;
import gov.hhs.fha.nhinc.docquery.entity.AggregationStrategy;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author achidamb
 */
public class StandardOutboundDocQueryTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final DocQueryPolicyChecker policyChecker = mock(DocQueryPolicyChecker.class);
    private final AggregationStrategy strategy = mock(AggregationStrategy.class);
    private final AggregationService service = mock(AggregationService.class);
    private static final String HOME_HCID = "1.0";
    private static final String SENDING_HCID_FORMATTED = "1.2";

    @Test
    public void testrespondingGatewayCrossGatewayQueryforNullEndPoint() throws Exception {
        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());
        NhinTargetSystemType target = null;
        final DocQueryAuditLogger auditLogger = getAuditLogger(true);
        AssertionType assertion = new AssertionType();

        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy, service, policyChecker) {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected String getSenderHcid() {
                return SENDING_HCID_FORMATTED;
            }
        };

        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        AdhocQueryResponse response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest,
            assertion, targets);
        verify(service).createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets));
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(adhocQueryRequest), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));

        AdhocQueryResponseAsserter.assertSchemaCompliant(response);
    }

    @Test
    public void errorResponseHasRegistryObjectList() throws Exception {
        final DocQueryAuditLogger auditLogger = getAuditLogger(true);
        StandardOutboundDocQuery docQuery = new StandardOutboundDocQuery(strategy, service, policyChecker) {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected String getSenderHcid() {
                return SENDING_HCID_FORMATTED;
            }
        };

        AdhocQueryRequest adhocQueryRequest = mock(AdhocQueryRequest.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = mock(NhinTargetCommunitiesType.class);
        AdhocQueryResponse response = docQuery
            .respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targets);
        AdhocQueryResponseAsserter.assertSchemaCompliant(response);
        verify(mockEJBLogger, never()).auditRequestMessage(eq(adhocQueryRequest), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));
    }

    private AdhocQueryRequest createRequest(List<SlotType1> slotList) {
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        adhocQuery.setHome(HOME_HCID);
        adhocQuery.getSlot().addAll(slotList);
        return adhocQueryRequest;
    }

    private List<SlotType1> createSlotList() {
        List<SlotType1> slotList = new ArrayList<>();
        SlotType1 t = new SlotType1();
        t.setName(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME);
        ValueListType value = new ValueListType();
        value.getValue().add("<id>^^^&<home community id>&ISO");
        t.setValueList(value);

        slotList.add(t);
        return slotList;
    }

    /**
     * @param targetCommunities
     * @return
     */
    public NhinTargetCommunitiesType createTargetCommunities(NhinTargetCommunityType... targetCommunities) {
        NhinTargetCommunitiesType targetCommunitiesType = new NhinTargetCommunitiesType();

        targetCommunitiesType.getNhinTargetCommunity().addAll(Arrays.asList(targetCommunities));

        return targetCommunitiesType;
    }

    /**
     * @param hcid
     * @param region
     * @param list
     * @return
     */
    public NhinTargetCommunityType createTargetCommunity(String hcid, String region, String list) {
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(region);
        targetCommunity.setList(list);
        return targetCommunity;
    }

    private NhinTargetCommunitiesType createNhinTargetCommunites() {
        return createTargetCommunities(createTargetCommunity("4.4", "US-FL", "Unimplemented"));
    }

    @Test
    public void hasBeginOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundDocQuery> clazz = StandardOutboundDocQuery.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayQuery", AdhocQueryRequest.class,
            AssertionType.class, NhinTargetCommunitiesType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(AdhocQueryRequestDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(AdhocQueryResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Query", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testWithPolicyFailures() throws Exception {

        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());
        NhinTargetSystemType target = null;
        OutboundDocQueryOrchestratable orch1 = mock(OutboundDocQueryOrchestratable.class);
        final DocQueryAuditLogger auditLogger = getAuditLogger(true);
        List<OutboundOrchestratable> list = new ArrayList<>();
        list.add(orch1);

        AssertionType assertion = new AssertionType();

        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy, service, policyChecker) {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected String getSenderHcid() {
                return SENDING_HCID_FORMATTED;
            }
        };

        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        when(service.createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets))).thenReturn(list);
        when(orch1.getAssertion()).thenReturn(assertion);
        when(orch1.getRequest()).thenReturn(adhocQueryRequest);
        when(orch1.getTarget()).thenReturn(target);
        when(policyChecker.checkOutgoingPolicy(any(AdhocQueryRequest.class), any(AssertionType.class))).thenReturn(false);

        AdhocQueryResponse response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest,
            assertion, targets);
        verify(service).createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets));
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(adhocQueryRequest), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));
        assertNotNull(response);
    }

    @Test
    public void testWithMixOfPolicyFailures() throws Exception {
        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());
        NhinTargetSystemType target = null;

        OutboundDocQueryOrchestratable orch1 = mock(OutboundDocQueryOrchestratable.class);
        OutboundDocQueryOrchestratable orch2 = mock(OutboundDocQueryOrchestratable.class);
        List<OutboundOrchestratable> list = new ArrayList<>();
        list.add(orch1);
        list.add(orch2);
        AssertionType assertion = new AssertionType();
        final DocQueryAuditLogger auditLogger = getAuditLogger(true);
        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy, service, policyChecker) {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected String getSenderHcid() {
                return SENDING_HCID_FORMATTED;
            }
        };

        when(policyChecker.checkOutgoingPolicy(any(AdhocQueryRequest.class), any(AssertionType.class))).thenReturn(false)
            .thenReturn(true);
        when(orch1.getAssertion()).thenReturn(assertion);
        when(orch1.getRequest()).thenReturn(adhocQueryRequest);
        when(orch1.getTarget()).thenReturn(target);
        when(orch2.getAssertion()).thenReturn(assertion);
        when(orch2.getRequest()).thenReturn(adhocQueryRequest);
        when(orch2.getTarget()).thenReturn(target);
        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        when(service.createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets))).thenReturn(list);

        entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targets);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, atLeast(1)).auditRequestMessage(eq(adhocQueryRequest), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));

        verify(strategy).execute(any(Aggregate.class));
    }

    @Test
    public void auditLoggingOffForOutboundDQ() throws Exception {
        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());
        NhinTargetSystemType target = null;

        OutboundDocQueryOrchestratable orch1 = mock(OutboundDocQueryOrchestratable.class);
        List<OutboundOrchestratable> list = new ArrayList<>();
        list.add(orch1);
        AssertionType assertion = new AssertionType();
        final DocQueryAuditLogger auditLogger = getAuditLogger(false);
        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy, service, policyChecker) {
            @Override
            protected DocQueryAuditLogger getAuditLogger() {
                return auditLogger;
            }

            @Override
            protected String getSenderHcid() {
                return SENDING_HCID_FORMATTED;
            }
        };

        when(policyChecker.checkOutgoingPolicy(any(AdhocQueryRequest.class), any(AssertionType.class))).thenReturn(false)
            .thenReturn(true);
        when(orch1.getAssertion()).thenReturn(assertion);
        when(orch1.getRequest()).thenReturn(adhocQueryRequest);
        when(orch1.getTarget()).thenReturn(target);
        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        when(service.createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets))).thenReturn(list);

        entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targets);
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(adhocQueryRequest), eq(assertion), eq(target),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.DOC_QUERY_SERVICE_NAME),
            any(DocQueryAuditTransforms.class));
    }

    private DocQueryAuditLogger getAuditLogger(final boolean isLoggingOn) {
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
