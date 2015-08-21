/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import gov.hhs.fha.nhinc.audit.AuditTransformConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Identifiers;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditDataBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryTransformConstants;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.XActMoodIntentEvent;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author achidamb
 * @param <T>
 * @param <K>
 */
public class PatientDiscoveryTransformsTest<T extends PRPAIN201305UV02, K extends PRPAIN201306UV02> extends
    AuditTransformTest<T, K> {

    public PatientDiscoveryTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void transformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/source/AuditService";
        PatientDiscoveryTransforms transforms = new PatientDiscoveryTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIP;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProeprties) {
                if (webContextProeprties != null && !webContextProeprties.isEmpty() && webContextProeprties.
                    getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {
                    return webContextProeprties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return remoteObjectIP;
            }

        };

        PRPAIN201305UV02 request = createPRPAIN201305UV02Request();
        AssertionType assertion = createAssertion();
        PatientDiscoveryAuditDataBuilder builder = new PatientDiscoveryAuditDataBuilder();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.TRUE,
            webContextProperties, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, builder);
        testGetEventIdentificationType(auditRequest, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, Boolean.TRUE,
            builder);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, localIP, webContextProperties);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest);
    }

    private void assertParticiopantObjectIdentification(LogEventRequestType auditRequest) {
        assertEquals("D123401^^^&1.1&ISO",
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertSame(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectTypeCode());
        assertSame(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectTypeCodeRole());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectIDTypeCode().getCode());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().
            getCodeSystemName());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().
            getDisplayName());
        assertSame(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectTypeCode());
        assertSame(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectTypeCodeRole());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().
            getCode());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().
            getCodeSystemName());
        assertEquals(PatientDiscoveryTransformConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().
            getDisplayName());
    }

    private AssertionType createAssertion() {
        AssertionType assertion = new AssertionType();
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType());
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createCeType());
        userType.setUserName("Wanderson");
        assertion.setUserInfo(userType);
        return assertion;
    }

    private HomeCommunityType createHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("1.1");
        homeCommunityType.setName("DOD");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private PersonNameType createPersonNameType() {
        PersonNameType personNameType = new PersonNameType();
        personNameType.setFamilyName("Tamney");
        personNameType.setFullName("Erica");
        personNameType.setGivenName("Jasmine");
        personNameType.setPrefix("Ms");
        return personNameType;
    }

    private CeType createCeType() {
        CeType ceType = new CeType();
        ceType.setCode("Code");
        ceType.setCodeSystem("CodeSystem");
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName("DisplayName");
        return ceType;
    }

    private NhinTargetSystemType createNhinTarget() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(createTragetHomeCommunityType());
        return targetSystem;
    }

    private HomeCommunityType createTragetHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("2.2");
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02Request() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        request.setControlActProcess(createControlActProcess());
        return request;
    }

    private PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createControlActProcess() {

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        CD code = new CD();
        code.setCode("PRPA_TE201306UV02");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);
        II subjectId = new II();
        subjectId.setRoot("1.1");
        subjectId.setExtension("D123401");
        controlActProcess.setQueryByParameter(createQueryParams("Allen", "Wanderson", "M", "09-8-1971", subjectId));
        return controlActProcess;
    }

    private static JAXBElement<PRPAMT201306UV02QueryByParameter> createQueryParams(String firstName, String lastName,
        String gender, String birthTime, II subjectId) {
        PRPAMT201306UV02QueryByParameter params = new PRPAMT201306UV02QueryByParameter();

        II id = new II();
        id.setRoot("12345");
        params.setQueryId(id);

        CS statusCode = new CS();
        statusCode.setCode("new");
        params.setStatusCode(statusCode);

        params.setParameterList(createParamList(firstName, lastName, gender, birthTime, subjectId));

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryParams
            = new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class,
                params);

        return queryParams;
    }

    private static PRPAMT201306UV02ParameterList createParamList(String firstName, String lastName, String gender,
        String birthTime, II subjectId) {
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

    private static PRPAMT201306UV02LivingSubjectId createSubjectId(II subjectId) {
        PRPAMT201306UV02LivingSubjectId id = new PRPAMT201306UV02LivingSubjectId();
        if (subjectId != null) {
            id.getValue().add(subjectId);
        }

        return id;
    }

    private static PRPAMT201306UV02LivingSubjectName createName(String firstName, String lastName) {
        PRPAMT201306UV02LivingSubjectName subjectName = new PRPAMT201306UV02LivingSubjectName();
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        ENExplicit name = (factory.createENExplicit());
        List namelist = name.getContent();

        if (lastName != null && lastName.length() > 0) {
            EnExplicitFamily familyName = new EnExplicitFamily();
            familyName.setPartType("FAM");
            familyName.setContent(lastName);

            namelist.add(factory.createENExplicitFamily(familyName));
        }

        if (firstName != null && firstName.length() > 0) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setPartType("GIV");
            givenName.setContent(firstName);

            namelist.add(factory.createENExplicitGiven(givenName));
        }

        subjectName.getValue().add(name);

        return subjectName;
    }

    private static PRPAMT201306UV02LivingSubjectBirthTime createBirthTime(String birthTime) {
        PRPAMT201306UV02LivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit bday = new IVLTSExplicit();

        if (birthTime != null && birthTime.length() > 0) {
            bday.setValue(birthTime);
            subjectBirthTime.getValue().add(bday);
        }

        return subjectBirthTime;
    }

    private static PRPAMT201306UV02LivingSubjectAdministrativeGender createGender(String gender) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender
            = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        CE genderCode = new CE();

        if (gender != null && gender.length() > 0) {
            genderCode.setCode(gender);
            adminGender.getValue().add(genderCode);
        }

        return adminGender;
    }

    private static MCCIMT000100UV01Receiver createReceiver() {
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

    public static Patient createMpiPatient(String firstName, String lastName, String gender, String birthTime,
        Identifier subjectId) {
        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }

    public static Patient createMpiPatient(String firstName, String lastName, String middleName, String gender,
        String birthTime, Identifier subjectId) {
        Patient result = new Patient();

        // Set the patient name
        PersonName name = new PersonName();
        name.setFirstName(firstName);
        name.setLastName(lastName);
        name.setMiddleName(middleName);
        result.getNames().add(name);

        // Set the patient gender
        result.setGender(gender);

        // Set the patient birth time
        result.setDateOfBirth(birthTime);

        // Set the patient Id
        Identifiers ids = new Identifiers();
        ids.add(subjectId);
        result.setIdentifiers(ids);

        return result;
    }

}
