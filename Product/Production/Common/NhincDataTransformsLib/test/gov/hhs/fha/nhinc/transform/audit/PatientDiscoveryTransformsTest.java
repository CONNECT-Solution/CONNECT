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
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author shawc
 *
 * Note: requirements for the LogEventRequestType
 * "Direction", required = true
 * "Interface", required = true
 * "EventIdentification", required = true
 * "ActiveParticipant", required = true
 * "AuditSourceIdentification", required = true
 * "ParticipantObjectIdentification", required = false
 */
public class PatientDiscoveryTransformsTest {

    private Mockery context;

    public PatientDiscoveryTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming Patient Discovery request message was null.");
                will(returnValue(null));
            }
        });

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming Patient Discovery request message was null.");
                will(returnValue(null));
            }
        });

        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformAdapterPRPAIN201305RequestToAuditMsgWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming Patient Discovery request message was null.");
                will(returnValue(null));
            }
        });

        testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillFailForNullAssertionTypeParameter() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).error("The Patient Discovery request did not have a PRPAIN201305UV object or an AssertionType object.");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillFailForNullAssertionTypeParameter() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).error("The AssertionType object was null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 oPatientDiscoveryRequest = new PRPAIN201305UV02();
        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformAdapterPRPAIN201305RequestToAuditMsgWillFailForNullAssertionTypeParameter() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                oneOf(mockLogger).error("The AssertionType object was null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 oPatientDiscoveryRequest = new PRPAIN201305UV02();
        testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        context.assertIsSatisfied();
    }

    /**
     * This method tests the transformPRPAIN201305RequestToAuditMsg private method
     * using the transformEntityPRPAIN201305RequestToAuditMsg method as an entry point.
     */
    @Test
    public void testTransformPRPAIN201305RequestToAuditMsgWillFailForLackOfRequiredUserInfoFields() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = getNewPatientDiscoveryTransformsObject(mockLogger);

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                one(mockLogger).error("The UserType object or request assertion object containing the assertion user info was null.");
                one(mockLogger).error("One of more UserInfo fields from the Assertion object were null.");
                one(mockLogger).error("One or more of the required fields needed to transform to an audit message request were null.");
                one(mockLogger).error("There was a problem translating the request into an audit log request object.");

                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertion = new AssertionType();

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertionType = new AssertionType();

        PRPAIN201305UV02 oPRPAIN201305UV = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        oPatientDiscoveryRequest.setAssertion(oAssertionType);
        oPatientDiscoveryRequest.setPRPAIN201305UV02(oPRPAIN201305UV);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms()
        {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage, AssertionType oAssertion)
            {
                return false;
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return false;
            }

            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage)
            {
                return mockListII.get(0);
            }
        };


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        final LogEventRequestType expected = testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        context.assertIsSatisfied();

        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        PRPAIN201305UV02 oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms()
        {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage, AssertionType oAssertion)
            {
                return false;
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return false;
            }

            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage)
            {
                return mockListII.get(0);
            }
        };



        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        final LogEventRequestType expected = testSubject.transformNhinPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        context.assertIsSatisfied();

        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformAdapterPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        PRPAIN201305UV02 oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms()
        {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage, AssertionType oAssertion)
            {
                return false;
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return false;
            }

            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage)
            {
                return mockListII.get(0);
            }
        };


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        final LogEventRequestType expected = testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);
        context.assertIsSatisfied();

        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformEntityPRPAIN201306ResponseToAuditMsgWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error("The Patient Discovery response message was null.");
            }
        });

        RespondingGatewayPRPAIN201306UV02ResponseType requestType = null;

        testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(requestType, null, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201306ResponseToAuditMsgMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201306UV02ResponseType oPatientDiscoveryResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        AssertionType oAssertion = new AssertionType();
        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        II typeId = new II();

        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);

        //TODO mock the next 3 lines
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        message.setSender(sender);

        UserType userInfo = getTestUserType();
        oAssertion.setUserInfo(userInfo);

        communityResponse.setPRPAIN201306UV02(message);
        oPatientDiscoveryResponse.getCommunityResponse().add(communityResponse);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected II getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage)
            {
                return mockListII.get(0);
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return false;
            }

            protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage, AssertionType oAssertion)
            {
                return false;
            }
        };


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        LogEventRequestType expectedResult = testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(oPatientDiscoveryResponse, oAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void confirmTransformPRPAIN201306ResponseToAuditMsgDoesTransformation() {
        final Log mockLogger = context.mock(Log.class);
        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected II getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage)
            {
                return mockListII.get(0);
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return false;
            }

            protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage, AssertionType oAssertion)
            {
                return false;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 pRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess pRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        pRPAIN201306UV.setControlActProcess(pRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV, oAssertionType, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        context.assertIsSatisfied();

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification().getEventID());

    }

    @Test
    public void testTransformPRPAIN201306ResponseToAuditMsgWillFailForNullRequiredFields()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage, AssertionType oAssertion)
            {
                return true;
            }

        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                one(mockLogger).error("One or more of the required fields needed to transform to an audit message request were null.");
                will(returnValue(null));

            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 pRPAIN201306UV = new PRPAIN201306UV02();

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV, oAssertionType, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        context.assertIsSatisfied();

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForMissingUserName()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                one(mockLogger).error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                one(mockLogger).error("One of more UserInfo fields from the Assertion object were null.");

            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        userInfo.setUserName(null);
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage, oAssertionType);

        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullHL7Identifiers()
    {
        final Log mockLogger = context.mock(Log.class);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("Unable to extract patient identifiers from the response message's ControlActProcess object due to a null value.");
                one(mockLogger).info("The response message's II object required for translating to the audit request messasge's AuditSourceIdentification object was null.");
            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage, oAssertionType);

        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullPatientId()
    {
        final Log mockLogger = context.mock(Log.class);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected II getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage)
            {
                return mockListII.get(0);
            }

        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The patient id from the II.getExtension method from the response message's II object was null.");
            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage, oAssertionType);

        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);
    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullCommunityId()
    {
        final Log mockLogger = context.mock(Log.class);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected II getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage)
            {
                return mockListII.get(0);
            }

        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The patient's assigning authority or community id from the response message's II object was null.");
            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage, oAssertionType);

        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);
    }

    @Test
    public void testAreRequiredUserTypeFieldsNullMethod()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        };
        context.checking(oExpectation);

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        boolean bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);

        context.assertIsSatisfied();
        Assert.assertFalse(bExpectedResult);

        //test with null username
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
            }
        };
        context.checking(oExpectation);
        oAssertionType.getUserInfo().setUserName(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

        //test with null home community id
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
            }
        };
        context.checking(oExpectation);
        oAssertionType.getUserInfo().setUserName("Test User");
        oAssertionType.getUserInfo().getOrg().setHomeCommunityId(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

        //test with null home community name
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
            }
        };
        context.checking(oExpectation);
        oAssertionType.getUserInfo().setUserName("Test User");
        oAssertionType.getUserInfo().getOrg().setHomeCommunityId("2.16.840.1.113883.3.200");
        oAssertionType.getUserInfo().getOrg().setName(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

        //test with null UserType object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The UserType object or request assertion object containing the assertion user info was null.");
            }
        };
        context.checking(oExpectation);
        oAssertionType.setUserInfo(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullResponseRequest()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("Unable to extract patient identifiers from the response message due to a null value.");
            }
        };
        context.checking(oExpectation);

        II oExpectedResult = testSubject.getHL7IdentitiersFromResponse(null);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullControlActProcess()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("Unable to extract patient identifiers from the response message's ControlActProcess object due to a null value.");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        II oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullQueryByParameter()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The QueryByParameter object was missing from the response");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailForNullQueryByParameter()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The QueryByParameter object was null");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        oPRPAIN201306UVMFMIMT700711UV01ControlActProcess.setQueryByParameter(oQueryByParameter);
         oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(null);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailOnNullQueryByParameterGetValue()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The QueryByParameter value object was null");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        oQueryByParameter.setValue(null);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailOnNullParameterList()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("The ParameterList object was null");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
        oQueryByParameter.setValue(oParamList);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailForEmptyLivingSubjectId()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).info("oLivingSubjectId.get(0) == null");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
        PRPAMT201306UV02ParameterList oPRPAMT201306UV02ParameterList = new PRPAMT201306UV02ParameterList();
        oPRPAMT201306UV02ParameterList.getLivingSubjectId().add(null);
        oParamList.setParameterList(oPRPAMT201306UV02ParameterList);

        oQueryByParameter.setValue(oParamList);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillPass()
    {
        final Log mockLogger = context.mock(Log.class);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };

        Expectations oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
//                one(mockLogger).error("Unable to extract the HL7 Identifiers (II) object containing the patient id and community id needed for the audit request message");
            }
        };
        context.checking(oExpectation);

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
//        List<PRPAMT201306UV02LivingSubjectId> olLivingSubjectId = new ArrayList<PRPAMT201306UV02LivingSubjectId>();
        PRPAMT201306UV02ParameterList oPRPAMT201306UV02ParameterList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02LivingSubjectId oLivingSubject = new PRPAMT201306UV02LivingSubjectId();
        final String root = "1.1";
        final String extension = "1.234.567.8902";
        II oII = new II();
        oII.setRoot(root);
        oII.setExtension(extension);
        oLivingSubject.getValue().add(oII);
        oPRPAMT201306UV02ParameterList.getLivingSubjectId().add(oLivingSubject);
        oParamList.setParameterList(oPRPAMT201306UV02ParameterList);

        oQueryByParameter.setValue(oParamList);

        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        context.assertIsSatisfied();
        Assert.assertNotNull(oExpectedResult);
        Assert.assertEquals(oExpectedResult.getRoot(), root);
        Assert.assertEquals(oExpectedResult.getExtension(), extension);
    }

    @Test
    public void testAreRequired201305fieldsNullMethod()
    {
        final Log mockLogger = context.mock(Log.class);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion)
            {
                return true;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One of more UserInfo fields from the Assertion object were null.");
            }
        });

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201305UV02 oPatientDiscoveryRequestMessage = new PRPAIN201305UV02();

        boolean bExpectedResult = testSubject.areRequired201305fieldsNull(oPatientDiscoveryRequestMessage, oAssertionType);

        context.assertIsSatisfied();
        Assert.assertTrue(bExpectedResult);

    }

    private UserType getTestUserType() {
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        UserType userInfo = new UserType();

        personName.setFamilyName("Kennedy");
        personName.setGivenName("John");
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        userInfo.setUserName("Test User");

        return userInfo;
    }

    private AuditMessageType getTestAuditMessageType(UserType userInfo, II typeId) {
        AuditMessageType expResult = new AuditMessageType();
        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserName("John Kennedy");
        expResult.getActiveParticipant().add(participant);
        AuditSourceIdentificationType auditSource = new AuditSourceIdentificationType();
        auditSource.setAuditEnterpriseSiteID(userInfo.getOrg().getName());
        expResult.getAuditSourceIdentification().add(auditSource);
        ParticipantObjectIdentificationType partObjectId = new ParticipantObjectIdentificationType();
        partObjectId.setParticipantObjectID(typeId.getExtension() + "^^^&" + userInfo.getOrg().getHomeCommunityId() + "&ISO");
        expResult.getParticipantObjectIdentification().add(partObjectId);

        return expResult;
    }

    private PRPAIN201305UV02 getTestPatientDiscoveryRequest() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();
        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);

        PRPAIN201305UV02 oPatientDiscoveryRequest = new PRPAIN201305UV02();
        oPatientDiscoveryRequest.setSender(sender);
        oPatientDiscoveryRequest.setTypeId(typeId);

        return oPatientDiscoveryRequest;
    }

    private PatientDiscoveryTransforms getNewPatientDiscoveryTransformsObject(final Log mockLogger) {
        PatientDiscoveryTransforms oRequest = new PatientDiscoveryTransforms() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected CodedValueType getCodedValueTypeFor201305UV() {
                return new CodedValueType();
            }

            protected CodedValueType getCodedValueTypeFor201306UV() {
                return new CodedValueType();
            }

            protected EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
                return new EventIdentificationType();
            }

            protected void marshalPatientDiscoveryMessage(ByteArrayOutputStream baOutStrm, PRPAIN201305UV02 oPatientDiscoveryRequestMessage) throws RuntimeException {
            }

            protected ActiveParticipant getActiveParticipant(UserType oUserInfo) {
                return new ActiveParticipant();
            }

            protected String getCompositePatientId(String sCommunityId, String sPatientId) {
                return sPatientId + "^^^&" + sCommunityId + "&ISO";
            }

            protected AuditSourceIdentificationType getAuditSourceIdentificationType(String sCommunityId, String sCommunityName) {
                return new AuditSourceIdentificationType();
            }

            protected ParticipantObjectIdentificationType getParticipantObjectIdentificationType(String sPatientId) {
                return new ParticipantObjectIdentificationType();
            }
        };
        return oRequest;
    }
}
