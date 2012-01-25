/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission_g1.nhin.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "XDRDeferredResponse_Service", portName = "XDRDeferredResponse_Port_Soap", endpointInterface = "nhin.deferred.XDRDeferredResponsePortType", targetNamespace = "urn:nhin:Deferred", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRDeferredResponse_g1.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class NhinXDRResponse_g1 {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        return new NhinDocSubmissionDeferredResponseImpl_g1().provideAndRegisterDocumentSetBResponse(body, context);
    }

}
