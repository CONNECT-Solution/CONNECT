/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapter.reidentification;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterReidentification", portName = "AdapterReidentificationBindingSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterreidentification.AdapterReidentificationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterreidentification", wsdlLocation = "WEB-INF/wsdl/AdapterReidentification/AdapterReidentification.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterReidentification {

    public org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType getRealIdentifierRequest) {
        AdapterReidentificationImpl impl = new AdapterReidentificationImpl();
        return impl.getRealIdentifier(getRealIdentifierRequest);
    }

}
