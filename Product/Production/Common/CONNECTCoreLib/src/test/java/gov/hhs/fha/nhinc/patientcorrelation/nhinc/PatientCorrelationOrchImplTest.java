/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDao;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.QualifiedPatientIdentifier;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.COCTMT090003UV01Device;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01ControlActProcess;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1;
import org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject2;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * @author achidambaram
 *
 */
public class PatientCorrelationOrchImplTest {

    CorrelatedIdentifiersDao dao = mock(CorrelatedIdentifiersDao.class);

    @Test
    public void testRetrievePatientCorrelations() {
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao) {
            @Override
            protected List<QualifiedPatientIdentifier> retrieveQualifiedPatientIdentifiers(QualifiedPatientIdentifier inputQualifiedPatientIdentifier,
                    List<String> dataSourceList) {
                List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = new ArrayList<>();
                QualifiedPatientIdentifier patIdentifier = new QualifiedPatientIdentifier();
                patIdentifier.setAssigningAuthority("2.2");
                patIdentifier.setPatientId("D123401");
                qualifiedPatientIdentifiers.add(patIdentifier);
                return qualifiedPatientIdentifiers;
            }

        };

        try {
               result = orchImpl.retrievePatientCorrelations(createMessage(),
                createAssertion());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(result.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getRoot(),"2.2");
        assertEquals(result.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                .getSubject1().getPatient().getId().get(0).getExtension(),"D123401");
    }


    @Test
    public void testRetrievePatientCorrelationsWhenPatIdentifierNull(){
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.retrievePatientCorrelations(createMessagePatIdentifierNull(),
                createAssertion()));

    }


    @Test
    public void testRetrievePatientCorrelationsWhenDataSourceNull(){
        RetrievePatientCorrelationsResponseType result = new RetrievePatientCorrelationsResponseType();
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao) {
            @Override
            protected List<QualifiedPatientIdentifier> retrieveQualifiedPatientIdentifiers(QualifiedPatientIdentifier inputQualifiedPatientIdentifier,
                    List<String> dataSourceList) {
                List<QualifiedPatientIdentifier> qualifiedPatientIdentifiers = new ArrayList<>();
                QualifiedPatientIdentifier patIdentifier = new QualifiedPatientIdentifier();
                patIdentifier.setAssigningAuthority("2.2");
                patIdentifier.setPatientId("D123401");
                qualifiedPatientIdentifiers.add(patIdentifier);
                return qualifiedPatientIdentifiers;
            }

        };

        try {
               result = orchImpl.retrievePatientCorrelations(createMessageForRetrieveWhenDataSourceNull(),
                createAssertion());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result.getPRPAIN201310UV02().getControlActProcess().getQueryByParameter().getValue()
                .getParameterList().getDataSource().isEmpty());
    }

    @Test
    public void testRetrievePatientCorrelationsWhenPatIdentifierValueNull(){
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.retrievePatientCorrelations(createMessagePatIdentifierValueNull(),
                createAssertion()));

    }


    private PRPAIN201309UV02 createMessagePatIdentifierValueNull() {
        PRPAIN201309UV02 message = createMessageForRetrieveNullConditions();
        PRPAMT201307UV02PatientIdentifier patIdentifier = new PRPAMT201307UV02PatientIdentifier();
        message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientIdentifier().add(patIdentifier);
        return message;
    }


    private PRPAIN201309UV02 createMessageForRetrieveWhenDataSourceNull() {
        PRPAIN201309UV02 message = createMessageForRetrieveNullConditions();
        message.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientIdentifier().add(
                createPRPAMT201307UV02PatientIdentifier());
        return message;
    }

    private PRPAIN201309UV02 createMessageForRetrieveNullConditions() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        PRPAMT201307UV02QueryByParameter parameter = new  PRPAMT201307UV02QueryByParameter();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter =
                new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        parameter.setParameterList(parameterList);
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess =
                new  PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setQueryByParameter(queryByParameter);
        message.setControlActProcess(controlActProcess);
        return message;
    }

    private PRPAIN201309UV02 createMessagePatIdentifierNull() {
        return createMessageForRetrieveNullConditions();
    }

    private AssertionType createAssertion() {
        return new AssertionType();
    }

    private PRPAIN201309UV02 createMessage() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        message.setControlActProcess(createControlActProcess());
        return message;
    }

    private PRPAIN201309UV02QUQIMT021001UV01ControlActProcess createControlActProcess() {
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess =
                new  PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setQueryByParameter(createQueryByParameter());
        return controlActProcess;

    }

    private JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameter() {
        PRPAMT201307UV02QueryByParameter parameter = new  PRPAMT201307UV02QueryByParameter();
        parameter.setQueryId(createII());
        parameter.setParameterList(createPRPAMT201307UV02ParameterList());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        return new JAXBElement<>(xmlqname,
        PRPAMT201307UV02QueryByParameter.class, parameter);
    }


    private PRPAMT201307UV02ParameterList createPRPAMT201307UV02ParameterList() {
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        parameterList.getPatientIdentifier().add(createPRPAMT201307UV02PatientIdentifier());
        parameterList.getDataSource().add(createPRPAMT201307UV02DataSource());
        return parameterList;
    }

    private PRPAMT201307UV02DataSource createPRPAMT201307UV02DataSource() {
        PRPAMT201307UV02DataSource datasource = new PRPAMT201307UV02DataSource();
        datasource.setTypeId(createII());
        datasource.getValue().add(createII());
        return datasource;
    }

    private PRPAMT201307UV02PatientIdentifier createPRPAMT201307UV02PatientIdentifier() {
        PRPAMT201307UV02PatientIdentifier patIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patIdentifier.setTypeId(createII());
        patIdentifier.getValue().add(createII());
        return patIdentifier;
    }


    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }

    @Test
    public void addPatientCorrelation() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        AddPatientCorrelationResponseType result = orchImpl.addPatientCorrelation(createPRPAIN201301UV02(), createAssertion());
        assertEquals(result.getMCCIIN000002UV01().getReceiver().get(0).getDevice().getId()
                .get(0).getAssigningAuthorityName(), "CONNECT");
    }

    @Test
    public void addPatientCorrelationWhenPatientNull() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.addPatientCorrelation(createPRPAIN201301UV02NullConditions(), createAssertion()));
    }

    @Test
    public void addPatientCorrelationWhenPatientId0Null() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.addPatientCorrelation(createPRPAIN201301UV02Id0Null(), createAssertion()));
    }

    @Test
    public void addPatientCorrelationWhenPatientId1Null() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.addPatientCorrelation(createPRPAIN201301UV02Id1Null() , createAssertion()));
    }

    @Test
    public void addPatientCorrelationWhenPatientId0AANull() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.addPatientCorrelation(createPRPAIN201301UV02Id0AANull() , createAssertion()));
    }

    @Test
    public void addPatientCorrelationWhenPatientId1AANull() {
        PatientCorrelationOrchImpl orchImpl = new PatientCorrelationOrchImpl(dao);
        assertNull(orchImpl.addPatientCorrelation(createPRPAIN201301UV02Id1AANull() , createAssertion()));
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02Id1AANull() {
        PRPAIN201301UV02 message = createPRPAIN201301UV02Id0AANull();
        message.getControlActProcess().getSubject().get(0).getRegistrationEvent()
            .getSubject1().getPatient().getId().get(0).setExtension("D123401");
        return message;

    }

    private PRPAIN201301UV02 createPRPAIN201301UV02Id0AANull() {
        PRPAIN201301UV02 message = createPRPAIN201301UV02IdsEmpty();
        List<II> ii = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId();
        II ii0 = new II();
        ii0.setAssigningAuthorityName("CONNECT");
        ii0.setRoot("1.1");
        ii.add(ii0);
        II ii1 = new II();
        ii1.setAssigningAuthorityName("CONNECT2");
        ii1.setRoot("2.2");
        ii.add(ii1);
        message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().addAll(ii);
        return message;
    }


    private PRPAIN201301UV02 createPRPAIN201301UV02Id1Null() {
        PRPAIN201301UV02 message = createPRPAIN201301UV02IdsEmpty();
        List<II> iiList = message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId();
        II ii0 = createTypeId();
        iiList.add(ii0);
        II ii1 = null;
        iiList.add(ii1);
        message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(ii0);
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02Id0Null() {
        PRPAIN201301UV02 message = createPRPAIN201301UV02IdsEmpty();
        II ii0 = null;
        message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(ii0);
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02IdsEmpty() {
        PRPAIN201301UV02 message = createPRPAIN201301UV02NullConditions();
        org.hl7.v3.PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        PRPAMT201301UV02Person patientPerson = new PRPAMT201301UV02Person();
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "patientPerson");
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");

        PNExplicit patientName = new PNExplicit();
        patientName.getNullFlavor().add("NA");
        message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().setPatient(patient);
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02NullConditions() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        PRPAIN201301UV02MFMIMT700701UV01ControlActProcess controlActProcess = new PRPAIN201301UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setTypeId(createTypeId());
        controlActProcess.setCode(createCD());
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
        PRPAIN201301UV02MFMIMT700701UV01Subject1 subject = new PRPAIN201301UV02MFMIMT700701UV01Subject1();
        subject.setTypeId(createTypeId());
        PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regEvent = new PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
        regEvent.setStatusCode(createCS());
        regEvent.setTypeId(createTypeId());
        regEvent.setCustodian(createMFMIMT700701UV01Custodian());
        PRPAIN201301UV02MFMIMT700701UV01Subject2 subject2 = new PRPAIN201301UV02MFMIMT700701UV01Subject2();
        subject2.setTypeId(createTypeId());
        regEvent.setSubject1(subject2);
        subject.setRegistrationEvent(regEvent);
        subjects.add(subject);
        controlActProcess.getSubject().addAll(subjects);
        message.setControlActProcess(controlActProcess);
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        message.setSender(createSender());
        return message;
    }

    private PRPAIN201301UV02 createPRPAIN201301UV02() {
        PRPAIN201301UV02 message = new PRPAIN201301UV02();
        message.setControlActProcess(createPRPAIN201301UV02MFMIMT700701UV01ControlActProcess());
        message.setId(createTypeId());
        message.setTypeId(createTypeId());
        message.setProcessingCode(createCS());
        message.setSender(createSender());
        return message;
    }

    private MCCIMT000100UV01Sender createSender() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        sender.setDevice(createSenderDevice());
        return sender;
    }

    private MCCIMT000100UV01Device createSenderDevice() {
        MCCIMT000100UV01Device senderDevice = new MCCIMT000100UV01Device();
        senderDevice.getId().add(createII());
        return senderDevice;
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
        List<PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = new ArrayList<>();
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
        JAXBElement<PRPAMT201301UV02Person> patientPersonElement = new JAXBElement<>(xmlqname,
                PRPAMT201301UV02Person.class, patientPerson);
        patient.setPatientPerson(patientPersonElement);
        patientPerson.getClassCode().add("ClassCode");

        patientPerson.setDeterminerCode("INSTANCE");
        patient.getId().addAll(createTypeIdList());

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
        return new JAXBElement<>(xmlqname,
                COCTMT090003UV01Device.class, device);
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

    private List<II> createTypeIdList() {
        List<II> ii = new ArrayList<>();
        II typeId1= new II();
        typeId1.setAssigningAuthorityName("CONNECT");
        typeId1.setExtension("D123401");
        typeId1.setRoot("1.1");
        ii.add(typeId1);
        II typeId2 = new II();
        typeId2.setAssigningAuthorityName("CONNECT2");
        typeId2.setExtension("D123401");
        typeId2.setRoot("2.2");
        ii.add(typeId2);
        return ii;
    }

    private II createTypeId() {
        II typeId1= new II();
        typeId1.setAssigningAuthorityName("CONNECT");
        typeId1.setExtension("D123401");
        typeId1.setRoot("1.1");
        return typeId1;
    }

}
