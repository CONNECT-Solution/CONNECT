package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy ;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryProxyNoOpImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testRespondingGatewayPRPAIN201305UV02()
    {
        try
        {
            EntityPatientDiscoveryProxyNoOpImpl noOpImpl = new EntityPatientDiscoveryProxyNoOpImpl();

            PRPAIN201305UV02 mockPdRequest = context.mock(PRPAIN201305UV02.class);
            AssertionType mockAssertion = context.mock(AssertionType.class);
            NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);

            RespondingGatewayPRPAIN201306UV02ResponseType response = noOpImpl.respondingGatewayPRPAIN201305UV02(mockPdRequest, mockAssertion, mockTargetCommunities);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02: " + t.getMessage());
        }
    }


}