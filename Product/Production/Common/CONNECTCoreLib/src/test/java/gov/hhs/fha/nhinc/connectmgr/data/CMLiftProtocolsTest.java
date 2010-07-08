/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.ArrayList;
import java.util.List;
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
public class CMLiftProtocolsTest {

    public CMLiftProtocolsTest() {
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
     * Test of clear method, of class CMLiftProtocols.
     */
    @Test
    public void testClear() {
        System.out.println("testClear");
        CMLiftProtocols instance = new CMLiftProtocols();
        instance.clear();

        assertEquals(0, instance.getProtocol().size());
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testEqualsSingle() {
        System.out.println("testEqualsSingle");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        oCompare.setProtocol(protocolList1);
        
        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("HTTPS");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testEqualsMult() {
        System.out.println("testEqualsMult");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("HTTPS");
        protocolList2.add("SMTP");
        protocolList2.add("FTP");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testEqualsMultDiffCase() {
        System.out.println("testEqualsMult");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("https");
        protocolList2.add("SmTp");
        protocolList2.add("ftP");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testEqualsBothNull() {
        System.out.println("testEqualsBothNull");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        oCompare.setProtocol(null);

        CMLiftProtocols instance = new CMLiftProtocols();
        instance.setProtocol(null);

        boolean result = instance.equals(oCompare);
        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testNotEqualsSingle() {
        System.out.println("testNotEqualsSingle");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("FTP");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testNotEqualsMultSingleMismatch() {
        System.out.println("testNotEqualsMultSingleMismatch");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("QVC");
        protocolList2.add("SMTP");
        protocolList2.add("FTP");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testNotEqualsMultAllMismatch() {
        System.out.println("testNotEqualsMultAllMismatch");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("QVC");
        protocolList2.add("ABC");
        protocolList2.add("123");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testNotEqualsOneNull() {
        System.out.println("testNotEqualsOneNull");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        instance.setProtocol(null);

        boolean result = instance.equals(oCompare);
        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMLiftProtocols.
     */
    @Test
    public void testNotEqualsMultDiffSizes() {
        System.out.println("testNotEqualsMultDiffSizes");
        CMLiftProtocols oCompare = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        oCompare.setProtocol(protocolList1);

        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList2 = new ArrayList<String>();
        protocolList2.add("HTTPS");
        protocolList2.add("SMTP");
        instance.setProtocol(protocolList2);

        boolean result = instance.equals(oCompare);
        assertEquals(false, result);
    }

    /**
     * Test of getProtocol method, of class CMLiftProtocols.
     */
    @Test
    public void testSetGetProtocolSingle() {
        System.out.println("testSetGetProtocolSingle");
        CMLiftProtocols instance = new CMLiftProtocols();
        String protocol1 = new String();
        protocol1 = "HTTPS";
        instance.getProtocol().add(protocol1);

        List<String> result = instance.getProtocol();

        assertEquals(1, result.size());
        assertEquals("HTTPS", result.get(0));
    }

    /**
     * Test of setProtocol method, of class CMLiftProtocols.
     */
    @Test
    public void testSetGetProtocolMult() {
        System.out.println("testSetGetProtocolMult");
        CMLiftProtocols instance = new CMLiftProtocols();
        List<String> protocolList1 = new ArrayList<String>();
        protocolList1.add("HTTPS");
        protocolList1.add("SMTP");
        protocolList1.add("FTP");
        instance.setProtocol(protocolList1);

        List<String> result = instance.getProtocol();

        assertEquals(3, result.size());
        assertEquals("HTTPS", result.get(0));
        assertEquals("SMTP", result.get(1));
        assertEquals("FTP", result.get(2));
    }

    /**
     * Test of createCopy method, of class CMLiftProtocols.
     */
    @Test
    public void testCreateCopy() {

        CMLiftProtocols origCMLiftProtocols = generateTestCMLiftProtocols();
        CMLiftProtocols copyCMLiftProtocols = origCMLiftProtocols.createCopy();
        assertTrue(origCMLiftProtocols.equals(copyCMLiftProtocols));
    }

    /**
     * Create a generic CMLiftProtocols filled with test data
     *
     * @return The test CMLiftProtocols
     */
    private CMLiftProtocols generateTestCMLiftProtocols() {
        CMLiftProtocols testLiftProtocols = new CMLiftProtocols();
        testLiftProtocols.getProtocol().add("testLiftProtocol");
        return testLiftProtocols;
    }
}