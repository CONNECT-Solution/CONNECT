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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseDelegate;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

import java.util.List;

import org.apache.log4j.Logger;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

public class StandardOutboundPatientDiscoveryDeferredResponse extends AbstractOutboundPatientDiscoveryDeferredResponse {

    private final static MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();

    private final PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;
    private final PatientDiscovery201306Processor pd201306Processor;
    private final PatientDiscoveryAuditor auditLogger;
    private final OutboundPatientDiscoveryDeferredResponseDelegate delegate;
    private final ConnectionManagerCache connectionManager;
    private static final Logger LOG = Logger.getLogger(StandardOutboundPatientDiscoveryDeferredResponse.class);

    /**
     * Constructor.
     */
    public StandardOutboundPatientDiscoveryDeferredResponse() {
        policyChecker = PatientDiscovery201306PolicyChecker.getInstance();
        pd201306Processor = new PatientDiscovery201306Processor();
        auditLogger = new PatientDiscoveryAuditLogger();
        delegate = new OutboundPatientDiscoveryDeferredResponseDelegate();
        connectionManager = ConnectionManagerCache.getInstance();
    }

    /**
     * Constructor with dependency injection parameters.
     * 
     * @param policyChecker
     * @param pd201306Processor
     * @param auditLogger
     * @param delegate
     * @param LOG
     */
    public StandardOutboundPatientDiscoveryDeferredResponse(
            PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker,
            PatientDiscovery201306Processor pd201306Processor, PatientDiscoveryAuditor auditLogger,
            OutboundPatientDiscoveryDeferredResponseDelegate delegate, ConnectionManagerCache connectionManager) {
        this.policyChecker = policyChecker;
        this.pd201306Processor = pd201306Processor;
        this.auditLogger = auditLogger;
        this.delegate = delegate;
        this.connectionManager = connectionManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.AbstractOutboundPatientDiscoveryDeferredResponse
     * #process(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType,
     * gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType)
     */
    @Override
    MCCIIN000002UV01 process(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetCommunitiesType targets) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        auditRequestFromAdapter(body, assertion, targets);

        List<UrlInfo> urlInfoList = getTargetEndpoints(targets);
        if (urlInfoList != null) {
            for (UrlInfo urlInfo : urlInfoList) {

                RespondingGatewayPRPAIN201306UV02RequestType newRequest = createNewRequestForSingleTarget(body,
                        assertion, targets, urlInfo.getHcid());

                if (isPolicyValid(newRequest)) {
                    ack = sendToNhin(newRequest, urlInfo);
                } else {
                    ack = HL7AckTransforms.createAckErrorFrom201306(body, "Policy Check Failed");
                }
            }
        } else {
            LOG.warn("No targets were found for the Patient Discovery Response");
            ack = HL7AckTransforms.createAckErrorFrom201306(body, "No Targets Found");
        }

        auditResponseToAdapter(ack, assertion);

        return ack;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.AbstractOutboundPatientDiscoveryDeferredResponse
     * #getAuditLogger()
     */
    @Override
    PatientDiscoveryAuditor getAuditLogger() {
        return auditLogger;
    }

    private List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        try {
            urlInfoList = connectionManager.getEndpointURLFromNhinTargetCommunities(targetCommunities,
                    NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            LOG.error("Failed to obtain target URLs for service "
                    + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    private RespondingGatewayPRPAIN201306UV02RequestType createNewRequestForSingleTarget(PRPAIN201306UV02 body,
            AssertionType assertion, NhinTargetCommunitiesType targets, String hcid) {

        PRPAIN201306UV02 new201306 = pd201306Processor.createNewRequest(body, hcid);
        return msgUtils.createRespondingGatewayRequest(new201306, assertion, targets);
    }

    private boolean isPolicyValid(RespondingGatewayPRPAIN201306UV02RequestType request) {
        return policyChecker.checkOutgoingPolicy(request);
    }

    private MCCIIN000002UV01 sendToNhin(RespondingGatewayPRPAIN201306UV02RequestType request, UrlInfo urlInfo) {

        NhinTargetSystemType targetSystemType = msgUtils
                .createNhinTargetSystemType(urlInfo.getUrl(), urlInfo.getHcid());

        return sendToNhin(delegate, request.getPRPAIN201306UV02(), request.getAssertion(), targetSystemType);
    }

    protected void auditRequestFromAdapter(PRPAIN201306UV02 message, AssertionType assertion,
            NhinTargetCommunitiesType targets) {

        RespondingGatewayPRPAIN201306UV02RequestType request = MessageGeneratorUtils.getInstance()
                .createRespondingGatewayRequest(message, assertion, targets);

        getAuditLogger().auditEntityDeferred201306(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void auditResponseToAdapter(MCCIIN000002UV01 ack, AssertionType assertion) {
        getAuditLogger().auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
    }
}
