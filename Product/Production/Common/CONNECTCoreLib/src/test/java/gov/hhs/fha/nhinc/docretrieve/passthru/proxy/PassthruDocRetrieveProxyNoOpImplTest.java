package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
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
public class PassthruDocRetrieveProxyNoOpImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            PassthruDocRetrieveProxyNoOpImpl sut = new PassthruDocRetrieveProxyNoOpImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieve()
    {
        try
        {
            PassthruDocRetrieveProxyNoOpImpl sut = new PassthruDocRetrieveProxyNoOpImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockLog).debug("Begin PassthruDocRetrieveProxyNoOpImpl.respondingGatewayCrossGatewayRetrieve");
                }
            });
            RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
            AssertionType assertion = new AssertionType();
            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            RetrieveDocumentSetResponseType response = sut.respondingGatewayCrossGatewayRetrieve(request, assertion, targetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieve: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieve: " + t.getMessage());
        }
    }

}