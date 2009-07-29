package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import static org.junit.Assert.*;
import org.junit.Test;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogNhinSubscribeRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubscribeResponseType;
import gov.hhs.fha.nhinc.common.auditlog.SubscribeResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.FilterType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test for SubscribeTransforms class
 * 
 * @author Neil Webb
 */
public class SubscribeTransformsTest
{
    private static Log log = LogFactory.getLog(SubscribeTransformsTest.class);

    @Test
    public void testSubscribeRequest()
    {
        log.debug("Begin testSubscribeRequest");
        try
        {
            LogNhinSubscribeRequestType logSubscribeRequest = new LogNhinSubscribeRequestType();
            logSubscribeRequest.setDirection("Inbound");
            logSubscribeRequest.setInterface("Nhin");

            SubscribeRequestType subscribeRequest = new SubscribeRequestType();
            Subscribe subscribe = new Subscribe();

            // Consumer Reference
            subscribe.setConsumerReference(createNhinSubscriptionReference("http://www.somewhere.com/SubscriptionManager", "http://www.hhs.gov/healthid/nhin", "SubscriptionId", "testval"));

            // Filter
            FilterType filter = new FilterType();
            TopicExpressionType topic = new TopicExpressionType();
            topic.setDialect("http://doc.oasis-open.org/wsn/t-1/TopicExpression/Simple");
            topic.getContent().add("document");
            org.oasis_open.docs.wsn.b_2.ObjectFactory wsntObjectFactory = new org.oasis_open.docs.wsn.b_2.ObjectFactory();
            filter.getAny().add(wsntObjectFactory.createTopicExpression(topic));
            subscribe.setFilter(filter);

            // AdhocQuery
            AdhocQueryType adhocQuery = new AdhocQueryType();
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
            adhocQuery.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
            SlotType1 slot = new SlotType1();
            slot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            valueList.getValue().add("'Local-1^^^&amp;1.1&amp;ISO'");
            adhocQuery.getSlot().add(slot);
            slot = new SlotType1();
            slot.setName("$XDSDocumentEntryClassCode");
            valueList = new ValueListType();
            valueList.getValue().add("('XNHIN-CONSENT')");
            adhocQuery.getSlot().add(slot);
            subscribe.getAny().add(rimObjectFactory.createAdhocQuery(adhocQuery));

            // Assertion
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId("2.16.840.1.113883.3.200");
            home.setName("Federal - VA");
            UserType userInfo = new UserType();
            userInfo.setOrg(home);
            PersonNameType personName = new PersonNameType();
            personName.setFamilyName("Smith");
            personName.setGivenName("Mary");
            userInfo.setPersonName(personName);
            AssertionType assertion = new AssertionType();
            assertion.setUserInfo(userInfo);

            subscribeRequest.setSubscribe(subscribe);
            subscribeRequest.setAssertion(assertion);
            logSubscribeRequest.setMessage(subscribeRequest);

            // Expected log result
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
            eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE);
            expResult.getAuditMessage().setEventIdentification(eventId);

            // Transform
            SubscribeTransforms transform = new SubscribeTransforms();
            LogEventRequestType result = transform.transformNhinSubscribeRequestToAuditMessage(logSubscribeRequest);

            // Validate
            assertNotNull("LogEventRequestType was null", result);
            assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
            assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                    result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
            assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        } catch (Throwable t)
        {
            log.error("Error in testSubscribeRequest: " + t.getMessage(), t);
            fail(t.getMessage());
        }
        log.debug("End testSubscribeRequest");
    }

    @Test
    public void testSubscribeResponse()
    {
        log.debug("Begin testSubscribeResponse");
        try
        {
            LogSubscribeResponseType logSubscribeResponse = new LogSubscribeResponseType();
            logSubscribeResponse.setDirection("Outbound");
            logSubscribeResponse.setInterface("Nhin");

            SubscribeResponseMessageType subscribeResponseMessage = new SubscribeResponseMessageType();
            logSubscribeResponse.setMessage(subscribeResponseMessage);

            // SubscribeResponse 
            SubscribeResponse subscribeResponse = new SubscribeResponse();
            subscribeResponse.setSubscriptionReference(createNhinSubscriptionReference("http://www.somewhere.com/SubscriptionManager", "http://www.hhs.gov/healthid/nhin", "SubscriptionId", "testval"));

            subscribeResponseMessage.setSubscribeResponse(subscribeResponse);

            // Assertion
            HomeCommunityType home = new HomeCommunityType();
            home.setHomeCommunityId("2.16.840.1.113883.3.200");
            home.setName("Federal - VA");
            UserType userInfo = new UserType();
            userInfo.setOrg(home);
            PersonNameType personName = new PersonNameType();
            personName.setFamilyName("Smith");
            personName.setGivenName("Mary");
            userInfo.setPersonName(personName);
            AssertionType assertion = new AssertionType();
            assertion.setUserInfo(userInfo);
            subscribeResponseMessage.setAssertion(assertion);

            // Expected log result
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
            eventId.setEventActionCode(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE);
            expResult.getAuditMessage().setEventIdentification(eventId);

            // Transform
            SubscribeTransforms transform = new SubscribeTransforms();
            LogEventRequestType result = transform.transformSubscribeResponseToAuditMessage(logSubscribeResponse);

            // Validate
            assertNotNull("LogEventRequestType was null", result);
            assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
            assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                    result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
            assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());
        } catch (Throwable t)
        {
            log.error("Error in testSubscribeResponse: " + t.getMessage(), t);
            fail(t.getMessage());
        }
        log.debug("End testSubscribeResponse");
    }

    public static W3CEndpointReference createNhinSubscriptionReference(String subMgrEndpointAddress, String refParamNamespace, String refParamElementName, String refParamValue)
    {
        log.info("begin createNhinSubscriptionReference");
        W3CEndpointReference subRef;

        W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();
        log.info("internalSubscriptionRef.getSubscriptionManagerEndpointAddress()=" + subMgrEndpointAddress);
        resultBuilder.address(subMgrEndpointAddress);

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try
        {
            doc = docBuilderFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex)
        {
            throw new RuntimeException(ex);
        }

        doc.setXmlStandalone(true);

        log.info("handling reference parameter " + refParamNamespace + ";" + refParamElementName + ";" + refParamValue);
        Element subscriptionElem = doc.createElementNS(refParamNamespace, refParamElementName);
        subscriptionElem.setTextContent(refParamValue);
        resultBuilder.referenceParameter(subscriptionElem);

        log.info("building.. resultBuilder.build()");
        subRef = resultBuilder.build();
        log.info("end createNhinSubscriptionReference");
        return subRef;
    }
}