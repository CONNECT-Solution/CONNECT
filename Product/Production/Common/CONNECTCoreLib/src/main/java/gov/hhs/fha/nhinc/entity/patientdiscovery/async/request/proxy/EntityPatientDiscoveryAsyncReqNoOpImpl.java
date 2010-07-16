/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class EntityPatientDiscoveryAsyncReqNoOpImpl implements EntityPatientDiscoveryAsyncReqProxy {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType processPatientDiscoveryAsyncReqAsyncRequest) {
        return new MCCIIN000002UV01();
    }
}
