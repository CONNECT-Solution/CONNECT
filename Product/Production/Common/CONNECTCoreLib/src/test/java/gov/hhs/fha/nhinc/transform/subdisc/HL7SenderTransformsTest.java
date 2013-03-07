package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertNull;

import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000200UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.junit.Test;

/**
 * @author achidambaram
 * 
 */
public class HL7SenderTransformsTest {

    @Test
    public void createMCCIMT000200UV01Sender() {
        MCCIMT000200UV01Sender sender = null;
        String OID = null;
        HL7SenderTransforms transforms = new HL7SenderTransforms();
        sender = transforms.createMCCIMT000200UV01Sender(OID);
        assertNull(sender.getDevice().getId().get(0).getRoot());
        assertNull(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

    @Test
    public void createMCCIMT000100UV01Sender() {
        MCCIMT000100UV01Sender sender = null;
        String OID = null;
        HL7SenderTransforms transforms = new HL7SenderTransforms();
        sender = transforms.createMCCIMT000100UV01Sender(OID);
        assertNull(sender.getDevice().getId().get(0).getRoot());
        assertNull(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

    @Test
    public void createMCCIMT000300UV01Sender() {
        MCCIMT000300UV01Sender sender = null;
        String OID = null;
        HL7SenderTransforms transforms = new HL7SenderTransforms();
        sender = transforms.createMCCIMT000300UV01Sender(OID);
        assertNull(sender.getDevice().getId().get(0).getRoot());
        assertNull(sender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0)
                .getRoot());
    }

}
