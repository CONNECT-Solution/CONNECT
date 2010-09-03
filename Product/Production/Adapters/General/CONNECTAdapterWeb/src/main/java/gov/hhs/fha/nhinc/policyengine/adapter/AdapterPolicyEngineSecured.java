/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineSecured", portName = "AdapterPolicyEngineSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyenginesecured.AdapterPolicyEngineSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyenginesecured", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineSecured/AdapterPolicyEngineSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPolicyEngineSecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestSecuredType body) {
        return new AdapterPolicyEngineSecuredImpl().checkPolicy(body, context);
    }

}
