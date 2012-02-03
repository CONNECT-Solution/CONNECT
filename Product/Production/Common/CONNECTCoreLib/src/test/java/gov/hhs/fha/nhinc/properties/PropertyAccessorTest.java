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
package gov.hhs.fha.nhinc.properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jun 1, 2010
 * Time: 2:57:51 PM
 */

public class PropertyAccessorTest {
    private static String m_sPropertiesDir = "";

    private static final String CACHE_REFRESH_DURATION_PROPNAME = "CacheRefreshDuration";
    private static final int iCACHE_REFRESH_DURATION = 10000;  // 10 SECONDS
    private static final String CACHE_REFRESH_DURATION = iCACHE_REFRESH_DURATION + "";  // 10 SECONDS

    private static String       tmpDir;
    private static String       nhinPropertiesDir;
    private static String       propertiesSubDir = "nhin";
    private static boolean      removeDirectory = false;


    private static final String PROPERTY_FILENAME_NEVER = "testnever";
    private static final String PROPERTY_FILENAME_ALWAYS = "testalways";
    private static final String PROPERTY_FILENAME_PERIODIC = "testperiodic";
    private static final String PROPERTY_FILENAME_PERIODIC_2 = "testperiodic2";       // Used to test Set capabilities.

    private static final String PROPERTY_NAME_1 = "Property1";
    private static final String PROPERTY_VALUE_1 = "Value1 ";
    private static final String PROPERTY_NAME_2 = "Property2";
    private static final String PROPERTY_VALUE_2 = "Value2 ";
    private static final String PROPERTY_NAME_3 = "Property3";
    private static final String PROPERTY_VALUE_3 = "Value3 ";
    private static final String PROPERTY_NAME_4 = "Property4";
    private static final String PROPERTY_VALUE_4 = "T ";
    private static final String PROPERTY_NAME_5 = "Property5";
    private static final String PROPERTY_VALUE_5 = "t ";
    private static final String PROPERTY_NAME_6 = "Property6";
    private static final String PROPERTY_VALUE_6 = "TrUe ";


   

    /**
     * This method is used to load up the property object
     * with the standard set of properties.
     *
     * @param oProps The property object to load up.
     */
    private static void loadProps(Properties oProps)
    {
        oProps.setProperty(PROPERTY_NAME_1, PROPERTY_VALUE_1);
        oProps.setProperty(PROPERTY_NAME_2, PROPERTY_VALUE_2);
        oProps.setProperty(PROPERTY_NAME_3, PROPERTY_VALUE_3);
        oProps.setProperty(PROPERTY_NAME_4, PROPERTY_VALUE_4);
        oProps.setProperty(PROPERTY_NAME_5, PROPERTY_VALUE_5);
        oProps.setProperty(PROPERTY_NAME_6, PROPERTY_VALUE_6);
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        File        nhincPropertiesFile;

        tmpDir = System.getProperty("user.dir", ".");
        nhinPropertiesDir = tmpDir + File.separator + propertiesSubDir;
        nhincPropertiesFile = new File(nhinPropertiesDir);

        if(nhincPropertiesFile.exists() == false) {
            nhincPropertiesFile.mkdirs();
            removeDirectory = true;
        }
        else {
            removeDirectory = false;
        }

        
      
        System.setProperty("nhinc.properties.dir", nhinPropertiesDir );
        m_sPropertiesDir = PropertyAccessor.getPropertyFileLocation();

        // Create some property files to be used for our testing...
        //---------------------------------------------------------

        // Never Refresh
        //---------------
        Properties oPropsNever = new Properties();
        loadProps(oPropsNever);

        Properties oPropsAlways = new Properties();
        oPropsAlways.setProperty(CACHE_REFRESH_DURATION_PROPNAME, "0 ");
        loadProps(oPropsAlways);

        Properties oPropsPeriodic = new Properties();
        oPropsPeriodic.setProperty(CACHE_REFRESH_DURATION_PROPNAME, CACHE_REFRESH_DURATION);
        loadProps(oPropsPeriodic);

        Properties oPropsPeriodic2 = new Properties();
        oPropsPeriodic2.setProperty(CACHE_REFRESH_DURATION_PROPNAME, CACHE_REFRESH_DURATION);
        loadProps(oPropsPeriodic2);

        FileWriter fwPropsNever = null;
        FileWriter fwPropsAlways = null;
        FileWriter fwPropsPeriodic = null;
        FileWriter fwPropsPeriodic2 = null;
        try
        {
            fwPropsNever = new FileWriter(m_sPropertiesDir + PROPERTY_FILENAME_NEVER + ".properties");
            oPropsNever.store(fwPropsNever, "");

            fwPropsAlways = new FileWriter(m_sPropertiesDir + PROPERTY_FILENAME_ALWAYS + ".properties");
            oPropsAlways.store(fwPropsAlways, "");

            fwPropsPeriodic = new FileWriter(m_sPropertiesDir + PROPERTY_FILENAME_PERIODIC + ".properties");
            oPropsPeriodic.store(fwPropsPeriodic, "");

            fwPropsPeriodic2 = new FileWriter(m_sPropertiesDir + PROPERTY_FILENAME_PERIODIC_2 + ".properties");
            oPropsPeriodic2.store(fwPropsPeriodic2, "");
        }
        finally
        {
            if (fwPropsNever != null)
            {
                fwPropsNever.close();
                fwPropsNever = null;
            }

            if (fwPropsAlways != null)
            {
                fwPropsAlways.close();
                fwPropsAlways = null;
            }

            if (fwPropsPeriodic != null)
            {
                fwPropsPeriodic.close();
                fwPropsPeriodic = null;
            }

            if (fwPropsPeriodic2 != null)
            {
                fwPropsPeriodic2.close();
                fwPropsPeriodic2 = null;
            }
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        if(removeDirectory) {
            new File(nhinPropertiesDir).delete();
        }
    }
    
    @Test
    public void testConstructor() {
    	PropertyAccessor pa = new PropertyAccessor(PROPERTY_FILENAME_NEVER);
    	assertNotNull(pa);
    }

    /**
     * Test to see if the environment variables were set.
     *
     * @throws PropertyAccessException
     */
    @Test
    public void  testCheckEnvVarSet() throws PropertyAccessException {

        PropertyAccessor.checkEnvVarSet();
    }

    /**
     * Test getting the original call.
     */
    @Test
    @Ignore
    public void  testGetPropertyFileLocation() {
        String      location;


        location = PropertyAccessor.getPropertyFileLocation();

        System.out.println("getPropertyFileLocation ");
        //
        // PropertyAccessor adds a trailing slash, so we need to acccount for that in the test.
        //
        assertEquals( nhinPropertiesDir + File.separator, location);
    }

    /**
     * Test getting the path.
     */
    @Test
    @Ignore
    public void  testGetPropertyFileURL() {
        String      location;

        System.out.println("getPropertyFileURL");

        location = PropertyAccessor.getPropertyFileURL();

        //
        // PropertyAccessor adds a trailing slash, so we need to account for that in the test.
        //
        assertEquals("file:///" + nhinPropertiesDir + File.separator, location);
    }

    /**
     * Test of getProperty method, of class PropertyAccessor.
     */
    @Test
    public void testGetProperty() throws Exception
    {
        System.out.println("getProperty");
        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        String sPropertyName = PROPERTY_NAME_1;
        String sValue = PropertyAccessor.getProperty(sPropertyFile, sPropertyName);
        assertEquals(PROPERTY_VALUE_1.trim(), sValue);
    }

    /**
     * Test of getPropertyBoolean method, of class PropertyAccessor.
     */
    @Test
    public void testGetPropertyBoolean() throws Exception
    {
        System.out.println("getPropertyBoolean");
        String sPropertyFile = PROPERTY_FILENAME_NEVER;

        // Test false one first.
        //----------------------
        String sPropertyName = PROPERTY_NAME_3;
        boolean bValue = PropertyAccessor.getPropertyBoolean(sPropertyFile, sPropertyName);
        assertEquals(false, bValue);

        // Test "T"
        //---------
        sPropertyName = PROPERTY_NAME_4;
        bValue = PropertyAccessor.getPropertyBoolean(sPropertyFile, sPropertyName);
        assertEquals(true, bValue);

        // Test "t"
        //---------
        sPropertyName = PROPERTY_NAME_5;
        bValue = PropertyAccessor.getPropertyBoolean(sPropertyFile, sPropertyName);
        assertEquals(true, bValue);

        // Test "TrUe"
        //-------------
        sPropertyName = PROPERTY_NAME_6;
        bValue = PropertyAccessor.getPropertyBoolean(sPropertyFile, sPropertyName);
        assertEquals(true, bValue);

    }


}
