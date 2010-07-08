/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import org.hl7.v3.*;
import javax.xml.bind.JAXBElement;

import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectAddedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevisedMessageType;
import gov.hhs.fha.nhinc.common.auditlog.NhinSubjectDiscoveryAckMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationRequestMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectReidentificationResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.*;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author MFLYNN02
 */
public class SubjectDiscoveryTransformsTest {

    private static Log log = LogFactory.getLog(SubjectDiscoveryTransformsTest.class);

    public SubjectDiscoveryTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transformPRPA2013012AuditMsg method, of class SubjectDiscoveryTransforms.
     */
    @Test
    public void testTransformPRPA2013012AuditMsg() {
        log.debug("Begin - testTransformPRPA2013012AuditMsg");
        
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        AssertionType assertion = new AssertionType();
       
        LogSubjectAddedRequestType logMessage = new LogSubjectAddedRequestType();
        personName.setFamilyName("Jefferson");
        personName.setGivenName("Thomas");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.198");
        home.setName("Federal - DoD");
        userInfo.setOrg(home);
        assertion.setUserInfo(userInfo);

        typeId.setRoot("2.16.840.1.113883.3.198");
        typeId.setExtension("user111");
        sender.setTypeId(typeId);
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "20080226", "123456789");
        patient = HL7PatientTransforms.create201301Patient(person, typeId);
                
        PRPAIN201301UV02 message201301 = HL7PRPA201301Transforms.createPRPA201301(patient, home.getHomeCommunityId(), home.getHomeCommunityId(), home.getHomeCommunityId());
 
        message201301.setSender(sender);
        
        // Assign to logMessage
        SubjectAddedMessageType subjectAdded = new SubjectAddedMessageType();
        subjectAdded.setAssertion(assertion);
        subjectAdded.setPRPAIN201301UV02(message201301);
        logMessage.setMessage(subjectAdded);

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Thomas Jefferson");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(typeId.getExtension() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);

        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(logMessage);
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("result.userName : " + result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID(),
                result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("result.patientId : " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());

        log.debug("End - testTransformPRPA2013012AuditMsg");
    }

    /**
     * Test of transformPRPA2013012AuditMsg method, of class SubjectDiscoveryTransforms.
     */
    @Test
    public void testTransformPRPA2013022AuditMsg() {
        log.debug("Begin - testTransformPRPA2013022AuditMsg");

        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        AssertionType assertion = new AssertionType();
       
        LogSubjectRevisedRequestType logMessage = new LogSubjectRevisedRequestType();

        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);
        PRPAIN201302UV02 message = new PRPAIN201302UV02();
        message.setSender(sender);

        personName.setFamilyName("Kennedy");
        personName.setGivenName("John");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        assertion.setUserInfo(userInfo);

        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "20080226", "123456789");
        patient = HL7PatientTransforms.create201301Patient(person, typeId);
                
        PRPAIN201302UV02 message201302 = HL7PRPA201302Transforms.createPRPA201302(patient, home.getHomeCommunityId(), home.getHomeCommunityId(), home.getHomeCommunityId(), home.getHomeCommunityId());
 
        message201302.setSender(sender);
        
        // Assign to logMessage
        SubjectRevisedMessageType subjectRevised = new SubjectRevisedMessageType();
        subjectRevised.setAssertion(assertion);
        subjectRevised.setPRPAIN201302UV02(message201302);
        logMessage.setMessage(subjectRevised);

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("John Kennedy");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(typeId.getExtension() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);
        
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        LogEventRequestType result = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(logMessage);
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("result.userName : " + result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID(),
                result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("result.patientId : " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());

        log.debug("End - testTransformPRPA2013022AuditMsg");

    }

    /**
     * Test of transformAck2AuditMsg method, of class SubjectDiscoveryTransforms.
     */
    @Test
    public void testTransformAck2AuditMsg() {
        log.debug("Begin - testTransformAck2AuditMsg");

        MCCIMT000200UV01Receiver receiver = new MCCIMT000200UV01Receiver();
        MCCIMT000200UV01Device device = new MCCIMT000200UV01Device();
        II typeId = new II();
        PersonNameType personName = new PersonNameType();
        AssertionType assertion = new AssertionType();
       
        LogNhinSubjectDiscoveryAckRequestType logMessage = new LogNhinSubjectDiscoveryAckRequestType();

        typeId.setRoot("2.16.840.1.113883.13.25");
        typeId.setExtension("Bloomington Hospital");
        MCCIIN000002UV01 MCCImessage = new MCCIIN000002UV01();
        device.getId().add(typeId);
        receiver.setDevice(device);
        MCCImessage.getReceiver().add(receiver);
        log.debug("receiver ext " + MCCImessage.getReceiver().get(0).getDevice().getId().get(0).getExtension());
        log.debug("receiver root " + MCCImessage.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        personName.setFamilyName("Jones");
        personName.setGivenName("Jennifer");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        assertion.setUserInfo(userInfo);
        assertion.getUniquePatientId().add(0, "888888");

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Jennifer Jones");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(typeId.getExtension());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(assertion.getUniquePatientId().get(0) + "^^^&" + typeId.getRoot() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);

        NhinSubjectDiscoveryAckMessageType ackMessage = new NhinSubjectDiscoveryAckMessageType();
        PIXConsumerMCCIIN000002UV01RequestType ack = new PIXConsumerMCCIIN000002UV01RequestType();
        ack.setAssertion(assertion);
        ack.setMCCIIN000002UV01(MCCImessage);
        ackMessage.setPIXConsumerMCCIIN000002UV01Request(ack);
        logMessage.setMessage(ackMessage);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = SubjectDiscoveryTransforms.transformAck2AuditMsg(logMessage);
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("result.userName : " + result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID(),
                result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("result.patientId : " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());

        log.debug("End - testTransformAck2AuditMsg");

    }
    
    /**
     * Test of transformPRPA2013092AuditMsg method, of class SubjectDiscoveryTransforms.
     */
    @Test
    public void testTransformPRPA2013092AuditMsg() {
        log.debug("Begin - testTransformPRPA2013092AuditMsg");
        
        II typeId = new II();
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        AssertionType assertion = new AssertionType();
       
        LogSubjectReidentificationRequestType logMessage = new LogSubjectReidentificationRequestType();
        personName.setFamilyName("Franklin");
        personName.setGivenName("Aretha");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.190");
        home.setName("Health Information Exchange of Medvirginia");
        userInfo.setOrg(home);
        assertion.setUserInfo(userInfo);

        typeId.setRoot("2.16.840.1.113883.3.190");
        typeId.setExtension("4444");
                
        PRPAIN201309UV02 message201309 = HL7PRPA201309Transforms.createPRPA201309(home.getHomeCommunityId(), typeId.getExtension());
 
        
        // Assign to logMessage
        SubjectReidentificationRequestMessageType subjectReident = new SubjectReidentificationRequestMessageType();
        subjectReident.setAssertion(assertion);
        subjectReident.setPRPAIN201309UV02(message201309);
        logMessage.setMessage(subjectReident);

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Aretha Franklin");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(typeId.getExtension() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);

        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(logMessage);
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("result.userName : " + result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID(),
                result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("result.patientId : " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());

        log.debug("End - testTransformPRPA2013092AuditMsg");
    }

        /**
     * Test of transformPRPA2013092AuditMsg method, of class SubjectDiscoveryTransforms.
     */
    @Test
    public void testTransformPRPA2013102AuditMsg() {
        log.debug("Begin - testTransformPRPA2013102AuditMsg");
        
        II typeId = new II();
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        AssertionType assertion = new AssertionType();
       
        LogSubjectReidentificationResponseType logMessage = new LogSubjectReidentificationResponseType();
        personName.setFamilyName("Joplin");
        personName.setGivenName("Janis");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.74");
        home.setName("West Virginia Health Information Network");
        userInfo.setOrg(home);
        assertion.setUserInfo(userInfo);

        typeId.setRoot("2.16.840.1.113883.3.74");
        typeId.setExtension("09890");

        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParam = null;

        PRPAIN201310UV02 message201310 = HL7PRPA201310Transforms.createPRPA201310(typeId.getExtension(), typeId.getRoot(), typeId.getRoot(), home.getHomeCommunityId(), home.getHomeCommunityId(), queryByParam);
        
        // Assign to logMessage
        SubjectReidentificationResponseMessageType subjectReident = new SubjectReidentificationResponseMessageType();
        subjectReident.setPRPAIN201310UV02(message201310);
        logMessage.setMessage(subjectReident);
        logMessage.setAssertion(assertion);

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Janis Joplin");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(typeId.getExtension() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);

        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(logMessage);
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("result.userName : " + result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        log.debug("Expected AuditEnterpriseSiteID " + expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        log.debug("Actual                         " + result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        log.debug("Expected ParticipantObjectID " + expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("Actual                       " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertEquals(expected.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID(),
                result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        log.debug("result.patientId : " + result.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());

        log.debug("End - testTransformPRPA2013102AuditMsg");
    }

}