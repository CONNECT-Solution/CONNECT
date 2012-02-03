/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

import javax.annotation.Resource;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;

/**
 * 
 * @author Sai Valluripalli
 */
@WebService(serviceName = "PatientCorrelationServiceSecured", portName = "PatientCorrelationSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceSecured/NhincComponentPatientCorrelationSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PatientCorrelationServiceSecured {

	@Resource
	private WebServiceContext context;
	
	private PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType> service;

	public PatientCorrelationServiceSecured() {
		this(PatientCorrelationServiceSecuredFactory.getInstance());
	}

	public PatientCorrelationServiceSecured(
			PatientCorrelationServiceFactory<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType> factory) {
		service = factory.createPatientCorrelationService();
	}

	public RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(
			RetrievePatientCorrelationsSecuredRequestType request) {
		AssertionType assertion = createAssertion(context);
		return service.retrievePatientCorrelations(
				request, assertion);
	}

	public AddPatientCorrelationSecuredResponseType addPatientCorrelation(
			AddPatientCorrelationSecuredRequestType request) {
		AssertionType assertion = createAssertion(context);
		return service.addPatientCorrelation(request,
				assertion);
	}

	private AssertionType createAssertion(WebServiceContext context) {
		AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

		// Extract the message id value from the WS-Addressing Header and place
		// it in the Assertion Class
		if (assertion != null) {
			assertion.setMessageId(AsyncMessageIdExtractor
					.GetAsyncMessageId(context));
		}

		return assertion;
	}
}
