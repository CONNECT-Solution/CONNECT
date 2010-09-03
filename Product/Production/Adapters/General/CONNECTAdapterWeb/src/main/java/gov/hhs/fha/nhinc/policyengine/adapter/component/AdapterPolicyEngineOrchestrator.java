/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.component;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPolicyEngineOrchestrator", portName = "AdapterPolicyEngineOrchestratorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpolicyengineorchestrator", wsdlLocation = "WEB-INF/wsdl/AdapterPolicyEngineOrchestrator/AdapterPolicyEngineOrchestrator.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPolicyEngineOrchestrator
{
    @Resource
    private WebServiceContext context;

    protected AdapterComponentPolicyEngineImpl getAdapterComponentPolicyEngineImpl()
    {
        return new AdapterComponentPolicyEngineImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }


    /**
     * Given a request to check the access policy, this service will interface
     * with the Adapter PEP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) 
    {
        return getAdapterComponentPolicyEngineImpl().checkPolicy(checkPolicyRequest, getWebServiceContext());
    }

}
