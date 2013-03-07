package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201301UV;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT090003UV01Device;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author achidambaram
 * 
 */
public class TestPRPAIN201301UVParser {

    private Appender appenderMock;

    @Before
    public void setupAppender() {
        appenderMock = mock(Appender.class);
        Logger.getRootLogger().addAppender(appenderMock);
    }

    @Test
    public void testParseSubjectFromMessage() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 returnedSubject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        returnedSubject = parser.parseSubjectFromMessage(createPRPAIN201301UV02());
        assertEquals(returnedSubject.getRegistrationEvent().getCustodian().getAssignedEntity().getAssignedDevice()
                .getValue().getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void ParseSubjectFromMessageWhenMessageNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = null;
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1= parser.parseSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ParseSubjectFromMessageWhenControlActProcessNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1= parser.parseSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void ParseSubjectFromMessageWhenSubjectNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        message.setControlActProcess(controlActProcess);
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject1= parser.parseSubjectFromMessage(message);
        assertNull(subject1);
    }

    @Test
    public void testParseHL7PatientFromMessage() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        patient = parser.parseHL7PatientFromMessage(createPRPAIN201301UV02());
        assertEquals(patient.getPatientPerson().getValue().getDeterminerCode(), "INSTANCE");
    }

    @Test
    public void testParseHL7PatientFromMessageWhenSubjectNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        assertNull(parser.parseHL7PatientFromMessage(message));

    }

    @Test
    public void testParseHL7PatientFromMessageWhenregistrationEventNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = createHL7MessageWhereregistrationEventNull();
        assertNull(parser.parseHL7PatientFromMessage(message));
    }

    @Test
    public void testParseHL7PatientFromMessageWhenSubject1Null() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = createMessageWhereSubject1Null();
        assertNull(parser.parseHL7PatientFromMessage(message));
    }

    @Test
    public void testParseHL7PatientFromMessageWhenPatientNull() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAIN201301UV02 message = createMessageWherePatientNull();
        assertNull(parser.parseHL7PatientFromMessage(message));
    }

    @Test
    public void testParseHL7PatientPersonFrom201301Message() {
        PRPAIN201301UVParser parser = new PRPAIN201301UVParser();
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        patient = parser.parseHL7PatientPersonFrom201301Message(createPRPAIN201301UV02());
    }

    private PRPAIN201301UV02 createMessageWherePatientNull() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject1 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        subject1.setTypeId(createTypeId());
        registrationevent.setSubject1(subject1);
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<PRPAIN201301UV02MFMIMT700701UV01Subject1>();

        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createMessageWhereSubject1Null() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent registrationevent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<PRPAIN201301UV02MFMIMT700701UV01Subject1>();
        registrationevent.setTypeId(createTypeId());
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(registrationevent);
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createHL7MessageWhereregistrationEventNull() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<PRPAIN201301UV02MFMIMT700701UV01Subject1>();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subjects.add(subject);
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        message.setControlActProcess(createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        return message;
    }

    private CS createCS() {
        CS code = new CS();
        code.setCode("CONNECT");
        code.setCodeSystem("CONNECTSystem");
        code.setCodeSystemName("CONNECTDomain");
        code.setDisplayName("CONNECT4.0");
        code.setCodeSystemVersion("4.0");
        return code;
    }

    private PRPAIN201301UV02MFMIMT700701UV01ControlActProcess createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess() {
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        controlActProcess.getSubject().addAll(createPRPAIN201301UV02MFMIMT700701UV01Subject1());
        return controlActProcess;
    }

    private List<PRPAIN201301UV02MFMIMT700701UV01Subject1> createPRPAIN201301UV02MFMIMT700701UV01Subject1() {
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<PRPAIN201301UV02MFMIMT700701UV01Subject1>();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        subject.setRegistrationEvent(createPRPAIN201301UV02MFMIMT700701UV01RegistrationEvent());
        subjects.add(subject);
        return subjects;
    }

    private PRPAIN201301UV02MFMIMT700701UV01Subject2 createSubject2() {
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        subject.setPatient(createPRPAMT201301UV02Patient());
        subject.setTypeId(createTypeId());
        return subject;
    }

    private PRPAMT201301UV02Patient createPRPAMT201301UV02Patient() {
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<PRPAMT201301UV02Person>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        return patient;
    }

    private PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent createPRPAIN201301UV02MFMIMT700701UV01RegistrationEvent() {
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        regEvent.setStatusCode(createCS());
        regEvent.setTypeId(createTypeId());
        regEvent.setCustodian(createMFMIMT700701UV01Custodian());
        regEvent.setSubject1(createSubject2());
        return regEvent;
    }

    private MFMIMT700701UV01Custodian createMFMIMT700701UV01Custodian() {
        MFMIMT700701UV01Custodian custodian = new MFMIMT700701UV01Custodian();
        custodian.setTypeId(createTypeId());
        custodian.setContextControlCode("ContextControlCode");
        custodian.setAssignedEntity(createCOCTM090003UV01AssignedEntity());
        return custodian;
    }

    private COCTMT090003UV01AssignedEntity createCOCTM090003UV01AssignedEntity() {
        COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode("ClassCode");
        assignedEntity.setTypeId(createTypeId());
        assignedEntity.setCode(createCE());
        assignedEntity.getId().add(createTypeId());
        assignedEntity.setAssignedDevice(createAssignedDevice());
        return assignedEntity;
    }

    private JAXBElement<COCTMT090003UV01Device> createAssignedDevice() {
        COCTMT090003UV01Device device = new COCTMT090003UV01Device();
        device.setDeterminerCode("INSTANCE");
        device.setTypeId(createTypeId());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "device");
        JAXBElement<COCTMT090003UV01Device> assignedDevice = new JAXBElement<COCTMT090003UV01Device>(xmlqname,
                COCTMT090003UV01Device.class, device);
        return assignedDevice;
    }

    private CE createCE() {
        CE ce = new CE();
        ce.setCode("CONNECT");
        ce.setCodeSystem("CONNECTSystem");
        ce.setCodeSystemName("CONNECTDomain");
        ce.setDisplayName("CONNECT4.0");
        ce.setCodeSystemVersion("4.0");
        return ce;
    }

    private CD createCD() {
        CD cd = new CD();
        cd.setCode("CONNECT");
        cd.setCodeSystem("CONNECTSystem");
        cd.setCodeSystemName("CONNECTDomain");
        cd.setDisplayName("CONNECT4.0");
        cd.setCodeSystemVersion("4.0");
        return cd;
    }

    private II createTypeId() {
        II typeId = new II();
        typeId.setAssigningAuthorityName("1.1");
        typeId.setExtension("D123401");
        typeId.setRoot("1.1");
        return typeId;
    }

}
