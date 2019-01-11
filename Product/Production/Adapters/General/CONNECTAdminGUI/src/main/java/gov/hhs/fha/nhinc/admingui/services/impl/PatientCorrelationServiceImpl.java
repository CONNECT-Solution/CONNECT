/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.PatientCorrelationService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.IIHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import java.util.Date;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jassmit
 */
public class PatientCorrelationServiceImpl implements PatientCorrelationService {

    private String localAA;
    private final PatientCorrelationProxy correlationClient = new PatientCorrelationProxyWebServiceUnsecuredImpl();

    @Override
    public II retrieveOrGenerateCorrelation(PatientSearchResults patientResults, AssertionType assertion) {
        II correlation = getLocalPatient(patientResults);
        if(correlation == null) {
            queryForCorrelations(patientResults, assertion);
            correlation = getLocalPatient(patientResults.getPatientList().get(0).getCorrelations());
        }
        
        if(correlation == null) {
            correlation = addCorrelation(patientResults, assertion);
        }
        return correlation;
    }
    
    @Override
    public II getLocalPatient(PatientSearchResults patientResults) {
        if (!patientResults.isPatientListEmpty() && NullChecker.isNotNullish(patientResults.getPatientList().get(0).getAaId())
                && patientResults.getPatientList().get(0).getAaId().equals(getLocalAA())) {
            return IIHelper.IIFactory(getLocalAA(), patientResults.getPatientList().get(0).getPid());
        }
        return null;
    }
    
    @Override
    public II getLocalPatient(List<II> correlations) {
        for(II correlation : correlations) {
            if(NullChecker.isNotNullish(correlation.getRoot()) && correlation.getRoot().equals(getLocalAA())) {
                return correlation;
            }
        }
        
        return null;
    }

    @Override
    public void queryForCorrelations(PatientSearchResults patientResults, AssertionType assertion) {
        PRPAIN201309UV02 request = buildPRPAINRequest(patientResults);

        if (request != null) {
            RetrievePatientCorrelationsResponseType response = correlationClient.retrievePatientCorrelations(request, assertion);
            addToCorrelationList(response, patientResults);
        }
    }

    @Override
    public II addCorrelation(PatientSearchResults patientResults, AssertionType assertion) {
        String aa = getLocalAA();
        II newCorrelation = IIHelper.IIFactory(aa, generatePatientId());
        II remoteCorrelation = IIHelper.IIFactory(patientResults.getPatientList().get(0).getAaId(), patientResults.getPatientList().get(0).getPid());
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        patient.getId().add(newCorrelation);
        patient.getId().add(remoteCorrelation);
        PRPAIN201301UV02 request = HL7PRPA201301Transforms.createPRPA201301(patient, aa, aa, aa);
        
        correlationClient.addPatientCorrelation(request, assertion);
        return newCorrelation;
    }

    private String getLocalAA() {
        if (NullChecker.isNullish(localAA)) {
            localAA = getPropertyAccessor().getProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME,
                NhincConstants.ASSIGNING_AUTH_PROPERTY, null);
        }
        return localAA;
    }

    private static PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    private static PRPAIN201309UV02 buildPRPAINRequest(PatientSearchResults patientResults) {
        RetrievePatientCorrelationsRequestType retrieveCorrelations = new RetrievePatientCorrelationsRequestType();

        if (!patientResults.isPatientListEmpty() && NullChecker.isNotNullish(patientResults.getPatientList().get(0).getPid())
                && NullChecker.isNotNullish(patientResults.getPatientList().get(0).getAaId())) {
            QualifiedSubjectIdentifierType qualPatientId = new QualifiedSubjectIdentifierType();
            qualPatientId.setAssigningAuthorityIdentifier(patientResults.getPatientList().get(0).getAaId());
            qualPatientId.setSubjectIdentifier(patientResults.getPatientList().get(0).getPid());
            retrieveCorrelations.setQualifiedPatientIdentifier(qualPatientId);
            return new PixRetrieveBuilder().createPixRetrieve(retrieveCorrelations);
        }

        return null;
    }

    private static void addToCorrelationList(RetrievePatientCorrelationsResponseType response, PatientSearchResults patientResults) {
        if(isPatientResultEmpty(patientResults) && isCorrelationResponseEmpty(response)) {
            List<PRPAIN201310UV02MFMIMT700711UV01Subject1> subjectList = response.getPRPAIN201310UV02().getControlActProcess().getSubject();
            for (PRPAIN201310UV02MFMIMT700711UV01Subject1 subject : subjectList) {
                addToCorrelationList(subject, patientResults);
            }
        }
    }
    
    private static boolean isPatientResultEmpty(PatientSearchResults patientResults) {
        return !patientResults.isPatientListEmpty() && patientResults.getPatientList().get(0) != null;
    }
    
    private static boolean isCorrelationResponseEmpty(RetrievePatientCorrelationsResponseType response) {
        return response != null && response.getPRPAIN201310UV02() != null
             && response.getPRPAIN201310UV02().getControlActProcess() != null
             && response.getPRPAIN201310UV02().getControlActProcess().getSubject() != null;
    }
    
    private static void addToCorrelationList(PRPAIN201310UV02MFMIMT700711UV01Subject1 subject, PatientSearchResults patientResults) {
        if (subject.getRegistrationEvent() != null && subject.getRegistrationEvent().getSubject1() != null
                && subject.getRegistrationEvent().getSubject1().getPatient() != null) {
            patientResults.getPatientList().get(0).getCorrelations().addAll(subject.getRegistrationEvent().getSubject1().getPatient().getId());
        }
    }

    private static String generatePatientId() {
        return "CONNECT-" + new Date().getTime();      
    }

}
