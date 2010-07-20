package gov.hhs.fha.nhinc.adaptercomponentredaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
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
public class AdapterComponentRedactionEngineTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterComponentRedactionEngineImpl mockRedactionImpl = context.mock(AdapterComponentRedactionEngineImpl.class);

    @Test
    public void testFilterDocQueryResultsHappy()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return mockRedactionImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionImpl).filterDocQueryResults(with(aNonNull(FilterDocQueryResultsRequestType.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullInput()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return mockRedactionImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionImpl).filterDocQueryResults(with(aNull(FilterDocQueryResultsRequestType.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = null;
            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullRedactionImpl()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return null;
                }
            };
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest);
            assertNull("FilterDocQueryResultsResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsHappy()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return mockRedactionImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionImpl).filterDocRetrieveResults(with(aNonNull(FilterDocRetrieveResultsRequestType.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullInput()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return mockRedactionImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockRedactionImpl).filterDocRetrieveResults(with(aNull(FilterDocRetrieveResultsRequestType.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = null;
            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullRedactionImpl()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return null;
                }
            };
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest);
            assertNull("FilterDocRetrieveResultsResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterComponentRedactionEngineImpl()
    {
        try
        {
            AdapterComponentRedactionEngine redactionEngine = new AdapterComponentRedactionEngine()
            {
                @Override
                protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
                {
                    return mockRedactionImpl;
                }
            };
            AdapterComponentRedactionEngineImpl redactionImpl = redactionEngine.getAdapterComponentRedactionEngineImpl();
            assertNotNull("AdapterComponentRedactionEngineImpl was null", redactionImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterComponentRedactionEngineImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentRedactionEngineImpl: " + t.getMessage());
        }
    }

}