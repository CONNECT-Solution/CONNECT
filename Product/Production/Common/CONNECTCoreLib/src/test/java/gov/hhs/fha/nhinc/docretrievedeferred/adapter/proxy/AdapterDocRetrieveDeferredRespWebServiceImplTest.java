package gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveResponseType;
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
         //
         // Define mock objects
         //
         final AdapterDocRetrieveDeferredRespWebServiceImpl         mockAdapterDocRetrieveDeferredRespWebServiceImpl;
         final RespondingGatewayCrossGatewayRetrieveResponseType    mockRespondingGatewayCrossGatewayRetrieveResponseType;
         final AssertionType                                        mockAssertionType;
         final DocRetrieveAcknowledgementType                       mockAck;

         //
         // Define the class to be tested and it's inputs and outputs.
         //
         AdapterDocRetrieveDeferredRespWebServiceImpl                       docRetrieve;
         RespondingGatewayCrossGatewayRetrieveResponseType                  req;
         AssertionType                                                      assertionType;
         RetrieveDocumentSetResponseType                                    retrieveDocumentSetResponseType;
         DocRetrieveAcknowledgementType                                     ack;

         //
         // Instantiate the mock objects.
         //
         mockAdapterDocRetrieveDeferredRespWebServiceImpl = context.mock(AdapterDocRetrieveDeferredRespWebServiceImpl.class);
         mockRespondingGatewayCrossGatewayRetrieveResponseType = context.mock(RespondingGatewayCrossGatewayRetrieveResponseType.class);
         mockAssertionType = context.mock(AssertionType.class);
         mockAck = context.mock(DocRetrieveAcknowledgementType.class);

         //
         // Set up the expectations using the instantiated mock objects.
         //
         context.checking(new Expectations() {
             {
                 allowing(mockAdapterDocRetrieveDeferredRespWebServiceImpl).receiveFromAdapter(mockRespondingGatewayCrossGatewayRetrieveResponseType,
                                                                             mockAssertionType);
                 will(returnValue(mockAck));
             }
         });

         //
         // Instantiate the object to be tested and it's inputs.
         //
         docRetrieve = new AdapterDocRetrieveDeferredRespWebServiceImpl();
         assertionType = new AssertionType();
         retrieveDocumentSetResponseType = new RetrieveDocumentSetResponseType();
         req = new RespondingGatewayCrossGatewayRetrieveResponseType();
         req.setRetrieveDocumentSetResponse(retrieveDocumentSetResponseType);

         //
         // Run the test.
         //
         ack = docRetrieve.receiveFromAdapter(req, assertionType);

         //
         // Check the results.
         //
         assertNotNull("Ack was null", ack);
         context.assertIsSatisfied();
     }
}

