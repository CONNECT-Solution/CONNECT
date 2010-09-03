/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subjectdiscovery.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxySubjectDiscoverySecured", portName = "NhincProxySubjectDiscoverySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubjectdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/NhincSubjectDiscoverySecured/NhincProxySubjectDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincSubjectDiscoverySecured
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PIXConsumerPRPAIN201301UVProxySecuredRequestType pixConsumerPRPAIN201301UVProxyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PIXConsumerPRPAIN201302UVProxySecuredRequestType pixConsumerPRPAIN201302UVProxyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PIXConsumerPRPAIN201304UVProxySecuredRequestType pixConsumerPRPAIN201304UVProxyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.PRPAIN201310UV02 pixConsumerPRPAIN201309UV(org.hl7.v3.PIXConsumerPRPAIN201309UVProxySecuredRequestType pixConsumerPRPAIN201309UVProxyRequest)
    {
        return new NhincProxySubjectDiscoveryImpl().pixConsumerPRPAIN201309UV(pixConsumerPRPAIN201309UVProxyRequest, context);
    }

}
