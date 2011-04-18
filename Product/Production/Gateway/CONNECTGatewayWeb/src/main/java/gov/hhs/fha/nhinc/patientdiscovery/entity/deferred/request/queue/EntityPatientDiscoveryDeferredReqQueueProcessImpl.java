/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author mastan.ketha
 */
public class EntityPatientDiscoveryDeferredReqQueueProcessImpl {

    public EntityPatientDiscoveryDeferredReqQueueProcessImpl() {
    }

    /**
     * processPatientDiscoveryAsyncReqQueue Implementation method for processing request queues on reponding gateway
     * @param messageId
     * @return org.hl7.v3.MCCIIN000002UV01
     */
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqQueue(String messageId) {
        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl entityPatientDiscoveryDeferredReqQueueProcessOrchImpl = new EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        mCCIIN000002UV01 = entityPatientDiscoveryDeferredReqQueueProcessOrchImpl.processPatientDiscoveryAsyncReqQueue(messageId);
        return mCCIIN000002UV01;
    }
}
