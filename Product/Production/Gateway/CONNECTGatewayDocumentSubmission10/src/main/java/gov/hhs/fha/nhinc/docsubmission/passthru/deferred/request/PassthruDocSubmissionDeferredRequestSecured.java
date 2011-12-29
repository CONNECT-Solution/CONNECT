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

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "ProxyXDRSecuredAsyncRequest_Service", portName = "ProxyXDRSecuredAsyncRequest_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredAsyncRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:request", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionDeferredRequestSecured/NhincProxyXDRSecuredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PassthruDocSubmissionDeferredRequestSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterAsyncReqRequest) {
        return new PassthruDocSubmissionDeferredRequestImpl().provideAndRegisterDocumentSetBRequest(provideAndRegisterAsyncReqRequest, context);
    }

}
