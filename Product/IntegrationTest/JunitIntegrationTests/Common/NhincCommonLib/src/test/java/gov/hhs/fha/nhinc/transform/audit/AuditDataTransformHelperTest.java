/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author MFLYNN02
 */
public class AuditDataTransformHelperTest {
    private static Log log = LogFactory.getLog(AuditDataTransformHelperTest.class);

    public AuditDataTransformHelperTest() {
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
     * Test of createEventIdentification method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateEventIdentification() {
        log.debug("Begin - testCreateEventIdentification");
        
        String actionCode = AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE;
        Integer eventOutcome = AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS;
        CodedValueType eventId = new CodedValueType();
        eventId.setCodeSystem(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ACK);
        eventId.setDisplayName(AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ENTITY_SD);
        
        EventIdentificationType expResult = new EventIdentificationType();
        expResult.setEventID(eventId);
        expResult.setEventActionCode(actionCode);
        
        EventIdentificationType result = AuditDataTransformHelper.createEventIdentification(actionCode, eventOutcome, eventId);
        
        assertSame(expResult.getEventID(), result.getEventID());
        assertSame(expResult.getEventActionCode(), result.getEventActionCode());
        
        log.debug("End - testCreateEventIdentification");
    }

    /**
     * Test of createEventId method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateEventId() {
        log.debug("Begin - testCreateEventId");
        
        String eventCode = AuditDataTransformConstants.EVENT_ACTION_CODE_DELETE;
        String eventCodeSys = "";
        String eventCodeSysName = AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDD;
        String dispName = AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDREV;
        
        CodedValueType expResult = new CodedValueType();
        expResult.setCodeSystemName(eventCodeSysName);
        expResult.setDisplayName(dispName);
        expResult.setCode(eventCode);
        
        CodedValueType result = AuditDataTransformHelper.createEventId(eventCode, eventCodeSys, eventCodeSysName, dispName);
        
        assertSame(expResult.getCodeSystemName(), result.getCodeSystemName());
        assertSame(expResult.getDisplayName(), result.getDisplayName());
        assertSame(expResult.getCode(), result.getCode());
        
        log.debug("End - testCreateEventId");
    }

    /**
     * Test of createActiveParticipantFromUser method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateActiveParticipantFromAssertion() {
        log.debug("End - testCreateActiveParticipantFromAssertion");
        
        String ipAddr = null;
        
        try {
            ipAddr = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }

        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Jones");
        personName.setGivenName("John");
        UserType userInfo = new UserType();
        userInfo.setPersonName(personName);
        userInfo.setUserName("tester");
        Boolean userIsReq = true;
        
        ActiveParticipant expResult = new ActiveParticipant();
        expResult.setNetworkAccessPointID(ipAddr);
        expResult.setUserName("John Jones");
        expResult.setUserID("tester");
        
        ActiveParticipant result = AuditDataTransformHelper.createActiveParticipantFromUser(userInfo, userIsReq);
        
        assertEquals(expResult.getNetworkAccessPointID(), result.getNetworkAccessPointID());
        assertEquals(expResult.getUserID(), result.getUserID());
        assertEquals (expResult.getUserName(), result.getUserName());
        
        log.debug("End - testCreateActiveParticipantFromAssertion");
    }

    /**
     * Test of createActiveParticipant method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateActiveParticipant() {
        log.debug("Begin - testCreateActiveParticipant");
        
        String userId = "test1";
        String altUserId = "John";
        String userName = "Thomas John Smith";
        Boolean userIsReq = true;
        
        ActiveParticipant expResult = new ActiveParticipant();
        expResult.setAlternativeUserID(altUserId);
        expResult.setUserID(userId);
        expResult.setUserName(userName);
        
        ActiveParticipant result = AuditDataTransformHelper.createActiveParticipant(userId, altUserId, userName, userIsReq);
        
        assertEquals(expResult.getAlternativeUserID(), result.getAlternativeUserID());
        assertEquals(expResult.getUserID(), result.getUserID());
        assertEquals(expResult.getUserName(), result.getUserName());
        
        log.debug("End - testCreateActiveParticipant");
    }

    /**
     * Test of createAuditSourceIdentificationFromUser method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateAuditSourceIdentificationFromAssertion() {
        log.debug("Begin - testCreateAuditSourceIdentificationFromAssertion");
        
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        UserType userInfo = new UserType();
        userInfo.setOrg(home);
        
        AuditSourceIdentificationType expResult = new AuditSourceIdentificationType();
        expResult.setAuditSourceID(home.getHomeCommunityId());
        expResult.setAuditEnterpriseSiteID(home.getName());
        
        AuditSourceIdentificationType result = AuditDataTransformHelper.createAuditSourceIdentificationFromUser(userInfo);

        assertEquals(expResult.getAuditEnterpriseSiteID(), result.getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditSourceID(), result.getAuditSourceID());
        
        log.debug("End - testCreateAuditSourceIdentificationFromAssertion");
    }

    /**
     * Test of createAuditSourceIdentification method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateAuditSourceIdentification() {
        log.debug("Begin - testCreateAuditSourceIdentification");
        
        String communityId = "2.16.840.1.113883.3.198";
        String communityName = "Federal - DoD";
        
        AuditSourceIdentificationType expResult = new AuditSourceIdentificationType();
        expResult.setAuditSourceID(communityId);
        expResult.setAuditEnterpriseSiteID(communityName);
        
        AuditSourceIdentificationType result = AuditDataTransformHelper.createAuditSourceIdentification(communityId, communityName);
        
        assertEquals(expResult.getAuditEnterpriseSiteID(), result.getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditSourceID(), result.getAuditSourceID());

        log.debug("End - testCreateAuditSourceIdentification");
    }

    /**
     * Test of createParticipantObjectIdentification method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateParticipantObjectIdentification() {
        log.debug("Begin - testCreateParticipantObjectIdentification");
        
        String patientId = "44444";
        
        ParticipantObjectIdentificationType expResult = new ParticipantObjectIdentificationType();
        expResult.setParticipantObjectID(patientId);
        
        ParticipantObjectIdentificationType result = AuditDataTransformHelper.createParticipantObjectIdentification(patientId);
        
        assertEquals(expResult.getParticipantObjectID(), result.getParticipantObjectID());

        log.debug("End - testCreateParticipantObjectIdentification");
    }

    /**
     * Test of logAuditMessage method, of class AuditDataTransformHelper.
     */
    @Test
    public void testLogAuditMessage() {
        log.debug("Begin - testLogAuditMessage");
        
        EventIdentificationType eventId = new EventIdentificationType();
        CodedValueType code = new CodedValueType();
        code.setCode(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE);
        code.setCodeSystem(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDN);
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE);
        eventId.setEventID(code);
        
        ActiveParticipant participant = new ActiveParticipant();
        participant.setUserID("tester");
        
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditSourceID("2.16.840.1.113883.3.198");
        sourceId.setAuditEnterpriseSiteID("Federal - DoD");
        
        AuditMessageType message = new AuditMessageType();
        message.setEventIdentification(eventId);
        message.getActiveParticipant().add(participant);
        message.getAuditSourceIdentification().add(sourceId);
        
        AuditDataTransformHelper.logAuditMessage(message);
        
        log.debug("End - testLogAuditMessage");
    }

    /**
     * Test of findSingleExternalIdentifier method, of class AuditDataTransformHelper.
     */
    @Test
    public void testFindSingleExternalIdentifier() {
        log.debug("Begin - testFindSingleExternalIdentifier");
        
        ExternalIdentifierType extId1 = new ExternalIdentifierType();
        ExternalIdentifierType extId2 = new ExternalIdentifierType();
        extId1.setIdentificationScheme("2.16.840.1.113883.3.198");
        List<ExternalIdentifierType> olExtId = new ArrayList();
        olExtId.add(extId1);
        extId2.setIdentificationScheme("2.16.840.1.113883.3.200");
        olExtId.add(extId2);
        
        String sIdentScheme = "2.16.840.1.113883.3.198";
        ExternalIdentifierType expResult = new ExternalIdentifierType();
        expResult = extId1;
        ExternalIdentifierType result = AuditDataTransformHelper.findSingleExternalIdentifier(olExtId, sIdentScheme);
        assertEquals(expResult.getIdentificationScheme(), result.getIdentificationScheme());
        
        log.debug("End - testFindSingleExternalIdentifier");
    }

    /**
     * Test of findSingleExternalIdentifierAndExtractValue method, of class AuditDataTransformHelper.
     */
    @Test
    public void testFindSingleExternalIdentifierAndExtractValue() {
        log.debug("Begin - testFindSingleExternalIdentifierAndExtractValue");
        
        ExternalIdentifierType extId1 = new ExternalIdentifierType();
        extId1.setIdentificationScheme("2.16.840.1.113883.3.198");
        extId1.setValue("198");
        
        ExternalIdentifierType extId2 = new ExternalIdentifierType();
        extId2.setIdentificationScheme("2.16.840.1.113883.3.200");
        extId2.setValue("200");
        
        List<ExternalIdentifierType> olExtId = new ArrayList();
        olExtId.add(extId1);
        olExtId.add(extId2);
        
        String sIdentScheme = "2.16.840.1.113883.3.198";
        String expResult = extId1.getValue();
        
        String result = AuditDataTransformHelper.findSingleExternalIdentifierAndExtractValue(olExtId, sIdentScheme);
        
        assertEquals(expResult, result);
        
        log.debug("End - testFindSingleExternalIdentifierAndExtractValue");
    }

    /**
     * Test of createCompositePatientId method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateCompositePatientId() {
        log.debug("Begin - testCreateCompositePatientId");
        
        String communityId = "2.16.840.1.113883.3.200";
        String patientId = "12332";
        String expResult = patientId +"^^^&" + communityId + "&ISO";
        
        String result = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);
        
        assertEquals(expResult, result);

        log.debug("End - testCreateCompositePatientId");
    }

    /**
     * Test of createCompositePatientIdFromAssertion method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateCompositePatientIdFromAssertion() {
        log.debug("Begin - testCreateCompositePatientIdFromAssertion");
        
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        UserType userInfo = new UserType();
        userInfo.setOrg(home);
        
        String uniquePatientId = "56765";
        
        String expResult = uniquePatientId +"^^^&" + userInfo.getOrg().getHomeCommunityId() + "&ISO";
        String result = AuditDataTransformHelper.createCompositePatientIdFromAssertion(userInfo, uniquePatientId);
        assertEquals(expResult, result);

        log.debug("End - testCreateCompositePatientIdFromAssertion");
    }

}