package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class SenderReceiverHelperTestMCCIMT000300UV01 {
    
    @Test
    public void testCreateSender() {
        SenderReceiverHelperMCCIMT000300UV01 sender = new SenderReceiverHelperMCCIMT000300UV01();
        MCCIMT000300UV01Sender senderDevice = new MCCIMT000300UV01Sender();
        senderDevice = sender.CreateSender(createSenderDeviceId());
        assertEquals(senderDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTSender");
        assertEquals(senderDevice.getDevice().getId().get(0).getExtension(),"D123401");
        assertEquals(senderDevice.getDevice().getId().get(0).getRoot(), "1.1");    
    }
    
    @Test
    public void testCreateSenderWhenDeviceIdNull() {
        SenderReceiverHelperMCCIMT000300UV01 sender = new SenderReceiverHelperMCCIMT000300UV01();
        MCCIMT000300UV01Sender senderDevice = new MCCIMT000300UV01Sender();
        senderDevice = sender.CreateSender();
        assertNull(senderDevice.getDevice().getId().get(0));
    }
    
    @Test
    public void testCreateReceiver() {
        SenderReceiverHelperMCCIMT000300UV01 receiver = new SenderReceiverHelperMCCIMT000300UV01();
        MCCIMT000300UV01Receiver receiverDevice = new MCCIMT000300UV01Receiver();
        receiverDevice = receiver.CreateReceiver(createReceiverDevice());
        assertEquals(receiverDevice.getDevice().getId().get(0).getAssigningAuthorityName(), "CONNECTReceiver");
        assertEquals(receiverDevice.getDevice().getId().get(0).getExtension(),"D123401");
        assertEquals(receiverDevice.getDevice().getId().get(0).getRoot(),"2.2");
    }
    
    @Test
    public void testCreateReceiverWhenDeviceIdNull() {
        SenderReceiverHelperMCCIMT000300UV01 receiver = new SenderReceiverHelperMCCIMT000300UV01();
        MCCIMT000300UV01Receiver receiverDevice = new MCCIMT000300UV01Receiver();
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
    
    private  II createReceiverDevice() {
        II receiverDevice = new II();
        receiverDevice.setAssigningAuthorityName("CONNECTReceiver");
        receiverDevice.setExtension("D123401");
        receiverDevice.setRoot("2.2");
        return receiverDevice;
    }

}
