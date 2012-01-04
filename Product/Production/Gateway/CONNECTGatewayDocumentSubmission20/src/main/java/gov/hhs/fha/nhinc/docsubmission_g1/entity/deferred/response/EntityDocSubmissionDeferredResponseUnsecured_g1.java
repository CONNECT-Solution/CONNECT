/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */

package gov.hhs.fha.nhinc.docsubmission_g1.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityXDRAsyncResponse_Service", portName = "EntityXDRAsyncResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.async.response.EntityXDRAsyncResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:response", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionDeferredResponseUnsecured/EntityXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocSubmissionDeferredResponseUnsecured_g1 {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterDocumentSetAsyncRespRequest) {
       return new EntityDocSubmissionDeferredResponseImpl_g1().provideAndRegisterDocumentSetBAsyncResponse(provideAndRegisterDocumentSetAsyncRespRequest, context);
    }

}
