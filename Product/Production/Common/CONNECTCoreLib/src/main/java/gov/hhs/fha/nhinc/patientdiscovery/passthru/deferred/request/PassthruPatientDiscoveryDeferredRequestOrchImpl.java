/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxyObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class PassthruPatientDiscoveryDeferredRequestOrchImpl
{

    private Log log = null;

    public PassthruPatientDiscoveryDeferredRequestOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PatientDiscoveryAuditLogger createAuditLogger()
    {
        return new PatientDiscoveryAuditLogger();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message,
            AssertionType assertion, NhinTargetSystemType targets)
    {
        log.debug("Entering PassthruPatientDiscoveryDeferredRequestOrchImpl.processPatientDiscoveryAsyncReq with message: "
                + message + " assertion: " + assertion + " targets: " + targets);
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        //PatientDiscoveryAuditLogger auditLog = createAuditLogger();

        //AcknowledgementType ack = auditLog.auditNhin201305(message, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryDeferredReqProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryDeferredReqProxyObjectFactory();
        NhinPatientDiscoveryDeferredReqProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncReqProxy();

        log.debug("Invoking " + proxy + ".respondingGatewayPRPAIN201305UV02 with message: "
                + message + " assertion: " + assertion + " targets: " + targets);
        response = proxy.respondingGatewayPRPAIN201305UV02(message, assertion, targets);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        //ack = auditLog.auditAck(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting PassthruPatientDiscoveryDeferredRequestOrchImpl.processPatientDiscoveryAsyncReq with response: " + response);
        return response;
    }
}
