/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.response.proxy.EntityPatientDiscoveryAsyncRespProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        EntityPatientDiscoveryAsyncRespProxyObjectFactory entityPatDiscAsyncReqFactory = new EntityPatientDiscoveryAsyncRespProxyObjectFactory();

        EntityPatientDiscoveryAsyncRespProxy proxy = entityPatDiscAsyncReqFactory.getEntityPatientDiscoveryAsyncRespProxy();

        ack = proxy.processPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
