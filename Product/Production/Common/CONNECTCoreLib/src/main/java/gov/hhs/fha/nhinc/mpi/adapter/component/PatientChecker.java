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
package gov.hhs.fha.nhinc.mpi.adapter.component;

import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201305;
import gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers.HL7Parser201306;
import gov.hhs.fha.nhinc.mpilib.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import org.hl7.v3.PRPAIN201305UV02;
/**
 *
 * @author Jon Hoppesch
 */
public class PatientChecker {

    private static Log log = LogFactory.getLog(PatientChecker.class);

    public static PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query) {
        log.debug("Entering PatientChecker.FindPatient method...");
        PRPAIN201306UV02 result = null;

        PRPAMT201306UV02ParameterList queryParams = HL7Parser201305.ExtractHL7QueryParamsFromMessage(query);

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7Parser201305.ExtractMpiPatientFromQueryParams(queryParams);
            log.info("perform patient lookup in mpi");

            log.info("source patient check 1 [" + sourcePatient.toString()+ "]");
            Patients searchResults = MpiDataAccess.LookupPatients(sourcePatient);
            if (CommonChecks.isZeroSearchResult(searchResults)) {
                log.info("patient not found in MPI");
                result = null;
            } else if (CommonChecks.isMultipleSearchResult(searchResults)) {
                log.info("multiple patients found in MPI [searchResults.size()=" + searchResults.size() + "]");
                result = null;
            } else {
                log.info("single patient found in MPI");
                Patient searchResultPatient = searchResults.get(0);
                log.info("Found patient " + searchResultPatient.toString());
            
                result = HL7Parser201306.BuildMessageFromMpiPatient (searchResultPatient, query);
            }
        }

        log.debug("Exiting PatientChecker.FindPatient method...");
        return result;
    }
}
