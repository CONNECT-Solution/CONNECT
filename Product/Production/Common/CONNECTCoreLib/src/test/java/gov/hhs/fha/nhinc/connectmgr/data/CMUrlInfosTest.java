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
 * @author jhoppesc
 */
public class CMUrlInfosTest {

    public CMUrlInfosTest() {
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
     * Test of clear method, of class CMUrlInfos.
     *    This tests the clear method with a single entry in the list.
     */
    @Test
    public void testClearWithSingle() {
        System.out.println("testClearWithSingle");
        
        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo = new CMUrlInfo();
        urlInfo.setHcid("1.1");
        urlInfo.setUrl("http://service.com");
        instance.getUrlInfo().add(urlInfo);

        assertEquals(1, instance.getUrlInfo().size());
        assertEquals("1.1", instance.getUrlInfo().get(0).getHcid());
        assertEquals("http://service.com", instance.getUrlInfo().get(0).getUrl());

        instance.clear();

        assertEquals(true, instance.getUrlInfo().isEmpty());
    }

    /**
     * Test of clear method, of class CMUrlInfos.
     *    This tests the clear method with a multiple entries in the list.
     */
    @Test
    public void testClearWithMultiple() {
        System.out.println("testClearWithMultiple");

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");

        instance.getUrlInfo().add(urlInfo1);
        instance.getUrlInfo().add(urlInfo2);
        instance.getUrlInfo().add(urlInfo3);

        assertEquals(3, instance.getUrlInfo().size());
        assertEquals("1.1", instance.getUrlInfo().get(0).getHcid());
        assertEquals("http://service1.com", instance.getUrlInfo().get(0).getUrl());
        assertEquals("2.2", instance.getUrlInfo().get(1).getHcid());
        assertEquals("http://service2.com", instance.getUrlInfo().get(1).getUrl());
        assertEquals("3.3", instance.getUrlInfo().get(2).getHcid());
        assertEquals("http://service3.com", instance.getUrlInfo().get(2).getUrl());

        instance.clear();

        assertEquals(true, instance.getUrlInfo().isEmpty());
    }

    /**
     * Test of clear method, of class CMUrlInfos.
     *    This tests the clear method when there is no entries in the list.
     */
    @Test
    public void testClearWithNoEntries() {
        System.out.println("testClearWithNoEntries");

        CMUrlInfos instance = new CMUrlInfos();

        instance.clear();

        assertEquals(true, instance.getUrlInfo().isEmpty());
    }

    /**
     * Test of equals method, of class CMUrlInfos.
     *    Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfos instance = new CMUrlInfos();

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are equal
     */
    @Test
    public void testEqualsSingleEntries() {
        System.out.println("testEqualsSingleEntries");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        oCompare.getUrlInfo().add(urlInfo1);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("1.1");
        urlInfo2.setUrl("http://service1.com");
        instance.getUrlInfo().add(urlInfo2);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are equal
     */
    @Test
    public void testEqualsMultipleEntries() {
        System.out.println("testEqualsMultipleEntries");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1");
        urlInfo4.setUrl("http://service1.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2");
        urlInfo5.setUrl("http://service2.com");
        CMUrlInfo urlInfo6 = new CMUrlInfo();
        urlInfo6.setHcid("3.3");
        urlInfo6.setUrl("http://service3.com");
        instance.getUrlInfo().add(urlInfo4);
        instance.getUrlInfo().add(urlInfo5);
        instance.getUrlInfo().add(urlInfo6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries of different cases are equal
     */
    @Test
    public void testEqualsDifferentCases() {
        System.out.println("testEqualsDifferentCases");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1");
        urlInfo4.setUrl("http://SERvice1.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2");
        urlInfo5.setUrl("http://SERVICE2.com");
        CMUrlInfo urlInfo6 = new CMUrlInfo();
        urlInfo6.setHcid("3.3");
        urlInfo6.setUrl("http://servICE3.com");
        instance.getUrlInfo().add(urlInfo4);
        instance.getUrlInfo().add(urlInfo5);
        instance.getUrlInfo().add(urlInfo6);

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are not equal
     */
    @Test
    public void testNotEqualsSingleEntries() {
        System.out.println("testNotEqualsSingleEntries");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        oCompare.getUrlInfo().add(urlInfo1);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        instance.getUrlInfo().add(urlInfo2);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesOneMismatch() {
        System.out.println("testNotEqualsMultipleEntriesOneMismatch");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1.1");
        urlInfo4.setUrl("http://service9.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2");
        urlInfo5.setUrl("http://service2.com");
        CMUrlInfo urlInfo6 = new CMUrlInfo();
        urlInfo6.setHcid("3.3");
        urlInfo6.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo4);
        oCompare.getUrlInfo().add(urlInfo5);
        oCompare.getUrlInfo().add(urlInfo6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesTwoMismatch() {
        System.out.println("testNotEqualsMultipleEntriesTwoMismatch");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1.1");
        urlInfo1.setUrl("http://service9.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1");
        urlInfo4.setUrl("http://service1.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2.2");
        urlInfo5.setUrl("http://service2a.com");
        CMUrlInfo urlInfo6 = new CMUrlInfo();
        urlInfo6.setHcid("3.3");
        urlInfo6.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo4);
        oCompare.getUrlInfo().add(urlInfo5);
        oCompare.getUrlInfo().add(urlInfo6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with multiple entries are not equal
     */
    @Test
    public void testNotEqualsMultipleEntriesAllMismatch() {
        System.out.println("testNotEqualsMultipleEntriesAllMismatch");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1.1");
        urlInfo1.setUrl("http://service9.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3.9");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1");
        urlInfo4.setUrl("http://service1.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2.2");
        urlInfo5.setUrl("http://service2a.com");
        CMUrlInfo urlInfo6 = new CMUrlInfo();
        urlInfo6.setHcid("3.3");
        urlInfo6.setUrl("http://service3b.com");
        oCompare.getUrlInfo().add(urlInfo4);
        oCompare.getUrlInfo().add(urlInfo5);
        oCompare.getUrlInfo().add(urlInfo6);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with different sizes are not equal
     */
    @Test
    public void testNotEqualsDifferentSizes() {
        System.out.println("testNotEqualsDifferentSizes");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        oCompare.getUrlInfo().add(urlInfo1);
        oCompare.getUrlInfo().add(urlInfo2);
        oCompare.getUrlInfo().add(urlInfo3);

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo4 = new CMUrlInfo();
        urlInfo4.setHcid("1.1");
        urlInfo4.setUrl("http://service1.com");
        CMUrlInfo urlInfo5 = new CMUrlInfo();
        urlInfo5.setHcid("2.2");
        urlInfo5.setUrl("http://service2.com");

        oCompare.getUrlInfo().add(urlInfo4);
        oCompare.getUrlInfo().add(urlInfo5);

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMInternalConnectionInfoStates.
     *    Test that two objects with single entries are not equal if one is empty
     */
    @Test
    public void testNotEqualsSingleEntriesOneNull() {
        System.out.println("testNotEqualsSingleEntriesOneNull");

        CMUrlInfos oCompare = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        oCompare.getUrlInfo().add(urlInfo1);

        CMUrlInfos instance = new CMUrlInfos();

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getUrlInfo method, of class CMUrlInfos.
     *    Test retreiving url information from an empty list
     */
    @Test
    public void testSetGetUrlInfoNone() {
        System.out.println("testSetGetUrlInfoNone");

        CMUrlInfos instance = new CMUrlInfos();
        List expResult = new ArrayList<CMUrlInfo>();

        List result = instance.getUrlInfo();

        assertEquals(expResult, result);
        assertEquals(true, result.isEmpty());
    }

    /**
     * Test of getState method, of class CMInternalConnectionInfoStates.
     *    Test retreiving url infos with a single entry
     */
    @Test
    public void testSetGetUrlInfoOne() {
        System.out.println("testSetGetUrlInfoOne");

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        instance.getUrlInfo().add(urlInfo1);

        List<CMUrlInfo> result = instance.getUrlInfo();

        assertEquals("1.1", result.get(0).getHcid());
        assertEquals("http://service1.com", result.get(0).getUrl());
    }

    /**
     * Test of getState method, of class CMInternalConnectionInfoStates.
     *    Test retreiving url infos with multiple entries
     */
    @Test
    public void testSetGetUrlInfoMultiple() {
        System.out.println("testSetGetUrlInfoMultiple");

        CMUrlInfos instance = new CMUrlInfos();
        CMUrlInfo urlInfo1 = new CMUrlInfo();
        urlInfo1.setHcid("1.1");
        urlInfo1.setUrl("http://service1.com");
        CMUrlInfo urlInfo2 = new CMUrlInfo();
        urlInfo2.setHcid("2.2");
        urlInfo2.setUrl("http://service2.com");
        CMUrlInfo urlInfo3 = new CMUrlInfo();
        urlInfo3.setHcid("3.3");
        urlInfo3.setUrl("http://service3.com");
        instance.getUrlInfo().add(urlInfo1);
        instance.getUrlInfo().add(urlInfo2);
        instance.getUrlInfo().add(urlInfo3);

        List<CMUrlInfo> result = instance.getUrlInfo();

        assertEquals("1.1", result.get(0).getHcid());
        assertEquals("http://service1.com", result.get(0).getUrl());
        assertEquals("2.2", result.get(1).getHcid());
        assertEquals("http://service2.com", result.get(1).getUrl());
        assertEquals("3.3", result.get(2).getHcid());
        assertEquals("http://service3.com", result.get(2).getUrl());
    }

}