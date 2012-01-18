/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;



/**
 *
 * @author rhalfert
 */
public class EntityDocSubmissionDeferredResponseImplTest {

    Mockery mockery = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final EntityDocSubmissionDeferredResponseOrchImpl mockOrchImpl = mockery.mock(EntityDocSubmissionDeferredResponseOrchImpl.class);
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
    final AssertionType mockAssertion = mockery.mock(AssertionType.class);
    final RegistryResponseType mockResponse = mockery.mock(RegistryResponseType.class);
    final NhinTargetCommunitiesType mockTargetComm = mockery.mock(NhinTargetCommunitiesType.class);

    public EntityDocSubmissionDeferredResponseImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class EntityDocSubmissionDeferredResponseImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponse() {
        System.out.println("provideAndRegisterDocumentSetBResponse");
        
        
    }

    /**
     * Test of provideAndRegisterDocumentSetBAsyncResponse method, of class EntityDocSubmissionDeferredResponseImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBAsyncResponse() {
        System.out.println("provideAndRegisterDocumentSetBAsyncResponse");
        
        EntityDocSubmissionDeferredResponseImpl testSubject = new EntityDocSubmissionDeferredResponseImpl() {
              @Override
              protected EntityDocSubmissionDeferredResponseOrchImpl createEntityDocSubmissionDeferredResponseOrchImpl() {
                return mockOrchImpl;
              }

              @Override
              protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn){
                  return mockAssertion;
              }
        };

        mockery.checking(new Expectations() {
          {
              ignoring(mockContext);
              ignoring(mockAssertion);
              oneOf(mockOrchImpl).provideAndRegisterDocumentSetBAsyncResponse(with(aNonNull(RegistryResponseType.class)), with(aNonNull(AssertionType.class)), with(aNonNull(NhinTargetCommunitiesType.class)));
          }
        });

        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType mockBody = new RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType();
        mockBody.setAssertion(mockAssertion);
        mockBody.setRegistryResponse(mockResponse);
        mockBody.setNhinTargetCommunities(mockTargetComm);

        XDRAcknowledgementType response = testSubject.provideAndRegisterDocumentSetBAsyncResponse(mockBody,mockContext);
        mockery.assertIsSatisfied();
    }

    /**
     * Test of extractAssertionFromContext method, of class EntityDocSubmissionDeferredResponseImpl.
     */
    @Test
    public void testExtractAssertionFromContext() {
        System.out.println("extractAssertionFromContext");

        EntityDocSubmissionDeferredResponseImpl testSubject = new EntityDocSubmissionDeferredResponseImpl();

        mockery.checking(new Expectations() {
          {
              ignoring(mockContext);
              ignoring(mockAssertion);
          }
        });

        AssertionType response = testSubject.extractAssertionFromContext(mockContext,mockAssertion);
        assert(response != null);
        mockery.assertIsSatisfied();

    }

    /**
     * Test of createEntityDocSubmissionDeferredResponseOrchImpl method, of class EntityDocSubmissionDeferredResponseImpl.
     */
    @Test
    public void testCreateEntityDocSubmissionDeferredResponseOrchImpl() {
        System.out.println("createEntityDocSubmissionDeferredResponseOrchImpl");
        EntityDocSubmissionDeferredResponseImpl testSubject = new EntityDocSubmissionDeferredResponseImpl();

        mockery.checking(new Expectations() {
          {
              ignoring(mockContext);
              ignoring(mockAssertion);
          }
        });

        EntityDocSubmissionDeferredResponseOrchImpl response = testSubject.createEntityDocSubmissionDeferredResponseOrchImpl();
        mockery.assertIsSatisfied();
        
        assert(response != null);

    }

}