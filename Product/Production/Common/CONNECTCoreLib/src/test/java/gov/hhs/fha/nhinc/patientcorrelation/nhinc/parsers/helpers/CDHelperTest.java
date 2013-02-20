package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.CD;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class CDHelperTest {
    
    @Test
    public void testCDFactory() {
        String codeValue = "CD";
        String codeSystem = "CONNECT";
        CDHelper cd = new CDHelper();
        CD code = new CD();
        code = cd.CDFactory(codeValue, codeSystem);
        assertEquals(code.getCode(),codeValue);
        assertEquals(code.getCodeSystem(), codeSystem);
    }

}
