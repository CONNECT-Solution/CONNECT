/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.services;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import java.util.List;

/**
 * @author ptambellini
 *
 */
public interface PatientLocationQuery {

    public List<RecordLocatorService> getAllPatientsBy(String rlsId);

}
