package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;


/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 4:28:25 PM
 */
public class AdapterDocRetrieveDeferredReqSecuredWebServiceImplTest {
    private Mockery context;

    public AdapterDocRetrieveDeferredReqSecuredWebServiceImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        System.setProperty("wsdl.path", "../../Gateway/CONNECTGatewayWeb/src/main/resources/xml-resources/web-services/AdapterDocRetrieveDeferredReqSecured/wsdl/");

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
//         final AdapterDocRetrieveDeferredReqSecuredWebServiceImpl           mockAdapterDocRetrieveDeferredReqSecuredWebServiceImpl;
//         final RespondingGatewayCrossGatewayRetrieveSecuredRequestType      mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
//         final AssertionType                                                mockAssertionType;
//         final DocRetrieveAcknowledgementType                               mockAck;
//
//         //
//         // Define the class to be tested and it's inputs and outputs.
//         //
//         AdapterDocRetrieveDeferredReqSecuredWebServiceImpl                 docRetrieve;
//         RespondingGatewayCrossGatewayRetrieveSecuredRequestType            req;
//         AssertionType                                                      assertionType;
//         RetrieveDocumentSetRequestType                                     retrieveDocumentSetRequestType;
//         DocRetrieveAcknowledgementType                                     ack;
//
//         //
//         // Instantiate the mock objects.
//         //
//         mockAdapterDocRetrieveDeferredReqSecuredWebServiceImpl = context.mock(AdapterDocRetrieveDeferredReqSecuredWebServiceImpl.class);
//         mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class);
//         mockAssertionType = context.mock(AssertionType.class);
//         mockAck = context.mock(DocRetrieveAcknowledgementType.class);
//
//         //
//         // Set up the expectations using the instantiated mock objects.
//         //
//         context.checking(new Expectations() {
//             {
//                 allowing(mockAdapterDocRetrieveDeferredReqSecuredWebServiceImpl).sendToAdapter(mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType,
//                                                                             mockAssertionType);
//                 will(returnValue(mockAck));
//             }
//         });
//
//         //
//         // Instantiate the object to be tested and it's inputs.
//         //
//         docRetrieve = new AdapterDocRetrieveDeferredReqSecuredWebServiceImpl();
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

