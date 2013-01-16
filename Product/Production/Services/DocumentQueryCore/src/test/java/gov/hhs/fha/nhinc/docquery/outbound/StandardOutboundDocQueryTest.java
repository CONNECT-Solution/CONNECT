/**
 *
 */
package gov.hhs.fha.nhinc.docquery.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
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

    @Test
    public void testrespondingGatewayCrossGatewayQueryforNullEndPoint() throws Exception {
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
				eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
				eq(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE), any(String.class));

		verify(mockAuditLogger).auditDQResponse(eq(response),
				eq(assertion), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
				eq(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE), any(String.class));
        
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
        adhocQuery.setHome("urn:oid:4.4");
        adhocQuery.setHome("urn:oid:6.6");
        adhocQuery.setHome("urn:oid:7.7");
        adhocQuery.setHome("urn:oid:2.2");
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
