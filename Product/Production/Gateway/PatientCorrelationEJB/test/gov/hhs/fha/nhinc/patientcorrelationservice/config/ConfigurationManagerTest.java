/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.config;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class ConfigurationManagerTest {

    public ConfigurationManagerTest() {
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
     * Test of loadExpirationConfiguration method, of class ConfigurationManager.
     */
    @Test
    public void testLoadSampleExpirationConfiguration() {
        System.out.println("testLoadSampleExpirationConfiguration");
        ExpirationConfiguration expResult = null;
        ExpirationConfiguration result = ConfigurationManager.loadExpirationConfiguration("samplePCConfiguration.xml");

        assertNotNull(result);
        assertEquals(30, result.getDefaultDuration());
        assertEquals("DAY", result.getDefaultUnits());
        assertEquals(2, result.getExpirations().size());
        assertEquals("1.1", result.getExpirations().get(0).getAssigningAuthority());
        assertEquals(30, result.getExpirations().get(0).getDuration());
        assertEquals("DAY",result.getExpirations().get(0).getUnits());
    }
    @Test
    public void testLoadSampleExpirationConfiguration_NoDefaults() {
        System.out.println("testLoadSampleExpirationConfiguration2");
        ExpirationConfiguration expResult = null;
        ExpirationConfiguration result = ConfigurationManager.loadExpirationConfiguration("samplePCConfiguration2.xml");

        assertNotNull(result);
        assertEquals(-1, result.getDefaultDuration());
        assertEquals("", result.getDefaultUnits());
        assertEquals(2, result.getExpirations().size());
        assertEquals("1.1", result.getExpirations().get(0).getAssigningAuthority());
        assertEquals(30, result.getExpirations().get(0).getDuration());
        assertEquals("DAY",result.getExpirations().get(0).getUnits());
    }
    @Test
    public void testloadConfiguration_Null()
    {
        System.out.println("testloadConfiguration_Null");        
        Expiration result = ConfigurationManager.loadConfiguration(null, "");

        assertNull(result);

    }
    @Test
    public void testloadConfiguration_NoAA()
    {
        System.out.println("testloadConfiguration_NoAA");
        ExpirationConfiguration config = ConfigurationManager.loadExpirationConfiguration("samplePCConfiguration.xml");
        Expiration result = ConfigurationManager.loadConfiguration(config, "3.3");

        assertNotNull(result);
        assertEquals(config.getDefaultDuration(), result.getDuration());
        assertEquals(config.getDefaultUnits(), result.getUnits());
        assertEquals("", result.getAssigningAuthority());

    }
    @Test
    public void testloadConfiguration()
    {
        System.out.println("testloadConfiguration_NoAA");
        ExpirationConfiguration config = ConfigurationManager.loadExpirationConfiguration("samplePCConfiguration.xml");
        Expiration result = ConfigurationManager.loadConfiguration(config, "1.1");

        assertNotNull(result);
        assertEquals(30, result.getDuration());
        assertEquals("DAY", result.getUnits());
        assertEquals("1.1", result.getAssigningAuthority());

    }
    @Test
    public void testloadConfiguration_Real()
    {
        System.out.println("testloadConfiguration_Real");
        ExpirationConfiguration config = ConfigurationManager.loadExpirationConfiguration();
        Expiration result = ConfigurationManager.loadConfiguration(config, "1.1");

        assertNotNull(result);

    }
}