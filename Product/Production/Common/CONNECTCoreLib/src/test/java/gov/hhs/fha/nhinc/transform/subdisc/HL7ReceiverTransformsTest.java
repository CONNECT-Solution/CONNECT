package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertNull;

import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000200UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.junit.Test;

/**
 * @author achidambaram
 * 
 */
public class HL7ReceiverTransformsTest {

    @Test
    public void createMCCIMT000200UV01Receiver() {
        String OID = null;
        HL7ReceiverTransforms transforms = new HL7ReceiverTransforms();
        MCCIMT000200UV01Receiver receiver = transforms.createMCCIMT000200UV01Receiver(OID);
        assertNull(receiver.getDevice().getId().get(0).getRoot());
        assertNull(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

    @Test
    public void createMCCIMT000100UV01Receiver() {
        String OID = null;
        HL7ReceiverTransforms transforms = new HL7ReceiverTransforms();
        MCCIMT000100UV01Receiver receiver = transforms.createMCCIMT000100UV01Receiver(OID);
        assertNull(receiver.getDevice().getId().get(0).getRoot());
        assertNull(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

    @Test
    public void createMCCIMT000300UV01Receiver() {
        String OID = null;
        HL7ReceiverTransforms transforms = new HL7ReceiverTransforms();
        MCCIMT000300UV01Receiver receiver = transforms.createMCCIMT000300UV01Receiver(OID);
        assertNull(receiver.getDevice().getId().get(0).getRoot());
        assertNull(receiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

}
