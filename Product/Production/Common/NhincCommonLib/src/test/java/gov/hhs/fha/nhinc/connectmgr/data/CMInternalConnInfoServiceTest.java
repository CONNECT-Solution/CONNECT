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
public class CMInternalConnInfoServiceTest {

    public CMInternalConnInfoServiceTest() {
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
     * Test of setSupportsLIFT and getSupportsLIFT methods, of class CMInternalConnectionInfo.
     *    Test setting/getting a false value
     */
    @Test
    public void testSetGetSupportLiftFalse() {
        System.out.println("testSetGetSupportLiftFalse");

        CMInternalConnInfoService instance = new CMInternalConnInfoService();
        instance.setSupportsLIFTFlag(false);

        boolean result = instance.getSupportsLIFTFlag();

        assertEquals(false, result);
    }

    /**
     * Test of setSupportsLIFT and getSupportsLIFT methods, of class CMInternalConnectionInfo.
     *    Test setting/getting a true value
     */
    @Test
    public void testSetGetSupportLiftTrue() {
        System.out.println("testSetGetSupportLiftTrue");

        CMInternalConnInfoService instance = new CMInternalConnInfoService();
        instance.setSupportsLIFTFlag(true);

        boolean result = instance.getSupportsLIFTFlag();

        assertEquals(true, result);
    }

    /**
     * Test of getServices method, of class CMInternalConnectionInfo.
     *    Test setting/getting a null service
     */
    @Test
    public void testSetGetProtocolsNull() {
        System.out.println("testSetGetProtocolsNull");

        CMInternalConnInfoService instance = new CMInternalConnInfoService();
        CMInternalConnectionInfoLiftProtocols expResult = null;
        instance.setLiftProtocols(expResult);

        CMInternalConnectionInfoLiftProtocols result = instance.getLiftProtocols();
        assertNull(result);

    }

    /**
     * Test of setServices method, of class CMInternalConnectionInfo.
     *    Test setting/getting a valid service
     */
    @Test
    public void testSetGetProtocols() {
        System.out.println("testSetGetProtocols");
        CMInternalConnectionInfoLiftProtocols protocols = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnInfoService instance = new CMInternalConnInfoService();

        CMInternalConnectionInfoLiftProtocol protocol = new CMInternalConnectionInfoLiftProtocol();
        protocol.setLiftProtocol("HTTPS");
        protocols.getProtocol().add(protocol);
        instance.setLiftProtocols(protocols);

        CMInternalConnectionInfoLiftProtocols result = instance.getLiftProtocols();

        assertEquals(protocols, result);
    }

}