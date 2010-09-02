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
@WebService(serviceName = "AdapterPolicyEngine", portName = "AdapterPolicyEnginePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengine.AdapterPolicyEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengine", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngine/AdapterPolicyEngine.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPolicyEngine {
    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest)
    {
        return new AdapterPolicyEngineImpl().checkPolicy(checkPolicyRequest, context);
    }

}
