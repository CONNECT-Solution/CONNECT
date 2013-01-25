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
package gov.hhs.fha.nhinc.patientdiscovery.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 * 
 */
public class PassthroughInboundPatientDiscovery extends AbstractInboundPatientDiscovery {

    private final AdapterPatientDiscoveryProxyObjectFactory adapterFactory;
    private final PatientDiscoveryAuditor auditLogger;

    /**
     * Constructor.
     */
    public PassthroughInboundPatientDiscovery() {
        adapterFactory = new AdapterPatientDiscoveryProxyObjectFactory();
        auditLogger = new PatientDiscoveryAuditLogger();
    }

    /**
     * Constructor.
     * 
     * @param adapterFactory
     * @param auditLogger
     */
    public PassthroughInboundPatientDiscovery(AdapterPatientDiscoveryProxyObjectFactory adapterFactory,
            PatientDiscoveryAuditor auditLogger) {
        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    @Override
    PRPAIN201306UV02 process(PRPAIN201305UV02 body, AssertionType assertion) throws PatientDiscoveryException {
        PRPAIN201306UV02 response = sendToAdapter(body, assertion);

        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.patientdiscovery.inbound.AbstractInboundPatientDiscovery#getAuditLogger()
     */
    @Override
    PatientDiscoveryAuditor getAuditLogger() {
        return auditLogger;
    }

    private PRPAIN201306UV02 sendToAdapter(PRPAIN201305UV02 request, AssertionType assertion)
            throws PatientDiscoveryException {
        AdapterPatientDiscoveryProxy proxy = adapterFactory.create();
        return proxy.respondingGatewayPRPAIN201305UV02(request, assertion);
    }

}
