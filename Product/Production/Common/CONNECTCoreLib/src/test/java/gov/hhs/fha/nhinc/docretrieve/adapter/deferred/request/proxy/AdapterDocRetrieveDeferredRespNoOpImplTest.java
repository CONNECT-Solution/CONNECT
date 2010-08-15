package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.response.AdapterDocRetrieveDeferredRespNoOpImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by
 * User: ralph
 * Date: Jul 29, 2010
 * Time: 4:00:08 PM
 */
public class AdapterDocRetrieveDeferredRespNoOpImplTest {
    private Mockery context;

    public AdapterDocRetrieveDeferredRespNoOpImplTest() {
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
         //
         // Define mock objects
         //
         final AdapterDocRetrieveDeferredRespNoOpImpl                        mockAdapterDocRetrieveDeferredRespNoOpImpl;
         final RespondingGatewayCrossGatewayRetrieveSecuredResponseType     mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType;
         final AssertionType                                                mockAssertionType;
         final DocRetrieveAcknowledgementType                               mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         AdapterDocRetrieveDeferredRespNoOpImpl                              docRetrieve;
         RespondingGatewayCrossGatewayRetrieveSecuredResponseType           req;
         AssertionType                                                      assertionType;
         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
         DocRetrieveAcknowledgementType                                     ack;

         //
         // Instantiate the mock objects.
         //
         mockAdapterDocRetrieveDeferredRespNoOpImpl = context.mock(AdapterDocRetrieveDeferredRespNoOpImpl.class);
         mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class);
         mockAssertionType = context.mock(AssertionType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockAdapterDocRetrieveDeferredRespNoOpImpl).sendToAdapter(mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType,
                                                                             mockAssertionType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         docRetrieve = new AdapterDocRetrieveDeferredRespNoOpImpl();
         assertionType = new AssertionType();
         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
         req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);

         //
         // Run the test.
         //
         ack = docRetrieve.sendToAdapter(req, assertionType);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}

