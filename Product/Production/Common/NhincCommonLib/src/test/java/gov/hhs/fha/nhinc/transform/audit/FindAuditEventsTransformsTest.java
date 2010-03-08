/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

/**
 *
 * @author mflynn02
 */
public class FindAuditEventsTransformsTest {
    private static Log log = LogFactory.getLog(FindAuditEventsTransformsTest.class);

    public FindAuditEventsTransformsTest() {
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
     * Test of transformFindAuditEventsReq2AuditMsg method, of class FindAuditEventsTransforms.
     */
    @Test
    public void testTransformFindAuditEventsReq2AuditMsg() {
        log.debug("Begin - testTransformFindAuditEventsReq2AuditMsg");
        LogFindAuditEventsRequestType logMessage = new LogFindAuditEventsRequestType();
        FindAuditEventsMessageType FAEMessage = new FindAuditEventsMessageType();
        AssertionType assertion = new AssertionType();
        
        FindAuditEventsType message = new FindAuditEventsType();
        HomeCommunityType home = new HomeCommunityType();
        AuditData auditData = new AuditData();
        auditData.setReceiverPatientId("909090");
        auditData.setMessageType("findAuditEvents");
        message.setPatientId("4321");
        
        UserType userInfo = new UserType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Armstrong");
        person.setGivenName("Neil");
        userInfo.setPersonName(person);
        userInfo.setUserName(person.getGivenName() + " " + person.getFamilyName());
        assertion.setUserInfo(userInfo);
        
        FAEMessage.setAssertion(assertion);
        FAEMessage.setFindAuditEvents(message);
        logMessage.setMessage(FAEMessage);
        
        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName(person.getGivenName() + " " + person.getFamilyName());
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(sourceId);
        ParticipantObjectIdentificationType partObjId = new ParticipantObjectIdentificationType();
        partObjId.setParticipantObjectID(auditData.getReceiverPatientId() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ADQ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        LogEventRequestType result = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logMessage);
        
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(), result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventID().getCode());
        
        log.debug("End - testTransformFindAuditEventsReq2AuditMsg");
    }

}