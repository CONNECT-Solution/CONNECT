package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 4:46:34 PM
 */
public class AdapterDocRetrieveDeferredRespWebServiceImplTest {
    private Mockery context;

    public AdapterDocRetrieveDeferredRespWebServiceImplTest() {
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
     public void testReceiveFromAdapter() {
//         //
//         // Define mock objects
//         //
//         final AdapterDocRetrieveDeferredRespWebServiceImpl         mockAdapterDocRetrieveDeferredRespWebServiceImpl;
//         final RespondingGatewayCrossGatewayRetrieveSecuredResponseType mSecuredockRespondingGatewayCrossGatewayRetrieveResponseType;
//         final AssertionType                                        mockAssertionType;
//         final DocRetrieveAcknowledgementType                       mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         AdapterDocRetrieveDeferredRespWebServiceImpl                       docRetrieve;
//         RespondingGatewayCrossGatewayRetrieveSecuredResponseType                  req;
//         AssertionType                                                      assertionType;
//         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
//         DocRetrieveAcknowledgementType                                     ack;
//
//         //
//         // Instantiate the mock objects.
//         //
//         mockAdapterDocRetrieveDeferredRespWebServiceImpl = context.mock(AdapterDocRetrieveDeferredRespWebServiceImpl.class);
//         mSecuredockRespondingGatewayCrossGatewayRetrieveResponseType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class);
//         mockAssertionType = context.mock(AssertionType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockAdapterDocRetrieveDeferredRespWebServiceImpl).sendToAdapter(mSecuredockRespondingGatewayCrossGatewayRetrieveResponseType,
//                                                                             mockAssertionType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         docRetrieve = new AdapterDocRetrieveDeferredRespWebServiceImpl();
//         assertionType = new AssertionType();
//         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
//         req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
//         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);
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

