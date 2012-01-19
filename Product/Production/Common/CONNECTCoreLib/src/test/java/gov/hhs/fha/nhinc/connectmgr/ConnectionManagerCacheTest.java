package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.EPRType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache2.UrlInfo;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.BusinessServices;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.IdentifierBag;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;
import static org.junit.Assert.*;

/**
 *
 * @author Arthur Kong
 */
public class ConnectionManagerCacheTest {

    private static String HCID_1 = "1.1";
    private static String HCID_2 = "2.2";
    private static String SERVICE_NAME_VALUE = "TestService";
    private static String ENDPOINT_URL_VALUE = "http://localhost:8080/";
    private static String REGION_VALUE = "DC";

    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final UddiConnectionInfoDAOFileImpl mockUddiDAO = context.mock(UddiConnectionInfoDAOFileImpl.class);
    final InternalConnectionInfoDAOFileImpl mockInternalDAO = context.mock(InternalConnectionInfoDAOFileImpl.class);

    protected ConnectionManagerCache2 createConnectionManager() throws ConnectionManagerException {
        return new ConnectionManagerCache2() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return mockUddiDAO;
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return mockInternalDAO;
            }
        };
    }

    protected ConnectionManagerCache2 createConnectionManagerWithExpectations() throws Exception {
        ConnectionManagerCache2 connectionManager = createConnectionManager();
        context.checking(new Expectations()
        {
            {
                allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_2)));
                allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
            }
        });

        return connectionManager;
    }

    protected BusinessDetail createNullBusinessDetail() {
        return null;
    }

    protected BusinessDetail createEmptyBusinessDetail() {
        return new BusinessDetail();
    }

    protected BusinessDetail createEmptyBusinessEntity() {
        BusinessDetail result = new BusinessDetail();
        BusinessEntity bEntity = new BusinessEntity();

        bEntity.setCategoryBag(null);
        result.getBusinessEntity().add(bEntity);

        return result;

    }

    protected BusinessDetail createBusinessDetail(String hcidValue, String serviceNameValue) {
        BusinessDetail bDetail = new BusinessDetail();
        BusinessEntity bEntity = new BusinessEntity();

        // One Business Service Definitions (ex. QueryForDocuments)
        BusinessService bService = new BusinessService();
        Name serviceName = new Name();
        serviceName.setValue(serviceNameValue);
        bService.getName().add(serviceName);

        BindingTemplates bindingTemplates = new BindingTemplates();
        BindingTemplate bindingTemplate = new BindingTemplate();

        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setValue(ENDPOINT_URL_VALUE);
        
        KeyedReference uddiSpecKey = new KeyedReference();
        uddiSpecKey.setTModelKey("uddi:nhin:versionofservice");
        uddiSpecKey.setKeyName("");
        uddiSpecKey.setKeyValue(UDDI_SPEC_VERSION.SPEC_1_0.toString());
        
        KeyedReference adapterKey = new KeyedReference();
        adapterKey.setTModelKey("apiLevel");
        adapterKey.setKeyName("");
        adapterKey.setKeyValue(ADAPTER_API_LEVEL.LEVEL_a0.toString());
        CategoryBag versionCategoryBag = new CategoryBag();

        versionCategoryBag.getKeyedReference().add(adapterKey);
        versionCategoryBag.getKeyedReference().add(uddiSpecKey);

        bindingTemplate.setAccessPoint(accessPoint);
        bindingTemplate.setCategoryBag(versionCategoryBag);
        bindingTemplates.getBindingTemplate().add(bindingTemplate);
        bService.setBindingTemplates(bindingTemplates);
    
        KeyedReference hcidKey = new KeyedReference();
        hcidKey.setTModelKey("uddi:nhin:nhie:homecommunityid");
        hcidKey.setKeyName("");
        hcidKey.setKeyValue(hcidValue);
        IdentifierBag hcidIdentifierBag = new IdentifierBag();
        hcidIdentifierBag.getKeyedReference().add(hcidKey);

        KeyedReference stateKey = new KeyedReference();
        stateKey.setTModelKey("uddi:uddi.org:ubr:categorization:iso3166");
        stateKey.setKeyName("");
        stateKey.setKeyValue(REGION_VALUE);
        CategoryBag stateCategoryBag = new CategoryBag();
        stateCategoryBag.getKeyedReference().add(stateKey);

        
        bDetail.getBusinessEntity().add(bEntity);
        bEntity.setBusinessKey("uddi:testnhincnode:1.1");
        bEntity.setBusinessServices(new BusinessServices());
        bEntity.getBusinessServices().getBusinessService().add(bService);
        bEntity.setIdentifierBag(hcidIdentifierBag);
        bEntity.setCategoryBag(stateCategoryBag);
      
        return bDetail;
    }

    protected BusinessDetail createCorrectKeyBusinessEntity(String hcid) {
        return createBusinessDetail(hcid, SERVICE_NAME_VALUE);
    }

    @Test
    public void testGetInstance() {
        ConnectionManagerCache2 connectionManager = ConnectionManagerCache2.getInstance();
        assertNotNull(connectionManager);

        ConnectionManagerCache2 connectionManager2 = ConnectionManagerCache2.getInstance();
        assertEquals(connectionManager, connectionManager2);

        UddiConnectionInfoDAOFileImpl uddiDAO = connectionManager.getUddiConnectionManagerDAO();
        assertNotNull(uddiDAO);
        
        InternalConnectionInfoDAOFileImpl internalDAO = connectionManager.getInternalConnectionManagerDAO();
        assertNotNull(internalDAO);
    }
    
    @Test
    public void testGetAllCommunities_NullBusinessDetail() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createNullBusinessDetail()));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createNullBusinessDetail()));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            assertTrue(connectionManager.getAllCommunities().isEmpty());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_NullBusinessDetail test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities_EmptyBusinessDetail() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessDetail()));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessDetail()));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            assertTrue(connectionManager.getAllCommunities().isEmpty());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessDetail test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities_EmptyBusinessEntity() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessEntity()));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessEntity()));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            assertTrue(connectionManager.getAllCommunities().isEmpty());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessEntity test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities_CorrectKeyBusinessEntity() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    exactly(1).of(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_2)));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            assertEquals(2, connectionManager.getAllCommunities().size());

            connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    exactly(1).of(mockInternalDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessEntity()));
                }
            });
            assertEquals(1, connectionManager.getAllCommunities().size());

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_CorrectKeyBusinessEntity test: " + t.getMessage());
        }
    }

    @Test
    public void testRefreshIfExpired() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockUddiDAO).loadBusinessDetail(); will(returnValue(createNullBusinessDetail()));
                    exactly(1).of(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    exactly(1).of(mockInternalDAO).loadBusinessDetail(); will(returnValue(createNullBusinessDetail()));
                    exactly(1).of(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });
            
            List<BusinessEntity> communities = connectionManager.getAllBusinessEntities();
            assertEquals(0, communities.size(), 0);

            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(1L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_2)));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(1L));
                }
            });

            communities = connectionManager.getAllBusinessEntities();
            assertEquals(2, communities.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testRefreshIfExpired test: " + t.getMessage());
        }
    }

    @Test
    public void testRefreshIfExpired_NoneExpired() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockUddiDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessEntity()));
                    exactly(1).of(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    exactly(1).of(mockInternalDAO).loadBusinessDetail(); will(returnValue(createEmptyBusinessEntity()));
                    exactly(1).of(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            List<BusinessEntity> communities = connectionManager.getAllBusinessEntities();
            assertEquals(0, communities.size(), 0);

            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    allowing(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_2)));
                    allowing(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            communities = connectionManager.getAllBusinessEntities();
            assertEquals(0, communities.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testRefreshIfExpired_NoneExpired test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllBusinessEntities() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    exactly(1).of(mockUddiDAO).getLastModified(); will(returnValue(0L));
                    exactly(1).of(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    exactly(1).of(mockInternalDAO).getLastModified(); will(returnValue(0L));
                }
            });

            List<BusinessEntity> entities = connectionManager.getAllBusinessEntities();
            assertEquals(entities.size(), 1);

            connectionManager = createConnectionManager();
            context.checking(new Expectations()
            {
                {
                    allowing(mockUddiDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_1)));
                    exactly(1).of(mockUddiDAO).getLastModified(); will(returnValue(1L));
                    allowing(mockInternalDAO).loadBusinessDetail(); will(returnValue(createCorrectKeyBusinessEntity(HCID_2)));
                    exactly(1).of(mockInternalDAO).getLastModified(); will(returnValue(1L));
                }
            });

            entities = connectionManager.getAllBusinessEntities();
            assertEquals(entities.size(), 2);

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testRefreshIfExpired test: " + t.getMessage());
        }
    }
    
    @Test
    public void testGetBusinessEntity() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            BusinessEntity entity = connectionManager.getBusinessEntity(HCID_1);
            assertNotNull(entity);
            assertEquals(entity.getIdentifierBag().getKeyedReference().get(0).getKeyValue(), HCID_1);

            entity = connectionManager.getBusinessEntity("hcidValue123");
            assertNull(entity);

            entity = connectionManager.getBusinessEntity(HCID_2);
            assertNotNull(entity);
            assertEquals(entity.getIdentifierBag().getKeyedReference().get(0).getKeyValue(), HCID_2);

            entity = connectionManager.getBusinessEntity("hcidValue123");
            assertNull(entity);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetBusinessEntity test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBusinessEntitySet() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            List<String> hcidList = new ArrayList<String>();
            hcidList.add(HCID_1);
            hcidList.add(HCID_2);
            Set<BusinessEntity> entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertEquals(entitySet.size(), 2);

            entitySet = connectionManager.getBusinessEntitySet(null);
            assertNull(entitySet);

            hcidList = new ArrayList<String>();
            hcidList.add("hcidValue1123");
            hcidList.add("hcidValue2123");
            entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertNull(entitySet);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetBusinessEntitySet test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLByServiceName() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            String url = connectionManager.getEndpointURLByServiceName(HCID_1, SERVICE_NAME_VALUE, GATEWAY_API_LEVEL.LEVEL_g0);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpointURLByServiceName("hcidValue123", SERVICE_NAME_VALUE, GATEWAY_API_LEVEL.LEVEL_g0);
            assertTrue(url.equals(""));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpointURLByServiceName test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLocalEndpointURLByServiceName() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();
            
            String url = connectionManager.getLocalEndpointURLByServiceName(SERVICE_NAME_VALUE);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));

            url = connectionManager.getLocalEndpointURLByServiceName("serviceNameValue123");
            assertTrue(url.equals(""));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpointURLByServiceName test: " + t.getMessage());
        }
    }

    protected NhinTargetSystemType createNhinTargetSystem() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        EPRType eprType = new EPRType();
        EndpointReferenceType endpointReference = new EndpointReferenceType();
        AttributedURI address = new AttributedURI();
        address.setValue(ENDPOINT_URL_VALUE);
        endpointReference.setAddress(address);
        eprType.setEndpointReference(endpointReference);
        targetSystem.setEpr(eprType);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_UrlOnly() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(ENDPOINT_URL_VALUE);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_HCIDOnly() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetSystem.setHomeCommunity(homeCommunity);

        return targetSystem;
    }

    @Test
    public void testGetEndpontURLFromNhinTarget() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            String url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem(), SERVICE_NAME_VALUE);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem_UrlOnly(), SERVICE_NAME_VALUE);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem_HCIDOnly(), SERVICE_NAME_VALUE);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpontURLFromNhinTarget test: " + t.getMessage());
        }
    }


    protected NhinTargetCommunitiesType createNhinTargetCommunites() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);

        return targetCommunities;
    }

    @Test
    public void testGetEndpontURLFromNhinTargetCommunities() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            List<UrlInfo> endpointUrlList = connectionManager.getEndpontURLFromNhinTargetCommunities(createNhinTargetCommunites(), SERVICE_NAME_VALUE);
            assertTrue(endpointUrlList.get(0).getUrl().equals(ENDPOINT_URL_VALUE));

            endpointUrlList = connectionManager.getEndpontURLFromNhinTargetCommunities(null, SERVICE_NAME_VALUE);
            assertEquals(2, endpointUrlList.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpontURLFromNhinTargetCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterEndpontURL() {
        try {
            ConnectionManagerCache2 connectionManager = createConnectionManagerWithExpectations();

            String url = connectionManager.getAdapterEndpontURL(SERVICE_NAME_VALUE, ADAPTER_API_LEVEL.LEVEL_a0);
            assertTrue(url.equals(ENDPOINT_URL_VALUE));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAdapterEndpontURL test: " + t.getMessage());
        }

    }
    
}
