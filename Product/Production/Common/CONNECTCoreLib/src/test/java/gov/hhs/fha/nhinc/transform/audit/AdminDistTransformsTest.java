/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 *
 * @author dunnek
 */
public class AdminDistTransformsTest {
    private Mockery context;
    
    public AdminDistTransformsTest() {
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



    /**
     * Test of transformEntitySendAlertToAuditMsg method, of class AdminDistTransforms.
     */
    @Test
    public void testTransformEntitySendAlertToAuditMsg_Null() {
        System.out.println("testTransformEntitySendAlertToAuditMsg_Null");
        final Log mockLogger = context.mock(Log.class);
        
        RespondingGatewaySendAlertMessageType message = null;
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The Incoming Send Alert message was Null");
                will(returnValue(null));
            }
        });
        
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformEntitySendAlertToAuditMsg(null, null,NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }
    @Test
    public void testTransformEntitySendAlertToAuditMsg_NullAssert() {
        System.out.println("testTransformEntitySendAlertToAuditMsg_NullAssert");
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The SendAlert did not have an EDXLDistribution or Assertion Object ");
                will(returnValue(null));
            }
        });

        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformEntitySendAlertToAuditMsg(message, null,NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }

   @Test
    public void testTransformEntitySendAlertToAuditMsg_Empty() {
        System.out.println("testTransformEntitySendAlertToAuditMsg_Empty");
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        AssertionType assertion = new AssertionType();
        String direction = "";
        String _interface = "";
        message.setEDXLDistribution(new EDXLDistribution());

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The UserType object or request assertion object containing the assertion user info was null.");
                one(mockLogger).error("One or more of the required fields needed to transform to an audit message request were null.");
                one(mockLogger).error("There was a problem translating the request into an audit log request object.");

                will(returnValue(null));
            }
        });

        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformEntitySendAlertToAuditMsg(message, assertion,NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }

   @Test
    public void testTransformEntitySendAlertToAuditMsg_Good() {
        System.out.println("testTransformEntitySendAlertToAuditMsg_Good");
        final Log mockLogger = context.mock(Log.class);

        RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId("123");
        hc.setName("test");

        user.setUserName("test");
        
        user.setOrg(hc);

        assertion.setUserInfo(user);
        
        message.setEDXLDistribution(new EDXLDistribution());

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                will(returnValue(null));
            }
        });

        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformEntitySendAlertToAuditMsg(message, assertion,NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        context.assertIsSatisfied();
        assertNotNull(result);
        assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, result.getDirection());
        assertEquals(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, result.getInterface());

        assertNotNull(result.getAuditMessage());
        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertNotNull(result.getAuditMessage().getEventIdentification());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,result.getAuditMessage().getEventIdentification().getEventID().getCode());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,result.getAuditMessage().getEventIdentification().getEventID().getCodeSystemName());
        

    }

    /**
     * Test of areRequiredUserTypeFieldsNull method, of class AdminDistTransforms.
     */
    @Test
    public void testAreRequiredUserTypeFieldsNull() {
        System.out.println("areRequiredUserTypeFieldsNull");
        final Log mockLogger = context.mock(Log.class);
        AssertionType oAssertion = null;
        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error(with(any(String.class)));
                will(returnValue(null));
            }
        });
        
        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(oAssertion);
        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }
    @Test
    public void testAreRequiredUserTypeFieldsNull_NoUserName() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoUserName");
        final Log mockLogger = context.mock(Log.class);
        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(new UserType());

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                
                one(mockLogger).error("Incomming request.getAssertion.getUserInfo.getUserName was null.");

                will(returnValue(null));
            }
        });

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);
        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NoHCID() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoHCID");
        final Log mockLogger = context.mock(Log.class);
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        user.setUserName("test");
        user.setOrg(hc);
        
        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                

                will(returnValue(null));
            }
        });

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);
        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NoHCName() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoHCName");
        final Log mockLogger = context.mock(Log.class);
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId("123");

        user.setUserName("test");
        user.setOrg(hc);

        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));


                will(returnValue(null));
            }
        });

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);
        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }

   @Test
    public void testAreRequiredUserTypeFieldsNull_False() {
        System.out.println("testAreRequiredUserTypeFieldsNull_False");
        final Log mockLogger = context.mock(Log.class);
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId("123");
        hc.setName("test");
        
        user.setUserName("test");
        user.setOrg(hc);

        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms(){

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));

                will(returnValue(null));
            }
        });

        boolean expResult = false;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);
        context.assertIsSatisfied();
        assertEquals(expResult, result);

    }
}