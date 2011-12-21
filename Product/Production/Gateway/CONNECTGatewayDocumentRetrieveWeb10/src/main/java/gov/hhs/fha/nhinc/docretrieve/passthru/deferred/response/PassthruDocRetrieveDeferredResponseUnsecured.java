/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieveDeferredResponse", portName = "NhincProxyDocRetrieveDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrieveresponse.NhincProxyDocRetrieveDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieveresponse", wsdlLocation = "WEB-INF/wsdl/PassthruDocRetrieveDeferredResponseUnsecured/NhincProxyDocRetrieveDeferredResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class PassthruDocRetrieveDeferredResponseUnsecured extends PassthruDocRetrieveDeferredResponseImpl {

    @Resource
    private WebServiceContext context;

    /**
     * 
     * @param crossGatewayRetrieveResponse
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
        return crossGatewayRetrieveResponse(crossGatewayRetrieveResponse, context);
    }
}
