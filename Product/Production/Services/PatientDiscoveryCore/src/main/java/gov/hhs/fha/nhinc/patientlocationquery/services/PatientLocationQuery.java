/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.services;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import java.util.List;

/**
 * @author ptambellini
 *
 */
public interface PatientLocationQuery {

    public List<RecordLocatorService> getAllPatientsBy(String rlsId);

    /**
     * @param msg
     * @return
     */
    AdapterPatientLocationQueryResponseType getAdapterPLQResponse(AdapterPatientLocationQueryRequestType msg);

}
