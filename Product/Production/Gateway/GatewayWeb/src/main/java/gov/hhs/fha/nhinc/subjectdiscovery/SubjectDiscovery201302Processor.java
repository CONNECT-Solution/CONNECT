/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201302UV02OtherIDs;
import org.hl7.v3.PRPAMT201302UV02Patient;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscovery201302Processor {

    private static Log log = LogFactory.getLog(SubjectDiscovery201302Processor.class);

    public MCCIIN000002UV01 process201302(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();
        String ackMsg = null;

        // Store Assigning Authority to Home Community Id Mapping
        storeMapping(request);

        // Query the MPI to see if we have a match
        PRPAIN201306UV02 patient = queryMpi(request);

        // Check to see if the Patient was found
        if (patient != null &&
                patient.getControlActProcess() != null &&
                request != null &&
                request.getPRPAIN201302UV02() != null &&
                request.getPRPAIN201302UV02().getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject()) &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0) != null &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson() != null &&
                request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() != null &&
                NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs())) {

            //  Check that the patient id is in one of the Other Id fields
            II localId = null;
            for (II otherId : request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getAsOtherIDs().get(0).getId()) {
                localId = isPatientMatch(otherId, patient);
                if (localId != null) {
                   break;
                }
            }

            if (localId != null) {
                // Create a patient correlation
                createPatientCorrelation(request.getPRPAIN201302UV02(), localId, request.getAssertion());

                // Successfully processed the 201302 so create an ack reflecting success
                ackMsg = "Success";
                ack = ackCreater.createAck(request, ackMsg);
            } else {
                ackMsg = "Patient Not Found";
                log.warn(ackMsg);
                ack = ackCreater.createAck(request, ackMsg);
            }
        } else {
            ackMsg = "Patient Not Found";
            log.warn(ackMsg);
            ack = ackCreater.createAck(request, ackMsg);
        }

        return ack;
    }

    private void storeMapping(PIXConsumerPRPAIN201302UVRequestType request) {
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String hcid = null;
        String assigningAuthority = null;


        if (request != null &&
                request.getPRPAIN201302UV02() != null) {
            if (request.getPRPAIN201302UV02().getSender() != null &&
                    request.getPRPAIN201302UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot())) {
                hcid = request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            if (request.getPRPAIN201302UV02().getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject()) &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0) != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
                assigningAuthority = request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot();
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

    private PRPAIN201306UV02 queryMpi(PIXConsumerPRPAIN201302UVRequestType request) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();
        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        String oid = null;

        if (request != null &&
                request.getPRPAIN201302UV02() != null) {
            // Set the sender and receiver oid to be the receiver OID from the original 201301
            if (NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                oid = request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Extract patient from the 201302
            // We need to convert this to a 201301 Patient field for the data transformation method
            PRPAMT201302UV02Patient patient = null;
            PRPAMT201301UV02Patient patient201301 = new PRPAMT201301UV02Patient();
            JAXBElement<PRPAMT201301UV02Person> patientPerson = null;
            if (request.getPRPAIN201302UV02().getControlActProcess() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getControlActProcess().getSubject()) &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0) != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                    request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                patient = request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
                patientPerson = HL7PatientTransforms.create201301PatientPerson(request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName().get(0), null, null, null);
                patient201301.setPatientPerson(patientPerson);
                patient201301.getId().add(request.getPRPAIN201302UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0));

            }

            if (NullChecker.isNotNullish(oid) &&
                    patient != null) {
                // Create the 201305 to query the MPI
                query = HL7PRPA201305Transforms.createPRPA201305(patient201301, oid, oid, null);

                // Query the MPI to see if the patient is found
                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();

                log.debug("Calling Secured MPI");
                queryResults = mpiProxy.findCandidates(query, request.getAssertion());
            } else {
                queryResults = null;
            }
        } else {
            queryResults = null;
        }

        return queryResults;
    }

    private void createPatientCorrelation(PRPAIN201302UV02 remotePatient, II localId, AssertionType assertion) {
        AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
        QualifiedSubjectIdentifierType localSubId = new QualifiedSubjectIdentifierType();
        QualifiedSubjectIdentifierType remoteSubId = new QualifiedSubjectIdentifierType();

        if (remotePatient != null) {
            if (localId != null &&
                    NullChecker.isNotNullish(localId.getExtension()) &&
                    NullChecker.isNotNullish(localId.getRoot())) {
                localSubId.setAssigningAuthorityIdentifier(localId.getRoot());
                localSubId.setSubjectIdentifier(localId.getExtension());

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
                request.getQualifiedPatientIdentifier().add(localSubId);

            }
        }

        if (request.getQualifiedPatientIdentifier().isEmpty() == false) {
            PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
            PatientCorrelationFacadeProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();

            request.setAssertion(assertion);
            patCorrelationProxy.addPatientCorrelation(request);
        }
    }

    private II isPatientMatch(II remotePatient, PRPAIN201306UV02 localPatient) {
        II id = null;

        if (remotePatient != null &&
                NullChecker.isNotNullish(remotePatient.getExtension()) &&
                NullChecker.isNotNullish(remotePatient.getRoot()) &&
                localPatient != null &&
                localPatient.getControlActProcess() != null &&
                NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject()) &&
                localPatient.getControlActProcess().getSubject().get(0) != null &&
                localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
           if (remotePatient.getExtension().equalsIgnoreCase(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                   remotePatient.getRoot().equalsIgnoreCase(localPatient.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
               id = new II();
               id.setExtension(remotePatient.getExtension());
               id.setRoot(remotePatient.getRoot());
           }

        }
        return id;
    }
}
