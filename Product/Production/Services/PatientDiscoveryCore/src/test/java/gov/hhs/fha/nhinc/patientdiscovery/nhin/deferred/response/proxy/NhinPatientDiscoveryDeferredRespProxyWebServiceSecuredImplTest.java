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
import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import java.lang.reflect.Method;

import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;

/**
 * @author achidamb
 *
 */
public class NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImplTest {
    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl> clazz = 
                NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("respondingGatewayPRPAIN201306UV02", PRPAIN201306UV02.class, 
                AssertionType.class, NhinTargetSystemType.class );
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201306UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Response", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }
}
