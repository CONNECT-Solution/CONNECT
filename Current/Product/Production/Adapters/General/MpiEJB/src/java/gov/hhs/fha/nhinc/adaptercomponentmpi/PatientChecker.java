/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptercomponentmpi;

import gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers.HL7Parser201305;
import gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers.HL7Parser201306;
import gov.hhs.fha.nhinc.mpilib.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author Jon Hoppesch
 */
public class PatientChecker {

    private static Log log = LogFactory.getLog(PatientChecker.class);

    public static PRPAIN201306UV FindPatient(PRPAIN201305UV query) {
        log.debug("Entering PatientChecker.FindPatient method...");
        PRPAIN201306UV result = null;

        PRPAMT201306UVParameterList queryParams = HL7Parser201305.ExtractHL7QueryParamsFromMessage(query);

        if (queryParams == null) {
            log.error("no query parameters were supplied");
        } else {
            Patient sourcePatient = HL7Parser201305.ExtractMpiPatientFromQueryParams(queryParams);
            log.info("perform patient lookup in mpi");

            log.info("source patient check 1 [" + sourcePatient.getName().getLastName() + "]");
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
                log.info("Found patient " + searchResultPatient.getName().getFirstName() + " " + searchResultPatient.getName().getLastName());
            
                result = HL7Parser201306.BuildMessageFromMpiPatient (searchResultPatient, query);
            }
        }

        log.debug("Exiting PatientChecker.FindPatient method...");
        return result;
    }
}
