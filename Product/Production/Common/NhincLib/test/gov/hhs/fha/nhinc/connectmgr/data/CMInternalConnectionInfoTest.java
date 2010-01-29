/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoTest {

    public CMInternalConnectionInfoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfo.
     *    This tests the clear method when there is data already in the field.
     */
    @Test
    public void testClearWithData() {
        System.out.println("testClearWithData");

        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        assertEquals(true, TestHelper.assertConnInfoNotEmpty(instance));

        instance.clear();

        assertEquals(true, TestHelper.assertConnInfoEmpty(instance));
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfo.
     *    This tests the clear method when there is not data already in the field.
     */
    @Test
    public void testClearWithNoData() {
        System.out.println("testClearWithNoData");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.clear();

        assertEquals(true, TestHelper.assertConnInfoEmpty(instance));
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMInternalConnectionInfo oCompare = new CMInternalConnectionInfo();
        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal
     */
    @Test
    public void testEquals() {
        System.out.println("testEquals");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal, but with different cases for strings
     */
    @Test
    public void testEqualsCaseDifference() {
        System.out.println("testEqualsCaseDifference");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "Community1", "NHIN Community 1", "Service 1 Description", "https://service1.COM", "Service 1", "fl");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "nhin Community 1", "SerVice 1 Description", "https://service1.com", "SERVICE 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are not equal
     */
    @Test
    public void testNotEquals() {
        System.out.println("testNotEquals");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("2.2", "community2", "NHIN Community 2", "Service 2 Description", "https://service2.com", "Service 2", "AL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the HCID
     */
    @Test
    public void testNotEqualsHcid() {
        System.out.println("testNotEqualsHcid");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("2.2", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the Community Name
     */
    @Test
    public void testNotEqualsCommName() {
        System.out.println("testNotEqualsCommName");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community2", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the Community Description
     */
    @Test
    public void testNotEqualsCommDesc() {
        System.out.println("testNotEqualsCommDesc");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 2", "Service 1 Description", "https://service1.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the Service Description
     */
    @Test
    public void testNotEqualsServDesc() {
        System.out.println("testNotEqualsServDesc");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 2 Description", "https://service1.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the Service URL
     */
    @Test
    public void testNotEqualsServUrl() {
        System.out.println("testNotEqualsServUrl");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service2.com", "Service 1", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the Service Name
     */
    @Test
    public void testNotEqualsServName() {
        System.out.println("testNotEqualsServName");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 2", "FL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfo.
     *    Test that two objects are equal except for the State
     */
    @Test
    public void testNotEqualsState() {
        System.out.println("testNotEqualsState");

        CMInternalConnectionInfo oCompare = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "AL");
        CMInternalConnectionInfo instance = TestHelper.createConnInfo("1.1", "community1", "NHIN Community 1", "Service 1 Description", "https://service1.com", "Service 1", "FL");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getDescription method, of class CMInternalConnectionInfo.
     *    Test getting a null description
     */
    @Test
    public void testSetGetDescriptionNull() {
        System.out.println("testSetGetDescriptionNull");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setDescription(null);

        String result = instance.getDescription();

        assertNull(result);
    }

    /**
     * Test of getDescription method, of class CMInternalConnectionInfo.
     *    Test getting a valid description
     */
    @Test
    public void testSetGetDescription() {
        System.out.println("testSetGetDescription");
        
        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setDescription("NHIN Community 1");

        String result = instance.getDescription();
        
        assertEquals("NHIN Community 1", result);
    }

    /**
     * Test of getHomeCommunityId method, of class CMInternalConnectionInfo.
     *    Test setting/getting a null home community id
     */
    @Test
    public void testSetGetHomeCommunityIdNull() {
        System.out.println("testSetGetHomeCommunityIdNull");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setHomeCommunityId(null);

        String result = instance.getHomeCommunityId();
        
        assertNull(result);
    }

    /**
     * Test of getHomeCommunityId method, of class CMInternalConnectionInfo.
     *    Test setting/getting a valid home community id
     */
    @Test
    public void testSetGetHomeCommunityId() {
        System.out.println("testSetGetHomeCommunityId");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setHomeCommunityId("1.1");

        String result = instance.getHomeCommunityId();
        assertEquals("1.1", result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfo.
     *    Test setting/getting a null home community name
     */
    @Test
    public void testSetGetNameNull() {
        System.out.println("testSetGetNameNull");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setName(null);

        String result = instance.getName();

        assertNull(result);
    }

    /**
     * Test of getName method, of class CMInternalConnectionInfo.
     *    Test setting/getting a valid home community name
     */
    @Test
    public void testSetGetName() {
        System.out.println("testSetGetName");
        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setName("Community 1");

        String result = instance.getName();

        assertEquals("Community 1", result);
    }

    /**
     * Test of getServices method, of class CMInternalConnectionInfo.
     *    Test setting/getting a null service
     */
    @Test
    public void testSetGetServicesNull() {
        System.out.println("testSetGetServicesNull");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        CMInternalConnInfoServices expResult = null;
        instance.setServices(expResult);

        CMInternalConnInfoServices result = instance.getServices();
        assertNull(result);

    }

    /**
     * Test of setServices method, of class CMInternalConnectionInfo.
     *    Test setting/getting a valid service
     */
    @Test
    public void testSetGetServices() {
        System.out.println("setServices");
        CMInternalConnInfoServices services = new CMInternalConnInfoServices();
        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();

        CMInternalConnInfoService service = new CMInternalConnInfoService();
        service.setDescription("Service 1 Description");
        service.setEndpointURL("https://service1.com");
        service.setExternalService(true);
        service.setName("Service 1");
        services.getService().add(service);
        instance.setServices(services);

        CMInternalConnInfoServices result = instance.getServices();

        assertEquals(services, result);
    }

    /**
     * Test of getState method, of class CMInternalConnectionInfo.
     *   Test setting/getting a null state
     */
    @Test
    public void testSetGetStateNull() {
        System.out.println("testSetGetStateNull");

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        CMInternalConnectionInfoStates expResult = null;
        instance.setStates(expResult);

        CMInternalConnectionInfoStates result = instance.getStates();
        assertNull(result);
    }

    /**
     * Test of setStates method, of class CMInternalConnectionInfo.
     *    Test ssetting/getting a valid state
     */
    @Test
    public void testSetGetStates() {
        System.out.println("testSetGetStates");

        CMInternalConnectionInfoStates states = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        CMInternalConnectionInfoState state = new CMInternalConnectionInfoState();
        state.setName("FL");
        states.getState().add(state);
        instance.setStates(states);
        
        CMInternalConnectionInfoStates result = instance.getStates();

        assertEquals(states, result);
    }

}