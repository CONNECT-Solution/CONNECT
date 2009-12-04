/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.properties;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

import java.util.Properties;
import java.util.Set;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author westbergl
 */
@Ignore //Move this test to Integration test suit
public class PropertyAccessorTest
{
    private static String m_sPropertiesDir = "";
    
    private static final String CACHE_REFRESH_DURATION_PROPNAME = "CacheRefreshDuration";
    private static final int iCACHE_REFRESH_DURATION = 10000;  // 10 SECONDS
    private static final String CACHE_REFRESH_DURATION = iCACHE_REFRESH_DURATION + "";  // 10 SECONDS
    
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
    

    public PropertyAccessorTest()
    {
    }
    
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
        // delete our test property files...
        //----------------------------------
        File fPropsNever = new File(m_sPropertiesDir + PROPERTY_FILENAME_NEVER + ".properties");
        fPropsNever.delete();
        
        File fPropsAlways = new File(m_sPropertiesDir + PROPERTY_FILENAME_ALWAYS + ".properties");
        fPropsAlways.delete();

        File fPropsPeriodic = new File(m_sPropertiesDir + PROPERTY_FILENAME_PERIODIC + ".properties");
        fPropsPeriodic.delete();

        File fPropsPeriodic2 = new File(m_sPropertiesDir + PROPERTY_FILENAME_PERIODIC_2 + ".properties");
        fPropsPeriodic2.delete();
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

    /**
     * Test of getPropertyNames method, of class PropertyAccessor.
     */
    @Test
    public void testGetPropertyNames() throws Exception
    {
        System.out.println("getPropertyNames");
        
        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        Set<String> setKeys = PropertyAccessor.getPropertyNames(sPropertyFile);
        assertNotNull(setKeys);
        
        boolean bFoundProp[] = {false, false, false, false, false, false};
        Iterator<String> iterKeys = setKeys.iterator();
        while (iterKeys.hasNext())
        {
            String sKey = iterKeys.next();
            if (sKey.equals(PROPERTY_NAME_1))
            {
                bFoundProp[0] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_2))
            {
                bFoundProp[1] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_3))
            {
                bFoundProp[2] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_4))
            {
                bFoundProp[3] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_5))
            {
                bFoundProp[4] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_6))
            {
                bFoundProp[5] = true;
            }
            else
            {
                fail("Found property that was not supposed to be in the file: " + sKey);
            }
        }   // while (iterKeys.hasNext())
        
        assertTrue(bFoundProp[0]);
        assertTrue(bFoundProp[1]);
        assertTrue(bFoundProp[2]);
        assertTrue(bFoundProp[3]);
        assertTrue(bFoundProp[4]);
        assertTrue(bFoundProp[5]);
    }

    /**
     * Test of getProperties method, of class PropertyAccessor.
     */
    @Test
    public void testGetProperties() throws Exception
    {
        System.out.println("getProperties");

        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        Properties oProps = PropertyAccessor.getProperties(sPropertyFile);
        assertNotNull(oProps);
        
        Set<String> setKeys = oProps.stringPropertyNames();
        
        assertNotNull(setKeys);
        
        boolean bFoundProp[] = {false, false, false, false, false, false};
        Iterator<String> iterKeys = setKeys.iterator();
        while (iterKeys.hasNext())
        {
            String sKey = iterKeys.next();
            if (sKey.equals(PROPERTY_NAME_1))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_1.trim(), sValue);
                bFoundProp[0] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_2))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_2.trim(), sValue);
                bFoundProp[1] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_3))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_3.trim(), sValue);
                bFoundProp[2] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_4))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_4.trim(), sValue);
                bFoundProp[3] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_5))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_5.trim(), sValue);
                bFoundProp[4] = true;
            }
            else if (sKey.equals(PROPERTY_NAME_6))
            {
                String sValue = oProps.getProperty(sKey);
                assertEquals(PROPERTY_VALUE_6.trim(), sValue);
                bFoundProp[5] = true;
            }
            else
            {
                fail("Found property that was not supposed to be in the file: " + sKey);
            }
        }   // while (iterKeys.hasNext())
        
        assertTrue(bFoundProp[0]);
        assertTrue(bFoundProp[1]);
        assertTrue(bFoundProp[2]);
        assertTrue(bFoundProp[3]);
        assertTrue(bFoundProp[4]);
        assertTrue(bFoundProp[5]);
    }

    /**
     * Test of getRefreshDuration method, of class PropertyAccessor.
     */
    @Test
    public void testGetRefreshDuration() throws Exception
    {
        System.out.println("getRefreshDuration");

        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        int iDuration = PropertyAccessor.getRefreshDuration(sPropertyFile);
        assertEquals(-1, iDuration);

        sPropertyFile = PROPERTY_FILENAME_ALWAYS;
        iDuration = PropertyAccessor.getRefreshDuration(sPropertyFile);
        assertEquals(0, iDuration);

        sPropertyFile = PROPERTY_FILENAME_PERIODIC;
        iDuration = PropertyAccessor.getRefreshDuration(sPropertyFile);
        assertEquals(CACHE_REFRESH_DURATION, Integer.toString(iDuration));
    }

    /**
     * Test of durationBeforeNextRefresh method, of class PropertyAccessor.
     */
    @Test
    public void testGetDurationBeforeNextRefresh() throws Exception
    {
        System.out.println("durationBeforeNextRefresh");
        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        int iDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        assertEquals(-1, iDuration);

        sPropertyFile = PROPERTY_FILENAME_ALWAYS;
        iDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        assertEquals(0, iDuration);

        sPropertyFile = PROPERTY_FILENAME_PERIODIC;
        iDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        assertTrue((iDuration >= 0) && (iDuration <= iCACHE_REFRESH_DURATION));
    }

    /**
     * Test of dumpPropsToLog method, of class PropertyAccessor.
     */
    @Test
    public void testDumpPropsToLog() throws Exception
    {
        System.out.println("dumpPropsToLog");
        String sPropertyFile = PROPERTY_FILENAME_NEVER;
        PropertyAccessor.dumpPropsToLog(sPropertyFile);
        
        // Only real way to verify this is to look at the log/output.
        //------------------------------------------------------------
    }

    /**
     * Test of setProperty method, of class PropertyAccessor.
     */
    @Test
    public void testSetProperty() throws Exception
    {
        System.out.println("setProperty");
        String sPropertyFile = PROPERTY_FILENAME_PERIODIC_2;
        String sPropertyName = PROPERTY_NAME_1;
        String sValue = "ANewValue";
        PropertyAccessor.setProperty(sPropertyFile, sPropertyName, sValue);
        
        // See what happens to this...
        //----------------------------
        String sRetValue = PropertyAccessor.getProperty(sPropertyFile, sPropertyName);
        assertEquals(sValue, sRetValue);
        
        // See what happened to the refresh info...
        //------------------------------------------
        int iDuration = PropertyAccessor.getRefreshDuration(sPropertyFile);
        assertEquals(-1, iDuration);
        
        iDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        assertEquals(-1, iDuration);
    }

    /**
     * Test of forceRefresh method, of class PropertyAccessor.
     */
    @Test
    public void testForceRefresh() throws Exception
    {
        System.out.println("forceRefresh");
        String sPropertyFile = PROPERTY_FILENAME_PERIODIC;
        PropertyAccessor.forceRefresh(sPropertyFile);
        
        // let's sleep for 3 seconds.
        //---------------------------
        try 
        {
            Thread.sleep(3000);
        } 
        catch (InterruptedException e) 
        {
        }

        int iFirstDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        
        PropertyAccessor.forceRefresh(sPropertyFile);

        int iSecondDuration = PropertyAccessor.getDurationBeforeNextRefresh(sPropertyFile);
        
        // since we had a delay after the first one and no delay after the second one,
        // the second one should be larger than the first.
        //-----------------------------------------------------------------------------
        assertTrue(iSecondDuration > iFirstDuration);
        
    }
    
    /**
     * This method will read in the property file, change the given property and store it back out.
     * 
     * @param sPropertyFile The name of the property file.
     * @param sPropertyName The name of the property to be changed.
     * @param sPropertyValue The value of the property to be changed.
     * @throws java.lang.Exception Any exception....
     */
    private void changePropertyAndStore(String sPropertyFile, String sPropertyName, String sPropertyValue)
        throws Exception
    {
        FileReader frPropFile = null;
        FileWriter fwPropFile = null;

        try
        {
            frPropFile = new FileReader(m_sPropertiesDir + sPropertyFile + ".properties");
            Properties oProps = new Properties();
            oProps.load(frPropFile);
            frPropFile.close();
            frPropFile = null;
            
            oProps.setProperty(sPropertyName, sPropertyValue);
            fwPropFile = new FileWriter(m_sPropertiesDir + sPropertyFile + ".properties");
            oProps.store(fwPropFile, "");
            fwPropFile.close();
            frPropFile = null;
        }
        finally
        {
            if (frPropFile != null)
            {
                frPropFile.close();
            }
            
            if (fwPropFile != null)
            {
                fwPropFile.close();
            }
        }
    }
    
 //  NOTE: THIS IS COMMENTED OUT FOR THE BUILD SERVER BECAUSE IT IS EXTREMELY TIME
 //  SENSITIVE - TESTING REFRESHING OF CACHE, ETC.  SO IT SHOULD BE UNCOMMENTED WHEN YOU
 //  WANT TO DO THIS LEVEL OF RUN.
    /**
     * This method is used to test the caching and refreshing capabilities.
     * To make sure that it is all working correctly...  It is pretty complex.
     * It will involve changing the property files/waiting to see when they
     * refresh, etc.
     */
//    @Test
//    public void testCachingAndRefreshing() throws Exception
//    {
//        // first let's test against the one that should never refresh...
//        //---------------------------------------------------------------
//        String sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_NEVER, PROPERTY_NAME_1);
//        assertEquals(PROPERTY_VALUE_1, sValue);
//        
//        // Change the value and see if we can force a refresh - it should not change...
//        //------------------------------------------------------------------------------
//        changePropertyAndStore(PROPERTY_FILENAME_NEVER, PROPERTY_NAME_1, "ModifiedValue");
//        PropertyAccessor.forceRefresh(PROPERTY_FILENAME_NEVER);
//        
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_NEVER, PROPERTY_NAME_1);
//        assertEquals(PROPERTY_VALUE_1, sValue);
//                
//        // Now let's test against the one that should always refresh
//        //----------------------------------------------------------
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_ALWAYS, PROPERTY_NAME_2);
//        assertEquals(PROPERTY_VALUE_2, sValue);
//        
//        changePropertyAndStore(PROPERTY_FILENAME_ALWAYS, PROPERTY_NAME_2, "ModVal2");
//        
//        // Note it should auto refresh.
//        //------------------------------
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_ALWAYS, PROPERTY_NAME_2);
//        assertEquals("ModVal2", sValue);
//        
//        // Now let's test against the one that refreshes every 10 seconds...
//        //------------------------------------------------------------------
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_PERIODIC, PROPERTY_NAME_3);
//        assertEquals(PROPERTY_VALUE_3, sValue);
//        
//        // Get a clean time on the clock...
//        //----------------------------------
//        PropertyAccessor.forceRefresh(PROPERTY_FILENAME_PERIODIC);
//        
//        changePropertyAndStore(PROPERTY_FILENAME_PERIODIC, PROPERTY_NAME_3, "ModVal3");
//
//        // We should not have refreshed yet - so should have the old value.
//        // (Unless our disc is really really slow....
//        //-----------------------------------------------------------------
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_PERIODIC, PROPERTY_NAME_3);
//        assertEquals(PROPERTY_VALUE_3, sValue);
//        
//        // Wait the cache duration time
//        //-----------------------------
//        try 
//        {
//            Thread.sleep(iCACHE_REFRESH_DURATION);
//        } 
//        catch (InterruptedException e) 
//        {
//        }
//
//        // Now we should have the new value.
//        //---------------------------------
//        sValue = PropertyAccessor.getProperty(PROPERTY_FILENAME_PERIODIC, PROPERTY_NAME_3);
//        assertEquals("ModVal3", sValue);
//        
//    }    
    
}