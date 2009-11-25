/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import junit.framework.Assert;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ActRelationshipMitigates;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueEvent;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueManagement;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodDefEvn;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class HL7PRPA201306TransformsTest {

    private Mockery context;

    private static Log log = LogFactory.getLog(HL7PRPA201306TransformsTest.class);
    private String localDeviceId = "2.16.840.1.113883.3.200.1";
    private String senderOID = "2.16.840.1.113883.3.200";
    private String receiverOID = "2.16.840.1.113883.3.184";
    private String patientFirstName = "Thomas";
    private String patientLastName = "Kirtland";
    private String gender = "M";
    private String birthTime = "19261225";
    private String ssn = "134679852";
    private String patId = "46821564";

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
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
    public void testCreatePRPA201306() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(PRPAIN201306UV02.class))));
            }
        });
        log.info("testCreatePRPA201306");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);

        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(patient, senderOID, receiverOID, localDeviceId);
        PRPAIN201306UV02 result = HL7PRPA201306Transforms.createPRPA201306(patient, senderOID, receiverOID, receiverOID, localDeviceId, query);

        context.assertIsSatisfied();

        TestHelper.assertReceiverIdEquals(receiverOID, result);
        TestHelper.assertSenderIdEquals(senderOID, result);
    /*
    TestHelper.assertPatientIdEquals(patId, localDeviceId, result);
    TestHelper.assertBirthTimeEquals(birthTime, result);
    TestHelper.assertPatientNameEquals(patientFirstName, patientLastName, result);
    TestHelper.assertGenderEquals(gender, result);
     */
    }

    @Test
    public void testcreatePRPA201306ForPatientNotFoundWillPass() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected String getReceiverOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
                return receiverOID;
            }

            protected String getSenderOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
                return senderOID;
            }

            protected II getHL7MessageId(String receiverOID) {
                II oII = new II();
                oII.setRoot(receiverOID);

                return oII;
            }

            protected TSExplicit getHL7CreationTime() {
                return new TSExplicit();
            }

            protected II getHL7InteractionId() {
                return new II();
            }

            protected CS getHL7ProcessingCode() {
                return new CS();
            }

            protected CS getHL7ProcessingModeCode() {
                return new CS();
            }

            protected CS getHL7AcceptAckCode() {
                return new CS();
            }
            
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(PRPAIN201306UV02.class))));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);

        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(patient, senderOID, receiverOID, localDeviceId);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        //reverse the sender and receiver expectations
        TestHelper.assertReceiverIdEquals(senderOID, result);
        TestHelper.assertSenderIdEquals(receiverOID, result);

        //AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        Assert.assertNotNull(result.getAcknowledgement());
        Assert.assertEquals("AA", result.getAcknowledgement().get(0).getTypeCode().getCode());
        //OK (data found, no errors) is returned in QueryAck.queryResponseCode (control act wrapper)
        Assert.assertNotNull(result.getControlActProcess());
        Assert.assertNotNull(result.getControlActProcess().getQueryAck());
        Assert.assertEquals("OK", result.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
        //There is no RegistrationEvent returned in the response
        Assert.assertNotNull(result.getControlActProcess().getSubject());
        Assert.assertNotNull(result.getControlActProcess().getSubject().get(0));
        Assert.assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent());

    }

    @Test
    public void testcreatePRPA201306ForPatientNotFoundWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(null);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testcreatePRPA201306ForPatientNotFoundWillFailForNullRequiredFields() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areIncommingRequiredPRPAIN201305FieldsNull(PRPAIN201305UV02 oRequest) {
                return true;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreRequiredPRPAIN201305FieldsForPatientNotFoundNullWillFailForNullReceiver() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return true;
            }


        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreRequiredPRPAIN201305FieldsForPatientNotFoundNullWillFailForNullSender() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }
            protected boolean areSenderFieldsNull(PRPAIN201305UV02 oRequest){
                return true;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreRequiredPRPAIN201305FieldsForPatientNotFoundNullWillFailForNullControlActProcess() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }
            protected boolean areSenderFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

            protected boolean areControlActProcessFieldsNull (PRPAIN201305UV02 oRequest){
                return true;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One or more ControlActProcess fields from the incomming request were null.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreRequiredPRPAIN201305FieldsForPatientNotFoundNullWillFailForNullInteractionId() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }
            protected boolean areSenderFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

            protected boolean areControlActProcessFieldsNull (PRPAIN201305UV02 oRequest){
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The InteractionId object from the incomming request message is null.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForEmptyReceiverList() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The list of receiver objects from the incomming request message were null or empty.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.getReceiver().clear();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForNullReceiver() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The request's receiver object is null.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.getReceiver().add(null);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForNullReceiverDevice() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The request's receiver device object is null.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = null;
        MCCIMT000100UV01Receiver oReceiver = new MCCIMT000100UV01Receiver();
        oReceiver.setDevice(oDevice);
        query.getReceiver().add(oReceiver);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForEmptyReceiverDeviceIds() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The list of device ids from the receiver object were null or empty.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        oDevice.getId().clear();
        MCCIMT000100UV01Receiver oReceiver = new MCCIMT000100UV01Receiver();
        oReceiver.setDevice(oDevice);
        query.getReceiver().add(oReceiver);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForNullReceiverDeviceId() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The device II id object is null.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        II oII = null;
        oDevice.getId().add(oII);
        MCCIMT000100UV01Receiver oReceiver = new MCCIMT000100UV01Receiver();
        oReceiver.setDevice(oDevice);
        query.getReceiver().add(oReceiver);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreReceiverFieldsNullWillFailForNullReceiverDeviceIdGetRoot() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The device II id getRoot value is null.");
                one(mockLogger).error("One or more request receiver fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        II oII = new II();
        oDevice.getId().add(oII);
        MCCIMT000100UV01Receiver oReceiver = new MCCIMT000100UV01Receiver();
        oReceiver.setDevice(oDevice);
        query.getReceiver().add(oReceiver);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreSenderFieldsNullWillFailForNullSender() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The sender object from the incomming request message was null.");
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        query.setSender(null);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreSenderFieldsNullWillFailForNullSenderDevice() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The request's sender device object is null.");
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Sender oSender = new MCCIMT000100UV01Sender();
        query.setSender(oSender);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreSenderFieldsNullWillFailForEmptySenderDeviceIds() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The list of device ids from the sender object were null or empty.");
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        oDevice.getId().clear();
        MCCIMT000100UV01Sender oSender = new MCCIMT000100UV01Sender();
        oSender.setDevice(oDevice);
        query.setSender(oSender);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreSenderFieldsNullWillFailForNullSenderDeviceId() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The sender device id (II) object is null.");
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        II oII = null;
        oDevice.getId().add(oII);
        MCCIMT000100UV01Sender oSender = new MCCIMT000100UV01Sender();
        oSender.setDevice(oDevice);
        query.setSender(oSender);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testAreSenderFieldsNullWillFailForNullSenderDeviceIdGetRoot() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areReceiverFieldsNull(PRPAIN201305UV02 oRequest){
                return false;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The device id value (i.e. II.getRoot() or " +
                    "oRequest.getSender().getDevice().getId().get(0).getRoot()) is null.");
                one(mockLogger).error("One or more request sender fields are null or empty.");
                one(mockLogger).error("One or more required fields from the patient discovery request " +
                    "for the patient not found scenario are null.");
                will(returnValue(null));
            }
        });

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        MCCIMT000100UV01Device oDevice = new MCCIMT000100UV01Device();
        II oII = new II();
        oDevice.getId().add(oII);
        MCCIMT000100UV01Sender oSender = new MCCIMT000100UV01Sender();
        oSender.setDevice(oDevice);
        query.setSender(oSender);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForPatientNotFound(query);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testCreatePRPA201306ForErrorsWillPass()
    {

        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected String getReceiverOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
                return receiverOID;
            }

            protected String getSenderOIDFromPRPAIN201305UV02Request(PRPAIN201305UV02 oRequest) {
                return senderOID;
            }

            protected II getHL7MessageId(String receiverOID) {
                II oII = new II();
                oII.setRoot(receiverOID);

                return oII;
            }

            protected TSExplicit getHL7CreationTime() {
                return new TSExplicit();
            }

            protected II getHL7InteractionId() {
                return new II();
            }

            protected CS getHL7ProcessingCode() {
                return new CS();
            }

            protected CS getHL7ProcessingModeCode() {
                return new CS();
            }

            protected CS getHL7AcceptAckCode() {
                return new CS();
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(PRPAIN201306UV02.class))));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientFirstName, patientLastName, gender, birthTime, ssn);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patId, localDeviceId);

        MCAIMT900001UV01DetectedIssueEvent oDetectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
        //<detectedIssueEvent classCode="ALRT" moodCode="EVN">
        oDetectedIssueEvent.getClassCode().add(HL7Constants.DETECTED_ISSUE_CLASSCODE_ALRT);
        oDetectedIssueEvent.getMoodCode().add(HL7Constants.DETECTED_ISSUE_MOODCODE_EVN);
        MCAIMT900001UV01DetectedIssueManagement oDetectedIssueManagement = new MCAIMT900001UV01DetectedIssueManagement();
        oDetectedIssueManagement.getClassCode().add(HL7Constants.DETECTEDISSUEMANAGEMENT_CLASSCODE);

        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(patient, senderOID, receiverOID, localDeviceId);
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForErrors(query,HL7Constants.DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY);

        context.assertIsSatisfied();

        //reverse the sender and receiver expectations
        TestHelper.assertReceiverIdEquals(senderOID, result);
        TestHelper.assertSenderIdEquals(receiverOID, result);

        //AA (application accept) is returned in Acknowledgement.typeCode (transmission wrapper).
        Assert.assertNotNull(result.getAcknowledgement());
        Assert.assertEquals("AA", result.getAcknowledgement().get(0).getTypeCode().getCode());
        //QE (application error) is returned in QueryAck.queryResponseCode (control act wrapper)
        Assert.assertNotNull(result.getControlActProcess());
        Assert.assertNotNull(result.getControlActProcess().getQueryAck());
        Assert.assertEquals("QE", result.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
        //There is no RegistrationEvent returned in the response
        Assert.assertNotNull(result.getControlActProcess().getSubject());
        Assert.assertNotNull(result.getControlActProcess().getSubject().get(0));
        Assert.assertNull(result.getControlActProcess().getSubject().get(0).getRegistrationEvent());

        // verify that the responderBusy code is returned
         Assert.assertNotNull(result.getControlActProcess().getReasonOf());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0));
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getClassCode());
         Assert.assertEquals(oDetectedIssueEvent.getClassCode(),
                 result.getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent()
                 .getClassCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMoodCode());
         Assert.assertEquals(oDetectedIssueEvent.getMoodCode(),
                 result.getControlActProcess().getReasonOf().get(0).getDetectedIssueEvent()
                 .getMoodCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getCode());
         Assert.assertEquals(HL7Constants.DETECTED_ISSUE_CODE_ADMINISTRATIVE,result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getCode().getCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getCode().getCodeSystem());
         Assert.assertEquals(HL7Constants.DETECTED_ISSUE_CODESYSTEM_ERROR_CODE,result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getCode().getCodeSystem());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0));
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getTypeCode());
         Assert.assertEquals(ActRelationshipMitigates.MITGT, result.getControlActProcess()
                 .getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0).getTypeCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement().getClassCode());
         Assert.assertEquals(oDetectedIssueManagement.getClassCode(), result.getControlActProcess()
                 .getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0)
                 .getDetectedIssueManagement().getClassCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement().getMoodCode());
         Assert.assertEquals(XActMoodDefEvn.EVN, result.getControlActProcess()
                 .getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0)
                 .getDetectedIssueManagement().getMoodCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement().getCode());

         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement()
                 .getCode().getCode());
         Assert.assertEquals(HL7Constants.DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY, result.getControlActProcess()
                 .getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0)
                 .getDetectedIssueManagement().getCode().getCode());
         Assert.assertNotNull(result.getControlActProcess().getReasonOf().get(0)
                 .getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement()
                 .getCode().getCodeSystem());
         Assert.assertEquals(HL7Constants.DETECTEDISSUEMANAGEMENT_CODESYSTEM, result.getControlActProcess()
                 .getReasonOf().get(0).getDetectedIssueEvent().getMitigatedBy().get(0).getDetectedIssueManagement()
                 .getCode().getCodeSystem());
         
    }

    @Test
    public void testCreatePRPA201306ForErrorsWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming patient discovery request, PRPAIN201305UV02, message was null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201306UV02 result = testSubject.createPRPA201306ForErrors(null,HL7Constants.DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testCreatePRPA201306ForErrorsWillFailForNullRequiredFields() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areIncommingRequiredPRPAIN201305FieldsNull(PRPAIN201305UV02 oRequest) {
                return true;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("One or more required fields from the patient discovery request are null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForErrors(query, HL7Constants.DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

    @Test
    public void testCreatePRPA201306ForErrorsWillFailForNullErrorCodeParameter() {
        final Log mockLogger = context.mock(Log.class);
        HL7PRPA201306Transforms testSubject = new HL7PRPA201306Transforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            protected boolean areIncommingRequiredPRPAIN201305FieldsNull(PRPAIN201305UV02 oRequest) {
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The sErrorCode parameter was null.");
                will(returnValue(null));
            }
        });
        log.info("testcreatePRPA201306ForPatientNotFoundWillPass");

        PRPAIN201305UV02 query = new PRPAIN201305UV02();
        PRPAIN201306UV02 result = testSubject.createPRPA201306ForErrors(query, null);

        context.assertIsSatisfied();

        Assert.assertNull(result);

    }

}
