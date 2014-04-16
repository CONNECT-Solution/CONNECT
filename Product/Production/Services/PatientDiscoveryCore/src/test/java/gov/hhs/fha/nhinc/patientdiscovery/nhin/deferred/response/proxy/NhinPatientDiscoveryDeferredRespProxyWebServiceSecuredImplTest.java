/**
 * @author achidamb
 *
 */
/**
 * 
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType;

import java.lang.reflect.Method;

import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;

/**
 * @author achidamb
 * 
 */
public class NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<RespondingGatewayDeferredResponsePortType> client = mock(CONNECTClient.class);
    private NhinTargetSystemType target;
    private AssertionType assertion;
    private PRPAIN201306UV02 request;

    @Test
    public void testNoMtom() throws Exception {
        NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl impl = getImpl();
        impl.respondingGatewayPRPAIN201306UV02(request, assertion, target);
        verify(client, never()).enableMtom();
    }

    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl> clazz = NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("respondingGatewayPRPAIN201306UV02", PRPAIN201306UV02.class,
                AssertionType.class, NhinTargetSystemType.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Response", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    /**
     * @return
     */
    private NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl getImpl() {
        return new NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl() {

            /*
             * (non-Javadoc)
             * 
             * @see gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyWebServiceSecuredImpl#
             * getCONNECTSecuredClient(gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType,
             * gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor, java.lang.String,
             * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
             */
            @Override
            protected CONNECTClient<RespondingGatewayDeferredResponsePortType> getCONNECTSecuredClient(
                    ServicePortDescriptor<RespondingGatewayDeferredResponsePortType> portDescriptor,
                    AssertionType assertion, String url, NhinTargetSystemType target) {
                return client;
            }
        };
    }
}
