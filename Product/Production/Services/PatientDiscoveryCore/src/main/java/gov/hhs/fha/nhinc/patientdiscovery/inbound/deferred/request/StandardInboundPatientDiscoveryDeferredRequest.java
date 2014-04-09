/*
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

import org.apache.log4j.Logger;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 * 
 */
public class StandardInboundPatientDiscoveryDeferredRequest extends AbstractInboundPatientDiscoveryDeferredRequest {

    private static final Logger LOG = Logger.getLogger(StandardInboundPatientDiscoveryDeferredRequest.class);
    private final PatientDiscoveryPolicyChecker policyChecker;
    private final AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory proxyErrorFactory;
    private final PatientDiscoveryAuditor auditLogger;

    /**
     * Constructor.
     */
    public StandardInboundPatientDiscoveryDeferredRequest() {
        super(new AdapterPatientDiscoveryDeferredReqProxyObjectFactory());
        policyChecker = PatientDiscoveryPolicyChecker.getInstance();
        proxyErrorFactory = new AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory();
        auditLogger = new PatientDiscoveryAuditLogger();
    }

    /**
     * Constructor.
     * 
     * @param policyChecker
     * @param proxyErrorFactory
     * @param passthroughPatientDiscovery
     * @param auditLogger
     * @param LOG
     */
    public StandardInboundPatientDiscoveryDeferredRequest(PatientDiscoveryPolicyChecker policyChecker,
            AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory proxyErrorFactory,
            AdapterPatientDiscoveryDeferredReqProxyObjectFactory adapterFactory, PatientDiscoveryAuditor auditLogger) {
        super(adapterFactory);
        this.policyChecker = policyChecker;
        this.proxyErrorFactory = proxyErrorFactory;
        this.auditLogger = auditLogger;
    }

    @Override
    @InboundProcessingEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class, afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class, serviceType = "Patient Discovery Deferred Request", version = "1.0")
    MCCIIN000002UV01 process(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 response = null;

        auditRequestToAdapter(request, assertion);

        String errMsg = null;
        if (isPolicyValid(request, assertion)) {
            LOG.debug("Adapter patient discovery deferred policy check successful");
            response = sendToAdapter(request, assertion);
        } else {
            errMsg = "Policy Check Failed";
            LOG.error(errMsg);
            response = sendErrorToAdapter(request, assertion, errMsg);
        }

        auditResponseFromAdapter(response, assertion);

        return response;
    }

    PatientDiscoveryAuditor getAuditLogger() {
        return auditLogger;
    }

    private boolean isPolicyValid(PRPAIN201305UV02 request, AssertionType assertion) {
        return policyChecker.checkIncomingPolicy(request, assertion);
    }

    private MCCIIN000002UV01 sendErrorToAdapter(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request);

        MCCIIN000002UV01 adapterResp = proxyErrorFactory.create().processPatientDiscoveryAsyncReqError(request,
                response, assertion, errMsg);

        return adapterResp;
    }
}
