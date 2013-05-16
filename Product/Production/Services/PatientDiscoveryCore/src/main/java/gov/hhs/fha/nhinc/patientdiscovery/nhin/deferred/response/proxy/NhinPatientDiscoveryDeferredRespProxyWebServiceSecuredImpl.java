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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.service.RespondingGatewayDeferredResponseServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType;

import org.apache.log4j.Logger;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
//CheckStyle:OFF
//CheckStyle:ON

/**
 * 
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl implements
        NhinPatientDiscoveryDeferredRespProxy {

    private static final Logger LOG = Logger
            .getLogger(NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    protected CONNECTClient<RespondingGatewayDeferredResponsePortType> getCONNECTSecuredClient(
            ServicePortDescriptor<RespondingGatewayDeferredResponsePortType> portDescriptor, AssertionType assertion,
            String url, NhinTargetSystemType target) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
                target.getHomeCommunity().getHomeCommunityId(),
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
    }

    @NwhinInvocationEvent(beforeBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class, serviceType = "Patient Discovery Deferred Response", version = "1.0")
    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(PRPAIN201306UV02 request, AssertionType assertion,
            NhinTargetSystemType target) {
        String url = null;
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        try {
            if (request != null) {
                LOG.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlFromTargetSystemByGatewayAPILevel(target,
                        NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g0);
                LOG.debug("After target system URL look up. URL for service: "
                        + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    ServicePortDescriptor<RespondingGatewayDeferredResponsePortType> portDescriptor = new RespondingGatewayDeferredResponseServicePortDescriptor();
                    CONNECTClient<RespondingGatewayDeferredResponsePortType> client = getCONNECTSecuredClient(
                            portDescriptor, assertion, url, target);

                    response = (MCCIIN000002UV01) client.invokePort(RespondingGatewayDeferredResponsePortType.class,
                            "respondingGatewayDeferredPRPAIN201306UV02", request);
                } else {
                    LOG.error("Failed to call the web service ("
                            + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME + ").  The URL is null.");
                }
            } else {
                LOG.error("Failed to call the web service ("
                        + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME
                        + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            LOG.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME
                    + ").  An unexpected exception occurred.  " + "Exception: " + e.getMessage(), e);
        }

        return response;
    }
}
