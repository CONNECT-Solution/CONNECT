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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.TrustMode;
import gov.hhs.fha.nhinc.patientdiscovery.response.VerifyMode;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredRespOrchImpl {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredRespOrchImpl.class);
    private final PatientDiscoveryAuditor auditLogger;
    private final GenericFactory<AdapterPatientDiscoveryDeferredRespProxy> proxyFactory;
    private final PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;

    public NhinPatientDiscoveryDeferredRespOrchImpl(ServicePropertyAccessor servicePropertyAccessor,
            PatientDiscoveryAuditor auditLogger, GenericFactory<AdapterPatientDiscoveryDeferredRespProxy> proxyFactory,
            PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker) {
        super();
        this.auditLogger = auditLogger;
        this.proxyFactory = proxyFactory;
        this.policyChecker = policyChecker;
    }

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02Orch(PRPAIN201306UV02 body, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        String ackMsg = "";

        // Audit the incoming Nhin 201306 Message
        auditLogger.auditNhinDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        RespondingGatewayPRPAIN201306UV02RequestType nhinResponse = new RespondingGatewayPRPAIN201306UV02RequestType();
        nhinResponse.setPRPAIN201306UV02(body);
        nhinResponse.setAssertion(assertion);

        // Perform a policy check
        if (checkPolicy(nhinResponse)) {
            // Obtain the response mode in order to determine how the message is to be processed
            int respModeType = getResponseMode();

            if (respModeType == ResponseFactory.PASSTHRU_MODE) {
                // Nothing to do here, empty target to cover the passthrough case
            } else if (respModeType == ResponseFactory.TRUST_MODE) {
                // Store AA to HCID Mapping
                storeMapping(body);

                processRespTrustMode(body, assertion);
            } else {
                // Store AA to HCID Mapping
                storeMapping(body);

                // Default is Verify Mode
                processRespVerifyMode(body, assertion);
            }

            resp = sendToAdapter(body, assertion);
        } else {
            ackMsg = "Policy Check Failed for incoming Patient Discovery Deferred Response";
            log.warn(ackMsg);

            // Set the error acknowledgement status
            resp = HL7AckTransforms.createAckErrorFrom201306(body, ackMsg);
        }

        // Audit the responding ack Message
        auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected int getResponseMode() {
        ResponseFactory respFactory = new ResponseFactory();

        return respFactory.getResponseModeType();
    }

    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
        auditLogger.auditAdapterDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        AdapterPatientDiscoveryDeferredRespProxy proxy = proxyFactory.create();

        MCCIIN000002UV01 resp = proxy.processPatientDiscoveryAsyncResp(body, assertion);

        auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
        // In Verify Mode:
        // 1) Query MPI to verify the patient is a match.
        // 2) If a match is found in MPI then proceed with the correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        // need for this method to return a value.

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        PDDeferredCorrelationDao pdCorrelationDao = new PDDeferredCorrelationDao();
        II patientId = pdCorrelationDao.queryByMessageId(messageId);
        if (patientId != null) {
            VerifyMode respProcessor = new VerifyMode();
            respProcessor.processResponse(patientId, body, assertion);
        }
    }

    protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
        // In Trust Mode:
        // 1) Query async database for a record corresponding to the message/relatesto id
        // 2) If a record is found then proceed with correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        // need for this method to return a value.

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        PDDeferredCorrelationDao pdCorrelationDao = new PDDeferredCorrelationDao();
        II patientId = pdCorrelationDao.queryByMessageId(messageId);
        if (patientId != null) {
            TrustMode respProcessor = new TrustMode();
            respProcessor.processResponse(body, assertion, patientId);
        }
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201306UV02RequestType request) {
        return policyChecker.checkOutgoingPolicy(request);
    }

    protected void storeMapping(PRPAIN201306UV02 msg) {
        PatientDiscovery201306Processor msgProcessor = new PatientDiscovery201306Processor();
        msgProcessor.storeMapping(msg);
    }

}
