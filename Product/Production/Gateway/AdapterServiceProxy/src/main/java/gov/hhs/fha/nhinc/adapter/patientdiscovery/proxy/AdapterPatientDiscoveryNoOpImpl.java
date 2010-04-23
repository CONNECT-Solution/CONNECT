/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryNoOpImpl implements AdapterPatientDiscoveryProxy {

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion) {
        
        return new PRPAIN201306UV02();
    }
}
