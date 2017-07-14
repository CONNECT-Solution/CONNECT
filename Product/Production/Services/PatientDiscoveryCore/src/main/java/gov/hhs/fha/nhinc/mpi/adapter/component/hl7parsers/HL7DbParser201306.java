/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT150002UV01Organization;
import org.hl7.v3.COCTMT150003UV03Organization;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Receiver;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.PRPAMT201310UV02QueryMatchObservation;
import org.hl7.v3.PRPAMT201310UV02Subject;
import org.hl7.v3.ParticipationTargetSubject;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodIntentEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class HL7DbParser201306 {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Parser201306.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";

    /**
     * Method to build a PRPAIN201306UV02 from a given list of Patients and a PRPAIN201305UV02 object.
     *
     * @param patients A list of patients from which to build the PRPAIN201306UV02 for.
     * @param query the requesting PRPAIN201305UV02 object
     * @return a PRPAIN201306UV02 response object
     */
    public static PRPAIN201306UV02 buildMessageFromMpiPatients(List<Patient> patients, PRPAIN201305UV02 query) {
        LOG.trace("Entering HL7Parser201306.BuildMessageFromMpiPatients method...");

        PRPAIN201306UV02 msg = new PRPAIN201306UV02();

        // Set up message header fields
        msg.setITSVersion("XML_1.0");

        II id = new II();
        try {
            id.setRoot(PropertyAccessor.getInstance().getProperty(PROPERTY_FILE, PROPERTY_NAME));
        } catch (PropertyAccessException e) {
            LOG.error(
                "PropertyAccessException - Default Assigning Authority property not defined in adapter.properties",
                e);
            // CONNECT environment corrupt; return error response
            // return BuildMessageForError(<ERROR_CODE>, query);
        }
        id.setExtension(MessageIdGenerator.generateMessageId());
        msg.setId(id);
        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue(buildCreationTimeString());
        msg.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201306UV02");
        msg.setInteractionId(interactionId);

        CS processingCode = new CS();
        processingCode.setCode("P");
        msg.setProcessingCode(processingCode);

        CS processingModeCode = new CS();
        // processingModeCode.setCode("R");
        processingModeCode.setCode("T");
        msg.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();
        // ackCode.setCode("AL");
        ackCode.setCode("NE");
        msg.setAcceptAckCode(ackCode);

        msg.getAcknowledgement().add(createAck(query));

        // Set the receiver and sender
        msg.getReceiver().add(createReceiver(query.getSender()));
        msg.setSender(createSender(query.getReceiver().get(0)));

        msg.setControlActProcess(createControlActProcess(patients, query));

        LOG.trace("Exiting HL7Parser201306.BuildMessageFromMpiPatient method...");
        return msg;
    }

    static String buildCreationTimeString() {
        return DateTimeFormat.forPattern("yyyyMMddHHmmss").print(new DateTime(DateTimeZone.UTC));
    }

    private static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createControlActProcess(List<Patient> patients,
        PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess =
            new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        CD code = new CD();
        code.setCode("PRPA_TE201306UV02");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        // Add a subject for each value unique patient
        if ((patients != null) && (patients.size() > 0)) {
            for (Patient patient : patients) {
                controlActProcess.getSubject().add(createSubject(patient));
            }
        } else {
            LOG.info("createControlActProcess - No patients found to create subject");
        }

        // Add in query parameters
        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null) {
            controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }

        controlActProcess.setQueryAck(createQueryAck(query));

        // Set original QueryByParameter in response
        controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());

        return controlActProcess;
    }

    private static MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201305UV02 query) {
        MFMIMT700711UV01QueryAck result = new MFMIMT700711UV01QueryAck();

        if (query.getControlActProcess() != null && query.getControlActProcess().getQueryByParameter() != null
            && query.getControlActProcess().getQueryByParameter().getValue() != null
            && query.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {
            result.setQueryId(query.getControlActProcess().getQueryByParameter().getValue().getQueryId());
        }

        CS respCode = new CS();
        respCode.setCode("OK");
        result.setQueryResponseCode(respCode);

        return result;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(Patient patient) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        subject.setRegistrationEvent(createRegEvent(patient));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(Patient patient) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent =
            new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        regEvent.getMoodCode().add("EVN");
        regEvent.getClassCode().add("REG");
        II id = new II();
        id.getNullFlavor().add("NA");
        regEvent.getId().add(id);

        CS statusCode = new CS();
        statusCode.setCode("active");

        regEvent.setStatusCode(statusCode);

        regEvent.setSubject1(createSubject1(patient));

        regEvent.setCustodian(createCustodian());

        return regEvent;
    }

    private static MFMIMT700711UV01Custodian createCustodian() {
        MFMIMT700711UV01Custodian result = new MFMIMT700711UV01Custodian();
        result.getTypeCode().add("CST");
        result.setAssignedEntity(createAssignEntity());

        return result;
    }

    private static COCTMT090003UV01AssignedEntity createAssignEntity() {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        II id = new II();
        id.setRoot(HomeCommunityMap.getLocalHomeCommunityId());
        assignedEntity.getId().add(id);
        CE ce = new CE();
        ce.setCode("NotHealthDataLocator");
        ce.setCodeSystem("1.3.6.1.4.1.19376.1.2.27.2");
        assignedEntity.setCode(ce);

        return assignedEntity;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject1(Patient patient) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject.setTypeCode(ParticipationTargetSubject.SBJ);
        // Add in patient
        subject.setPatient(createPatient(patient));

        return subject;
    }

    private static PRPAMT201310UV02Patient createPatient(Patient patient) {
        PRPAMT201310UV02Patient subjectPatient = new PRPAMT201310UV02Patient();

        subjectPatient.getClassCode().add("PAT");

        CS statusCode = new CS();
        statusCode.setCode("SD");
        subjectPatient.setStatusCode(statusCode);

        // Add in patient id
        subjectPatient.getId().add(createSubjectId(patient));

        // Add in patient person
        subjectPatient.setPatientPerson(createPatientPerson(patient));

        // Add in provider organization
        subjectPatient.setProviderOrganization(createProviderOrg(patient));

        // Add in query match observation
        subjectPatient.getSubjectOf1().add(createSubjectOf1());

        return subjectPatient;
    }

    private static PRPAMT201310UV02Subject createSubjectOf1() {
        PRPAMT201310UV02Subject result = new PRPAMT201310UV02Subject();

        result.setQueryMatchObservation(createQueryMatch());
        return result;
    }

    private static PRPAMT201310UV02QueryMatchObservation createQueryMatch() {
        PRPAMT201310UV02QueryMatchObservation queryMatch = new PRPAMT201310UV02QueryMatchObservation();
        queryMatch.getMoodCode().add("EVN");
        queryMatch.getClassCode().add("CASE");
        CD code = new CD();
        code.setCode("IHE_PDQ");
        queryMatch.setCode(code);

        INT intValue = new INT();
        //CHECKSTYLE:OFF
        intValue.setValue(BigInteger.valueOf(100));
        //CHECKSTYLE:ON
        queryMatch.setValue(intValue);

        return queryMatch;
    }

    private static JAXBElement<COCTMT150003UV03Organization> createProviderOrg(Patient patient) {
        COCTMT150003UV03Organization org = new COCTMT150003UV03Organization();
        org.setDeterminerCode("INSTANCE");
        org.setClassCode("ORG");
        II id = new II();

        if (patient.getIdentifiers() != null && patient.getIdentifiers().size() > 0
            && patient.getIdentifiers().get(0).getOrganizationId() != null
            && patient.getIdentifiers().get(0).getOrganizationId().length() > 0) {
            id.setRoot(HomeCommunityMap.formatHomeCommunityId(patient.getIdentifiers().get(0).getOrganizationId()));
        }
        org.getId().add(id);

        org.getContactParty().add(null);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "providerOrganization");

        return new JAXBElement<>(xmlqname, COCTMT150003UV03Organization.class, org);
    }

    private static II createSubjectId(Patient patient) {
        II id = new II();

        if (patient.getIdentifiers() != null && patient.getIdentifiers().size() > 0
            && patient.getIdentifiers().get(0) != null) {

            if (patient.getIdentifiers().get(0).getOrganizationId() != null
                && patient.getIdentifiers().get(0).getOrganizationId().length() > 0) {
                LOG.info("Setting Patient Id root in 201306: " + patient.getIdentifiers().get(0).getOrganizationId());
                id.setRoot(HomeCommunityMap.formatHomeCommunityId(patient.getIdentifiers().get(0).getOrganizationId()));
            }

            if (patient.getIdentifiers().get(0).getId() != null
                && patient.getIdentifiers().get(0).getId().length() > 0) {
                LOG.info("Setting Patient Id extension in 201306: " + patient.getIdentifiers().get(0).getId());
                id.setExtension(patient.getIdentifiers().get(0).getId());
            }
        }

        return id;
    }

    private static JAXBElement<PRPAMT201310UV02Person> createPatientPerson(Patient patient) {
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

        // Set classCode
        person.getClassCode().add("PSN");

        // Set determinerCode
        person.setDeterminerCode("INSTANCE");

        // Set the Subject Gender
        if (patient.getGender() != null && patient.getGender().length() > 0) {
            person.setAdministrativeGenderCode(createGender(patient));
        }

        // Set the Subject Name
        if (patient.getPersonnames().size() > 0) {
            for (Personname name : patient.getPersonnames()) {
                person.getName().add(createSubjectName(name));
            }
        } else {
            person.getName().add(createSubjectName(patient));
        }

        // Set the Birth Time
        if (patient.getDateOfBirth() != null) {
            person.setBirthTime(createBirthTime(patient));
        }

        // Set the Addresses
        if (patient.getAddresses().size() > 0) {
            for (Address add : patient.getAddresses()) {
                person.getAddr().add(createAddress(add));
            }
        }

        // Set the phone Numbers
        if (patient.getPhonenumbers().size() > 0) {
            for (Phonenumber number : patient.getPhonenumbers()) {
                TELExplicit tele = HL7DataTransformHelper.createTELExplicit(number.getValue());

                person.getTelecom().add(tele);
            }
        }

        // Set the SSN
        if (patient.getSsn() != null && patient.getSsn().length() > 0) {
            person.getAsOtherIDs().add(createOtherIds(patient));
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");

        return new JAXBElement<>(xmlqname, PRPAMT201310UV02Person.class, person);
    }

    private static PRPAMT201310UV02OtherIDs createOtherIds(Patient patient) {
        PRPAMT201310UV02OtherIDs otherIds = new PRPAMT201310UV02OtherIDs();

        otherIds.getClassCode().add("SD");

        // Set the SSN
        if (patient.getSsn() != null && patient.getSsn().length() > 0) {
            II ssn = new II();
            ssn.setExtension(patient.getSsn());
            ssn.setRoot("2.16.840.1.113883.4.1");
            LOG.info("Setting Patient SSN in 201306 --> Patient SSN is not null." );
            otherIds.getId().add(ssn);

            COCTMT150002UV01Organization scopingOrg = new COCTMT150002UV01Organization();
            scopingOrg.setClassCode("ORG");
            scopingOrg.setDeterminerCode("INSTANCE");
            II orgId = new II();
            orgId.setRoot(ssn.getRoot());
            scopingOrg.getId().add(orgId);
            otherIds.setScopingOrganization(scopingOrg);
        }

        return otherIds;
    }

    private static TSExplicit createBirthTime(Patient patient) {
        TSExplicit birthTime = new TSExplicit();

        if (patient.getDateOfBirth() != null) {
            LOG.info("Setting Patient Birthday in 201306: " + patient.getDateOfBirth());
            UTCDateUtil utcDateUtil = new UTCDateUtil();
            // Format for date only, no time portion
            birthTime.setValue(utcDateUtil.formatUTCDateOnly(patient.getDateOfBirth()));
        }

        return birthTime;
    }

    private static PNExplicit createSubjectName(Patient patient) {
        return createSubjectName(patient.getPersonnames().get(0));
    }

    private static PNExplicit createSubjectName(Personname personname) {
        String lastName = personname.getLastName();
        String firstName = personname.getFirstName();
        String middleName = personname.getMiddleName();
        String prefix = personname.getPrefix();
        String suffix = personname.getSuffix();

        return HL7DataTransformHelper.createPNExplicit(firstName, middleName, lastName, prefix, suffix);
    }

    private static CE createGender(Patient patient) {
        CE gender = new CE();

        if (patient.getGender() != null && patient.getGender().length() > 0) {
            LOG.info("Setting Patient Gender in 201306: " + patient.getGender());
            gender.setCode(patient.getGender());
        }
        return gender;
    }

    private static MCCIMT000300UV01Acknowledgement createAck(PRPAIN201305UV02 query) {
        MCCIMT000300UV01Acknowledgement ack = new MCCIMT000300UV01Acknowledgement();
        ack.setTypeId(query.getInteractionId());

        CS typeCode = new CS();
        typeCode.setCode("AA");

        ack.setTypeCode(typeCode);

        return ack;
    }

    private static MCCIMT000300UV01Receiver createReceiver(MCCIMT000100UV01Sender querySender) {
        MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        String app = null;
        String oid = null;

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        if (querySender.getDevice() != null && NullChecker.isNotNullish(querySender.getDevice().getId())
            && querySender.getDevice().getId().get(0) != null
            && NullChecker.isNotNullish(querySender.getDevice().getId().get(0).getRoot())) {
            app = querySender.getDevice().getId().get(0).getRoot();
        }

        if (querySender.getDevice() != null
            && querySender.getDevice().getAsAgent() != null
            && querySender.getDevice().getAsAgent().getValue() != null
            && querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(querySender.getDevice().getAsAgent().getValue()
            .getRepresentedOrganization().getValue().getId())
            && querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(
            0) != null
            && NullChecker.isNotNullish(querySender.getDevice().getAsAgent().getValue()
            .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid =
                querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
                .get(0).getRoot();
        }

        MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        receiverDevice.setClassCode(EntityClassDevice.DEV);
        LOG.debug("Setting receiver device id (applicationId) to query sender's device id " + app);
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory(app));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(oid);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg =
            new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem =
            new JAXBElement<>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem =
            new JAXBElement<>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        receiverDevice.setAsAgent(agentElem);

        receiver.setDevice(receiverDevice);

        return receiver;
    }

    private static MCCIMT000300UV01Sender createSender(MCCIMT000100UV01Receiver queryReceiver) {
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        String app = null;
        String oid = null;

        sender.setTypeCode(CommunicationFunctionType.SND);

        if (queryReceiver.getDevice() != null && NullChecker.isNotNullish(queryReceiver.getDevice().getId())
            && queryReceiver.getDevice().getId().get(0) != null
            && NullChecker.isNotNullish(queryReceiver.getDevice().getId().get(0).getRoot())) {
            app = queryReceiver.getDevice().getId().get(0).getRoot();
        }

        if (queryReceiver.getDevice() != null
            && queryReceiver.getDevice().getAsAgent() != null
            && queryReceiver.getDevice().getAsAgent().getValue() != null
            && queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null
            && NullChecker.isNotNullish(queryReceiver.getDevice().getAsAgent().getValue()
            .getRepresentedOrganization().getValue().getId())
            && queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
            .get(0) != null
            && NullChecker.isNotNullish(queryReceiver.getDevice().getAsAgent().getValue()
            .getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid =
                queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()
                .get(0).getRoot();
        }

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        senderDevice.setClassCode(EntityClassDevice.DEV);
        LOG.debug("Setting sender device id (applicationId) to query receiver's device id " + app);
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory(app));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(oid);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg =
            new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem =
            new JAXBElement<>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem =
            new JAXBElement<>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }

    private static ADExplicit createAddress(Address add) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ADExplicit result = (factory.createADExplicit());
        List addrlist = result.getContent();

        if (add != null) {
            if (add.getStreet1() != null && add.getStreet1().length() > 0) {
                AdxpExplicitStreetAddressLine street = new AdxpExplicitStreetAddressLine();
                street.setContent(add.getStreet1());

                addrlist.add(factory.createADExplicitStreetAddressLine(street));
            }

            if (add.getStreet2() != null && add.getStreet2().length() > 0) {
                AdxpExplicitStreetAddressLine street = new AdxpExplicitStreetAddressLine();
                street.setContent(add.getStreet2());

                addrlist.add(factory.createADExplicitStreetAddressLine(street));
            }
            if (add.getCity() != null && add.getCity().length() > 0) {
                AdxpExplicitCity city = new AdxpExplicitCity();
                city.setContent(add.getCity());

                addrlist.add(factory.createADExplicitCity(city));
            }
            if (add.getState() != null && add.getState().length() > 0) {
                AdxpExplicitState state = new AdxpExplicitState();
                state.setContent(add.getState());

                addrlist.add(factory.createADExplicitState(state));
            }
            if (add.getPostal() != null && add.getPostal().length() > 0) {
                AdxpExplicitPostalCode zip = new AdxpExplicitPostalCode();
                zip.setContent(add.getPostal());

                addrlist.add(factory.createADExplicitPostalCode(zip));
            }
        }

        return result;
    }
}
