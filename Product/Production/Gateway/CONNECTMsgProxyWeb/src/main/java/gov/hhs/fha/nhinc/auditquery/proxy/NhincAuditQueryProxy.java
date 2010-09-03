/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditquery.proxy;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyAuditLogQuerySecured", portName = "NhincProxyAuditLogQuerySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquerysecured", wsdlLocation = "WEB-INF/wsdl/NhincAuditQueryProxy/NhincProxyAuditLogQuerySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincAuditQueryProxy {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType findAuditEventsRequest)
    {
        return new ProxyAuditLogQueryImpl().findAuditEvents(findAuditEventsRequest, context);
    }

}
