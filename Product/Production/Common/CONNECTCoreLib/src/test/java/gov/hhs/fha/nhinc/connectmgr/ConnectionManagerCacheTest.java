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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.uddi.api_v3.BusinessEntity;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 *
 * @author Arthur Kong, msw
 */
public class ConnectionManagerCacheTest extends BaseConnctionManagerCache {

    private PropertyAccessor accessor;

    protected ConnectionManagerCache createConnectionManager_Empty() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/emptyBusinessDetailConnectionInfo.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/emptyBusinessDetailConnectionInfo.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager_EmptyBusinessEntity() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/emptyBusinessEntityConnectionInfo.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/emptyBusinessEntityConnectionInfo.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager() throws ConnectionManagerException {

        accessor = mock(PropertyAccessor.class);
        return new ConnectionManagerCache(accessor) {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/uddiConnectionInfoTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/internalConnectionInfoTest.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager_Override() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/uddiConnectionInfoMergeTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/internalConnectionInfoMergeTest.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager_Merge() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/smallUddiConnectionInfoTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                    "/config/ConnectionManagerCacheTest/smallInternalConnectionInfoTest.xml");
            }
        };
    }

    @Test
    public void testGetInstance() {
        ConnectionManagerCache connectionManager = ConnectionManagerCache.getInstance();
        assertNotNull(connectionManager);

        ConnectionManagerCache connectionManager2 = ConnectionManagerCache.getInstance();
        assertEquals(connectionManager, connectionManager2);

        UddiConnectionInfoDAOFileImpl uddiDAO = connectionManager.getUddiConnectionManagerDAO();
        assertNotNull(uddiDAO);

        InternalConnectionInfoDAOFileImpl internalDAO = connectionManager.getInternalConnectionManagerDAO();
        assertNotNull(internalDAO);
    }

    @Test
    public void testGetAllCommunities_EmptyBusinessDetail() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_Empty();
            assertTrue(connectionManager.getAllBusinessEntities().isEmpty());
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessDetail test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities_EmptyBusinessEntity() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_EmptyBusinessEntity();
            assertTrue(connectionManager.getAllBusinessEntities().isEmpty());
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessEntity test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            assertEquals(2, connectionManager.getAllBusinessEntities().size());
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetAllCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testSetCommunityId() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            BusinessEntity businessEntity = connectionManager.getBusinessEntity(HCID_1);
            ConnectionManagerCacheHelper helper = new ConnectionManagerCacheHelper();
            String HCID_3 = "3.3";
            connectionManager.setCommunityId(businessEntity, HCID_3);
            String newHCID = helper.getCommunityId(businessEntity);
            assertTrue(newHCID.equals(HCID_3));
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testSetCommunityId test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllBusinessEntities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            List<BusinessEntity> entities = connectionManager.getAllBusinessEntities();
            assertEquals(2, entities.size());

            connectionManager = createConnectionManager_Override();
            entities = connectionManager.getAllBusinessEntities();
            assertEquals(3, entities.size());
            BusinessEntity businessEntity = connectionManager.getBusinessEntity("1.1");
            assertEquals(3, businessEntity.getBusinessServices().getBusinessService().size());

            // Merge business services
            connectionManager = createConnectionManager_Merge();
            entities = connectionManager.getAllBusinessEntities();
            assertEquals(2, entities.size());

            // Override 1.1 DQ with internal url
            businessEntity = connectionManager.getBusinessEntity("1.1");
            assertEquals(3, businessEntity.getBusinessServices().getBusinessService().size());
            String url = connectionManager.getDefaultEndpointURLByServiceName("1.1", QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals("https://localhost:8181/InternalQueryForDocuments"));

            // No override 2.2 DQ from UDDI
            businessEntity = connectionManager.getBusinessEntity("2.2");
            assertEquals(1, businessEntity.getBusinessServices().getBusinessService().size());
            url = connectionManager.getDefaultEndpointURLByServiceName("2.2", QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals("https://server4:8181/UddiQueryForDocuments"));

        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetAllBusinessEntities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBusinessEntityByHCID() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            assertNotNull(connectionManager.getBusinessEntityByHCID(HCID_1));
            assertNotNull(connectionManager.getBusinessEntityByHCID(HCID_2));
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running getBusinessEntityByHCID test: " + t.getMessage());
        }
    }

    @Test
    public void testMerging() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_Override();

            String url = connectionManager.getDefaultEndpointURLByServiceName(HCID_2, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL_22, url);

            url = connectionManager.getDefaultEndpointURLByServiceName(HCID_2, "QueryForDocument");
            assertEquals(QUERY_FOR_DOCUMENTS_URL_22, url);

            url = connectionManager.getDefaultEndpointURLByServiceName(HCID_1, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL, url);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testMerging test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBusinessEntitySet() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<String> hcidList = new ArrayList<>();
            hcidList.add(HCID_1);
            hcidList.add(HCID_2);
            Set<BusinessEntity> entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertEquals(2, entitySet.size());

            entitySet = connectionManager.getBusinessEntitySet(null);
            assertNull(entitySet);

            hcidList = new ArrayList<>();
            hcidList.add("hcidValue1123");
            hcidList.add("hcidValue2123");
            entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertNull(entitySet);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetBusinessEntitySet test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLByServiceName() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getDefaultEndpointURLByServiceName(HCID_1, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL, url);

            url = connectionManager.getDefaultEndpointURLByServiceName("hcidValue123", QUERY_FOR_DOCUMENTS_NAME);
            assertEquals("", url);

            url = connectionManager.getDefaultEndpointURLByServiceName(HCID_2, DOC_QUERY_DEFERRED_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_DEFERRED_URL_22, url);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetEndpointURLByServiceName test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLocalEndpointURLByServiceName() throws Exception {
        ConnectionManagerCache connectionManager = createConnectionManager();
        String localHomeCommunityId = "1.1";
        when(accessor.getProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(localHomeCommunityId);

        String url = connectionManager.getInternalEndpointURLByServiceName(QUERY_FOR_DOCUMENTS_NAME);
        assertEquals(QUERY_FOR_DOCUMENTS_URL, url);

        url = connectionManager.getInternalEndpointURLByServiceName("serviceNameValue123");
        assertEquals("", url);

        url = connectionManager.getInternalEndpointURLByServiceName(DOCUMENT_SUBMISSION_NAME);
        assertEquals("", url);
    }

    protected NhinTargetSystemType createNhinTargetSystem() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        EndpointReferenceType endpointReference = new EndpointReferenceType();
        AttributedURIType address = new AttributedURIType();
        address.setValue(NHIN_TARGET_ENDPOINT_URL_VALUE);
        endpointReference.setAddress(address);
        targetSystem.setEpr(endpointReference);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_UrlOnly() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(NHIN_TARGET_ENDPOINT_URL_VALUE);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_HCIDOnly() {
        return createNhinTargetSystem_HCIDOnly(HCID_1, null);
    }

    protected NhinTargetSystemType createNhinTargetSystem_HCIDOnly(String hcid, String userSpec) {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        targetSystem.setHomeCommunity(homeCommunity);
        targetSystem.setUseSpecVersion(userSpec);
        return targetSystem;
    }

    @Test
    public void testGetEndpointURLFromNhinTarget() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getEndpointURLFromNhinTarget(createNhinTargetSystem(),
                QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(NHIN_TARGET_ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpointURLFromNhinTarget(createNhinTargetSystem_UrlOnly(),
                QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(NHIN_TARGET_ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpointURLFromNhinTarget(createNhinTargetSystem_HCIDOnly(),
                QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_URL));
            url = connectionManager.getEndpointURLFromNhinTarget(
                createNhinTargetSystem_HCIDOnly(HCID_2, VERSION_OF_SERVICE_2_0), QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL_2, url);
            url = connectionManager.getEndpointURLFromNhinTarget(
                createNhinTargetSystem_HCIDOnly(HCID_2, VERSION_OF_SERVICE_3_0), QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL_3, url);
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetEndpointURLFromNhinTarget test: " + t.getMessage());
        }
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunites() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(FL_REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);

        return targetCommunities;
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunitesForNullendPoints() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_3);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(FL_REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);

        return targetCommunities;
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunitesWithDuplicateTargetCommunities() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(FL_REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);

        NhinTargetCommunityType targetCommunityStateOnly = new NhinTargetCommunityType();
        targetCommunityStateOnly.setRegion(FL_REGION_VALUE);
        targetCommunityStateOnly.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunityStateOnly);

        return targetCommunities;
    }

    @Ignore
    public void testGetEndpointURLFromNhinTargetCommunities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<UrlInfo> endpointUrlList = connectionManager
                .getEndpointURLFromNhinTargetCommunities(createNhinTargetCommunites(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(endpointUrlList.get(0).getUrl().equals(QUERY_FOR_DOCUMENTS_URL));

            endpointUrlList = connectionManager.getEndpointURLFromNhinTargetCommunities(null, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(2, endpointUrlList.size());

        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetEndpointURLFromNhinTargetCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLFromNhinTargetCommunitiesForNullEndPoints() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<UrlInfo> endpointUrlList = connectionManager.getEndpointURLFromNhinTargetCommunities(
                createNhinTargetCommunitesForNullendPoints(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(endpointUrlList.get(0).getUrl().equals(QUERY_FOR_DOCUMENTS_NULL_URL));

        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetEndpointURLFromNhinTargetCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLFromNhinTargetCommunitiesUniqueness() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<UrlInfo> endpointUrlList = connectionManager.getEndpointURLFromNhinTargetCommunities(
                createNhinTargetCommunitesWithDuplicateTargetCommunities(), QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(1, endpointUrlList.size());

        } catch (Throwable t) {
            t.printStackTrace();
            fail("Error running testGetEndpointURLFromNhinTargetCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterEndpointURL() throws ConnectionManagerException, PropertyAccessException {

        ConnectionManagerCache connectionManager = createConnectionManager();
        String localHomeCommunityId = "1.1";
        when(accessor.getProperty(Mockito.anyString(), Mockito.anyString())).thenReturn(localHomeCommunityId);

        String url = connectionManager.getAdapterEndpointURL(QUERY_FOR_DOCUMENTS_NAME, ADAPTER_API_LEVEL.LEVEL_a0);
        assertNull(url);

        url = connectionManager.getAdapterEndpointURL(DOC_QUERY_DEFERRED_NAME, ADAPTER_API_LEVEL.LEVEL_a0);
        assertNotNull(url);
        assertTrue(url.equals(QUERY_FOR_DOCUMENTS_DEFERRED_URL));

    }

}
