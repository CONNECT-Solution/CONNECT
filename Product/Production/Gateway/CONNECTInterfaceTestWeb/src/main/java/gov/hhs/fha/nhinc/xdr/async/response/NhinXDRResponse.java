/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.xdr.async.response;

import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "XDRDeferredResponse_Service", portName = "XDRDeferredResponse_Port_Soap", endpointInterface = "ihe.iti.xdr._2007.XDRDeferredResponsePortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinXDRResponse {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(RegistryResponseType body) {
        return new NhinXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body);
    }

}
