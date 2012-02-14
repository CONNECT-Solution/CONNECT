/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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
public class CMUrlInfoTest {

    public CMUrlInfoTest() {
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
     * Test of clear method, of class CMUrlInfo. This tests the clear method when there is data already in the field.
     */
    @Test
    public void testClearWithData() {
        System.out.println("testClearWithData");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        assertEquals("1.1", instance.getHcid());
        assertEquals("http://service.com", instance.getUrl());

        instance.clear();

        assertEquals("", instance.getHcid());
        assertEquals("", instance.getUrl());
    }

    /**
     * Test of clear method, of class CMUrlInfo. This tests the clear method when there is no data already in the field.
     */
    @Test
    public void testClearWithNoData() {
        System.out.println("testClearWithNoData");

        CMUrlInfo instance = new CMUrlInfo();

        instance.clear();

        assertEquals("", instance.getHcid());
        assertEquals("", instance.getUrl());
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two empty objects are equal
     */
    @Test
    public void testEqualsEmptyObjects() {
        System.out.println("testEqualsEmptyObjects");

        CMUrlInfo oCompare = new CMUrlInfo();
        CMUrlInfo instance = new CMUrlInfo();

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);

    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two empty objects are not equal if one is empty
     */
    @Test
    public void testNotEqualsOneEmpty() {
        System.out.println("testNotEqualsOneEmpty");

        CMUrlInfo oCompare = new CMUrlInfo();
        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are equal if both have the same values
     */
    @Test
    public void testEquals() {
        System.out.println("testEquals");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("1.1");
        oCompare.setUrl("http://service.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are equal if both have the same values, but url
     * is mixed case
     */
    @Test
    public void testEqualsMixedCase() {
        System.out.println("testEquals");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("1.1");
        oCompare.setUrl("http://service.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://SERVICE.com");

        boolean result = instance.equals(oCompare);

        assertEquals(true, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are not equal if both have differnt url and hcid
     * values
     */
    @Test
    public void testNotEquals() {
        System.out.println("testNotEquals");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("2.2");
        oCompare.setUrl("http://service4.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are not equal if both have different url and
     * same hcid values
     */
    @Test
    public void testNotEqualsUrl() {
        System.out.println("testNotEqualsUrl");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("1.1");
        oCompare.setUrl("http://service4.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are not equal if both have same url and
     * different hcid values
     */
    @Test
    public void testNotEqualsHcid() {
        System.out.println("testNotEqualsHcid");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("2.2");
        oCompare.setUrl("http://service.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are not equal if both have null url and same
     * hcid values
     */
    @Test
    public void testNotEqualsNullUrl() {
        System.out.println("testNotEqualsNullUrl");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("1.1");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid("1.1");
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of equals method, of class CMUrlInfo. Test that two objects are not equal if both have same url and null
     * hcid values
     */
    @Test
    public void testNotEqualsNullHcid() {
        System.out.println("testNotEqualsNullHcid");

        CMUrlInfo oCompare = new CMUrlInfo();
        oCompare.setHcid("2.2");
        oCompare.setUrl("http://service.com");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setUrl("http://service.com");

        boolean result = instance.equals(oCompare);

        assertEquals(false, result);
    }

    /**
     * Test of getUrl method, of class CMUrlInfo. Test empty case for Set/Get
     */
    @Test
    public void testSetGetUrlEmpty() {
        System.out.println("testSetGetUrlEmpty");

        CMUrlInfo instance = new CMUrlInfo();

        String result = instance.getUrl();

        assertEquals("", result);
    }

    /**
     * Test of setUrl method, of class CMUrlInfo. Test case for Set/Get with a value (all upper)
     */
    @Test
    public void testSetGetUrlUpper() {
        System.out.println("testSetGetUrlUpper");

        String url = "HTTP://SERVICE.COM";
        CMUrlInfo instance = new CMUrlInfo();
        instance.setUrl(url);

        String result = instance.getUrl();

        assertEquals(url, result);
    }

    /**
     * Test of setUrl method, of class CMUrlInfo. Test case for Set/Get with a value (all lower)
     */
    @Test
    public void testSetGetUrlLower() {
        System.out.println("testSetGetUrlLower");

        String url = "http://service.com";
        CMUrlInfo instance = new CMUrlInfo();
        instance.setUrl(url);

        String result = instance.getUrl();

        assertEquals(url, result);
    }

    /**
     * Test of setUrl method, of class CMUrlInfo. Test case for Set/Get with a value (mixed case)
     */
    @Test
    public void testSetGetUrlMixed() {
        System.out.println("testSetGetUrlMixed");

        String url = "http://SeRvIcE.com";
        CMUrlInfo instance = new CMUrlInfo();
        instance.setUrl(url);

        String result = instance.getUrl();

        assertEquals(url, result);
    }

    /**
     * Test of setUrl method, of class CMUrlInfo. Test case for Set/Get with a null value
     */
    @Test
    public void testSetGetUrlNull() {
        System.out.println("testSetGetUrlMixed");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setUrl(null);

        String result = instance.getUrl();

        assertNull(result);
    }

    /**
     * Test of getHcid method, of class CMUrlInfo. Test empty case for Set/Get
     */
    @Test
    public void testSetGetHcidEmpty() {
        System.out.println("testSetGetHcidEmpty");

        CMUrlInfo instance = new CMUrlInfo();

        String result = instance.getHcid();

        assertEquals("", result);
    }

    /**
     * Test of setHcid method, of class CMUrlInfo. Test the setting and getting the HCID
     */
    @Test
    public void testSetGetHcid() {
        System.out.println("testSetGetHcid");

        String homeCommunityId = "1.1";

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid(homeCommunityId);

        String result = instance.getHcid();

        assertEquals(homeCommunityId, result);
    }

    /**
     * Test of setHcid method, of class CMUrlInfo. Test the setting and getting a null HCID
     */
    @Test
    public void testSetGetHcidNull() {
        System.out.println("testSetGetHcidNull");

        CMUrlInfo instance = new CMUrlInfo();
        instance.setHcid(null);

        String result = instance.getHcid();

        assertNull(result);
    }

}