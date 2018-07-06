/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.services;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.patientlocationquery.dao.RecordLocationServiceDAO;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ptambellini
 *
 */
public class PatientLocationQueryImpl implements PatientLocationQuery {
    private static final Logger LOG = LoggerFactory.getLogger(PatientLocationQuery.class);
    private static PatientLocationQuery patientLocationQuery = new PatientLocationQueryImpl();

    private PatientLocationQueryImpl() {
        LOG.info("PatientLocationQueryImpl - Initialized");
    }

    public static PatientLocationQuery getPatientLocationQuery() {
        LOG.debug("getPatientLocationQuery()..");
        return patientLocationQuery;
    }

    @Override
    public List<RecordLocatorService> getAllPatientsBy(String rlsId) {

        List<RecordLocatorService> rlsList;

        rlsList = RecordLocationServiceDAO.getAllPatientsBy(rlsId);

        LOG.debug("RecordLocationService.findAllPatientsBy() - End");

        return rlsList;
    }

    @Override
    public AdapterPatientLocationQueryResponseType getAdapterPLQResponse(AdapterPatientLocationQueryRequestType msg) {
        AdapterPatientLocationQueryResponseType response = new AdapterPatientLocationQueryResponseType();
        List<PatientLocationResponse> rlsList = response.getPatientLocationQueryResponse().getPatientLocationResponse();
        rlsList.add(new PatientLocationResponse());

        return response;
    }

}
