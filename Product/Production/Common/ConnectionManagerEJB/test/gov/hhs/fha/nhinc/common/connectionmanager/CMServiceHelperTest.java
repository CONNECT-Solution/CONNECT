package gov.hhs.fha.nhinc.common.connectionmanager;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntitiesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessServiceType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntityType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfosType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ContactType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ForceRefreshInternalConnectCacheRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ForceRefreshUDDICacheRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllBusinessEntitiesRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllBusinessEntitySetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllCommunitiesRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllConnectionInfoEndpointSetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAllConnectionInfoSetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetAssigningAuthoritiesByHomeCommunityResponseType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntityByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntityRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntitySetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetBusinessEntitySetRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointSetByServiceNameRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.GetConnectionInfoEndpointSetRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunitiesWithServiceNameType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.HomeCommunityWithServiceNameType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfoEndpointType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfoType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.StoreAssigningAuthorityToHomeCommunityMappingRequestType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.SuccessOrFailType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.UniformServiceNameType;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssigningAuthorityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import java.util.List;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
public class CMServiceHelperTest {

    public CMServiceHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        // Point to our local copy of the property files for testing purposes.  
        // This will pick up the ones in this project.
        //--------------------------------------------------------------------
        ConnectionManagerCache.overrideFileLocations("uddiConnectionInfo.xml", "internalConnectionInfo.xml");
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    /**
     * This method validates the entire contents of a home community for
     * HomeCommunity 1111.1111.1111.1111
     * 
     * @param oHomeCommunity The business entity for this home community.
     */
    private void validateHomeCommunity_1111_1111_1111_1111(HomeCommunityType oHomeCommunity)
    {
        assertNotNull(oHomeCommunity);
        assertEquals("1111.1111.1111.1111", oHomeCommunity.getHomeCommunityId());
        assertEquals("Home1", oHomeCommunity.getName());
        assertEquals("Home1 Description", oHomeCommunity.getDescription());
    }
    
    /**
     * This method validates the entire contents of a home community for
     * HomeCommunity 2222.2222.2222.2222
     * 
     * @param oHomeCommunity The business entity for this home community.
     */
    private void validateHomeCommunity_2222_2222_2222_2222(HomeCommunityType oHomeCommunity)
    {
        assertNotNull(oHomeCommunity);
        assertEquals("2222.2222.2222.2222", oHomeCommunity.getHomeCommunityId());
        assertEquals("Home2", oHomeCommunity.getName());
        assertEquals("Home2 Description", oHomeCommunity.getDescription());
    }
    
    /**
     * This method validates the entire contents of a home community for
     * HomeCommunity 1111.1111.1111.1111..2
     * 
     * @param oHomeCommunity The business entity for this home community.
     */
    private void validateHomeCommunity_1111_1111_1111_1111__2(HomeCommunityType oHomeCommunity)
    {
        assertNotNull(oHomeCommunity);
        assertEquals("1111.1111.1111.1111..2", oHomeCommunity.getHomeCommunityId());
        assertEquals("DuplicateFromUDDI Name", oHomeCommunity.getName());
        assertEquals("DuplicateFromUDDI Description", oHomeCommunity.getDescription());
    }    
    
    /**
     * This method validates the entire contents of a home community for
     * HomeCommunity 1111.1111.1111.1111..1
     * 
     * @param oHomeCommunity The business entity for this home community.
     */
    private void validateHomeCommunity_1111_1111_1111_1111__1(HomeCommunityType oHomeCommunity)
    {
        assertNotNull(oHomeCommunity);
        assertEquals("1111.1111.1111.1111..1", oHomeCommunity.getHomeCommunityId());
        assertEquals("BusinessName.1.1", oHomeCommunity.getName());
        assertEquals("BusinessDescription.1.1", oHomeCommunity.getDescription());
    }    
    
    /**
     * This method validates the entire contents of a connection info for
     * HomeCommunity 1111.1111.1111.1111
     * 
     * @param oConnInfo The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfoEndpoint_1111_1111_1111_1111(ConnectionInfoEndpointType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("Home1", oConnInfo.getHomeCommunity().getName());
        assertEquals("Home1 Description", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints());
        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoEndpointType oService : oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_3_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service1.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_3_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:3:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_3_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_3_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_4_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service2.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_4_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:4:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_4_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_4_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }

            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }
    
    /**
     * This method validates the entire contents of a ConnectionInfo for
     * HomeCommunity 1111.1111.1111.1111..1
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfoEndpoint_1111_1111_1111_1111__1(ConnectionInfoEndpointType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111..1", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("BusinessName.1.1", oConnInfo.getHomeCommunity().getName());
        assertEquals("BusinessDescription.1.1", oConnInfo.getHomeCommunity().getDescription());

        // Business Services
        //------------------
        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints());
        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoEndpointType oService : oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_1_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.test.com:8080/EndpointURL.1.1.1", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_1_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:1:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_1_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_1_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
           }
            else if (oService.getServiceName().equals("UnitTestServiceName_1_2"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.test.com:8080/EndpointURL.1.2.1", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_1_2", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:1:2", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_1_2", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_1_2", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 1111.1111.1111.1111..2
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfoEndpoint_1111_1111_1111_1111__2(ConnectionInfoEndpointType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111..2", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("BusinessName.2.1", oConnInfo.getHomeCommunity().getName());
        assertEquals("BusinessDescription.2.1", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints());
        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(4, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }

        boolean baFoundService[] = {false, false, false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoEndpointType oService : oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_7_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service5.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_7_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:7:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_7_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_7_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_8_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service6.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_8_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:8:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_8_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_8_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_2_1"))
            {
                baFoundService[2] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.test.com:8080/EndpointURL.2.1.1", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_2_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:2:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_2_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_2_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
           }
           else if (oService.getServiceName().equals("UnitTestServiceName_2_2"))
           {
                baFoundService[3] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.test.com:8080/EndpointURL.2.2.1", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_2_2", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:2:2", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_2_2", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_2_2", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
            assertTrue(baFoundService[2]);
            assertTrue(baFoundService[3]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 2222.2222.2222.2222
     * 
     * @param oConnInfo The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     * 
     */
    private void validateConnectionInfoEndpoint_2222_2222_2222_2222(ConnectionInfoEndpointType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("2222.2222.2222.2222", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("Home2", oConnInfo.getHomeCommunity().getName());
        assertEquals("Home2 Description", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints());
        assertNotNull(oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoEndpointType oService : oConnInfo.getServiceConnectionInfoEndpoints().getServiceConnectionInfoEndpoint())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_5_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service3.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_5_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:5:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_5_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_5_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_6_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getEPR());
                assertNotNull(oService.getEPR().getEndpointReference());
                assertNotNull(oService.getEPR().getEndpointReference().getAddress());
                assertEquals("http://www.service4.com", oService.getEPR().getEndpointReference().getAddress().getValue());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName());
                assertEquals("Port_6_1", oService.getEPR().getEndpointReference().getServiceName().getPortName());

                assertNotNull(oService.getEPR().getEndpointReference().getServiceName().getValue());
                assertEquals("urn:unit:test:6:1", oService.getEPR().getEndpointReference().getServiceName().getValue().getNamespaceURI());
                assertEquals("Service_6_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getLocalPart());
                assertEquals("Prefix_6_1", oService.getEPR().getEndpointReference().getServiceName().getValue().getPrefix());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }
    
    
    /**
     * This method validates the entire contents of a connection info for
     * HomeCommunity 1111.1111.1111.1111
     * 
     * @param oConnInfo The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfo_1111_1111_1111_1111(ConnectionInfoType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("Home1", oConnInfo.getHomeCommunity().getName());
        assertEquals("Home1 Description", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfos());
        assertNotNull(oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoType oService : oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_3_1"))
            {
                baFoundService[0] = true;
                assertEquals("http://www.service1.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service1.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_4_1"))
            {
                baFoundService[1] = true;
                assertEquals("http://www.service2.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service2.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }

            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }
    
    /**
     * This method validates the entire contents of a ConnectionInfo for
     * HomeCommunity 1111.1111.1111.1111..1
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfo_1111_1111_1111_1111__1(ConnectionInfoType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111..1", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("BusinessName.1.1", oConnInfo.getHomeCommunity().getName());
        assertEquals("BusinessDescription.1.1", oConnInfo.getHomeCommunity().getDescription());

        // Business Services
        //------------------
        assertNotNull(oConnInfo.getServiceConnectionInfos());
        assertNotNull(oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoType oService : oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_1_1"))
            {
                baFoundService[0] = true;
                assertEquals("http://www.test.com:8080/EndpointURL.1.1.1", oService.getUrl());
                assertEquals("/EndpointURL.1.1.1", oService.getFile());
                assertEquals("www.test.com", oService.getHost());
                assertEquals("/EndpointURL.1.1.1", oService.getPath());
                assertEquals("8080", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_1_2"))
            {
                baFoundService[1] = true;
                assertEquals("http://www.test.com:8080/EndpointURL.1.2.1", oService.getUrl());
                assertEquals("/EndpointURL.1.2.1", oService.getFile());
                assertEquals("www.test.com", oService.getHost());
                assertEquals("/EndpointURL.1.2.1", oService.getPath());
                assertEquals("8080", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 1111.1111.1111.1111..2
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateConnectionInfo_1111_1111_1111_1111__2(ConnectionInfoType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("1111.1111.1111.1111..2", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("BusinessName.2.1", oConnInfo.getHomeCommunity().getName());
        assertEquals("BusinessDescription.2.1", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfos());
        assertNotNull(oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(4, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }

        boolean baFoundService[] = {false, false, false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoType oService : oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_7_1"))
            {
                baFoundService[0] = true;
                assertEquals("http://www.service5.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service5.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_8_1"))
            {
                baFoundService[1] = true;
                assertEquals("http://www.service6.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service6.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_2_1"))
            {
                baFoundService[2] = true;
                assertEquals("http://www.test.com:8080/EndpointURL.2.1.1", oService.getUrl());
                assertEquals("/EndpointURL.2.1.1", oService.getFile());
                assertEquals("www.test.com", oService.getHost());
                assertEquals("/EndpointURL.2.1.1", oService.getPath());
                assertEquals("8080", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_2_2"))
            {
                baFoundService[3] = true;
                assertEquals("http://www.test.com:8080/EndpointURL.2.2.1", oService.getUrl());
                assertEquals("/EndpointURL.2.2.1", oService.getFile());
                assertEquals("www.test.com", oService.getHost());
                assertEquals("/EndpointURL.2.2.1", oService.getPath());
                assertEquals("8080", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
            assertTrue(baFoundService[2]);
            assertTrue(baFoundService[3]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 2222.2222.2222.2222
     * 
     * @param oConnInfo The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     * 
     */
    private void validateConnectionInfo_2222_2222_2222_2222(ConnectionInfoType oConnInfo, String sUniformServiceName)
    {
        assertNotNull(oConnInfo);
        assertNotNull(oConnInfo.getHomeCommunity());
        assertEquals("2222.2222.2222.2222", oConnInfo.getHomeCommunity().getHomeCommunityId());
        assertEquals("Home2", oConnInfo.getHomeCommunity().getName());
        assertEquals("Home2 Description", oConnInfo.getHomeCommunity().getDescription());

        assertNotNull(oConnInfo.getServiceConnectionInfos());
        assertNotNull(oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }
        else
        {
            assertEquals(1, oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (ServiceConnectionInfoType oService : oConnInfo.getServiceConnectionInfos().getServiceConnectionInfo())
        {
            if (oService.getServiceName().equals("UnitTestServiceName_5_1"))
            {
                baFoundService[0] = true;
                assertEquals("http://www.service3.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service3.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else if (oService.getServiceName().equals("UnitTestServiceName_6_1"))
            {
                baFoundService[1] = true;
                assertEquals("http://www.service4.com", oService.getUrl());
                assertEquals("", oService.getFile());
                assertEquals("www.service4.com", oService.getHost());
                assertEquals("", oService.getPath());
                assertEquals("-1", oService.getPort());
                assertEquals("http", oService.getProtocol());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getServiceName())))
            {
                bMatchedService = true;
            }
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }

    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 1111.1111.1111.1111
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateEntity_1111_1111_1111_1111(BusinessEntityType oEntity, String sUniformServiceName)
    {
        assertNotNull(oEntity);
        assertEquals("1111.1111.1111.1111", oEntity.getHomeCommunityId());
        
        assertNotNull(oEntity.getNames());
        assertEquals(1, oEntity.getNames().getBusinessName().size());
        assertEquals("Home1", oEntity.getNames().getBusinessName().get(0));

        assertNotNull(oEntity.getDescriptions());
        assertEquals(1, oEntity.getDescriptions().getBusinessDescription().size());
        assertEquals("Home1 Description", oEntity.getDescriptions().getBusinessDescription().get(0));

        assertNotNull(oEntity.getBusinessServices());
        assertNotNull(oEntity.getBusinessServices().getBusinessService());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oEntity.getBusinessServices().getBusinessService().size());
        }
        else
        {
            assertEquals(1, oEntity.getBusinessServices().getBusinessService().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (BusinessServiceType oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("UnitTestServiceName_3_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 1 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service1.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("UnitTestServiceName_4_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 2 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(true, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service2.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getUniformServiceName());
            }

            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getUniformServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 1111.1111.1111.1111..1
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateEntity_1111_1111_1111_1111__1(BusinessEntityType oEntity, String sUniformServiceName)
    {
        assertNotNull(oEntity);
        assertEquals("1111.1111.1111.1111..1", oEntity.getHomeCommunityId());
        
        assertEquals("BusinessKey.1", oEntity.getBusinessKey());
        
        assertNotNull(oEntity.getDiscoveryURLs());
        assertEquals(2, oEntity.getDiscoveryURLs().getDiscoveryURL().size());
        assertEquals("URL.1.1", oEntity.getDiscoveryURLs().getDiscoveryURL().get(0));
        assertEquals("URL.1.2", oEntity.getDiscoveryURLs().getDiscoveryURL().get(1));
        
        assertNotNull(oEntity.getNames());
        assertEquals(2, oEntity.getNames().getBusinessName().size());
        assertEquals("BusinessName.1.1", oEntity.getNames().getBusinessName().get(0));
        assertEquals("BusinessName.1.2", oEntity.getNames().getBusinessName().get(1));

        assertNotNull(oEntity.getDescriptions());
        assertEquals(2, oEntity.getDescriptions().getBusinessDescription().size());
        assertEquals("BusinessDescription.1.1", oEntity.getDescriptions().getBusinessDescription().get(0));
        assertEquals("BusinessDescription.1.2", oEntity.getDescriptions().getBusinessDescription().get(1));
        
        assertNotNull(oEntity.getContacts());
        assertNotNull(oEntity.getContacts().getContact());
        assertEquals(2, oEntity.getContacts().getContact().size());
        boolean baFoundContact[] = {false, false};
        for (ContactType oContact : oEntity.getContacts().getContact())
        {
            assertNotNull(oContact.getPersonNames());
            assertNotNull(oContact.getPersonNames().getPersonName());
            assertEquals(2, oContact.getPersonNames().getPersonName().size());
            if (oContact.getPersonNames().getPersonName().get(0).equals("ContactPersonName.1.1.1"))
            {
                baFoundContact[0] = true;
                assertEquals("ContactPersonName.1.1.1", oContact.getPersonNames().getPersonName().get(0));
                assertEquals("ContactPersonName.1.1.2", oContact.getPersonNames().getPersonName().get(1));
                
                assertNotNull(oContact.getDescriptions());
                assertNotNull(oContact.getDescriptions().getDescription());
                assertEquals(2, oContact.getDescriptions().getDescription().size());
                assertEquals("ContactDescription.1.1.1", oContact.getDescriptions().getDescription().get(0));
                assertEquals("ContactDescription.1.1.2", oContact.getDescriptions().getDescription().get(1));

                assertNotNull(oContact.getPhones());
                assertNotNull(oContact.getPhones().getPhone());
                assertEquals(2, oContact.getPhones().getPhone().size());
                assertEquals("ContactPhone.1.1.1", oContact.getPhones().getPhone().get(0));
                assertEquals("ContactPhone.1.1.2", oContact.getPhones().getPhone().get(1));

                assertNotNull(oContact.getEmails());
                assertNotNull(oContact.getEmails().getEmail());
                assertEquals(2, oContact.getEmails().getEmail().size());
                assertEquals("Email.1.1.1", oContact.getEmails().getEmail().get(0));
                assertEquals("Email.1.1.2", oContact.getEmails().getEmail().get(1));

                assertNotNull(oContact.getAddresses());
                assertNotNull(oContact.getAddresses().getAddress());
                assertEquals(2, oContact.getAddresses().getAddress().size());
                
                assertNotNull(oContact.getAddresses().getAddress().get(0).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(0).getAddressLine().size());
                assertEquals("AddressLine.1.1.1.1", oContact.getAddresses().getAddress().get(0).getAddressLine().get(0));
                assertEquals("AddressLine.1.1.1.2", oContact.getAddresses().getAddress().get(0).getAddressLine().get(1));

                assertNotNull(oContact.getAddresses().getAddress().get(1).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(1).getAddressLine().size());
                assertEquals("AddressLine.1.1.2.1", oContact.getAddresses().getAddress().get(1).getAddressLine().get(0));
                assertEquals("AddressLine.1.1.2.2", oContact.getAddresses().getAddress().get(1).getAddressLine().get(1));
            }
            else if (oContact.getPersonNames().getPersonName().get(0).equals("ContactPersonName.1.2.1"))
            {
                baFoundContact[1] = true;

                assertEquals("ContactPersonName.1.2.1", oContact.getPersonNames().getPersonName().get(0));
                assertEquals("ContactPersonName.1.2.2", oContact.getPersonNames().getPersonName().get(1));
                
                assertNotNull(oContact.getDescriptions());
                assertNotNull(oContact.getDescriptions().getDescription());
                assertEquals(2, oContact.getDescriptions().getDescription().size());
                assertEquals("ContactDescription.1.2.1", oContact.getDescriptions().getDescription().get(0));
                assertEquals("ContactDescription.1.2.2", oContact.getDescriptions().getDescription().get(1));

                assertNotNull(oContact.getPhones());
                assertNotNull(oContact.getPhones().getPhone());
                assertEquals(2, oContact.getPhones().getPhone().size());
                assertEquals("ContactPhone.1.2.1", oContact.getPhones().getPhone().get(0));
                assertEquals("ContactPhone.1.2.2", oContact.getPhones().getPhone().get(1));

                assertNotNull(oContact.getEmails());
                assertNotNull(oContact.getEmails().getEmail());
                assertEquals(2, oContact.getEmails().getEmail().size());
                assertEquals("Email.1.2.1", oContact.getEmails().getEmail().get(0));
                assertEquals("Email.1.2.2", oContact.getEmails().getEmail().get(1));

                assertNotNull(oContact.getAddresses());
                assertNotNull(oContact.getAddresses().getAddress());
                assertEquals(2, oContact.getAddresses().getAddress().size());
                
                assertNotNull(oContact.getAddresses().getAddress().get(0).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(0).getAddressLine().size());
                assertEquals("AddressLine.1.2.1.1", oContact.getAddresses().getAddress().get(0).getAddressLine().get(0));
                assertEquals("AddressLine.1.2.1.2", oContact.getAddresses().getAddress().get(0).getAddressLine().get(1));

                assertNotNull(oContact.getAddresses().getAddress().get(1).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(1).getAddressLine().size());
                assertEquals("AddressLine.1.2.2.1", oContact.getAddresses().getAddress().get(1).getAddressLine().get(0));
                assertEquals("AddressLine.1.2.2.2", oContact.getAddresses().getAddress().get(1).getAddressLine().get(1));
            }
            else
            {
                fail("Found unexpected contact: " + oContact.getPersonNames().getPersonName().get(0));
            }
        }   // for (CMContact oContact : oEntity.getContacts().getContact())
        assertTrue(baFoundContact[0]);
        assertTrue(baFoundContact[1]);
        
        assertNotNull(oEntity.getStates());
        assertEquals(2, oEntity.getStates().getState().size());
        assertEquals("State.1.1", oEntity.getStates().getState().get(0));
        assertEquals("State.1.2", oEntity.getStates().getState().get(1));
        
        assertEquals(true, oEntity.isFederalHIE());
        
        assertEquals("http://www.thekey.com.1", oEntity.getPublicKeyURI());
        
        assertNotNull(oEntity.getPublicKey());
        assertEquals(6, oEntity.getPublicKey().length);
        assertEquals(100, (int) oEntity.getPublicKey()[0]);
        assertEquals(100, (int) oEntity.getPublicKey()[1]);
        assertEquals(100, (int) oEntity.getPublicKey()[2]);
        assertEquals(100, (int) oEntity.getPublicKey()[3]);
        assertEquals(100, (int) oEntity.getPublicKey()[4]);
        assertEquals(100, (int) oEntity.getPublicKey()[5]);

        // Business Services
        //------------------
        assertNotNull(oEntity.getBusinessServices());
        assertNotNull(oEntity.getBusinessServices().getBusinessService());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oEntity.getBusinessServices().getBusinessService().size());
        }
        else
        {
            assertEquals(1, oEntity.getBusinessServices().getBusinessService().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        
        for (BusinessServiceType oService : oEntity.getBusinessServices().getBusinessService())
        {
            assertNotNull(oService.getServiceKey());
            if (oService.getServiceKey().equals("ServiceKey.1.1"))
            {
                baFoundService[0] = true;
                
                assertNotNull(oService.getNames());
                assertEquals(2, oService.getNames().size());
                assertNotNull(oService.getNames().get(0));
                assertNotNull(oService.getNames().get(0).getName());
                assertEquals(1, oService.getNames().get(0).getName().size());
                assertEquals("BindingName.1.1.1", oService.getNames().get(0).getName().get(0));
                assertNotNull(oService.getNames().get(1));
                assertNotNull(oService.getNames().get(1).getName());
                assertEquals(1, oService.getNames().get(1).getName().size());
                assertEquals("BindingName.1.1.2", oService.getNames().get(1).getName().get(0));
                
                assertNotNull(oService.getDescriptions());
                assertEquals(2, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("BindingDescription.1.1.1", oService.getDescriptions().get(0).getDescription().get(0));
                assertNotNull(oService.getDescriptions().get(1));
                assertNotNull(oService.getDescriptions().get(1).getDescription());
                assertEquals(1, oService.getDescriptions().get(1).getDescription().size());
                assertEquals("BindingDescription.1.1.2", oService.getDescriptions().get(1).getDescription().get(0));
                
                assertEquals("UnitTestServiceName_1_1", oService.getUniformServiceName());
                assertEquals("1.0", oService.getServiceVersion());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());

                assertEquals("BindingKey.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
            }
            else if (oService.getServiceKey().equals("ServiceKey.1.2"))
            {
                baFoundService[1] = true;
                
                assertNotNull(oService.getNames());
                assertEquals(2, oService.getNames().size());
                assertNotNull(oService.getNames().get(0));
                assertNotNull(oService.getNames().get(0).getName());
                assertEquals(1, oService.getNames().get(0).getName().size());
                assertEquals("BindingName.1.2.1", oService.getNames().get(0).getName().get(0));
                assertNotNull(oService.getNames().get(1));
                assertNotNull(oService.getNames().get(1).getName());
                assertEquals(1, oService.getNames().get(1).getName().size());
                assertEquals("BindingName.1.2.2", oService.getNames().get(1).getName().get(0));
                
                assertNotNull(oService.getDescriptions());
                assertEquals(2, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("BindingDescription.1.2.1", oService.getDescriptions().get(0).getDescription().get(0));
                assertNotNull(oService.getDescriptions().get(1));
                assertNotNull(oService.getDescriptions().get(1).getDescription());
                assertEquals(1, oService.getDescriptions().get(1).getDescription().size());
                assertEquals("BindingDescription.1.2.2", oService.getDescriptions().get(1).getDescription().get(0));
                
                assertEquals("UnitTestServiceName_1_2", oService.getUniformServiceName());
                assertEquals("1.0", oService.getServiceVersion());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());

                assertEquals("BindingKey.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());

            }
            else
            {
                fail("Found an unexpected service: " + oService.getServiceKey());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getUniformServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 1111.1111.1111.1111..2
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateEntity_1111_1111_1111_1111__2(BusinessEntityType oEntity, String sUniformServiceName)
    {
        assertNotNull(oEntity);
        assertEquals("1111.1111.1111.1111..2", oEntity.getHomeCommunityId());
        
        assertNotNull(oEntity.getDiscoveryURLs());
        assertEquals(2, oEntity.getDiscoveryURLs().getDiscoveryURL().size());
        assertEquals("URL.2.1", oEntity.getDiscoveryURLs().getDiscoveryURL().get(0));
        assertEquals("URL.2.2", oEntity.getDiscoveryURLs().getDiscoveryURL().get(1));
        
        assertNotNull(oEntity.getNames());
        assertEquals(2, oEntity.getNames().getBusinessName().size());
        assertEquals("BusinessName.2.1", oEntity.getNames().getBusinessName().get(0));
        assertEquals("BusinessName.2.2", oEntity.getNames().getBusinessName().get(1));

        assertNotNull(oEntity.getDescriptions());
        assertEquals(2, oEntity.getDescriptions().getBusinessDescription().size());
        assertEquals("BusinessDescription.2.1", oEntity.getDescriptions().getBusinessDescription().get(0));
        assertEquals("BusinessDescription.2.2", oEntity.getDescriptions().getBusinessDescription().get(1));

        assertNotNull(oEntity.getContacts());
        assertNotNull(oEntity.getContacts().getContact());
        assertEquals(2, oEntity.getContacts().getContact().size());
        boolean baFoundContact[] = {false, false};
        for (ContactType oContact : oEntity.getContacts().getContact())
        {
            assertNotNull(oContact.getPersonNames());
            assertNotNull(oContact.getPersonNames().getPersonName());
            assertEquals(2, oContact.getPersonNames().getPersonName().size());
            if (oContact.getPersonNames().getPersonName().get(0).equals("ContactPersonName.2.1.1"))
            {
                baFoundContact[0] = true;
                assertEquals("ContactPersonName.2.1.1", oContact.getPersonNames().getPersonName().get(0));
                assertEquals("ContactPersonName.2.1.2", oContact.getPersonNames().getPersonName().get(1));
                
                assertNotNull(oContact.getDescriptions());
                assertNotNull(oContact.getDescriptions().getDescription());
                assertEquals(2, oContact.getDescriptions().getDescription().size());
                assertEquals("ContactDescription.2.1.1", oContact.getDescriptions().getDescription().get(0));
                assertEquals("ContactDescription.2.1.2", oContact.getDescriptions().getDescription().get(1));

                assertNotNull(oContact.getPhones());
                assertNotNull(oContact.getPhones().getPhone());
                assertEquals(2, oContact.getPhones().getPhone().size());
                assertEquals("ContactPhone.2.1.1", oContact.getPhones().getPhone().get(0));
                assertEquals("ContactPhone.2.1.2", oContact.getPhones().getPhone().get(1));

                assertNotNull(oContact.getEmails());
                assertNotNull(oContact.getEmails().getEmail());
                assertEquals(2, oContact.getEmails().getEmail().size());
                assertEquals("Email.2.1.1", oContact.getEmails().getEmail().get(0));
                assertEquals("Email.2.1.2", oContact.getEmails().getEmail().get(1));

                assertNotNull(oContact.getAddresses());
                assertNotNull(oContact.getAddresses().getAddress());
                assertEquals(2, oContact.getAddresses().getAddress().size());
                
                assertNotNull(oContact.getAddresses().getAddress().get(0).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(0).getAddressLine().size());
                assertEquals("AddressLine.2.1.1.1", oContact.getAddresses().getAddress().get(0).getAddressLine().get(0));
                assertEquals("AddressLine.2.1.1.2", oContact.getAddresses().getAddress().get(0).getAddressLine().get(1));

                assertNotNull(oContact.getAddresses().getAddress().get(1).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(1).getAddressLine().size());
                assertEquals("AddressLine.2.1.2.1", oContact.getAddresses().getAddress().get(1).getAddressLine().get(0));
                assertEquals("AddressLine.2.1.2.2", oContact.getAddresses().getAddress().get(1).getAddressLine().get(1));
            }
            else if (oContact.getPersonNames().getPersonName().get(0).equals("ContactPersonName.2.2.1"))
            {
                baFoundContact[1] = true;

                assertEquals("ContactPersonName.2.2.1", oContact.getPersonNames().getPersonName().get(0));
                assertEquals("ContactPersonName.2.2.2", oContact.getPersonNames().getPersonName().get(1));
                
                assertNotNull(oContact.getDescriptions());
                assertNotNull(oContact.getDescriptions().getDescription());
                assertEquals(2, oContact.getDescriptions().getDescription().size());
                assertEquals("ContactDescription.2.2.1", oContact.getDescriptions().getDescription().get(0));
                assertEquals("ContactDescription.2.2.2", oContact.getDescriptions().getDescription().get(1));

                assertNotNull(oContact.getPhones());
                assertNotNull(oContact.getPhones().getPhone());
                assertEquals(2, oContact.getPhones().getPhone().size());
                assertEquals("ContactPhone.2.2.1", oContact.getPhones().getPhone().get(0));
                assertEquals("ContactPhone.2.2.2", oContact.getPhones().getPhone().get(1));

                assertNotNull(oContact.getEmails());
                assertNotNull(oContact.getEmails().getEmail());
                assertEquals(2, oContact.getEmails().getEmail().size());
                assertEquals("Email.2.2.1", oContact.getEmails().getEmail().get(0));
                assertEquals("Email.2.2.2", oContact.getEmails().getEmail().get(1));

                assertNotNull(oContact.getAddresses());
                assertNotNull(oContact.getAddresses().getAddress());
                assertEquals(2, oContact.getAddresses().getAddress().size());
                
                assertNotNull(oContact.getAddresses().getAddress().get(0).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(0).getAddressLine().size());
                assertEquals("AddressLine.2.2.1.1", oContact.getAddresses().getAddress().get(0).getAddressLine().get(0));
                assertEquals("AddressLine.2.2.1.2", oContact.getAddresses().getAddress().get(0).getAddressLine().get(1));

                assertNotNull(oContact.getAddresses().getAddress().get(1).getAddressLine());
                assertEquals(2, oContact.getAddresses().getAddress().get(1).getAddressLine().size());
                assertEquals("AddressLine.2.2.2.1", oContact.getAddresses().getAddress().get(1).getAddressLine().get(0));
                assertEquals("AddressLine.2.2.2.2", oContact.getAddresses().getAddress().get(1).getAddressLine().get(1));
            }
            
            else
            {
                fail("Found unexpected contact: " + oContact.getPersonNames().getPersonName().get(0));
            }
        }   // for (CMContact oContact : oEntity.getContacts().getContact())
        assertTrue(baFoundContact[0]);
        assertTrue(baFoundContact[1]);
        
        assertNotNull(oEntity.getStates());
        assertEquals(2, oEntity.getStates().getState().size());
        assertEquals("State.2.1", oEntity.getStates().getState().get(0));
        assertEquals("State.2.2", oEntity.getStates().getState().get(1));
        
        assertEquals(true, oEntity.isFederalHIE());
        
        assertEquals("http://www.thekey.com.2", oEntity.getPublicKeyURI());
        
        assertNotNull(oEntity.getPublicKey());
        assertEquals(6, oEntity.getPublicKey().length);
        assertEquals(100, (int) oEntity.getPublicKey()[0]);
        assertEquals(100, (int) oEntity.getPublicKey()[1]);
        assertEquals(100, (int) oEntity.getPublicKey()[2]);
        assertEquals(100, (int) oEntity.getPublicKey()[3]);
        assertEquals(100, (int) oEntity.getPublicKey()[4]);
        assertEquals(100, (int) oEntity.getPublicKey()[5]);

        assertNotNull(oEntity.getBusinessServices());
        assertNotNull(oEntity.getBusinessServices().getBusinessService());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(4, oEntity.getBusinessServices().getBusinessService().size());
        }
        else
        {
            assertEquals(1, oEntity.getBusinessServices().getBusinessService().size());
        }

        boolean baFoundService[] = {false, false, false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (BusinessServiceType oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("UnitTestServiceName_7_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 5 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service5.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("UnitTestServiceName_8_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 6 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(true, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service6.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getServiceKey().equals("ServiceKey.2.1"))
            {
                baFoundService[2] = true;
                
                assertNotNull(oService.getNames());
                assertEquals(2, oService.getNames().size());
                assertNotNull(oService.getNames().get(0));
                assertNotNull(oService.getNames().get(0).getName());
                assertEquals(1, oService.getNames().get(0).getName().size());
                assertEquals("BindingName.2.1.1", oService.getNames().get(0).getName().get(0));
                assertNotNull(oService.getNames().get(1));
                assertNotNull(oService.getNames().get(1).getName());
                assertEquals(1, oService.getNames().get(1).getName().size());
                assertEquals("BindingName.2.1.2", oService.getNames().get(1).getName().get(0));
                
                assertNotNull(oService.getDescriptions());
                assertEquals(2, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("BindingDescription.2.1.1", oService.getDescriptions().get(0).getDescription().get(0));
                assertNotNull(oService.getDescriptions().get(1));
                assertNotNull(oService.getDescriptions().get(1).getDescription());
                assertEquals(1, oService.getDescriptions().get(1).getDescription().size());
                assertEquals("BindingDescription.2.1.2", oService.getDescriptions().get(1).getDescription().get(0));
                
                assertEquals("UnitTestServiceName_2_1", oService.getUniformServiceName());
                assertEquals("1.0", oService.getServiceVersion());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());

                assertEquals("BindingKey.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
            }
            else if (oService.getServiceKey().equals("ServiceKey.2.2"))
            {
                baFoundService[3] = true;
                
                assertNotNull(oService.getNames());
                assertEquals(2, oService.getNames().size());
                assertNotNull(oService.getNames().get(0));
                assertNotNull(oService.getNames().get(0).getName());
                assertEquals(1, oService.getNames().get(0).getName().size());
                assertEquals("BindingName.2.2.1", oService.getNames().get(0).getName().get(0));
                assertNotNull(oService.getNames().get(1));
                assertNotNull(oService.getNames().get(1).getName());
                assertEquals(1, oService.getNames().get(1).getName().size());
                assertEquals("BindingName.2.2.2", oService.getNames().get(1).getName().get(0));
                
                assertNotNull(oService.getDescriptions());
                assertEquals(2, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("BindingDescription.2.2.1", oService.getDescriptions().get(0).getDescription().get(0));
                assertNotNull(oService.getDescriptions().get(1));
                assertNotNull(oService.getDescriptions().get(1).getDescription());
                assertEquals(1, oService.getDescriptions().get(1).getDescription().size());
                assertEquals("BindingDescription.2.2.2", oService.getDescriptions().get(1).getDescription().get(0));

                assertEquals("UnitTestServiceName_2_2", oService.getUniformServiceName());
                assertEquals("1.0", oService.getServiceVersion());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());

                assertEquals("BindingKey.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("http://www.test.com:8080/EndpointURL.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getUniformServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getUniformServiceName())))
            {
                bMatchedService = true;
            }
            
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
            assertTrue(baFoundService[2]);
            assertTrue(baFoundService[3]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
    }    
    
    /**
     * This method validates the entire contents of a business entity for
     * HomeCommunity 2222.2222.2222.2222
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     * 
     */
    private void validateEntity_2222_2222_2222_2222(BusinessEntityType oEntity, String sUniformServiceName)
    {
        assertNotNull(oEntity);
        assertEquals("2222.2222.2222.2222", oEntity.getHomeCommunityId());

        assertNotNull(oEntity.getNames());
        assertEquals(1, oEntity.getNames().getBusinessName().size());
        assertEquals("Home2", oEntity.getNames().getBusinessName().get(0));

        assertNotNull(oEntity.getDescriptions());
        assertEquals(1, oEntity.getDescriptions().getBusinessDescription().size());
        assertEquals("Home2 Description", oEntity.getDescriptions().getBusinessDescription().get(0));

        assertNotNull(oEntity.getBusinessServices());
        assertNotNull(oEntity.getBusinessServices().getBusinessService());

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertEquals(2, oEntity.getBusinessServices().getBusinessService().size());
        }
        else
        {
            assertEquals(1, oEntity.getBusinessServices().getBusinessService().size());
        }

        boolean baFoundService[] = {false, false};
        boolean bMatchedService = false;            // Used when we are looking just for one service.
        for (BusinessServiceType oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("UnitTestServiceName_5_1"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 3 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service3.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("UnitTestServiceName_6_1"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertEquals(1, oService.getDescriptions().size());
                assertNotNull(oService.getDescriptions().get(0));
                assertNotNull(oService.getDescriptions().get(0).getDescription());
                assertEquals(1, oService.getDescriptions().get(0).getDescription().size());
                assertEquals("Service 4 Description", oService.getDescriptions().get(0).getDescription().get(0));

                assertEquals(true, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service4.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else
            {
                fail("Found an unexpected service: " + oService.getUniformServiceName());
            }
            
            // We can check this here because if it does not match either of the two specified, we will
            // get an error.  So we are guaranteed that we will either fail before we are done, or
            // that everything is in order.
            //------------------------------------------------------------------------------------------
            if ((sUniformServiceName != null) && (sUniformServiceName.equals(oService.getUniformServiceName())))
            {
                bMatchedService = true;
            }
            
        }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())

        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            assertTrue(baFoundService[0]);
            assertTrue(baFoundService[1]);
        }
        else
        {
            assertTrue(bMatchedService);
        }
        
    }
    

    /**
     * Test of getAllCommunities method, of class CMServiceHelper.
     */
    @Test
    public void testGetAllCommunities()
    {
        System.out.println("getAllCommunities");
        GetAllCommunitiesRequestType part1 = new GetAllCommunitiesRequestType();
        HomeCommunitiesType oResult = CMServiceHelper.getAllCommunities(part1);
        assertNotNull(oResult);
        assertNotNull(oResult.getHomeCommunity());
        assertEquals(4, oResult.getHomeCommunity().size());
        boolean baFound[] = {false, false, false, false};

        for (HomeCommunityType oHomeCommunity : oResult.getHomeCommunity())
        {
            if (oHomeCommunity.getHomeCommunityId().equals("1111.1111.1111.1111"))
            {
                validateHomeCommunity_1111_1111_1111_1111(oHomeCommunity);
                baFound[0] = true;
            }
            else if (oHomeCommunity.getHomeCommunityId().equals("2222.2222.2222.2222"))
            {
                validateHomeCommunity_2222_2222_2222_2222(oHomeCommunity);
                baFound[1] = true;
            }
            else if (oHomeCommunity.getHomeCommunityId().equals("1111.1111.1111.1111..2"))
            {
                validateHomeCommunity_1111_1111_1111_1111__2(oHomeCommunity);
                baFound[2] = true;
            }
            else if (oHomeCommunity.getHomeCommunityId().equals("1111.1111.1111.1111..1"))
            {
                validateHomeCommunity_1111_1111_1111_1111__1(oHomeCommunity);
                baFound[3] = true;
            }
            else
            {
                fail("Found an unexpected HomeCommunity.  HomeCommunityId:" + oHomeCommunity.getHomeCommunityId());
            }
        }
        assertTrue(baFound[0]);
        assertTrue(baFound[1]);
        assertTrue(baFound[2]);
        assertTrue(baFound[3]);
    }
    
    

    /**
     * Test of getAllBusinessEntities method, of class CMServiceHelper.
     */
    @Test
    public void testGetAllBusinessEntities()
    {
        System.out.println("getAllBusinessEntities");
        GetAllBusinessEntitiesRequestType part1 = null;
        BusinessEntitiesType oEntities = null;
        oEntities = CMServiceHelper.getAllBusinessEntities(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(4, oEntities.getBusinessEntity().size());
        boolean baFound[] = {false, false, false, false};

        for (BusinessEntityType oEntity : oEntities.getBusinessEntity())
        {
            if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111"))
            {
                validateEntity_1111_1111_1111_1111(oEntity, "");
                baFound[0] = true;
            }
            else if (oEntity.getHomeCommunityId().equals("2222.2222.2222.2222"))
            {
                validateEntity_2222_2222_2222_2222(oEntity, "");
                baFound[1] = true;
            }
            else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..2"))
            {
                validateEntity_1111_1111_1111_1111__2(oEntity, "");
                baFound[2] = true;
            }
            else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..1"))
            {
                validateEntity_1111_1111_1111_1111__1(oEntity, "");
                baFound[3] = true;
            }
            else
            {
                fail("Found an unexpected entry.  HomeCommunityId:" + oEntity.getHomeCommunityId());
            }
        }
        assertTrue(baFound[0]);
        assertTrue(baFound[1]);
        assertTrue(baFound[2]);
        assertTrue(baFound[3]);
    }

    /**
     * Test of getBusinessEntity method, of class CMServiceHelper.
     */
    @Test
    public void testGetBusinessEntity()
    {
        System.out.println("getBusinessEntity");
        GetBusinessEntityRequestType part1 = new GetBusinessEntityRequestType();
        part1.setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111");
        BusinessEntityType oEntity = CMServiceHelper.getBusinessEntity(part1);
        assertNotNull(oEntity);
        validateEntity_1111_1111_1111_1111(oEntity, "");
        
        part1 = new GetBusinessEntityRequestType();
        part1.setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..2");
        oEntity = CMServiceHelper.getBusinessEntity(part1);
        assertNotNull(oEntity);
        validateEntity_1111_1111_1111_1111__2(oEntity, "");
            
        part1 = new GetBusinessEntityRequestType();
        part1.setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..1");
        oEntity = CMServiceHelper.getBusinessEntity(part1);
        assertNotNull(oEntity);
        validateEntity_1111_1111_1111_1111__1(oEntity, "");
    }

    /**
     * Test of getConnectionInfoSet method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoSet()
    {
        System.out.println("getConnectionInfoSet");
        HomeCommunitiesType part1 = new HomeCommunitiesType();
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        part1.getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunity().add(oHomeCommunity);

        ConnectionInfosType oConnInfos = CMServiceHelper.getConnectionInfoSet(part1);
        assertNotNull(oConnInfos);
        assertNotNull(oConnInfos.getConnectionInfo());
        assertEquals(3, oConnInfos.getConnectionInfo().size());

        boolean baFound[] = {false, false, false};
        for (ConnectionInfoType oConnInfo: oConnInfos.getConnectionInfo())
        {
            assertNotNull(oConnInfo.getHomeCommunity());
            if (oConnInfo.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111"))
            {
                baFound[0] = true;
                validateConnectionInfo_1111_1111_1111_1111(oConnInfo, "");
            }
            else if (oConnInfo.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111..2"))
            {
                baFound[1] = true;
                validateConnectionInfo_1111_1111_1111_1111__2(oConnInfo, "");
            }
            else if (oConnInfo.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111..1"))
            {
                baFound[2] = true;
                validateConnectionInfo_1111_1111_1111_1111__1(oConnInfo, "");
            }
            else
            {
                fail("Found an unexpected business entity: " + oConnInfo.getHomeCommunity().getHomeCommunityId());
            }
        }   // for (CMBusinessEntity oEntity: oEntities.getBusinessEntity())

        assertTrue(baFound[0]);
        assertTrue(baFound[1]);
        assertTrue(baFound[2]);
    }

    /**
     * Test of getConnectionInfoEndpointSet method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoEndpointSet()
    {
        System.out.println("getConnectionInfoEndpointSet");
        GetConnectionInfoEndpointSetRequestType part1 = new GetConnectionInfoEndpointSetRequestType();
        part1.setHomeCommunities(new HomeCommunitiesType());
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        ConnectionInfoEndpointsType oConnInfoEndpoints = CMServiceHelper.getConnectionInfoEndpointSet(part1);
        assertNotNull(oConnInfoEndpoints);
        assertNotNull(oConnInfoEndpoints.getConnectionInfoEndpoint());
        assertEquals(3, oConnInfoEndpoints.getConnectionInfoEndpoint().size());
            
        boolean baFound[] = {false, false, false};
        for (ConnectionInfoEndpointType oConnInfoEndpoint: oConnInfoEndpoints.getConnectionInfoEndpoint())
        {
            assertNotNull(oConnInfoEndpoint.getHomeCommunity());
            
            if (oConnInfoEndpoint.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111"))
            {
                baFound[0] = true;
                validateConnectionInfoEndpoint_1111_1111_1111_1111(oConnInfoEndpoint, "");
            }
            else if (oConnInfoEndpoint.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111..2"))
            {
                baFound[1] = true;
                validateConnectionInfoEndpoint_1111_1111_1111_1111__2(oConnInfoEndpoint, "");
            }
            else if (oConnInfoEndpoint.getHomeCommunity().getHomeCommunityId().equals("1111.1111.1111.1111..1"))
            {
                baFound[2] = true;
                validateConnectionInfoEndpoint_1111_1111_1111_1111__1(oConnInfoEndpoint, "");
            }
            else
            {
                fail("Found an unexpected business entity: " + oConnInfoEndpoint.getHomeCommunity().getHomeCommunityId());
            }
        }   // for (CMBusinessEntity oEntity: oEntities.getBusinessEntity())

        assertTrue(baFound[0]);
        assertTrue(baFound[1]);
        assertTrue(baFound[2]);
        
    }

    /**
     * Test of getBusinessEntitySet method, of class CMServiceHelper.
     */
    @Test
    public void testGetBusinessEntitySet()
    {
        System.out.println("getBusinessEntitySet");
        GetBusinessEntitySetRequestType part1 = new GetBusinessEntitySetRequestType();
        part1.setHomeCommunities(new HomeCommunitiesType());
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        BusinessEntitiesType oEntities = CMServiceHelper.getBusinessEntitySet(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(3, oEntities.getBusinessEntity().size());

        boolean baFound[] = {false, false, false};
        for (BusinessEntityType oEntity: oEntities.getBusinessEntity())
        {
            if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111"))
            {
                baFound[0] = true;
                validateEntity_1111_1111_1111_1111(oEntity, "");
            }
            else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..2"))
            {
                baFound[1] = true;
                validateEntity_1111_1111_1111_1111__2(oEntity, "");
            }
            else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..1"))
            {
                baFound[2] = true;
                validateEntity_1111_1111_1111_1111__1(oEntity, "");
            }
            else
            {
                fail("Found an unexpected business entity: " + oEntity.getHomeCommunityId());
            }
        }   // for (CMBusinessEntity oEntity: oEntities.getBusinessEntity())

        assertTrue(baFound[0]);
        assertTrue(baFound[1]);
        assertTrue(baFound[2]);
        
    }

    /**
     * Test of getConnectionInfoSetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoSetByServiceName()
    {
        System.out.println("getConnectionInfoSetByServiceName");
        HomeCommunitiesWithServiceNameType part1 = new HomeCommunitiesWithServiceNameType();
        part1.setHomeCommunities(new HomeCommunitiesType());
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("2222.2222.2222.2222");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        part1.setServiceName("UnitTestServiceName_5_1");
        ConnectionInfosType oConnInfos = CMServiceHelper.getConnectionInfoSetByServiceName(part1);
        assertNotNull(oConnInfos);
        assertNotNull(oConnInfos.getConnectionInfo());
        assertEquals(1, oConnInfos.getConnectionInfo().size());
        validateConnectionInfo_2222_2222_2222_2222(oConnInfos.getConnectionInfo().get(0), "UnitTestServiceName_5_1");

        part1.setServiceName("UnitTestServiceName_1_2");
        oConnInfos = CMServiceHelper.getConnectionInfoSetByServiceName(part1);
        assertNotNull(oConnInfos);
        assertNotNull(oConnInfos.getConnectionInfo());
        assertEquals(1, oConnInfos.getConnectionInfo().size());
        validateConnectionInfo_1111_1111_1111_1111__1(oConnInfos.getConnectionInfo().get(0), "UnitTestServiceName_1_2");
        
    }

    /**
     * Test of getConnectionInfoEndpointSetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoEndpointSetByServiceName()
    {
        System.out.println("getConnectionInfoEndpointSetByServiceName");
        GetConnectionInfoEndpointSetByServiceNameRequestType part1 = new GetConnectionInfoEndpointSetByServiceNameRequestType();
        part1.setHomeCommunitiesWithServiceName(new HomeCommunitiesWithServiceNameType());
        part1.getHomeCommunitiesWithServiceName().setHomeCommunities(new HomeCommunitiesType());
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("2222.2222.2222.2222");
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        part1.getHomeCommunitiesWithServiceName().setServiceName("UnitTestServiceName_5_1");
        ConnectionInfoEndpointsType oConnInfoEndpoints = CMServiceHelper.getConnectionInfoEndpointSetByServiceName(part1);
        assertNotNull(oConnInfoEndpoints);
        assertNotNull(oConnInfoEndpoints.getConnectionInfoEndpoint());
        assertEquals(1, oConnInfoEndpoints.getConnectionInfoEndpoint().size());
        validateConnectionInfoEndpoint_2222_2222_2222_2222(oConnInfoEndpoints.getConnectionInfoEndpoint().get(0), "UnitTestServiceName_5_1");

        part1.getHomeCommunitiesWithServiceName().setServiceName("UnitTestServiceName_1_2");
        oConnInfoEndpoints = CMServiceHelper.getConnectionInfoEndpointSetByServiceName(part1);
        assertNotNull(oConnInfoEndpoints);
        assertNotNull(oConnInfoEndpoints.getConnectionInfoEndpoint());
        assertEquals(1, oConnInfoEndpoints.getConnectionInfoEndpoint().size());
        validateConnectionInfoEndpoint_1111_1111_1111_1111__1(oConnInfoEndpoints.getConnectionInfoEndpoint().get(0), "UnitTestServiceName_1_2");
    }

    /**
     * Test of getBusinessEntitySetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetBusinessEntitySetByServiceName()
    {
        System.out.println("getBusinessEntitySetByServiceName");
        GetBusinessEntitySetByServiceNameRequestType part1 = new GetBusinessEntitySetByServiceNameRequestType();
        part1.setHomeCommunitiesWithServiceName(new HomeCommunitiesWithServiceNameType());
        part1.getHomeCommunitiesWithServiceName().setHomeCommunities(new HomeCommunitiesType());
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("2222.2222.2222.2222");
        oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111");
        oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..2");
        oHomeCommunity = new HomeCommunityType();
        part1.getHomeCommunitiesWithServiceName().getHomeCommunities().getHomeCommunity().add(oHomeCommunity);
        oHomeCommunity.setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunitiesWithServiceName().setServiceName("UnitTestServiceName_5_1");
        BusinessEntitiesType oEntities = CMServiceHelper.getBusinessEntitySetByServiceName(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(1, oEntities.getBusinessEntity().size());
        validateEntity_2222_2222_2222_2222(oEntities.getBusinessEntity().get(0), "UnitTestServiceName_5_1");

        part1.getHomeCommunitiesWithServiceName().setServiceName("UnitTestServiceName_1_2");
        oEntities = CMServiceHelper.getBusinessEntitySetByServiceName(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(1, oEntities.getBusinessEntity().size());
        validateEntity_1111_1111_1111_1111__1(oEntities.getBusinessEntity().get(0), "UnitTestServiceName_1_2");

    }

    /**
     * Test of getConnectionInfoByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoByServiceName()
    {
        System.out.println("getConnectionInfoByServiceName");
        HomeCommunityWithServiceNameType part1 = new HomeCommunityWithServiceNameType();
        part1.setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..1");
        part1.setServiceName("UnitTestServiceName_1_2");
        ConnectionInfoType oConnInfo = CMServiceHelper.getConnectionInfoByServiceName(part1);
        assertNotNull(oConnInfo);
        validateConnectionInfo_1111_1111_1111_1111__1(oConnInfo, "UnitTestServiceName_1_2");

        part1.getHomeCommunity().setHomeCommunityId("2222.2222.2222.2222");
        part1.setServiceName("UnitTestServiceName_5_1");
        oConnInfo = CMServiceHelper.getConnectionInfoByServiceName(part1);
        assertNotNull(oConnInfo);
        validateConnectionInfo_2222_2222_2222_2222(oConnInfo, "UnitTestServiceName_5_1");

        part1.getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..2");
        part1.setServiceName("UnitTestServiceName_8_1");
        oConnInfo = CMServiceHelper.getConnectionInfoByServiceName(part1);
        assertNotNull(oConnInfo);
        validateConnectionInfo_1111_1111_1111_1111__2(oConnInfo, "UnitTestServiceName_8_1");
    }

    /**
     * Test of getConnectionInfoEndpointByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetConnectionInfoEndpointByServiceName()
    {
        System.out.println("getConnectionInfoEndpointByServiceName");
        GetConnectionInfoEndpointByServiceNameRequestType part1 = new GetConnectionInfoEndpointByServiceNameRequestType();
        part1.setHomeCommunityWithServiceName(new HomeCommunityWithServiceNameType());
        part1.getHomeCommunityWithServiceName().setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_1_2");
        ConnectionInfoEndpointType oConnInfoEndpoint = CMServiceHelper.getConnectionInfoEndpointByServiceName(part1);
        assertNotNull(oConnInfoEndpoint);
        validateConnectionInfoEndpoint_1111_1111_1111_1111__1(oConnInfoEndpoint, "UnitTestServiceName_1_2");
            
        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("2222.2222.2222.2222");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_5_1");
        oConnInfoEndpoint = CMServiceHelper.getConnectionInfoEndpointByServiceName(part1);
        assertNotNull(oConnInfoEndpoint);
        validateConnectionInfoEndpoint_2222_2222_2222_2222(oConnInfoEndpoint, "UnitTestServiceName_5_1");

        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_8_1");
        oConnInfoEndpoint = CMServiceHelper.getConnectionInfoEndpointByServiceName(part1);
        assertNotNull(oConnInfoEndpoint);
        validateConnectionInfoEndpoint_1111_1111_1111_1111__2(oConnInfoEndpoint, "UnitTestServiceName_8_1");
    }

    /**
     * Test of getBusinessEntityByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetBusinessEntityByServiceName()
    {
        System.out.println("getBusinessEntityByServiceName");
        GetBusinessEntityByServiceNameRequestType part1 = new GetBusinessEntityByServiceNameRequestType();
        part1.setHomeCommunityWithServiceName(new HomeCommunityWithServiceNameType());
        part1.getHomeCommunityWithServiceName().setHomeCommunity(new HomeCommunityType());
        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..1");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_1_2");
        BusinessEntityType oEntity = CMServiceHelper.getBusinessEntityByServiceName(part1);
        assertNotNull(oEntity);
        validateEntity_1111_1111_1111_1111__1(oEntity, "UnitTestServiceName_1_2");

        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("2222.2222.2222.2222");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_5_1");
        oEntity = CMServiceHelper.getBusinessEntityByServiceName(part1);
        assertNotNull(oEntity);
        validateEntity_2222_2222_2222_2222(oEntity, "UnitTestServiceName_5_1");

        part1.getHomeCommunityWithServiceName().getHomeCommunity().setHomeCommunityId("1111.1111.1111.1111..2");
        part1.getHomeCommunityWithServiceName().setServiceName("UnitTestServiceName_8_1");
        oEntity = CMServiceHelper.getBusinessEntityByServiceName(part1);
        assertNotNull(oEntity);
        validateEntity_1111_1111_1111_1111__2(oEntity, "UnitTestServiceName_8_1");
        
    }

    /**
     * Test of getAllConnectionInfoSetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetAllConnectionInfoSetByServiceName()
    {
        System.out.println("getAllConnectionInfoSetByServiceName");
        GetAllConnectionInfoSetByServiceNameRequestType part1 = new GetAllConnectionInfoSetByServiceNameRequestType();
        part1.setServiceName("UnitTestServiceName_5_1");
        ConnectionInfosType oConnInfos = CMServiceHelper.getAllConnectionInfoSetByServiceName(part1);
        assertNotNull(oConnInfos);
        assertNotNull(oConnInfos.getConnectionInfo());
        assertEquals(1, oConnInfos.getConnectionInfo().size());
        validateConnectionInfo_2222_2222_2222_2222(oConnInfos.getConnectionInfo().get(0), "UnitTestServiceName_5_1");

        part1.setServiceName("UnitTestServiceName_1_2");
        oConnInfos = CMServiceHelper.getAllConnectionInfoSetByServiceName(part1);
        assertNotNull(oConnInfos);
        assertNotNull(oConnInfos.getConnectionInfo());
        assertEquals(1, oConnInfos.getConnectionInfo().size());
        validateConnectionInfo_1111_1111_1111_1111__1(oConnInfos.getConnectionInfo().get(0), "UnitTestServiceName_1_2");
    }

    /**
     * Test of getAllConnectionInfoEndpointSetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetAllConnectionInfoEndpointSetByServiceName()
    {
        System.out.println("getAllConnectionInfoEndpointSetByServiceName");
        GetAllConnectionInfoEndpointSetByServiceNameRequestType part1 = new GetAllConnectionInfoEndpointSetByServiceNameRequestType();
        part1.setServiceName("UnitTestServiceName_5_1");
        ConnectionInfoEndpointsType oConnInfoEndpoints = CMServiceHelper.getAllConnectionInfoEndpointSetByServiceName(part1);
        assertNotNull(oConnInfoEndpoints);
        assertNotNull(oConnInfoEndpoints.getConnectionInfoEndpoint());
        assertEquals(1, oConnInfoEndpoints.getConnectionInfoEndpoint().size());
        validateConnectionInfoEndpoint_2222_2222_2222_2222(oConnInfoEndpoints.getConnectionInfoEndpoint().get(0), "UnitTestServiceName_5_1");

        part1.setServiceName("UnitTestServiceName_1_2");
        oConnInfoEndpoints = CMServiceHelper.getAllConnectionInfoEndpointSetByServiceName(part1);
        assertNotNull(oConnInfoEndpoints);
        assertNotNull(oConnInfoEndpoints.getConnectionInfoEndpoint());
        assertEquals(1, oConnInfoEndpoints.getConnectionInfoEndpoint().size());
        validateConnectionInfoEndpoint_1111_1111_1111_1111__1(oConnInfoEndpoints.getConnectionInfoEndpoint().get(0), "UnitTestServiceName_1_2");
    }

    /**
     * Test of getAllBusinessEntitySetByServiceName method, of class CMServiceHelper.
     */
    @Test
    public void testGetAllBusinessEntitySetByServiceName()
    {
        System.out.println("getAllBusinessEntitySetByServiceName");
        GetAllBusinessEntitySetByServiceNameRequestType part1 = new GetAllBusinessEntitySetByServiceNameRequestType();
        part1.setServiceName("UnitTestServiceName_5_1");
        BusinessEntitiesType oEntities = CMServiceHelper.getAllBusinessEntitySetByServiceName(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(1, oEntities.getBusinessEntity().size());
        validateEntity_2222_2222_2222_2222(oEntities.getBusinessEntity().get(0), "UnitTestServiceName_5_1");

        part1.setServiceName("UnitTestServiceName_1_2");
        oEntities = CMServiceHelper.getAllBusinessEntitySetByServiceName(part1);
        assertNotNull(oEntities);
        assertNotNull(oEntities.getBusinessEntity());
        assertEquals(1, oEntities.getBusinessEntity().size());
        validateEntity_1111_1111_1111_1111__1(oEntities.getBusinessEntity().get(0), "UnitTestServiceName_1_2");
    }

    /**
     * Test of forceRefreshUDDICache method, of class CMServiceHelper.
     */
    @Test
    public void testForceRefreshUDDICache()
    {
        //  This now hits the UDDI server.  I am not sure we want this to be a unit test.
        // It is really an integration test - so I am commenting it out.
        //----------------------------------------------------------------------------------
       
//        System.out.println("forceRefreshUDDICache");
//        ForceRefreshUDDICacheRequestType part1 = new ForceRefreshUDDICacheRequestType();
//        SuccessOrFailType oResult = CMServiceHelper.forceRefreshUDDICache(part1);
//        assertTrue(oResult.isSuccess());
    }

    /**
     * Test of forceRefreshInternalConnectCache method, of class CMServiceHelper.
     */
    @Test
    public void testForceRefreshInternalConnectCache()
    {
        System.out.println("forceRefreshInternalConnectCache");
        ForceRefreshInternalConnectCacheRequestType part1 = new ForceRefreshInternalConnectCacheRequestType();
        SuccessOrFailType oResult = CMServiceHelper.forceRefreshInternalConnectCache(part1);
        assertTrue(oResult.isSuccess());
    }

    @Test
    public void testStoreAssigningAuthorityToHomeCommunityMapping(){
//        StoreAssigningAuthorityToHomeCommunityMappingRequestType storeMap = new StoreAssigningAuthorityToHomeCommunityMappingRequestType();
//        AssigningAuthorityType assingAuthTypeId = new AssigningAuthorityType();
//        HomeCommunityType hcType = new HomeCommunityType();
//        hcType.setHomeCommunityId("2.16.840.1.113883.3.200");
//        assingAuthTypeId.setAssigningAuthorityId("1.3");
//        storeMap.setAssigningAuthority(assingAuthTypeId);
//        storeMap.setHomeCommunity(hcType);
//        AcknowledgementType ack = CMServiceHelper.storeAssigningAuthorityToHomeCommunityMapping(storeMap);
//        System.out.println("Acknowledgement message -> "+ack.getMessage());
//        assertNotNull(ack.getMessage());
    }
    
    @Test
    public void testFormatHomeCommunityId()
    {
        String formattedHomeCommunityId = CMTransform.formatHomeCommunityId("urn:id:2.16.840.1.113883.3.200");
        System.out.println("Formatted Home Community Id->"+formattedHomeCommunityId);
        assertNotNull(formattedHomeCommunityId);
        assertEquals(formattedHomeCommunityId, "2.16.840.1.113883.3.200");
        formattedHomeCommunityId = CMTransform.formatHomeCommunityId("1111.1111.1111.1111.2");
        System.out.println("Home Community Not required to format just return the same value as input ->"+formattedHomeCommunityId);
        assertEquals(formattedHomeCommunityId, "1111.1111.1111.1111.2");
    }
    
    @Test
    public void testGetAssigningAuthoritiesByHomeCommunity()
    {
        GetAssigningAuthoritiesByHomeCommunityRequestType request = new GetAssigningAuthoritiesByHomeCommunityRequestType();
        HomeCommunityType hcType = new HomeCommunityType();
        hcType.setHomeCommunityId("1.2.3.4.55.500");
        request.setHomeCommunity(hcType);
        GetAssigningAuthoritiesByHomeCommunityResponseType response = CMServiceHelper.getAssigningAuthoritiesByHomeCommunity(request);
        List<AssigningAuthorityType> assingList = response.getAssigningAuthoritiesId().getAssigningAuthority();
        //assertNotNull(assingList);
        //assertTrue(assingList.size()==2);
    }
}
