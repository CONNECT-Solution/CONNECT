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
public class CMBusinessServiceTest {

    public CMBusinessServiceTest() {
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
     * Test of getLiftSupported method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetLiftSupportedFalse() {
        System.out.println("testSetGetLiftSupportedFalse");
        CMBusinessService instance = new CMBusinessService();
        instance.setLiftSupported(false);

        boolean expResult = false;

        boolean result = instance.getLiftSupported();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLiftSupported method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetLiftSupportedTrue() {
        System.out.println("testSetGetLiftSupportedTrue");
        CMBusinessService instance = new CMBusinessService();
        instance.setLiftSupported(true);

        boolean expResult = true;

        boolean result = instance.getLiftSupported();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLiftProtocols method, of class CMBusinessEntity.
     */
    @Test
    public void testSetGetLiftProtocols() {
        System.out.println("getLiftProtocols");
        CMBusinessService instance = new CMBusinessService();
        CMLiftProtocols protocols = new CMLiftProtocols();
        String protocol = new String();
        protocol = "HTTPS";
        protocols.getProtocol().add(protocol);
        instance.setLiftProtocols(protocols);

        CMLiftProtocols result = instance.getLiftProtocols();

        assertEquals("HTTPS", result.getProtocol().get(0));
    }

}