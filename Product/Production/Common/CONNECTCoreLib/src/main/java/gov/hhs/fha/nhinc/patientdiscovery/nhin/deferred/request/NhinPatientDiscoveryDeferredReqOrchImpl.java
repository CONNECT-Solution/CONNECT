/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAdapterSender;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.policy.PatientDiscoveryPolicyTransformHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredReqOrchImpl {
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredReqOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhinDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // Check if the Patient Discovery Async Request Service is enabled
        if (isServiceEnabled()) {
            if (isInPassThroughMode()) {
                // Send the 201305 to the Adapter Interface
                resp = sendToAgency(request, assertion);
            } else {               
                resp = serviceInternal(request, assertion);
            }
        } else {
            // Send the error to the Adapter Error Interface
            String ackMsg = "Patient Discovery Deferred Request Service Not Enabled";
            log.warn(ackMsg);
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

    /**
     * Checks the policy of the passed in request.
     *
     * @param request
     * @param assertion
     * @return
     */
    protected boolean checkPolicy(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();
        return policyChecker.checkIncomingPolicy(request, assertion);
    }

    /**
     *
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return <code>MCCIIN000002UV01</code>
     */
    private MCCIIN000002UV01 serviceInternal(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 response = null;
        //HomeCommunityType hcId = new HomeCommunityType();
        String errMsg = null;

        //hcId.setHomeCommunityId(homeCommunityId);
        if (checkPolicy(request, assertion)) {
            log.debug("Adapter patient discovery deferred policy check successful");
            response = sendToAgency(request, assertion);
        } else {
            errMsg = "Policy Check Failed";
            log.error(errMsg);
            response = sendToAgencyError(request, assertion, errMsg);
        }

        return response;
    }

    protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapterDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        AdapterPatientDiscoveryDeferredReqProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredReqProxyObjectFactory();
        AdapterPatientDiscoveryDeferredReqProxy proxy = factory.getAdapterPatientDiscoveryDeferredReqProxy();

        MCCIIN000002UV01 resp = proxy.processPatientDiscoveryAsyncReq(request, assertion);
       
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapterDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredReqErrorToAgency(request, assertion, errMsg);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

}
