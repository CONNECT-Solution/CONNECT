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
package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class EntityDocSubmissionProxyWebServiceSecuredImplTest {

    private static final String mockUrl = "http://localhost:8080/";

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final WebServiceProxyHelper mockProxyHelper = context.mock(WebServiceProxyHelper.class);
    @SuppressWarnings("unchecked")
    final CONNECTClient<EntityXDRSecuredPortType> mockCONNECTClient = context.mock(CONNECTClient.class);

    @Test
    public void testProvideAndRegisterDocumentSetB() throws Exception {
        expectMockCONNECTClient();
        expectMockWebServiceProxyHelperGetUrl();

        EntityDocSubmissionProxyWebServiceSecuredImpl proxyImpl = createWebServiceSecuredImpl();
        RegistryResponseType response = invokeProvideAndRegisterDocumentSetB(proxyImpl);

        context.assertIsSatisfied();
        assertEquals(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS, response.getStatus());
    }

    @Test
    public void testGetters() {
        EntityDocSubmissionProxyWebServiceSecuredImpl proxyImpl = new EntityDocSubmissionProxyWebServiceSecuredImpl();

        assertNotNull(proxyImpl.createWebServiceProxyHelper());
    }

    private RegistryResponseType invokeProvideAndRegisterDocumentSetB(EntityDocSubmissionProxyWebServiceSecuredImpl proxyImpl) {
        ProvideAndRegisterDocumentSetRequestType message = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        UrlInfoType urlInfo = new UrlInfoType();

        return proxyImpl.provideAndRegisterDocumentSetB(message, assertion, targets, urlInfo);
    }

    private void expectMockWebServiceProxyHelperGetUrl() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(mockProxyHelper).getUrlLocalHomeCommunity(with(equal(NhincConstants.ENTITY_XDR_SECURED_SERVICE_NAME)));
                will(returnValue(mockUrl));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void expectMockCONNECTClient() throws Exception {
        context.checking(new Expectations() {
            {
                // TODO: Using "anything()" to match "Object..." due to JMock upgrade
                oneOf(mockCONNECTClient).invokePort(with(any(Class.class)),
                        with(any(String.class)),
                        with(anything()));
                will(returnValue(createValidRegistryResponse()));
            }
        });
    }

    private RegistryResponseType createValidRegistryResponse() {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus(DocumentConstants.XDS_SUBMISSION_RESPONSE_STATUS_SUCCESS);

        return response;
    }

    private EntityDocSubmissionProxyWebServiceSecuredImpl createWebServiceSecuredImpl() {
        return new EntityDocSubmissionProxyWebServiceSecuredImpl() {
            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return mockProxyHelper;
            }

            @Override
            protected CONNECTClient<EntityXDRSecuredPortType> getCONNECTClient(
                    ServicePortDescriptor<EntityXDRSecuredPortType> portDescriptor, String url, AssertionType assertion) {
                return mockCONNECTClient;
            }
        };
    }
}
