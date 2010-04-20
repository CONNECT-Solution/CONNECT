/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import org.hl7.v3.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7AckTransformsTest {

    private static Log log = LogFactory.getLog(HL7AckTransformsTest.class);
    
    private String localDeviceId = "2.16.840.1.113883.3.198.1";
    private II origMsgId = HL7DataTransformHelper.IIFactory("2.16.840.1.113883.3.200.1", "123456789");
    private String msgText = "Success";
    private String senderOID = "2.16.840.1.113883.3.200";
    private String receiverOID = "2.16.840.1.113883.3.198";

    public HL7AckTransformsTest() {
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
     * Test of createAckMessage method, with providing all of the arguments
     */
    @Test
    public void testCreateAckMessage() {
        log.info("testCreateAckMessage"); 

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, origMsgId, msgText, senderOID, receiverOID);
        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456789");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Success");
    }
    
    /**
     * Test of createAckMessage method, default local device id
     */
    @Test
    public void testCreateAckMessage_NoLocalDeviceId() {
        log.info("testCreateAckMessage_NoLocalDeviceId");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(null, origMsgId, msgText, senderOID, receiverOID);
        
        assertEquals(result.getId().getRoot(), HL7Constants.DEFAULT_LOCAL_DEVICE_ID);
        assertEquals(result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456789");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Success");
    }
    
    /**
     * Test of createAckMessage method, no original message id
     */
    @Test
    public void testCreateAckMessage_NoOrigMsgId() {
        log.info("testCreateAckMessage_NoOrigMsgId");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, null, msgText, senderOID, receiverOID);
        
        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertNull(result.getAcknowledgement().get(0).getTargetMessage());
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent().get(0), "Success");
    }
    
    /**
     * Test of createAckMessage method, no message text
     */
    @Test
    public void testCreateAckMessage_NoMsgText() {
        log.info("testCreateAckMessage_NoMsgText");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, origMsgId, null, senderOID, receiverOID);
        
        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getRoot(), "2.16.840.1.113883.3.200.1");
        assertEquals(result.getAcknowledgement().get(0).getTargetMessage().getId().getExtension(), "123456789");
        assertEquals(result.getAcknowledgement().get(0).getAcknowledgementDetail().size(), 0);
    }
    
    /**
     * Test of createAckMessage method, no acknowledgement section
     */
    @Test
    public void testCreateAckMessage_NoAckSection() {
        log.info("testCreateAckMessage_NoAckSection");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, null, null, senderOID, receiverOID);
        
        assertEquals(result.getId().getRoot(), "2.16.840.1.113883.3.198.1");
        assertEquals(result.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.200");
        assertEquals(result.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot(), "2.16.840.1.113883.3.198");
        assertEquals(result.getAcknowledgement().size(), 0);
    }
    
    /**
     * Test of createAckMessage method, no sender OID
     */
    @Test
    public void testCreateAckMessage_NoSenderOID() {
        log.info("testCreateAckMessage_NoSenderOID");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, origMsgId, msgText, null, receiverOID);
        
        assertNull(result);
    }
    
    /**
     * Test of createAckMessage method, no receiver OID
     */
    @Test
    public void testCreateAckMessage_NoReceiverOID() {
        log.info("testCreateAckMessage_NoReceiverOID");

        MCCIIN000002UV01 result = HL7AckTransforms.createAckMessage(localDeviceId, origMsgId, msgText, senderOID, null);
        
        assertNull(result);
    }
}