/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.auditrepository.nhinc;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AuditRepositoryManagerSecuredService", portName = "AuditRepositoryManagerSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository", wsdlLocation = "WEB-INF/wsdl/AuditRepository/NhincComponentAuditRepositorySecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AuditRepository
{
    @Resource
    private WebServiceContext context;
    protected AuditRepositorySecuredImpl getAuditRepositorySecuredImpl() {
        return new AuditRepositorySecuredImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest)
    {
        return getAuditRepositorySecuredImpl().findAudit(queryAuditEventsRequest.getFindAuditEvents(), getWebServiceContext());
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEvent(gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType logEventRequest)
    {
       return getAuditRepositorySecuredImpl().logAudit(logEventRequest, getWebServiceContext());
    }

}
