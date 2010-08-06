package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sai Valluripalli
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
    public void testCrossGatewayRetrieveResponseBad()
    {
        NhincProxyDocRetrieveDeferredRespSecured testSubject = new NhincProxyDocRetrieveDeferredRespSecured();
        assertNull(testSubject.crossGatewayRetrieveResponse(null));
    }

    @Ignore
    public void testCrossGatewayRetrieveResponseHappy()
    {
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        final NhincProxyDocRetrieveDeferredRespImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespImpl.class);
        final RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        final NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        NhincProxyDocRetrieveDeferredRespSecured testSubject = new NhincProxyDocRetrieveDeferredRespSecured()
        {
            @Override
            protected AssertionType extractAssertionInfo() {
                return mockAssertion;
            }

            @Override
            protected DocRetrieveAcknowledgementType sendToNhincProxyImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
                return mockAck;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveResponse(retrieveDocumentSetResponse, mockAssertion, nhinTargetSystem);
                will(returnValue(mockAck));
            }
        });
        RespondingGatewayCrossGatewayRetrieveSecuredResponseType body = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
        body.setNhinTargetSystem(nhinTargetSystem);
        body.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        assertNotNull(testSubject.crossGatewayRetrieveResponse(body));
        mockery.assertIsSatisfied();
    }

    @Ignore
    public void testCrossGatewayRetrieveResponseHappySomethingWentBad()
    {
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final NhincProxyDocRetrieveDeferredRespImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespImpl.class);
        final RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        final NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();

        NhincProxyDocRetrieveDeferredRespSecured testSubject = new NhincProxyDocRetrieveDeferredRespSecured()
        {
            @Override
            protected AssertionType extractAssertionInfo() {
                return mockAssertion;
            }

            @Override
            protected DocRetrieveAcknowledgementType sendToNhincProxyImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
                return null;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveResponse(retrieveDocumentSetResponse, mockAssertion, nhinTargetSystem);
                will(returnValue(null));
            }
        });
        RespondingGatewayCrossGatewayRetrieveSecuredResponseType body = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
        body.setNhinTargetSystem(nhinTargetSystem);
        body.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        assertNull(testSubject.crossGatewayRetrieveResponse(body));
        mockery.assertIsSatisfied();
    }

}
