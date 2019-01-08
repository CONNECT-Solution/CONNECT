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
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author MFLYNN02
 */
public class AuditDataTransformHelperTest {

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
       String actionCode = AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE;
        Integer eventOutcome = AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS;
        CodedValueType eventId = new CodedValueType();
        eventId.setCodeSystem(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ACK);
        eventId.setDisplayName(AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ENTITY_SD);

        EventIdentificationType expResult = new EventIdentificationType();
        expResult.setEventID(eventId);
        expResult.setEventActionCode(actionCode);

        EventIdentificationType result = AuditDataTransformHelper.createEventIdentification(actionCode, eventOutcome,
                eventId);

        assertSame(expResult.getEventID(), result.getEventID());
        assertSame(expResult.getEventActionCode(), result.getEventActionCode());
    }

    /**
     * Test of createEventId method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateEventId() {
        String eventCode = AuditDataTransformConstants.EVENT_ACTION_CODE_DELETE;
        String eventCodeSys = "";
        String eventCodeSysName = AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_SDD;
        String dispName = AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_SDREV;

        CodedValueType expResult = new CodedValueType();
        expResult.setCodeSystemName(eventCodeSysName);
        expResult.setDisplayName(dispName);
        expResult.setCode(eventCode);

        CodedValueType result = AuditDataTransformHelper.createEventId(eventCode, eventCodeSys, eventCodeSysName,
                dispName);

        assertSame(expResult.getCodeSystemName(), result.getCodeSystemName());
        assertSame(expResult.getDisplayName(), result.getDisplayName());
        assertSame(expResult.getCode(), result.getCode());
    }

    /**
     * Test of createActiveParticipantFromUser method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateActiveParticipantFromAssertion() {
        String ipAddr;

        try {
            ipAddr = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
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
        assertEquals(expResult.getUserName(), result.getUserName());
    }

    /**
     * Test of createActiveParticipant method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateActiveParticipant() {
        String userId = "test1";
        String altUserId = "John";
        String userName = "Thomas John Smith";
        Boolean userIsReq = true;

        ActiveParticipant expResult = new ActiveParticipant();
        expResult.setAlternativeUserID(altUserId);
        expResult.setUserID(userId);
        expResult.setUserName(userName);

        ActiveParticipant result = AuditDataTransformHelper.createActiveParticipant(userId, altUserId, userName,
                userIsReq);

        assertEquals(expResult.getAlternativeUserID(), result.getAlternativeUserID());
        assertEquals(expResult.getUserID(), result.getUserID());
        assertEquals(expResult.getUserName(), result.getUserName());
    }

    /**
     * Test of createAuditSourceIdentificationFromUser method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateAuditSourceIdentificationFromAssertion() {
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        UserType userInfo = new UserType();
        userInfo.setOrg(home);

        AuditSourceIdentificationType expResult = new AuditSourceIdentificationType();
        expResult.setAuditSourceID(home.getHomeCommunityId());
        expResult.setAuditEnterpriseSiteID(home.getName());

        AuditSourceIdentificationType result = AuditDataTransformHelper
                .createAuditSourceIdentificationFromUser(userInfo);

        assertEquals(expResult.getAuditEnterpriseSiteID(), result.getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditSourceID(), result.getAuditSourceID());
    }

    /**
     * Test of createAuditSourceIdentification method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateAuditSourceIdentification() {
        String communityId = "2.16.840.1.113883.3.198";
        String communityName = "Federal - DoD";

        AuditSourceIdentificationType expResult = new AuditSourceIdentificationType();
        expResult.setAuditSourceID(communityId);
        expResult.setAuditEnterpriseSiteID(communityName);

        AuditSourceIdentificationType result = AuditDataTransformHelper.createAuditSourceIdentification(communityId,
                communityName);

        assertEquals(expResult.getAuditEnterpriseSiteID(), result.getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditSourceID(), result.getAuditSourceID());
    }

    /**
     * Test of createParticipantObjectIdentification method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateParticipantObjectIdentification() {
        String patientId = "44444";

        ParticipantObjectIdentificationType expResult = new ParticipantObjectIdentificationType();
        expResult.setParticipantObjectID(patientId);

        ParticipantObjectIdentificationType result = AuditDataTransformHelper
                .createParticipantObjectIdentification(patientId);

        assertEquals(expResult.getParticipantObjectID(), result.getParticipantObjectID());
    }

    /**
     * Test of logAuditMessage method, of class AuditDataTransformHelper.
     */
    @Test
    public void testLogAuditMessage() {
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

    }

    /**
     * Test of findSingleExternalIdentifier method, of class AuditDataTransformHelper.
     */
    @Test
    public void testFindSingleExternalIdentifier() {
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

    }

    /**
     * Test of findSingleExternalIdentifierAndExtractValue method, of class AuditDataTransformHelper.
     */
    @Test
    public void testFindSingleExternalIdentifierAndExtractValue() {
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

    }

    /**
     * Test of createCompositePatientId method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateCompositePatientId() {
        String communityId = "2.16.840.1.113883.3.200";
        String patientId = "12332";
        String expResult = patientId + "^^^&" + communityId + "&ISO";

        String result = AuditDataTransformHelper.createCompositePatientId(communityId, patientId);

        assertEquals(expResult, result);

    }

    /**
     * Test of createCompositePatientIdFromAssertion method, of class AuditDataTransformHelper.
     */
    @Test
    public void testCreateCompositePatientIdFromAssertion() {
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        UserType userInfo = new UserType();
        userInfo.setOrg(home);

        String uniquePatientId = "56765";

        String expResult = uniquePatientId + "^^^&" + userInfo.getOrg().getHomeCommunityId() + "&ISO";
        String result = AuditDataTransformHelper.createCompositePatientIdFromAssertion(userInfo, uniquePatientId);
        assertEquals(expResult, result);

    }

}
