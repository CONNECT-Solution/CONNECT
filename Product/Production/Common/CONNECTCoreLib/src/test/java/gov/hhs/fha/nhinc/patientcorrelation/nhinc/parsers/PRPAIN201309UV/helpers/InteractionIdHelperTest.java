package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.II;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class InteractionIdHelperTest {
    
    @Test
    public void createInteractionId() {
        II ii = new II();
        InteractionIdHelper helper = new InteractionIdHelper();
        String extension = "D123401";
        ii= helper.createInteractionId(extension);
        assertEquals(ii.getRoot(), "2.16.840.1.113883.1.6");
        assertEquals(ii.getExtension(),"D123401");
    }

}
