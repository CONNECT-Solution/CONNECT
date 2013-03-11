package gov.hhs.fha.nhinc.transform.subdisc;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.PRPAIN201309UV02;
import org.junit.Test;

/**
 * @author achidambaram
 * 
 */
public class HL7PRPA201309TransformsTest {
    @Test
    public void createPRPA201309() {
        String patientId = "D123401";
        String homeCommunityId = "1.1";
        PRPAIN201309UV02 result = null;
        HL7PRPA201309Transforms transforms = new HL7PRPA201309Transforms();
        result = transforms.createPRPA201309(homeCommunityId, patientId);
        assertEquals(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().get(0).getValue().get(0).getExtension(), "D123401");
        assertEquals(result.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getPatientIdentifier().get(0).getValue().get(0).getRoot(), "1.1");
    }

}
