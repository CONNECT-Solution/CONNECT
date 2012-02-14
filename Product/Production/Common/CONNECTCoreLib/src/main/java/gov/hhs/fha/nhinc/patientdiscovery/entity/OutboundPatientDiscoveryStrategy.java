/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bhumphrey/paul
 * 
 */
public abstract class OutboundPatientDiscoveryStrategy implements OrchestrationStrategy {

    private static Log log = LogFactory.getLog(OutboundPatientDiscoveryStrategy.class);

    private Log getLogger() {
        return log;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy#execute(gov.hhs.fha.nhinc.orchestration.Orchestratable)
     */
    @Override
    public void execute(Orchestratable message) {
        if (message instanceof OutboundPatientDiscoveryOrchestratable) {
            execute((OutboundPatientDiscoveryOrchestratable) message);
        } else {
            // shouldn't get here
            getLogger()
                    .error("NhinPatientDiscoveryStrategy input Orchestratable was not an EntityPatientDiscoveryOrchestratable!!!");
            // throw new
            // Exception("NhinPatientDiscoveryStrategy input message was not an EntityPatientDiscoveryOrchestratable!!!");
        }
    }

    abstract public void execute(OutboundPatientDiscoveryOrchestratable message);

    protected void auditRequestMessage(PRPAIN201305UV02 request, AssertionType assertion, String hcid) {

        RespondingGatewayPRPAIN201305UV02RequestType auditRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        auditRequest.setAssertion(assertion);
        // set targetList with individual target for hcid
        NhinTargetCommunitiesType targetList = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);
        targetList.getNhinTargetCommunity().add(target);
        auditRequest.setNhinTargetCommunities(targetList);
        auditRequest.setPRPAIN201305UV02(request);
        new PatientDiscoveryAuditLogger().auditEntity201305(auditRequest, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseMessage(PRPAIN201306UV02 response, AssertionType assertion, String hcid) {

        RespondingGatewayPRPAIN201306UV02ResponseType auditResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
        communityResponse.setPRPAIN201306UV02(response);
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        target.setHomeCommunity(home);
        communityResponse.setNhinTargetCommunity(target);
        auditResponse.getCommunityResponse().add(communityResponse);
        new PatientDiscoveryAuditLogger().auditEntity201306(auditResponse, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

}
