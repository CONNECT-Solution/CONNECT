/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class SenderReceiverHelperTestMCCIMT000200UV01 {
   
    @Test
    public void testCreateSender() {
        SenderReceiverHelperMCCIMT000200UV01 sender = new SenderReceiverHelperMCCIMT000200UV01();
        MCCIMT000200UV01Sender senderDevice = new MCCIMT000200UV01Sender();
        senderDevice = sender.CreateSender(createSenderDevice());
        assertEquals(senderDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTSender");
        assertEquals(senderDevice.getDevice().getId().get(0).getExtension(),"D123401");
        assertEquals(senderDevice.getDevice().getId().get(0).getRoot(), "1.1");
    }
    
    @Test
    public void testCreateSenderWhenSenderDeviceIdNull() {
        SenderReceiverHelperMCCIMT000200UV01 sender = new SenderReceiverHelperMCCIMT000200UV01();
        MCCIMT000200UV01Sender senderDevice = new MCCIMT000200UV01Sender();
        senderDevice = sender.CreateSender();
        assertNull(senderDevice.getDevice().getId().get(0));
    }
    
    @Test
    public void testCreateReceiver() {
        SenderReceiverHelperMCCIMT000200UV01 receiver = new SenderReceiverHelperMCCIMT000200UV01();
        MCCIMT000200UV01Receiver receiverDevice = new MCCIMT000200UV01Receiver();
        receiverDevice = receiver.CreateReceiver(createReceiverDevice());
        assertEquals(receiverDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTReceiver");
        assertEquals(receiverDevice.getDevice().getId().get(0).getExtension(), "D123401");
        assertEquals(receiverDevice.getDevice().getId().get(0).getRoot(), "2.2");
    }
    
    @Test
    public void testCreateReceiverWhenDeviceIdNull() {
        SenderReceiverHelperMCCIMT000200UV01 receiver = new SenderReceiverHelperMCCIMT000200UV01();
        MCCIMT000200UV01Receiver receiverDevice = new MCCIMT000200UV01Receiver();
        receiverDevice = receiver.CreateReceiver();
        assertNull(receiverDevice.getDevice().getId().get(0));
        
    }
    
    private II createSenderDevice() {
        II senderDeviceId = new II();
        senderDeviceId.setAssigningAuthorityName("CONNECTSender");
        senderDeviceId.setExtension("D123401");
        senderDeviceId.setRoot("1.1");
        return senderDeviceId;    
    }
    
    private II createReceiverDevice() {
        II receiverDevice = new II();
        receiverDevice.setAssigningAuthorityName("CONNECTReceiver");
        receiverDevice.setExtension("D123401");
        receiverDevice.setRoot("2.2");
        return receiverDevice;
    }

}
