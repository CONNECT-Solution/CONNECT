/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBElement;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;

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

/**
 *
 * @author MFLYNN02
 */
public class DocumentQueryTransformsTest {
    private static Log log = LogFactory.getLog(DocumentQueryTransformsTest.class);

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
        log.debug("Begin - testTransformDocQueryReq2AuditMsg");
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
        home.setName("Federal - VA");
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
        
        LogEventRequestType result = DocumentQueryTransforms.transformDocQueryReq2AuditMsg(logMessage);
             
        assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
        log.debug("End - testTransformDocQueryReq2AuditMsg");
    }
    /**
     * Test of transformDocQueryReq2AuditMsg method, of class DocumentQueryTransforms.
     */
    @Test
    public void testTransformDocQueryResp2AuditMsg() {
        log.debug("Begin - testTransformDocQueryResp2AuditMsg");
        AdhocQueryResponse message = new AdhocQueryResponse();
        UserType userInfo = new UserType();
        AuditData auditData = new AuditData();
        LogAdhocQueryResultRequestType logMessage = new LogAdhocQueryResultRequestType();
        AdhocQueryResponseMessageType adhocMessage = new AdhocQueryResponseMessageType();
        AssertionType assertion = new AssertionType();
        
        AdhocQueryType adhocType = new AdhocQueryType();
        adhocType.setId("test query id");
        message.setRequestId("5555555");
        
        HomeCommunityType home = new HomeCommunityType();
        home.setHomeCommunityId("2.16.840.1.113883.3.198");
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
        
        LogEventRequestType result = DocumentQueryTransforms.transformDocQueryResp2AuditMsg(logMessage);
             
        assertEquals(expected.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
        assertEquals(expected.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        
        log.debug("End - testTransformDocQueryResp2AuditMsg");
    }

}