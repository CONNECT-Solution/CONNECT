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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.adapter.deferred.queue.service.AdapterPatientDiscoveryDeferredReqQueueProcessServicePortDescriptor;
import gov.hhs.fha.nhinc.adapterpatientdiscoveryreqqueueprocess.AdapterPatientDiscoveryDeferredReqQueueProcessPortType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.SuccessOrFailType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class PatientDiscoveryDeferredReqQueueClient {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscoveryDeferredReqQueueClient.class);
    private static final String SERVICE_NAME
        = NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_QUEUE_PROCESS_SERVICE_NAME;

    private final WebServiceProxyHelper proxyHelper;

    /**
     * Default constructor.
     */
    public PatientDiscoveryDeferredReqQueueClient() {
        proxyHelper = new WebServiceProxyHelper();
    }

    /**
     * Send queue process request for a deferred patient discovery queue record.
     *
     * @param messageId takes in MessageID
     * @return queue process response
     */
    public PatientDiscoveryDeferredReqQueueProcessResponseType
        processPatientDiscoveryDeferredReqQueue(String messageId) {
        String msgText;

        PatientDiscoveryDeferredReqQueueProcessResponseType response
            = new PatientDiscoveryDeferredReqQueueProcessResponseType();
        SuccessOrFailType sof = new SuccessOrFailType();
        sof.setSuccess(Boolean.FALSE);
        response.setSuccessOrFail(sof);

        try {

            String endpointURL = getUrl(SERVICE_NAME);

            if (NullChecker.isNotNullish(endpointURL)) {
                ServicePortDescriptor<AdapterPatientDiscoveryDeferredReqQueueProcessPortType> portDescriptor
                    = new AdapterPatientDiscoveryDeferredReqQueueProcessServicePortDescriptor();
                CONNECTClient<AdapterPatientDiscoveryDeferredReqQueueProcessPortType> client = CONNECTClientFactory.
                    getInstance().getCONNECTClientUnsecured(portDescriptor, endpointURL, null);

                PatientDiscoveryDeferredReqQueueProcessRequestType request
                    = new PatientDiscoveryDeferredReqQueueProcessRequestType();
                request.setMessageId(messageId);
                response = (PatientDiscoveryDeferredReqQueueProcessResponseType) client.invokePort(
                    AdapterPatientDiscoveryDeferredReqQueueProcessPortType.class,
                    "processPatientDiscoveryDeferredReqQueue", request);
            } else {
                msgText = "Endpoint URL not found for local home community service name ["
                    + NhincConstants.PATIENT_DISCOVERY_ADAPTER_ASYNC_REQ_QUEUE_PROCESS_SERVICE_NAME + "]";
                LOG.error(msgText);
                response.setResponse(msgText);
            }
        } catch (Exception ex) {
            msgText = "Exception occurred during deferred patient discovery queue processing";
            LOG.error(msgText, ex);
            response.setResponse(msgText);
        }
        return response;
    }

    /**
     * Get's the URL.
     *
     * @param serviceName String servicename
     * @return the URL
     * @throws ConnectionManagerException an error
     */
    protected String getUrl(String serviceName) throws ExchangeManagerException {
        return proxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
    }

}
