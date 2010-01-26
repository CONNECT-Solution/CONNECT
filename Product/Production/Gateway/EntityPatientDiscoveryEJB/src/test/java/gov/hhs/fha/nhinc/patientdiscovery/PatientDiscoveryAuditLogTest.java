/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLog;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
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
 */
public class PatientDiscoveryAuditLogTest {

    private Mockery context;

    public PatientDiscoveryAuditLogTest() {
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
    public void testAuditEntityRequestWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the entity. The incomming request was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.auditEntityRequest(null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditEntityResponseWillFailForNullResponse() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the entity. The incomming response was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.auditEntityResponse(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditEntityResponseWillFailForNullAssertion() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the entity. The incomming response assertion was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        RespondingGatewayPRPAIN201306UV02ResponseType oResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        testSubject.auditEntityResponse(oResponse, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyRequestWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the proxy. The incomming request was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.auditProxyRequest(null,null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyRequestWillFailForNullAssertion() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the proxy. The incomming request assertion was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        ProxyPRPAIN201305UVProxySecuredRequestType oRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
        testSubject.auditProxyRequest(oRequest, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyResponseWillFailForNullResponse() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the proxy. The incomming response was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.auditProxyResponse(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyResponseWillFailForNullAssertion() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("Unable to create an audit log record for the proxy. The incomming response assertion was null.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        PRPAIN201306UV02 oResponse = new PRPAIN201306UV02();
        testSubject.auditProxyResponse(oResponse, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryRequestWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("There was a problem creating an audit log for the request (LogEventRequestType parameter was null). The audit record was not created.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.logPatientDiscoveryRequest(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryRequestWillFailForNullAssertion() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("There was a problem creating an audit log for the request (AssertionType parameter was null). The audit record was not created.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        LogEventRequestType oLogEventRequestType = new LogEventRequestType();
        testSubject.logPatientDiscoveryRequest(oLogEventRequestType, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryResponseWillFailForNullResponse() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("There was a problem creating an audit log for the response (LogEventRequestType parameter was null). The audit record was not created.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        testSubject.logPatientDiscoveryResponse(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryResponseWillFailForNullAssertion() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("There was a problem creating an audit log for the response (AssertionType parameter was null). The audit record was not created.");
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        LogEventRequestType oLogEventRequestType = new LogEventRequestType();
        testSubject.logPatientDiscoveryResponse(oLogEventRequestType, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryResponseWillPass() {
        final Log mockLogger = context.mock(Log.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = context.mock(AuditRepositoryProxy.class);
        final AuditRepositoryProxyObjectFactory mockAuditRepositoryProxyObjectFactory =
                context.mock(AuditRepositoryProxyObjectFactory.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory() {
                return mockAuditRepositoryProxyObjectFactory;
            }

            @Override
            protected AuditRepositoryProxy getAuditRepositoryProxy(AuditRepositoryProxyObjectFactory auditRepoFactory) {
                return mockAuditRepositoryProxy;
            }

            @Override
            protected AcknowledgementType getAuditLogProxyResponse(AuditRepositoryProxy proxy, LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        LogEventRequestType oLogEventRequestType = new LogEventRequestType();
        AssertionType oAssertionType = new AssertionType();
        testSubject.logPatientDiscoveryResponse(oLogEventRequestType, oAssertionType);
        context.assertIsSatisfied();
    }

    @Test
    public void testLogPatientDiscoveryRequestWillPass() {
        final Log mockLogger = context.mock(Log.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = context.mock(AuditRepositoryProxy.class);
        final AuditRepositoryProxyObjectFactory mockAuditRepositoryProxyObjectFactory =
                context.mock(AuditRepositoryProxyObjectFactory.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory() {
                return mockAuditRepositoryProxyObjectFactory;
            }

            @Override
            protected AuditRepositoryProxy getAuditRepositoryProxy(AuditRepositoryProxyObjectFactory auditRepoFactory) {
                return mockAuditRepositoryProxy;
            }

            @Override
            protected AcknowledgementType getAuditLogProxyResponse(AuditRepositoryProxy proxy, LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        LogEventRequestType oLogEventRequestType = new LogEventRequestType();
        AssertionType oAssertionType = new AssertionType();
        testSubject.logPatientDiscoveryRequest(oLogEventRequestType, oAssertionType);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditEntityResponseWillPass() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected LogEventRequestType getLogEventRequestTypeForEntityResponseMessage(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
                return new LogEventRequestType();
            }

            @Override
            public AcknowledgementType logPatientDiscoveryResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        RespondingGatewayPRPAIN201306UV02ResponseType oResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        AssertionType oAssertionType = new AssertionType();
        testSubject.auditEntityResponse(oResponse, oAssertionType);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditEntityRequestWillPass() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected LogEventRequestType getLogEventRequestTypeForEntityRequestMessage(RespondingGatewayPRPAIN201305UV02RequestType request) {
                return new LogEventRequestType();
            }

            @Override
            public AcknowledgementType logPatientDiscoveryRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        testSubject.auditEntityRequest(oRequest);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyResponseWillPass() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected LogEventRequestType getLogEventRequestTypeForProxyResponseMessage(PRPAIN201306UV02 response, AssertionType assertion) {
                return new LogEventRequestType();
            }

            @Override
            public AcknowledgementType logPatientDiscoveryResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        PRPAIN201306UV02 oResponse = new PRPAIN201306UV02();
        AssertionType oAssertionType = new AssertionType();
        testSubject.auditProxyResponse(oResponse, oAssertionType);
        context.assertIsSatisfied();
    }

    @Test
    public void testAuditProxyRequestWillPass() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryAuditLog testSubject = new PatientDiscoveryAuditLog() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected LogEventRequestType getLogEventRequestTypeForProxyRequestMessage(ProxyPRPAIN201305UVProxySecuredRequestType request, AssertionType assertion) {
                return new LogEventRequestType();
            }

            @Override
            public AcknowledgementType logPatientDiscoveryRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
                return new AcknowledgementType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(AcknowledgementType.class))));
            }
        });

        ProxyPRPAIN201305UVProxySecuredRequestType oRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
        AssertionType oAssertionType = new AssertionType();
        testSubject.auditProxyRequest(oRequest, oAssertionType);
        context.assertIsSatisfied();
    }


}
