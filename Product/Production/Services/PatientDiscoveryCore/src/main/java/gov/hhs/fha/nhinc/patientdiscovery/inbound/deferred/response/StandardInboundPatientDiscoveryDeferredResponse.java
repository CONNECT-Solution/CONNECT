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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory.ResponseModeType;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseMode;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.util.Properties;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardInboundPatientDiscoveryDeferredResponse extends AbstractInboundPatientDiscoveryDeferredResponse {

    private final PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;
    private final ResponseFactory responseFactory;
    private final PatientDiscovery201306Processor msgProcessor;
    private final PDDeferredCorrelationDao pdCorrelationDao;
    private final PatientDiscoveryDeferredResponseAuditLogger auditLogger;
    private static final Logger LOG = LoggerFactory.getLogger(StandardInboundPatientDiscoveryDeferredResponse.class);

    /**
     * Constructor.
     */
    public StandardInboundPatientDiscoveryDeferredResponse() {
        policyChecker = PatientDiscovery201306PolicyChecker.getInstance();
        responseFactory = new ResponseFactory();
        msgProcessor = new PatientDiscovery201306Processor();
        pdCorrelationDao = new PDDeferredCorrelationDao();
        auditLogger = new PatientDiscoveryDeferredResponseAuditLogger();
    }

    /**
     * Constructor with dependency injection arguments.
     *
     * @param policyChecker
     * @param responseFactory
     * @param msgProcessor
     * @param proxyFactory
     * @param pdCorrelationDao
     * @param auditLogger
     */
    public StandardInboundPatientDiscoveryDeferredResponse(
        PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker,
        ResponseFactory responseFactory, PatientDiscovery201306Processor msgProcessor,
        GenericFactory<AdapterPatientDiscoveryDeferredRespProxy> proxyFactory,
        PDDeferredCorrelationDao pdCorrelationDao, PatientDiscoveryDeferredResponseAuditLogger auditLogger) {
        super(proxyFactory);
        this.policyChecker = policyChecker;
        this.responseFactory = responseFactory;
        this.msgProcessor = msgProcessor;
        this.pdCorrelationDao = pdCorrelationDao;
        this.auditLogger = auditLogger;
    }

    @Override
    @InboundProcessingEvent(beforeBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class, serviceType = "Patient Discovery Deferred Response", version = "1.0")
    public MCCIIN000002UV01 respondingGatewayDeferredPRPAIN201306UV02(PRPAIN201306UV02 request, AssertionType assertion,
        Properties webContextProperties) {

        MCCIIN000002UV01 response = process(request, assertion);

        auditResponseToNhin(request, response, assertion, webContextProperties);

        return response;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.AbstractInboundPatientDiscoveryDeferredResponse#
     * process(org.hl7.v3.PRPAIN201306UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public MCCIIN000002UV01 process(PRPAIN201306UV02 request, AssertionType assertion) {
        logInfoServiceProcess(this.getClass());
        MCCIIN000002UV01 response;
        String ackMsg;

        if (isPolicyValid(request, assertion)) {

            ResponseModeType respModeType = responseFactory.getResponseModeType();
            if (respModeType != ResponseModeType.PASSTHROUGH) {
                createHCIDtoAAMapping(request);

                processResponseMode(request, assertion);
            }

            response = sendToAdapter(request, assertion);
        } else {
            ackMsg = "Policy Check Failed";
            LOG.warn(ackMsg);
            response = HL7AckTransforms.createAckErrorFrom201306(request, ackMsg);
        }

        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.AbstractInboundPatientDiscoveryDeferredResponse#
     * getAuditLogger()
     */
    @Override
    public PatientDiscoveryDeferredResponseAuditLogger getAuditLogger() {
        return auditLogger;
    }

    private boolean isPolicyValid(PRPAIN201306UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201306UV02RequestType gatewayRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
        gatewayRequest.setPRPAIN201306UV02(request);
        gatewayRequest.setAssertion(assertion);

        return policyChecker.checkOutgoingPolicy(gatewayRequest);
    }

    private void createHCIDtoAAMapping(PRPAIN201306UV02 msg) {
        msgProcessor.storeMapping(msg);
    }

    /**
     * This call will create a correlation if the patient verification passes.
     *
     * Currently only the message from the Nhin is sent to the Agency so there is no need for this method to return a
     * value as we want to send the original request.
     *
     * @param request
     * @param assertion
     */
    private void processResponseMode(PRPAIN201306UV02 request, AssertionType assertion) {
        String messageId = "";
        if (CollectionUtils.isNotEmpty(assertion.getRelatesToList())) {
            messageId = assertion.getRelatesToList().get(0);
        }

        II patientId = pdCorrelationDao.queryByMessageId(messageId);
        if (patientId != null) {
            ResponseMode respProcessor = responseFactory.getResponseMode();
            respProcessor.processResponse(request, assertion, patientId);
        }
    }
}
