/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@WebService(serviceName = "EntityXDR_Service", portName = "EntityXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.EntityXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionUnsecured/EntityXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocSubmissionUnsecured {

    @Resource
    private WebServiceContext context;

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetRequestType body)
    {
        RegistryResponseType response = null;

        EntityDocSubmissionImpl impl = getEntityDocSubmissionImpl();
        if (impl != null)
        {
            response = impl.provideAndRegisterDocumentSetBUnsecured(body, getWebServiceContext());
        }

        return response;
    }

    protected EntityDocSubmissionImpl getEntityDocSubmissionImpl()
    {
        return new EntityDocSubmissionImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
