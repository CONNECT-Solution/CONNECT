/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

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
