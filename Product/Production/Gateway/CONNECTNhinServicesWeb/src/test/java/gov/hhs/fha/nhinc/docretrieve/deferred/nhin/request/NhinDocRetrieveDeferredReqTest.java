package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.request;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 2:02:44 PM
 */
public class NhinDocRetrieveDeferredReqTest {
    private Mockery context;

    public NhinDocRetrieveDeferredReqTest() {
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
//         //
//         // Define mock objects
//         //
//         final NhinDocRetrieveDeferredReq                                   mockNhinDocRetrieveDeferredReq;
//         final RespondingGatewayCrossGatewayRetrieveSecuredRequestType      mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
//         final DocRetrieveAcknowledgementType                               mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         NhinDocRetrieveDeferredReq                                         retrieveRequest;
//         RespondingGatewayCrossGatewayRetrieveSecuredRequestType            req;
//         RetrieveDocumentSetRequestType                                     retrieveDocumentSetRequestType;
//         DocRetrieveAcknowledgementType                                     ack;
//
//
//         //
//         // Instantiate the mock objects.
//         //
//         mockNhinDocRetrieveDeferredReq = context.mock(NhinDocRetrieveDeferredReq.class);
//         mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockNhinDocRetrieveDeferredReq).respondingGatewayDeferredRequestCrossGatewayRetrieve(mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
//         req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
//         req.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);
//
//         NhinDocRetrieveDeferredReq testSubject = new NhinDocRetrieveDeferredReq()
//         {
//             @Override
//             protected DocRetrieveAcknowledgementType getResponse(RespondingGatewayCrossGatewayRetrieveSecuredRequestType req)
//             {
//                 return mockAck;
//             }
//         };
//
//         //
//         // Run the test.
//         //
//         ack = testSubject.respondingGatewayDeferredRequestCrossGatewayRetrieve(req);
//
//         //
//         // Check the results.
//         //
//         assertNotNull("Ack was null", ack);
//         context.assertIsSatisfied();
     }
}
