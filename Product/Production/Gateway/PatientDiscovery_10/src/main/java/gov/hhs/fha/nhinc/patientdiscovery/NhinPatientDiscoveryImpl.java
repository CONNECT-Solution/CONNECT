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
package gov.hhs.fha.nhinc.patientdiscovery;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchestration;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryImpl extends WebServiceHelper {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);

    private final GenericFactory<InboundPatientDiscoveryOrchestration> orchestrationFactory;
    private final PatientDiscoveryAuditor auditLogger;

    public NhinPatientDiscoveryImpl(PatientDiscoveryAuditor auditLogger,
            GenericFactory<InboundPatientDiscoveryOrchestration> orchestrationFactory) {
        this.orchestrationFactory = orchestrationFactory;
        this.auditLogger = auditLogger;
    }

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, WebServiceContext context)
            throws PatientDiscoveryException {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        AssertionType assertion = getSamlAssertion(context);
        String targetCommunityId = getTargetCommunityId(body);

        getPerformanceManager().logPerformanceStart(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetCommunityId);

        PRPAIN201306UV02 response;
        response = respondingGatewayPRPAIN201305UV02(body, assertion);

        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetCommunityId);

        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;

    }

    protected PerformanceManager getPerformanceManager() {
        return PerformanceManager.getPerformanceManagerInstance();
    }

    private PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion)
            throws PatientDiscoveryException {
        InboundPatientDiscoveryOrchestration oOrchestrator1 = orchestrationFactory.create();
        InboundPatientDiscoveryOrchestration oOrchestrator = oOrchestrator1;

        PRPAIN201306UV02 response = oOrchestrator.respondingGatewayPRPAIN201305UV02(body, assertion);
        return response;
    }

    protected PatientDiscoveryAuditor getAuditLogger() {
        return auditLogger;
    }

    private String getTargetCommunityId(PRPAIN201305UV02 body) {
        PatientDiscoveryTransforms transforms = getTransforms();
        String targetCommunityId = transforms.getPatientDiscoveryMessageCommunityId(body,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_SYNC_TYPE, NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        return targetCommunityId;
    }

    protected PatientDiscoveryTransforms getTransforms() {
        return new PatientDiscoveryTransforms();
    }

}
