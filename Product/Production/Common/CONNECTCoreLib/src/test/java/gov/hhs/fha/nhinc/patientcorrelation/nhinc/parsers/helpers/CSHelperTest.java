/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

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
        CSHelper cs = new CSHelper();
        CS csCode = new CS();
        String code = "CONNECT";
        csCode = cs.buildCS(code);
        assertEquals(csCode.getCode(),code);
    }
}
