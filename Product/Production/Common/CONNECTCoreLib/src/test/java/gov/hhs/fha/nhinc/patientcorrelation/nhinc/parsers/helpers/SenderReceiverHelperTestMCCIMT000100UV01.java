/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class SenderReceiverHelperTestMCCIMT000100UV01 {
    
    @Test
    public void testCreateSender() {
        SenderReceiverHelperMCCIMT000100UV01 sender = new SenderReceiverHelperMCCIMT000100UV01();
        MCCIMT000100UV01Sender senderDevice = new MCCIMT000100UV01Sender();
        senderDevice = sender.CreateSender(createSenderDeviceId());
        assertEquals(senderDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTSender");
        assertEquals(senderDevice.getDevice().getId().get(0).getExtension(),"D123401");
        assertEquals(senderDevice.getDevice().getId().get(0).getRoot(), "1.1");
    }
    
    @Test
    public void testCreateSenderWhenDeviceIdNull() {
        MCCIMT000100UV01Sender senderDevice = new MCCIMT000100UV01Sender();
        SenderReceiverHelperMCCIMT000100UV01 sender = new SenderReceiverHelperMCCIMT000100UV01();
        senderDevice = sender.CreateSender();
        assertNull(senderDevice.getDevice().getId().get(0));
        
    }
    
    @Test
    public void testCreateReceiver() {
        MCCIMT000100UV01Receiver receiverDevice = new MCCIMT000100UV01Receiver();
        SenderReceiverHelperMCCIMT000100UV01 receiver = new SenderReceiverHelperMCCIMT000100UV01();
        receiverDevice = receiver.CreateReceiver(createReceiverDeviceId());
        assertEquals(receiverDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTReceiver");
        assertEquals(receiverDevice.getDevice().getId().get(0).getExtension(), "D123401");
    }
    
    @Test
    public void CreateReceiver() {
        MCCIMT000100UV01Receiver receiverDevice = new MCCIMT000100UV01Receiver();
        SenderReceiverHelperMCCIMT000100UV01 receiver = new SenderReceiverHelperMCCIMT000100UV01();
        receiverDevice = receiver.CreateReceiver();
        assertNull(receiverDevice.getDevice().getId().get(0));
    }
    
    private II createSenderDeviceId() {
        II senderDeviceId = new II();
        senderDeviceId.setAssigningAuthorityName("CONNECTSender");
        senderDeviceId.setExtension("D123401");
        senderDeviceId.setRoot("1.1");
        return senderDeviceId;
    }
    
    private II createReceiverDeviceId() {
        II receiverDeviceId = new II();
        receiverDeviceId.setAssigningAuthorityName("CONNECTReceiver");
        receiverDeviceId.setExtension("D123401");
        receiverDeviceId.setRoot("1.1");
        return receiverDeviceId;
    }

}
