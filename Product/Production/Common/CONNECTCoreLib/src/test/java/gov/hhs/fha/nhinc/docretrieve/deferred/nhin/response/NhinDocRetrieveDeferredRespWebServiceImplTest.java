package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.response;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 3:30:43 PM
 */
public class NhinDocRetrieveDeferredRespWebServiceImplTest {
    private Mockery context;

    public NhinDocRetrieveDeferredRespWebServiceImplTest() {
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
     public void testRespondingGatewayDeferredResponse_CrossGatewayRetrieve() {
//         //
//         // Define mock objects
//         //
//         final NhinDocRetrieveDeferredRespWebServiceImpl                                  mockNhinDocRetrieveDeferredRespWebServiceImpl;
//         final RespondingGatewayCrossGatewayRetrieveSecuredResponseType         mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType;
//         final AssertionType                                                    mockAssertionType;
//         final DocRetrieveAcknowledgementType                                   mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         NhinDocRetrieveDeferredRespWebServiceImpl                                    retrieveRequest;
//         RespondingGatewayCrossGatewayRetrieveSecuredResponseType           req;
//         AssertionType                                                      assertionType;
//         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
//         DocRetrieveAcknowledgementType                                     ack;
//
//         //
//         // Instantiate the mock objects.
//         //
//         mockNhinDocRetrieveDeferredRespWebServiceImpl = context.mock(NhinDocRetrieveDeferredRespWebServiceImpl.class);
//         mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class);
//         mockAssertionType = context.mock(AssertionType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockNhinDocRetrieveDeferredRespWebServiceImpl).sendToRespondingGateway(mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType,
//                                                                             mockAssertionType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         retrieveRequest = new NhinDocRetrieveDeferredRespWebServiceImpl();
//         assertionType = new AssertionType();
//         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
//         req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
//         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);
//
//         //
//         // Run the test.
//         //
//         ack = retrieveRequest.sendToRespondingGateway(req, assertionType);
//
//         //
//         // Check the results.
//         //
//         assertNotNull("Ack was null", ack);
//         context.assertIsSatisfied();
     }
}

