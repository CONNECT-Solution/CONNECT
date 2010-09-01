package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.CD;

/**
 *
 * @author rayj
 */
public class CDHelper {

    public static CD CDFactory(String codeValue, String codeSystem) {
                CD code = new CD();
        code.setCode( codeValue);
        code.setCodeSystem(codeSystem);
return code;
    }
}
