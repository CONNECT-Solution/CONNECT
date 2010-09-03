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
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.PatientCorrelationOrchImpl;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationServiceUnsecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationServiceUnsecuredImpl.class);

    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(RetrievePatientCorrelationsRequestType retrievePatientCorrelationsRequest, WebServiceContext context) {
        if (retrievePatientCorrelationsRequest != null &&
                retrievePatientCorrelationsRequest.getAssertion() != null) {
            retrievePatientCorrelationsRequest.getAssertion().setMessageId(createMessageId(context));
        }

        log.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
        RetrievePatientCorrelationsResponseType response = new PatientCorrelationOrchImpl().retrievePatientCorrelations(retrievePatientCorrelationsRequest.getPRPAIN201309UV02(), retrievePatientCorrelationsRequest.getAssertion());

        return response;
    }

    public AddPatientCorrelationResponseType addPatientCorrelation(AddPatientCorrelationRequestType addPatientCorrelationRequest, WebServiceContext context) {
        if (addPatientCorrelationRequest != null &&
                addPatientCorrelationRequest.getAssertion() != null) {
            addPatientCorrelationRequest.getAssertion().setMessageId(createMessageId(context));
        }

        log.info("Calling the Patient Correlation Add Correlations Orch Impl");
        AddPatientCorrelationResponseType response = new PatientCorrelationOrchImpl().addPatientCorrelation(addPatientCorrelationRequest.getPRPAIN201301UV02(), addPatientCorrelationRequest.getAssertion());

        return response;
    }

    private String createMessageId(WebServiceContext context) {
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        return msgIdExtractor.GetAsyncMessageId(context);
    }
}
