package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;


/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 4:55:56 PM
 */
public class AdapterDocRetrieveDeferredReqWebServiceImplTest {
    private Mockery context;

    public AdapterDocRetrieveDeferredReqWebServiceImplTest() {
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
     public void testSendToAdapter() {
         //
         // Define mock objects
         //
//         final AdapterDocRetrieveDeferredReqWebServiceImpl                  mockAdapterDocRetrieveDeferredReqWebServiceImpl;
//         final RespondingGatewayCrossGatewayRetrieveSecuredRequestType      mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
//         final AssertionType mockAssertionType;
//         final DocRetrieveAcknowledgementType mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         AdapterDocRetrieveDeferredReqWebServiceImpl                        docRetrieve;
//         RespondingGatewayCrossGatewayRetrieveSecuredRequestType            req;
//         AssertionType                                                      assertionType;
//         RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
//         DocRetrieveAcknowledgementType                                     ack;
//
//         //
//         // Instantiate the mock objects.
//         //
//         mockAdapterDocRetrieveDeferredReqWebServiceImpl = context.mock(AdapterDocRetrieveDeferredReqWebServiceImpl.class);
//         mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class);
//         mockAssertionType = context.mock(AssertionType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockAdapterDocRetrieveDeferredReqWebServiceImpl).sendToAdapter(mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType,
//                                                                             mockAssertionType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         docRetrieve = new AdapterDocRetrieveDeferredReqWebServiceImpl();
//         assertionType = new AssertionType();
//         retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
//         req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
//         req.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);
//
//         //
//         // Run the test.
//         //
//         ack = docRetrieve.sendToAdapter(req, assertionType);
//
//         //
//         // Check the results.
//         //
//         assertNotNull("Ack was null", ack);
//         context.assertIsSatisfied();
     }
}

