/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseDelegate;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.util.List;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundPatientDiscoveryDeferredResponse extends AbstractOutboundPatientDiscoveryDeferredResponse {

    private final static PDMessageGeneratorUtils msgUtils = PDMessageGeneratorUtils.getInstance();

    private final PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;
    private final PatientDiscovery201306Processor pd201306Processor;
    private final PatientDiscoveryDeferredResponseAuditLogger auditLogger;
    private final OutboundPatientDiscoveryDeferredResponseDelegate delegate;
    private final ExchangeManager exchangeManager;
    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundPatientDiscoveryDeferredResponse.class);

    /**
     * Constructor.
     */
    public StandardOutboundPatientDiscoveryDeferredResponse() {
        policyChecker = PatientDiscovery201306PolicyChecker.getInstance();
        pd201306Processor = new PatientDiscovery201306Processor();
        auditLogger = new PatientDiscoveryDeferredResponseAuditLogger();
        delegate = new OutboundPatientDiscoveryDeferredResponseDelegate();
        exchangeManager = ExchangeManager.getInstance();
    }

    /**
     * Constructor with dependency injection parameters.
     *
     * @param policyChecker
     * @param pd201306Processor
     * @param auditLogger
     * @param delegate
     * @param exchangeManager
     */
    public StandardOutboundPatientDiscoveryDeferredResponse(
        PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker,
        PatientDiscovery201306Processor pd201306Processor, PatientDiscoveryDeferredResponseAuditLogger auditLogger,
        OutboundPatientDiscoveryDeferredResponseDelegate delegate, ExchangeManager exchangeManager) {
        this.policyChecker = policyChecker;
        this.pd201306Processor = pd201306Processor;
        this.auditLogger = auditLogger;
        this.delegate = delegate;
        this.exchangeManager = exchangeManager;
    }

    @Override
    @OutboundProcessingEvent(beforeBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, afterReturningBuilder
    = MCCIIN000002UV01EventDescriptionBuilder.class, serviceType = "Patient Discovery Deferred Response", version
    = "1.0")
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion,
        NhinTargetCommunitiesType target) {

        return process(request, PDMessageGeneratorUtils.getInstance().generateMessageId(assertion),
            target);
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
    public MCCIIN000002UV01 process(PRPAIN201306UV02 body, AssertionType assertion, NhinTargetCommunitiesType targets) {
        logInfoServiceProcess(this.getClass());

        auditRequest(body, assertion, targets);
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        List<UrlInfo> urlInfoList = getTargetEndpoints(targets);
        if (urlInfoList != null) {
            for (UrlInfo urlInfo : urlInfoList) {

                RespondingGatewayPRPAIN201306UV02RequestType newRequest = createNewRequestForSingleTarget(body,
                    assertion, targets, urlInfo.getHcid());

                if (isPolicyValid(newRequest)) {
                    ack = sendToNhin(targets.getExchangeName(), newRequest, urlInfo);
                } else {
                    ack = HL7AckTransforms.createAckErrorFrom201306(body, "Policy Check Failed");
                }
            }
        } else {
            LOG.warn("No targets were found for the Patient Discovery Response");
            ack = HL7AckTransforms.createAckErrorFrom201306(body, "No Targets Found");
        }

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
    public PatientDiscoveryDeferredResponseAuditLogger getAuditLogger() {
        return auditLogger;
    }

    private List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList;

        try {
            urlInfoList = exchangeManager.getEndpointURLFromNhinTargetCommunities(targetCommunities,
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Failed to obtain target URLs for service "
                + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME + ex.getLocalizedMessage(), ex);
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

    private MCCIIN000002UV01 sendToNhin(String exchangeName,
        RespondingGatewayPRPAIN201306UV02RequestType request, UrlInfo urlInfo) {

        NhinTargetSystemType targetSystemType = msgUtils
            .createNhinTargetSystemType(exchangeName, urlInfo.getUrl(), urlInfo.getHcid());

        return sendToNhin(delegate, request.getPRPAIN201306UV02(), request.getAssertion(), targetSystemType);
    }
}
