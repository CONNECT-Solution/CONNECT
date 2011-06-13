/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;

/**
 *
 * @author mflynn02
 */
public class NhincPatientDiscoveryOrchImpl {

    private Log log = null;

    public NhincPatientDiscoveryOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }

    protected void logNhincPatientDiscoveryRequest(PRPAIN201305UV02 request, AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditNhin201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void logNhincPatientDiscoveryResponse(PRPAIN201306UV02 response, AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditNhin201306(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected PRPAIN201306UV02 sendToNhinProxy(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        NhinPatientDiscoveryProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryProxyObjectFactory();
        NhinPatientDiscoveryProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryProxy();

        return proxy.respondingGatewayPRPAIN201305UV02(request, assertion, target);

    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, AssertionType assertion) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV(request, assertion) method");
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        logNhincPatientDiscoveryRequest(request.getPRPAIN201305UV02(), assertion);

        // Log the start of the adapter performance record
        String responseCommunityId = "";
        if (request != null && request.getNhinTargetSystem() != null &&
                request.getNhinTargetSystem().getHomeCommunity() != null) {
            responseCommunityId = request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId();
        }
        Timestamp starttime = new Timestamp(System.currentTimeMillis());
        Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, responseCommunityId);

        PRPAIN201306UV02 response = sendToNhinProxy(request.getPRPAIN201305UV02(), assertion, request.getNhinTargetSystem());

        // Log the end of the performance record
        Timestamp stoptime = new Timestamp(System.currentTimeMillis());
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        logNhincPatientDiscoveryResponse(response, assertion);

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV(request, assertion) method");
        return response;
    }
}
