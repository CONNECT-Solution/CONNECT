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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

/**
 *
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseOrchImpl implements EntityPatientDiscoveryDeferredResponseOrch {

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryDeferredResponseOrchImpl.class);

    private final PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;
    private final WebServiceProxyHelper webserviceProxyhelper;
    private final PatientDiscovery201306Processor pd201306Processor;

    EntityPatientDiscoveryDeferredResponseOrchImpl(
            PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker) {
        this.policyChecker = policyChecker;

        this.webserviceProxyhelper = new WebServiceProxyHelper();
        this.pd201306Processor = new PatientDiscovery201306Processor();
    }

    protected PatientDiscoveryAuditor createAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return this.webserviceProxyhelper;
    }

    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncRespOrch(PRPAIN201306UV02 body, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        if (body != null && assertion != null) {

            RespondingGatewayPRPAIN201306UV02RequestType request = createNewRespondingGatewayRequest(body, assertion,
                    target);
            auditRequestFromAdapter(request, assertion);

            // loop through the communities and send request if results were not null
            List<UrlInfo> urlInfoList = getTargetEndpoints(target);
            if (urlInfoList != null) {
                for (UrlInfo urlInfo : urlInfoList) {

                    // Log the start of the performance record
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                            NhincConstants.PATIENT_DISCOVERY_DEFERRED_SERVICE_NAME,
                            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                            HomeCommunityMap.getLocalHomeCommunityId());

                    // create a new request to send out to each target community
                    PRPAIN201306UV02 new201306 = pd201306Processor.createNewRequest(body, urlInfo.getHcid());
                    RespondingGatewayPRPAIN201306UV02RequestType newRequest = createNewRespondingGatewayRequest(
                            new201306, assertion, target);

                    // check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);
                    if (bIsPolicyOk) {
                        ack = sendToProxy(body, assertion, target, urlInfo);
                    } else {
                        ack = HL7AckTransforms.createAckFrom201306(body, "Policy Failed");
                    }

                    // Log the end of the performance record
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                            NhincConstants.PATIENT_DISCOVERY_DEFERRED_SERVICE_NAME,
                            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                            HomeCommunityMap.getLocalHomeCommunityId());
                }
            } else {
                log.warn("No targets were found for the Patient Discovery Response");
                ack = HL7AckTransforms.createAckFrom201306(body, "No Targets Found");
            }

            auditResponseToAdapter(ack, assertion);
        }

        return ack;
    }

    protected void auditRequestFromAdapter(RespondingGatewayPRPAIN201306UV02RequestType request, AssertionType assertion) {
        log.debug("Begin logRequest");
        PatientDiscoveryAuditor auditLog = createAuditLogger();
        auditLog.auditEntityDeferred201306(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug("End logRequest");
    }

    protected void auditResponseToAdapter(MCCIIN000002UV01 ack, AssertionType assertion) {
        log.debug("Begin logResponse");
        PatientDiscoveryAuditor auditLog = createAuditLogger();
        auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        log.debug("End logResponse");
    }

    protected List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTargetCommunities(
                    targetCommunities, NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service "
                    + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected RespondingGatewayPRPAIN201306UV02RequestType createNewRespondingGatewayRequest(PRPAIN201306UV02 message,
            AssertionType assertion, NhinTargetCommunitiesType target) {
        RespondingGatewayPRPAIN201306UV02RequestType newRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201306UV02(message);
        newRequest.setNhinTargetCommunities(target);

        return newRequest;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201306UV02RequestType request) {
        return policyChecker.checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201306UV02 request, AssertionType assertion,
            NhinTargetCommunitiesType target, UrlInfo urlInfo) {

        auditRequestToNhin(request, assertion);

        NhinTargetSystemType targetSystemType = createNhinTargetSystemType(urlInfo.getUrl(), urlInfo.getHcid());
        OutboundPatientDiscoveryDeferredResponseDelegate pdRespDelegate = new OutboundPatientDiscoveryDeferredResponseDelegate();
        OutboundPatientDiscoveryDeferredResponseOrchestratable pdRespOrchestratable = new OutboundPatientDiscoveryDeferredResponseOrchestratable(
                pdRespDelegate);
        pdRespOrchestratable.setAssertion(assertion);
        pdRespOrchestratable.setRequest(request);
        pdRespOrchestratable.setTarget(targetSystemType);
        MCCIIN000002UV01 resp = ((OutboundPatientDiscoveryDeferredResponseOrchestratable) pdRespDelegate
                .process(pdRespOrchestratable)).getResponse();

        auditResponseFromNhin(resp, assertion);

        return resp;
    }

    protected NhinTargetSystemType createNhinTargetSystemType(String url, String hcid) {
        NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
        targetSystemType.setUrl(url);

        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        targetSystemType.setHomeCommunity(homeCommunity);

        return targetSystemType;
    }

    protected void auditRequestToNhin(PRPAIN201306UV02 request, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditNhinDeferred201306(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromNhin(MCCIIN000002UV01 resp, AssertionType assertion) {
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }
}
