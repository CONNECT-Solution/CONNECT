package gov.hhs.fha.nhinc.docretrievedeferred.nhinc.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
        final NhincProxyDocRetrieveDeferredRespImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespImpl.class);
        RespondingGatewayCrossGatewayRetrieveResponseType retrieveResponse = new RespondingGatewayCrossGatewayRetrieveResponseType();
        NhincProxyDocRetrieveDeferredResp testSubject = new NhincProxyDocRetrieveDeferredResp()
        {
            @Override
            protected DocRetrieveAcknowledgementType sendToNhincProxyImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
                return mockAck;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveResponse(with(any(RetrieveDocumentSetResponseType.class)), with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)));
                will(returnValue(mockAck));
            }
        });
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        retrieveResponse.setAssertion(assertion);
        retrieveResponse.setNhinTargetSystem(nhinTargetSystem);
        retrieveResponse.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        assertNotNull(testSubject.crossGatewayRetrieveResponse(retrieveResponse));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveResponseHappySomethingWentBad()
    {
        final NhincProxyDocRetrieveDeferredRespImpl mockImpl = mockery.mock(NhincProxyDocRetrieveDeferredRespImpl.class);
        RespondingGatewayCrossGatewayRetrieveResponseType retrieveResponse = new RespondingGatewayCrossGatewayRetrieveResponseType();
        NhincProxyDocRetrieveDeferredResp testSubject = new NhincProxyDocRetrieveDeferredResp()
        {
            @Override
            protected DocRetrieveAcknowledgementType sendToNhincProxyImpl(RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
                return null;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveResponse(with(any(RetrieveDocumentSetResponseType.class)), with(any(AssertionType.class)), with(any(NhinTargetSystemType.class)));
                will(returnValue(null));
            }
        });
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        AssertionType assertion = new AssertionType();
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        retrieveResponse.setAssertion(assertion);
        retrieveResponse.setNhinTargetSystem(nhinTargetSystem);
        retrieveResponse.setRetrieveDocumentSetResponse(retrieveDocumentSetResponse);
        assertNull(testSubject.crossGatewayRetrieveResponse(retrieveResponse));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveResponseBad()
    {
        NhincProxyDocRetrieveDeferredResp testSubject = new NhincProxyDocRetrieveDeferredResp();
        assertNull(testSubject.crossGatewayRetrieveResponse(null));
    }
}