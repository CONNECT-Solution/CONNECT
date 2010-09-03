/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subdisc.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubjectDiscovery", portName = "NhincProxySubjectDiscoveryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubjectdiscovery.NhincProxySubjectDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubjectdiscovery", wsdlLocation = "WEB-INF/wsdl/NhincProxySubjectDiscovery/NhincProxySubjectDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxySubjectDiscovery {

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType pixConsumerPRPAIN201301UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVProxyRequestType pixConsumerPRPAIN201302UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVProxyRequestType pixConsumerPRPAIN201304UVProxyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.PRPAIN201310UV02 pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType pixConsumerPRPAIN201309UVProxyRequest) {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVProxyRequest);
    }

}
