package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.CS;

/**
 *
 * @author rayj
 */
public class CSHelper {
    public static CS buildCS(String code) {
        CS csCode = new CS();
        csCode.setCode(code);
        return csCode;
    }

}
