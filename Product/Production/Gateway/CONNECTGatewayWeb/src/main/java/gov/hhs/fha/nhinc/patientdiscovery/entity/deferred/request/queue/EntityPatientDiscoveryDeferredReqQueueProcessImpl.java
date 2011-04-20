/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
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

    protected EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
        return new EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
    }

    /**
     * processPatientDiscoveryAsyncReqQueue Implementation method for processing request queues on reponding gateway
     * @param messageId
     * @return org.hl7.v3.MCCIIN000002UV01
     */
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqQueue(String messageId) {
        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl entityPatientDiscoveryDeferredReqQueueProcessOrchImpl = getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        mCCIIN000002UV01 = entityPatientDiscoveryDeferredReqQueueProcessOrchImpl.processPatientDiscoveryAsyncReqQueue(messageId);
        return mCCIIN000002UV01;
    }
}
