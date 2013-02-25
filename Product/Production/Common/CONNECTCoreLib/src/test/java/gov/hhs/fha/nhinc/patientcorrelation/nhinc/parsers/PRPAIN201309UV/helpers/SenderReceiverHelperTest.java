package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class SenderReceiverHelperTest {
    
    @Test
    public void testCreateSender() {
    MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
    SenderReceiverHelper helper = new SenderReceiverHelper();
    String senderDeviceId = "1.16.17.19";
    sender = helper.CreateSender(senderDeviceId);
    assertEquals(sender.getDevice().getId().get(0).getRoot(), "1.16.17.19");
    }
    
    @Test
    public void testCreateSenderDeviceIdNull() {
    MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
    SenderReceiverHelper helper = new SenderReceiverHelper();
    sender = helper.CreateSender();
    assertEquals(sender.getDevice().getId().get(0).getNullFlavor().get(0), "NA");
    }
    
    @Test
    public void CreateReceiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        SenderReceiverHelper helper = new SenderReceiverHelper();
        String receiverDeviceId = "1.16.17.19";
        receiver = helper.CreateReceiver(receiverDeviceId);
        assertEquals(receiver.getDevice().getId().get(0).getRoot(), "1.16.17.19");      
    }
    
    @Test
    public void CreateReceiverDeviceIdNull() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
        SenderReceiverHelper helper = new SenderReceiverHelper();
        String receiverDeviceId = "1.16.17.19";
        receiver = helper.CreateReceiver();
        assertEquals(receiver.getDevice().getId().get(0).getNullFlavor().get(0), "NA"); 
    }
}
