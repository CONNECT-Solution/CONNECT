package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
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
public class AdapterComponentRedactionEngineImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final WebServiceContext mockContext = context.mock(WebServiceContext.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertNotNull("Logger was null", redactionEngine.createLogger());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResults: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResults: " + t.getMessage());
        }
    }

    
    @Test
    public void testFilterDocQueryResultsHappy()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
  
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResultsHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsHappy: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullInput()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = null;
            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNull("FilterDocQueryResultsResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResultsNullInput: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullInput: " + t.getMessage());
        }
    }

    

    @Test
    public void testFilterDocQueryResultsNullResponse()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery (FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return null;
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNull("AdhocQueryResponse was not null", response.getAdhocQueryResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResultsNullResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullQueryRequest()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery (FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return new AdhocQueryResponse();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = null;
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResultsNullQueryRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullQueryRequest: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocQueryResultsNullQueryResponse()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected AdhocQueryResponse invokeRedactionEngineForQuery (FilterDocQueryResultsRequestType filterDocQueryResultsRequest) {
                    return new AdhocQueryResponse();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocQueryResultsRequestType filterDocQueryResultsRequest = new FilterDocQueryResultsRequestType();
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryResponse adhocQueryResponse = null;
            filterDocQueryResultsRequest.setAdhocQueryRequest(adhocQueryRequest);
            filterDocQueryResultsRequest.setAdhocQueryResponse(adhocQueryResponse);

            FilterDocQueryResultsResponseType response = redactionEngine.filterDocQueryResults(filterDocQueryResultsRequest, mockContext);
            assertNotNull("FilterDocQueryResultsResponseType was null", response);
            assertNotNull("AdhocQueryResponse was null", response.getAdhocQueryResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocQueryResultsNullQueryResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocQueryResultsNullQueryResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsHappy()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocRetrieveResultsHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsHappy: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullInput()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn(with(any(String.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = null;

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNull("FilterDocRetrieveResultsResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocRetrieveResultsNullInput: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullInput: " + t.getMessage());
        }
    }

    
    @Test
    public void testFilterDocRetrieveResultsNullResponse()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return null;
                }
 
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNull("RetrieveDocumentSetResponseType was not null", response.getRetrieveDocumentSetResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocRetrieveResultsNullResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullResponse: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullRetrieveRequest()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = null;
            RetrieveDocumentSetResponseType retreiveDocResponse = new RetrieveDocumentSetResponseType();
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocRetrieveResultsNullRetrieveRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullRetrieveRequest: " + t.getMessage());
        }
    }

    @Test
    public void testFilterDocRetrieveResultsNullRetrieveResponse()
    {
        try
        {
            AdapterComponentRedactionEngineImpl redactionEngine = new AdapterComponentRedactionEngineImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AssertionType getAssertion(WebServiceContext context) {
                    return new AssertionType();
                }
                @Override
                protected RetrieveDocumentSetResponseType invokeRedactionEngineForRetrieve (FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest) {
                    return new RetrieveDocumentSetResponseType();
                }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest = new FilterDocRetrieveResultsRequestType();
            RetrieveDocumentSetRequestType retrieveDocRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retreiveDocResponse = null;
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetRequest(retrieveDocRequest);
            filterDocRetrieveResultsRequest.setRetrieveDocumentSetResponse(retreiveDocResponse);

            FilterDocRetrieveResultsResponseType response = redactionEngine.filterDocRetrieveResults(filterDocRetrieveResultsRequest, mockContext);
            assertNotNull("FilterDocRetrieveResultsResponseType was null", response);
            assertNotNull("RetrieveDocumentSetResponseType was null", response.getRetrieveDocumentSetResponse());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterDocRetrieveResultsNullRetrieveResponse: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterDocRetrieveResultsNullRetrieveResponse: " + t.getMessage());
        }
    }

}