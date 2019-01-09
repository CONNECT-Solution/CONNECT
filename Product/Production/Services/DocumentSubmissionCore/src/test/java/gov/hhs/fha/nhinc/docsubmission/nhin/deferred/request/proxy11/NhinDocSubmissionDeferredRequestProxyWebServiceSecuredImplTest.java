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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xdr._2007.XDRDeferredRequestPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.lang.reflect.Method;
import org.junit.Test;

public class NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<XDRDeferredRequestPortType> client = mock(CONNECTClient.class);
    private final DocSubmissionUtils utils = mock(DocSubmissionUtils.class);
    private ProvideAndRegisterDocumentSetRequestType request = mock(ProvideAndRegisterDocumentSetRequestType.class);
    private AssertionType assertion = mock(AssertionType.class);
    private WebServiceProxyHelper proxyHelper = mock(WebServiceProxyHelper.class);

    @Test
    public void test() throws ExchangeManagerException, Exception {
        NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId("1.1");
        targetSystem.setHomeCommunity(hcid);
        when(proxyHelper.getUrlFromTargetSystemByGatewayAPILevel(targetSystem,
            NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME, NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0)).thenReturn("");

        impl.provideAndRegisterDocumentSetBRequest11(request, assertion, targetSystem);
        verify(client).enableMtom();
    }

    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl> clazz
            = NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBRequest11",
            ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class, NhinTargetSystemType.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Request", annotation.serviceType());
        assertEquals("1.1", annotation.version());
    }

    private NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl getImpl() {
        return new NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {

            @Override
            protected CONNECTClient<XDRDeferredRequestPortType> getCONNECTClientSecured(
                ServicePortDescriptor<XDRDeferredRequestPortType> portDescriptor, String url,
                AssertionType assertion, NhinTargetSystemType target, String serviceName) {
                return client;
            }

            @Override
            protected DocSubmissionUtils getDocSubmissionUtils() {
                return utils;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return proxyHelper;
            }
        };
    }
}
