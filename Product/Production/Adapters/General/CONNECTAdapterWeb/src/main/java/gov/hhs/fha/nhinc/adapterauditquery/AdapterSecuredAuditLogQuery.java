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

package gov.hhs.fha.nhinc.adapterauditquery;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuerySamlService", portName = "AdapterAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/AdapterSecuredAuditLogQuery/AdapterAuditLogQuerySaml.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterSecuredAuditLogQuery {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(com.services.nhinc.schema.auditmessage.FindAuditEventsType findAuditEventsRequest) {
        return new AdapterSecuredAuditLogQueryImpl().queryAdapter(findAuditEventsRequest, context);
    }

}
