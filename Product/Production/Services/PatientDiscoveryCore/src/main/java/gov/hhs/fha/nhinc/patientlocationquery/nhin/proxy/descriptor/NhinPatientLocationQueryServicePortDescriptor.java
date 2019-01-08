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
package gov.hhs.fha.nhinc.patientlocationquery.nhin.proxy.descriptor;

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import ihe.iti.xcpd._2009.RespondingGatewayPLQPortType;

public class NhinPatientLocationQueryServicePortDescriptor
extends SOAP12ServicePortDescriptor<RespondingGatewayPLQPortType> {

    private static final String NAMESPACE_URI = "urn:ihe:iti:xcpd:2009";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_PLQ_Port_Soap";

    @Override
    public String getWSAddressingAction() {
        return "urn:ihe:iti:xcpd:2009:PatientLocationQuery";
    }

    public String getNamespaceUri() {
        return NAMESPACE_URI;
    }

    public String getServiceLocalPart() {
        return SERVICE_LOCAL_PART;
    }

    public String getPortLocalPart() {
        return PORT_LOCAL_PART;
    }

    @Override
    public Class<RespondingGatewayPLQPortType> getPortClass() {
        return RespondingGatewayPLQPortType.class;
    }
}
