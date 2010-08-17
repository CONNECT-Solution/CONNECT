package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
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
public class AdapterDocQueryOrchImplTest
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
            AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            Log log = docQueryImpl.createLogger();
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
    public void testGetRedactionEngineProxy()
    {
        try
        {
            AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
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

            AdapterRedactionEngineProxy redactionEngineProxy = docQueryImpl.getRedactionEngineProxy();
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
//            AdhocQueryRequest mockQueryRequest = context.mock(AdhocQueryRequest.class);
//            AdhocQueryResponse mockQueryResponse = context.mock(AdhocQueryResponse.class);
//
//            AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
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
//                    one(mockRedactionEngineProxy).filterAdhocQueryResults(with(aNonNull(AdhocQueryRequest.class)), with(aNonNull(AdhocQueryResponse.class)));
//                }
//            });
//
//            AdhocQueryResponse response = docQueryImpl.callRedactionEngine(mockQueryRequest, mockQueryResponse);
//            assertNotNull("AdhocQueryResponse returned was null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCallRedactionEngineHappy test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCallRedactionEngineHappy test: " + t.getMessage());
//        }
//    }

//    @Test
//    public void testCallRedactionEngineNullInputResponse()
//    {
//        try
//        {
//            AdhocQueryRequest mockQueryRequest = context.mock(AdhocQueryRequest.class);
//            AdhocQueryResponse mockQueryResponse = null;
//
//            AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
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
//                    one(mockLog).warn("Did not call redaction engine because the query response was null.");
//                }
//            });
//
//            AdhocQueryResponse response = docQueryImpl.callRedactionEngine(mockQueryRequest, mockQueryResponse);
//            assertNull("AdhocQueryResponse returned was not null", response);
//        }
//        catch(Throwable t)
//        {
//            System.out.println("Error running testCallRedactionEngineNullInputResponse test: " + t.getMessage());
//            t.printStackTrace();
//            fail("Error running testCallRedactionEngineNullInputResponse test: " + t.getMessage());
//        }
//    }

}