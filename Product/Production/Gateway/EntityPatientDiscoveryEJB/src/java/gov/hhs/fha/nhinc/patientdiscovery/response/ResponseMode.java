/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201306UV02;
/**
 *
 * @author dunnek
 */
public interface ResponseMode {    
    public PRPAIN201306UV02 processResponse(ResponseParams params);
}
