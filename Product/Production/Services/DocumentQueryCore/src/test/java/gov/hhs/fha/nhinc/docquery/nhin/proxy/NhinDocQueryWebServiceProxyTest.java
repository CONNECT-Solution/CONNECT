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
package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryRequestDescriptionBuilder;
import gov.hhs.fha.nhinc.docquery.aspect.AdhocQueryResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import java.lang.reflect.Method;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class NhinDocQueryWebServiceProxyTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Service mockService = context.mock(Service.class);
    final RespondingGatewayQueryPortType mockPort = context.mock(RespondingGatewayQueryPortType.class);

    @SuppressWarnings("unchecked")
    private CONNECTClient<RespondingGatewayQueryPortType> client = mock(CONNECTClient.class);
    private ExchangeManager cache = mock(ExchangeManager.class);
    private AdhocQueryRequest request;
    private AssertionType assertion;

    @Test
    public void hasBeginOutboundProcessingEvent() throws Exception {
        Class<NhinDocQueryProxyWebServiceSecuredImpl> clazz = NhinDocQueryProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayQuery", AdhocQueryRequest.class,
            AssertionType.class, NhinTargetSystemType.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(AdhocQueryRequestDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(AdhocQueryResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Query", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testNoMtom() throws Exception {
        NhinDocQueryProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType target = getTarget("1.1");
        impl.respondingGatewayCrossGatewayQuery(request, assertion, target);
        verify(client, never()).enableMtom();
    }

    @Test
    public void testUsingGuidance() throws Exception {
        NhinDocQueryProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType target = getTarget("1.1");
        impl.respondingGatewayCrossGatewayQuery(request, assertion, target);
        verify(cache).getEndpointURL(any(String.class), any(String.class), any(
            UDDI_SPEC_VERSION.class), any(String.class));
    }

    /**
     * @param hcidValue
     * @return
     */
    private NhinTargetSystemType getTarget(String hcidValue) {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId(hcidValue);
        target.setHomeCommunity(hcid);
        target.setUseSpecVersion("2.0");
        return target;
    }

    /**
     * @return
     */
    private NhinDocQueryProxyWebServiceSecuredImpl getImpl() {
        return new NhinDocQueryProxyWebServiceSecuredImpl() {
            /*
             * (non-Javadoc)
             *
             * @see
             * gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyWebServiceSecuredImpl#getCONNECTClientSecured(
             * gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor,
             * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String,
             * gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType)
             */
            @Override
            public CONNECTClient<RespondingGatewayQueryPortType> getCONNECTClientSecured(
                ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor, AssertionType assertion,
                String url, NhinTargetSystemType target) {
                return client;
            }

            /*
             * (non-Javadoc) @see
             * gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyWebServiceSecuredImpl#getCMInstance()
             */
            @Override
            protected ExchangeManager getCMInstance() {
                return cache;
            }
        };
    }
}
