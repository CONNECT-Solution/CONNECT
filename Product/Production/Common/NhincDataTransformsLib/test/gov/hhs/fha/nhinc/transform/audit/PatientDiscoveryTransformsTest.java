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
import java.io.ByteArrayOutputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
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

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(null, null);
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

        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(null, null);
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

        testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(null, null);
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
//                oneOf(mockLogger).error("There was a problem translating the request into an audit log request object.");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null);
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
//                oneOf(mockLogger).error("There was a problem translating the request into an audit log request object.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 oPatientDiscoveryRequest = new PRPAIN201305UV02();
        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null);
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
        testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null);
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

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertion);

        context.assertIsSatisfied();
    }

    /**
     * This method tests the transformPRPAIN201305RequestToAuditMsg private method
     * using the transformEntityPRPAIN201305RequestToAuditMsg method as an entry point.
     */
    @Test
    public void testTransformPRPAIN201305RequestToAuditMsgWillFailForLackOfRequiredPatientIdFields() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryTransforms testSubject = getNewPatientDiscoveryTransformsObject(mockLogger);

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                //TODO create additional expectations centered around why we are getting a null return value
                one(mockLogger).error("The request parameter object for the getHL7IdentifiersFromRequest() method is null.");
                one(mockLogger).error("One or more of the required fields needed to transform to an audit message request were null.");
                one(mockLogger).error("There was a problem translating the request into an audit log request object.");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertion = new AssertionType();
        UserType oUserInfo = new UserType();
        String sUserName = "Test Name";
        String sHomeCommunityId = "Test Home Community Id";
        String sHomeCommunityName = "Test Home Community Name";
        oUserInfo.setUserName(sUserName);
        HomeCommunityType oHomeCommunityType = new HomeCommunityType();
        oHomeCommunityType.setHomeCommunityId(sHomeCommunityId);
        oHomeCommunityType.setName(sHomeCommunityName);
        oUserInfo.setOrg(oHomeCommunityType);
        oAssertion.setUserInfo(oUserInfo);

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertion);

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

        final LogEventRequestType expected = testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
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

        final LogEventRequestType expected = testSubject.transformNhinPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
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

        final LogEventRequestType expected = testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
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
                //will(returnValue(with(any(LogEventRequestType.class))));
            }
        });

        testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201306ResponseToAuditMsgMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201306UV02ResponseType oPatientDiscoveryResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        AssertionType oAssertion = new AssertionType();

        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        II typeId = new II();

        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);

        //TODO mock the next 3 lines
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        message.setSender(sender);

        UserType userInfo = getTestUserType();
        oAssertion.setUserInfo(userInfo);

        oPatientDiscoveryResponse.setPRPAIN201306UV02(message);

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

        LogEventRequestType expectedResult = testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(oPatientDiscoveryResponse, oAssertion);

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

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV, oAssertionType);

        context.assertIsSatisfied();

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification().getEventID());
//        Assert.assertTrue(expectedResult.getAuditMessage().getEventIdentification().getEventID().getCode().equalsIgnoreCase(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRQ));
//        Assert.assertTrue(expectedResult.getAuditMessage().getEventIdentification().getEventID().getCodeSystem().equalsIgnoreCase(AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDREQ));
        /*AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_PRQ,
                               AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_PDREQ*/

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

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV, oAssertionType);

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
                one(mockLogger).error("Unable to extract patient identifiers from the response message's ControlActProcess object due to a null value.");
                one(mockLogger).error("The response message's II object required for translating to the audit request messasge's AuditSourceIdentification object was null.");
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
                one(mockLogger).error("The patient id from the II.getExtension method from the response message's II object was null.");
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
                one(mockLogger).error("The patient's assigning authority or community id from the response message's II object was null.");
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
    public void testGetHL7IdentitiersFromResponseMethod()
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

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        final PRPAIN201306UV02MFMIMT700711UV01Subject1 oPRPAIN201306UVMFMIMT700711UV01Subject1 = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        final PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent oPRPAIN201306UVMFMIMT700711UV01RegistrationEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        final PRPAIN201306UV02MFMIMT700711UV01Subject2 oPRPAIN201306UVMFMIMT700711UV01Subject2 = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        final PRPAMT201310UV02Patient oPRPAMT201310UVPatient = new PRPAMT201310UV02Patient();
        final II oII = new II();
        oPRPAMT201310UVPatient.getId().add(oII);
        oPRPAIN201306UVMFMIMT700711UV01Subject2.setPatient(oPRPAMT201310UVPatient);
        oPRPAIN201306UVMFMIMT700711UV01RegistrationEvent.setSubject1(oPRPAIN201306UVMFMIMT700711UV01Subject2);
        oPRPAIN201306UVMFMIMT700711UV01Subject1.setRegistrationEvent(oPRPAIN201306UVMFMIMT700711UV01RegistrationEvent);
        oPRPAIN201306UVMFMIMT700711UV01ControlActProcess.getSubject().add(oPRPAIN201306UVMFMIMT700711UV01Subject1);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        //test that all requirements are met
        II oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);

        context.assertIsSatisfied();
        Assert.assertNotNull(oExpectedResult);

        //test with null or empty List<II>
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's II List object due to a null or empty value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);
        //test with null or empty List<II>
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's II List object due to a null or empty value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

        //test with null patient object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's Patient object due to a null value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().setPatient(null);
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

        //test with null subject2 object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's Subject2 object due to a null value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().get(0).getRegistrationEvent().setSubject1(null);
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

        //test with null registration object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's RegistrationEvent object due to a null value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().get(0).setRegistrationEvent(null);
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

        //test with null subject1 object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's Subject1 object due to a null or empty value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.getControlActProcess().getSubject().clear();
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

        //test with null subject1 object
        oExpectation = new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to extract patient identifiers from the response message's ControlActProcess object due to a null value.");
            }
        };
        context.checking(oExpectation);
        oPRPAIN201306UV.setControlActProcess(null);
        oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        context.assertIsSatisfied();
        Assert.assertNull(oExpectedResult);

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
