package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogNhinUnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogUnsubscribeResponseType;
import gov.hhs.fha.nhinc.common.auditlog.UnsubscribeResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for UnsubscribeTransforms class
 * 
 * @author Neil Webb
 */
public class UnsubscribeTransformsTest
{

    private static Log log = LogFactory.getLog(UnsubscribeTransformsTest.class);

    @Test
    public void testUnsubscribeRequest()
    {
        log.debug("Start testUnsubscribeRequest");
        try
        {
            LogNhinUnsubscribeRequestType message = new LogNhinUnsubscribeRequestType();
            message.setDirection("Inbound");
            message.setInterface("Nhin");

            UnsubscribeRequestType unsubscribeRequest = new UnsubscribeRequestType();

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
            unsubscribeRequest.setAssertion(assertion);

            // Unsubscribe
            UnsubscribeType unsubscribe = new UnsubscribeType();
            SubscriptionReferenceType subRef = new SubscriptionReferenceType();
            ReferenceParametersType refParams = new ReferenceParametersType();
            ReferenceParameterType refParam = new ReferenceParameterType();
            refParam.setPrefix("nhin");
            refParam.setNamespace("http://www.hhs.gov/healthid/nhin");
            refParam.setElementName("SubscriptionId");
            refParam.setValue("testval");
            refParams.getReferenceParameter().add(refParam);
            subRef.setSubscriptionManagerEndpointAddress("http://www.somewhere.com/SubscriptionManager");
            subRef.setReferenceParameters(refParams);
            unsubscribe.setSubscriptionReference(subRef);
            unsubscribeRequest.setUnsubscribe(unsubscribe);

            message.setMessage(unsubscribeRequest);

            UnsubscribeTransforms transform = new UnsubscribeTransforms();
            LogEventRequestType result = transform.transformNhinUnsubscribeRequestToAuditMessage(message);

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

            // Validate
            assertNotNull("LogEventRequestType was null", result);
            assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
            assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                    result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
            assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());

        } catch (Throwable t)
        {
            log.error("Error occured in testUnsubscribeRequest: " + t.getMessage(), t);
            fail(t.getMessage());
        }
        log.debug("End testUnsubscribeRequest");
    }

    @Test
    public void testUnsubscribeResponse()
    {
        log.debug("Begin testUnsubscribeResponse");
        try
        {
            LogUnsubscribeResponseType message = new LogUnsubscribeResponseType();
            message.setDirection("Inbound");
            message.setInterface("Nhin");

            UnsubscribeResponseMessageType unsubscribeResponseMessage = new UnsubscribeResponseMessageType();

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
            unsubscribeResponseMessage.setAssertion(assertion);

            // Unsubscribe response
            UnsubscribeResponse unsubscribeResponse = new UnsubscribeResponse();
            SubscriptionReferenceType subRef = new SubscriptionReferenceType();
            ReferenceParametersType refParams = new ReferenceParametersType();
            ReferenceParameterType refParam = new ReferenceParameterType();
            refParam.setPrefix("nhin");
            refParam.setNamespace("http://www.hhs.gov/healthid/nhin");
            refParam.setElementName("SubscriptionId");
            refParam.setValue("testval");
            refParams.getReferenceParameter().add(refParam);
            subRef.setSubscriptionManagerEndpointAddress("http://www.somewhere.com/SubscriptionManager");
            subRef.setReferenceParameters(refParams);
            unsubscribeResponseMessage.setUnsubscribeResponse(unsubscribeResponse);

            message.setMessage(unsubscribeResponseMessage);

            UnsubscribeTransforms transform = new UnsubscribeTransforms();
            LogEventRequestType result = transform.transformUnsubscribeResponseToGenericAudit(message);

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

            // Validate
            assertNotNull("LogEventRequestType was null", result);
            assertEquals(expResult.getAuditMessage().getActiveParticipant().get(0).getUserName(), result.getAuditMessage().getActiveParticipant().get(0).getUserName());
            assertEquals(expResult.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID(),
                    result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
            assertEquals(expResult.getAuditMessage().getEventIdentification().getEventActionCode(), result.getAuditMessage().getEventIdentification().getEventActionCode());

        } catch (Throwable t)
        {
            log.error("Error running testUnsubscribeResponse: " + t.getMessage(), t);
            fail(t.getMessage());
        }
        log.debug("End testUnsubscribeResponse");
    }
}