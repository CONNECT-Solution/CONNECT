package gov.hhs.fha.nhinc.connectmgr.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the serialization and deserialization of the InternalConnectionInfos object.
 * 
 * @author Les Westberg
 */
public class CMInternalConnectionInfosXMLTest
{
    public CMInternalConnectionInfosXMLTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of serialize method, of class CMInternalConnectionInfosXML.
     */
    @Test
    public void testSerializeAndDeserialize()
    {
        System.out.println("serializeAndDeserialize");
        
        CMInternalConnectionInfos oConnInfos = new CMInternalConnectionInfos();
        CMInternalConnectionInfo oConnInfo = new CMInternalConnectionInfo();
        oConnInfos.getInternalConnectionInfo().add(oConnInfo);
        oConnInfo.setDescription("Home1 Description");
        oConnInfo.setHomeCommunityId("1111.1111.1111.1111");
        oConnInfo.setName("Home1");
        CMInternalConnectionInfoStates oStates = new CMInternalConnectionInfoStates();
        oConnInfo.setStates(oStates);
        CMInternalConnectionInfoState state = new CMInternalConnectionInfoState();
        oStates.getState().add(state);
        state.setName("FL");
        CMInternalConnInfoServices oServices = new CMInternalConnInfoServices();
        oConnInfo.setServices(oServices);
        CMInternalConnInfoService oService = new CMInternalConnInfoService();
        oServices.getService().add(oService);
        oService.setDescription("Service 1 Description");
        oService.setName("Service 1 Name");
        oService.setEndpointURL("http://www.service1.com");
        oService.setExternalService(true);
        oService = new CMInternalConnInfoService();
        oServices.getService().add(oService);
        oService.setDescription("Service 2 Description");
        oService.setName("Service 2 Name");
        oService.setEndpointURL("http://www.service2.com");
        oService.setExternalService(false);
        
        oConnInfo = new CMInternalConnectionInfo();
        oConnInfos.getInternalConnectionInfo().add(oConnInfo);
        oConnInfo.setDescription("Home2 Description");
        oConnInfo.setHomeCommunityId("2222.2222.2222.2222");
        oConnInfo.setName("Home2");
        oStates = new CMInternalConnectionInfoStates();
        oConnInfo.setStates(oStates);
        state = new CMInternalConnectionInfoState();
        oStates.getState().add(state);
        state.setName("AL");
        oServices = new CMInternalConnInfoServices();
        oConnInfo.setServices(oServices);
        oService = new CMInternalConnInfoService();
        oServices.getService().add(oService);
        oService.setDescription("Service 3 Description");
        oService.setName("Service 3 Name");
        oService.setEndpointURL("http://www.service3.com");
        oService.setExternalService(true);
        oService = new CMInternalConnInfoService();
        oServices.getService().add(oService);
        oService.setDescription("Service 4 Description");
        oService.setName("Service 4 Name");
        oService.setEndpointURL("http://www.service4.com");
        oService.setExternalService(false);
        
        String sXML = CMInternalConnectionInfosXML.serialize(oConnInfos);
        assertNotNull(sXML);
        System.out.println(sXML);
        
        // Now deserialize and see if we got back what we expected.
        //---------------------------------------------------------
        CMInternalConnectionInfos oResult = CMInternalConnectionInfosXML.deserialize(sXML);
        assertNotNull(oResult);
        assertNotNull(oResult.getInternalConnectionInfo());
        assertEquals(2, oResult.getInternalConnectionInfo().size());
        boolean baFoundConn[] = {false, false};
        for (CMInternalConnectionInfo oResultConnInfo : oResult.getInternalConnectionInfo())
        {
            if (oResultConnInfo.getName().equals("Home1"))
            {
                baFoundConn[0] = true;
                assertEquals("Home1 Description", oResultConnInfo.getDescription());
                assertEquals("1111.1111.1111.1111", oResultConnInfo.getHomeCommunityId());

                assertEquals(1, oResultConnInfo.getStates().getState().size());
                for (CMInternalConnectionInfoState oState : oResultConnInfo.getStates().getState()) {
                    assertEquals("FL", oState.getName());
                }
                
                assertNotNull(oResultConnInfo.getServices());
                assertNotNull(oResultConnInfo.getServices().getService());
                assertEquals(2, oResultConnInfo.getServices().getService().size());
                boolean baFoundService[] = {false, false};
                for (CMInternalConnInfoService oResultService : oResultConnInfo.getServices().getService())
                {
                    if (oResultService.getName().equals("Service 1 Name"))
                    {
                        baFoundService[0] = true;
                        assertEquals("Service 1 Description", oResultService.getDescription());
                        assertEquals("http://www.service1.com", oResultService.getEndpointURL());
                        assertEquals(true, oResultService.isExternalService());
                    }
                    else if (oResultService.getName().equals("Service 2 Name"))
                    {
                        baFoundService[1] = true;
                        assertEquals("Service 2 Description", oResultService.getDescription());
                        assertEquals("http://www.service2.com", oResultService.getEndpointURL());
                        assertEquals(false, oResultService.isExternalService());
                    }
                    else
                    {
                        fail("Found an unexpected service.");
                    }
                }   // for (CMInternalConnInfoService oResultService : oResultConnInfo.getServices().getService())
                assertTrue(baFoundService[0]);
                assertTrue(baFoundService[1]);
            }   // if (oResultConnInfo.getName().equals("Home1"))
            else if (oResultConnInfo.getName().equals("Home2"))
            {
                baFoundConn[1] = true;
                assertEquals("Home2 Description", oResultConnInfo.getDescription());
                assertEquals("2222.2222.2222.2222", oResultConnInfo.getHomeCommunityId());

                assertEquals(1, oResultConnInfo.getStates().getState().size());
                for (CMInternalConnectionInfoState oState : oResultConnInfo.getStates().getState()) {
                    assertEquals("AL", oState.getName());
                }
                
                assertNotNull(oResultConnInfo.getServices());
                assertNotNull(oResultConnInfo.getServices().getService());
                assertEquals(2, oResultConnInfo.getServices().getService().size());
                boolean baFoundService[] = {false, false};
                for (CMInternalConnInfoService oResultService : oResultConnInfo.getServices().getService())
                {
                    if (oResultService.getName().equals("Service 3 Name"))
                    {
                        baFoundService[0] = true;
                        assertEquals("Service 3 Description", oResultService.getDescription());
                        assertEquals("http://www.service3.com", oResultService.getEndpointURL());
                        assertEquals(true, oResultService.isExternalService());
                    }
                    else if (oResultService.getName().equals("Service 4 Name"))
                    {
                        baFoundService[1] = true;
                        assertEquals("Service 4 Description", oResultService.getDescription());
                        assertEquals("http://www.service4.com", oResultService.getEndpointURL());
                        assertEquals(false, oResultService.isExternalService());
                    }
                    else
                    {
                        fail("Found an unexpected service.");
                    }
                }   // for (CMInternalConnInfoService oResultService : oResultConnInfo.getServices().getService())
                assertTrue(baFoundService[0]);
                assertTrue(baFoundService[1]);
            }
            else
            {
                fail("Found an unexpected CMInternalConnectionInfo object");
            }
        }
        assertTrue(baFoundConn[0]);
        assertTrue(baFoundConn[1]);
        
        
        
        
        
    }

}