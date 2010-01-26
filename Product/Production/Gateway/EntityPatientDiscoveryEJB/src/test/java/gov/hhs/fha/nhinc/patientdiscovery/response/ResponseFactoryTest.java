/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;

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
public class ResponseFactoryTest {
    private Mockery context;
    
    public ResponseFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new Mockery() {

            {
                //setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getResponseMode method, of class ResponseFactory.
     */
    @Test
    public void testGetResponseMode_Verify() {
        System.out.println("testGetResponseMode_Verify");
        ResponseFactory instance = getFactory("verify");
        
        ResponseMode result = instance.getResponseMode();
        assertTrue(result instanceof VerifyMode);
        assertFalse(result instanceof TrustMode);
        assertFalse(result instanceof PassThruMode);
        
    }
    @Test
    public void testGetResponseMode_Default() {
        System.out.println("testGetResponseMode_Default");
        ResponseFactory instance = getFactory("");

        ResponseMode result = instance.getResponseMode();
        assertTrue(result instanceof VerifyMode);
        assertFalse(result instanceof TrustMode);
        assertFalse(result instanceof PassThruMode);

    }
    @Test
    public void testGetResponseMode_NoSetting() {
        System.out.println("testGetResponseMode_NoSetting");
        ResponseFactory instance = getFactory(null);

        ResponseMode result = instance.getResponseMode();
        assertTrue(result instanceof VerifyMode);
        assertFalse(result instanceof TrustMode);
        assertFalse(result instanceof PassThruMode);

    }
    @Test
    public void testGetResponseMode_TrustMode() {
        System.out.println("testGetResponseMode_TrustMode");
        ResponseFactory instance = getFactory("trust");

        ResponseMode result = instance.getResponseMode();
        assertFalse(result instanceof VerifyMode);
        assertTrue(result instanceof TrustMode);
        assertFalse(result instanceof PassThruMode);

    }
    @Test
    public void testGetResponseMode_PassThruMode() {
        System.out.println("testGetResponseMode_PassThruMode");
        ResponseFactory instance = getFactory("passthrough");

        ResponseMode result = instance.getResponseMode();
        assertFalse(result instanceof VerifyMode);
        assertFalse(result instanceof TrustMode);
        assertTrue(result instanceof PassThruMode);

    }

    private ResponseFactory getFactory(String mode)
    {
        final Log mockLogger = context.mock(Log.class);
        final String finalMode = mode;
        
        ResponseFactory result = new ResponseFactory() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected String getModeProperty()
            {
                return finalMode;
            }
        };

        return result;
    }
}