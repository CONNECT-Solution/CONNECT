/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class ExtractEntityDocRetrieveDeferredRequestValuesTest {
    private ExtractEntityDocRetrieveDeferredRequestValues testSubject = null;
    private static Mockery mockery = null;
    
    public ExtractEntityDocRetrieveDeferredRequestValuesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testExtractRetrieveDocumentSetRequestTypeHappy()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setRetrieveDocumentSetRequest(new RetrieveDocumentSetRequestType());
        assertNotNull(testSubject.extractRetrieveDocumentSetRequestType(request));
    }

    @Test
    public void testExtractRetrieveDocumentSetRequestTypeBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractRetrieveDocumentSetRequestType(null));
    }

    @Test
    public void testExtractAssertionHappy()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setAssertion(new AssertionType());
        assertNotNull(testSubject.extractAssertion(request));
    }

    @Test
    public void testExtractAssertionBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractAssertion(null));
    }

    @Test
    public void testExtractNhinTargetCommunitiesHappy()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setNhinTargetCommunities(new NhinTargetCommunitiesType());
        assertNotNull(testSubject.extractNhinTargetCommunities(request));
    }

    @Test
    public void testExtractNhinTargetCommunitiesBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractNhinTargetCommunities(null));
    }

    @Test
    public void testExtractRetrieveDocumentSetSecuredRequestTypeHappy()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        request.setRetrieveDocumentSetRequest(new RetrieveDocumentSetRequestType());
        assertNotNull(testSubject.extractRetrieveDocumentSetSecuredRequestType(request));
    }

    @Test
    public void testExtractRetrieveDocumentSetSecuredRequestTypeBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractRetrieveDocumentSetSecuredRequestType(null));
    }
    
    @Test
    public void testExtractSecuredNhinTargetCommunitiesHappy()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        request.setNhinTargetCommunities(new NhinTargetCommunitiesType());
        assertNotNull(testSubject.extractSecuredNhinTargetCommunities(request));
    }

    @Test
    public void testExtractSecuredNhinTargetCommunitiesBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractSecuredNhinTargetCommunities(null));
    }

    @Ignore
    public void testExtractSecuredAssertionHappy()
    {
        final AssertionType mockAssertionType = mockery.mock(AssertionType.class);
        final WebServiceContext mockWebServiceContext = mockery.mock(WebServiceContext.class);
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues()
        {
            @Override
            protected AssertionType getSamlTokenExtractor(WebServiceContext context)
            {
                return mockAssertionType;
            }
        };

        assertNotNull(testSubject.extractSecuredAssertion(mockWebServiceContext));
    }

    @Test
    public void testExtractSecuredAssertionBad()
    {
        testSubject = new ExtractEntityDocRetrieveDeferredRequestValues();
        assertNull(testSubject.extractSecuredAssertion(null));
        
    }
}