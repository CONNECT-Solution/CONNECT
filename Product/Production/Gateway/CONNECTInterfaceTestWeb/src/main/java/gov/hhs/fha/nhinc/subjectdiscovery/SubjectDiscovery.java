/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "PIXConsumer_Service", portName = "PIXConsumer_Port_Soap", endpointInterface = "ihe.iti.pixv3._2007.PIXConsumerPortType", targetNamespace = "urn:ihe:iti:pixv3:2007", wsdlLocation = "WEB-INF/wsdl/SubjectDiscovery/NhinSubjectDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class SubjectDiscovery
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201301UV(org.hl7.v3.PRPAIN201301UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201302UV(org.hl7.v3.PRPAIN201302UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.MCCIIN000002UV01 pixConsumerPRPAIN201304UV(org.hl7.v3.PRPAIN201304UV02 body)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public org.hl7.v3.PRPAIN201310UV02 pixConsumerPRPAIN201309UV(org.hl7.v3.PRPAIN201309UV02 body)
    {
        return SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV(body, context);
    }

}
