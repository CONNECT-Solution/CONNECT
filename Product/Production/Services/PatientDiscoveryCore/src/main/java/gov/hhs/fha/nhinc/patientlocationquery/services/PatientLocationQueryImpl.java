/**
 *
 */
package gov.hhs.fha.nhinc.patientlocationquery.services;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQuerySecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQuerySecuredResponseType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.patientlocationquery.dao.RecordLocationServiceDAO;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
import java.util.List;
import org.hl7.v3.II;
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
    public List<RecordLocatorService> getAllPatientsBy(String requestedPatientId) {

        List<RecordLocatorService> requestedPatientIdList;

        requestedPatientIdList = RecordLocationServiceDAO.getAllPatientsBy(requestedPatientId);

        LOG.debug("RecordLocationService.findAllPatientsBy() - End");

        return requestedPatientIdList;
    }

    @Override
    public AdapterPatientLocationQueryResponseType getAdapterPLQResponse(AdapterPatientLocationQueryRequestType msg) {

        AdapterPatientLocationQueryResponseType response = new AdapterPatientLocationQueryResponseType();
        if (msg != null && msg.getPatientLocationQueryRequest() != null
            && msg.getPatientLocationQueryRequest().getRequestedPatientId() != null) {
            II requestedPatientId = msg.getPatientLocationQueryRequest().getRequestedPatientId();
            String requestedPatientid = msg.getPatientLocationQueryRequest().getRequestedPatientId().getExtension();

            List<RecordLocatorService> requestedPatientIdList = getAllPatientsBy(requestedPatientid);

            List<PatientLocationResponse> plqList = response.getPatientLocationQueryResponse()
                .getPatientLocationResponse();
            for (RecordLocatorService rec : requestedPatientIdList) {
                plqList.add(convertPatientLocationResponse(rec, requestedPatientId));
            }

        }
        return response;
    }

    @Override
    public AdapterPatientLocationQuerySecuredResponseType getAdapterPLQSecuredResponse(
        AdapterPatientLocationQuerySecuredRequestType msg) {

        AdapterPatientLocationQuerySecuredResponseType response = new AdapterPatientLocationQuerySecuredResponseType();
        response.setPatientLocationQueryResponse(new PatientLocationQueryResponseType());
        if (msg != null && msg.getPatientLocationQueryRequest() != null
            && msg.getPatientLocationQueryRequest().getRequestedPatientId() != null) {
            II requestPatientId = msg.getPatientLocationQueryRequest().getRequestedPatientId();
            String rlsid = msg.getPatientLocationQueryRequest().getRequestedPatientId().getExtension();

            List<RecordLocatorService> rlsList = getAllPatientsBy(rlsid);

            response.setPatientLocationQueryResponse(new PatientLocationQueryResponseType());
            List<PatientLocationResponse> plqList = response.getPatientLocationQueryResponse()
                .getPatientLocationResponse();
            for (RecordLocatorService rec : rlsList) {
                plqList.add(convertPatientLocationResponse(rec, requestPatientId));
            }
        }
        return response;
    }

    private static PatientLocationResponse convertPatientLocationResponse(RecordLocatorService rls,
        II requestPatientId) {
        PatientLocationResponse plr = new PatientLocationResponse();
        plr.setHomeCommunityId(rls.getAssigningAuthorityId());
        II corespondingPatientId = new II();
        corespondingPatientId.setRoot(rls.getAssigningAuthorityId());
        corespondingPatientId.setExtension(rls.getPatientId());
        plr.setCorrespondingPatientId(corespondingPatientId);
        plr.setRequestedPatientId(requestPatientId);
        return plr;
    }
}
