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
package gov.hhs.fha.nhinc.patientdiscovery.model.builder.impl;

/**
 * PatientSearchResultsModelBuilderImpl process a PD response.
 * <p>
 * Currently it process firstName, lastNaeme,middleName, dateOfBirth, gender, address, assigningAuthorityId and patient
 * Id
 *
 * @author tjafri
 */
import gov.hhs.fha.nhinc.patientdiscovery.model.Patient;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.patientdiscovery.model.builder.AbstractPatientSearchResultsModelBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.model.builder.PatientSearchResultsModelBuilder;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientSearchResultsModelBuilderImpl extends AbstractPatientSearchResultsModelBuilder
implements PatientSearchResultsModelBuilder {

    private PatientSearchResults results;
    private PRPAIN201306UV02 message;
    private static final Logger LOG = LoggerFactory.getLogger(PatientSearchResultsModelBuilderImpl.class);

    @Override
    public void build() {
        results = new PatientSearchResults();
        if (message != null) {
            LOG.debug("Retrieving Patient Results from Response: " + message);
            List<PRPAIN201306UV02MFMIMT700711UV01Subject1> subjects = getSubjects(message);
            LOG.debug("Mpi Search Results Number Subject found: " + subjects.size());
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : subjects) {
                PRPAMT201310UV02Patient patient = getSubject1Patient(subject);
                if (patient != null) {
                    extractAndAddPatient(patient);
                }
            }
        }
    }

    @Override
    public void setMessage(PRPAIN201306UV02 message) {
        this.message = message;
    }

    @Override
    public PatientSearchResults getPatientSearchResultModel() {
        return results;
    }

    private void extractAndAddPatient(PRPAMT201310UV02Patient msgPatient) {
        if (msgPatient != null && msgPatient.getPatientPerson() != null
            && msgPatient.getPatientPerson().getValue() != null) {

            PRPAMT201310UV02Person person = msgPatient.getPatientPerson().getValue();
            Patient patient = new Patient();

            extractNames(person, patient);
            extractPidAndAAId(msgPatient, patient);
            extractAddress(person, patient);
            extractSsn(person, patient);
            extractTelephone(person, patient);
            extractGender(person, patient);
            extractDob(person, patient);
            results.addPatient(patient);
        }
    }

    private void extractNames(PRPAMT201310UV02Person person, Patient patient) {
        if (CollectionUtils.isNotEmpty(person.getName()) && person.getName().get(0) != null
            && CollectionUtils.isNotEmpty(person.getName().get(0).getContent())) {
            for (Serializable object : person.getName().get(0).getContent()) {
                if (object instanceof JAXBElement<?>) {
                    Object nameValue = ((JAXBElement<?>) object).getValue();

                    if (nameValue instanceof EnExplicitFamily) {
                        patient.setLastName(((EnExplicitFamily) nameValue).getContent());
                    } else if (nameValue instanceof EnExplicitGiven) {
                        //the first given name is first name, following by middle name
                        String givenName = ((EnExplicitGiven) nameValue).getContent();
                        if (StringUtils.isNotBlank(patient.getFirstName())){
                            patient.setMiddleName(givenName);
                        }else{
                            patient.setFirstName(givenName);
                        }

                    }
                }
            }
        }

    }

    private void extractPidAndAAId(PRPAMT201310UV02Patient msgPatient, Patient patient) {
        if (msgPatient.getId() != null) {
            for (II idxId : msgPatient.getId()) {
                if (idxId != null && idxId.getExtension() != null && idxId.getRoot() != null) {
                    patient.setPid(idxId.getExtension());
                    patient.setAaId(idxId.getRoot());
                    break;
                }
            }
        }
    }

    private void extractAddress(PRPAMT201310UV02Person person, Patient patient) {
        if (CollectionUtils.isNotEmpty(person.getAddr()) && person.getAddr().get(0) != null
            && CollectionUtils.isNotEmpty(person.getAddr().get(0).getContent())) {

            for (Serializable object : person.getAddr().get(0).getContent()) {
                if (object instanceof JAXBElement<?>) {
                    Object value = ((JAXBElement<?>) object).getValue();

                    if (value instanceof AdxpExplicitStreetAddressLine) {
                        patient.setStreetAddr(((AdxpExplicitStreetAddressLine) value).getContent());
                    } else if (value instanceof AdxpExplicitPostalCode) {
                        patient.setZip(((AdxpExplicitPostalCode) value).getContent());
                    } else if (value instanceof AdxpExplicitState) {
                        patient.setState(((AdxpExplicitState) value).getContent());
                    } else if (value instanceof AdxpExplicitCity) {
                        patient.setCity(((AdxpExplicitCity) value).getContent());
                    } else {
                        LOG.warn("Address object not identified: " + value);
                    }
                }
            }
        }
    }

    private void extractSsn(PRPAMT201310UV02Person person, Patient patient) {
        if (person.getAsOtherIDs() != null) {
            for (PRPAMT201310UV02OtherIDs otherId : person.getAsOtherIDs()) {
                if (otherId.getId() != null) {
                    for (II id : otherId.getId()) {
                        patient.setSsn(id.getExtension());
                        return;
                    }
                }
            }
        }
    }

    private void extractTelephone(PRPAMT201310UV02Person person, Patient patient) {
        if (CollectionUtils.isNotEmpty(person.getTelecom()) && person.getTelecom().get(0) != null
            && StringUtils.isNotEmpty(person.getTelecom().get(0).getValue())) {

            patient.setPhone(person.getTelecom().get(0).getValue());
        }
    }

    private void extractGender(PRPAMT201310UV02Person person, Patient patient) {
        if (person.getAdministrativeGenderCode() != null && person.getAdministrativeGenderCode().getCode() != null
            && !person.getAdministrativeGenderCode().getCode().isEmpty()) {

            patient.setGender(person.getAdministrativeGenderCode().getCode());
        }
    }

    private void extractDob(PRPAMT201310UV02Person person, Patient patient) {
        if (person.getBirthTime() != null
            && StringUtils.isNotEmpty(person.getBirthTime().getValue())) {
            patient.setBirthDate(person.getBirthTime().getValue());
        }
    }
}
