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
public class CMBusinessEntityTest {

    public CMBusinessEntityTest() {
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
     * Test of clear method, of class CMBusinessEntity.
     */
    @Test
    public void testClearNoData() {
        System.out.println("testClearNoData");
        CMBusinessEntity instance = new CMBusinessEntity();
        instance.clear();

        assertEquals(true, TestHelper.assertBusinessEntityEmpty(instance));
    }

    /**
     * Test of getBusinessKey method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetBusinessKey() {
        System.out.println("testSetGetBusinessKey");
        CMBusinessEntity instance = new CMBusinessEntity();

        String expResult = "testKey";

        instance.setBusinessKey(expResult);

        String result = instance.getBusinessKey();

        assertEquals(expResult, result);
    }

    /**
     * Test of getBusinessServices method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetBusinessServices() {
        System.out.println("testSetGetBusinessServices");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMBusinessServices businessServices = new CMBusinessServices();
        CMBusinessService service = new CMBusinessService();
        service.setUniformServiceName("testService");
        businessServices.getBusinessService().add(service);
        instance.setBusinessServices(businessServices);

        CMBusinessServices result = instance.getBusinessServices();

        assertEquals("testService", result.getBusinessService().get(0).getUniformServiceName());
    }

    /**
     * Test of getContacts method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetContacts() {
        System.out.println("testSetGetContacts");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMContacts contacts = new CMContacts();
        CMContact contact = new CMContact();
        CMPersonNames persons = new CMPersonNames();
        persons.getPersonName().add("Dave Johnson");
        contact.setPersonNames(persons);
        contacts.getContact().add(contact);
        instance.setContacts(contacts);

        CMContacts result = instance.getContacts();

        assertEquals("Dave Johnson", result.getContact().get(0).getPersonNames().getPersonName().get(0));
    }

    /**
     * Test of getDescriptions method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetDescriptions() {
        System.out.println("testSetGetDescriptions");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMBusinessDescriptions descriptions = new CMBusinessDescriptions();
        descriptions.getBusinessDescription().add("TestDescription");
        instance.setDescriptions(descriptions);

        CMBusinessDescriptions result = instance.getDescriptions();

        assertEquals("TestDescription", result.getBusinessDescription().get(0));
    }

    /**
     * Test of getDiscoveryURLs method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetDiscoveryURLs() {
        System.out.println("testSetGetDiscoveryURLs");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMDiscoveryURLs discoveryURLs = new CMDiscoveryURLs();
        discoveryURLs.getDiscoveryURL().add("http://testurl.com");
        instance.setDiscoveryURLs(discoveryURLs);
        
        CMDiscoveryURLs result = instance.getDiscoveryURLs();
        
        assertEquals("http://testurl.com", result.getDiscoveryURL().get(0));
    }

    /**
     * Test of isFederalHIE method, of class CMBusinessEntity.
     */
    @Test
    public void testIsFederalHIEFalse() {
        System.out.println("testIsFederalHIEFalse");
        CMBusinessEntity instance = new CMBusinessEntity();
        instance.setFederalHIE(false);
        boolean expResult = false;
        
        boolean result = instance.isFederalHIE();

        assertEquals(expResult, result);
    }

    /**
     * Test of isFederalHIE method, of class CMBusinessEntity.
     */
    @Test
    public void testIsFederalHIETrue() {
        System.out.println("testIsFederalHIETrue");
        CMBusinessEntity instance = new CMBusinessEntity();
        instance.setFederalHIE(true);
        boolean expResult = true;

        boolean result = instance.isFederalHIE();

        assertEquals(expResult, result);
    }

    /**
     * Test of getHomeCommunityId method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetHomeCommunityId() {
        System.out.println("testSetGetHomeCommunityId");
        CMBusinessEntity instance = new CMBusinessEntity();
        String homeCommunityId = "1.2";
        instance.setHomeCommunityId(homeCommunityId);
        
        String result = instance.getHomeCommunityId();

        assertEquals("1.2", result);
    }

    /**
     * Test of getNames method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetNames() {
        System.out.println("testSetGetNames");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMBusinessNames names = new CMBusinessNames();
        names.getBusinessName().add("Business1");
        names.getBusinessName().add("Business2");
        instance.setNames(names);

        CMBusinessNames result = instance.getNames();

        assertEquals(2, result.getBusinessName().size());
        assertEquals("Business1", result.getBusinessName().get(0));
        assertEquals("Business2", result.getBusinessName().get(1));
    }

    /**
     * Test of getPublicKey method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetPublicKey() {
        System.out.println("testSetGetPublicKey");
        CMBusinessEntity instance = new CMBusinessEntity();
        String publicKey = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        instance.setPublicKey(publicKey);
        
        String result = instance.getPublicKey();

        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    /**
     * Test of getPublicKeyURI method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetPublicKeyURI() {
        System.out.println("testSetGetPublicKeyURI");
        CMBusinessEntity instance = new CMBusinessEntity();
        String publicKeyURI = "http://www.publickey.com";
        instance.setPublicKeyURI(publicKeyURI);
        
        String result = instance.getPublicKeyURI();

        assertEquals("http://www.publickey.com", result);
    }

    /**
     * Test of getStates method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetStates() {
        System.out.println("testSetGetStates");
        CMBusinessEntity instance = new CMBusinessEntity();
        CMStates states = new CMStates();
        states.getState().add("FL");
        instance.setStates(states);

        CMStates result = instance.getStates();

        assertEquals("FL", result.getState().get(0));
    }

    

}