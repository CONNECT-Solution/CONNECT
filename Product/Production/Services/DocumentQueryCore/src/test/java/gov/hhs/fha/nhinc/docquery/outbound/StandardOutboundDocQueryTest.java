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
package gov.hhs.fha.nhinc.docquery.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docquery.AdhocQueryResponseAsserter;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryUnitTestUtil;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.entity.AggregationService;
import gov.hhs.fha.nhinc.docquery.entity.AggregationStrategy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.junit.Test;

/**
 * @author achidamb
 */
public class StandardOutboundDocQueryTest {

    final DocQueryAuditLog mockAuditLogger = mock(DocQueryAuditLog.class);
    private static final String HOME_HCID = "1.0";
    private static final String SENDING_HCID_FORMATTED = "1.2";

    @Test
    public void testrespondingGatewayCrossGatewayQueryforNullEndPoint() throws Exception {
        
        System.setProperty("nhinc.properties.dir", "" + DocQueryUnitTestUtil.getClassPath());
        
        AggregationStrategy strategy = mock(AggregationStrategy.class);

        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest = createRequest(createSlotList());

        AggregationService service = mock(AggregationService.class);

        AssertionType assertion = new AssertionType();

        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy, service) {
            @Override
            protected DocQueryAuditLog getAuditLogger() {
                return mockAuditLogger;
            }
        };

        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        AdhocQueryResponse response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest,
                assertion, targets);
        verify(service).createChildRequests(eq(adhocQueryRequest), eq(assertion), eq(targets));

        verify(mockAuditLogger).auditDQRequest(eq(adhocQueryRequest), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE),
                eq(SENDING_HCID_FORMATTED));

        verify(mockAuditLogger).auditDQResponse(eq(response), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE),
                eq(SENDING_HCID_FORMATTED));

        AdhocQueryResponseAsserter.assertSchemaCompliant(response);

    }

    @Test
    public void errorResponseHasRegistryObjectList() throws Exception {
        AggregationStrategy strategy = mock(AggregationStrategy.class);
        AggregationService service = mock(AggregationService.class);
        StandardOutboundDocQuery docQuery = new StandardOutboundDocQuery(strategy, service) {
            @Override
            protected DocQueryAuditLog getAuditLogger() {
                return mockAuditLogger;
            }
        };

        AdhocQueryRequest adhocQueryRequest = mock(AdhocQueryRequest.class);
        AssertionType assertion = mock(AssertionType.class);
        NhinTargetCommunitiesType targets = mock(NhinTargetCommunitiesType.class);
        AdhocQueryResponse response = docQuery
                .respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, targets);
        AdhocQueryResponseAsserter.assertSchemaCompliant(response);
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
        List<SlotType1> slotList = new ArrayList<SlotType1>();
        SlotType1 t = new SlotType1();
        t.setName(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME);
        ValueListType value = new ValueListType();
        value.getValue().add("<id>^^^&<home community id>&ISO");
        t.setValueList(value);

        slotList.add(t);
        return slotList;
    }

    /**
     * @param targetCommunity
     * @return
     */
    public NhinTargetCommunitiesType createTargetCommunities(NhinTargetCommunityType... targetCommunities) {
        NhinTargetCommunitiesType targetCommunitiesType = new NhinTargetCommunitiesType();

        for (NhinTargetCommunityType targetCommunity : targetCommunities)
            targetCommunitiesType.getNhinTargetCommunity().add(targetCommunity);

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
}
