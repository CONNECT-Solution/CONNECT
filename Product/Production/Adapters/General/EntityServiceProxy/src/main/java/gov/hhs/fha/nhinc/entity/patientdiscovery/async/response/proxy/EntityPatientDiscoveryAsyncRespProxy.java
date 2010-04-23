/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public interface EntityPatientDiscoveryAsyncRespProxy {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType processPatientDiscoveryAsyncRespAsyncRequest);

}
