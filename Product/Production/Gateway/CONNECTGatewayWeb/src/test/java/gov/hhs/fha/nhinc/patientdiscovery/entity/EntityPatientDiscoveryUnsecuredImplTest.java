package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
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
public class EntityPatientDiscoveryUnsecuredImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final EntityPatientDiscoveryOrchImpl mockEntityPatientDiscoveryProcessor = context.mock(EntityPatientDiscoveryOrchImpl.class);
    final PRPAIN201305UV02 mockPdMessage = context.mock(PRPAIN201305UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
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
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            Log log = pdUnsecuredImpl.createLogger();
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
    public void testGetEntityPatientDiscoveryProcessor()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return mockEntityPatientDiscoveryProcessor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            EntityPatientDiscoveryOrchImpl processor = pdUnsecuredImpl.getEntityPatientDiscoveryProcessor();
            assertNotNull("EntityPatientDiscoveryProcessor was null", processor);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProcessor: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return mockEntityPatientDiscoveryProcessor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockEntityPatientDiscoveryProcessor).respondingGatewayPRPAIN201305UV02(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)), with(aNonNull(AssertionType.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl.respondingGatewayPRPAIN201305UV02(request);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullRequest()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    return mockEntityPatientDiscoveryProcessor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = null;

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl.respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullProcessor()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
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
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("EntityPatientDiscoveryProcessor was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl.respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullResponse()
    {
        try
        {
            EntityPatientDiscoveryUnsecuredImpl pdUnsecuredImpl = new EntityPatientDiscoveryUnsecuredImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected EntityPatientDiscoveryOrchImpl getEntityPatientDiscoveryProcessor()
                {
                    EntityPatientDiscoveryOrchImpl processor = new EntityPatientDiscoveryOrchImpl()
                    {
                        @Override
                        public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                        {
                            return null;
                        }
                    };
                    return processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPdMessage);
            request.setAssertion(mockAssertion);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdUnsecuredImpl.respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullProcessor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullResponse: " + t.getMessage());
        }
    }

}