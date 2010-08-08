package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
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
public class NhincProxyDocRetrieveDeferredRespTest {
    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredRespTest() {
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
    public void testCrossGatewayRetrieveResponseHappy()
    {
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        final NhincProxyDocRetrieveDeferredRespOrchImpl mockOrchImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespOrchImpl.class);
        NhincProxyDocRetrieveDeferredResp testSubject = new NhincProxyDocRetrieveDeferredResp(){
        @Override
        protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
            return mockAck;
        }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockOrchImpl).crossGatewayRetrieveResponse(with(any(RespondingGatewayCrossGatewayRetrieveResponseType.class)), with(any(WebServiceContext.class)));
                will(returnValue(mockAck));
            }
        });
        RespondingGatewayCrossGatewayRetrieveResponseType req = new RespondingGatewayCrossGatewayRetrieveResponseType();
        assertNotNull(testSubject.crossGatewayRetrieveResponse(req));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveResponseBad()
    {
        final NhincProxyDocRetrieveDeferredRespOrchImpl mockOrchImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespOrchImpl.class);
        NhincProxyDocRetrieveDeferredResp testSubject = new NhincProxyDocRetrieveDeferredResp(){
        @Override
        protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse) {
            return null;
        }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockOrchImpl).crossGatewayRetrieveResponse(with(any(RespondingGatewayCrossGatewayRetrieveResponseType.class)), with(any(WebServiceContext.class)));
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveResponseType req = new RespondingGatewayCrossGatewayRetrieveResponseType();
        assertNull(testSubject.crossGatewayRetrieveResponse(req));
        mockery.assertIsSatisfied();
    }
}