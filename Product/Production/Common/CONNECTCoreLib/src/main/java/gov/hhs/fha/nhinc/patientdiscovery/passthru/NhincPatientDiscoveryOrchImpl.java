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
package gov.hhs.fha.nhinc.patientdiscovery.passthru;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryOrchestratable;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

/**
 *
 * @author mflynn02
 * @paul.eftis updated exception handling to return PD error response with error/exception detail within PD response.
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
        getPatientDiscoveryAuditLogger().auditNhin201305(request, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void logNhincPatientDiscoveryResponse(PRPAIN201306UV02 response, AssertionType assertion) {
        getPatientDiscoveryAuditLogger().auditNhin201306(response, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected PRPAIN201306UV02 sendToNhinProxy(PRPAIN201305UV02 request, AssertionType assertion,
            NhinTargetSystemType target) {
        try {
            OutboundPatientDiscoveryDelegate delegate = new OutboundPatientDiscoveryDelegate();
            OutboundPatientDiscoveryOrchestratable inMessage = new OutboundPatientDiscoveryOrchestratable(delegate,
                    null, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, target, request);
            OutboundPatientDiscoveryOrchestratable outMessage = delegate.process(inMessage);
            return outMessage.getResponse();
        } catch (Exception ex) {
            log.error("Passthru NhinpatientDiscoveryOrchImpl Exception", ex);
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, target,
                    NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            return generateErrorResponse(target, request, err);
        }
    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request,
            AssertionType assertion) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV(request, assertion) method");
        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        logNhincPatientDiscoveryRequest(request.getPRPAIN201305UV02(), assertion);

        PRPAIN201306UV02 response = sendToNhinProxy(request.getPRPAIN201305UV02(), assertion,
                request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        logNhincPatientDiscoveryResponse(response, assertion);

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV(request, assertion) method");
        return response;
    }

    private PRPAIN201306UV02 generateErrorResponse(NhinTargetSystemType target, PRPAIN201305UV02 request, String error) {
        String errStr = "Error from target homeId=" + target.getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        return (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request, errStr);
    }

}
