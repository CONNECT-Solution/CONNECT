/**
 * 
 */
package gov.hhs.fha.nhinc.docquery.entity;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;

/**
 * @author achidamb
 * 
 */

/* This method tests only AdhocQueryId supplied is not in the defined list and checks the ErrorCode * */
public class OutboundDocQueryProcessorTest {
    private static Log log = LogFactory.getLog(OutboundDocQueryProcessorTest.class);
    OutboundOrchestratableMessage individual = null;
    OutboundOrchestratableMessage cumulative = null;

    @Test
    public void testProcessNhinResponse() {
        OutboundDocQueryProcessor processor = new OutboundDocQueryProcessor() {
            @Override
            protected OutboundOrchestratableMessage getCumulativeResponseBasedGLEVEL(
                    OutboundOrchestratableMessage cumulativeResponse, OutboundOrchestratableMessage individual) {
                cumulativeResponse = createCumulativeResponse(individual);
                return cumulativeResponse;
            }

            @Override
            public OutboundOrchestratableMessage aggregateResponse(OutboundDocQueryOrchestratable individual,
                    OutboundOrchestratableMessage cumulative) {

                OutboundDocQueryOrchestratable_a0 cumulativeResponse = (OutboundDocQueryOrchestratable_a0) createCumulativeResponse(individual);
                OutboundDocQueryOrchestratable_a0 individualResponse = (OutboundDocQueryOrchestratable_a0) individualResponse(individual);
                try {

                    aggregateResponse_a0(individualResponse, cumulativeResponse);
                    cumulativeResponse.setResponse(cumulativeResponse.getCumulativeResponse());
                } catch (Exception e) {
                    log.debug("Error while aggregating");
                    e.printStackTrace();
                }

                return cumulativeResponse;

            }
        };
        OutboundDocQueryOrchestratable_a0 response = null;

        response = (OutboundDocQueryOrchestratable_a0) processor.processNhinResponse(
                individualResponse(getIndividualResponse()), cumulative);
        assertSame(NhincConstants.DOC_QUERY_SERVICE_NAME, response.getServiceName());
        assertEquals(response.getResponse().getRegistryErrorList().getRegistryError().get(0).getErrorCode(),
                "XDSUnknownStoredQuery");

    }

    private OutboundDocQueryOrchestratable_a0 createCumulativeResponse(OutboundOrchestratableMessage individual) {

        AssertionType assertion = new AssertionType();
        OutboundDocQueryOrchestratable_a0 cumulativeResponse = new OutboundDocQueryOrchestratable_a0(null, null, null,
                null, assertion, NhincConstants.DOC_QUERY_SERVICE_NAME, createNhinTargetSystem(), createCloneRequest());
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType targetCommunity = new HomeCommunityType();
        targetCommunity.setHomeCommunityId("2.2");
        target.setHomeCommunity(targetCommunity);
        cumulativeResponse.setTarget(createNhinTargetSystem());
        cumulativeResponse.setRequest(createCloneRequest());
        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        newResponse.setStartIndex(BigInteger.ZERO);
        newResponse.setTotalResultCount(BigInteger.ZERO);
        cumulativeResponse.setCumulativeResponse(newResponse);
        return cumulativeResponse;
    }

    private OutboundDocQueryOrchestratable_a0 individualResponse(OutboundOrchestratableMessage individual) {

        AdhocQueryResponse newResponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        newResponse.setRegistryErrorList(regErrList);
        newResponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        OutboundDocQueryOrchestratable_a0 individualResponse = (OutboundDocQueryOrchestratable_a0) individual;
        RegistryError regErr = new RegistryError();
        regErr.setCodeContext("Unknown Stored Query query id" + "9a74-a90016b0af0d");
        regErr.setErrorCode("XDSUnknownStoredQuery");
        regErr.setSeverity(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR);
        regErrList.getRegistryError().add(regErr);
        individualResponse.setResponse(newResponse);
        return individualResponse;
    }

    private OutboundOrchestratableMessage getIndividualResponse() {
        AssertionType assertion = new AssertionType();

        OutboundDelegate nd = new OutboundDocQueryDelegate();
        OutboundResponseProcessor np = null;
        OutboundDocQueryOrchestratable_a0 individualResp = new OutboundDocQueryOrchestratable_a0(nd, np, null, null,
                assertion, NhincConstants.DOC_QUERY_SERVICE_NAME, createNhinTargetSystem(), createCloneRequest());

        return individualResp;

    }

    private AdhocQueryRequest createCloneRequest() {
        AdhocQueryRequest clonedRequest = new AdhocQueryRequest();
        AdhocQueryType adhocQuery = new AdhocQueryType();
        clonedRequest.setAdhocQuery(adhocQuery);
        adhocQuery.setHome("urn:oid:2.2");
        SlotType1 patientIdSlot = new SlotType1();
        adhocQuery.getSlot().add(patientIdSlot);
        patientIdSlot.setName("$XDSDocumentEntryPatientId");
        ValueListType valueList = new ValueListType();
        patientIdSlot.setValueList(valueList);
        valueList.getValue().add("'500000000^^^&amp;1.1&amp;ISO'");
        clonedRequest.setAdhocQuery(adhocQuery);
        return clonedRequest;
    }

    private NhinTargetSystemType createNhinTargetSystem() {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType targetCommunity = new HomeCommunityType();
        targetCommunity.setHomeCommunityId("2.2");
        target.setHomeCommunity(targetCommunity);
        return target;
    }

}