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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAdapterSender {

    public PRPAIN201306UV02 send201305ToAgency(PRPAIN201305UV02 request, AssertionType assertion)
            throws PatientDiscoveryException {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryProxyObjectFactory factory = new AdapterPatientDiscoveryProxyObjectFactory();
        AdapterPatientDiscoveryProxy proxy = factory.getAdapterPatientDiscoveryProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        PRPAIN201306UV02 adapterResp = proxy.respondingGatewayPRPAIN201305UV02(adapterReq.getPRPAIN201305UV02(),
                adapterReq.getAssertion());

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredReqErrorToAgency(PRPAIN201305UV02 request, AssertionType assertion,
            String errMsg) {
        AsyncAdapterPatientDiscoveryErrorRequestType adapterReq = new AsyncAdapterPatientDiscoveryErrorRequestType();

        AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory();
        AdapterPatientDiscoveryDeferredReqErrorProxy proxy = factory.create();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        adapterReq.setErrorMsg(errMsg);
        PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request);

        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncReqError(request, response, assertion, errMsg);

        return adapterResp;
    }

    public MCCIIN000002UV01 sendDeferredRespToAgency(PRPAIN201306UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201306UV02RequestType adapterReq = new RespondingGatewayPRPAIN201306UV02RequestType();

        AdapterPatientDiscoveryDeferredRespProxyObjectFactory factory = new AdapterPatientDiscoveryDeferredRespProxyObjectFactory();
        AdapterPatientDiscoveryDeferredRespProxy proxy = factory.getAdapterPatientDiscoveryDeferredRespProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201306UV02(request);
        MCCIIN000002UV01 adapterResp = proxy.processPatientDiscoveryAsyncResp(request, assertion);

        return adapterResp;
    }

}
