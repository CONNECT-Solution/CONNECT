/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxy;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
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
 * @author mflynn02
 */
@RunWith(JMock.class)
public class NhincPatientDiscoveryOrchImplTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PRPAIN201305UV02 mockPRPAIN201305UV02 = context.mock(PRPAIN201305UV02.class);
    final PRPAIN201306UV02 mockPRPAIN201306UV02 = context.mock(PRPAIN201306UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetSystemType mockTargetSystem = context.mock(NhinTargetSystemType.class);
    final NhinPatientDiscoveryProxy mockProxy = context.mock(NhinPatientDiscoveryProxy.class);

    /**
     * Test of createLogger method, of class NhincPatientDiscoveryOrchImpl.
     */
    @Test
    public void testCreateLogger() {
        {
            try {
                NhincPatientDiscoveryOrchImpl processor = new NhincPatientDiscoveryOrchImpl() {

                    @Override
                    protected Log createLogger() {
                        return mockLog;
                    }

                };
                context.checking(new Expectations() {

                    {
                        allowing(mockLog).debug(with(aNonNull(String.class)));
                    }
                });

                Log log = processor.createLogger();
                assertNotNull("Log was null", log);
            } catch (Throwable t) {
                System.out.println("Error running testCreateLogger: " + t.getMessage());
                t.printStackTrace();
                fail("Error running testCreateLogger: " + t.getMessage());
            }
        }
    }

    /**
     * Test of proxyPRPAIN201305UV method, of class NhincPatientDiscoveryOrchImpl.
     */
    @Test
    public void testProxyPRPAIN201305UV() {
        try {
            NhincPatientDiscoveryOrchImpl instance = new NhincPatientDiscoveryOrchImpl()
            {
                @Override
                protected Log createLogger ( ) {
                   return mockLog;
                }
                    @Override
                    protected void logNhincPatientDiscoveryRequest(PRPAIN201305UV02 request, AssertionType assertion) {
                    }

                    @Override
                    protected void logNhincPatientDiscoveryResponse(PRPAIN201306UV02 response, AssertionType assertion) {
                    }
                    @Override
                    protected PRPAIN201306UV02 sendToNhinProxy(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetSystemType target) {
                        return mockPRPAIN201306UV02;
                    }

            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    allowing(mockTargetSystem).getHomeCommunity();
                }
            });

            ProxyPRPAIN201305UVProxySecuredRequestType request = new ProxyPRPAIN201305UVProxySecuredRequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetSystem(mockTargetSystem);

            PRPAIN201306UV02 result = instance.proxyPRPAIN201305UV(request, mockAssertion);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", result);
        } catch (Throwable t) {
            System.out.println("Error running testProxyPRPAIN201305UV: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProxyPRPAIN201305UV: " + t.getMessage());
        }

    }
}
