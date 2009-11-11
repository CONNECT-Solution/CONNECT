/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy.NhinSubjectDiscoveryProxy;
import gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy.NhinSubjectDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201302Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscovery201301Processor {

    private static Log log = LogFactory.getLog(SubjectDiscovery201301Processor.class);

    public MCCIIN000002UV01 process201301(PIXConsumerPRPAIN201301UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();
        String ackMsg = null;

        // Store Assigning Authority to Home Community Id Mapping
        storeMapping(request);

        // Query the MPI to see if we have a match
        PRPAIN201306UV02 patient = queryMpi(request);

        // Check to see if the Patient was found
        if (patient != null &&
                patient.getControlActProcess() != null) {
            II patIdOverride = new II();

            if (NullChecker.isNotNullish(patient.getControlActProcess().getSubject()) &&
                    patient.getControlActProcess().getSubject().get(0) != null &&
                    patient.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                patIdOverride.setExtension(patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
                patIdOverride.setRoot(patient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
            } else {
                patIdOverride = null;
            }

            // Check to make sure the policy is valid

            SubjectDiscoveryPolicyChecker policyChecker = new SubjectDiscoveryPolicyChecker();
            if (policyChecker.check201301Policy(request, patIdOverride)) {
                // Create 201302
                PRPAIN201302UV02 msg201302 = create201302(patient, request.getPRPAIN201301UV02());

                // Create a patient correlation
                createPatientCorrelation(request.getPRPAIN201301UV02(), msg201302, request.getAssertion());

                // Set up the target
                NhinTargetSystemType target = new NhinTargetSystemType();
                HomeCommunityType hc = new HomeCommunityType();
                hc.setHomeCommunityId(msg201302.getReceiver().get(0).getDevice().getId().get(0).getRoot());
                target.setHomeCommunity(hc);

                // Send 201302 to requesting Gateway
                NhinSubjectDiscoveryProxyObjectFactory sdFactory = new NhinSubjectDiscoveryProxyObjectFactory();
                NhinSubjectDiscoveryProxy proxy = sdFactory.getNhinSubjectDiscoveryProxy();
                MCCIIN000002UV01 ack201302 = proxy.pixConsumerPRPAIN201302UV(msg201302, request.getAssertion(), target);

                // Successfully processed the 201301 so create an ack reflecting success
                ackMsg = "Success";
                ack = ackCreater.createAck(request, ackMsg);
            } else {
                ackMsg = "Policy Check failed for the 201301 request";
                log.error(ackMsg);
                ack = ackCreater.createAck(request, ackMsg);
            }
        } else {
            ackMsg = "Patient Not Found";
            log.warn(ackMsg);
            ack = ackCreater.createAck(request, ackMsg);
        }

        return ack;
    }

    private void storeMapping(PIXConsumerPRPAIN201301UVRequestType request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;


        if (request != null &&
                request.getPRPAIN201301UV02() != null) {
            if (request.getPRPAIN201301UV02().getSender() != null &&
                    request.getPRPAIN201301UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot())) {
                hcid = request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            if (request.getPRPAIN201301UV02().getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getControlActProcess().getSubject()) &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0) != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                assigningAuthority = request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot();
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

    private PRPAIN201306UV02 queryMpi(PIXConsumerPRPAIN201301UVRequestType request) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        String oid = null;

        if (request != null &&
                request.getPRPAIN201301UV02() != null) {
            // Set the sender and receiver oid to be the receiver OID from the original 201301
            if (NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                oid = request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            } else {
                log.error("Failed to extract OID from receiver node in 201301");
            }

            // Extract patient from the 201301
            PRPAMT201301UV02Patient patient = null;
            if (request.getPRPAIN201301UV02().getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getControlActProcess().getSubject()) &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0) != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                patient = request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
            } else {
                log.error("Failed to extract PATIENT from receiver node in 201301");
            }

            if (NullChecker.isNotNullish(oid) &&
                    patient != null) {
                // Create the 201305 to query the MPI
                query = HL7PRPA201305Transforms.createPRPA201305(patient, oid, oid, null);

                // Query the MPI to see if the patient is found
                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
                log.info("Sending query to the Secured MPI");
                queryResults = mpiProxy.findCandidates(query, request.getAssertion());
            } else {
                queryResults = null;
            }
        } else {
            log.error("MPI Request is null");
            queryResults = null;
        }

        return queryResults;
    }

    private PRPAIN201302UV02 create201302(PRPAIN201306UV02 localPatient, PRPAIN201301UV02 request) {
        PRPAIN201302UV02 result = new PRPAIN201302UV02();
        PRPAMT201310UV02Patient patient = new PRPAMT201310UV02Patient();
        String senderOID = null;
        String receiverOID = null;
        String remotePatId = null;
        String remoteDeviceId = null;

        if (localPatient != null &&
                request != null) {
            // Extract the patient and ids from the 201306
            if (localPatient.getControlActProcess() != null &&
                    NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject()) &&
                    localPatient.getControlActProcess().getSubject().get(0) != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                patient = localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();

            }

            if (request.getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject()) &&
                    request.getControlActProcess().getSubject().get(0) != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                    NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                remotePatId = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension();
                remoteDeviceId = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot();
            }


            // Set the Sender OID to the Receiver OID from the original request
            if (NullChecker.isNotNullish(request.getReceiver()) &&
                    request.getReceiver().get(0) != null &&
                    request.getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getId()) &&
                    request.getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the Receiver OID to the Sender OID from the original request
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getId()) &&
                    request.getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getId().get(0).getRoot())) {
                senderOID = request.getSender().getDevice().getId().get(0).getRoot();
            }

            if (patient != null &&
                    NullChecker.isNotNullish(senderOID) &&
                    NullChecker.isNotNullish(receiverOID) &&
                    NullChecker.isNotNullish(remotePatId) &&
                    NullChecker.isNotNullish(remoteDeviceId)) {
                result = HL7PRPA201302Transforms.createPRPA201302(patient, remotePatId, remoteDeviceId, receiverOID, senderOID);
            } else {
                result = null;
            }
        } else {
            result = null;
        }
        return result;
    }

    private void createPatientCorrelation(PRPAIN201301UV02 remotePatient, PRPAIN201302UV02 localPatient, AssertionType assertion) {
        AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
        QualifiedSubjectIdentifierType localSubId = new QualifiedSubjectIdentifierType();
        QualifiedSubjectIdentifierType remoteSubId = new QualifiedSubjectIdentifierType();

        if (remotePatient != null &&
                localPatient != null) {
            if (localPatient.getControlActProcess() != null &&
                    NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject()) &&
                    localPatient.getControlActProcess().getSubject().get(0) != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null) {
                if (NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                        NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                    localSubId.setAssigningAuthorityIdentifier(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
                    localSubId.setSubjectIdentifier(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
                }

            }

            if (remotePatient.getControlActProcess() != null &&
                    NullChecker.isNotNullish(remotePatient.getControlActProcess().getSubject()) &&
                    remotePatient.getControlActProcess().getSubject().get(0) != null &&
                    remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null) {
                if (NullChecker.isNotNullish(remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                        NullChecker.isNotNullish(remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                    remoteSubId.setAssigningAuthorityIdentifier(remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
                    remoteSubId.setSubjectIdentifier(remotePatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());

                    // Add both ids to thst list
                    request.getQualifiedPatientIdentifier().add(remoteSubId);
                    log.info("Set Id 1 to PAT Id: " + remoteSubId.getSubjectIdentifier());
                    log.info("Set Id 1 to AA Id: " + remoteSubId.getAssigningAuthorityIdentifier());
                    request.getQualifiedPatientIdentifier().add(localSubId);
                    log.info("Set Id 2 to PAT Id: " + localSubId.getSubjectIdentifier());
                    log.info("Set Id 2 to AA Id: " + localSubId.getAssigningAuthorityIdentifier());
                }

            }

            if (request.getQualifiedPatientIdentifier().isEmpty() == false) {
                PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
                PatientCorrelationFacadeProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();
                request.setAssertion(assertion);
                patCorrelationProxy.addPatientCorrelation(request);
            }
        }
    }
}
