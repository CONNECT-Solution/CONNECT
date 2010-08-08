package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
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
public class NhincProxyDocRetrieveDeferredReqTest {
    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredReqTest() {
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
    public void testCrossGatewayRetrieveRequestHappy()
    {
        final NhincProxyDocRetrieveDeferredReqOrchImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredReqOrchImpl.class);
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        NhincProxyDocRetrieveDeferredReq testSubject = new NhincProxyDocRetrieveDeferredReq()
        {
            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return mockAck;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)), with(any(WebServiceContext.class)));
                will(returnValue(mockAck));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNotNull(testSubject.crossGatewayRetrieveRequest(crossGatewayRetrieveRequest));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad()
    {
        final NhincProxyDocRetrieveDeferredReqOrchImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredReqOrchImpl.class);
        NhincProxyDocRetrieveDeferredReq testSubject = new NhincProxyDocRetrieveDeferredReq()
        {
            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest) {
                return null;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RespondingGatewayCrossGatewayRetrieveRequestType.class)), with(any(WebServiceContext.class)));
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveRequestType crossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        assertNull(testSubject.crossGatewayRetrieveRequest(crossGatewayRetrieveRequest));
        mockery.assertIsSatisfied();
    }

}