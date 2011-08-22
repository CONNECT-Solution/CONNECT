/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAdapterSender;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.TrustMode;
import gov.hhs.fha.nhinc.patientdiscovery.response.VerifyMode;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredRespOrchImpl {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredRespOrchImpl.class);

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02Orch(PRPAIN201306UV02 body, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        String ackMsg = "";

        // Audit the incoming Nhin 201306 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhinDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        RespondingGatewayPRPAIN201306UV02RequestType nhinResponse = new RespondingGatewayPRPAIN201306UV02RequestType();
        nhinResponse.setPRPAIN201306UV02(body);
        nhinResponse.setAssertion(assertion);

        // Check if the Patient Discovery Async Response Service is enabled
        if (isServiceEnabled()) {
            // Perform a policy check
            if (checkPolicy(body, assertion)) {
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
        } else {
            ackMsg = "Patient Discovery Async Response Service Not Enabled";
            log.warn(ackMsg);

            // Set the error acknowledgement status
            resp = HL7AckTransforms.createAckErrorFrom201306(body, ackMsg);
        }

        // Audit the responding ack Message
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected int getResponseMode() {
        ResponseFactory respFactory = new ResponseFactory();

        return respFactory.getResponseModeType();
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Response Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinPatientDiscoveryUtils.isServiceEnabled(NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
    }

    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapterDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredRespToAgency(body, assertion);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
        // In Verify Mode:
        //    1)  Query MPI to verify the patient is a match.
        //    2)  If a match is found in MPI then proceed with the correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.

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
        //    1)  Query async database for a record corresponding to the message/relatesto id
        //    2)  If a record is found then proceed with correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.

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

    protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        II patIdOverride = new II();

        if (NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                response.getControlActProcess().getSubject().get(0) != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            patIdOverride.setExtension(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            patIdOverride.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else {
            patIdOverride = null;
        }

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
    }

    protected void storeMapping(PRPAIN201306UV02 msg) {
        PatientDiscovery201306Processor msgProcessor = new PatientDiscovery201306Processor();
        msgProcessor.storeMapping(msg);
    }

}
