/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryImpl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import javax.xml.ws.soap.Addressing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscovery", portName = "EntityPatientDiscoveryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscovery", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryUnsecured/EntityPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityPatientDiscoveryUnsecured extends PatientDiscoveryBase {
	private static final Log log = LogFactory
			.getLog(EntityPatientDiscoveryUnsecured.class);

	
	public EntityPatientDiscoveryUnsecured() {
		super();
	}

	public EntityPatientDiscoveryUnsecured(
			PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}

	protected EntityPatientDiscoveryImpl getEntityPatientDiscoveryImpl() {
		return getServiceFactory().getEntityPatientDiscoveryImpl();
	}

	public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
			RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
		log.debug("Begin EntityPatientDiscoveryUnsecured.respondingGatewayPRPAIN201305UV02...");
		RespondingGatewayPRPAIN201306UV02ResponseType response = null;

		EntityPatientDiscoveryImpl impl = getEntityPatientDiscoveryImpl();
		if (impl != null) {
			response = impl
					.respondingGatewayPRPAIN201305UV02(respondingGatewayPRPAIN201305UV02Request);
		}
		log.debug("End EntityPatientDiscoveryUnsecured.respondingGatewayPRPAIN201305UV02...");
		return response;
	}

}
