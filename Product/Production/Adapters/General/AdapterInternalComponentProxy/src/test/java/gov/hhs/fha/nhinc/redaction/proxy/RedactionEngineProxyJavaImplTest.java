package gov.hhs.fha.nhinc.redaction.proxy;

import gov.hhs.fha.nhinc.redaction.RedactionEngine;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class RedactionEngineProxyJavaImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testGetRedactionEngine()
    {
        try
        {
            RedactionEngineProxyJavaImpl javaProxy = new RedactionEngineProxyJavaImpl();
            RedactionEngine redactionEngine = javaProxy.getRedactionEngine();
            assertNotNull("RedactionEngine was null", redactionEngine);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngine test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngine test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsHappy()
    {
        try
        {
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);
            final RedactionEngine mockRedactionEngine = context.mock(RedactionEngine.class);

            RedactionEngineProxyJavaImpl javaProxy = new RedactionEngineProxyJavaImpl()
            {
                @Override
                protected RedactionEngine getRedactionEngine()
                {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionEngine).filterAdhocQueryResults(with(aNonNull(AdhocQueryRequest.class)), with(aNonNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = javaProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResults test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsNullInputs()
    {
        try
        {
            final AdhocQueryRequest mockAdhocQueryRequest = null;
            final AdhocQueryResponse mockAdhocQueryResponse = null;
            final RedactionEngine mockRedactionEngine = context.mock(RedactionEngine.class);

            RedactionEngineProxyJavaImpl javaProxy = new RedactionEngineProxyJavaImpl()
            {
                @Override
                protected RedactionEngine getRedactionEngine()
                {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionEngine).filterAdhocQueryResults(with(aNull(AdhocQueryRequest.class)), with(aNull(AdhocQueryResponse.class)));
                }
            });

            AdhocQueryResponse response = javaProxy.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResults test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsHappy()
    {
        try
        {
            final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
            final RetrieveDocumentSetResponseType mockResponse = context.mock(RetrieveDocumentSetResponseType.class);
            final RedactionEngine mockRedactionEngine = context.mock(RedactionEngine.class);
            String homeCommunityId = "1.1";

            RedactionEngineProxyJavaImpl javaProxy = new RedactionEngineProxyJavaImpl()
            {
                @Override
                protected RedactionEngine getRedactionEngine()
                {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionEngine).filterRetrieveDocumentSetResults(with(aNonNull(String.class)), with(aNonNull(RetrieveDocumentSetRequestType.class)), with(aNonNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = javaProxy.filterRetrieveDocumentSetResults(homeCommunityId, mockRequest, mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResultsNullInputs()
    {
        try
        {
            final RetrieveDocumentSetRequestType mockRequest = null;
            final RetrieveDocumentSetResponseType mockResponse = null;
            final RedactionEngine mockRedactionEngine = context.mock(RedactionEngine.class);
            String homeCommunityId = null;

            RedactionEngineProxyJavaImpl javaProxy = new RedactionEngineProxyJavaImpl()
            {
                @Override
                protected RedactionEngine getRedactionEngine()
                {
                    return mockRedactionEngine;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionEngine).filterRetrieveDocumentSetResults(with(aNull(String.class)), with(aNull(RetrieveDocumentSetRequestType.class)), with(aNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = javaProxy.filterRetrieveDocumentSetResults(homeCommunityId, mockRequest, mockResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
        }
    }

}