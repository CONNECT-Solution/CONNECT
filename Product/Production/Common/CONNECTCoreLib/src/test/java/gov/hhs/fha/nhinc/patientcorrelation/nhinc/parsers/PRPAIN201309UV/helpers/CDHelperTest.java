package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import static org.junit.Assert.assertEquals;

import org.hl7.v3.CD;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class CDHelperTest {
    @Test
    public void CDFactory() {
        CD cd = new CD();
        CDHelper helper = new CDHelper();
        String codeValue = "CD";
        String codeSystem = "Connect";
        cd = helper.CDFactory(codeValue, codeSystem);
        assertEquals(cd.getCode(), codeValue);
        assertEquals(cd.getCodeSystem(), "Connect");
        
    }

}
