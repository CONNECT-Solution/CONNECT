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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7DbParser201305;
import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7DbParser201306;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.service.PatientService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class PatientDbChecker implements AdapterComponentMpiChecker {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDbChecker.class);

    @Override
    public PRPAIN201306UV02 findPatient(PRPAIN201305UV02 query) {
        LOG.trace("Entering PatientDbChecker.FindPatient method...");
        PRPAIN201306UV02 result;

        PRPAMT201306UV02ParameterList queryParams = HL7DbParser201305.extractHL7QueryParamsFromMessage(query);
        List<Patient> filteredPatients = new ArrayList<>();

        if (queryParams == null) {
            LOG.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7DbParser201305.extractMpiPatientFromQueryParams(queryParams);

            // Perform find
            PatientService patientService = PatientService.getPatientService();
            List<Patient> patientList = patientService.findPatients(sourcePatient);

            if (CollectionUtils.isNotEmpty(patientList)) {

                List<String> dupOrgIds = new ArrayList<>();
                for (Patient patient : patientList) {
                    if (CollectionUtils.isNotEmpty(patient.getIdentifiers())
                        && patient.getIdentifiers().get(0).getOrganizationId() != null) {

                        for (Patient tempPatient : filteredPatients) {
                            if (tempPatient.getIdentifiers().get(0).getOrganizationId().equalsIgnoreCase(patient
                                .getIdentifiers().get(0).getOrganizationId())) {
                                dupOrgIds.add(patient.getIdentifiers().get(0).getOrganizationId());
                            }
                        }
                        filteredPatients.add(patient);
                    }
                }

                if (CollectionUtils.isNotEmpty(dupOrgIds)) {
                    HashSet<String> hashSet = new HashSet<>(dupOrgIds);
                    dupOrgIds = new ArrayList<>(hashSet);
                    LOG.debug("More than one matching patient found in some organizations. dupOrgIds.size(): "
                        + dupOrgIds.size());
                }

                for (Patient patient : patientList) {
                    if (CollectionUtils.isNotEmpty(patient.getIdentifiers())
                        && patient.getIdentifiers().get(0).getOrganizationId() != null) {

                        for (String str : dupOrgIds) {
                            if (patient.getIdentifiers().get(0).getOrganizationId().equalsIgnoreCase(str)) {
                                filteredPatients.remove(patient);
                            }
                        }
                    }
                }
                LOG.debug("After duplicates removed - filteredPatients.size(): " + filteredPatients.size());
            } else {
                // No matches found, generate appropriate empty response
                LOG.debug("No matches found, generate appropriate empty response");
            }
        }

        result = HL7DbParser201306.buildMessageFromMpiPatients(filteredPatients, query);

        LOG.trace("Exiting PatientDbChecker.FindPatient method...");
        return result;
    }

    /**
     *
     * @param query the PRPAIN201305UV02 object
     * @return true - minimum params found; false - not found
     */
    @Override
    public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query) {
        boolean result = false;

        PRPAMT201306UV02ParameterList queryParams = HL7DbParser201305.extractHL7QueryParamsFromMessage(query);
        Patient sourcePatient = HL7DbParser201305.extractMpiPatientFromQueryParams(queryParams);

        if (sourcePatient != null && CollectionUtils.isNotEmpty(sourcePatient.getPersonnames())
            && sourcePatient.getPersonnames().get(0) != null
            && NullChecker.isNotNullish(sourcePatient.getPersonnames().get(0).getFirstName())
            && NullChecker.isNotNullish(sourcePatient.getPersonnames().get(0).getLastName())
            && NullChecker.isNotNullish(sourcePatient.getGender()) && sourcePatient.getDateOfBirth() != null) {
            result = true;
        }

        return result;
    }
}
