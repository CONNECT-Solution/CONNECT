/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers;

import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class InteractionIdHelper {


    public static II createInteractionId(String extension) {
        return IIHelper.IIFactory( Constants.HL7_OID, extension);
    }
}
