package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImpl;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
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
public class EntityPatientDiscoveryProxyJavaImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PRPAIN201305UV02 mockPdRequest = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final EntityPatientDiscoveryOrchImpl mockProcessor = context.mock(EntityPatientDiscoveryOrchImpl.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl()
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
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoveryProcessor()
    {
        try
        {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return mockProcessor;
                }
            };
            EntityPatientDiscoveryOrchImpl processor = sut.getEntityPatientDiscoveryProcessor();
            assertNotNull("EntityPatientDiscoveryProcessor was null", processor);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoveryProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProcessor test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return mockProcessor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockProcessor).respondingGatewayPRPAIN201305UV02(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)), with(aNonNull(AssertionType.class)));
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = sut.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy test: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullProcessor()
    {
        try
        {
            EntityPatientDiscoveryProxyJavaImpl sut = new EntityPatientDiscoveryProxyJavaImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockLog).warn("EntityPatientDiscoveryProcessor was null");
                }
            });
            RespondingGatewayPRPAIN201306UV02ResponseType response = sut.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor test: " + t.getMessage());
        }
    }

}