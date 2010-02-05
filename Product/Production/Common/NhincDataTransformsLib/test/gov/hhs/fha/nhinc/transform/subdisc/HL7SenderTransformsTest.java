/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.subdisc;

import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CommunicationFunctionType;

/**
 *
 * @author jhoppesc
 */
public class HL7SenderTransformsTest {
    
    private static Log log = LogFactory.getLog(HL7SenderTransformsTest.class);

    public HL7SenderTransformsTest() {
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
     * Test of testCreateMCCIMT000200UV01Sender method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000200UV01Sender() {
        log.info("testCreateMCCIMT000200UV01Sender");
        
        String OID = "2.16.840.1.113883.3.200";
        MCCIMT000200UV01Sender result = HL7SenderTransforms.createMCCIMT000200UV01Sender(OID);
        
        assertEquals(result.getTypeCode(), CommunicationFunctionType.SND);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.SENDER_DETERMINER_CODE);
        assertEquals(result.getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
    }

    /**
     * Test of testCreateMCCIMT000200UV01Sender method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000200UV01Sender_NullInput() {
        log.info("testCreateMCCIMT000200UV01Senderr_NullInput");
        
        MCCIMT000200UV01Sender result = HL7SenderTransforms.createMCCIMT000200UV01Sender(null);
        
        assertNotNull(result);
    }
    
    /**
     * Test of testCreateMCCIMT000100UV01Sender method, of class HL7DataTransformHelper.
     */
    @Test
    public void testCreateMCCIMT000100UV01Sender() {
        log.info("testCreateMCCIMT000100UV01Sender");
        
        String OID = "2.16.840.1.113883.3.166.4";
        MCCIMT000100UV01Sender result = HL7SenderTransforms.createMCCIMT000100UV01Sender(OID);
        
        assertEquals(result.getTypeCode(), CommunicationFunctionType.SND);
        assertEquals(result.getDevice().getDeterminerCode(), HL7Constants.SENDER_DETERMINER_CODE);
        assertEquals(result.getDevice().getId().get(0).getRoot(), "2.16.840.1.113883.3.166.4");
    }

    /**
     * Test of testCreateMCCIMT000100UV01Sender method, of class HL7DataTransformHelper
     * with no input.
     */
    @Test
    public void testCreateMCCIMT000100UV01Sender_NoInput() {
        log.info("testCreateMCCIMT000100UV01Sender_NoInput");
        
        MCCIMT000100UV01Sender result = HL7SenderTransforms.createMCCIMT000100UV01Sender(null);
        
        assertNotNull(result);
    }
   
}