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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundPatientDiscoveryDeferredRequest extends AbstractOutboundPatientDiscoveryDeferredRequest {

    private static final PDMessageGeneratorUtils msgUtils = PDMessageGeneratorUtils.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundPatientDiscoveryDeferredRequest.class);
    private final PatientDiscovery201305Processor pd201305Processor;
    private final AsyncMessageProcessHelper asyncProcessHelper;
    private final PatientDiscoveryPolicyChecker policyChecker;
    private final OutboundPatientDiscoveryDeferredRequestDelegate delegate;
    private final PDDeferredCorrelationDao correlationDao;
    private final ExchangeManager exchangeManager;
    private final PatientDiscoveryDeferredRequestAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public StandardOutboundPatientDiscoveryDeferredRequest() {
        pd201305Processor = new PatientDiscovery201305Processor();
        asyncProcessHelper = new AsyncMessageProcessHelper();
        policyChecker = PatientDiscoveryPolicyChecker.getInstance();
        delegate = new OutboundPatientDiscoveryDeferredRequestDelegate();
        correlationDao = new PDDeferredCorrelationDao();
        exchangeManager = ExchangeManager.getInstance();
        auditLogger = new PatientDiscoveryDeferredRequestAuditLogger();
    }

    /**
     * Constructor with dependency injection.
     *
     * @param pd201305Processor
     * @param asyncProcessHelper
     * @param policyChecker
     * @param delegate
     * @param correlationDao
     * @param exchangeManager
     * @param auditLogger
     */
    public StandardOutboundPatientDiscoveryDeferredRequest(PatientDiscovery201305Processor pd201305Processor,
        AsyncMessageProcessHelper asyncProcessHelper, PatientDiscoveryPolicyChecker policyChecker,
        OutboundPatientDiscoveryDeferredRequestDelegate delegate, PDDeferredCorrelationDao correlationDao,
        ExchangeManager exchangeManager, PatientDiscoveryDeferredRequestAuditLogger auditLogger) {
        this.pd201305Processor = pd201305Processor;
        this.asyncProcessHelper = asyncProcessHelper;
        this.policyChecker = policyChecker;
        this.delegate = delegate;
        this.correlationDao = correlationDao;
        this.exchangeManager = exchangeManager;
        this.auditLogger = auditLogger;
    }

    /**
     * Processes and sends the request to the Nhin. This method will add mappings and correlations and will send the
     * request to all targets passed in.
     *
     * @param message
     * @param assertion
     * @param targets
     * @return the MCCIIN000002UV01 response from the Nhin
     */
    @Override
    @OutboundProcessingEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
    afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class,
    serviceType = "Patient Discovery Deferred Request", version = "1.0")
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        logInfoServiceProcess(this.getClass());

        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        List<UrlInfo> urlInfoList = getTargetEndpoints(targets);
        if (NullChecker.isNotNullish(urlInfoList)) {

            createLocalHCIDtoPatientAAMapping(message, assertion, targets);

            createMessageIdToPatientIdCorrelation(message, assertion);

            for (UrlInfo urlInfo : urlInfoList) {
                RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRespondingGatewayRequestForOneTarget(
                    message, assertion, targets, urlInfo.getHcid());

                auditRequest(message, assertion, msgUtils.convertFirstToNhinTargetSystemType(targets));

                if (isPolicyValid(newRequest)) {
                    ack = sendToNhin(newRequest.getPRPAIN201305UV02(), newRequest.getAssertion(), urlInfo, targets.
                        getExchangeName());
                } else {
                    ack = HL7AckTransforms.createAckErrorFrom201305(message, "Policy Check Failed");
                }
            }
        } else {
            String ackMsg = "No targets were found for the Patient Discovery Request";
            LOG.warn(ackMsg);
            ack = HL7AckTransforms.createAckErrorFrom201305(message, ackMsg);
        }
        return ack;
    }

    @Override
    public PatientDiscoveryDeferredRequestAuditLogger getPatientDiscoveryDeferredAuditLogger() {
        return auditLogger;
    }

    private List<UrlInfo> getTargetEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList;

        try {
            urlInfoList = exchangeManager.getEndpointURLFromNhinTargetCommunities(targetCommunities,
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Failed to obtain target URLs for service {}: {}",
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME, ex.getLocalizedMessage(), ex);
            return null;
        }

        return urlInfoList;
    }

    private void createLocalHCIDtoPatientAAMapping(PRPAIN201305UV02 message, AssertionType assertion,
        NhinTargetCommunitiesType targets) {
        RespondingGatewayPRPAIN201305UV02RequestType request = msgUtils.createRespondingGatewayRequest(message,
            assertion, targets);

        // Process local unique patient id and home community id to create local aa to hcid mapping
        pd201305Processor.storeLocalMapping(request);
    }

    private void createMessageIdToPatientIdCorrelation(PRPAIN201305UV02 message, AssertionType assertion) {
        II patientId = pd201305Processor.extractPatientIdFrom201305(message);
        String messageId = assertion.getMessageId();

        if (patientId != null && NullChecker.isNotNullish(messageId)) {
            correlationDao.saveOrUpdate(messageId, patientId);
        } else {
            LOG.error("Failed to retrieve patient or message id from outgoing PD deferred request.");
        }
    }

    private RespondingGatewayPRPAIN201305UV02RequestType createNewRespondingGatewayRequestForOneTarget(
        PRPAIN201305UV02 message, AssertionType assertion, NhinTargetCommunitiesType targets, String hcid) {

        PRPAIN201305UV02 new201305 = createNewPRPAIN201305UV02ForOneTargetCommunity(message, hcid);
        AssertionType newAssertion = asyncProcessHelper.copyAssertionTypeObject(assertion);
        msgUtils.generateMessageId(newAssertion);
        return msgUtils.createRespondingGatewayRequest(new201305, newAssertion, targets);
    }

    private PRPAIN201305UV02 createNewPRPAIN201305UV02ForOneTargetCommunity(PRPAIN201305UV02 message, String hcid) {

        PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, hcid);

        // Make sure the response modality and response priority codes are set as per the spec
        if (new201305.getControlActProcess() != null && new201305.getControlActProcess().getQueryByParameter() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = new201305.getControlActProcess().getQueryByParameter()
                .getValue();
            if (queryParams.getResponseModalityCode() == null) {
                queryParams.setResponseModalityCode(HL7DataTransformHelper.CSFactory("R"));
            }
            if (queryParams.getResponsePriorityCode() == null) {
                queryParams.setResponsePriorityCode(HL7DataTransformHelper.CSFactory("I"));
            }
        }

        return new201305;
    }

    private boolean isPolicyValid(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return policyChecker.checkOutgoingPolicy(request);
    }

    private MCCIIN000002UV01 sendToNhin(PRPAIN201305UV02 request, AssertionType assertion, UrlInfo urlInfo,
        String exchangeName) {
        NhinTargetSystemType targetSystemType = msgUtils
            .createNhinTargetSystemType(exchangeName, urlInfo.getUrl(), urlInfo.getHcid());

        OutboundPatientDiscoveryDeferredRequestOrchestratable orchestratable
        = new OutboundPatientDiscoveryDeferredRequestOrchestratable(delegate);
        orchestratable.setAssertion(assertion);
        orchestratable.setRequest(request);
        orchestratable.setTarget(targetSystemType);

        return ((OutboundPatientDiscoveryDeferredRequestOrchestratable) delegate
            .process(orchestratable)).getResponse();
    }
}
