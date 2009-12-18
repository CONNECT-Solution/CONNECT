/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import gov.hhs.fha.nhinc.transform.subdisc.HL7DataTransformHelper;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import org.hl7.v3.*;
import java.util.List;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author dunnek
 */
public class TestHelper {

    
    public PRPAIN201306UV02 build201306(PRPAMT201301UV02Patient patient, String oid, PRPAIN201305UV02 query)
    {
        //PRPAMT201301UV02Patient patient = HL7
        return HL7PRPA201306Transforms.createPRPA201306(patient, oid, oid, oid, oid, query);
    }
    public PRPAIN201306UV02 build201306(String firstName, String lastName, String gender, String birthTime, II subjectId)
    {
        PRPAIN201306UV02 msg = new PRPAIN201306UV02();

        msg.setITSVersion("XML_1.0");
        II id = new II();
        id.setRoot("1.1");
        msg.setId(id);


        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue("20090202000000");
        msg.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201306UV");
        msg.setInteractionId(id);
        
        CS processingCode = new CS();
        processingCode.setCode("P");
        msg.setProcessingCode(processingCode);
        

        CS processingModeCode = new CS();
        processingModeCode.setCode("R");
        msg.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();
        ackCode.setCode("AL");
        msg.setAcceptAckCode(ackCode);



        msg.setControlActProcess(createControlActProcess201306(firstName, lastName, gender, birthTime, subjectId));
        
        return msg;
    }


    public PRPAIN201305UV02 build201305(String firstName, String lastName, String gender, String birthTime, II subjectId) {
        PRPAIN201305UV02 msg = new PRPAIN201305UV02();

        // Set up message header fields
        msg.setITSVersion("XML_1.0");

        II id = new II();
        id.setRoot("1.1");
        msg.setId(id);

        TSExplicit creationTime = new TSExplicit();
        creationTime.setValue("20090202000000");
        msg.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.1.6");
        interactionId.setExtension("PRPA_IN201305UV");
        msg.setInteractionId(id);

        CS processingCode = new CS();
        processingCode.setCode("P");
        msg.setProcessingCode(processingCode);

        CS processingModeCode = new CS();
        processingModeCode.setCode("R");
        msg.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();
        ackCode.setCode("AL");
        msg.setAcceptAckCode(ackCode);

        // Set the receiver and sender
        msg.getReceiver().add(createReceiver());
        msg.setSender(createSender());

        msg.setControlActProcess(createControlActProcess(firstName, lastName, gender, birthTime, subjectId));

        return msg;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createControlActProcess(String firstName, String lastName, String gender, String birthTime, II subjectId) {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        CD code = new CD();
        code.setCode("PRPA_TE201305UV");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        controlActProcess.setQueryByParameter(createQueryParams(firstName, lastName, gender, birthTime, subjectId));

        return controlActProcess;
    }

    private PRPAIN201306UV02MFMIMT700711UV01ControlActProcess createControlActProcess201306(String firstName, String lastName, String gender, String birthTime, II subjectId) {
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        CD code = new CD();
        code.setCode("PRPA_TE201306UV");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);


        org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1 subj1;
        subj1 = createSubject(firstName, lastName, gender, "1");
        
        controlActProcess.getSubject().add(subj1);
        
        controlActProcess.setQueryByParameter(createQueryParams(firstName, lastName, gender, birthTime, subjectId));

        return controlActProcess;
    }
    private PRPAIN201306UV02MFMIMT700711UV01Subject1 createSubject(String firstName, String lastName, String gender, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();

        subject.getTypeCode().add("SUBJ");

        subject.setRegistrationEvent(createRegEvent(firstName, lastName, gender, orgId));

        return subject;
    }
      private  PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent createRegEvent(String firstName, String lastName, String gender, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent regEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();

        II id = new II();
        id.getNullFlavor().add("NA");
        regEvent.getId().add(id);

        CS statusCode = new CS();
        statusCode.setCode("active");

        regEvent.setStatusCode(statusCode);


        regEvent.setSubject1(createSubject2(firstName, lastName, gender, orgId));


        return regEvent;
    }
    public  PRPAIN201306UV02MFMIMT700711UV01Subject2 createSubject2(String firstName, String lastName, String gender, String orgId) {
        PRPAIN201306UV02MFMIMT700711UV01Subject2 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject2();

        PRPAMT201310UV02Patient pat310 = new PRPAMT201310UV02Patient();
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

        CE genderCode = new CE();
        genderCode.setCode("M");
        person.setAdministrativeGenderCode(genderCode);
        PNExplicit name = new PNExplicit();


        pat310.setPatientPerson( create201310PatientPerson(CreatePNExplicit(firstName, lastName),genderCode));


        subject.setPatient(pat310);

        return subject;
    }
    private JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(String firstName, String lastName, String gender, String birthTime, II subjectId) {
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        II id = new II();
        id.setRoot("12345");
        params.setQueryId(id);

        CS statusCode = new CS();
        statusCode.setCode("new");
        params.setStatusCode(statusCode);

        params.setParameterList(createParamList(firstName, lastName, gender, birthTime, subjectId));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryParams = new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, params);

        return queryParams;
    }

    private PRPAMT201306UV02ParameterList createParamList(String firstName, String lastName, String gender, String birthTime, II subjectId) {
        PRPAMT201306UV02ParameterList paramList = new PRPAMT201306UV02ParameterList();

        // Set the Subject Gender Code
        paramList.getLivingSubjectAdministrativeGender().add(createGender(gender));

        // Set the Subject Birth Time
        paramList.getLivingSubjectBirthTime().add(createBirthTime(birthTime));

        // Set the Subject Name
        paramList.getLivingSubjectName().add(createName(firstName, lastName));

        // Set the subject Id
        paramList.getLivingSubjectId().add(createSubjectId(subjectId));

        return paramList;
    }

    public PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
        if (subjectId != null) {
            id.getValue().add(subjectId);
        }

        return id;
    }

    private PRPAMT201306UV02LivingSubjectName createName(String firstName, String lastName) {
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit name = (ENExplicit) (factory.createENExplicit());
        List namelist = name.getContent();

        if (lastName != null &&
                lastName.length() > 0) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);

            namelist.add(factory.createENExplicitFamily(familyName));
        }

        if (firstName != null &&
                firstName.length() > 0) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);

            namelist.add(factory.createENExplicitGiven(givenName));
        }

        subjectName.getValue().add(name);

        return subjectName;
    }

    private PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null &&
                birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
        }

        return subjectBirthTime;
    }

    private PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        CE genderCode = new CE();

        if (gender != null &&
                gender.length() > 0) {
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);
        }

        return adminGender;
    }

    private MCCIMT000100UV01Receiver createReceiver() {
        MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

        receiver.setTypeCode(CommunicationFunctionType.RCV);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        TELExplicit url = new TELExplicit();
        url.setValue("http://localhost:9080/NhinConnect/AdapterComponentMpiService");
        device.getTelecom().add(url);

        receiver.setDevice(device);

        return receiver;
    }

    private  MCCIMT000100UV01Sender createSender() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
        device.setDeterminerCode("INSTANCE");

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        sender.setDevice(device);

        return sender;
    }

    public II createII(String root, String extension)
    {
        II result = new II();
        result.setRoot(root);
        result.setExtension(extension);

        return result;
    }
    public static JAXBElement<PRPAMT201310UV02Person> create201310PatientPerson(PNExplicit patName, CE gender) {
        PRPAMT201310UV02Person person = new PRPAMT201310UV02Person();

        // Set the Subject Name
        if (patName != null) {
            person.getName().add(patName);
        }

        // Set the Subject Gender
        if (gender != null) {
            person.setAdministrativeGenderCode(gender);
        }

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201310UV02Person> result = new JAXBElement<PRPAMT201310UV02Person>(xmlqname, PRPAMT201310UV02Person.class, person);

        return result;
    }

    public  PNExplicit CreatePNExplicit (String firstName, String lastName) {

        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        PNExplicit name = (PNExplicit) (factory.createPNExplicit());
        List namelist = name.getContent();

        {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);

            namelist.add(factory.createPNExplicitFamily(familyName));
        }

         {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);
            namelist.add(factory.createPNExplicitGiven(givenName));
        }

        return name;
    }
}
