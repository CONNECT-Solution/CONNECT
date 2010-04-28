package gov.hhs.fha.nhinc.patientdiscovery.entity;

import javax.xml.ws.WebServiceContext;
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
public class EntityPatientDiscoverySecuredTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final EntityPatientDiscoverySecuredImpl mockServiceImpl = context.mock(EntityPatientDiscoverySecuredImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);
    final RespondingGatewayPRPAIN201305UV02RequestType mockRequest = context.mock(RespondingGatewayPRPAIN201305UV02RequestType.class);
    
    @Test
    public void testGetEntityPatientDiscoverySecuredImpl()
    {
        try
        {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured()
            {
                @Override
                protected EntityPatientDiscoverySecuredImpl getEntityPatientDiscoverySecuredImpl()
                {
                    return mockServiceImpl;
                }
            };

            EntityPatientDiscoverySecuredImpl servcieImpl = pdSecured.getEntityPatientDiscoverySecuredImpl();
            assertNotNull("EntityPatientDiscoverySecuredImpl was null", servcieImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEntityPatientDiscoverySecuredImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoverySecuredImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext()
    {
        try
        {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured()
            {
                @Override
                protected WebServiceContext getWebServiceContext()
                {
                    return mockWebServiceContext;
                }
            };

            WebServiceContext webServiceContext = pdSecured.getWebServiceContext();
            assertNotNull("WebServiceContext was null", webServiceContext);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetWebServiceContext: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetWebServiceContext: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured()
            {
                @Override
                protected EntityPatientDiscoverySecuredImpl getEntityPatientDiscoverySecuredImpl()
                {
                    return mockServiceImpl;
                }
                @Override
                protected WebServiceContext getWebServiceContext()
                {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations()
            {
                {
                    oneOf(mockServiceImpl).respondingGatewayPRPAIN201305UV02(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)), with(aNonNull(WebServiceContext.class)));
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecured.respondingGatewayPRPAIN201305UV02(mockRequest);
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
    public void testRespondingGatewayPRPAIN201305UV02NullServiceImpl()
    {
        try
        {
            EntityPatientDiscoverySecured pdSecured = new EntityPatientDiscoverySecured()
            {
                @Override
                protected EntityPatientDiscoverySecuredImpl getEntityPatientDiscoverySecuredImpl()
                {
                    return null;
                }
                @Override
                protected WebServiceContext getWebServiceContext()
                {
                    return mockWebServiceContext;
                }
            };

            RespondingGatewayPRPAIN201306UV02ResponseType response = pdSecured.respondingGatewayPRPAIN201305UV02(mockRequest);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullServiceImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullServiceImpl: " + t.getMessage());
        }
    }

}