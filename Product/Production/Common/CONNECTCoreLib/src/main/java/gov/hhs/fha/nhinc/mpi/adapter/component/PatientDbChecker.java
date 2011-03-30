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

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7DbParser201305.ExtractMpiPatientFromQueryParams(queryParams);

            // Check for required NHIN query parameters
            if (!isNhinRequiredParamsFound(sourcePatient)) {
                // Not all required NHIN query parameters found, generate appropriate empty response
                log.debug("Not all required NHIN query parameters found, generate appropriate empty response");
            } else {
                // Minimum required NHIN query parameters found, perform find
                PatientService patientService = PatientService.getPatientService();
                List<Patient> patientList = patientService.findPatients(sourcePatient);

                if (patientList != null && patientList.size() > 0) {
                    int counter = 0;
                    for (Patient patient : patientList) {
                        log.debug("patientList[" + counter + "] = " + patient.toString());
                    }

                    // At lease one match found, generate response checking for duplicates within each AA
                    log.debug("At lease one match found, generate response checking for duplicates");
                    //result = HL7DbParser201306.BuildMessageFromMpiPatients(patientList, query);
                } else {
                    // No matches found, generate appropriate empty response
                    log.debug("No matches found, generate appropriate empty response");
                }
            }
        }

        log.debug("Exiting PatientDbChecker.FindPatient method...");
        return result;
    }

    private boolean isNhinRequiredParamsFound(Patient sourcePatient) {
        boolean result = false;

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
