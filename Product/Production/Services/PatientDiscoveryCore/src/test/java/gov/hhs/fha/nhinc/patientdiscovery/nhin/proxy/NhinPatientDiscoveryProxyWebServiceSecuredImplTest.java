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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy;

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
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import ihe.iti.xcpd._2009.RespondingGatewayPortType;
import java.lang.reflect.Method;
import javax.xml.ws.Service;
import org.hl7.v3.PRPAIN201305UV02;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
public class NhinPatientDiscoveryProxyWebServiceSecuredImplTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Service mockService = context.mock(Service.class);
    final RespondingGatewayPortType mockPort = context.mock(RespondingGatewayPortType.class);
    @SuppressWarnings("unchecked")
    private final CONNECTClient<RespondingGatewayPortType> client = mock(CONNECTClient.class);
    private NhinTargetSystemType target = mock(NhinTargetSystemType.class);
    private PRPAIN201305UV02 request = mock(PRPAIN201305UV02.class);
    private AssertionType assertion = mock(AssertionType.class);

    public NhinPatientDiscoveryProxyWebServiceSecuredImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinPatientDiscoveryProxyWebServiceSecuredImpl> clazz = NhinPatientDiscoveryProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("respondingGatewayPRPAIN201305UV02", PRPAIN201305UV02.class,
                AssertionType.class, NhinTargetSystemType.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(PRPAIN201306UV02EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void testNoMtom() throws Exception {
        NhinPatientDiscoveryProxyWebServiceSecuredImpl impl = getImpl();
        Mockito.when(target.getUrl()).thenReturn("localhost");
        impl.respondingGatewayPRPAIN201305UV02(request, assertion, target);
        verify(client, never()).enableMtom();
    }

    /**
     * @return
     */
    private NhinPatientDiscoveryProxyWebServiceSecuredImpl getImpl() {
        return new NhinPatientDiscoveryProxyWebServiceSecuredImpl() {

            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy.NhinPatientDiscoveryProxyWebServiceSecuredImpl#
             * getCONNECTSecuredClient(gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType,
             * gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor, java.lang.String,
             * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType)
             */
            @Override
            protected CONNECTClient<RespondingGatewayPortType> getCONNECTSecuredClient(NhinTargetSystemType target,
                    ServicePortDescriptor<RespondingGatewayPortType> portDescriptor, String url, AssertionType assertion) {
                return client;
            }
        };
    }
}
