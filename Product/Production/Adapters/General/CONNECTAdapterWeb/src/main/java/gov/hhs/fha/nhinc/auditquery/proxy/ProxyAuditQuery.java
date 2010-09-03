/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.auditquery.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyAuditLogQuery", portName = "NhincProxyAuditLogQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquery.NhincProxyAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquery", wsdlLocation = "WEB-INF/wsdl/ProxyAuditQuery/NhincProxyAuditLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class ProxyAuditQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType findAuditEventsRequest) {
        return new ProxyAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
