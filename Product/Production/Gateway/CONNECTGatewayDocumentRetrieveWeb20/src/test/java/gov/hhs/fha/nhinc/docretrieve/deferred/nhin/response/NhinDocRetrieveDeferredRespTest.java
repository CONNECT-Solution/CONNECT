package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.response;

import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.response.NhinDocRetrieveDeferredResponse;
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
         final NhinDocRetrieveDeferredResponse                                  mockNhinDocRetrieveDeferredResp;
         final RetrieveDocumentSetResponseType                              mockRetrieveDocumentSetResponseType;
         final DocRetrieveAcknowledgementType                               mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
         DocRetrieveAcknowledgementType                                     ack;

         //
         // Instantiate the mock objects.
         //
         mockNhinDocRetrieveDeferredResp = context.mock(NhinDocRetrieveDeferredResponse.class);
         mockRetrieveDocumentSetResponseType = context.mock(RetrieveDocumentSetResponseType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockNhinDocRetrieveDeferredResp).respondingGatewayDeferredResponseCrossGatewayRetrieve(mockRetrieveDocumentSetResponseType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();

         NhinDocRetrieveDeferredResponse testSubject = new NhinDocRetrieveDeferredResponse()
         {
             @Override
             public DocRetrieveAcknowledgementType respondingGatewayDeferredResponseCrossGatewayRetrieve(RetrieveDocumentSetResponseType req)
             {
                 return mockAck;
             }
         };

         //
         // Run the test.
         //
         ack = testSubject.respondingGatewayDeferredResponseCrossGatewayRetrieve(retrieveDocumentSetResponseType);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}
