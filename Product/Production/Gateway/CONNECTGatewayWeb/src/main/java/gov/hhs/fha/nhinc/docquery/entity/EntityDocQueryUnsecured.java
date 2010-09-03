/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import javax.xml.ws.soap.Addressing;

@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryUnsecured/EntityDocQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocQueryUnsecured
{

    @Resource
    private WebServiceContext context;

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request)
    {
        AdhocQueryResponse response = null;

        EntityDocQueryImpl impl = getEntityDocQueryImpl();
        if (impl != null)
        {
            response = impl.respondingGatewayCrossGatewayQueryUnsecured(request, getWebServiceContext());
        }

        return response;
    }

    protected EntityDocQueryImpl getEntityDocQueryImpl()
    {
        return new EntityDocQueryImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
