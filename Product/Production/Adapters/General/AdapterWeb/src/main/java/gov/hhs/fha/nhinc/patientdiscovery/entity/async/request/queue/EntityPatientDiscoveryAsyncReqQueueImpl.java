/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request.queue;

import gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.queue.proxy.EntityPatientDiscoveryAsyncReqQueueProxy;
import gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.queue.proxy.EntityPatientDiscoveryAsyncReqQueueProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryAsyncReqQueueImpl {
    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        EntityPatientDiscoveryAsyncReqQueueProxyObjectFactory entityPatDiscAsyncReqQueueFactory = new EntityPatientDiscoveryAsyncReqQueueProxyObjectFactory();

        EntityPatientDiscoveryAsyncReqQueueProxy proxy = entityPatDiscAsyncReqQueueFactory.getEntityPatientDiscoveryAsyncReqQueueProxy();

        ack = proxy.addPatientDiscoveryAsyncReq(request);

        return ack;
    }
}
