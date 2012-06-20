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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.healthit.nhin.PatientDiscoveryFaultType;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "RespondingGateway_Service", portName = "RespondingGateway_Port_Soap", endpointInterface = "ihe.iti.xcpd._2009.RespondingGatewayPortType", targetNamespace = "urn:ihe:iti:xcpd:2009", wsdlLocation = "WEB-INF/wsdl/NhinPatientDiscovery/NhinPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
@HandlerChain(file="../../../../../../../../handler-chain.xml")
public class NhinPatientDiscovery extends PatientDiscoveryBase {

    @Resource
    private WebServiceContext context;

    public NhinPatientDiscovery() {
        super();
    }

    public NhinPatientDiscovery(PatientDiscoveryServiceFactory serviceFactory) {
        super(serviceFactory);
    }

    public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body) throws PRPAIN201305UV02Fault {
        try {
            return getNhinPatientDiscoveryService().respondingGatewayPRPAIN201305UV02(body, context);
        } catch (PatientDiscoveryException e) {
            PatientDiscoveryFaultType type = new PatientDiscoveryFaultType();
            type.setErrorCode("920");
            type.setMessage(e.getLocalizedMessage());
            PRPAIN201305UV02Fault fault = new PRPAIN201305UV02Fault(e.getMessage(), type);
            throw fault;
        }
    }

    protected NhinPatientDiscoveryImpl getNhinPatientDiscoveryService() {
        return getServiceFactory().getNhinPatientDiscoveryService();
    }

}
