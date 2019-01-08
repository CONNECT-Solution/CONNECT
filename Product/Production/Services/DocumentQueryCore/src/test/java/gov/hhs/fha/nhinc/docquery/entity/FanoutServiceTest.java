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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.docquery.outbound.StandardOutboundDocQueryHelper;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.logging.transaction.TransactionLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyFactory;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201304UV02Patient;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author bhumphrey
 *
 */
public class FanoutServiceTest {

    PatientCorrelationProxyFactory patientCorrelationProxyFactory = mock(PatientCorrelationProxyFactory.class);
    ExchangeManager exchangeManager = mock(ExchangeManager.class);
    PixRetrieveBuilder pixRetrieveBuilder = mock(PixRetrieveBuilder.class);
    PatientCorrelationProxy patientCorrelationProxy = mock(PatientCorrelationProxy.class);
    StandardOutboundDocQueryHelper standardDocQueryHelper = mock(StandardOutboundDocQueryHelper.class);
    TransactionLogger transactionLogger = mock(TransactionLogger.class);
    AggregationService service = new AggregationService(exchangeManager, patientCorrelationProxyFactory,
        pixRetrieveBuilder, standardDocQueryHelper, transactionLogger);

    @Test
    public void createChildRequestsSingle() throws ExchangeManagerException {
        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());
        NhinTargetCommunitiesType targets = createNhinTargetCommunites();
        AssertionType assertion = new AssertionType();

        List<UrlInfo> urlInfoList = new ArrayList<>();
        urlInfoList.add(createUrlInfo("4.4", ""));

        when(patientCorrelationProxyFactory.getPatientCorrelationProxy()).thenReturn(patientCorrelationProxy);

        when(
            exchangeManager.getEndpointURLFromNhinTargetCommunities(eq(targets),
                eq(NhincConstants.DOC_QUERY_SERVICE_NAME))).thenReturn(urlInfoList);

        II ii = new II();
        ii.setRoot("4.4");
        ii.setExtension("D123401");

        RetrievePatientCorrelationsResponseType retrievePatientCorrelationsResponseType
            = createPatientCorrelationResponseType(ii);

        when(patientCorrelationProxy.retrievePatientCorrelations(any(PRPAIN201309UV02.class), eq(assertion)))
            .thenReturn(retrievePatientCorrelationsResponseType);

        when(standardDocQueryHelper.lookupHomeCommunityId(any(String.class), any(String.class))).thenReturn(
            new HomeCommunityType() {
            @Override
            public String getHomeCommunityId() {
                return "1.1";
            }
        });

        List<OutboundOrchestratable> results = service.createChildRequests(adhocQueryRequest, assertion, targets);
        assertEquals(1, results.size());

    }

    /**
     * @param ii
     * @return
     */
    public RetrievePatientCorrelationsResponseType createPatientCorrelationResponseType(II ii) {
        RetrievePatientCorrelationsResponseType retrievePatientCorrelationsResponseType
            = new RetrievePatientCorrelationsResponseType();
        retrievePatientCorrelationsResponseType.setPRPAIN201310UV02(new PRPAIN201310UV02());

        retrievePatientCorrelationsResponseType.getPRPAIN201310UV02().setControlActProcess(
            new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess());

        PRPAIN201310UV02MFMIMT700711UV01Subject1 e = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        retrievePatientCorrelationsResponseType.getPRPAIN201310UV02().getControlActProcess().getSubject().add(e);

        e.setRegistrationEvent(new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent());

        e.getRegistrationEvent().setSubject1(new PRPAIN201310UV02MFMIMT700711UV01Subject2());

        e.getRegistrationEvent().getSubject1().setPatient(new PRPAMT201304UV02Patient());

        e.getRegistrationEvent().getSubject1().getPatient().getId().add(ii);
        return retrievePatientCorrelationsResponseType;
    }

    /**
     * @param hcid
     * @param url
     * @return
     */
    public UrlInfo createUrlInfo(String hcid, String url) {
        UrlInfo urlInfo = new UrlInfo();
        urlInfo.setHcid(hcid);
        urlInfo.setUrl(url);
        return urlInfo;
    }

    @Test
    public void cloneRequest() {

        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());

        AdhocQueryRequest result = service.cloneRequest(adhocQueryRequest);

        assertSame(adhocQueryRequest, result);

    }

    @Test
    public void hasidentities() {
        RetrievePatientCorrelationsResponseType results = null;
        assertFalse(service.hasIdentities(results));

        results = new RetrievePatientCorrelationsResponseType();
        assertFalse(service.hasIdentities(results));

        results.setPRPAIN201310UV02(new PRPAIN201310UV02());
        assertFalse(service.hasIdentities(results));

        results.getPRPAIN201310UV02().setControlActProcess(new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess());
        assertFalse(service.hasIdentities(results));

        PRPAIN201310UV02MFMIMT700711UV01Subject1 e = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        results.getPRPAIN201310UV02().getControlActProcess().getSubject().add(e);
        assertFalse(service.hasIdentities(results));

        e.setRegistrationEvent(new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent());
        assertFalse(service.hasIdentities(results));

        e.getRegistrationEvent().setSubject1(new PRPAIN201310UV02MFMIMT700711UV01Subject2());
        assertFalse(service.hasIdentities(results));

        e.getRegistrationEvent().getSubject1().setPatient(new PRPAMT201304UV02Patient());
        assertFalse(service.hasIdentities(results));

        II ii = new II();
        ii.setAssigningAuthorityName("aaaaaaaaaaa");
        e.getRegistrationEvent().getSubject1().getPatient().getId().add(ii);

        assertTrue(service.hasIdentities(results));

    }

    @Test
    public void getQualifiedSubjectIndentifer() {

        AdhocQueryRequest adhocQueryRequest = createRequest(createSlotList());

        QualifiedSubjectIdentifierType result = service.getQualifiedSubjectIndentifer(adhocQueryRequest);

        assertEquals("<home community id>", result.getAssigningAuthorityIdentifier());
        assertEquals("<id>", result.getSubjectIdentifier());

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

    /**
     * @param targetCommunity
     * @return
     */
    public NhinTargetCommunitiesType createTargetCommunities(NhinTargetCommunityType... targetCommunities) {
        NhinTargetCommunitiesType targetCommunitiesType = new NhinTargetCommunitiesType();

        for (NhinTargetCommunityType targetCommunity : targetCommunities) {
            targetCommunitiesType.getNhinTargetCommunity().add(targetCommunity);
        }

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

    private NhinTargetCommunitiesType createNhinTargetCommunitesForMultipleTargets() {
        return createTargetCommunities(createTargetCommunity("4.4", "US-FL", "Unimplemented"),
            createTargetCommunity("2.2", null, null));
    }

    private NhinTargetCommunitiesType createNhinTargetCommunites() {
        return createTargetCommunities(createTargetCommunity("4.4", "US-FL", "Unimplemented"));
    }

    private void assertSame(AdhocQueryRequest a, AdhocQueryRequest b) {
        AdhocQueryType currentQuery = a.getAdhocQuery();
        AdhocQueryType newQuery = b.getAdhocQuery();

        assertSame(currentQuery, newQuery);

        assertEquals(a.getComment(), b.getComment());
        assertEquals(a.getFederation(), b.getFederation());
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getMaxResults(), b.getMaxResults());
        assertEquals(a.getResponseOption(), b.getResponseOption());
        assertEquals(a.getStartIndex(), b.getStartIndex());

    }

    private void assertSame(AdhocQueryType query1, AdhocQueryType query2) {
        List<SlotType1> slotList1 = query2.getSlot();

        List<SlotType1> slotList2 = query1.getSlot();

        assertSame(slotList1, slotList2);

        assertEquals(query1.getDescription(), query1.getDescription());

        assertEquals(query2.getHome(), query1.getHome());

        assertEquals(query2.getId(), query1.getId());

        assertEquals(query2.getLid(), query1.getLid());

        assertEquals(query2.getName(), query1.getName());

        assertEquals(query2.getObjectType(), query1.getObjectType());
        assertEquals(query2.getQueryExpression(), query1.getQueryExpression());
        assertEquals(query2.getStatus(), query1.getStatus());

        assertEquals(query2.getVersionInfo(), query1.getVersionInfo());

    }

    private void assertSame(List<SlotType1> slotList, List<SlotType1> newslotList) {
        assertTrue(slotList.size() == newslotList.size());

        for (int count = 0; count < slotList.size(); count++) {

            SlotType1 slot1 = slotList.get(count);
            SlotType1 slot2 = slotList.get(count);

            assertEquals(slot1.getName(), slot2.getName());

            assertEquals(slot1.getSlotType(), slot2.getSlotType());

            assertSame(slot1.getValueList(), slot2.getValueList());

        }
    }

    private void assertSame(ValueListType list1, ValueListType list2) {
        List<String> values1 = list1.getValue();
        List<String> values2 = list2.getValue();

        assertTrue(values1.size() == values2.size());

        for (int index = 0; index < values1.size(); index++) {
            assertEquals(values1.get(index), values2.get(index));
        }
    }
}
