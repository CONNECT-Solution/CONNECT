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
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Arthur Kong
 */
public class HomeCommunityMapTest {

    ExchangeManager exManager = mock(ExchangeManager.class);

    PropertyAccessor accessor = mock(PropertyAccessor.class);

    public HomeCommunityMapTest() {
    }

    protected OrganizationType createOrganization(String orgName) {
        OrganizationType org = new OrganizationType();
        org.setName(orgName);

        return org;
    }

    @Test
    public void testGetHomeCommunityName() {

        final String homeCommunityName = "DoD";

        try {
            String homeCommunityId = "1.1";

            HomeCommunityMap.setExchangeManager(exManager);

            when(exManager.getOrganization(Mockito.anyString())).thenReturn(createOrganization(homeCommunityName));

            String foundName = HomeCommunityMap.getHomeCommunityName(homeCommunityId);
            assertEquals(homeCommunityName, foundName);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error running testGetHomeCommunityName test: " + e.getMessage());
        }
    }

    @Test
    public void testGetHomeCommunityNameNull() {

        try {
            String homeCommunityId = "123456";

            HomeCommunityMap.setExchangeManager(exManager);

            when(exManager.getOrganization(Mockito.anyString())).thenReturn(null);

            String foundName = HomeCommunityMap.getHomeCommunityName(homeCommunityId);
            assertEquals("", foundName);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error running testGetHomeCommunityName test: " + e.getMessage());
        }
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

    public void testGetCommunityIdFromAssertionNull() {
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

    }

    @Test(expected = NullPointerException.class)
    public void testGetCommunityIdFromAssertion() {
        String communityId;
        AssertionType assertion = new AssertionType();
        assertion.getUserInfo().getOrg().setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals("1.1", communityId);

        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("1.1");
        assertion.setHomeCommunity(home);
        assertion.setUserInfo(null);
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals("1.1", communityId);

        UserType info = new UserType();
        HomeCommunityType org1 = new HomeCommunityType();
        org1.setHomeCommunityId("");
        info.setOrg(org1);
        assertion.setUserInfo(info);
        HomeCommunityType homecommunity = new HomeCommunityType();
        homecommunity.setHomeCommunityId("1.1");
        communityId = HomeCommunityMap.getCommunityIdFromAssertion(assertion);
        assertEquals("1.1", communityId);
    }

    @Test
    public void testGetCommunityIdForDeferredQDRequest() {
        String communityId = HomeCommunityMap.getCommunityId(null);
        assertEquals(null, communityId);

        AdhocQueryType doc = new AdhocQueryType();
        communityId = HomeCommunityMap.getCommunityId(doc);
        assertEquals(null, communityId);

        doc.setHome("1.1");
        communityId = HomeCommunityMap.getCommunityId(doc);
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
        assertEquals("urn:oid:1.1", communityId);
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
        assertEquals("1.1", communityId);

        docResponse.setHomeCommunityId("urn:oid:1.1");
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("1.1", communityId);

        docResponse.setHomeCommunityId("randomstring");
        communityId = HomeCommunityMap.getCommunityIdForDeferredRDResponse(doc);
        assertEquals("randomstring", communityId);
    }

    @Test
    public void testGetLocalHomeCommunityId() throws PropertyAccessException {

        final String localCommunityId = "1.1";
        HomeCommunityMap.setPropertyAccessor(accessor);

        when(accessor.getProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(localCommunityId);

        String retrievedId = HomeCommunityMap.formatHomeCommunityId(HomeCommunityMap.getLocalHomeCommunityId());
        assertEquals(localCommunityId, retrievedId);
    }
}
