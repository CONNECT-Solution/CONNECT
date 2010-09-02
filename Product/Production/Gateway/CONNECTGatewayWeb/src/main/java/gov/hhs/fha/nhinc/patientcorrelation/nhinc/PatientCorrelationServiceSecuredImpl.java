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
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.*;

/**
 *
 * @author svalluripalli
 */
public class PatientCorrelationServiceSecuredImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PatientCorrelationServiceSecuredImpl.class);

    public RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(RetrievePatientCorrelationsSecuredRequestType retrievePatientCorrelationsRequest, WebServiceContext context) {
        RetrievePatientCorrelationsSecuredResponseType response = new RetrievePatientCorrelationsSecuredResponseType();
        AssertionType assertion = createAssertion(context);

        log.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
        RetrievePatientCorrelationsResponseType unsecureResp = new PatientCorrelationOrchImpl().retrievePatientCorrelations(retrievePatientCorrelationsRequest.getPRPAIN201309UV02(), assertion);

        if (unsecureResp != null &&
                unsecureResp.getPRPAIN201310UV02() != null) {
            response.setPRPAIN201310UV02(unsecureResp.getPRPAIN201310UV02());
        }
        return response;
    }

    public AddPatientCorrelationSecuredResponseType addPatientCorrelation(AddPatientCorrelationSecuredRequestType addPatientCorrelationRequest, WebServiceContext context) {
        AddPatientCorrelationSecuredResponseType response = new AddPatientCorrelationSecuredResponseType();
        AssertionType assertion = createAssertion(context);

        log.info("Calling the Patient Correlation Add Correlations Orch Impl");
        AddPatientCorrelationResponseType unsecureResp = new PatientCorrelationOrchImpl().addPatientCorrelation(addPatientCorrelationRequest.getPRPAIN201301UV02(), assertion);

        if (unsecureResp != null &&
                unsecureResp.getMCCIIN000002UV01() != null) {
            response.setMCCIIN000002UV01(unsecureResp.getMCCIIN000002UV01());
        }

        return response;
    }

    private AssertionType createAssertion (WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }
    
}
