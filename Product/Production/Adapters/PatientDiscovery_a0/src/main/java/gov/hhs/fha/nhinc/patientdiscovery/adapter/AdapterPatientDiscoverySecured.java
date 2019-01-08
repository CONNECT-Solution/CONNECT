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
package gov.hhs.fha.nhinc.patientdiscovery.adapter;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredFault;
import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecuredPortType;
import gov.hhs.healthit.nhin.PatientDiscoveryFaultType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.SOAPBinding;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sai Valluripalli
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPatientDiscoverySecured implements AdapterPatientDiscoverySecuredPortType {

    @Resource
    private WebServiceContext context;

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoverySecured.class);

    @Override
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
        RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request)
        throws AdapterPatientDiscoverySecuredFault {

        PRPAIN201306UV02 response;

        try {
            AdapterPatientDiscoveryImpl impl = new AdapterPatientDiscoveryImpl();
            response = impl.respondingGatewayPRPAIN201305UV02(true,
                respondingGatewayPRPAIN201305UV02Request, context);
        } catch (Exception e) {
            LOG.trace("Adapter PD Secured exception: {}", e.getLocalizedMessage(), e);
            PatientDiscoveryFaultType type = new PatientDiscoveryFaultType();
            type.setErrorCode("920");
            type.setMessage(e.getLocalizedMessage());
            throw new AdapterPatientDiscoverySecuredFault(e.getLocalizedMessage(), type);
        }

        return response;
    }
}
