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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public abstract class AbstractInboundPatientDiscoveryDeferredRequest implements InboundPatientDiscoveryDeferredRequest {

    private final AdapterPatientDiscoveryDeferredReqProxyObjectFactory adapterFactory;
    
    public AbstractInboundPatientDiscoveryDeferredRequest(AdapterPatientDiscoveryDeferredReqProxyObjectFactory factory) {
        adapterFactory = factory;
    }
    
    abstract MCCIIN000002UV01 process(PRPAIN201305UV02 request, AssertionType assertion);
    
    abstract PatientDiscoveryAuditor getAuditLogger();

    /**
     * Processes the PD Deferred request message.  This call will audit the message and send it to the Nhin.
     * 
     * @param request
     * @param assertion
     * @return MCCIIN000002UV01
     */
    @InboundProcessingEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
            afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class, 
            serviceType = "Patient Discovery Deferred Request",
            version = "1.0")
    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion) {
        auditRequestFromNhin(request, assertion);

        MCCIIN000002UV01 response = process(request, assertion);

        auditResponseToNhin(response, assertion);

        return response;
    }
    
    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201305UV02 request, AssertionType assertion) {
        AdapterPatientDiscoveryDeferredReqProxy proxy = adapterFactory.getAdapterPatientDiscoveryDeferredReqProxy();
        return proxy.processPatientDiscoveryAsyncReq(request, assertion);
    }

    private void auditRequestFromNhin(PRPAIN201305UV02 request, AssertionType assertion) {
        getAuditLogger().auditNhinDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToNhin(MCCIIN000002UV01 response, AssertionType assertion) {
        getAuditLogger().auditAck(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
    }

    protected void auditRequestToAdapter(PRPAIN201305UV02 request, AssertionType assertion) {
        getAuditLogger().auditAdapterDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected void auditResponseFromAdapter(MCCIIN000002UV01 response, AssertionType assertion) {
        getAuditLogger().auditAck(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
    }

}
