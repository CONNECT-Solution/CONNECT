package gov.hhs.fha.nhinc.patientdiscovery.entity;

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
public class EntityPatientDiscoveryUnsecuredTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final EntityPatientDiscoveryUnsecuredImpl mockServiceImpl = context.mock(EntityPatientDiscoveryUnsecuredImpl.class);

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            EntityPatientDiscoveryUnsecured unsecuredService = new EntityPatientDiscoveryUnsecured()
            {
                @Override
                protected EntityPatientDiscoveryUnsecuredImpl getEntityPatientDiscoveryUnsecuredImpl()
                {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockServiceImpl).respondingGatewayPRPAIN201305UV02(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

            RespondingGatewayPRPAIN201306UV02ResponseType response = unsecuredService.respondingGatewayPRPAIN201305UV02(request);
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
    public void testRespondingGatewayPRPAIN201305UV02NullImpl()
    {
        try
        {
            EntityPatientDiscoveryUnsecured unsecuredService = new EntityPatientDiscoveryUnsecured()
            {
                @Override
                protected EntityPatientDiscoveryUnsecuredImpl getEntityPatientDiscoveryUnsecuredImpl()
                {
                    return null;
                }
            };

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

            RespondingGatewayPRPAIN201306UV02ResponseType response = unsecuredService.respondingGatewayPRPAIN201305UV02(request);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullImpl: " + t.getMessage());
        }
    }


}