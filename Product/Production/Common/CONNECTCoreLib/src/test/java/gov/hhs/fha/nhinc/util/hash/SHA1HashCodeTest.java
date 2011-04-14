/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.util.hash;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
public class SHA1HashCodeTest
{

    public SHA1HashCodeTest()
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
     * Test of caluclateSHA1 method, of class SHA1HashCode.
     */
    @Test
    public void testCalculateSHA1() throws Exception
    {
        System.out.println("caluclateSHA1");
        String sText = "<text></text>";
        String sResult = SHA1HashCode.calculateSHA1(sText);
        assertEquals("941ce1af75a6a3c1d3b614188b70cd7e20da42fd", sResult);
        
        // Test on an empty string.
        //--------------------------
        sText = "";
        sResult = SHA1HashCode.calculateSHA1(sText);
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", sResult);
        
        // Test on null.
        //--------------
        sText = null;
        sResult = SHA1HashCode.calculateSHA1(sText);
        assertEquals("", sResult);

    }
}