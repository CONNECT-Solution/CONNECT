package gov.hhs.fha.nhinc.docretrievedeferred;

import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.common.auditlog.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
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

/**
 *
 * @author Sai Valluripalli
 */
public class DocRetrieveDeferredAuditLoggerTest {

    private Mockery mockery;

    public DocRetrieveDeferredAuditLoggerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLogDocRetrieveDeferredHappyWithLog() {
        final Log mockLogger = mockery.mock(Log.class);
        final LogEventRequestType mockLogEventRequestType = mockery.mock(LogEventRequestType.class);
        final AcknowledgementType mockAcknowledgementType = mockery.mock(AcknowledgementType.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = mockery.mock(AuditRepositoryProxy.class);

        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return true;
            }

            @Override
            protected LogEventRequestType auditRepositoryLogger(DocRetrieveMessageType message, String direction, String _interface) {
                return mockLogEventRequestType;
            }

            @Override
            protected AuditRepositoryProxy getAuditProxy() {
                return mockAuditRepositoryProxy;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
                allowing(mockAuditRepositoryProxy).auditLog(with(any(LogEventRequestType.class)), with(any(AssertionType.class)));
                will(returnValue(mockAcknowledgementType));
            }
        });

        DocRetrieveMessageType message = new DocRetrieveMessageType();
        String direction = "Inbound";
        String _interface = "Entity";
        AssertionType assertion = new AssertionType();
        //assertNotNull(testSubject.auditDocRetrieveDeferredRequest(null, assertion));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testLogDocRetrieveDeferredHappyWithoutLog() {
        final Log mockLogger = mockery.mock(Log.class);
        final LogEventRequestType mockLogEventRequestType = mockery.mock(LogEventRequestType.class);
        final AcknowledgementType mockAcknowledgementType = mockery.mock(AcknowledgementType.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = mockery.mock(AuditRepositoryProxy.class);

        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return false;
            }

            @Override
            protected LogEventRequestType auditRepositoryLogger(DocRetrieveMessageType message, String direction, String _interface) {
                return mockLogEventRequestType;
            }

            @Override
            protected AuditRepositoryProxy getAuditProxy() {
                return mockAuditRepositoryProxy;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockAuditRepositoryProxy).auditLog(with(any(LogEventRequestType.class)), with(any(AssertionType.class)));
                will(returnValue(mockAcknowledgementType));
            }
        });

        DocRetrieveMessageType message = new DocRetrieveMessageType();
        String direction = "Inbound";
        String _interface = "Entity";
        AssertionType assertion = new AssertionType();
        //assertNotNull(testSubject.logDocRetrieveDeferred(message, direction, _interface, assertion));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testLogDocRetrieveDeferredBad() {
        final Log mockLogger = mockery.mock(Log.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = mockery.mock(AuditRepositoryProxy.class);

        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return true;
            }

            @Override
            protected LogEventRequestType auditRepositoryLogger(DocRetrieveMessageType message, String direction, String _interface) {
                return null;
            }

            @Override
            protected AuditRepositoryProxy getAuditProxy() {
                return mockAuditRepositoryProxy;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
                allowing(mockAuditRepositoryProxy).auditLog(with(any(LogEventRequestType.class)), with(any(AssertionType.class)));
                will(returnValue(null));
            }
        });

        DocRetrieveMessageType message = new DocRetrieveMessageType();
        String direction = "Inbound";
        String _interface = "Entity";
        AssertionType assertion = new AssertionType();
        //assertNull(testSubject.logDocRetrieveDeferred(message, direction, _interface, assertion));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testLogDocRetrieveDeferredBadBad() {
        final Log mockLogger = mockery.mock(Log.class);
        final AuditRepositoryProxy mockAuditRepositoryProxy = mockery.mock(AuditRepositoryProxy.class);

        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return true;
            }

            @Override
            protected LogEventRequestType auditRepositoryLogger(DocRetrieveMessageType message, String direction, String _interface) {
                return null;
            }

            @Override
            protected AuditRepositoryProxy getAuditProxy() {
                return null;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
                allowing(mockAuditRepositoryProxy).auditLog(with(any(LogEventRequestType.class)), with(any(AssertionType.class)));
                will(returnValue(null));
            }
        });

        DocRetrieveMessageType message = new DocRetrieveMessageType();
        String direction = "Inbound";
        String _interface = "Entity";
        AssertionType assertion = new AssertionType();
        //assertNull(testSubject.logDocRetrieveDeferred(message, direction, _interface, assertion));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testAuditDocRetrieveDeferredRequestHappyWithLogger() {
        final Log mockLogger = mockery.mock(Log.class);
        final AcknowledgementType mockAcknowledgementType = mockery.mock(AcknowledgementType.class);
        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return true;
            }

            @Override
            protected AcknowledgementType logDocRetrieveDeferred(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion) {
                return mockAcknowledgementType;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
            }
        });

        RetrieveDocumentSetRequestType auditMsg = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        assertNotNull(testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
        assertEquals(mockAcknowledgementType, testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
    }

    @Test
    public void testAuditDocRetrieveDeferredRequestHappyWithoutLogger() {
        final Log mockLogger = mockery.mock(Log.class);
        final AcknowledgementType mockAcknowledgementType = mockery.mock(AcknowledgementType.class);
        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return false;
            }

            @Override
            protected AcknowledgementType logDocRetrieveDeferred(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion) {
                return mockAcknowledgementType;
            }
        };

        RetrieveDocumentSetRequestType auditMsg = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        assertNotNull(testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
        assertEquals(mockAcknowledgementType, testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
    }

    @Test
    public void testAuditDocRetrieveDeferredRequestBad() {
        final Log mockLogger = mockery.mock(Log.class);

        DocRetrieveDeferredAuditLogger testSubject = new DocRetrieveDeferredAuditLogger() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean setLog4jDebugValue() {
                return true;
            }

            @Override
            protected AcknowledgementType logDocRetrieveDeferred(DocRetrieveMessageType message, String direction, String _interface, AssertionType assertion) {
                return null;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                will(returnValue(null));
            }
        });

        RetrieveDocumentSetRequestType auditMsg = null;
        AssertionType assertion = null;
        assertNull(testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
        assertEquals(null, testSubject.auditDocRetrieveDeferredRequest(auditMsg, assertion));
    }
}
