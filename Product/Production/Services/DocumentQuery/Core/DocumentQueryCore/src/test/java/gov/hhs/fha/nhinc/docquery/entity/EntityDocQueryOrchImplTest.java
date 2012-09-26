/**
 *
 */
package gov.hhs.fha.nhinc.docquery.entity;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinCallableRequest;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;

/**
 * @author achidamb
 *
 *
 */
public class EntityDocQueryOrchImplTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);

    @Test
    public void testrespondingGatewayCrossGatewayQueryforNullEndPoint() {

        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        adhocQueryRequest = createRequest();

        AssertionType assertion = new AssertionType();

        AdhocQueryResponse response = null;
        EntityDocQueryOrchImpl entitydocqueryimpl = new EntityDocQueryOrchImpl() {

            @Override
            protected boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion,
                    HomeCommunityType targetCommunity) {
                return true;
            }

            @Override
            protected void auditInitialEntityRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request,
                    AssertionType assertion, DocQueryAuditLog auditLog) {

            }

            @Override
            protected List<QualifiedSubjectIdentifierType> retrieveCorrelation(List<SlotType1> slotList,
                    List<UrlInfo> urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunityId) {

                List<QualifiedSubjectIdentifierType> correlationResult = new ArrayList<QualifiedSubjectIdentifierType>();
                QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
                qualSubId.setAssigningAuthorityIdentifier("4.4");
                qualSubId.setSubjectIdentifier("D123401");
                correlationResult.add(qualSubId);
                return correlationResult;
            }

            @Override
            protected void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion,
                    DocQueryAuditLog auditLog) {

            }

            @Override
            protected AdhocQueryRequest cloneRequest(AdhocQueryRequest request) {
                return request;
            }

            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected HomeCommunityType getTargetCommunity(String assigningAuthorityIdentifier, String localAA,
                    String localHomeCommunityId) {
                HomeCommunityType targetCommunity = new HomeCommunityType();
                targetCommunity.setHomeCommunityId(assigningAuthorityIdentifier);
                return targetCommunity;

            }

            @Override
            protected List<UrlInfo> getEndpointForNhinTargetCommunities(NhinTargetCommunitiesType targets,
                    String docQueryServiceName) {
                List<UrlInfo> urlInfoList = new ArrayList<UrlInfo>();
                UrlInfo urlInfo = new UrlInfo();
                urlInfo.setHcid("4.4");
                urlInfo.setUrl("");
                urlInfoList.add(urlInfo);
                return urlInfoList;
            }

            @Override
            protected boolean getApiLevel(String localHomeCommunityId, NHIN_SERVICE_NAMES documentQuery) {
                return true;
            }

        };
        try {

            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).error(with(any(String.class)));
                }
            });
            response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion,
                    createNhinTargetCommunites());

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertSame(response.getStatus(), DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

    }

    @Test
    public void testrespondingGatewayCrossGatewayQueryforMultipleTragets() {

        AdhocQueryRequest adhocQueryRequest = null;
        adhocQueryRequest = createRequest();

        AssertionType assertion = new AssertionType();

        AdhocQueryResponse response = new AdhocQueryResponse();
        EntityDocQueryOrchImpl entitydocqueryimpl = new EntityDocQueryOrchImpl() {

            @Override
            protected boolean isValidPolicy(AdhocQueryRequest queryRequest, AssertionType assertion,
                    HomeCommunityType targetCommunity) {
                return true;
            }

            @Override
            protected void auditInitialEntityRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request,
                    AssertionType assertion, DocQueryAuditLog auditLog) {

            }

            @Override
            protected List<QualifiedSubjectIdentifierType> retrieveCorrelation(List<SlotType1> slotList,
                    List<UrlInfo> urlInfoList, AssertionType assertion, boolean isTargeted, String localHomeCommunityId) {
                List<QualifiedSubjectIdentifierType> correlationResult = new ArrayList<QualifiedSubjectIdentifierType>();
                QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
                qualSubId.setAssigningAuthorityIdentifier("4.4");
                qualSubId.setSubjectIdentifier("D123401");
                correlationResult.add(qualSubId);
                QualifiedSubjectIdentifierType qualSubId1 = new QualifiedSubjectIdentifierType();
                qualSubId1.setAssigningAuthorityIdentifier("2.2");
                qualSubId1.setSubjectIdentifier("D123401");
                correlationResult.add(qualSubId1);
                return correlationResult;
            }

            @Override
            protected void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion,
                    DocQueryAuditLog auditLog) {

            }

            @Override
            protected AdhocQueryRequest cloneRequest(AdhocQueryRequest request) {
                return request;
            }

            @Override
            protected AdhocQueryResponse specResponseA0(List<QualifiedSubjectIdentifierType> correlationsResult,
                    List<NhinCallableRequest<OutboundDocQueryOrchestratable>> callableList, String transactionId,
                    AdhocQueryResponse response, RegistryErrorList policyErrList) throws InterruptedException,
                    ExecutionException {

                if (callableList.size() > 0) {
                    response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
                } else {
                    response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
                }
                return response;
            }

            @Override
            protected AdhocQueryResponse specResponseA1(List<QualifiedSubjectIdentifierType> correlationsResult,
                    List<NhinCallableRequest<OutboundDocQueryOrchestratable>> callableList, String transactionId,
                    AdhocQueryResponse response, RegistryErrorList policyErrList) throws InterruptedException,
                    ExecutionException {
                return response;
            }

            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected HomeCommunityType getTargetCommunity(String assigningAuthorityIdentifier, String localAA,
                    String localHomeCommunityId) {

                HomeCommunityType targetCommunity = new HomeCommunityType();
                targetCommunity.setHomeCommunityId(assigningAuthorityIdentifier);
                return targetCommunity;

            }

            @Override
            protected List<UrlInfo> getEndpointForNhinTargetCommunities(NhinTargetCommunitiesType targets,
                    String docQueryServiceName) {
                List<UrlInfo> urlInfoList = new ArrayList<UrlInfo>();
                UrlInfo urlInfo = new UrlInfo();
                urlInfo.setHcid("4.4");
                urlInfo.setUrl("");
                urlInfoList.add(urlInfo);
                UrlInfo urlInfo1 = new UrlInfo();
                urlInfo1.setHcid("2.2");
                urlInfo1.setUrl("https://localhost:8080/connect/DocQuery");
                urlInfoList.add(urlInfo1);
                return urlInfoList;

            }

            @Override
            protected boolean getApiLevel(String localHomeCommunityId, NHIN_SERVICE_NAMES documentQuery) {
                return true;
            }

        };
        try {

            context.checking(new Expectations() {
                {
                    allowing(mockLog).isDebugEnabled();                    
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).error(with(any(String.class)));
                }
            });

            response = entitydocqueryimpl.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion,
                    createNhinTargetCommunitesForMultipleTargets());

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertSame(response.getStatus(), DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIALSUCCESS);

    }

    private AdhocQueryRequest createRequest() {
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQueryRequest.setAdhocQuery(adhocQuery);
        adhocQuery.setHome("urn:oid:4.4");
        adhocQuery.setHome("urn:oid:6.6");
        adhocQuery.setHome("urn:oid:7.7");
        adhocQuery.setHome("urn:oid:2.2");
        SlotType1 patientIdSlot = new SlotType1();
        adhocQuery.getSlot().add(patientIdSlot);
        patientIdSlot.setName("$XDSDocumentEntryPatientId");
        ValueListType valueList = new ValueListType();
        patientIdSlot.setValueList(valueList);
        valueList.getValue().add("'500000000^^^&amp;1.1&amp;ISO'");
        return adhocQueryRequest;
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunites() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("4.4");
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion("US-FL");
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        return targetCommunities;
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunitesForMultipleTargets() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("4.4");
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion("US-FL");
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        NhinTargetCommunityType targetCommunity1 = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity1 = new HomeCommunityType();
        homeCommunity1.setHomeCommunityId("2.2");
        targetCommunity1.setHomeCommunity(homeCommunity1);
        /*
         * targetCommunity.setList("US-FL"); targetCommunity.setRegion("Unimplemented");
         */
        targetCommunities.getNhinTargetCommunity().add(targetCommunity1);
        return targetCommunities;
    }

}
