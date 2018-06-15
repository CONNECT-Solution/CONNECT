/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.model.Patient;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class PatientCorrelationServiceImpl implements PatientCorrelationService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationServiceImpl.class);
    private String localAA;
    private PatientCorrelationProxy correlationClient = new PatientCorrelationProxyWebServiceUnsecuredImpl();

    @Override
    public String getLocalPatient(PatientSearchResults patientResults) {
        for (Patient patient : patientResults.getPatientList()) {
            if (NullChecker.isNotNullish(patient.getAaId()) && patient.getAaId().equals(getLocalAA())) {
                return patient.getPatientId();
            }
        }
        return null;
    }

    @Override
    public void queryForLocalPatient(PatientSearchResults patientResults, AssertionType assertion) {
        PRPAIN201309UV02 request = buildPRPAINRequest(patientResults);

        if (request != null) {
            RetrievePatientCorrelationsResponseType response = correlationClient.retrievePatientCorrelations(request, assertion);
        }
    }

    @Override
    public void addCorrelation(PatientSearchResults patientResults, AssertionType assertion) {

    }

    private String getLocalAA() {
        if (NullChecker.isNullish(localAA)) {
            try {
                localAA = getPropertyAccessor().getProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME, NhincConstants.ASSIGNING_AUTH_PROPERTY);
            } catch (PropertyAccessException ex) {
                LOG.warn("Unable to get local assigning authority due to: {}", ex.getLocalizedMessage());
            }
        }
        return localAA;
    }

    private PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    private PRPAIN201309UV02 buildPRPAINRequest(PatientSearchResults patientResults) {
        RetrievePatientCorrelationsRequestType retrieveCorrelations = new RetrievePatientCorrelationsRequestType();

        if (!patientResults.isPatientListEmpty() && NullChecker.isNotNullish(patientResults.getPatientList().get(0).getPatientId())
                && NullChecker.isNotNullish(patientResults.getPatientList().get(0).getAaId())) {
            QualifiedSubjectIdentifierType qualPatientId = new QualifiedSubjectIdentifierType();
            qualPatientId.setAssigningAuthorityIdentifier(patientResults.getPatientList().get(0).getAaId());
            qualPatientId.setSubjectIdentifier(patientResults.getPatientList().get(0).getPid());
            retrieveCorrelations.setQualifiedPatientIdentifier(qualPatientId);
            return new PixRetrieveBuilder().createPixRetrieve(retrieveCorrelations);
        }

        return null;
    }

}
