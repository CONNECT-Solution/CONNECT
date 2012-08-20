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
package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;

/**
 *
 * @author westberg
 */
public class NhinPatientDiscoveryOrchImpl implements InboundPatientDiscoveryOrchestration {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryOrchImpl.class);

    private final ServicePropertyAccessor servicePropertyAccessor;

    private final PatientDiscoveryAuditor auditLogger;

    private final PatientDiscoveryProcessor patientDiscoveryProcessor;

    private final GenericFactory<AdapterPatientDiscoveryProxy> proxyFactory;

    NhinPatientDiscoveryOrchImpl(ServicePropertyAccessor servicePropertyAccessor, PatientDiscoveryAuditor auditLogger,
            PatientDiscoveryProcessor patientDiscoveryProcessor,
            GenericFactory<AdapterPatientDiscoveryProxy> proxyFactory) {
        this.servicePropertyAccessor = servicePropertyAccessor;
        this.auditLogger = auditLogger;
        this.patientDiscoveryProcessor = patientDiscoveryProcessor;
        this.proxyFactory = proxyFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchestration#respondingGatewayPRPAIN201305UV02
     * (org.hl7.v3.PRPAIN201305UV02, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
     */
    @Override
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion)
            throws PatientDiscoveryException {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        // Check if the Patient Discovery Service is enabled
        if (isServiceEnabled()) {

            response = auditAndProcess(body, assertion, auditLogger);
        }

        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }

    /**
     * @param body
     * @param assertion
     * @param auditLogger
     * @return
     * @throws PatientDiscoveryException
     */
    protected PRPAIN201306UV02 auditAndProcess(PRPAIN201305UV02 body, AssertionType assertion,
            PatientDiscoveryAuditor auditLogger) throws PatientDiscoveryException {
        PRPAIN201306UV02 response;

        // Audit the incoming Nhin 201305 Message
        auditLogger.auditNhin201305(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        response = process(body, assertion);

        // Audit the outgoing Nhin 201306 Message - response that came
        // back from the adapter.
        auditLogger.auditNhin201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        return response;
    }

    /**
     * @param body
     * @param assertion
     * @return
     * @throws PatientDiscoveryException
     */
    protected PRPAIN201306UV02 process(PRPAIN201305UV02 body, AssertionType assertion) throws PatientDiscoveryException {
        PRPAIN201306UV02 response;
        // Check if in Pass-Through Mode
        if (isInPassThroughMode()) {
            response = send201305ToAgency(body, assertion);
        } else {
            response = process201305(body, assertion);
        }
        return response;
    }

    protected PRPAIN201306UV02 send201305ToAgency(PRPAIN201305UV02 request, AssertionType assertion)
            throws PatientDiscoveryException {
        AdapterPatientDiscoveryProxy proxy = proxyFactory.create();
        PRPAIN201306UV02 adapterResp = proxy.respondingGatewayPRPAIN201305UV02(request, assertion);
        return adapterResp;
    }

    protected PRPAIN201306UV02 process201305(PRPAIN201305UV02 body, AssertionType assertion)
            throws PatientDiscoveryException {
        PRPAIN201306UV02 adapterResp = null;
        auditLogger.auditAdapter201305(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        adapterResp = patientDiscoveryProcessor.process201305(body, assertion);
        auditLogger.auditAdapter201306(adapterResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        return adapterResp;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Service is enabled.
     *
     * @return Returns true if the servicePatientDiscovery is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return servicePropertyAccessor.isServiceEnabled();
    }

    /**
     * Checks to see if the query should be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthrough property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return servicePropertyAccessor.isInPassThroughMode();
    }

}
