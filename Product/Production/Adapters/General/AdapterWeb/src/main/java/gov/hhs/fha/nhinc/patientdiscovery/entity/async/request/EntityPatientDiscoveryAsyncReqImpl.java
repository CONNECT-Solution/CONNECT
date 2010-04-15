/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request;

import gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.proxy.EntityPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.proxy.EntityPatientDiscoveryAsyncReqProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class EntityPatientDiscoveryAsyncReqImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        EntityPatientDiscoveryAsyncReqProxyObjectFactory entityPatDiscAsyncReqFactory = new EntityPatientDiscoveryAsyncReqProxyObjectFactory();

        EntityPatientDiscoveryAsyncReqProxy proxy = entityPatDiscAsyncReqFactory.getEntityPatientDiscoveryAsyncReqProxy();

        ack = proxy.processPatientDiscoveryAsyncReq(request);

        return ack;
    }
}
