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
package gov.hhs.fha.nhinc.patientdiscovery.outbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinCallableRequest;
import gov.hhs.fha.nhinc.gateway.executorservice.NhinTaskExecutor;
import gov.hhs.fha.nhinc.logging.transaction.TransactionLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02ArgTransformer;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.RespondingGatewayPRPAIN201306UV02Builder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryOrchestratable;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryProcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardOutboundPatientDiscovery implements OutboundPatientDiscovery {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundPatientDiscovery.class);
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;
    private final TransactionLogger transactionLogger = new TransactionLogger();

    /**
     * Add default constructor that is used by test cases Note that implementations should always use constructor that
     * takes the executor services as input
     */
    public StandardOutboundPatientDiscovery() {
        regularExecutor = Executors.newFixedThreadPool(1);
        largejobExecutor = Executors.newFixedThreadPool(1);
    }

    /**
     * We construct the orch impl class with references to both executor services that could be used for this particular
     * orchestration instance. Determination of which executor service to use (largejob or regular) is based on the size
     * of the pdlist and configs
     *
     * @param e
     * @param le
     */
    public StandardOutboundPatientDiscovery(final ExecutorService e, final ExecutorService le) {
        setExecutorService(e, le);
    }

    /**
     * Sets the executor services to be used for fan out.
     *
     * @param regularExecutor
     * @param largeJobExecutor
     */
    @Override
    public void setExecutorService(final ExecutorService regularExecutor, final ExecutorService largeJobExecutor) {
        this.regularExecutor = regularExecutor;
        largejobExecutor = largeJobExecutor;
    }

    @Override
    @OutboundProcessingEvent(beforeBuilder = PRPAIN201305UV02ArgTransformer.class, afterReturningBuilder
    = RespondingGatewayPRPAIN201306UV02Builder.class, serviceType = "Patient Discovery", version = "1.0")
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
        final RespondingGatewayPRPAIN201305UV02RequestType request, final AssertionType assertion) {
        logInfoServiceProcess(this.getClass());

        LOG.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try {
            if (request == null) {
                throw new IllegalArgumentException("PatientDiscovery RespondingGatewayPRPAIN201305UV02RequestType request was null.");
            } else if (assertion == null) {
                throw new IllegalArgumentException("Assertion was null.");
            } else if (request.getPRPAIN201305UV02() == null) {
                throw new IllegalArgumentException("PatientDiscovery PRPAIN201305UV02 request was null.");
            } else {
                response = getResponseFromCommunities(request, assertion);
            }
        } catch (final IllegalArgumentException e) {
            addErrorMessageToResponse(request, response, e);
            throw new ErrorEventException(e, response, "Unable to obtain response from communities");
        }
        LOG.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

    /**
     * @param request
     * @param response
     * @param e
     */
    protected void addErrorMessageToResponse(final RespondingGatewayPRPAIN201305UV02RequestType request,
        final RespondingGatewayPRPAIN201306UV02ResponseType response, final Exception e) {
        final CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
        communityResponse.setPRPAIN201306UV02(
            new HL7PRPA201306Transforms().createPRPA201306ForErrors(request.getPRPAIN201305UV02(), e.getMessage()));
        response.getCommunityResponse().add(communityResponse);
    }

    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(
        final RespondingGatewayPRPAIN201305UV02RequestType request, final AssertionType assertion) {
        LOG.debug("Begin getResponseFromCommunities");

        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        final NhincConstants.GATEWAY_API_LEVEL gatewayLevel = getGatewayVersion();
        try {
            final List<UrlInfo> urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            if (NullChecker.isNullish(urlInfoList)) {
                throw new Exception("No target endpoints were found for the Patient Discovery Request.");
            } else {
                final List<NhinCallableRequest<OutboundPatientDiscoveryOrchestratable>> callableList = new ArrayList<>();
                final String transactionId = UUID.randomUUID().toString();

                // we hold the error messages for any failed policy checks in policyErrList
                final List<CommunityPRPAIN201306UV02ResponseType> policyErrList = new ArrayList<>();

                // loop through the communities and send request if results were not null
                for (final UrlInfo urlInfo : urlInfoList) {
                    final NhinTargetSystemType target = createNhinTargetSystemType(urlInfo.getHcid(), request.
                        getNhinTargetCommunities());

                    // create a new request to send out to each target community
                    final RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRequest(request, assertion,
                        urlInfo, urlInfoList.size());

                    if (checkPolicy(newRequest)) {
                        setHomeCommunityIdInRequest(newRequest, urlInfo.getHcid());

                        logTransaction(assertion.getMessageId(), newRequest.getAssertion().getMessageId());

                        final OutboundPatientDiscoveryOrchestratable message = createOrchestratable(
                            newRequest.getPRPAIN201305UV02(), newRequest.getAssertion(), target, gatewayLevel);
                        auditRequest(message.getRequest(), message.getAssertion(), message.getTarget());
                        callableList.add(new NhinCallableRequest<OutboundPatientDiscoveryOrchestratable>(message));

                        LOG.debug("Added NhinCallableRequest" + " for hcid="
                            + target.getHomeCommunity().getHomeCommunityId());
                    } else {
                        LOG.debug("Policy Check Failed for homeId=" + urlInfo.getHcid());
                        final CommunityPRPAIN201306UV02ResponseType communityResponse
                        = createFailedPolicyCommunityResponseFromRequest(
                            request.getPRPAIN201305UV02(), urlInfo.getHcid());

                        policyErrList.add(communityResponse);
                    }
                }
                if (CollectionUtils.isNotEmpty(callableList)) {
                    LOG.debug("Executing tasks to concurrently retrieve responses");
                    final NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable, OutboundPatientDiscoveryOrchestratable> pdExecutor
                    = new NhinTaskExecutor<>(
                        ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(callableList.size())
                        ? largejobExecutor : regularExecutor,
                            callableList, transactionId);
                    pdExecutor.executeTask();
                    LOG.debug("Aggregating all responses");
                    response = getCumulativeResponse(pdExecutor);
                }

                addPolicyErrorsToResponse(response, policyErrList);
            }
        } catch (final Exception e) {
            addErrorMessageToResponse(request, response, e);
            throw new ErrorEventException(e, response, "Exception while getting responses from communities");
        }

        LOG.debug("Exiting getResponseFromCommunities");
        return response;
    }

    /**
     * Returns the Gateway version to use.
     *
     * @return the Gateway version
     */
    protected GATEWAY_API_LEVEL getGatewayVersion() {
        return new NhinEndpointManager().getApiVersion(getLocalHomeCommunityId(),
            NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
    }

    protected NhinTargetSystemType createNhinTargetSystemType(final String hcid, NhinTargetCommunitiesType nhinTargets) {
        final NhinTargetSystemType target = new NhinTargetSystemType();
        final HomeCommunityType targetCommunity = new HomeCommunityType();
        targetCommunity.setHomeCommunityId(hcid);
        target.setHomeCommunity(targetCommunity);
        if (null != nhinTargets) {
            target.setExchangeName(nhinTargets.getExchangeName());
        }

        return target;
    }

    protected void setHomeCommunityIdInRequest(final RespondingGatewayPRPAIN201305UV02RequestType request,
        final String hcid) {
        if (request.getPRPAIN201305UV02() != null && request.getPRPAIN201305UV02().getReceiver() != null
            && request.getPRPAIN201305UV02().getReceiver().get(0) != null
            && request.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
            && request.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
            && request.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null) {

            request.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0)
            .setRoot(HomeCommunityMap.formatHomeCommunityId(hcid));
        }
    }

    protected OutboundPatientDiscoveryOrchestratable createOrchestratable(final PRPAIN201305UV02 message,
        final AssertionType assertion, final NhinTargetSystemType target,
        final NhincConstants.GATEWAY_API_LEVEL gatewayLevel) {
        final OutboundDelegate nd = new OutboundPatientDiscoveryDelegate();
        final OutboundResponseProcessor np = new OutboundPatientDiscoveryProcessor(gatewayLevel);
        final OutboundPatientDiscoveryOrchestratable orchestratable = new OutboundPatientDiscoveryOrchestratable(nd,
            Optional.of(np), null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, target, message);

        return orchestratable;
    }

    protected CommunityPRPAIN201306UV02ResponseType createFailedPolicyCommunityResponseFromRequest(
        final PRPAIN201305UV02 message, final String hcid) {
        final CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
        final NhinTargetCommunityType tc = new NhinTargetCommunityType();
        final HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(hcid);
        tc.setHomeCommunity(home);
        communityResponse.setNhinTargetCommunity(tc);
        communityResponse.setPRPAIN201306UV02(new HL7PRPA201306Transforms().createPRPA201306ForErrors(message,
            "Policy Check Failed for homeId=" + hcid));

        return communityResponse;
    }

    protected RespondingGatewayPRPAIN201306UV02ResponseType getCumulativeResponse(
        final NhinTaskExecutor<OutboundPatientDiscoveryOrchestratable, OutboundPatientDiscoveryOrchestratable> dqexecutor) {
        final OutboundPatientDiscoveryOrchestratable orchResponse = dqexecutor.getFinalResponse();
        return orchResponse.getCumulativeResponse();
    }

    protected void addPolicyErrorsToResponse(final RespondingGatewayPRPAIN201306UV02ResponseType response,
        final List<CommunityPRPAIN201306UV02ResponseType> policyErrList) {
        for (final CommunityPRPAIN201306UV02ResponseType policyError : policyErrList) {
            response.getCommunityResponse().add(policyError);
        }
    }

    /**
     * Policy Check verification done here....from connect code
     *
     * @param request
     * @return
     */
    protected boolean checkPolicy(final RespondingGatewayPRPAIN201305UV02RequestType request) {
        return PatientDiscoveryPolicyChecker.getInstance().checkOutgoingPolicy(request);
    }

    /**
     * Create a new RespondingGatewayPRPAIN201305UV02RequestType which has a new PRPAIN201305UV02 cloned from the
     * original. This request will have a cloned assertion with the same message id if numTargets == 1 and a new message
     * id otherwise.
     *
     * @param request the request to be cloned
     * @param assertion the assertion to be cloned
     * @param urlInfo the url info to use
     * @param numTargets the number of total outbound targets of the originating request
     * @return new RespondingGatewayPRPAIN201305UV02RequestType
     */
    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
        final RespondingGatewayPRPAIN201305UV02RequestType request, final AssertionType assertion,
        final UrlInfo urlInfo, final int numTargets) {

        AssertionType newAssertion;
        if (numTargets == 1) {
            newAssertion = MessageGeneratorUtils.getInstance()
                .clone(MessageGeneratorUtils.getInstance().generateMessageId(assertion));
        } else {
            newAssertion = MessageGeneratorUtils.getInstance().cloneWithNewMsgId(assertion);
        }

        return createNewRequest(request, newAssertion, urlInfo);
    }

    /**
     * Create a new RespondingGatewayPRPAIN201305UV02RequestType which has a new PRPAIN201305UV02 cloned from the
     * original. This call will NOT clone the passed in assertion and instead will use it immediately for the request.
     *
     * @param request the request to be cloned
     * @param assertion the assertion to be used to the request
     * @param urlInfo the url info to use
     * @return new RespondingGatewayPRPAIN201305UV02RequestType
     */
    private RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
        final RespondingGatewayPRPAIN201305UV02RequestType request, final AssertionType newAssertion,
        final UrlInfo urlInfo) {
        final RespondingGatewayPRPAIN201305UV02RequestType newRequest
        = new RespondingGatewayPRPAIN201305UV02RequestType();

        final PRPAIN201305UV02 new201305 = new PatientDiscovery201305Processor()
            .createNewRequest(cloneRequest(request.getPRPAIN201305UV02()), urlInfo.getHcid());

        // Make sure the response modality and response priority codes are set as per the spec
        if (new201305.getControlActProcess() != null
            && new201305.getControlActProcess().getQueryByParameter() != null) {

            final PRPAMT201306UV02QueryByParameter queryParams = new201305.getControlActProcess().getQueryByParameter()
                .getValue();
            if (queryParams.getResponseModalityCode() == null) {
                queryParams.setResponseModalityCode(HL7DataTransformHelper.CSFactory("R"));
            }
            if (queryParams.getResponsePriorityCode() == null) {
                queryParams.setResponsePriorityCode(HL7DataTransformHelper.CSFactory("I"));
            }
        }

        newRequest.setAssertion(newAssertion);
        newRequest.setPRPAIN201305UV02(new201305);
        newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        return newRequest;
    }

    private static PRPAIN201305UV02 cloneRequest(final PRPAIN201305UV02 request) {
        return MessageGeneratorUtils.getInstance().clone(request);
    }

    /**
     * Log the transaction of the new request message id, but only if it's not the same as the related message id.
     *
     * @param relatedMessageId the message id of a previous transaction that the request message id should be a part of
     * @param requestMessageId the message id to be logged
     */
    private void logTransaction(final String relatedMessageId, final String requestMessageId) {
        if (!relatedMessageId.equals(requestMessageId)) {
            transactionLogger.logTransactionFromRelatedMessageId(relatedMessageId, requestMessageId);
        }
    }

    protected List<UrlInfo> getEndpoints(final NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;
        try {
            urlInfoList = ExchangeManager.getInstance().getEndpointURLFromNhinTargetCommunities(
                targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (final ExchangeManagerException ex) {
            LOG.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;
        try {
            sHomeCommunity = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (final PropertyAccessException ex) {
            LOG.error("Error while retrieving the HomeCommunityId: {}", ex.getMessage(), ex);
        }
        return sHomeCommunity;
    }

    /**
     * @return a new instance of PatientDiscoveryAuditLogger
     */
    protected PatientDiscoveryAuditLogger getAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    private void auditRequest(final PRPAIN201305UV02 request, final AssertionType assertion,
        final NhinTargetSystemType target) {
        getAuditLogger().auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null,
            NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
    }
}
