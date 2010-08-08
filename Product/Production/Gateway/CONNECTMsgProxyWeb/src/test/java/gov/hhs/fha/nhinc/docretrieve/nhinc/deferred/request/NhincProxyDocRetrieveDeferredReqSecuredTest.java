package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import javax.xml.ws.WebServiceContext;
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
public class NhincProxyDocRetrieveDeferredReqSecuredTest {

    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredReqSecuredTest() {
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
    public void testCrossGatewayRetrieveRequestHappy() {
        final NhincProxyDocRetrieveDeferredReqOrchImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredReqOrchImpl.class);
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        NhincProxyDocRetrieveDeferredReqSecured testSubject = new NhincProxyDocRetrieveDeferredReqSecured() {

            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
                return mockAck;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class)), with(any(WebServiceContext.class)));
            }
        });
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType body = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        assertNotNull(testSubject.crossGatewayRetrieveRequest(body));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad() {
        final NhincProxyDocRetrieveDeferredReqOrchImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredReqOrchImpl.class);
        NhincProxyDocRetrieveDeferredReqSecured testSubject = new NhincProxyDocRetrieveDeferredReqSecured() {

            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body) {
                return null;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class)), with(any(WebServiceContext.class)));
            }
        });
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType body = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        assertNull(testSubject.crossGatewayRetrieveRequest(body));
        mockery.assertIsSatisfied();
    }
}
