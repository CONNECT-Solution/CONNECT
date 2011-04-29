/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import org.jmock.Expectations;
import org.jmock.Mockery;

/**
 *
 * @author MFLYNN02
 */
public class DocumentQueryTransformsTest {
    Mockery context = new Mockery();

    public DocumentQueryTransformsTest() {
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
     * Test of transformDocQueryReq2AuditMsg method, of class DocumentQueryTransforms.
     */
    @Test
    public void testTransformDocQueryReq2AuditMsg() {
        LogAdhocQueryRequestType logMessage = new LogAdhocQueryRequestType();
        AdhocQueryMessageType adhocMessage = new AdhocQueryMessageType();
        AdhocQueryRequest message = new AdhocQueryRequest();
        UserType userInfo = new UserType();
        AuditData auditData = new AuditData();
              
        AdhocQueryType adhocType = new AdhocQueryType();
        adhocType.setId("test query id");
        SlotType1 slot = new SlotType1();
        ValueListType valueList = new ValueListType();
        valueList.getValue().add("12345");
        slot.setValueList(valueList);
        adhocType.getSlot().add(slot);
        message.setAdhocQuery(adhocType);
        
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        //home.setName("Federal - VA");
        home.setName("2.16.840.1.113883.3.200");
        userInfo.setOrg(home);
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Smith");
        personName.setGivenName("Mary");
        userInfo.setPersonName(personName);
        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(userInfo);
        
        adhocMessage.setAdhocQueryRequest(message);
        adhocMessage.setAssertion(assertion);
        
        logMessage.setMessage(adhocMessage);
        
        auditData.setReceiverPatientId("12345");
        
        LogEventRequestType expResult = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();
        expResult.setAuditMessage(auditMsg);
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Mary Smith");
        expResult.getAuditMessage().getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditMessage().getAuditSourceIdentification().add(sourceId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE);
        expResult.getAuditMessage().setEventIdentification(eventId);
        
        // Create mock objects
        final Log mockLog = context.mock(Log.class);

        DocumentQueryTransforms transformer = new DocumentQueryTransforms()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            allowing (mockLog).isDebugEnabled();
            allowing (mockLog).debug(with(any(String.class)));
            allowing (mockLog).info(with(any(String.class)));
        }});

        LogEventRequestType result = transformer.transformDocQueryReq2AuditMsg(logMessage, home.getHomeCommunityId());
             
        assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
    }

    /**
     * Test of transformDocQueryReq2AuditMsg method of class 
     * DocumentQueryTransforms for a response generated from a LeafClass query.
     */
    @Test
    public void testTransformDocQueryResp2AuditMsgLeafClass() {
        AdhocQueryResponse message = new AdhocQueryResponse();
        UserType userInfo = new UserType();
        AuditData auditData = new AuditData();
        LogAdhocQueryResultRequestType logMessage = new LogAdhocQueryResultRequestType();
        AdhocQueryResponseMessageType adhocMessage = new AdhocQueryResponseMessageType();
        AssertionType assertion = new AssertionType();
        String expectedHomeCommunity = "2.16.840.1.113883.3.198";

        AdhocQueryType adhocType = new AdhocQueryType();
        adhocType.setId("test query id");
        message.setRequestId("5555555");
        
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(expectedHomeCommunity);
        home.setName("Federal - DoD");
        userInfo.setOrg(home);
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Jones");
        personName.setGivenName("Bob");
        userInfo.setPersonName(personName);
        assertion.setUserInfo(userInfo);
        
        adhocMessage.setAdhocQueryResponse(message);
        adhocMessage.setAssertion(assertion);
        logMessage.setMessage(adhocMessage);
        auditData.setReceiverPatientId("999999");
        
        // Build Registry Object List for response
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryObjectListType registryObjectList = new RegistryObjectListType();
        message.setRegistryObjectList(registryObjectList);
        // Add first result - ExtrinsicObject
        ExtrinsicObjectType docResult = new ExtrinsicObjectType();
        docResult.setId("urn:uuid:571a20d5-948c-4580-b863-514fdd63511d");
        docResult.setHome(expectedHomeCommunity);
        registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(docResult));

        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Bob Jones");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(sourceId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);
        
        // Create mock objects
        final Log mockLog = context.mock(Log.class);

        DocumentQueryTransforms transformer = new DocumentQueryTransforms()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            allowing (mockLog).isDebugEnabled();
            allowing (mockLog).debug(with(any(String.class)));
            allowing (mockLog).info(with(any(String.class)));
        }});

        LogEventRequestType result = transformer.transformDocQueryResp2AuditMsg(logMessage,expectedHomeCommunity);
             
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
        // Validate home community id
        assertNotNull("AuditSourceIdentification list", result.getAuditMessage().getAuditSourceIdentification());
        assertFalse("AuditSourceIdentification list empty", result.getAuditMessage().getAuditSourceIdentification().isEmpty());
        AuditSourceIdentificationType auditSrcId = result.getAuditMessage().getAuditSourceIdentification().get(0);
        assertNotNull("AuditSourceIdentification object", auditSrcId);
        assertEquals("AuditSourceIdentification empty", expectedHomeCommunity, auditSrcId.getAuditSourceID());
    }

    /**
     * Test of transformDocQueryReq2AuditMsg method of class
     * DocumentQueryTransforms for a response generated from a ObjectRef query.
     */
    @Test
    public void testTransformDocQueryResp2AuditMsgObjectRef() {
        // Build base audit objects
        LogAdhocQueryResultRequestType logMessage = new LogAdhocQueryResultRequestType();
        AdhocQueryResponseMessageType adhocMessage = new AdhocQueryResponseMessageType();

        String expectedHomeCommunity = "2.16.840.1.113883.3.198";

        // Build Assertion object
        AssertionType assertion = new AssertionType();
        UserType userInfo = new UserType();
        AuditData auditData = new AuditData();
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId(expectedHomeCommunity);
        home.setName("Federal - DoD");
        userInfo.setOrg(home);
        PersonNameType personName = new PersonNameType();
        personName.setFamilyName("Jones");
        personName.setGivenName("Bob");
        userInfo.setPersonName(personName);
        assertion.setUserInfo(userInfo);

        // Build AdhocQueryResponse object
        AdhocQueryResponse message = new AdhocQueryResponse();
        message.setRequestId("5555555");
        message.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");

        // Build Registry Object List for response
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        RegistryObjectListType registryObjectList = new RegistryObjectListType();
        message.setRegistryObjectList(registryObjectList);
        // Add first result - ObjectRef
        ObjectRefType docResult = new ObjectRefType();
        docResult.setId("urn:uuid:571a20d5-948c-4580-b863-514fdd63511d");
        docResult.setHome(expectedHomeCommunity);
        registryObjectList.getIdentifiable().add(rimObjFact.createObjectRef(docResult));
        // Add second result - ObjectRef
        docResult = new ObjectRefType();
        docResult.setId("urn:uuid:a456a3d5-b26f-4e0d-8ec6-34ceffa43811");
        docResult.setHome(expectedHomeCommunity);
        registryObjectList.getIdentifiable().add(rimObjFact.createObjectRef(docResult));
        // Add third result - ObjectRef
        docResult = new ObjectRefType();
        docResult.setId("urn:uuid:0a27e4fc-b77e-474e-a750-0fb4dd953729");
        docResult.setHome(expectedHomeCommunity);
        registryObjectList.getIdentifiable().add(rimObjFact.createObjectRef(docResult));

        adhocMessage.setAdhocQueryResponse(message);
        adhocMessage.setAssertion(assertion);
        logMessage.setMessage(adhocMessage);
        auditData.setReceiverPatientId("999999");

        // Build expected result
        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("Bob Jones");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType sourceId = new AuditSourceIdentificationType();
        sourceId.setAuditEnterpriseSiteID(home.getName());
        expResult.getAuditSourceIdentification().add(sourceId);
        EventIdentificationType eventId = new EventIdentificationType();
        eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_EXECUTE);
        expResult.setEventIdentification(eventId);
        LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        // Create mock objects
        final Log mockLog = context.mock(Log.class);

        DocumentQueryTransforms transformer = new DocumentQueryTransforms()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        // Set expectations
        context.checking(new Expectations(){{
            allowing (mockLog).isDebugEnabled();
            allowing (mockLog).debug(with(any(String.class)));
            allowing (mockLog).info(with(any(String.class)));
        }});

        LogEventRequestType result = transformer.transformDocQueryResp2AuditMsg(logMessage,expectedHomeCommunity);

        //result.getAuditMessage().getAuditSourceIdentification().
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());

        // Validate home community id
        assertNotNull("AuditSourceIdentification list", result.getAuditMessage().getAuditSourceIdentification());
        assertFalse("AuditSourceIdentification list empty", result.getAuditMessage().getAuditSourceIdentification().isEmpty());
        AuditSourceIdentificationType auditSrcId = result.getAuditMessage().getAuditSourceIdentification().get(0);
        assertNotNull("AuditSourceIdentification object", auditSrcId);
        assertEquals("AuditSourceIdentification empty", expectedHomeCommunity, auditSrcId.getAuditSourceID());
    }

}