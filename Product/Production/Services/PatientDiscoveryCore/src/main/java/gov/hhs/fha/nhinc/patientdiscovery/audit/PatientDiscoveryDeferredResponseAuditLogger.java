/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.audit;

import gov.hhs.fha.nhinc.audit.AuditLogger;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryDeferredResponseAuditTransforms;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author achidamb
 */
public class PatientDiscoveryDeferredResponseAuditLogger extends AuditLogger<PRPAIN201306UV02, MCCIIN000002UV01>  {
    
     @Override
    protected AuditTransforms<PRPAIN201306UV02, MCCIIN000002UV01> getAuditTransforms() {
        return new PatientDiscoveryDeferredResponseAuditTransforms();
    }
    
}
