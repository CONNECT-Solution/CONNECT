/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.RespondingGatewayDeferredResponsePortType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

/**
 * @author achidamb
 *
 */
public class NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<RespondingGatewayDeferredResponsePortType> client = mock(CONNECTClient.class);
    private NhinTargetSystemType target = mock(NhinTargetSystemType.class);
    private AssertionType assertion = mock(AssertionType.class);
    private PRPAIN201306UV02 request = mock(PRPAIN201306UV02.class);

    @Test
    public void testNoMtom() throws Exception {
        NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl impl = getImpl();

        Field helper = ReflectionUtils.findField(NhinPatientDiscoveryDeferredRespProxyWebServiceSecuredImpl.class, "oProxyHelper");
        ReflectionUtils.makeAccessible(helper);
        WebServiceProxyHelper helperMock = mock(WebServiceProxyHelper.class);
        Mockito.when(helperMock.getUrlFromTargetSystemByGatewayAPILevel(target,NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME, GATEWAY_API_LEVEL.LEVEL_g0)).thenReturn("Localhost");
        ReflectionUtils.setField(helper, impl, helperMock);
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
