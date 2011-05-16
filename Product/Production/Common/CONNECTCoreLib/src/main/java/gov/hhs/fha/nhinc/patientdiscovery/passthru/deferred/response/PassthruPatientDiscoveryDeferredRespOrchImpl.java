/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespOrchImpl {

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    /**
     *
     * @param request
     * @param assertion
     * @param targetSystem
     * @return Patient Discovery Response Acknowledgement
     */
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        MCCIIN000002UV01 response = null;
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditNhin201306(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryDeferredRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryDeferredRespProxyObjectFactory();
        NhinPatientDiscoveryDeferredRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

        // ASYNCMSG PROCESSING - REQSENT
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        RespondingGatewayPRPAIN201306UV02RequestType nhinResponse = new RespondingGatewayPRPAIN201306UV02RequestType();
        nhinResponse.setPRPAIN201306UV02(request);
        nhinResponse.setAssertion(assertion);
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        target.setHomeCommunity(targetSystem.getHomeCommunity());
        targets.getNhinTargetCommunity().add(target);
        nhinResponse.setNhinTargetCommunities(targets);

        boolean bIsQueueOk = asyncProcess.processPatientDiscoveryResponse(assertion.getMessageId(), AsyncMsgRecordDao.QUEUE_STATUS_RSPSENT, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, nhinResponse);

        // check for valid queue update
        if (bIsQueueOk) {
            response = proxy.respondingGatewayPRPAIN201306UV02(request, assertion, targetSystem);

            // ASYNCMSG PROCESSING - REQSENTACK
            bIsQueueOk = asyncProcess.processAck(assertion.getRelatesToList().get(0), AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPSENTERR, response);
        } else {
            String ackMsg = "Deferred Patient Discovery response processing halted; deferred queue repository error encountered";

            // Set the error acknowledgement status
            // fatal error with deferred queue repository
            response = HL7AckTransforms.createAckErrorFrom201306(request, ackMsg);
        }

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        auditLog.auditAck(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return response;
    }
}
