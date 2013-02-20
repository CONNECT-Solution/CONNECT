/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.II;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class InteractionIdHelperTest {
    
    @Test
    public void testCreateInteractionId() {
        InteractionIdHelper helper = new InteractionIdHelper();
        String extension ="D123401";
        II ii = new II();
        ii = helper.createInteractionId(extension);
        assertEquals(ii.getRoot(), "2.16.840.1.113883.1.6");
        assertEquals(ii.getExtension(), "D123401");
    }
}
