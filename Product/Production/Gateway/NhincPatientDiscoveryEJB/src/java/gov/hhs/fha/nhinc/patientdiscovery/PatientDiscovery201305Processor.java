/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
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

        // Set the sender and receiver OID for the response

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
                    createPatientCorrelation(requestPatId, patIdOverride, assertion);
                }
            } else {
                log.error("Policy Check Failed");
                response = createEmpty201306 (senderOID, receiverOID, request);
            }
        } else {
            log.warn("Patient Not Found");
            response = createEmpty201306 (senderOID, receiverOID, request);
        }
        return response;
    }

    protected boolean checkPolicy (PRPAIN201306UV02 response, II patIdOverride, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
    }

    protected PRPAIN201306UV02 createEmpty201306 (String senderOID, String receiverOID, PRPAIN201305UV02 request) {
        return HL7PRPA201306Transforms.createPRPA201306(null, senderOID, null, receiverOID, null, request);
    }

    protected void storeMapping(PRPAIN201305UV02 request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;


        if (request != null) {
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getId()) &&
                    request.getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getId().get(0).getRoot())) {
                hcid = request.getSender().getDevice().getId().get(0).getRoot();
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

    private void createPatientCorrelation(II remotePatient, II localPatient, AssertionType assertion) {
        AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
        QualifiedSubjectIdentifierType localSubId = new QualifiedSubjectIdentifierType();
        QualifiedSubjectIdentifierType remoteSubId = new QualifiedSubjectIdentifierType();

        if (remotePatient != null &&
                localPatient != null) {
            if (NullChecker.isNotNullish(localPatient.getExtension()) &&
                    NullChecker.isNotNullish(localPatient.getRoot())) {
                localSubId.setAssigningAuthorityIdentifier(localPatient.getRoot());
                localSubId.setSubjectIdentifier(localPatient.getExtension());
            }

            if (NullChecker.isNotNullish(remotePatient.getExtension()) &&
                    NullChecker.isNotNullish(remotePatient.getRoot())) {
                remoteSubId.setAssigningAuthorityIdentifier(remotePatient.getRoot());
                remoteSubId.setSubjectIdentifier(remotePatient.getExtension());

                // Add both ids to thst list
                request.getQualifiedPatientIdentifier().add(remoteSubId);
                log.info("Set Id 1 to PAT Id: " + remoteSubId.getSubjectIdentifier());
                log.info("Set Id 1 to AA Id: " + remoteSubId.getAssigningAuthorityIdentifier());
                request.getQualifiedPatientIdentifier().add(localSubId);
                log.info("Set Id 2 to PAT Id: " + localSubId.getSubjectIdentifier());
                log.info("Set Id 2 to AA Id: " + localSubId.getAssigningAuthorityIdentifier());
            }

            if (request.getQualifiedPatientIdentifier().isEmpty() == false) {
                PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
                PatientCorrelationFacadeProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();
                request.setAssertion(assertion);
                patCorrelationProxy.addPatientCorrelation(request);
            }
        }
    }

    private II providedPatientId(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        try
        {
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
                        for (PRPAMT201306UV02LivingSubjectId livingSubId:request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                            if (NullChecker.isNotNullish(livingSubId.getValue()) &&
                                    livingSubId.getValue().get(0) != null &&
                                    NullChecker.isNotNullish(livingSubId.getValue().get(0).getRoot()) &&
                                    NullChecker.isNotNullish(livingSubId.getValue().get(0).getExtension()) &&
                                    aaId.contentEquals(livingSubId.getValue().get(0).getRoot())){
                                patId = new II();
                                patId.setRoot(livingSubId.getValue().get(0).getRoot());
                                patId.setExtension(livingSubId.getValue().get(0).getExtension());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
            patId = null;
        }


        return patId;
    }
}
