/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.nhin.proxy;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import ihe.iti.xdr._2007.DocumentRepositoryXDRPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author achidamb
 *
 */
public class NhinDocSubmissionProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<DocumentRepositoryXDRPortType> client = mock(CONNECTClient.class);
    private final DocSubmissionUtils utils = mock(DocSubmissionUtils.class);
    private ProvideAndRegisterDocumentSetRequestType request = mock(ProvideAndRegisterDocumentSetRequestType.class);
    private AssertionType assertion = mock(AssertionType.class);

    @Test
    public void testG0() {
        NhinDocSubmissionProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType targetSystem = getNhinTargetSystemType("1.1");
        impl.provideAndRegisterDocumentSetB(request, assertion, targetSystem, GATEWAY_API_LEVEL.LEVEL_g0);
        verify(client).enableMtom();
    }

    @Test
    public void testG1() {
        NhinDocSubmissionProxyWebServiceSecuredImpl impl = getImpl();
        NhinTargetSystemType targetSystem = getNhinTargetSystemType("1.1");
        impl.provideAndRegisterDocumentSetB(request, assertion, targetSystem, GATEWAY_API_LEVEL.LEVEL_g1);
        verify(client).enableMtom();
    }

    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinDocSubmissionProxyWebServiceSecuredImpl> clazz = NhinDocSubmissionProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetB",
                ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class, NhinTargetSystemType.class,
                GATEWAY_API_LEVEL.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    private NhinDocSubmissionProxyWebServiceSecuredImpl getImpl() {
        return new NhinDocSubmissionProxyWebServiceSecuredImpl() {

            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.docsubmission.nhin.proxy.NhinDocSubmissionProxyWebServiceSecuredImpl#
             * getCONNECTClientSecured(gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor,
             * gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String, java.lang.String)
             */
            @Override
            protected CONNECTClient<DocumentRepositoryXDRPortType> getCONNECTClientSecured(
                    ServicePortDescriptor<DocumentRepositoryXDRPortType> portDescriptor, AssertionType assertion,
                    String url, String targetHomeCommunityId, String serviceName) {
                return client;
            }

            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request.proxy11.
             * NhinDocSubmissionDeferredRequestProxyWebServiceSecuredImpl#getDocSubmissionUtils()
             */
            @Override
            protected DocSubmissionUtils getDocSubmissionUtils() {
                return utils;
            }
        };
    }

    private NhinTargetSystemType getNhinTargetSystemType(String sHcid) {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId(sHcid);
        targetSystem.setHomeCommunity(hcid);
        return targetSystem;
    }

}
