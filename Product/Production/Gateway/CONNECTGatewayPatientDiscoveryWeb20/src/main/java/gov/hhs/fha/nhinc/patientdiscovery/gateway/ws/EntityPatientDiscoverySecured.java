/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryImpl;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import javax.xml.ws.soap.Addressing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "EntityPatientDiscoverySecured", portName = "EntityPatientDiscoverySecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecured", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecured/EntityPatientDiscoverySecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityPatientDiscoverySecured extends PatientDiscoveryBase {
	private static final Log log = LogFactory
			.getLog(EntityPatientDiscoverySecured.class);

	@Resource
	private WebServiceContext context;

	public EntityPatientDiscoverySecured() {
		super();
	}

	public EntityPatientDiscoverySecured(
			PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}

	public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
			RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request) {
		log.debug("Begin EntityPatientDiscoverySecured.respondingGatewayPRPAIN201305UV02...");
		RespondingGatewayPRPAIN201306UV02ResponseType response = null;

		EntityPatientDiscoveryImpl serviceImpl = getEntityPatientDiscoveryImpl();
		if (serviceImpl != null) {
			response = serviceImpl.respondingGatewayPRPAIN201305UV02(
					respondingGatewayPRPAIN201305UV02Request,
					getWebServiceContext());
		}
		log.debug("End EntityPatientDiscoverySecured.respondingGatewayPRPAIN201305UV02...");
		return response;
	}

	protected EntityPatientDiscoveryImpl getEntityPatientDiscoveryImpl() {
		return getServiceFactory().getEntityPatientDiscoveryImpl();
	}

	protected WebServiceContext getWebServiceContext() {
		return context;
	}

}
