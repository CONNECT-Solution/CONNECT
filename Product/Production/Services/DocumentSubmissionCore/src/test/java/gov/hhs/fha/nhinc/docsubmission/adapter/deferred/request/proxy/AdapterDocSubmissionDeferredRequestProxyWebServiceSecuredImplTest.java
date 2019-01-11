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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionBaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.lang.reflect.Method;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author patlollav
 */
public class AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImplTest {

    private final WebServiceProxyHelper mockWSProxyHelper = mock(WebServiceProxyHelper.class);

    public AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImplTest() {
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

    class CONNECTClientMock implements CONNECTClient<AdapterXDRRequestSecuredPortType> {

        @Override
        public AdapterXDRRequestSecuredPortType getPort() {
            return null;
        }

        @Override
        public Object invokePort(Class<AdapterXDRRequestSecuredPortType> portClass, String methodName,
            Object... operationInput) throws Exception {
            XDRAcknowledgementType response = new XDRAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
            response.setMessage(regResp);
            return response;
        }

        /*
         * (non-Javadoc)
         *
         * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#supportMtom()
         */
        @Override
        public void enableMtom() {

        }

        /*
         * (non-Javadoc) @see
         * gov.hhs.fha.nhinc.messaging.client.CONNECTClient#enableWSA(gov.hhs.fha.nhinc.common.nhinccommon.AssertionType,
         * java.lang.String, java.lang.String)
         */
        @Override
        public void enableWSA(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId) {

        }
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequest() throws ExchangeManagerException {
        final CONNECTClient<AdapterXDRRequestSecuredPortType> mockClient = new CONNECTClientMock();
        AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl adapterXDRRequestWebServiceProxy
            = new AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {

            @Override
            protected CONNECTClient<AdapterXDRRequestSecuredPortType> getCONNECTClientSecured(
                ServicePortDescriptor<AdapterXDRRequestSecuredPortType> portDescriptor, String url,
                AssertionType assertion) {
                return mockClient;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return mockWSProxyHelper;
            }
        };
        when(mockWSProxyHelper.getAdapterEndPointFromConnectionManager(anyString())).thenReturn(
            "https://localhost:8181/Adapter/DocumentSubmission/A_0/AdapterService/AdapterDocSubmissionDeferredRequestSecured");
        ProvideAndRegisterDocumentSetRequestType iheMsg = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(iheMsg,
            assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestFailureCase() throws ExchangeManagerException {
        final CONNECTClient<AdapterXDRRequestSecuredPortType> mockClient = new CONNECTClientMock();

        AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl adapterXDRRequestWebServiceProxy
            = new AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {

            @Override
            protected CONNECTClient<AdapterXDRRequestSecuredPortType> getCONNECTClientSecured(
                ServicePortDescriptor<AdapterXDRRequestSecuredPortType> portDescriptor, String url,
                AssertionType assertion) {
                return mockClient;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return mockWSProxyHelper;
            }

        };
        when(mockWSProxyHelper.getAdapterEndPointFromConnectionManager(anyString())).thenReturn(
            "https://localhost:8181/Adapter/DocumentSubmission/A_0/AdapterService/AdapterDocSubmissionDeferredRequestSecured");
        ProvideAndRegisterDocumentSetRequestType iheMsg = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(iheMsg,
            assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    @Test
    public void hasAdapterDelegationEvent() throws Exception {
        Class<AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl> clazz
            = AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBRequest",
            ProvideAndRegisterDocumentSetRequestType.class, AssertionType.class);
        AdapterDelegationEvent annotation = method.getAnnotation(AdapterDelegationEvent.class);
        assertNotNull(annotation);
        assertEquals(DocSubmissionBaseEventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionArgTransformerBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Request", annotation.serviceType());
        assertEquals("LEVEL_a0", annotation.version());
    }
}
