package gov.hhs.fha.nhinc.common.propaccessor;

import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationRequestType;
import gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationResponseType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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
@Ignore // TODO: Move this to an integration test
public class PropertyAccessHelperTest
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
    private static final String PROPERTY_VALUE_1 = "Value1";
    private static final String PROPERTY_NAME_2 = "Property2";
    private static final String PROPERTY_VALUE_2 = "Value2";
    private static final String PROPERTY_NAME_3 = "Property3";
    private static final String PROPERTY_VALUE_3 = "Value3";
    private static final String PROPERTY_NAME_4 = "Property4";
    private static final String PROPERTY_VALUE_4 = "T";
    private static final String PROPERTY_NAME_5 = "Property5";
    private static final String PROPERTY_VALUE_5 = "t";
    private static final String PROPERTY_NAME_6 = "Property6";
    private static final String PROPERTY_VALUE_6 = "TrUe";
    
    public PropertyAccessHelperTest()
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
        oPropsAlways.setProperty(CACHE_REFRESH_DURATION_PROPNAME, "0");
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
     * Test of getProperty method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetProperty() throws Exception
    {
        System.out.println("getProperty");
        GetPropertyRequestType oInput = new GetPropertyRequestType();
        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        oInput.setPropertyName(PROPERTY_NAME_1);
        GetPropertyResponseType oOutput = PropertyAccessHelper.getProperty(oInput);
        assertNotNull(oOutput);
        assertEquals(PROPERTY_VALUE_1, oOutput.getPropertyValue());
    }

    /**
     * Test of getPropertyBoolean method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetPropertyBoolean() throws Exception
    {
        System.out.println("getPropertyBoolean");
        GetPropertyBooleanRequestType oInput = new GetPropertyBooleanRequestType();
        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);

        // Test false one first.
        //----------------------
        oInput.setPropertyName(PROPERTY_NAME_3);
        GetPropertyBooleanResponseType oOutput = PropertyAccessHelper.getPropertyBoolean(oInput);
        assertNotNull(oOutput);
        assertEquals(false, oOutput.isPropertyValue());

        // Test "T"
        //---------
        oInput.setPropertyName(PROPERTY_NAME_4);
        oOutput = PropertyAccessHelper.getPropertyBoolean(oInput);
        assertNotNull(oOutput);
        assertEquals(true, oOutput.isPropertyValue());

        // Test "t"
        //---------
        oInput.setPropertyName(PROPERTY_NAME_5);
        oOutput = PropertyAccessHelper.getPropertyBoolean(oInput);
        assertNotNull(oOutput);
        assertEquals(true, oOutput.isPropertyValue());
        
        // Test "TrUe"
        //-------------
        oInput.setPropertyName(PROPERTY_NAME_6);
        oOutput = PropertyAccessHelper.getPropertyBoolean(oInput);
        assertNotNull(oOutput);
        assertEquals(true, oOutput.isPropertyValue());
    }

    /**
     * Test of getPropertyNames method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetPropertyNames() throws Exception
    {
        System.out.println("getPropertyNames");
        GetPropertyNamesRequestType oInput = new GetPropertyNamesRequestType();
        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        GetPropertyNamesResponseType oOutput = PropertyAccessHelper.getPropertyNames(oInput);
        assertNotNull(oOutput);
        
        List<String> olKeys = oOutput.getPropertyName();
        assertNotNull(olKeys);
        
        boolean bFoundProp[] = {false, false, false, false, false, false};
        Iterator<String> iterKeys = olKeys.iterator();
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
     * Test of getProperties method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetProperties() throws Exception
    {
        System.out.println("getProperties");
        GetPropertiesRequestType oInput = new GetPropertiesRequestType();
        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        GetPropertiesResponseType oOutput = PropertyAccessHelper.getProperties(oInput);
        assertNotNull(oOutput);
        assertNotNull(oOutput.getProperties());
        assertNotNull(oOutput.getProperties().getProperty());
        
        List<PropertyType> olProps = oOutput.getProperties().getProperty();
        Iterator<PropertyType> iterProps = olProps.iterator();
        
        boolean bFoundProp[] = {false, false, false, false, false, false};

        while (iterProps.hasNext())
        {
            PropertyType oProp = iterProps.next();
            if (oProp.getPropertyName().equals(PROPERTY_NAME_1))
            {
                assertEquals(PROPERTY_VALUE_1, oProp.getPropertyValue());
                bFoundProp[0] = true;
            }
            else if (oProp.getPropertyName().equals(PROPERTY_NAME_2))
            {
                assertEquals(PROPERTY_VALUE_2, oProp.getPropertyValue());
                bFoundProp[1] = true;
            }
            else if (oProp.getPropertyName().equals(PROPERTY_NAME_3))
            {
                assertEquals(PROPERTY_VALUE_3, oProp.getPropertyValue());
                bFoundProp[2] = true;
            }
            else if (oProp.getPropertyName().equals(PROPERTY_NAME_4))
            {
                assertEquals(PROPERTY_VALUE_4, oProp.getPropertyValue());
                bFoundProp[3] = true;
            }
            else if (oProp.getPropertyName().equals(PROPERTY_NAME_5))
            {
                assertEquals(PROPERTY_VALUE_5, oProp.getPropertyValue());
                bFoundProp[4] = true;
            }
            else if (oProp.getPropertyName().equals(PROPERTY_NAME_6))
            {
                assertEquals(PROPERTY_VALUE_6, oProp.getPropertyValue());
                bFoundProp[5] = true;
            }
            else
            {
                fail("Found property that was not supposed to be in the file: " + oProp.getPropertyName());
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
     * Test of getRefreshDuration method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetRefreshDuration() throws Exception
    {
        System.out.println("getRefreshDuration");

        GetRefreshDurationRequestType oInput = new GetRefreshDurationRequestType();
        GetRefreshDurationResponseType oOutput = null;

        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        oOutput = PropertyAccessHelper.getRefreshDuration(oInput);
        assertNotNull(oOutput);
        assertEquals(-1, oOutput.getDurationMillis());

        oInput.setPropertyFile(PROPERTY_FILENAME_ALWAYS);
        oOutput = PropertyAccessHelper.getRefreshDuration(oInput);
        assertNotNull(oOutput);
        assertEquals(0, oOutput.getDurationMillis());

        oInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
        oOutput = PropertyAccessHelper.getRefreshDuration(oInput);
        assertEquals(iCACHE_REFRESH_DURATION, oOutput.getDurationMillis());
    }

    /**
     * Test of getDurationBeforeNextRefresh method, of class PropertyAccessHelper.
     */
    @Test
    public void testGetDurationBeforeNextRefresh() throws Exception
    {
        System.out.println("getDurationBeforeNextRefresh");
        
        GetDurationBeforeNextRefreshRequestType oInput = new GetDurationBeforeNextRefreshRequestType();
        GetDurationBeforeNextRefreshResponseType oOutput = null;

        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        oOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(oInput);
        assertNotNull(oOutput);
        assertEquals(-1, oOutput.getDurationMillis());

        oInput.setPropertyFile(PROPERTY_FILENAME_ALWAYS);
        oOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(oInput);
        assertNotNull(oOutput);
        assertEquals(0, oOutput.getDurationMillis());

        oInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
        oOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(oInput);
        assertNotNull(oOutput);
        assertTrue((oOutput.getDurationMillis() >= 0) && (oOutput.getDurationMillis() <= iCACHE_REFRESH_DURATION));
    }

    /**
     * Test of dumpPropsToLog method, of class PropertyAccessHelper.
     */
    @Test
    public void testDumpPropsToLog() throws Exception
    {
        System.out.println("dumpPropsToLog");
        DumpPropsToLogRequestType oInput = new DumpPropsToLogRequestType();
        DumpPropsToLogResponseType oOutput = null;
    
        oInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
        oOutput = PropertyAccessHelper.dumpPropsToLog(oInput);
        
        // Only real way to verify this is to look at the log/output.
        //------------------------------------------------------------
    }

    /**
     * Test of forceRefresh method, of class PropertyAccessHelper.
     */
    @Test
    public void testForceRefresh() throws Exception
    {
        System.out.println("forceRefresh");
        ForceRefreshRequestType oInput = new ForceRefreshRequestType();
        ForceRefreshResponseType oOutput = null;
        
        oInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
        oOutput = PropertyAccessHelper.forceRefresh(oInput);
        
        // let's sleep for 3 seconds.
        //---------------------------
        try 
        {
            Thread.sleep(3000);
        } 
        catch (InterruptedException e) 
        {
        }

        GetDurationBeforeNextRefreshRequestType oDurInput = new GetDurationBeforeNextRefreshRequestType();
        oDurInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
        GetDurationBeforeNextRefreshResponseType oFirstDurOutput = null;
        oFirstDurOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(oDurInput);
        assertNotNull(oFirstDurOutput);
        
        oOutput = PropertyAccessHelper.forceRefresh(oInput);

        GetDurationBeforeNextRefreshResponseType oSecondDurOutput = null;
        oSecondDurOutput = PropertyAccessHelper.getDurationBeforeNextRefresh(oDurInput);
        assertNotNull(oSecondDurOutput);
        
        // since we had a delay after the first one and no delay after the second one,
        // the second one should be larger than the first.
        //-----------------------------------------------------------------------------
        assertTrue(oSecondDurOutput.getDurationMillis() > oFirstDurOutput.getDurationMillis());
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
//        GetPropertyRequestType oPropInput = new GetPropertyRequestType();
//        oPropInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
//        oPropInput.setPropertyName(PROPERTY_NAME_1);
//        GetPropertyResponseType oPropOutput = null;
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals(PROPERTY_VALUE_1, oPropOutput.getPropertyValue());
//        
//        // Change the value and see if we can force a refresh - it should not change...
//        //------------------------------------------------------------------------------
//        changePropertyAndStore(PROPERTY_FILENAME_NEVER, PROPERTY_NAME_1, "ModifiedValue");
//        ForceRefreshRequestType oRefInput = new ForceRefreshRequestType();
//        oRefInput.setPropertyFile(PROPERTY_FILENAME_NEVER);
//        PropertyAccessHelper.forceRefresh(oRefInput);
//        
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals(PROPERTY_VALUE_1, oPropOutput.getPropertyValue());
//                
//        // Now let's test against the one that should always refresh
//        //----------------------------------------------------------
//        oPropInput.setPropertyFile(PROPERTY_FILENAME_ALWAYS);
//        oPropInput.setPropertyName(PROPERTY_NAME_2);
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals(PROPERTY_VALUE_2, oPropOutput.getPropertyValue());
//        
//        changePropertyAndStore(PROPERTY_FILENAME_ALWAYS, PROPERTY_NAME_2, "ModVal2");
//        
//        // Note it should auto refresh.
//        //------------------------------
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals("ModVal2", oPropOutput.getPropertyValue());
//        
//        // Now let's test against the one that refreshes every 10 seconds...
//        //------------------------------------------------------------------
//        oPropInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
//        oPropInput.setPropertyName(PROPERTY_NAME_3);
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals(PROPERTY_VALUE_3, oPropOutput.getPropertyValue());
//        
//        // Get a clean time on the clock...
//        //----------------------------------
//        oRefInput.setPropertyFile(PROPERTY_FILENAME_PERIODIC);
//        PropertyAccessHelper.forceRefresh(oRefInput);
//        
//        changePropertyAndStore(PROPERTY_FILENAME_PERIODIC, PROPERTY_NAME_3, "ModVal3");
//
//        // We should not have refreshed yet - so should have the old value.
//        // (Unless our disc is really really slow....
//        //-----------------------------------------------------------------
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals(PROPERTY_VALUE_3, oPropOutput.getPropertyValue());
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
//        oPropOutput = PropertyAccessHelper.getProperty(oPropInput);
//        assertNotNull(oPropOutput);
//        assertEquals("ModVal3", oPropOutput.getPropertyValue());
//        
//    }    

}