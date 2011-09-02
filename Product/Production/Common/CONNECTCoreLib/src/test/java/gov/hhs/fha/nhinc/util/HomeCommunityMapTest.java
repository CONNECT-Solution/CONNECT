/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Arthur Kong
 */
public class HomeCommunityMapTest {

    private HomeCommunityMap hMap;

    public HomeCommunityMapTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        hMap = new HomeCommunityMap();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetHomeCommunityName() {

        // The id and name are read from the internalconnections.xml file
        // whose location is defined in the properties variable
        String homeCommunityId = "1.1";
        String homeCommunityName = "DoD";
        
        String foundName = hMap.getHomeCommunityName(homeCommunityId);
        assertEquals(homeCommunityName, foundName);

        homeCommunityId = "123456";
        foundName = hMap.getHomeCommunityName(homeCommunityId);
        assertEquals("", foundName);
    }

    @Test
    public void testGetCommunityIdFromTargetCommunities() {
        String communityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(null);
        assertEquals(null, communityId);

        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        communityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(target);
        assertEquals(null, communityId);

        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        targetCommunity.setHomeCommunity(home);
        target.getNhinTargetCommunity().add(targetCommunity);
        communityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(target);
        assertEquals(null, communityId);

        target.getNhinTargetCommunity().get(0).getHomeCommunity().setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdFromTargetCommunities(target);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdFromTargetSystem() {
        String communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(null);
        assertEquals(null, communityId);

        NhinTargetSystemType target = new NhinTargetSystemType();
        communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
        assertEquals(null, communityId);

        HomeCommunityType home = new HomeCommunityType();
        target.setHomeCommunity(home);
        communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
        assertEquals(null, communityId);

        target.getHomeCommunity().setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdFromAssertion() {
        String communityId = HomeCommunityMap.getCommunityIdFromAssertion(null);
        assertEquals(null, communityId);

        AssertionType assertion = new AssertionType();
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals(null, communityId);

        UserType userInfo = new UserType();
        assertion.setUserInfo(userInfo);
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals(null, communityId);

        HomeCommunityType org = new HomeCommunityType();
        assertion.getUserInfo().setOrg(org);
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals(null, communityId);
        
        assertion.getUserInfo().getOrg().setHomeCommunityId("");
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals(null, communityId);

        assertion.getUserInfo().getOrg().setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals("1.1", communityId);

        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("1.1");
        assertion.setHomeCommunity(home);
        assertion.setUserInfo(null);
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdForDeferredQDRequest() {
        String communityId = HomeCommunityMap.getCommunityIdForDeferredQDRequest(null);
        assertEquals(null, communityId);

        AdhocQueryType doc = new AdhocQueryType();
        communityId = HomeCommunityMap.getCommunityIdForDeferredQDRequest(doc);
        assertEquals(null, communityId);

        doc.setHome("1.1");
        communityId = HomeCommunityMap.getCommunityIdForDeferredQDRequest(doc);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdForDeferredQDResponse() {
        String communityId = HomeCommunityMap.getCommunityIdForDeferredQDResponse(null);
        assertEquals(null, communityId);

        AdhocQueryResponse doc = new AdhocQueryResponse();
        communityId = HomeCommunityMap.getCommunityIdForDeferredQDResponse(doc);
        assertEquals(null, communityId);
    }

    @Test
    public void testGetCommunityIdForRDRequest() {
        String communityId = HomeCommunityMap.getCommunityIdForRDRequest(null);
        assertEquals(null, communityId);

        RetrieveDocumentSetRequestType doc = new RetrieveDocumentSetRequestType();
        communityId = HomeCommunityMap.getCommunityIdForRDRequest(doc);
        assertEquals(null, communityId);

        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setHomeCommunityId("1.1");
        doc.getDocumentRequest().add(docRequest);
        communityId = HomeCommunityMap.getCommunityIdForRDRequest(doc);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdForDeferredRDResponse() {
        String communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(null);
        assertEquals(null, communityId);

        RetrieveDocumentSetResponseType doc = new RetrieveDocumentSetResponseType();
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals(null, communityId);

        DocumentResponse docResponse = new DocumentResponse();
        doc.getDocumentResponse().add(docResponse);
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals(null, communityId);

        doc.getDocumentResponse().get(0).setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testFormatHomeCommunityId() {
        RetrieveDocumentSetResponseType doc = new RetrieveDocumentSetResponseType();
        DocumentResponse docResponse = new DocumentResponse();
        docResponse.setHomeCommunityId("1.1");
        doc.getDocumentResponse().add(docResponse);

        String communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("1.1",  communityId);

        docResponse.setHomeCommunityId("urn:oid:1.1");
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("1.1", communityId);

        docResponse.setHomeCommunityId("randomstring");
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("randomstring", communityId);
    }

    @Test
    public void testGetLocalHomeCommunityId() {
        String localCommunityId = "1.1";

        String retrievedId = HomeCommunityMap.getLocalHomeCommunityId();
        assertEquals(localCommunityId, retrievedId);
    }
}