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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreqerror.AdapterPatientDiscoveryAsyncReqErrorPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * 
 * @author jhoppesc
 */
public class AdapterPatientDiscoveryDeferredReqErrorProxyWebServiceUnsecuredImpl implements
        AdapterPatientDiscoveryDeferredReqErrorProxy {
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPatientDiscoveryDeferredReqErrorProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(PRPAIN201305UV02 request, PRPAIN201306UV02 response,
            AssertionType assertion, String errMsg) {
        log.debug("Begin processPatientDiscoveryAsyncReqError");
        MCCIIN000002UV01 ack = null;

        try {
            String url = oProxyHelper
                    .getAdapterEndPointFromConnectionManager(NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_ERROR_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {

                if (request == null) {
                    log.error("Request was null");
                } else if (response == null) {
                    log.error("Response was null");
                } else if (assertion == null) {
                    log.error("assertion was null");
                } else if (NullChecker.isNullish(errMsg)) {
                    log.error("targets was null");
                } else {
                    ServicePortDescriptor<AdapterPatientDiscoveryAsyncReqErrorPortType> portDescriptor = new PatientDiscoveryDeferredReqErrorUnsecuredServicePortDescriptor();
                    CONNECTClient<AdapterPatientDiscoveryAsyncReqErrorPortType> client = CONNECTClientFactory
                            .getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
                    AsyncAdapterPatientDiscoveryErrorRequestType msg = new AsyncAdapterPatientDiscoveryErrorRequestType();
                    msg.setAssertion(assertion);
                    msg.setPRPAIN201305UV02(request);
                    msg.setPRPAIN201306UV02(response);
                    msg.setErrorMsg(errMsg);

                    ack = (MCCIIN000002UV01) client.invokePort(AdapterPatientDiscoveryAsyncReqErrorPortType.class,
                            "processPatientDiscoveryAsyncReqError", msg);
                }
            } else {
                log.error("Failed to call the web service ("
                        + NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_ERROR_SERVICE_NAME
                        + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error calling processPatientDiscoveryAsyncReqError: " + ex.getMessage(), ex);
            ack = HL7AckTransforms.createAckFrom201305(request, errMsg);
        }

        log.debug("End processPatientDiscoveryAsyncReqError");
        return ack;
    }

}
