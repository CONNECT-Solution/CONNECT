/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docprovideandregister;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterDocProvideAndRegisterService", portName = "AdapterDocProvideAndRegisterPortTypeBindingPortSoap", endpointInterface = "org.netbeans.j2ee.wsdl.interfaces.adapterdocprovideandregister.AdapterDocProvideAndRegisterPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/Interfaces/AdapterDocProvideAndRegister", wsdlLocation = "WEB-INF/wsdl/AdapterDocProvideAndRegisterservice/AdapterDocProvideAndRegister.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocProvideAndRegisterservice {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType adapterDocProvideAndRegisterOperation(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType part1) {
        return new AdapterDocProvideAndRegisterImpl().adapterDocProvideAndRegisterOperation(part1);
    }

}
