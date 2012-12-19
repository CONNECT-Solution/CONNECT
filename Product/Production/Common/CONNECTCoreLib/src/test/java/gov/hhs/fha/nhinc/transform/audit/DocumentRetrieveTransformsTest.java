/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;

/**
 * 
 * @author mflynn02
 */
public class DocumentRetrieveTransformsTest {
    
    public DocumentRetrieveTransformsTest() {
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
     * Test of transformDocRetrieveReq2AuditMsg method, of class DocumentRetrieveTransforms.
     */
    @Test
    public void testTransformDocRetrieveReq2AuditMsg() {
        LogDocRetrieveRequestType logMessage = new LogDocRetrieveRequestType();
        DocRetrieveMessageType docReqMessage = new DocRetrieveMessageType();
        AssertionType assertion = new AssertionType();

        RetrieveDocumentSetRequestType message = new RetrieveDocumentSetRequestType();
        DocumentRequest docReq = new DocumentRequest();
        docReq.setDocumentUniqueId("123-45-6789");
        docReq.setHomeCommunityId("2.16.840.1.113883.3.200");
        message.getDocumentRequest().add(docReq);

        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Presley");
        person.setGivenName("Elvis");
        userInfo.setPersonName(person);
        assertion.setUserInfo(userInfo);

        docReqMessage.setAssertion(assertion);
        docReqMessage.setRetrieveDocumentSetRequest(message);
        logMessage.setMessage(docReqMessage);

        AuditData auditData = new AuditData();
        auditData.setReceiverPatientId("999999");
        auditData.setMessageType("RetrieveDocumentSetRequest");

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
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_READ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        LogEventRequestType result = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logMessage);

        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage()
                .getActiveParticipant().get(0).getUserName());
        // assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
        // result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage()
                .getEventIdentification().getEventActionCode());
    
    }

    /**
     * Test of transformDocRetrieveResp2AuditMsg method, of class DocumentRetrieveTransforms.
     */
    @Test
    public void testTransformDocRetrieveResp2AuditMsg() {
        LogDocRetrieveResultRequestType logMessage = new LogDocRetrieveResultRequestType();
        DocRetrieveResponseMessageType docRespMessage = new DocRetrieveResponseMessageType();
        AssertionType assertion = new AssertionType();

        RetrieveDocumentSetResponseType message = new RetrieveDocumentSetResponseType();
        DocumentRequest docReq = new DocumentRequest();
        docReq.setDocumentUniqueId("987-06543-021");
        docReq.setHomeCommunityId("2.16.840.1.113883.3.166.4");
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("DocRetrieve Response Status");
        message.setRegistryResponse(resp);

        UserType userInfo = new UserType();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.166.4");
        home.setName("CareSpark");
        userInfo.setOrg(home);
        PersonNameType person = new PersonNameType();
        person.setFamilyName("Jackson");
        person.setGivenName("Andrew");
        userInfo.setPersonName(person);
        assertion.setUserInfo(userInfo);

        docRespMessage.setAssertion(assertion);
        docRespMessage.setRetrieveDocumentSetResponse(message);
        logMessage.setMessage(docRespMessage);

        AuditData auditData = new AuditData();
        auditData.setReceiverPatientId("78987");
        auditData.setMessageType("RetrieveDocumentSetResponse");

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
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_READ);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        LogEventRequestType result = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logMessage);

        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage()
                .getActiveParticipant().get(0).getUserName());
        // assertEquals(expected.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
        // result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage()
                .getEventIdentification().getEventActionCode());

    }

}