/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pep;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPEP", portName = "AdapterPEPPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpep", wsdlLocation = "WEB-INF/wsdl/AdapterPEPService/AdapterPEP.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPEPService
{
    @Resource
    private WebServiceContext context;

    protected AdapterPEPServiceImpl getAdapterPEPServiceImpl()
    {
        return new AdapterPEPServiceImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }

    /**
     * Given a request to check the access policy, this service will interface
     * with the PDP to determine if access is to be granted or denied.
     * @param checkPolicyRequest The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicy(gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType checkPolicyRequest) 
    {
        return getAdapterPEPServiceImpl().checkPolicy(checkPolicyRequest, getWebServiceContext());
    }

}
