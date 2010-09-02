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

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAdapterSender;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredReqOrchImpl {
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredReqOrchImpl.class);

     public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        String ackMsg = "Success";

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // Check if the Patient Discovery Async Request Service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                // Query the MPI to see if we have a match
                PRPAIN201306UV02 response = queryMpi(request, assertion);

                if (response != null &&
                        response.getControlActProcess() != null) {
                    if (checkPolicy(response, assertion)) {
                        // Send the 201305 to the Adapter Queue Interface
                        resp = sendToAgencyQueue(request, assertion);
                    } else {
                        // Send the error to the Adapter Error Interface
                        ackMsg = "Policy Check Failed";
                        log.error(ackMsg);
                        resp = sendToAgencyError(request, assertion, ackMsg);
                    }
                } else {
                    // Send the error to the Adapter Error Interface
                    ackMsg = "Invalid response from MPI";
                    log.error(ackMsg);
                    resp = sendToAgencyError(request, assertion, ackMsg);
                }
            } else {
                // Send the 201305 to the Adapter Interface
                resp = sendToAgency(request, assertion);
            }
        } else {
            // Send the error to the Adapter Error Interface
            ackMsg = "Patient Discovery Async Request Service Not Enabled";
            log.error(ackMsg);
            resp = sendToAgencyError(request, assertion, ackMsg);
        }

        // Audit the responding ack Message
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Request Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinPatientDiscoveryUtils.isServiceEnabled(NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return NhinPatientDiscoveryUtils.isInPassThroughMode(NhincConstants.PATIENT_DISCOVERY_SERVICE_ASYNC_REQ_PASSTHRU_PROPERTY);
    }

    protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapter201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredReqToAgency(request, assertion);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected MCCIIN000002UV01 sendToAgencyQueue(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapter201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendAsyncReqToAgencyQueue(request, assertion);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapter201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredReqErrorToAgency(request, assertion, errMsg);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
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

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();

        return msgProcessor.queryMpiForPatients(query, assertion);
    }

}
