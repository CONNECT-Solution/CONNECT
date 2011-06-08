/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7DbParser201305;
import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7DbParser201306;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdb.model.*;
import gov.hhs.fha.nhinc.patientdb.service.PatientService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author richard.ettema
 */
public class PatientDbChecker implements AdapterComponentMpiChecker {

    private static Log log = LogFactory.getLog(PatientDbChecker.class);

    public PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query) {
        log.debug("Entering PatientDbChecker.FindPatient method...");
        PRPAIN201306UV02 result = null;

        PRPAMT201306UV02ParameterList queryParams = HL7DbParser201305.ExtractHL7QueryParamsFromMessage(query);
        List<Patient> filteredPatients = new ArrayList<Patient>();

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7DbParser201305.ExtractMpiPatientFromQueryParams(queryParams);

            // Perform find
            PatientService patientService = PatientService.getPatientService();
            List<Patient> patientList = patientService.findPatients(sourcePatient);

            if (patientList != null && patientList.size() > 0) {

                List<String> dupOrgIds = new ArrayList<String>();
                for (Patient patient : patientList) {
                    if ((patient.getIdentifiers() != null) &&
                            (patient.getIdentifiers().size() > 0) &&
                            (patient.getIdentifiers().get(0).getOrganizationId() != null)) {

                        for (Patient tempPatient : filteredPatients) {
                            if ((tempPatient.getIdentifiers().get(0).getOrganizationId()).equalsIgnoreCase(patient.getIdentifiers().get(0).getOrganizationId())) {
                                dupOrgIds.add(patient.getIdentifiers().get(0).getOrganizationId());
                            }
                        }
                        filteredPatients.add(patient);
                    }
                }

                if ((dupOrgIds != null) &&
                        (dupOrgIds.size() > 0)) {
                    HashSet hashSet = new HashSet(dupOrgIds);
                    dupOrgIds = new ArrayList(hashSet);
                    log.debug("More than one matching patient found in some organizations. dupOrgIds.size(): " + dupOrgIds.size());
                }

                for (Patient patient : patientList) {
                    if ((patient.getIdentifiers() != null) &&
                            (patient.getIdentifiers().size() > 0) &&
                            (patient.getIdentifiers().get(0).getOrganizationId() != null)) {

                        for (String str : dupOrgIds) {
                            if ((patient.getIdentifiers().get(0).getOrganizationId()).equalsIgnoreCase(str)) {
                                filteredPatients.remove(patient);
                            }
                        }
                    }
                }

                if (filteredPatients != null) {
                    log.debug("After duplicates removed - filteredPatients.size(): " + filteredPatients.size());
                } else {
                    log.debug("filteredPatients - null");
                }

            } else {
                // No matches found, generate appropriate empty response
                log.debug("No matches found, generate appropriate empty response");
            }
        }

        result = HL7DbParser201306.BuildMessageFromMpiPatients(filteredPatients, query);

        log.debug("Exiting PatientDbChecker.FindPatient method...");
        return result;
    }

    /**
     *
     * @param query
     * @return true - minimum params found; false - not found
     */
    public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query) {
        boolean result = false;

        PRPAMT201306UV02ParameterList queryParams = HL7DbParser201305.ExtractHL7QueryParamsFromMessage(query);
        Patient sourcePatient = HL7DbParser201305.ExtractMpiPatientFromQueryParams(queryParams);

        if (sourcePatient != null &&
                sourcePatient.getPersonnames() != null &&
                sourcePatient.getPersonnames().size() > 0 &&
                sourcePatient.getPersonnames().get(0) != null &&
                NullChecker.isNotNullish(sourcePatient.getPersonnames().get(0).getFirstName()) &&
                NullChecker.isNotNullish(sourcePatient.getPersonnames().get(0).getLastName()) &&
                NullChecker.isNotNullish(sourcePatient.getGender()) &&
                sourcePatient.getDateOfBirth() != null) {
            result = true;
        }

        return result;
    }
}
