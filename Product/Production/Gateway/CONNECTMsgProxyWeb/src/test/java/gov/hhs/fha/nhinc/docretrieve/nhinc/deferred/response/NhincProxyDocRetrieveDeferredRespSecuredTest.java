package gov.hhs.fha.nhinc.docretrieve.nhinc.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.NhincProxyDocRetrieveDeferredRespOrchImpl;
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
 * @author svalluripalli
 */
public class NhincProxyDocRetrieveDeferredRespSecuredTest {

    private Mockery mockery = null;

    public NhincProxyDocRetrieveDeferredRespSecuredTest() {
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
    public void testCrossGatewayRetrieveResponseHappy() {
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        final NhincProxyDocRetrieveDeferredRespOrchImpl mockOrchImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespOrchImpl.class);
        NhincProxyDocRetrieveDeferredRespSecured testSubject = new NhincProxyDocRetrieveDeferredRespSecured() {

            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
                return mockAck;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockOrchImpl).crossGatewayRetrieveResponse(with(any(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class)), with(any(WebServiceContext.class)));
                will(returnValue(mockAck));
            }
        });
        assertNotNull(testSubject.crossGatewayRetrieveResponse(new RespondingGatewayCrossGatewayRetrieveSecuredResponseType()));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveResponseBad() {
        final NhincProxyDocRetrieveDeferredRespOrchImpl mockOrchImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespOrchImpl.class);
        NhincProxyDocRetrieveDeferredRespSecured testSubject = new NhincProxyDocRetrieveDeferredRespSecured() {

            @Override
            protected DocRetrieveAcknowledgementType sendToOrchImpl(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body) {
                return null;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockOrchImpl).crossGatewayRetrieveResponse(with(any(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class)), with(any(WebServiceContext.class)));
                will(returnValue(null));
            }
        });
        assertNull(testSubject.crossGatewayRetrieveResponse(new RespondingGatewayCrossGatewayRetrieveSecuredResponseType()));
        mockery.assertIsSatisfied();
    }
}
