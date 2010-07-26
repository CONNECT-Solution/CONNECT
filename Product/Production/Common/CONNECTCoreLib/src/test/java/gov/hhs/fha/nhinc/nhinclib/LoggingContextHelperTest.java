/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinclib;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.log4j.NDC;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 * This class is used to test the LoggingContextHelper class
 */
public class LoggingContextHelperTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    final Log mockLog = context.mock(Log.class);

    public LoggingContextHelperTest() {
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

    @Test
    public void testCreateLogger() {
        try {
            LoggingContextHelper loggingContextHelper = new LoggingContextHelper() {

                @Override
                protected Log createLogger() {
                    return mockLog;
                }
            };
            Log log = loggingContextHelper.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testLoggingContext() {
        LoggingContextHelper loggingContextHelper = new LoggingContextHelper() {

            @Override
            protected String generateLoggingContextId(WebServiceContext webServiceContext) {
                return "TestUUID";
            }
        };

        try {
            // Passing in null WebServiceContext is ok, as value set is overridden above
            loggingContextHelper.setContext(null);
            // Access to the same nested diagnostic context as in the  same thread
            assertEquals("Nested diagnostic context is not properly constructed. ", 1, NDC.getDepth());
            assertEquals("Context contents are not successfully generated. ", "TestUUID", NDC.peek());
        } finally {
            loggingContextHelper.clearContext();
            assertEquals("Nested diagnostic context is not properly cleared. ", 0, NDC.getDepth());
        }
    }
}
