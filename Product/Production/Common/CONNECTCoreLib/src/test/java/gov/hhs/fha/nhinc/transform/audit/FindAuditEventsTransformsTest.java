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
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.FindAuditEventsMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mflynn02
 */
public class FindAuditEventsTransformsTest {

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
        partObjId
                .setParticipantObjectID(auditData.getReceiverPatientId() + "^^^&" + home.getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_ADQ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        LogEventRequestType result = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logMessage);

        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage()
                .getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage()
                .getEventIdentification().getEventID().getCode());

    }

}