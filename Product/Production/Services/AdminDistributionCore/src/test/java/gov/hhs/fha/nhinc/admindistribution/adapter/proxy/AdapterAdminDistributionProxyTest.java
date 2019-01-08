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
package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionSecuredPortType;
import gov.hhs.fha.nhinc.admindistribution.aspect.EDXLDistributionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import java.lang.reflect.Method;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Test;

public class AdapterAdminDistributionProxyTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<AdapterAdministrativeDistributionSecuredPortType> secureClient = mock(
        CONNECTClient.class);
    @SuppressWarnings("unchecked")
    private final CONNECTClient<AdapterAdministrativeDistributionPortType> client = mock(CONNECTClient.class);
    private EDXLDistribution request = mock(EDXLDistribution.class);
    private AssertionType assertion = mock(AssertionType.class);

    @Test
    public void testSecuredForMtom() {
        AdapterAdminDistributionProxyWebServiceSecuredImpl impl = getSecuredImpl();
        impl.sendAlertMessage(request, assertion);
        verify(secureClient).enableMtom();
    }

    @Test
    public void testUnsecuredForMtom() {
        AdapterAdminDistributionProxyWebServiceUnsecuredImpl impl = getUnsecuredImpl();
        impl.sendAlertMessage(request, assertion);
        verify(client).enableMtom();
    }

    @Test
    public void hasEventAnnotation() throws Exception {
        Class<?>[] classes = {AdapterAdminDistributionProxyJavaImpl.class, AdapterAdminDistributionProxyNoOpImpl.class,
            AdapterAdminDistributionProxyWebServiceSecuredImpl.class,
            AdapterAdminDistributionProxyWebServiceUnsecuredImpl.class};
        for (Class<?> clazz : classes) {
            Method method = clazz.getMethod("sendAlertMessage", EDXLDistribution.class, AssertionType.class);
            AdapterDelegationEvent annotation = method.getAnnotation(AdapterDelegationEvent.class);
            assertNotNull(annotation);
            assertEquals(EDXLDistributionEventDescriptionBuilder.class, annotation.beforeBuilder());
            assertEquals(EDXLDistributionEventDescriptionBuilder.class, annotation.afterReturningBuilder());
            assertEquals("Admin Distribution", annotation.serviceType());
            assertEquals("LEVEL_a0", annotation.version());
        }
    }

    private AdapterAdminDistributionProxyWebServiceSecuredImpl getSecuredImpl() {
        return new AdapterAdminDistributionProxyWebServiceSecuredImpl() {

            /*
             * (non-Javadoc)
             *
             * @see #getCONNECTClientSecured(gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor,
             * java.lang.String, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
             */
            @Override
            protected CONNECTClient<AdapterAdministrativeDistributionSecuredPortType> getCONNECTClientSecured(
                ServicePortDescriptor<AdapterAdministrativeDistributionSecuredPortType> portDescriptor, String url,
                AssertionType assertion) {
                return secureClient;
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyWebServiceSecuredImpl#
             * getHelper()
             */
            @Override
            protected String getUrl() {
                return "endpoint";
            }
        };
    }

    private AdapterAdminDistributionProxyWebServiceUnsecuredImpl getUnsecuredImpl() {
        return new AdapterAdminDistributionProxyWebServiceUnsecuredImpl() {

            /*
             * (non-Javadoc)
             *
             * @see #getCONNECTClientSecured(gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor,
             * java.lang.String, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
             */
            @Override
            protected CONNECTClient<AdapterAdministrativeDistributionPortType> getCONNECTClientUnsecured(
                ServicePortDescriptor<AdapterAdministrativeDistributionPortType> portDescriptor, String url,
                AssertionType assertion) {
                return client;
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyWebServiceSecuredImpl#
             * getHelper()
             */
            @Override
            protected String getUrl() {
                return "endpoint";
            }
        };
    }
}
