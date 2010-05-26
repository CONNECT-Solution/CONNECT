package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMContact;

import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import java.util.List;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class is used to test the ConnectionManagerCache class.
 * 
 * @author Les Westberg
 */
public class ConnectionManagerCacheTest {

    public ConnectionManagerCacheTest() {
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
     * Test of forceRefreshUDDICache method, of class ConnectionManagerCache.
     */
    @Test
    public void testForceRefreshUDDICache() throws Exception
    {
        System.out.println("forceRefreshUDDICache");
        
        try
        {
            ConnectionManagerCache.forceRefreshUDDICache();
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    /**
     * Test of forceRefreshInternalConnectCache method, of class ConnectionManagerCache.
     */
    @Test
    public void testForceRefreshInternalConnectCache() throws Exception
    {
        System.out.println("forceRefreshInternalConnectCache");
        
        try
        {
            ConnectionManagerCache.forceRefreshInternalConnectCache();
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    /**
     * Test of getAllCommunities method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetAllCommunities() throws Exception
    {
        System.out.println("getAllCommunities");

        try
        {
            List<CMHomeCommunity> oaHomeComm = null;
            oaHomeComm = ConnectionManagerCache.getAllCommunities();
            assertNotNull(oaHomeComm);
            assertEquals(6, oaHomeComm.size());
            boolean baFound[] = {false, false, false, false, false, false};
            
            for (CMHomeCommunity oComm : oaHomeComm)
            {
                if (oComm.getHomeCommunityId().equals("1111.1111.1111.1111"))
                {
                    baFound[0] = true;
                    assertEquals("Home1", oComm.getName());
                    assertEquals("Home1 Description", oComm.getDescription());
                }
                else if (oComm.getHomeCommunityId().equals("2222.2222.2222.2222"))
                {
                    baFound[1] = true;
                    assertEquals("Home2", oComm.getName());
                    assertEquals("Home2 Description", oComm.getDescription());
                }
                else if (oComm.getHomeCommunityId().equals("3333.3333.3333.3333"))
                {
                    baFound[2] = true;
                    assertEquals("Home3", oComm.getName());
                    assertEquals("Home3 Description", oComm.getDescription());
                }
                else if (oComm.getHomeCommunityId().equals("1111.1111.1111.1111..2"))
                {
                    baFound[3] = true;
                    assertEquals("DuplicateFromUDDI Name", oComm.getName());
                    assertEquals("DuplicateFromUDDI Description", oComm.getDescription());
                }
                else if (oComm.getHomeCommunityId().equals("1111.1111.1111.1111..1"))
                {
                    baFound[4] = true;
                    assertEquals("BusinessName.1.1", oComm.getName());
                    assertEquals("BusinessDescription.1.1", oComm.getDescription());
                }
                else if (oComm.getHomeCommunityId().equals("2.16.840.1.113883.3.200"))
                {
                    baFound[5] = true;
                    assertEquals("VA", oComm.getName());
                    assertEquals("Federal-VA", oComm.getDescription());
                }
                else
                {
                    fail("Found an unexpected entry.  HomeCommunityId:" + oComm.getHomeCommunityId());
                }
            }
            assertTrue(baFound[0]);
            assertTrue(baFound[1]);
            assertTrue(baFound[2]);
            assertTrue(baFound[3]);
            assertTrue(baFound[4]);
            assertTrue(baFound[5]);
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
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
    private void validateEntity_1111_1111_1111_1111(CMBusinessEntity oEntity, String sUniformServiceName)
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
        for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("Service 1 Name"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 1 Description", oService.getDescriptions().getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service1.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("Service 2 Name"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 2 Description", oService.getDescriptions().getDescription().get(0));

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
     * HomeCommunity 2222.2222.2222.2222
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     * 
     */
    private void validateEntity_2222_2222_2222_2222(CMBusinessEntity oEntity, String sUniformServiceName)
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
        for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("Service 1 Name"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 1 Description", oService.getDescriptions().getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service3.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("Service 4 Name"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 4 Description", oService.getDescriptions().getDescription().get(0));

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
     * This method validates the entire contents of a business entity for
     * HomeCommunity 3333.3333.3333.3333
     *
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     *
     */
    private void validateEntity_3333_3333_3333_3333(CMBusinessEntity oEntity, String sUniformServiceName)
    {
        assertNotNull(oEntity);
        assertEquals("3333.3333.3333.3333", oEntity.getHomeCommunityId());

        assertNotNull(oEntity.getNames());
        assertEquals(1, oEntity.getNames().getBusinessName().size());
        assertEquals("Home3", oEntity.getNames().getBusinessName().get(0));

        assertNotNull(oEntity.getDescriptions());
        assertEquals(1, oEntity.getDescriptions().getBusinessDescription().size());
        assertEquals("Home3 Description", oEntity.getDescriptions().getBusinessDescription().get(0));

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
        for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("Service 7 Name"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 7 Description", oService.getDescriptions().getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service7.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("Service 8 Name"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 8 Description", oService.getDescriptions().getDescription().get(0));

                assertEquals(true, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service8.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
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
     * HomeCommunity 1111.1111.1111.1111..2
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateEntity_1111_1111_1111_1111__2(CMBusinessEntity oEntity, String sUniformServiceName)
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
        for (CMContact oContact : oEntity.getContacts().getContact())
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
        assertEquals("ZGRkZGRk", oEntity.getPublicKey());

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
        for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
        {
            if (oService.getUniformServiceName().equals("Service 5 Name"))
            {
                baFoundService[0] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 5 Description", oService.getDescriptions().getDescription().get(0));

                assertEquals(false, oService.isInternalWebService());

                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(1, oService.getBindingTemplates().getBindingTemplate().size());
                assertEquals("http://www.service5.com", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
            }
            else if (oService.getUniformServiceName().equals("Service 6 Name"))
            {
                baFoundService[1] = true;
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(1, oService.getDescriptions().getDescription().size());
                assertEquals("Service 6 Description", oService.getDescriptions().getDescription().get(0));

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
                assertNotNull(oService.getNames().getName());
                assertEquals(2, oService.getNames().getName().size());
                assertEquals("BindingName.2.1.1", oService.getNames().getName().get(0));
                assertEquals("BindingName.2.1.2", oService.getNames().getName().get(1));
                
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(2, oService.getDescriptions().getDescription().size());
                assertEquals("BindingDescription.2.1.1", oService.getDescriptions().getDescription().get(0));
                assertEquals("BindingDescription.2.1.2", oService.getDescriptions().getDescription().get(1));
                
                assertEquals("Service 1 Name", oService.getUniformServiceName());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("EndpointURL.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.2.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());
                assertEquals("1.0", oService.getBindingTemplates().getBindingTemplate().get(0).getServiceVersion());

                assertEquals("BindingKey.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("EndpointURL.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.2.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
				assertEquals("2.0", oService.getBindingTemplates().getBindingTemplate().get(1).getServiceVersion());

            }
            else if (oService.getServiceKey().equals("ServiceKey.2.2"))
            {
                baFoundService[3] = true;
                
                assertNotNull(oService.getNames());
                assertNotNull(oService.getNames().getName());
                assertEquals(2, oService.getNames().getName().size());
                assertEquals("BindingName.2.2.1", oService.getNames().getName().get(0));
                assertEquals("BindingName.2.2.2", oService.getNames().getName().get(1));
                
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(2, oService.getDescriptions().getDescription().size());
                assertEquals("BindingDescription.2.2.1", oService.getDescriptions().getDescription().get(0));
                assertEquals("BindingDescription.2.2.2", oService.getDescriptions().getDescription().get(1));
                
                assertEquals("Service 2 Name", oService.getUniformServiceName());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("EndpointURL.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.2.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());
				assertEquals("1.0", oService.getBindingTemplates().getBindingTemplate().get(0).getServiceVersion());

                assertEquals("BindingKey.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("EndpointURL.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.2.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
				assertEquals("2.0", oService.getBindingTemplates().getBindingTemplate().get(1).getServiceVersion());

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
     * HomeCommunity 1111.1111.1111.1111..1
     * 
     * @param oEntity The business entity for this home community.
     * @param sUniformServiceName If this is passed, then it will validate all of the
     *                            general stuff and only the one specific service and that
     *                            the service is the only one that exists.
     */
    private void validateEntity_1111_1111_1111_1111__1(CMBusinessEntity oEntity, String sUniformServiceName)
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
        for (CMContact oContact : oEntity.getContacts().getContact())
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
        assertEquals("ZGRkZGRk", oEntity.getPublicKey());

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
        
        for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
        {
            assertNotNull(oService.getServiceKey());
            if (oService.getServiceKey().equals("ServiceKey.1.1"))
            {
                baFoundService[0] = true;
                
                assertNotNull(oService.getNames());
                assertNotNull(oService.getNames().getName());
                assertEquals(2, oService.getNames().getName().size());
                assertEquals("BindingName.1.1.1", oService.getNames().getName().get(0));
                assertEquals("BindingName.1.1.2", oService.getNames().getName().get(1));
                
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(2, oService.getDescriptions().getDescription().size());
                assertEquals("BindingDescription.1.1.1", oService.getDescriptions().getDescription().get(0));
                assertEquals("BindingDescription.1.1.2", oService.getDescriptions().getDescription().get(1));
                
                assertEquals("Service 1 Name", oService.getUniformServiceName());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("EndpointURL.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.1.1.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());
				assertEquals("1.0", oService.getBindingTemplates().getBindingTemplate().get(0).getServiceVersion());

                assertEquals("BindingKey.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("EndpointURL.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.1.1.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
				assertEquals("2.0", oService.getBindingTemplates().getBindingTemplate().get(1).getServiceVersion());

            }
            else if (oService.getServiceKey().equals("ServiceKey.1.2"))
            {
                baFoundService[1] = true;
                
                assertNotNull(oService.getNames());
                assertNotNull(oService.getNames().getName());
                assertEquals(2, oService.getNames().getName().size());
                assertEquals("BindingName.1.2.1", oService.getNames().getName().get(0));
                assertEquals("BindingName.1.2.2", oService.getNames().getName().get(1));
                
                assertNotNull(oService.getDescriptions());
                assertNotNull(oService.getDescriptions().getDescription());
                assertEquals(2, oService.getDescriptions().getDescription().size());
                assertEquals("BindingDescription.1.2.1", oService.getDescriptions().getDescription().get(0));
                assertEquals("BindingDescription.1.2.2", oService.getDescriptions().getDescription().get(1));
                
                assertEquals("ServiceName.1.2", oService.getUniformServiceName());
                assertEquals(true, oService.isInternalWebService());
                
                assertNotNull(oService.getBindingTemplates());
                assertNotNull(oService.getBindingTemplates().getBindingTemplate());
                assertEquals(2, oService.getBindingTemplates().getBindingTemplate().size());
                
                assertEquals("BindingKey.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getBindingKey());
                assertEquals("EndpointURL.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL());
                assertEquals("WSDLURL.1.2.1", oService.getBindingTemplates().getBindingTemplate().get(0).getWsdlURL());
				assertEquals("1.0", oService.getBindingTemplates().getBindingTemplate().get(0).getServiceVersion());

                assertEquals("BindingKey.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getBindingKey());
                assertEquals("EndpointURL.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getEndpointURL());
                assertEquals("WSDLURL.1.2.2", oService.getBindingTemplates().getBindingTemplate().get(1).getWsdlURL());
				assertEquals("2.0", oService.getBindingTemplates().getBindingTemplate().get(1).getServiceVersion());

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
     * Test of getAllBusinessEntities method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetAllBusinessEntities() throws Exception
    {
        System.out.println("getAllBusinessEntities");

        try
        {
            CMBusinessEntities oEntities = null;
            oEntities = ConnectionManagerCache.getAllBusinessEntities();
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(6, oEntities.getBusinessEntity().size());
            boolean baFound[] = {false, false, false, false, false, false};
            
            for (CMBusinessEntity oEntity : oEntities.getBusinessEntity())
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
                else if (oEntity.getHomeCommunityId().equals("3333.3333.3333.3333"))
                {
                    validateEntity_3333_3333_3333_3333(oEntity, "");
                    baFound[2] = true;
                }
                else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..2"))
                {
                    validateEntity_1111_1111_1111_1111__2(oEntity, "");
                    baFound[3] = true;
                }
                else if (oEntity.getHomeCommunityId().equals("1111.1111.1111.1111..1"))
                {
                    validateEntity_1111_1111_1111_1111__1(oEntity, "");
                    baFound[4] = true;
                }
                else if (oEntity.getHomeCommunityId().equals("2.16.840.1.113883.3.200"))
                {
                    baFound[5] = true;
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
            assertTrue(baFound[4]);
            assertTrue(baFound[5]);
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
        
    }
    

    /**
     * Test of getBusinessEntity method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetBusinessEntity() throws Exception
    {
        System.out.println("getBusinessEntity");

        try
        {
            CMBusinessEntity oEntity = null;
            oEntity = ConnectionManagerCache.getBusinessEntity("1111.1111.1111.1111");
            assertNotNull(oEntity);
            validateEntity_1111_1111_1111_1111(oEntity, "");
            
            oEntity = ConnectionManagerCache.getBusinessEntity("1111.1111.1111.1111..2");
            assertNotNull(oEntity);
            validateEntity_1111_1111_1111_1111__2(oEntity, "");
            
            oEntity = ConnectionManagerCache.getBusinessEntity("1111.1111.1111.1111..1");
            assertNotNull(oEntity);
            validateEntity_1111_1111_1111_1111__1(oEntity, "");
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
        
    }
    
    /**
     * Test of getBusinessEntitySet method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetBusinessEntitySet() throws Exception
    {
        System.out.println("getBusinessEntitySet");

        try
        {
            CMBusinessEntities oEntities = null;
            ArrayList<String> saHomeCommunityId = new ArrayList<String>();
            saHomeCommunityId.add("1111.1111.1111.1111");
            saHomeCommunityId.add("1111.1111.1111.1111..2");
            saHomeCommunityId.add("1111.1111.1111.1111..1");
            
            oEntities = ConnectionManagerCache.getBusinessEntitySet(saHomeCommunityId);
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(3, oEntities.getBusinessEntity().size());
            
            boolean baFound[] = {false, false, false};
            for (CMBusinessEntity oEntity: oEntities.getBusinessEntity())
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
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
        
    }
    
    /**
     * Test of getBusinessEntityByServiceName method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetBusinessEntityByServiceName() throws Exception
    {
        System.out.println("getBusinessEntityByServiceNamet");

        try
        {
            CMBusinessEntity oEntity = null;
            oEntity = ConnectionManagerCache.getBusinessEntityByServiceName("1111.1111.1111.1111..1", "ServiceName.1.2");
            assertNotNull(oEntity);
            validateEntity_1111_1111_1111_1111__1(oEntity, "ServiceName.1.2");
            
            oEntity = ConnectionManagerCache.getBusinessEntityByServiceName("2222.2222.2222.2222", "Service 1 Name");
            assertNotNull(oEntity);
            validateEntity_2222_2222_2222_2222(oEntity, "Service 1 Name");

            oEntity = ConnectionManagerCache.getBusinessEntityByServiceName("1111.1111.1111.1111..2", "Service 6 Name");
            assertNotNull(oEntity);
            validateEntity_1111_1111_1111_1111__2(oEntity, "Service 6 Name");
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
        
    }
    
    /**
     * Test of getBusinessEntityByServiceName method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetEndpointURLByServiceName() throws Exception
    {
        System.out.println("getEndpointURLByServiceNamet");

        try
        {
            String sEndpointURL = "";
            sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName("1111.1111.1111.1111..1", "ServiceName.1.2");
            assertEquals("EndpointURL.1.2.1", sEndpointURL);
            
            sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName("2222.2222.2222.2222", "Service 1 Name");
            assertEquals("http://www.service3.com", sEndpointURL);

            sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName("1111.1111.1111.1111..2", "Service 6 Name");
            assertEquals("http://www.service6.com", sEndpointURL);
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
        
    }
    
    
    /**
     * Test of getBusinessEntityByServiceName method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetBusinessEntitySetByServiceName() throws Exception
    {
        System.out.println("getBusinessEntitySetByServiceNamet");

        try
        {
            CMBusinessEntities oEntities = null;
            ArrayList<String> saHomeCommunityId = new ArrayList<String>();
            saHomeCommunityId.add("2222.2222.2222.2222");
            saHomeCommunityId.add("1111.1111.1111.1111");
            saHomeCommunityId.add("1111.1111.1111.1111..2");
            saHomeCommunityId.add("1111.1111.1111.1111..1");
            
            oEntities = ConnectionManagerCache.getBusinessEntitySetByServiceName(saHomeCommunityId, "Service 4 Name");
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(1, oEntities.getBusinessEntity().size());
            validateEntity_2222_2222_2222_2222(oEntities.getBusinessEntity().get(0), "Service 4 Name");

            oEntities = ConnectionManagerCache.getBusinessEntitySetByServiceName(saHomeCommunityId, "ServiceName.1.2");
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(1, oEntities.getBusinessEntity().size());
            validateEntity_1111_1111_1111_1111__1(oEntities.getBusinessEntity().get(0), "ServiceName.1.2");
            
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    /**
     * Test of getAllBusinessEntityByServiceName method, of class ConnectionManagerCache.
     */
    @Test
    public void testGetAllBusinessEntitySetByServiceName() throws Exception
    {
        System.out.println("getAllBusinessEntitySetByServiceNamet");

        try
        {
            CMBusinessEntities oEntities = null;
            oEntities = ConnectionManagerCache.getAllBusinessEntitySetByServiceName("Service 4 Name");
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(1, oEntities.getBusinessEntity().size());
            validateEntity_2222_2222_2222_2222(oEntities.getBusinessEntity().get(0), "Service 4 Name");

            oEntities = ConnectionManagerCache.getAllBusinessEntitySetByServiceName("ServiceName.1.2");
            assertNotNull(oEntities);
            assertNotNull(oEntities.getBusinessEntity());
            assertEquals(1, oEntities.getBusinessEntity().size());
            validateEntity_1111_1111_1111_1111__1(oEntities.getBusinessEntity().get(0), "ServiceName.1.2");
            
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Community Not Found for a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesCommunityNotFound() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesCommunityNotFound");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("1.1", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Invalid Community id for a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesInvalidCommunity() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesInvalidCommunity");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("A.B.C", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test valid Community id for a invalid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesInvalidService() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesInvalidService");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 3 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test valid Community and null service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesNullService() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesNullService");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, null);

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test null Community and null service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesNullCommunityAndService() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesNullCommunityAndService");

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(null, null);

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Single Community from internal connections
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesSingleCommunityIntConnections() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleCommunityIntConnections");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Single Community from UDDI
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesSingleCommunityUDDI() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleCommunityUDDI");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity("1111.1111.1111.1111..1", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Two Communities, both from internal connections
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesTwoCommsBothIntConnections() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesTwoCommsBothIntConnections");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("2222.2222.2222.2222", null, null);
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Two Communities, one from UDDI, one from internal connections
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesTwoCommsOneEach() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesTwoCommsBothIntConnections");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("1111.1111.1111.1111..1", null, null);
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test Three Communities
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesThreeCommunities() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesThreeCommunities");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("1111.1111.1111.1111..1", null, null);
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, null);
        NhinTargetCommunityType community3 = TestHelper.createTargetCommunity("2222.2222.2222.2222", null, null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);
        communities.getNhinTargetCommunity().add(community3);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(3, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(1).getHcid());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(2).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(2).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test All Communities for Service 1
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesAllCommunitiesService1() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesAllCommunities1");

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(null, "Service 1 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(4, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.2.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..2", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(1).getHcid());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(2).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(2).getHcid());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(3).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(3).getHcid());
    }

        /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test All Communities for Service 3
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesAllCommunitiesService5() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesAllCommunities5");

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(null, "Service 5 Name");

        assertNotNull(urlList);
        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("http://www.service5.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..2", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test unknown state with a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesUnknownState() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleState");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity(null, null, "AL");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test valid state with an unknown service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesValidStateUnknownService() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleState");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity(null, null, "FL");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 9 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test single state with a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesSingleState() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleState");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity(null, null, "FL");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test single state from UDDI with a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesSingleStateUDDI() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleStateUDDI");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity(null, null, "State.1.1");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test two states from UDDI with a valid service, only one entry has the service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesTwoStatesUDDI() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleStateUDDI");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity(null, null, "State.1.1");
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity(null, null, "State.2.1");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "ServiceName.1.2");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.2.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
    }
    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test two states from UDDI and internal connections with a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesTwoStatesBothFiles() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesTwoStatesBothFiles");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity(null, null, "State.1.1");
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity(null, null, "FL");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test two states with a valid service
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesTwoStates() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesTwoStates");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity(null, null, "FL");
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity(null, null, "AZ");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test two states with same entry with a valid service
     *    Should only return a single URL because duplicates get removed
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesSingleHcidTwoStates() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesSingleHcidTwoStates");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity(null, null, "VA");
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity(null, null, "AZ");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test querying for hcid and state in two different entries
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesHcidAndStateSeparate() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesHcidAndStateSeparate");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("1111.1111.1111.1111..1", null, null);
        NhinTargetCommunityType community2 = TestHelper.createTargetCommunity(null, null, "AZ");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);
        communities.getNhinTargetCommunity().add(community2);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test querying for hcid and state in a single entry
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesHcidAndStateSingle() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesHcidAndStateSingle");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("1111.1111.1111.1111..1", null, "AZ");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(2, urlList.getUrlInfo().size());
        assertEquals("EndpointURL.1.1.1", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111..1", urlList.getUrlInfo().get(0).getHcid());
        assertEquals("http://www.service3.com", urlList.getUrlInfo().get(1).getUrl());
        assertEquals("2222.2222.2222.2222", urlList.getUrlInfo().get(1).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test querying for hcid and state that overlap
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesDupHcidAndState() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesDupHcidAndState");

        NhinTargetCommunityType community1 = TestHelper.createTargetCommunity("1111.1111.1111.1111", null, "FL");
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community1);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(false, urlList.getUrlInfo().isEmpty());
        assertEquals(1, urlList.getUrlInfo().size());
        assertEquals("http://www.service1.com", urlList.getUrlInfo().get(0).getUrl());
        assertEquals("1111.1111.1111.1111", urlList.getUrlInfo().get(0).getHcid());
    }

    /**
     * Test of getEndpontURLFromNhinTargetCommunities method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testGetEndpontURLFromNhinTargetCommunitiesList() throws Exception
    {
        System.out.println("testGetEndpontURLFromNhinTargetCommunitiesList");

        NhinTargetCommunityType community = TestHelper.createTargetCommunity(null, "list", null);
        NhinTargetCommunitiesType communities = new NhinTargetCommunitiesType();

        communities.getNhinTargetCommunity().add(community);

        CMUrlInfos urlList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(communities, "Service 1 Name");

        assertEquals(true, urlList.getUrlInfo().isEmpty());
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityTrue() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunity");

        String homeCommunityId = "1111.1111.1111.1111";
        String protocol = "HTTPS";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "Service 1 Name");

        assertEquals(true, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityTrueCaseDiff() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityTrueCaseDiff");

        String homeCommunityId = "1111.1111.1111.1111";
        String protocol = "https";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "Service 1 Name");

        assertEquals(true, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityTrueMultLast() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityTrueMultLast");

        String homeCommunityId = "2.16.840.1.113883.3.200";
        String protocol = "XMPP";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "nhincsubjectdiscovery");

        assertEquals(true, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityTrueMultMidDiffCase() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityTrueMultMidDiffCase");

        String homeCommunityId = "2.16.840.1.113883.3.200";
        String protocol = "ftp";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "nhincsubjectdiscovery");

        assertEquals(true, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityTrueMultFirstMixedCase() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityTrueMultFirstMixedCase");

        String homeCommunityId = "2.16.840.1.113883.3.200";
        String protocol = "hTtPs";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "nhincsubjectdiscovery");

        assertEquals(true, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityFalseProtocol() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityFalseProtocol");

        String homeCommunityId = "2.16.840.1.113883.3.200";
        String protocol = "ABC";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "nhincsubjectdiscovery");

        assertEquals(false, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityFalseFlag() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityFalseFlag");

        String homeCommunityId = "1111.1111.1111.1111..2";
        String protocol = "HTTPS";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "Service 5 Name");

        assertEquals(false, result);
    }

    /**
     * Test of liftProtocolSupportedForHomeCommunity method, of class ConnectionManagerCache.
     *    Test List, since this is not implemented yet it should return null
     */
    @Test
    public void testLiftProtocolSupportedForHomeCommunityFalseNoTags() throws Exception
    {
        System.out.println("testLiftProtocolSupportedForHomeCommunityFalseNoTags");

        String homeCommunityId = "3333.3333.3333.3333";
        String protocol = "HTTPS";

        boolean result = ConnectionManagerCache.liftProtocolSupportedForHomeCommunity(homeCommunityId, protocol, "Service 7 Name");

        assertEquals(false, result);
    }
    
}
