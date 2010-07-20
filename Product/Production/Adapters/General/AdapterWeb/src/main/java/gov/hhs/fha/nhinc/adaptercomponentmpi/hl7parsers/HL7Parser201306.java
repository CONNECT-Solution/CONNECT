/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers;

import gov.hhs.fha.nhinc.mpilib.*;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import java.math.BigInteger;
import java.util.List;
import java.util.TimeZone;
import java.util.GregorianCalendar;
//import javax.xml.datatype.XMLGregorianCalendar;
//import javax.xml.datatype.DatatypeFactory;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hl7.v3.*;


/**
 *
 * @author Jon Hoppesch
 */
public class HL7Parser201306 {

    private static Log log = LogFactory.getLog(HL7Parser201306.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";
    private static final String DEFAULT_AA_OID = "1.1";

    public static PRPAIN201306UV02 BuildMessageFromMpiPatient(Patient patient, PRPAIN201305UV02 query) {
        log.debug("Entering HL7Parser201306.BuildMessageFromMpiPatient method...");

        PRPAIN201306UV02 msg = new PRPAIN201306UV02();

        // Set up message header fields
        msg.setITSVersion("XML_1.0");

        II id = new II();
        try {
           id.setRoot(PropertyAccessor.getProperty(PROPERTY_FILE, PROPERTY_NAME));
        }
        catch (Exception e) {
            id.setRoot(DEFAULT_AA_OID);
        }
           id.setExtension(MessageIdGenerator.GenerateMessageId());
        msg.setId(id);

        // Set up the creation time string
        String timestamp = "";
        try {
            GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    
            timestamp = String.valueOf(today.get(GregorianCalendar.YEAR)) +
                        String.valueOf(today.get(GregorianCalendar.MONTH)+1) +
                        String.valueOf(today.get(GregorianCalendar.DAY_OF_MONTH)) +
                        String.valueOf(today.get(GregorianCalendar.HOUR_OF_DAY)) +
                        String.valueOf(today.get(GregorianCalendar.MINUTE)) +
                        String.valueOf(today.get(GregorianCalendar.SECOND));
        } catch (Exception e) {
            log.error("Exception when creating XMLGregorian Date");
            log.error(" message: " + e.getMessage());
        }
        
        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue(timestamp);
        msg.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201306UV");
        msg.setInteractionId(interactionId);

        CS processingCode = new CS();
        processingCode.setCode("P");
        msg.setProcessingCode(processingCode);

        CS processingModeCode = new CS();
        processingModeCode.setCode("R");
        msg.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();
        ackCode.setCode("AL");
        msg.setAcceptAckCode(ackCode);

        msg.getAcknowledgement().add(createAck(query));

        // Set the receiver and sender
        msg.getReceiver().add(createReceiver(query.getSender()));
        msg.setSender(createSender(query.getReceiver().get(0)));

        msg.setControlActProcess(createControlActProcess(patient, query));

        log.debug("Exiting HL7Parser201306.BuildMessageFromMpiPatient method...");
        return msg;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createControlActProcess(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        CD code = new CD();
        code.setCode("PRPA_TE201306UV");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        controlActProcess.getSubject().add(createSubject(patient, query));

        // Add in query parameters
        if (query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null) {
           controlActProcess.setQueryByParameter(query.getControlActProcess().getQueryByParameter());
        }
        
        controlActProcess.setQueryAck(createQueryAck(query));

        MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
        authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);

        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        try {
           id.setRoot(PropertyAccessor.getProperty(PROPERTY_FILE, PROPERTY_NAME));
        }
        catch (Exception e) {
            id.setRoot(DEFAULT_AA_OID);
        }

        assignedDevice.getId().add(id);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = new JAXBElement<COCTMT090300UV01AssignedDevice>(xmlqname, COCTMT090300UV01AssignedDevice.class, assignedDevice);

        authorOrPerformer.setAssignedDevice(assignedDeviceJAXBElement);

        controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
        
        return controlActProcess;
    }
    
    private static MFMIMT700711UV01QueryAck createQueryAck(PRPAIN201305UV02 query) {
        MFMIMT700711UV01QueryAck  result = new MFMIMT700711UV01QueryAck();
        
        if (query.getControlActProcess() != null &&
                query.getControlActProcess().getQueryByParameter() != null &&
                query.getControlActProcess().getQueryByParameter().getValue() != null &&
                query.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {
           result.setQueryId(query.getControlActProcess().getQueryByParameter().getValue().getQueryId());
        }
        
        CS respCode = new CS();
        respCode.setCode("OK");
        result.setQueryResponseCode(respCode);
        
        INT totalQuanity = new INT();
        totalQuanity.setValue(BigInteger.valueOf(1));
        result.setResultTotalQuantity(totalQuanity);
        
        INT currQuanity = new INT();
        currQuanity.setValue(BigInteger.valueOf(1));
        result.setResultCurrentQuantity(currQuanity);
        
        
        INT remainQuanity = new INT();
        remainQuanity.setValue(BigInteger.valueOf(0));
        result.setResultRemainingQuantity(remainQuanity);

        return result;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        subject.setRegistrationEvent(createRegEvent(patient, query));

        return subject;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();

        II id = new II();
        id.getNullFlavor().add("NA");
        regEvent.getId().add(id);

        CS statusCode = new CS();
        statusCode.setCode("active");

        regEvent.setStatusCode(statusCode);

        regEvent.setSubject1(createSubject1(patient, query));
        
        regEvent.setCustodian(createCustodian(patient));
        
        return regEvent;
    }
    
    private static MFMIMT700711UV01Custodian createCustodian (Patient patient) {
        MFMIMT700711UV01Custodian result = new MFMIMT700711UV01Custodian();
        
        result.setAssignedEntity(createAssignEntity(patient));
        
        return result;
    }
    
    private static COCTMT090003UV01AssignedEntity createAssignEntity (Patient patient) {
        COCTMT090003UV01AssignedEntity  assignedEntity = new COCTMT090003UV01AssignedEntity();
        
        II id = new II();
        id.setRoot(patient.getIdentifiers().get(0).getOrganizationId());       
        assignedEntity.getId().add(id);
        
        return assignedEntity;
    }

    private static PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject1(Patient patient, PRPAIN201305UV02 query) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();

        // Add in patient
        subject.setPatient(createPatient(patient, query));
        
        return subject;
    }

    private static PRPAMT201310UV02Patient createPatient(Patient patient, PRPAIN201305UV02 query) {
        PRPAMT201310UV02Patient subjectPatient = new PRPAMT201310UV02Patient();

        subjectPatient.getClassCode().add("PAT");
        
        // TODO: Temporary value until confirmation can be made on the actual value
        CS statusCode = new CS();
        statusCode.setCode("SD");
        subjectPatient.setStatusCode(statusCode);

        // Add in patient id 
        subjectPatient.getId().add(createSubjectId(patient));
        
        // Add in patient person
        subjectPatient.setPatientPerson(createPatientPerson(patient, query));
        
        // Add in provider organization
        subjectPatient.setProviderOrganization(createProviderOrg(patient));
        
        // Add in query match observation
        subjectPatient.getSubjectOf1().add(createSubjectOf1());
        
        return subjectPatient;
    }
    
    private static PRPAMT201310UV02Subject createSubjectOf1 () {
        PRPAMT201310UV02Subject result = new PRPAMT201310UV02Subject();
        
        result.setQueryMatchObservation(createQueryMatch());
        return result;
    }
    
    private static PRPAMT201310UV02QueryMatchObservation createQueryMatch () {
        PRPAMT201310UV02QueryMatchObservation queryMatch = new PRPAMT201310UV02QueryMatchObservation();
        
        CD code = new CD();
        code.setCode("IHE_PDQ");
        queryMatch.setCode(code);
        
        INT intValue = new INT();
        intValue.setValue(BigInteger.valueOf(100));
        queryMatch.setValue(intValue);
        
        return queryMatch;
    }
    
    private static JAXBElement<COCTMT150003UV03Organization> createProviderOrg (Patient patient) {
        COCTMT150003UV03Organization org = new COCTMT150003UV03Organization();
        
        II id = new II();
        
        if (patient.getIdentifiers() != null &&
                patient.getIdentifiers().size() > 0 &&
                patient.getIdentifiers().get(0).getOrganizationId() != null &&
                patient.getIdentifiers().get(0).getOrganizationId().length() > 0) {
           id.setRoot(patient.getIdentifiers().get(0).getOrganizationId());
        }
        org.getId().add(id);
        
        org.getContactParty().add(null);
        
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "providerOrganization");
        JAXBElement<COCTMT150003UV03Organization> result = new JAXBElement<COCTMT150003UV03Organization>(xmlqname, COCTMT150003UV03Organization.class, org);
    
        return result;
    }
    
    private static II createSubjectId (Patient patient) {
        II id = new II();
        
        if (patient.getIdentifiers() != null &&
                patient.getIdentifiers().size() > 0 &&
                patient.getIdentifiers().get(0) != null) {
            
            if (patient.getIdentifiers().get(0).getOrganizationId() != null &&
                patient.getIdentifiers().get(0).getOrganizationId().length() > 0) {
                log.info("Setting Patient Id root in 201306: " + patient.getIdentifiers().get(0).getOrganizationId());
                id.setRoot(patient.getIdentifiers().get(0).getOrganizationId());
            }
        
            if (patient.getIdentifiers().get(0).getId() != null &&
                patient.getIdentifiers().get(0).getId().length() > 0) { 
                log.info("Setting Patient Id extension in 201306: " + patient.getIdentifiers().get(0).getId());
                id.setExtension(patient.getIdentifiers().get(0).getId());
            }
        }
        
        return id;
    }

    private static JAXBElement<PRPAMT201310UV02Person> createPatientPerson (Patient patient, PRPAIN201305UV02 query) {
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();
        
        // Set the Subject Gender
        if (patient.getGender() != null &&
                patient.getGender().length() > 0) {
           person.setAdministrativeGenderCode(createGender(patient));
        }
        
        // Set the Subject Name        
        if(patient.getNames().size() > 0)
        {
            for(PersonName name : patient.getNames())
            {
                 person.getName().add(createSubjectName(name));
            }
        }
        else
        {
            person.getName().add(createSubjectName(patient));
        }
        
        // Set the Birth Time
        if (patient.getDateOfBirth() != null &&
                patient.getDateOfBirth().length() > 0) {
           person.setBirthTime(createBirthTime(patient));
        }
        
        // Set the Address
        if (patient.getAddresses().size() > 0 ) {
            for(Address add : patient.getAddresses())
            {
               //ADExplicit address =  HL7DataTransformHelper.CreateADExplicit(add.getStreet1(),
               //        add.getStreet2(), add.getCity(), add.getState(), add.getZip());

               person.getAddr().add(createAddress(add));
            }
           
        }


        //Set the phone Numbers
        if(patient.getPhoneNumbers().size() > 0)
        {
            for(PhoneNumber number : patient.getPhoneNumbers())
            {
                TELExplicit tele = HL7DataTransformHelper.createTELExplicit(number.getPhoneNumber());

                person.getTelecom().add(tele);
            }
        }

        
        // Set the SSN
        if (patient.getSSN() != null &&
                patient.getSSN().length() > 0) {
           person.getAsOtherIDs().add(createOtherIds(patient));
        }
        
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> result = new JAXBElement<PRPAMT201310UV02Person>(xmlqname, PRPAMT201310UV02Person.class, person);
        
        return result;
    }

    private static TELExplicit createPhoneNumber(String number)
    {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        TELExplicit result = (TELExplicit) (factory.createTELExplicit());
        result.setValue(number);

        return result;

    }
    private static ADExplicit createAddress (Address add)
    {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ADExplicit result =  (ADExplicit) (factory.createADExplicit());
        List addrlist = result.getContent();

        if(add != null)
        {
            if (add.getStreet1() != null &&
                   add.getStreet1().length() > 0) {
                AdxpExplicitStreetAddressLine street = new AdxpExplicitStreetAddressLine();
                street.setContent(add.getStreet1());

                addrlist.add(factory.createADExplicitStreetAddressLine(street));
            }

            if (add.getStreet2() != null &&
                   add.getStreet2().length() > 0) {
                AdxpExplicitStreetAddressLine street = new AdxpExplicitStreetAddressLine();
                street.setContent(add.getStreet2());

                addrlist.add(factory.createADExplicitStreetAddressLine(street));
            }
            if(add.getCity() != null && add.getCity().length() > 0)
            {
                AdxpExplicitCity city = new AdxpExplicitCity();
                city.setContent(add.getCity());

                addrlist.add(factory.createADExplicitCity(city));
            }
            if (add.getState() != null &&
                    add.getState().length() > 0) {
                AdxpExplicitState state = new AdxpExplicitState();
                state.setContent(add.getState());

                addrlist.add(factory.createADExplicitState(state));
            }
            if (add.getZip() != null &&
                    add.getZip().length() > 0) {
                AdxpExplicitPostalCode zip = new AdxpExplicitPostalCode();
                zip.setContent(add.getZip());

                addrlist.add(factory.createADExplicitPostalCode(zip));
            }
        }




        return result;

    }
    private static ADExplicit createAddress (Patient patient) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ADExplicit address =  (ADExplicit) (factory.createADExplicit());
        List addrlist = address.getContent();
        
        if (patient.getAddress() != null) {
            if (patient.getAddress().getCity() != null &&
                    patient.getAddress().getCity().length() > 0) {
                AdxpExplicitCity city = new AdxpExplicitCity();
                city.setContent(patient.getAddress().getCity());

                addrlist.add(factory.createADExplicitCity(city));
            }
            
            if (patient.getAddress().getState() != null &&
                    patient.getAddress().getState().length() > 0) {
                AdxpExplicitState state = new AdxpExplicitState();
                state.setContent(patient.getAddress().getState());

                addrlist.add(factory.createADExplicitState(state));
            }
            
            if (patient.getAddress().getStreet1() != null &&
                    patient.getAddress().getStreet1().length() > 0) {
                AdxpExplicitStreetAddressLine street = new AdxpExplicitStreetAddressLine();
                street.setContent(patient.getAddress().getStreet1());

                addrlist.add(factory.createADExplicitStreetAddressLine(street));
            }
            
            if (patient.getAddress().getZip() != null &&
                    patient.getAddress().getZip().length() > 0) {
                AdxpExplicitPostalCode zip = new AdxpExplicitPostalCode();
                zip.setContent(patient.getAddress().getZip());

                addrlist.add(factory.createADExplicitPostalCode(zip));
            }
        }
        
        return address;
    }
    
    private static PRPAMT201310UV02OtherIDs createOtherIds (Patient patient) {
        PRPAMT201310UV02OtherIDs  otherIds = new PRPAMT201310UV02OtherIDs();
        
        // TODO: Temporary assignment until actual value can be determined
        otherIds.getClassCode().add("SD");
        
        // Set the SSN
        if (patient.getSSN() != null &&
                patient.getSSN().length() > 0) {
            II ssn = new II();
            ssn.setExtension(patient.getSSN());
            ssn.setRoot("2.16.840.1.113883.4.1");
            log.info("Setting Patient SSN in 201306: " + patient.getSSN());
            otherIds.getId().add(ssn);
            
            COCTMT150002UV01Organization scopingOrg = new COCTMT150002UV01Organization();
            II orgId = new II();
            orgId.setRoot(ssn.getRoot());
            scopingOrg.getId().add(orgId);
            otherIds.setScopingOrganization(scopingOrg);
        }
        
        return otherIds;
    }
    
    
    private static TSExplicit createBirthTime (Patient patient) {
        TSExplicit birthTime = new TSExplicit();
        
        if (patient.getDateOfBirth() != null &&
                patient.getDateOfBirth().length() > 0) {
            log.info("Setting Patient Birthday in 201306: " + patient.getDateOfBirth());
            birthTime.setValue(patient.getDateOfBirth());
        }
        
        return birthTime;
    }
    
    private static PNExplicit createSubjectName (Patient patient) {       
        return createSubjectName(patient.getName());
    }
    private static PNExplicit createSubjectName (PersonName personName) {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());
        List namelist = name.getContent();

        String lastName =  personName.getLastName();
        String firstName =personName.getFirstName();
        String middleName= personName.getMiddleName();
        String prefix = personName.getTitle();
        String suffix = personName.getSuffix();


        name = HL7DataTransformHelper.CreatePNExplicit(firstName, middleName, lastName, prefix, suffix);

        return name;
    }
    private static CE createGender (Patient patient) {
        CE gender = new CE();
        
        if (patient.getGender() != null &&
                patient.getGender().length() > 0) {
            log.info("Setting Patient Gender in 201306: " + patient.getGender());
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
        String oid = null;

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        if (querySender.getDevice() != null &&
                querySender.getDevice().getAsAgent() != null &&
                querySender.getDevice().getAsAgent().getValue() != null &&
                querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = querySender.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        MCCIMT000300UV01Device receiverDevice = new MCCIMT000300UV01Device();
        receiverDevice.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);

        log.debug("Setting receiver application to 1.2.345.678.999");
        receiverDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.RECEIVER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(oid);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        receiverDevice.setAsAgent(agentElem);

        receiver.setDevice(receiverDevice);

        return receiver;
    }

    private static MCCIMT000300UV01Sender createSender(MCCIMT000100UV01Receiver queryReceiver) {
        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        String oid = null;

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setDeterminerCode("INSTANCE");

        if (queryReceiver.getDevice() != null &&
                queryReceiver.getDevice().getAsAgent() != null &&
                queryReceiver.getDevice().getAsAgent().getValue() != null &&
                queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            oid = queryReceiver.getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        }

        MCCIMT000300UV01Device senderDevice = new MCCIMT000300UV01Device();
        senderDevice.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);

        log.debug("Setting sender OID to 1.2.345.678.999");
        senderDevice.getId().add(HL7DataTransformHelper.IIFactory("1.2.345.678.999"));

        MCCIMT000300UV01Agent agent = new MCCIMT000300UV01Agent();
        MCCIMT000300UV01Organization org = new MCCIMT000300UV01Organization();
        org.setClassCode(HL7Constants.ORG_CLASS_CODE);
        org.setDeterminerCode(HL7Constants.SENDER_DETERMINER_CODE);
        II id = HL7DataTransformHelper.IIFactory(oid);
        org.getId().add(id);

        javax.xml.namespace.QName xmlqnameorg = new javax.xml.namespace.QName("urn:hl7-org:v3", "representedOrganization");
        JAXBElement<MCCIMT000300UV01Organization> orgElem = new JAXBElement<MCCIMT000300UV01Organization>(xmlqnameorg, MCCIMT000300UV01Organization.class, org);
        agent.setRepresentedOrganization(orgElem);
        agent.getClassCode().add(HL7Constants.AGENT_CLASS_CODE);

        javax.xml.namespace.QName xmlqnameagent = new javax.xml.namespace.QName("urn:hl7-org:v3", "asAgent");
        JAXBElement<MCCIMT000300UV01Agent> agentElem = new JAXBElement<MCCIMT000300UV01Agent>(xmlqnameagent, MCCIMT000300UV01Agent.class, agent);

        senderDevice.setAsAgent(agentElem);

        sender.setDevice(senderDevice);

        return sender;
    }

}
