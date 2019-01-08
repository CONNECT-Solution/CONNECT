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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import gov.hhs.fha.nhinc.aspect.InboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.InboundPatientDiscoveryDeferredResponse;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import org.hl7.v3.PRPAIN201306UV02;

@Addressing(enabled = true)
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class NhinPatientDiscoveryDeferredResponse extends BaseService implements
    RespondingGatewayDeferredResponsePortType {

    private InboundPatientDiscoveryDeferredResponse inboundPatientDiscoveryResponse;

    private WebServiceContext context;

    /**
     * Constructor.
     */
    public NhinPatientDiscoveryDeferredResponse() {
        super();
    }

    /**
     * Web Service implementation of Patient Discovery Deferred Response.
     *
     * @param body the request message from the Nhin
     * @return the response message to the Nhin
     */
    @InboundMessageEvent(beforeBuilder = PRPAIN201306UV02EventDescriptionBuilder.class,
        afterReturningBuilder = MCCIIN000002UV01EventDescriptionBuilder.class,
        serviceType = "Patient Discovery Deferred Response", version = "1.0")
    @Override
    public org.hl7.v3.MCCIIN000002UV01 respondingGatewayDeferredPRPAIN201306UV02(PRPAIN201306UV02 body) {
        AssertionType assertion = getAssertion(context, null);

        return inboundPatientDiscoveryResponse.respondingGatewayDeferredPRPAIN201306UV02(body, assertion,
            getWebContextProperties(context));
    }

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    public void setInboundPatientDiscoveryResponse(
        InboundPatientDiscoveryDeferredResponse inboundPatientDiscoveryResponse) {
        this.inboundPatientDiscoveryResponse = inboundPatientDiscoveryResponse;
    }

    /**
     * Gets the inbound patient discovery.
     *
     * @return the inbound patient discovery
     */
    public InboundPatientDiscoveryDeferredResponse getInboundPatientDiscovery() {
        return this.inboundPatientDiscoveryResponse;
    }

}
