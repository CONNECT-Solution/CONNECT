/**
 *
 */
package gov.hhs.fha.nhinc.docquery.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.entity.AggregationStrategy;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryAggregate;
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
import org.mockito.ArgumentCaptor;

/**
 * @author achidamb
 */
public class StandardOutboundDocQueryTest {

  
    
    @Test
    public void testrespondingGatewayCrossGatewayQueryforNullEndPoint() {

        AggregationStrategy strategy = mock(AggregationStrategy.class);
        
        
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest = createRequest(createSlotList());

        AssertionType assertion = new AssertionType();

        AdhocQueryResponse response = null;
        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery(strategy);
     
            NhinTargetCommunitiesType targets = createNhinTargetCommunites();
            response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion,
                    targets);
            
            ArgumentCaptor<OutboundDocQueryAggregate> aggregate = ArgumentCaptor.forClass(OutboundDocQueryAggregate.class);

        verify(strategy).execute(aggregate.capture());
        
        assertSame(aggregate.getValue().getAdhocQueryRequest(), adhocQueryRequest);
        assertSame(aggregate.getValue().getAssertion(), assertion);
        assertSame(aggregate.getValue().getTargets(), targets);
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
        
        for(NhinTargetCommunityType targetCommunity: targetCommunities)
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
    
//    @Test
//    public void testrespondingGatewayCrossGatewayQueryforMultipleTragets() {
//
//        AdhocQueryRequest adhocQueryRequest = null;
//        adhocQueryRequest = createRequest();
//
//        AssertionType assertion = new AssertionType();
//
//        AdhocQueryResponse response = new AdhocQueryResponse();
//        StandardOutboundDocQuery entitydocqueryimpl = new StandardOutboundDocQuery() {
//
//            @Override
//            protected boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion,
//                    HomeCommunityType targetCommunity) {
//                return true;
//            }
//
//            @Override
//            protected void auditInitialEntityRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request,
//                    AssertionType assertion, DocQueryAuditLog auditLog) {
//
//            }
//
//            @Override
//            protected List<QualifiedSubjectIdentifierType> retrieveCorrelation(List<SlotType1> slotList,
//                    List<UrlInfo> urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunityId) {
//                List<QualifiedSubjectIdentifierType> correlationResult = new ArrayList<QualifiedSubjectIdentifierType>();
//                QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
//                qualSubId.setAssigningAuthorityIdentifier("4.4");
//                qualSubId.setSubjectIdentifier("D123401");
//                correlationResult.add(qualSubId);
//                QualifiedSubjectIdentifierType qualSubId1 = new QualifiedSubjectIdentifierType();
//                qualSubId1.setAssigningAuthorityIdentifier("2.2");
//                qualSubId1.setSubjectIdentifier("D123401");
//                correlationResult.add(qualSubId1);
//                return correlationResult;
//            }
//
//            @Override
//            protected void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion,
//                    DocQueryAuditLog auditLog) {
//
//            }
//
//            @Override
//            protected AdhocQueryRequest cloneRequest(AdhocQueryRequest request) {
//                return request;
//            }
//
//            @Override
//            protected AdhocQueryResponse specResponseA0(List<QualifiedSubjectIdentifierType> correlationsResult,
//                    List<NhinCallableRequest<OutboundDocQueryOrchestratable>> callableList, String transactionId,
//                    AdhocQueryResponse response, RegistryErrorList policyErrList) throws InterruptedException,
//                    ExecutionException {
//
//                if (callableList.size() > 0) {
//                    response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
//                } else {
//                    response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
//                }
//                return response;
//            }
//
//            @Override
//            protected AdhocQueryResponse specResponseA1(List<QualifiedSubjectIdentifierType> correlationsResult,
//                    List<NhinCallableRequest<OutboundDocQueryOrchestratable>> callableList, String transactionId,
//                    AdhocQueryResponse response, RegistryErrorList policyErrList) throws InterruptedException,
//                    ExecutionException {
//                return response;
//            }
//
//            @Override
//            protected Log createLogger() {
//                return mockLog;
//            }
//
//            @Override
//            protected HomeCommunityType getTargetCommunity(String assigningAuthorityIdentifier, String localAA,
//                    String localHomeCommunityId) {
//
//                HomeCommunityType targetCommunity = new HomeCommunityType();
//                targetCommunity.setHomeCommunityId(assigningAuthorityIdentifier);
//                return targetCommunity;
//
//            }
//
//            @Override
//            protected List<UrlInfo> getEndpointForNhinTargetCommunities(NhinTargetCommunitiesType targets,
//                    String docQueryServiceName) {
//                List<UrlInfo> urlInfoList = new ArrayList<UrlInfo>();
//                UrlInfo urlInfo = new UrlInfo();
//                urlInfo.setHcid("4.4");
//                urlInfo.setUrl("");
//                urlInfoList.add(urlInfo);
//                UrlInfo urlInfo1 = new UrlInfo();
//                urlInfo1.setHcid("2.2");
//                urlInfo1.setUrl("https://localhost:8080/connect/DocQuery");
//                urlInfoList.add(urlInfo1);
//                return urlInfoList;
//
//            }
//
//            @Override
//            protected boolean getApiLevel(String localHomeCommunityId, NHIN_SERVICE_NAMES documentQuery) {
//                return true;
//            }
//
//        };
//        try {
//
//            context.checking(new Expectations() {
//                {
//                    allowing(mockLog).isDebugEnabled();
//                    allowing(mockLog).debug(with(any(String.class)));
//                    allowing(mockLog).error(with(any(String.class)));
//                }
//            });
//
//            response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion,
//                    createNhinTargetCommunitesForMultipleTargets());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        assertSame(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIALSUCCESS, response.getStatus());
//
//    }

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
