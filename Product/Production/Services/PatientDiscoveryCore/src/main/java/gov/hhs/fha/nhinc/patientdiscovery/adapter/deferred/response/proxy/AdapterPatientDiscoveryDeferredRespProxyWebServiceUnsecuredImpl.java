/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncresp.AdapterPatientDiscoveryAsyncRespPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.service.AdapterPatientDiscoveryAsyncRespServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl implements
        AdapterPatientDiscoveryDeferredRespProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    @AdapterDelegationEvent(beforeBuilder = PRPAIN201306UV02EventDescriptionBuilder.class,
            afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class,
            serviceType = "Patient Discovery Deferred Response",
            version = "1.0")
    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion) {
        LOG.debug("Begin processPatientDiscoveryAsyncReqError");
        MCCIIN000002UV01 ack = null;

        try {
            String url = oProxyHelper
                    .getAdapterEndPointFromConnectionManager(NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_RESP_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {
                if (request == null) {
                    LOG.error("Request was null");
                } else if (assertion == null) {
                    LOG.error("assertion was null");
                } else {
                    ServicePortDescriptor<AdapterPatientDiscoveryAsyncRespPortType> portDescriptor =
                            new AdapterPatientDiscoveryAsyncRespServicePortDescriptor();
                    CONNECTClient<AdapterPatientDiscoveryAsyncRespPortType> client = CONNECTClientFactory.getInstance()
                            .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    RespondingGatewayPRPAIN201306UV02RequestType msg =
                            new RespondingGatewayPRPAIN201306UV02RequestType();
                    msg.setAssertion(assertion);
                    msg.setPRPAIN201306UV02(request);

                    ack = (MCCIIN000002UV01) client.invokePort(AdapterPatientDiscoveryAsyncRespPortType.class,
                            "processPatientDiscoveryAsyncResp", msg);
                }
            } else {
                LOG.error("Failed to call the web service ("
                        + NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_RESP_SERVICE_NAME + ").  The URL is null.");
            }
        } catch (Exception ex) {
            LOG.error("Error calling processPatientDiscoveryAsyncResp: " + ex.getMessage(), ex);
            ack = HL7AckTransforms.createAckFrom201306(request,
                    NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
        }

        LOG.debug("End processPatientDiscoveryAsyncReqError");
        return ack;
    }

}
