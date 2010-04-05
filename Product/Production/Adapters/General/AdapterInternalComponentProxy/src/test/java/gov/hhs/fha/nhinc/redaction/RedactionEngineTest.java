package gov.hhs.fha.nhinc.redaction;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
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
public class RedactionEngineTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);

    @Test
    public void testGetLogger()
    {
        try
        {
            RedactionEngine redactionEngine = new RedactionEngine()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log log = redactionEngine.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterAdhocQueryResultsHappy()
    {
        try
        {
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);

            RedactionEngine redactionEngine = new RedactionEngine()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
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

            RedactionEngine redactionEngine = new RedactionEngine()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            AdhocQueryResponse response = redactionEngine.filterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse);
            assertNull("AdhocQueryResponse should be null", response);
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
            String homeCommunityId = "1.1";

            RedactionEngine redactionEngine = new RedactionEngine()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(homeCommunityId, mockRequest, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
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
            String homeCommunityId = "1.1";

            RedactionEngine redactionEngine = new RedactionEngine()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            RetrieveDocumentSetResponseType response = redactionEngine.filterRetrieveDocumentSetResults(homeCommunityId, mockRequest, mockResponse);
            assertNull("RetrieveDocumentSetResponseType should be null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
        }
    }

}