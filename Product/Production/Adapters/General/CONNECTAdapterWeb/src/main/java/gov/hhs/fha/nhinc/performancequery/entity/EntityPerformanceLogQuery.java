/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.performancequery.entity;

import gov.hhs.fha.nhinc.common.entityperformancelogquery.EntityPerformanceLogQueryRequestType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.EntityPerformanceLogQueryResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "EntityPerformanceLogQuery", portName = "EntityPerformanceLogQueryPort", endpointInterface = "gov.hhs.fha.nhinc.entityperformancelogquery.EntityPerformanceLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityperformancelogquery", wsdlLocation = "WEB-INF/wsdl/EntityPerformanceLogQuery/EntityPerformanceLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityPerformanceLogQuery {

    @Resource
    private WebServiceContext context;

    public EntityPerformanceLogQueryResponseType findPerformanceData(EntityPerformanceLogQueryRequestType entityPerformanceLogQueryRequest) {
        return new EntityPerformanceLogQueryImpl().findPerformanceData(entityPerformanceLogQueryRequest, context);
    }

}
