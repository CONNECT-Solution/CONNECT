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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.service.AdapterPatientDiscoverySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.service.AdapterPatientDiscoveryServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class AdapterPatientDiscoveryProxyWebServiceHelper {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryProxyWebServiceHelper.class);
    private static final AdapterPatientDiscoveryProxyWebServiceHelper instance = new AdapterPatientDiscoveryProxyWebServiceHelper();
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private AdapterPatientDiscoveryProxyWebServiceHelper() {
    }

    public static AdapterPatientDiscoveryProxyWebServiceHelper getInstance() {
        return instance;
    }

    public PRPAIN201306UV02 sendToPRPAIN201306UV02Adapter(PRPAIN201305UV02 body, AssertionType assertion,
        String sServiceName) {
        String url;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        try {
            if (body != null) {
                LOG.debug("Before target system URL look up.");
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                LOG.debug("After target system URL look up. URL for service: {} and url : {}  ", sServiceName, url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
                    request.setAssertion(assertion);
                    request.setPRPAIN201305UV02(body);
                    request.setNhinTargetCommunities(null);
                    CONNECTClient client = getClientService(sServiceName, url, assertion);
                    response = (PRPAIN201306UV02) client.invokePort(client.getPort().getClass(),
                        "respondingGatewayPRPAIN201305UV02", request);

                } else {
                    throw new PatientDiscoveryException(
                        "Failed to call the adapter web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                throw new PatientDiscoveryException(
                    "Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            throw new ErrorEventException(e, "Unable to call Patient Discovery Adapter");
        }
        return response;
    }

    private static CONNECTClient getClientService(String serviceName, String url, AssertionType assertion) {
        ServicePortDescriptor portDescriptor;
        CONNECTClient client;
        if (NhincConstants.ADAPTER_PATIENT_DISCOVERY_SECURED_SERVICE_NAME.equals(serviceName)) {
            portDescriptor = new AdapterPatientDiscoverySecuredServicePortDescriptor();
            client = CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
        } else { // it is unsecure service
            portDescriptor = new AdapterPatientDiscoveryServicePortDescriptor();
            client = CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
        }
        return client;
    }


}
