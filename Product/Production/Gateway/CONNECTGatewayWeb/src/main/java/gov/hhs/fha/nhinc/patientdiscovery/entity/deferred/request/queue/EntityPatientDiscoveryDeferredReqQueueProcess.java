/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.hl7.v3.MCCIIN000002UV01;

@WebService(serviceName = "EntityPatientDiscoveryAsyncReqQueueProcess")
public class EntityPatientDiscoveryDeferredReqQueueProcess {

    protected EntityPatientDiscoveryDeferredReqQueueProcessImpl getEntityPatientDiscoveryDeferredReqQueueProcessImpl() {
        return new EntityPatientDiscoveryDeferredReqQueueProcessImpl();
    }

    /**
     * processPatientDiscoveryAsyncReqQueue WebMethod for processing request queues on reponding gateway
     * @param messageId
     * @return
     */
    @WebMethod(operationName = "processPatientDiscoveryAsyncReqQueue")
    public String processPatientDiscoveryAsyncReqQueue(@WebParam(name = "messageId") String messageId) {
        String result = "Failed";
        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        EntityPatientDiscoveryDeferredReqQueueProcessImpl entityPatientDiscoveryDeferredReqQueueProcessImpl = getEntityPatientDiscoveryDeferredReqQueueProcessImpl();
        mCCIIN000002UV01 = entityPatientDiscoveryDeferredReqQueueProcessImpl.processPatientDiscoveryAsyncReqQueue(messageId);
        if (mCCIIN000002UV01 != null) {
            result = "Success";
        }
        return result;
    }
}
