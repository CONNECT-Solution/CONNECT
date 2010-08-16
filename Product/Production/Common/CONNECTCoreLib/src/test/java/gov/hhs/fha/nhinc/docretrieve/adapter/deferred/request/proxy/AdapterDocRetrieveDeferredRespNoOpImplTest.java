package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response.proxy.AdapterDocRetrieveDeferredRespProxyNoOpImpl;
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
         final AdapterDocRetrieveDeferredRespProxyNoOpImpl                        mockAdapterDocRetrieveDeferredRespNoOpImpl;
         final RetrieveDocumentSetResponseType     mockRetrieveDocumentSetResponseType;
         final AssertionType                                                mockAssertionType;
         final DocRetrieveAcknowledgementType                               mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         AdapterDocRetrieveDeferredRespProxyNoOpImpl                              docRetrieve;
         RespondingGatewayCrossGatewayRetrieveSecuredResponseType           req;
         AssertionType                                                      assertionType;
         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
         DocRetrieveAcknowledgementType                                     ack;

         //
         // Instantiate the mock objects.
         //
         mockAdapterDocRetrieveDeferredRespNoOpImpl = context.mock(AdapterDocRetrieveDeferredRespProxyNoOpImpl.class);
         mockRetrieveDocumentSetResponseType = context.mock(RetrieveDocumentSetResponseType.class);
         mockAssertionType = context.mock(AssertionType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockAdapterDocRetrieveDeferredRespNoOpImpl).sendToAdapter(mockRetrieveDocumentSetResponseType,
                                                                             mockAssertionType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         docRetrieve = new AdapterDocRetrieveDeferredRespProxyNoOpImpl();
         assertionType = new AssertionType();
         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
         req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);

         //
         // Run the test.
         //
         ack = docRetrieve.sendToAdapter(retrieveDocumentSetResponseType, assertionType);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}

