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
public class CMInternalConnectionInfoLiftProtocolsTest {

    public CMInternalConnectionInfoLiftProtocolsTest() {
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
     * Test of clear method, of class CMInternalConnectionInfoLiftProtocols.
     *   This tests the clear method with a single entry in the list.
     */
    @Test
    public void testClearWithSingle() {
        System.out.println("testClearWithSingle");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol = new CMInternalConnectionInfoLiftProtocol();
        protocol.setLiftProtocol("HTTPS");
        instance.getProtocol().add(protocol);

        assertEquals(1, instance.getProtocol().size());
        assertEquals("HTTPS", instance.getProtocol().get(0).getLiftProtocol());

        instance.clear();

        assertEquals(true, instance.getProtocol().isEmpty());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoLiftProtocols.
     *   This tests the clear method with a multiple entries in the list.
     */
    @Test
    public void testClearWithMultiple() {
        System.out.println("testClearWithMultiple");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        instance.getProtocol().add(protocol1);
        instance.getProtocol().add(protocol2);
        instance.getProtocol().add(protocol3);

        assertEquals(3, instance.getProtocol().size());
        assertEquals("HTTPS", instance.getProtocol().get(0).getLiftProtocol());
        assertEquals("FTP", instance.getProtocol().get(1).getLiftProtocol());
        assertEquals("SMTP", instance.getProtocol().get(2).getLiftProtocol());

        instance.clear();

        assertEquals(true, instance.getProtocol().isEmpty());
    }

    /**
     * Test of clear method, of class CMInternalConnectionInfoLiftProtocols.
     *   This tests the clear method when there is no entries in the list.
     */
    @Test
    public void testClearWithNoEntries() {
        System.out.println("testClearWithNoEntries");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();

        instance.clear();

        assertEquals(true, instance.getProtocol().isEmpty());
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with single entries are equal
     */
    @Test
    public void testEqualsSingleEntries() {
        System.out.println("testEqualsSingleEntries");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        oCompare.getProtocol().add(protocol1);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("HTTPS");
        instance.getProtocol().add(protocol2);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with multiple entries are equal
     */
    @Test
    public void testEqualsMultipleEntries() {
        System.out.println("testEqualsMultipleEntries");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol6 = new CMInternalConnectionInfoLiftProtocol();
        protocol6.setLiftProtocol("SMTP");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);
        instance.getProtocol().add(protocol6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with multiple entries of different cases are equal
     */
    @Test
    public void testEqualsDifferentCases() {
        System.out.println("testEqualsMultipleEntries");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FtP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("smTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("https");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("ftp");
        CMInternalConnectionInfoLiftProtocol protocol6 = new CMInternalConnectionInfoLiftProtocol();
        protocol6.setLiftProtocol("sMtp");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);
        instance.getProtocol().add(protocol6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with single entries are not equal
     */
    @Test
    public void testNotEqualsSingleEntries() {
        System.out.println("testNotEqualsSingleEntries");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTP");
        oCompare.getProtocol().add(protocol1);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("XMPP");
        instance.getProtocol().add(protocol2);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesOneMismatch() {
        System.out.println("testNotEqualsMultipleEntriesOneMismatch");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("XMPP");
        CMInternalConnectionInfoLiftProtocol protocol6 = new CMInternalConnectionInfoLiftProtocol();
        protocol6.setLiftProtocol("SMTP");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);
        instance.getProtocol().add(protocol6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesTwoMismatch() {
        System.out.println("testNotEqualsMultipleEntriesTwoMismatch");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("ABC");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("XMPP");
        CMInternalConnectionInfoLiftProtocol protocol6 = new CMInternalConnectionInfoLiftProtocol();
        protocol6.setLiftProtocol("SMTP");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);
        instance.getProtocol().add(protocol6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesAllMismatch() {
        System.out.println("testNotEqualsMultipleEntriesAllMismatch");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("ABC");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("XMPP");
        CMInternalConnectionInfoLiftProtocol protocol6 = new CMInternalConnectionInfoLiftProtocol();
        protocol6.setLiftProtocol("XYZ");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);
        instance.getProtocol().add(protocol6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with different sizes are not equal
     */
    @Test
    public void testNotEqualsDifferentSizes() {
        System.out.println("testNotEqualsDifferentSizes");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        oCompare.getProtocol().add(protocol1);
        oCompare.getProtocol().add(protocol2);
        oCompare.getProtocol().add(protocol3);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol4 = new CMInternalConnectionInfoLiftProtocol();
        protocol4.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol5 = new CMInternalConnectionInfoLiftProtocol();
        protocol5.setLiftProtocol("FTP");
        instance.getProtocol().add(protocol4);
        instance.getProtocol().add(protocol5);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test that two objects with single entries are not equal if one is empty
     */
    @Test
    public void testNotEqualsSingleEntriesOneNull() {
        System.out.println("testNotEqualsSingleEntriesOneNull");

        CMInternalConnectionInfoLiftProtocols oCompare = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        oCompare.getProtocol().add(protocol1);

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test retreiving protocols from an empty list
     */
    @Test
    public void testGetProtocolsNone() {
        System.out.println("testGetProtocolsNone");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();

        List expResult = new ArrayList<CMInternalConnectionInfoLiftProtocol>();

        List result = instance.getProtocol();

        assertEquals(expResult, result);
        assertEquals(true, result.isEmpty());
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test retreiving protocols with a single entry
     */
    @Test
    public void testGetProtocolsOne() {
        System.out.println("testGetProtocolsOne");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        instance.getProtocol().add(protocol1);

        List<CMInternalConnectionInfoLiftProtocol> result = instance.getProtocol();

        assertEquals("HTTPS", result.get(0).getLiftProtocol());
    }

    /**
     * Test of getProtocol method, of class CMInternalConnectionInfoLiftProtocols.
     *    Test retreiving protocols with multiple entries
     */
    @Test
    public void testGetProtocolsMultiple() {
        System.out.println("testGetProtocolsMultiple");

        CMInternalConnectionInfoLiftProtocols instance = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol1 = new CMInternalConnectionInfoLiftProtocol();
        protocol1.setLiftProtocol("HTTPS");
        CMInternalConnectionInfoLiftProtocol protocol2 = new CMInternalConnectionInfoLiftProtocol();
        protocol2.setLiftProtocol("FTP");
        CMInternalConnectionInfoLiftProtocol protocol3 = new CMInternalConnectionInfoLiftProtocol();
        protocol3.setLiftProtocol("SMTP");
        instance.getProtocol().add(protocol1);
        instance.getProtocol().add(protocol2);
        instance.getProtocol().add(protocol3);

        List<CMInternalConnectionInfoLiftProtocol> result = instance.getProtocol();

        assertEquals("HTTPS", result.get(0).getLiftProtocol());
        assertEquals("FTP", result.get(1).getLiftProtocol());
        assertEquals("SMTP", result.get(2).getLiftProtocol());
    }

}