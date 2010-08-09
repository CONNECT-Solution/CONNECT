/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import java.util.ArrayList;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
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
 * @author patlollav
 */
public class NhincDocQueryDeferredRequestOrchImplTest {

    private Mockery mockery = null;

    public NhincDocQueryDeferredRequestOrchImplTest() {
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

    /**
     * Test of crossGatewayQueryRequest method, of class NhincDocQueryDeferredRequestImpl.
     */
    @Test
    public void testCrossGatewayQueryRequestHappyPath() {
        System.out.println("crossGatewayQueryRequest: Happy path");


        final Log mockLogger = mockery.mock(Log.class);
        final RespondingGatewayCrossGatewayQueryRequestType mockCrossGatewayQueryRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
        final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocQueryAuditLog mockDocQueryAuditLog = mockery.mock(DocQueryAuditLog.class);
        final AdhocQueryRequest mockAdhocQueryRequest = mockery.mock(AdhocQueryRequest.class);
        final NhinTargetSystemType mockTarget = mockery.mock(NhinTargetSystemType.class);


        NhincDocQueryDeferredRequestOrchImpl instance = new NhincDocQueryDeferredRequestOrchImpl(){
            protected Log getLogger(){
                return mockLogger;
            }

            protected DocQueryAuditLog getDocQueryAuditLogger(){
                return mockDocQueryAuditLog;

            }

            @Override
            protected DocQueryAcknowledgementType callNhinDocQueryDeferredService(AdhocQueryRequest adhocQueryRequest, AssertionType assertion, NhinTargetSystemType target) {
                return new DocQueryAcknowledgementType();
            }


        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                allowing(mockCrossGatewayQueryRequest).getAssertion();
                will(returnValue(mockAssertion));
                allowing(mockCrossGatewayQueryRequest).getAdhocQueryRequest();
                will(returnValue(mockAdhocQueryRequest));
                allowing(mockCrossGatewayQueryRequest).getNhinTargetSystem();
                will(returnValue(mockTarget));
                allowing(mockAssertion).setMessageId(with(any(String.class)));
                allowing(mockAssertion).getRelatesToList();
                one(mockDocQueryAuditLog).auditDQRequest(with(any(AdhocQueryRequest.class)), with(any(AssertionType.class)),
                        with(any(String.class)), with(any(String.class)));
                will(returnValue(null));
                ignoring(mockContext);
            }
        });

        DocQueryAcknowledgementType expResult = null;

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, mockContext);
        assertNotNull(result);
    }

    @Test
    public void testCrossGatewayQueryRequestXGWRequestisNull() {
        System.out.println("crossGatewayQueryRequest: Cross Gateway Query request is null");

        final Log mockLogger = mockery.mock(Log.class);
        RespondingGatewayCrossGatewayQueryRequestType mockCrossGatewayQueryRequest = null;
        final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocQueryAuditLog mockDocQueryAuditLog = mockery.mock(DocQueryAuditLog.class);
        final AdhocQueryRequest mockAdhocQueryRequest = mockery.mock(AdhocQueryRequest.class);
        final NhinTargetSystemType mockTarget = mockery.mock(NhinTargetSystemType.class);

        NhincDocQueryDeferredRequestOrchImpl instance = new NhincDocQueryDeferredRequestOrchImpl(){
            protected Log getLogger(){
                return mockLogger;
            }

            protected DocQueryAuditLog getDocQueryAuditLogger(){
                return mockDocQueryAuditLog;

            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, mockContext);
        assertNotNull(result);

    }

    @Test
    public void testCrossGatewayQueryRequestWebServiceConextidNull() {
        System.out.println("crossGatewayQueryRequest: WebServiceContext is null");


        final Log mockLogger = mockery.mock(Log.class);
        final RespondingGatewayCrossGatewayQueryRequestType mockCrossGatewayQueryRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
        WebServiceContext mockContext = null;
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocQueryAuditLog mockDocQueryAuditLog = mockery.mock(DocQueryAuditLog.class);
        final AdhocQueryRequest mockAdhocQueryRequest = mockery.mock(AdhocQueryRequest.class);
        final NhinTargetSystemType mockTarget = mockery.mock(NhinTargetSystemType.class);


        NhincDocQueryDeferredRequestOrchImpl instance = new NhincDocQueryDeferredRequestOrchImpl(){
            protected Log getLogger(){
                return mockLogger;
            }

            protected DocQueryAuditLog getDocQueryAuditLogger(){
                return mockDocQueryAuditLog;

            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        DocQueryAcknowledgementType expResult = null;

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, mockContext);
        assertNotNull(result);
    }

    @Test
    public void testCrossGatewayQueryRequestAssertionisNull() {
        System.out.println("crossGatewayQueryRequest: Assertion is null");


        final Log mockLogger = mockery.mock(Log.class);
        final RespondingGatewayCrossGatewayQueryRequestType mockCrossGatewayQueryRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
        final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
        final DocQueryAuditLog mockDocQueryAuditLog = mockery.mock(DocQueryAuditLog.class);
        final AdhocQueryRequest mockAdhocQueryRequest = mockery.mock(AdhocQueryRequest.class);
        final NhinTargetSystemType mockTarget = mockery.mock(NhinTargetSystemType.class);


        NhincDocQueryDeferredRequestOrchImpl instance = new NhincDocQueryDeferredRequestOrchImpl(){
            protected Log getLogger(){
                return mockLogger;
            }

            protected DocQueryAuditLog getDocQueryAuditLogger(){
                return mockDocQueryAuditLog;

            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                one(mockCrossGatewayQueryRequest).getAssertion();
                will(returnValue(null));
            }
        });

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, mockContext);
        assertNotNull(result);
    }
}