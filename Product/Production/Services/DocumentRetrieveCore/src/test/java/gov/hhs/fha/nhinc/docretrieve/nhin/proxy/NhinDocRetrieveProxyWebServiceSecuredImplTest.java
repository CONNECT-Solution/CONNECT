/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author achidamb
 * 
 */
public class NhinDocRetrieveProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private CONNECTClient<RespondingGatewayRetrievePortType> client = mock(CONNECTClient.class);
    private RetrieveDocumentSetRequestType request = mock(RetrieveDocumentSetRequestType.class);
    private AssertionType assertion;

    @Test
    public void hasNwhinInvocationEventEvent() throws Exception {
        Class<NhinDocRetrieveProxyWebServiceSecuredImpl> clazz = NhinDocRetrieveProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("respondingGatewayCrossGatewayRetrieve", RetrieveDocumentSetRequestType.class,
                AssertionType.class, NhinTargetSystemType.class, GATEWAY_API_LEVEL.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(RetrieveDocumentSetRequestTypeDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(RetrieveDocumentSetResponseTypeDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Retrieve Document", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testMtomg0() throws Exception {
        NhinDocRetrieveProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType target = getTarget("1.1");
        impl.respondingGatewayCrossGatewayRetrieve(request, assertion, target, GATEWAY_API_LEVEL.LEVEL_g0);
        verify(client).enableMtom();
    }

    @Test
    public void testMtomg1() throws Exception {
        NhinDocRetrieveProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType target = getTarget("1.1");
        impl.respondingGatewayCrossGatewayRetrieve(request, assertion, target, GATEWAY_API_LEVEL.LEVEL_g1);
        verify(client).enableMtom();
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
        return target;
    }

    /**
     * @return
     */
    private NhinDocRetrieveProxyWebServiceSecuredImpl getImpl() {
        return new NhinDocRetrieveProxyWebServiceSecuredImpl() {

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
            public CONNECTClient<RespondingGatewayRetrievePortType> getCONNECTClientSecured(
                    ServicePortDescriptor<RespondingGatewayRetrievePortType> portDescriptor, AssertionType assertion,
                    String url, NhinTargetSystemType target) {
                return client;
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxyWebServiceSecuredImpl#getUrl(gov.hhs.fha
             * .nhinc.common.nhinccommon.NhinTargetSystemType, java.lang.String,
             * gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL)
             */
            @Override
            protected String getUrl(NhinTargetSystemType targetSystem, String sServiceName, GATEWAY_API_LEVEL level)
                    throws IllegalArgumentException, ConnectionManagerException, Exception {
                return "endpoint";
            }
        };
    }
}
