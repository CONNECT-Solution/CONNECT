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
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201306UV;
import org.hl7.v3.PRPAIN201306UVMFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UVMFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UVMFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UVMFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UVPatient;
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

        PRPAIN201305UV oPatientDiscoveryRequest = new PRPAIN201305UV();
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
//                oneOf(mockLogger).error("There was a problem translating the request into an audit log request object.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV oPatientDiscoveryRequest = new PRPAIN201305UV();
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
        PatientDiscoveryTransforms testSubject = getNewRequestObject();

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
//                allowing(mockLogger).error(with(any(String.class)));

                allowing(mockLogger).error("The UserType object or request assertion object containing the assertion user info was null.");
//                oneOf(mockLogger).error("One or more of the required fields needed to transform to an audit message request were null.");
//                atLeast(1).of(mockLogger).error("There was a problem translating the request into an audit log request object.");

                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertion = new AssertionType();
//        UserType oUserInfo = new UserType();
//        String sUserRole = "test user role";
//        String sUserName = "Test Name";
//        oUserInfo.setRole(sUserRole);
//        oUserInfo.setUserName(sUserName);
//        oAssertion.setUserInfo(oUserInfo);

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
        PatientDiscoveryTransforms testSubject = getNewRequestObject();

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));

                //TODO create additional expectations centered around why we are getting a null return value
//                oneOf(mockLogger).error("The request parameter object for the getHL7IdentifiersFromRequest() method is null.");
//                oneOf(mockLogger).error("The ControlActProcess object was missing from the request");
//                oneOf(mockLogger).error("The QueryByParameter object was missing from the request");
//                oneOf(mockLogger).error("The ParameterList object was missing from the request");
//                oneOf(mockLogger).error("The LivingSubjectId object was missing from the request");
//                oneOf(mockLogger).error("Unable to extract the HL7 Identifiers (II) object containing the patient id and community id needed for the audit request message");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
//        oPatientDiscoveryRequest.getPRPAIN201305UV().setControlActProcess(null);
        AssertionType oAssertion = new AssertionType();
        UserType oUserInfo = new UserType();
        String sUserRole = "test user role";
        String sUserName = "Test Name";
        String sHomeCommunityId = "Test Home Community Id";
        String sHomeCommunityName = "Test Home Community Name";
        oUserInfo.setRole(sUserRole);
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

        PRPAIN201305UV oPRPAIN201305UV = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        //create a LogEventRequestType that holds the same data as what should 
        //be returned by the dte method.
        AuditMessageType expResult = getTestAuditMessageType(userInfo, oPRPAIN201305UV.getTypeId());
        final LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        oPatientDiscoveryRequest.setAssertion(oAssertionType);
        oPatientDiscoveryRequest.setPRPAIN201305UV(oPRPAIN201305UV);

        PatientDiscoveryTransforms testSubject = getNewRequestObject();


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                //create mock 2 calls to the helper classes
                will(returnValue(with(any(LogEventRequestType.class))));
                will(returnValue(expected));
            }
        });

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
        context.assertIsSatisfied();
        //assertNotNull(expectedReturnValue.getAssertion());
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        PRPAIN201305UV oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        //create a LogEventRequestType that holds the same data as what should
        //be returned by the dte method.
        AuditMessageType expResult = getTestAuditMessageType(userInfo, oPatientDiscoveryRequest.getTypeId());
        final LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        PatientDiscoveryTransforms testSubject = getNewRequestObject();


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                //create mock 2 calls to the helper classes
                will(returnValue(with(any(LogEventRequestType.class))));
                will(returnValue(expected));
            }
        });

        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
        context.assertIsSatisfied();
        //assertNotNull(expectedReturnValue.getAssertion());
    }

    @Test
    public void testTransformAdapterPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        PRPAIN201305UV oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        //create a LogEventRequestType that holds the same data as what should
        //be returned by the dte method.
        AuditMessageType expResult = getTestAuditMessageType(userInfo, oPatientDiscoveryRequest.getTypeId());
        final LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        PatientDiscoveryTransforms testSubject = getNewRequestObject();


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                //create mock 2 calls to the helper classes
                will(returnValue(with(any(LogEventRequestType.class))));
                will(returnValue(expected));
            }
        });

        testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertionType);
        context.assertIsSatisfied();
        //assertNotNull(expectedReturnValue.getAssertion());
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

        PRPAIN201305UV query = new PRPAIN201305UV();
        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);

        //TODO mock the next 3 lines
        PRPAIN201306UV message = new PRPAIN201306UV();
        message.setSender(sender);

        UserType userInfo = getTestUserType();
        oAssertion.setUserInfo(userInfo);

//        PRPAIN201306UVMFMIMT700711UV01ControlActProcess oControlActProcess = oPatientDiscoveryResponseMessage.getControlActProcess();
//        List<PRPAIN201306UVMFMIMT700711UV01Subject1> oSubject1 = oControlActProcess.getSubject();
//        PRPAIN201306UVMFMIMT700711UV01RegistrationEvent oRegistrationEvent = oSubject1.get(0).getRegistrationEvent();
//        PRPAIN201306UVMFMIMT700711UV01Subject2 oSubject2 = oRegistrationEvent.getSubject1();
//        PRPAMT201310UVPatient oPatient = oSubject2.getPatient();
//        List<II> oII = oPatient.getId();
//        sPatientId = oII.get(0).getExtension();
//        sCommunityId = oII.get(0).getRoot();

        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);

        //create a LogEventRequestType that holds the same data as what should
        //be returned by the dte method.
        AuditMessageType expResult = getTestAuditMessageType(userInfo, typeId);
        final LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        PatientDiscoveryTransforms testSubject = getNewRequestObject();


        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                //create mock 2 calls to the helper classes
                will(returnValue(with(any(LogEventRequestType.class))));
                will(returnValue(expected));
            }
        });

        testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(oPatientDiscoveryResponse, oAssertion);

        context.assertIsSatisfied();
    }

    @Test
    public void confirmTransformPRPAIN201306ResponseToAuditMsgDoesTransformation() {
        final Log mockLogger = context.mock(Log.class);
        final PRPAMT201310UVPatient mockPatient = context.mock(PRPAMT201310UVPatient.class);
        final List<II> mockListII = new ArrayList<II>();
        II ii = new II();
        ii.setExtension("");
        ii.setRoot("");
        mockListII.add(ii);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {

            protected PRPAIN201306UVMFMIMT700711UV01RegistrationEvent getRegistrationEventFromSubject(List<PRPAIN201306UVMFMIMT700711UV01Subject1> oSubject1) {
                List<II> oII = new ArrayList<II>();

                final PRPAMT201310UVPatient pRPAMT201310UVPatient = new PRPAMT201310UVPatient();


                final PRPAIN201306UVMFMIMT700711UV01Subject2 pRPAIN201306UVMFMIMT700711UV01Subject2 = new PRPAIN201306UVMFMIMT700711UV01Subject2();
                pRPAIN201306UVMFMIMT700711UV01Subject2.setPatient(pRPAMT201310UVPatient);

                final PRPAIN201306UVMFMIMT700711UV01RegistrationEvent pRPAIN201306UVMFMIMT700711UV01RegistrationEvent = new PRPAIN201306UVMFMIMT700711UV01RegistrationEvent();

                pRPAIN201306UVMFMIMT700711UV01RegistrationEvent.setSubject1(pRPAIN201306UVMFMIMT700711UV01Subject2);
                return pRPAIN201306UVMFMIMT700711UV01RegistrationEvent;
            }

            protected PRPAMT201310UVPatient getPatient(PRPAIN201306UVMFMIMT700711UV01Subject2 oSubject2) {
                return mockPatient;
            }
        };

        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockPatient).getId();
                will(returnValue(mockListII));
            }
        });

        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(new UserType());

        final PRPAIN201306UV pRPAIN201306UV = new PRPAIN201306UV();
        final PRPAIN201306UVMFMIMT700711UV01ControlActProcess pRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UVMFMIMT700711UV01ControlActProcess();

        pRPAIN201306UV.setControlActProcess(pRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV, oAssertionType);

        context.assertIsSatisfied();

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
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

    private PRPAIN201305UV getTestPatientDiscoveryRequest() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();
        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);

        PRPAIN201305UV oPatientDiscoveryRequest = new PRPAIN201305UV();
        oPatientDiscoveryRequest.setSender(sender);
        oPatientDiscoveryRequest.setTypeId(typeId);

        return oPatientDiscoveryRequest;
    }

    private PatientDiscoveryTransforms getNewRequestObject() {
        PatientDiscoveryTransforms oRequest = new PatientDiscoveryTransforms() {

            protected CodedValueType getCodedValueTypeFor201305UV() {
                return new CodedValueType();
            }

            protected CodedValueType getCodedValueTypeFor201306UV() {
                return new CodedValueType();
            }

            protected EventIdentificationType getEventIdentificationType(CodedValueType eventID) {
                return new EventIdentificationType();
            }

            protected void marshalPatientDiscoveryMessage(ByteArrayOutputStream baOutStrm, PRPAIN201305UV oPatientDiscoveryRequestMessage) throws RuntimeException {
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
