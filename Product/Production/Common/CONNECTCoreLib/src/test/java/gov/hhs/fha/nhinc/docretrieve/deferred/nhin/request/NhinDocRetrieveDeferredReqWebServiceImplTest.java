package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.request;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 3:21:08 PM
 */
public class NhinDocRetrieveDeferredReqWebServiceImplTest {
    private Mockery context;

    public NhinDocRetrieveDeferredReqWebServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
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
     public void testRespondingGatewayDeferredRequest_CrossGatewayRetrieve() {
         //
         // Define mock objects
         //
//         final NhinDocRetrieveDeferredReqWebServiceImpl mockNhinDocRetrieveDeferredReqWebServiceImpl;
//         final RespondingGatewayCrossGatewayRetrieveSecuredRequestType mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
//         final AssertionType mockAssertionType;
//         final DocRetrieveAcknowledgementType mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         NhinDocRetrieveDeferredReqWebServiceImpl retrieveRequest;
//         RespondingGatewayCrossGatewayRetrieveSecuredRequestType            req;
//         RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
//         DocRetrieveAcknowledgementType                                     ack;
//         AssertionType                                                      assertion;
//         //
//         // Instantiate the mock objects.
//         //
//         mockNhinDocRetrieveDeferredReqWebServiceImpl = context.mock(NhinDocRetrieveDeferredReqWebServiceImpl.class);
//         mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class);
//         mockAssertionType = context.mock(AssertionType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockNhinDocRetrieveDeferredReqWebServiceImpl).sendToRespondingGateway(mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType,
//                                                                            mockAssertionType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         retrieveRequest = new NhinDocRetrieveDeferredReqWebServiceImpl();
//         retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
//         req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
//         req.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);
//         assertion = new AssertionType();
//
//         //
//         // Run the test.
//         //
//         ack = retrieveRequest.sendToRespondingGateway(req, assertion);
//
//         //
//         // Check the results.
//         //
//         assertNotNull("Ack was null", ack);
//         context.assertIsSatisfied();
     }
}
