/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

@WebService(serviceName = "NhincProxyPatientDiscoverySecuredAsyncReq", portName = "NhincProxyPatientDiscoverySecuredAsyncReqPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecuredasyncreq.NhincProxyPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryDeferredRequestSecured/NhincProxyPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class NhincProxyPatientDiscoveryDeferredRequestSecured
{

    @Resource
    private WebServiceContext context;

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxySecuredRequestType request)
    {
        MCCIIN000002UV01 response = null;

        NhincProxyPatientDiscoveryDeferredRequestImpl serviceImpl = getNhincProxyPatientDiscoveryDeferredRequestImpl();
        if (serviceImpl != null)
        {
            response = serviceImpl.processPatientDiscoveryAsyncRequestSecured(request, getWebServiceContext());
        }
        return response;
    }

    protected NhincProxyPatientDiscoveryDeferredRequestImpl getNhincProxyPatientDiscoveryDeferredRequestImpl()
    {
        return new NhincProxyPatientDiscoveryDeferredRequestImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
