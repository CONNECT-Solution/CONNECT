/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredResponseAuditLogger;
import java.util.Properties;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 *
 */
public abstract class AbstractInboundPatientDiscoveryDeferredResponse implements
    InboundPatientDiscoveryDeferredResponse {

    private final GenericFactory<AdapterPatientDiscoveryDeferredRespProxy> proxyFactory;

    public abstract MCCIIN000002UV01 process(PRPAIN201306UV02 request, AssertionType assertion);

    public abstract PatientDiscoveryDeferredResponseAuditLogger getAuditLogger();

    public AbstractInboundPatientDiscoveryDeferredResponse() {
        proxyFactory = new AdapterPatientDiscoveryDeferredRespProxyObjectFactory();
    }

    public AbstractInboundPatientDiscoveryDeferredResponse(
        GenericFactory<AdapterPatientDiscoveryDeferredRespProxy> factory) {
        proxyFactory = factory;
    }

    @Override
    public MCCIIN000002UV01 respondingGatewayDeferredPRPAIN201306UV02(PRPAIN201306UV02 request, AssertionType assertion,
        Properties webContextProperties) {

        MCCIIN000002UV01 response = process(request, assertion);

        auditResponseToNhin(request, response, assertion, webContextProperties);

        return response;
    }

    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 request, AssertionType assertion) {
        AdapterPatientDiscoveryDeferredRespProxy proxy = proxyFactory.create();

        return proxy.processPatientDiscoveryAsyncResp(request, assertion);
    }

    protected void auditResponseToNhin(PRPAIN201306UV02 request, MCCIIN000002UV01 response, AssertionType assertion,
        Properties webContextProperties) {
        getAuditLogger().auditResponseMessage(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
    }

}
