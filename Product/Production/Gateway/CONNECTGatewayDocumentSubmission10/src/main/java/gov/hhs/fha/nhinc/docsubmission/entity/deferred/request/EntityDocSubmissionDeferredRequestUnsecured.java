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

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityXDRAsyncRequest_Service", portName = "EntityXDRAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.async.request.EntityXDRAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr:async:request", wsdlLocation = "WEB-INF/wsdl/EntityDocSubmissionDeferredRequestUnsecured/EntityXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class EntityDocSubmissionDeferredRequestUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterAsyncReqRequest) {
        return new EntityDocSubmissionDeferredRequestImpl().provideAndRegisterDocumentSetBAsyncRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
