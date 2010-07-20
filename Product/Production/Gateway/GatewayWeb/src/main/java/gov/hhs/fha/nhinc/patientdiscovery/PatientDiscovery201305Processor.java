/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscovery201305Processor {

    private static Log log = LogFactory.getLog(PatientDiscovery201305Processor.class);

    public PRPAIN201306UV02 process201305(PRPAIN201305UV02 request, AssertionType assertion) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        String senderOID = null;
        String receiverOID = null;

        // Store Assigning Authority to Home Community Id Mapping
        storeMapping(request);

        // Query the MPI to see if we have a match
        response = queryMpi(request, assertion);

        // Check to see if the Patient was found
        if (response != null &&
                response.getControlActProcess() != null) {
            II patIdOverride = new II();

            if (NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                    response.getControlActProcess().getSubject().get(0) != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                patIdOverride.setExtension(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
                patIdOverride.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
            } else {
                patIdOverride = null;
            }

            // Check to make sure the policy is valid
            if (checkPolicy(response, patIdOverride, assertion)) {
                II requestPatId = providedPatientId(request);
                if (requestPatId != null) {
                    // Create a patient correlation
                    createPatientCorrelation(response, patIdOverride, assertion, request);
                }
            } else {
                log.error("Policy Check Failed");
                response = createEmpty201306(senderOID, receiverOID, request);
            }
        } else {
            log.warn("Patient Not Found");
            response = createEmpty201306(senderOID, receiverOID, request);
        }
        return response;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, II patIdOverride, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
    }

    protected PRPAIN201306UV02 createEmpty201306(String senderOID, String receiverOID, PRPAIN201305UV02 request) {
        return HL7PRPA201306Transforms.createPRPA201306(null, senderOID, null, receiverOID, null, request);
    }

    protected void storeMapping(PRPAIN201305UV02 request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;


        if (request != null) {
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                hcid = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            if (request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                    request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
                assigningAuthority = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
            }

        }

        if (NullChecker.isNotNullish(hcid) &&
                NullChecker.isNotNullish(assigningAuthority)) {
            boolean result = mappingDao.storeMapping(hcid, assigningAuthority);

            if (result == false) {
                log.warn("Failed to store home community - assigning authority mapping");
            }

        }
    }

    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            log.info("Sending query to the Secured MPI");
            queryResults =
                    mpiProxy.findCandidates(query, assertion);

        } else {
            log.error("MPI Request is null");
            queryResults =
                    null;
        }

        return queryResults;
    }

    private void createPatientCorrelation(PRPAIN201306UV02 queryResult, II localPatId, AssertionType assertion, PRPAIN201305UV02 query) {
        PRPAIN201301UV02 request = new PRPAIN201301UV02();
        String localAA = null;
        if (queryResult != null &&
                localPatId != null &&
                NullChecker.isNotNullish(localPatId.getRoot()) &&
                NullChecker.isNotNullish(localPatId.getExtension()) &&
                assertion != null) {
            if (queryResult.getControlActProcess() != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject()) &&
                    queryResult.getControlActProcess().getSubject().get(0) != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                localAA = queryResult.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot();
            }
            request = HL7PRPA201301Transforms.createPRPA201301(query, localAA);
            if (request != null &&
                    request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                    request.getControlActProcess().getSubject().get(0) != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
                // Need to Switch patient ids so the sender and reciever match.  This is to avoid an exception by the Patient Correlation Component
                II remotePatient = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localPatId);
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remotePatient);

                PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();

                patCorrelationProxy.addPatientCorrelation(request, assertion, null);
            }
        } else {
            log.error("Null parameter passed to createPatientCorrelation method, no correlation created");
        }
    }

    private II providedPatientId(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        try {
            if (request != null &&
                    request.getControlActProcess() != null) {
                if (NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                        request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                        request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                        request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                        NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                        request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                        NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
                    aaId = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
                }

                if (NullChecker.isNotNullish(aaId)) {
                    if (request.getControlActProcess().getQueryByParameter() != null &&
                            request.getControlActProcess().getQueryByParameter().getValue() != null &&
                            request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                            NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {
                        for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                            for (II id : livingSubId.getValue()) {
                                if (id != null &&
                                        NullChecker.isNotNullish(id.getRoot()) &&
                                        NullChecker.isNotNullish(id.getExtension()) &&
                                        aaId.contentEquals(id.getRoot())) {
                                    patId = new II();
                                    patId.setRoot(id.getRoot());
                                    patId.setExtension(id.getExtension());

                                    // break out of inner loop
                                    break;
                                }
                            }

                            // If the patient id was found then break out of outer loop
                            if (patId != null) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            patId = null;
        }

        return patId;
    }
}
