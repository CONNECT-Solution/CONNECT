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
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

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


        PRPAIN201306UV02 response = sendToNhinProxy(request.getPRPAIN201305UV02(), assertion, request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        logNhincPatientDiscoveryResponse(response, assertion);

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV(request, assertion) method");
        return response;
    }
}
