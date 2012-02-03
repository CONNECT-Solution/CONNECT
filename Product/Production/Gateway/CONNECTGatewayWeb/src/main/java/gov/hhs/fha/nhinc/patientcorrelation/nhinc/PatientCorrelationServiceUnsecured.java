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

package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * 
 * @author jhoppesc
 */
@WebService(serviceName = "PatientCorrelationService", portName = "PatientCorrelationPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpatientcorrelation", wsdlLocation = "WEB-INF/wsdl/PatientCorrelationServiceUnsecured/NhincComponentPatientCorrelation.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PatientCorrelationServiceUnsecured {
	@Resource
	private WebServiceContext context;

	private PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> service;

	public PatientCorrelationServiceUnsecured() {
		this(PatientCorrelationServiceUnsecuredFactory.getInstance());

	}

	public PatientCorrelationServiceUnsecured(
			PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> factory) {
		service = factory.createPatientCorrelationService();
	}

	public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(
			RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest) {
		AssertionType assertionType = createAssertion(context);

		if (retrievePatientCorrelationsRequest != null
				&& retrievePatientCorrelationsRequest.getAssertion() != null) {
			retrievePatientCorrelationsRequest.getAssertion().setMessageId(
					createMessageId(context));
		}
		return service.retrievePatientCorrelations(
				retrievePatientCorrelationsRequest, assertionType);
	}

	public AddPatientCorrelationResponseType addPatientCorrelation(
			AddPatientCorrelationRequestType addPatientCorrelationRequest) {
		AssertionType assertionType = createAssertion(context);

		if (addPatientCorrelationRequest != null
				&& addPatientCorrelationRequest.getAssertion() != null) {
			addPatientCorrelationRequest.getAssertion().setMessageId(
					createMessageId(context));
		}

		return service.addPatientCorrelation(addPatientCorrelationRequest,
				assertionType);
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

	private String createMessageId(WebServiceContext context) {
		return AsyncMessageIdExtractor.GetAsyncMessageId(context);
	}

}
