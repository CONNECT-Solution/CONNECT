/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author mastan.ketha
 */
public class EntityPatientDiscoveryDeferredReqQueueProcessImpl {

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryDeferredReqQueueProcessImpl.class);

    protected EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
        return new EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
    }

    /**
     * processPatientDiscoveryDeferredReqQueue Implementation for processing request queues on reponding gateway
     * @param request
     * @return response
     */
    public PatientDiscoveryDeferredReqQueueProcessResponseType processPatientDiscoveryDeferredReqQueue(PatientDiscoveryDeferredReqQueueProcessRequestType request, WebServiceContext context) {

        PatientDiscoveryDeferredReqQueueProcessResponseType response = new PatientDiscoveryDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl entityPatientDiscoveryDeferredReqQueueProcessOrchImpl = getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        mCCIIN000002UV01 = entityPatientDiscoveryDeferredReqQueueProcessOrchImpl.processPatientDiscoveryDeferredReqQueue(request.getMessageId());

        if (mCCIIN000002UV01 != null &&
                mCCIIN000002UV01.getAcknowledgement() != null &&
                mCCIIN000002UV01.getAcknowledgement().size() > 0 &&
                mCCIIN000002UV01.getAcknowledgement().get(0) != null &&
                mCCIIN000002UV01.getAcknowledgement().get(0).getTypeCode() != null &&
                mCCIIN000002UV01.getAcknowledgement().get(0).getTypeCode().getCode() != null &&
                mCCIIN000002UV01.getAcknowledgement().get(0).getTypeCode().getCode().equals(HL7AckTransforms.ACK_TYPE_CODE_ACCEPT)) {
            sof.setSuccess(Boolean.TRUE);
        }

        return response;
    }

}
