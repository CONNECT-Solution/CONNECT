/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.MCCIMT000300UV01Receiver;

/**
 *
 * @author jhoppesc
 */
public class HL7ReceiverTransformsTest {
    
    private static Log log = LogFactory.getLog(HL7ReceiverTransformsTest.class);

    public HL7ReceiverTransformsTest() {
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
     * Test of testCreateMCCIMT000200UV01Receiver method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000200UV01Receiver() {
        log.info("testCreateMCCIMT000200UV01Receiver");

        String OID = "2.16.840.1.113883.3.198";
        MCCIMT000200UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000200UV01Receiver(OID);

        assertEquals(result.getTypeCode(), CommunicationFunctionType.RCV);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.RECEIVER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
    }

    /**
     * Test of testCreateMCCIMT000200UV01Receiver method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000200UV01Receiver_NullInput() {
        log.info("testCreateMCCIMT000200UV01Receiver_NullInput");

        MCCIMT000200UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000200UV01Receiver(null);

        assertNotNull(result);
    }
    
     /**
     * Test of testCreateMCCIMT000100UV01Receiver method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000100UV01Receiver() {
        log.info("testCreateMCCIMT000100UV01Receiver");

        String OID = "2.16.840.1.113883.3.123";
        MCCIMT000100UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(OID);

        assertEquals(result.getTypeCode(), CommunicationFunctionType.RCV);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.RECEIVER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.123");
    }

    /**
     * Test of testCreateMCCIMT000100UV01Receiver method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000100UV01Receiver_NullInput() {
        log.info("testCreateMCCIMT000100UV01Receiver_NullInput");

        MCCIMT000100UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(null);

        assertNotNull(result);
    }

    /**
     * Test of testCreateMCCIMT000300UV01Receiver method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000300UV01Receiver() {
        log.info("testCreateMCCIMT000300UV01Receiver");

        String OID = "2.16.840.1.113883.3.199";
        MCCIMT000300UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(OID);

        assertEquals(result.getTypeCode(), CommunicationFunctionType.RCV);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.RECEIVER_DETERMINER_CODE);
        assertEquals(result.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.199");
    }

    /**
     * Test of testCreateMCCIMT000300UV01Receiver method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000300UV01Receiver_NullInput() {
        log.info("testCreateMCCIMT000300UV01Receiver_NullInput");

        MCCIMT000300UV01Receiver result = HL7ReceiverTransforms.createMCCIMT000300UV01Receiver(null);

        assertNotNull(result);
    }

}