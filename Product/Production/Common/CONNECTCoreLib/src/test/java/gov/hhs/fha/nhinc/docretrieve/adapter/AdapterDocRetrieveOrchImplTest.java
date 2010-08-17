package gov.hhs.fha.nhinc.docretrieve.adapter;

import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
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
 * @author westberg
 */
@RunWith(JMock.class)
public class AdapterDocRetrieveOrchImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final AdapterRedactionEngineProxy mockRedactionEngineProxy = context.mock(AdapterRedactionEngineProxy.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            AdapterDocRetrieveOrchImpl docRetrieveImpl = new AdapterDocRetrieveOrchImpl()
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
            AdapterDocRetrieveOrchImpl docRetrieveImpl = new AdapterDocRetrieveOrchImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected AdapterRedactionEngineProxy getRedactionEngineProxy()
                {
                    return mockRedactionEngineProxy;
                }
            };

            AdapterRedactionEngineProxy redactionEngineProxy = docRetrieveImpl.getRedactionEngineProxy();
            assertNotNull("Redaction engine proxy was null", redactionEngineProxy);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetRedactionEngineProxy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxy test: " + t.getMessage());
        }
    }

//    @Test
//    public void testCallRedactionEngineHappy()
//    {
//        try
//        {
//            RetrieveDocumentSetRequestType mockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
//            RetrieveDocumentSetResponseType mockRetrieveResponse = context.mock(RetrieveDocumentSetResponseType.class);
//
//            AdapterDocRetrieveOrchImpl docRetrieveImpl = new AdapterDocRetrieveOrchImpl()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected AdapterRedactionEngineProxy getRedactionEngineProxy()
//                {
//                    return mockRedactionEngineProxy;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    one(mockRedactionEngineProxy).filterRetrieveDocumentSetResults(with(aNonNull(RetrieveDocumentSetRequestType.class)), with(aNonNull(RetrieveDocumentSetResponseType.class)));
//                }
//            });
//
//            RetrieveDocumentSetResponseType response = docRetrieveImpl.callRedactionEngine(mockRetrieveRequest, mockRetrieveResponse);
//            assertNotNull("AdhocQueryResponse returned was null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCallRedactionEngineHappy test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCallRedactionEngineHappy test: " + t.getMessage());
//        }
//    }
//
//    @Test
//    public void testCallRedactionEngineNullRetrieveResponse()
//    {
//        try
//        {
//            RetrieveDocumentSetRequestType mockRetrieveRequest = context.mock(RetrieveDocumentSetRequestType.class);
//            RetrieveDocumentSetResponseType retrieveResponse = null;
//
//            AdapterDocRetrieveOrchImpl docRetrieveImpl = new AdapterDocRetrieveOrchImpl()
//            {
//                @Override
//                protected Log createLogger()
//                {
//                    return mockLog;
//                }
//                @Override
//                protected AdapterRedactionEngineProxy getRedactionEngineProxy()
//                {
//                    return mockRedactionEngineProxy;
//                }
//            };
//            context.checking(new Expectations()
//            {
//                {
//                    allowing(mockLog).debug(with(any(String.class)));
//                    one(mockLog).warn("Did not call redaction engine because the retrieve response was null.");
//                }
//            });
//
//            RetrieveDocumentSetResponseType response = docRetrieveImpl.callRedactionEngine(mockRetrieveRequest, retrieveResponse);
//            assertNull("AdhocQueryResponse returned was not null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCallRedactionEngineNullRetrieveResponse test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCallRedactionEngineNullRetrieveResponse test: " + t.getMessage());
//        }
//    }
}
