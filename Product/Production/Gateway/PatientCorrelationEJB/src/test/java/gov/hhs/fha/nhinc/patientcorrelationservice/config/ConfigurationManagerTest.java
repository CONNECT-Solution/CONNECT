/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelationservice.config;

import java.io.File;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class ConfigurationManagerTest {
    private Mockery context;
    
    public ConfigurationManagerTest() {
    }
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
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
        
        final Log mockLogger = context.mock(Log.class);
        ConfigurationManager manager = new ConfigurationManager() {

        @Override
        protected Log createLogger() {
            return mockLogger;
        }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        ExpirationConfiguration expResult = null;
        ExpirationConfiguration result = manager.loadExpirationConfiguration("samplePCConfiguration.xml");

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
        final Log mockLogger = context.mock(Log.class);
        
        ConfigurationManager manager = new ConfigurationManager() {

        @Override
        protected Log createLogger() {
            return mockLogger;
        }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Error");
                will(returnValue(null));
            }
        });

        ExpirationConfiguration result = manager.loadExpirationConfiguration("samplePCConfiguration2.xml");

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
         final Log mockLogger = context.mock(Log.class);
        ConfigurationManager manager = new ConfigurationManager() {

        @Override
        protected Log createLogger() {
            return mockLogger;
        }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        Expiration result = manager.loadConfiguration(null, "");

        assertNull(result);

    }




}