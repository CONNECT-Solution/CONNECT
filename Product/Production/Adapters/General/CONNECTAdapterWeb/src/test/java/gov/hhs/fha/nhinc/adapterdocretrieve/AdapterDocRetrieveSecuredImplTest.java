package gov.hhs.fha.nhinc.adapterdocretrieve;

import gov.hhs.fha.nhinc.redaction.proxy.RedactionEngineProxy;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class AdapterDocRetrieveSecuredImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final RedactionEngineProxy mockRedactionEngineProxy = context.mock(RedactionEngineProxy.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            AdapterDocRetrieveSecuredImpl docRetrieveImpl = new AdapterDocRetrieveSecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            Log log = docRetrieveImpl.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testRedactionEngineProxy()
    {
        try
        {
            AdapterDocRetrieveSecuredImpl docRetrieveImpl = new AdapterDocRetrieveSecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected RedactionEngineProxy getRedactionEngineProxy()
                {
                    return mockRedactionEngineProxy;
                }
            };

            RedactionEngineProxy redactionEngineProxy = docRetrieveImpl.getRedactionEngineProxy();
            assertNotNull("Redaction engine proxy was null", redactionEngineProxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngineProxy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxy test: " + t.getMessage());
        }
    }

    @Test
    public void testCallRedactionEngineHappy()
    {
        try
        {
            RetrieveDocumentSetRequestType mockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
            RetrieveDocumentSetResponseType mockRetrieveResponse = context.mock(RetrieveDocumentSetResponseType.class);

            AdapterDocRetrieveSecuredImpl docRetrieveImpl = new AdapterDocRetrieveSecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected RedactionEngineProxy getRedactionEngineProxy()
                {
                    return mockRedactionEngineProxy;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockRedactionEngineProxy).filterRetrieveDocumentSetResults(with(aNonNull(RetrieveDocumentSetRequestType.class)), with(aNonNull(RetrieveDocumentSetResponseType.class)));
                }
            });

            RetrieveDocumentSetResponseType response = docRetrieveImpl.callRedactionEngine(mockRetrieveRequest, mockRetrieveResponse);
            assertNotNull("AdhocQueryResponse returned was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCallRedactionEngineHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCallRedactionEngineHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testCallRedactionEngineNullRetrieveResponse()
    {
        try
        {
            RetrieveDocumentSetRequestType mockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
            RetrieveDocumentSetResponseType retrieveResponse = null;

            AdapterDocRetrieveSecuredImpl docRetrieveImpl = new AdapterDocRetrieveSecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected RedactionEngineProxy getRedactionEngineProxy()
                {
                    return mockRedactionEngineProxy;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockLog).warn("Did not call redaction engine because the retrieve response was null.");
                }
            });

            RetrieveDocumentSetResponseType response = docRetrieveImpl.callRedactionEngine(mockRetrieveRequest, retrieveResponse);
            assertNull("AdhocQueryResponse returned was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCallRedactionEngineNullRetrieveResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCallRedactionEngineNullRetrieveResponse test: " + t.getMessage());
        }
    }

}