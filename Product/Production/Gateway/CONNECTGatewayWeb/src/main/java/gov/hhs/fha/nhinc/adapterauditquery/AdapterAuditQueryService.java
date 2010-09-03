/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapterauditquery;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuery", portName = "AdapterAuditLogQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquery", wsdlLocation = "WEB-INF/wsdl/AdapterAuditQueryService/AdapterAuditLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterAuditQueryService {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest)
    {
        return new AdapterAuditQueryServiceImpl().findAuditEvents(findAuditEventsRequest);
    }

}
