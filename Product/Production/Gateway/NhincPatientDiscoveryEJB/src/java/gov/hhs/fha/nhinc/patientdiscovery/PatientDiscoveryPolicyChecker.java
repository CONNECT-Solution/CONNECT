/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryPolicyChecker {

    public boolean check201305Policy(PRPAIN201305UV02 message, II patIdOverride) {
        return true;
    }

}
