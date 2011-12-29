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

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "ProxyXDRSecuredAsyncResponse_Service", portName = "ProxyXDRSecuredAsyncResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.response.ProxyXDRSecuredAsyncResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured:async:response", wsdlLocation = "WEB-INF/wsdl/PassthruDocSubmissionDeferredResponseSecured/NhincProxyXDRSecuredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class PassthruDocSubmissionDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterAsyncRespRequest) {
        return new PassthruDocSubmissionDeferredResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterAsyncRespRequest, context);
    }

}
