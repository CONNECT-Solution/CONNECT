/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "RespondingGatewayDeferredResp_Service", portName = "RespondingGatewayDeferredResponse_Port", endpointInterface = "ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType", targetNamespace = "urn:ihe:iti:xcpd:2009", wsdlLocation = "WEB-INF/wsdl/NhinPatientDiscoveryAsyncResp/NhinPatientDiscoveryDeferredResponse.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
@Addressing(enabled=true)
public class NhinPatientDiscoveryAsyncResp extends PatientDiscoveryBase {
    @Resource
    private WebServiceContext context;
    
   
    public NhinPatientDiscoveryAsyncResp() {
		super();
	}

	public NhinPatientDiscoveryAsyncResp(
			PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}

	public org.hl7.v3.MCCIIN000002UV01 respondingGatewayDeferredPRPAIN201306UV02(PRPAIN201306UV02 body) {
        return getServiceFactory().getNhinPatientDiscoveryAsyncRespImpl().respondingGatewayPRPAIN201306UV02(body, getWebServiceContext());
    }

	protected WebServiceContext getWebServiceContext() {
		return context;
	}

}
