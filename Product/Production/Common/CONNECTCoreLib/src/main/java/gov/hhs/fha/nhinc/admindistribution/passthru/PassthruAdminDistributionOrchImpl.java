/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution.passthru;

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionOrchestratable;
import gov.hhs.fha.nhinc.admindistribution.entity.OutboundAdminDistributionDelegate;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionOrchImpl {

    private Log log = null;

    public PassthruAdminDistributionOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
             NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.info("begin sendAlert");
        //TODO: LogRequest        
        AcknowledgementType ack = getLogger().auditNhincAdminDist(body, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }
        
        RespondingGatewaySendAlertMessageType newRequest = createRespondingGatewaySendAlertMessageType(target, assertion, body);

        OutboundAdminDistributionDelegate adDelegate = new OutboundAdminDistributionDelegate();
        OutboundAdminDistributionOrchestratable orchestratable =
                new OutboundAdminDistributionOrchestratable(adDelegate);
        orchestratable.setRequest(newRequest);
        orchestratable.setAssertion(assertion);
        orchestratable.setTarget(target);

        execute(adDelegate, orchestratable);
    }

    private RespondingGatewaySendAlertMessageType createRespondingGatewaySendAlertMessageType(NhinTargetSystemType target, AssertionType assertion, EDXLDistribution body) {
        NhinTargetCommunitiesType targetCommunitiesType = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunityType = new NhinTargetCommunityType();
        targetCommunityType.setHomeCommunity(target.getHomeCommunity());
        targetCommunitiesType.getNhinTargetCommunity().add(targetCommunityType);
        RespondingGatewaySendAlertMessageType newRequest = new RespondingGatewaySendAlertMessageType();
        newRequest.setAssertion(assertion);
        newRequest.setEDXLDistribution(body);
        newRequest.setNhinTargetCommunities(targetCommunitiesType);
        return newRequest;
    }
    
    protected void execute(OutboundAdminDistributionDelegate adDelegate, OutboundAdminDistributionOrchestratable orchestratable) {
        adDelegate.process(orchestratable);
    }

    protected AdminDistributionAuditLogger getLogger() {
        return new AdminDistributionAuditLogger();
    }
}
