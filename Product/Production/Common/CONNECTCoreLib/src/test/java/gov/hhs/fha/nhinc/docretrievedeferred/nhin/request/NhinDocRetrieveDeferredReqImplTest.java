package gov.hhs.fha.nhinc.docretrievedeferred.nhin.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.NhinDocRetrieveDeferredReqImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.jmock.Expectations;
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
public class NhinDocRetrieveDeferredReqImplTest {
    private Mockery context;

    public NhinDocRetrieveDeferredReqImplTest() {
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
         final NhinDocRetrieveDeferredReqImpl mockNhinDocRetrieveDeferredReqImpl;
         final RespondingGatewayCrossGatewayRetrieveSecuredRequestType mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
         final AssertionType mockAssertionType;
         final DocRetrieveAcknowledgementType mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         NhinDocRetrieveDeferredReqImpl                                     retrieveRequest;
         RespondingGatewayCrossGatewayRetrieveSecuredRequestType            req;
         RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
         DocRetrieveAcknowledgementType                                     ack;
         AssertionType                                                      assertion;
         //
         // Instantiate the mock objects.
         //
         mockNhinDocRetrieveDeferredReqImpl = context.mock(NhinDocRetrieveDeferredReqImpl.class);
         mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context.mock(RespondingGatewayCrossGatewayRetrieveSecuredRequestType.class);
         mockAssertionType = context.mock(AssertionType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockNhinDocRetrieveDeferredReqImpl).sendToAdapter(mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType,
                                                                            mockAssertionType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         retrieveRequest = new NhinDocRetrieveDeferredReqImpl();
         retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
         req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
         req.setRetrieveDocumentSetRequest(retrieveDocumentSetRequestType);
         assertion = new AssertionType();

         //
         // Run the test.
         //
         ack = retrieveRequest.sendToAdapter(req, assertion);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}
