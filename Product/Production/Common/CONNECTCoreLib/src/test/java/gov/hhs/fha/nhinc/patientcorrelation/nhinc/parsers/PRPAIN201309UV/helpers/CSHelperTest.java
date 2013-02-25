package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.CS;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class CSHelperTest {
    
    @Test
    public void testBuildCS() {
        String code = "CS";
        CS cs = new CS();
        CSHelper helper = new CSHelper();
        cs = helper.buildCS(code);
        assertEquals(cs.getCode(),"CS");
        
    }

}
