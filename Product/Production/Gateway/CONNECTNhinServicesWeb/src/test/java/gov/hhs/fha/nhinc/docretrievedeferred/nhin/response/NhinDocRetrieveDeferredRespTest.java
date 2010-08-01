package gov.hhs.fha.nhinc.docretrievedeferred.nhin.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.request.NhinDocRetrieveDeferredReq;
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
 * Time: 2:45:00 PM
 */
public class NhinDocRetrieveDeferredRespTest {
    private Mockery context;

    public NhinDocRetrieveDeferredRespTest() {
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
         //
         // Define mock objects
         //
         final NhinDocRetrieveDeferredResp                                  mockNhinDocRetrieveDeferredResp;
         final RespondingGatewayCrossGatewayRetrieveSecuredResponseType     mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType;
         final DocRetrieveAcknowledgementType                               mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         NhinDocRetrieveDeferredResp                                        retrieveRequest;
         RespondingGatewayCrossGatewayRetrieveSecuredResponseType           req;
         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
         DocRetrieveAcknowledgementType                                     ack;

         //
         // Instantiate the mock objects.
         //
         mockNhinDocRetrieveDeferredResp = context.mock(NhinDocRetrieveDeferredResp.class);
         mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredResponseType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockNhinDocRetrieveDeferredResp).respondingGatewayDeferredRequest_CrossGatewayRetrieve(mockRespondingGatewayCrossGatewayRetrieveSecuredResponseType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
         req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);

         NhinDocRetrieveDeferredResp testSubject = new NhinDocRetrieveDeferredResp()
         {
             @Override
             protected DocRetrieveAcknowledgementType getResponse(RespondingGatewayCrossGatewayRetrieveSecuredResponseType req)
             {
                 return mockAck;
             }
         };

         //
         // Run the test.
         //
         ack = testSubject.respondingGatewayDeferredRequest_CrossGatewayRetrieve(req);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}
