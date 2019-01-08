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

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.patientdiscovery._10.entity.EntityPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02ArgTransformer;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.RespondingGatewayPRPAIN201306UV02Builder;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.OutboundPatientDiscovery;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

@Addressing(enabled = true)
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class EntityPatientDiscoverySecured extends BaseService implements EntityPatientDiscoverySecuredPortType {

    private OutboundPatientDiscovery outboundPatientDiscovery;

    private WebServiceContext context;

    public EntityPatientDiscoverySecured() {
        super();
    }

    @OutboundMessageEvent(beforeBuilder = PRPAIN201305UV02ArgTransformer.class,
        afterReturningBuilder = RespondingGatewayPRPAIN201306UV02Builder.class, serviceType = "Patient Discovery",
        version = "1.0")
    @Override
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
        RespondingGatewayPRPAIN201305UV02RequestType request) {

        AssertionType assertion = getAssertion(context, null);

        return new EntityPatientDiscoveryImpl(outboundPatientDiscovery).respondingGatewayPRPAIN201305UV02(request,
            assertion);
    }

    public void setOutboundPatientDiscovery(OutboundPatientDiscovery outboundPatientDiscovery) {
        this.outboundPatientDiscovery = outboundPatientDiscovery;
    }

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

}
