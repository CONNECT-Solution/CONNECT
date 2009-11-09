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
import gov.hhs.fha.nhinc.common.auditlog.LogPatientDiscoveryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.PatientDiscoveryMessageType;
import gov.hhs.fha.nhinc.common.auditlog.SubjectRevisedMessageType;
import gov.hhs.fha.nhinc.transform.policy.*;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201302Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PIXConsumerPRPAIN201305UVRequestType;
import org.hl7.v3.PRPAIN201302UV;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAIN201305UVQUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UVPatient;
import org.hl7.v3.PRPAMT201301UVPerson;
import org.hl7.v3.PRPAMT201306UVLivingSubjectId;
import org.hl7.v3.PRPAMT201306UVParameterList;
import org.hl7.v3.PRPAMT201306UVQueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
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
    public void setUp() {
        context = new Mockery();
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
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillFailForLackOfRequiredFields() {
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
        AssertionType oAssertion = new AssertionType();
        UserType oUserInfo = new UserType();
        String sUserRole = "test user role";
        String sUserName = "Test Name";
        oUserInfo.setRole(sUserRole);
        oUserInfo.setUserName(sUserName);
        oAssertion.setUserInfo(oUserInfo);

        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, oAssertion);
        
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillPass() {
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertionType = new AssertionType();

        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();

        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);

        //TODO mock the next 3 lines
        JAXBElement<PRPAMT201301UVPerson> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", "20080226", "123456789");
        PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(person, typeId);
        PRPAIN201305UV message = HL7PRPA201305Transforms.createPRPA201305(patient, sender.getTypeId().getRoot(), "receiverId", "localDeviceId");//new PRPAIN201305UV();
        message.setSender(sender);

        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);

        //create a LogEventRequestType that holds the same data as what should 
        //be returned by the dte method.
        AuditMessageType expResult = getTestAuditMessageType(userInfo, typeId);
        final LogEventRequestType expected = new LogEventRequestType();
        expected.setAuditMessage(expResult);

        oPatientDiscoveryRequest.setAssertion(oAssertionType);
        oPatientDiscoveryRequest.setPRPAIN201305UV(message);

        PatientDiscoveryTransforms testSubject = getNewRequestObject();
        

        context.checking(new Expectations()
        {
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
    public void testTransformEntityPRPAIN201306ResponseToAuditMsgMsgWillPass()
    {
        final Log mockLogger = context.mock(Log.class);

    }

    private UserType getTestUserType()
    {
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

    private AuditMessageType getTestAuditMessageType(UserType userInfo, II typeId)
    {
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

    private PatientDiscoveryTransforms getNewRequestObject()
    {
        PatientDiscoveryTransforms oRequest = new PatientDiscoveryTransforms(){
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
