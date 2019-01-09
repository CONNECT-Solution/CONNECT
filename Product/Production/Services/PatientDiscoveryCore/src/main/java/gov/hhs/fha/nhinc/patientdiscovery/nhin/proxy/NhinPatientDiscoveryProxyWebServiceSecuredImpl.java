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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.service.RespondingGatewayServicePortDescriptor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import ihe.iti.xcpd._2009.RespondingGatewayPortType;
import javax.xml.ws.WebServiceException;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryProxyWebServiceSecuredImpl implements NhinPatientDiscoveryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinPatientDiscoveryProxyWebServiceSecuredImpl.class);

    protected CONNECTClient<RespondingGatewayPortType> getCONNECTSecuredClient(NhinTargetSystemType target,
        ServicePortDescriptor<RespondingGatewayPortType> portDescriptor, String url, AssertionType assertion) {
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url,
            target, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
    }

    @Override
    @NwhinInvocationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class, afterReturningBuilder
        = PRPAIN201306UV02EventDescriptionBuilder.class, serviceType = "Patient Discovery", version = "1.0")
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion,
        NhinTargetSystemType target) throws Exception {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        try {
            if (request != null && target != null) {

                LOG.debug("Before target system URL look up.");
                String url = target.getUrl();
                if (NullChecker.isNullish(url)) {
                    url = ExchangeManager.getInstance().getDefaultEndpointURL(
                        target.getHomeCommunity().getHomeCommunityId(),
                        NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, target.getExchangeName());
                    LOG.debug("After target system URL look up. URL for service: {} is: {}" ,NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, url);
                }

                if (NullChecker.isNotNullish(url)) {
                    ServicePortDescriptor<RespondingGatewayPortType> portDescriptor
                        = new RespondingGatewayServicePortDescriptor();

                    CONNECTClient<RespondingGatewayPortType> client = getCONNECTSecuredClient(target, portDescriptor,
                        url, assertion);

                    response = (PRPAIN201306UV02) client.invokePort(RespondingGatewayPortType.class,
                        "respondingGatewayPRPAIN201305UV02", request);
                } else {
                    throw new WebServiceException("Could not determine URL for Patient Discovery Deferred Response endpoint");
                }
            } else {
                throw new IllegalArgumentException("Request Message must be provided");
            }
        } catch (Exception e) {
            PRPAIN201306UV02 errorResponse = new HL7PRPA201306Transforms().createPRPA201306ForErrors(request,
                NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE, e.getMessage());

            throw new ErrorEventException(e,errorResponse, "Unable to call Nhin Patient Discovery");
        }

        return response;
    }
}
