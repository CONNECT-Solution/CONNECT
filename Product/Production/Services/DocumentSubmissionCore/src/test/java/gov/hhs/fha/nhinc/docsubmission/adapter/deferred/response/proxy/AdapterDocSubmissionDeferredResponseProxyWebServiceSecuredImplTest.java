/*
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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.docsubmission.aspect.DocSubmissionArgTransformerBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;

import java.lang.reflect.Method;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author patlollav
 */
public class AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImplTest {
    Mockery mockery = new Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final CONNECTClient<AdapterXDRResponseSecuredPortType> client = new CONNECTClientMock();

    public AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImplTest() {
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

    class CONNECTClientMock implements CONNECTClient<AdapterXDRResponseSecuredPortType> {

        @Override
        public AdapterXDRResponseSecuredPortType getPort() {
            return null;
        }

        @Override
        public Object invokePort(Class<AdapterXDRResponseSecuredPortType> portClass, String methodName,
                Object operationInput) throws Exception {
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

        /* (non-Javadoc)
         * @see gov.hhs.fha.nhinc.messaging.client.CONNECTClient#enableWSA(gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String)
         */
        @Override
        public void enableWSA(AssertionType assertion, String wsAddressingTo, String wsAddressingActionId) {
            
        }
    }

    private WebServiceProxyHelper createMockWebServiceProxyHelper(final String url) {
        return new WebServiceProxyHelper() {

            @Override
            public String getAdapterEndPointFromConnectionManager(String sServiceName) {
                return url;
            }
        };
    }

    private AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl createWebServiceProxy(
            final WebServiceProxyHelper proxyHelper) {
        return new AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return proxyHelper;
            }

            @Override
            protected CONNECTClient<AdapterXDRResponseSecuredPortType> getCONNECTClientSecured(
                    ServicePortDescriptor<AdapterXDRResponseSecuredPortType> portDescriptor, String url,
                    AssertionType assertion) {

                return client;
            }
        };
    }

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponse() {
        WebServiceProxyHelper proxyHelper = createMockWebServiceProxyHelper("url");
        AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl adapterXDRResponseWebServiceProxy = createWebServiceProxy(proxyHelper);

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRResponseWebServiceProxy.provideAndRegisterDocumentSetBResponse(body,
                assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponseFailureCase() {
        WebServiceProxyHelper proxyHelper = createMockWebServiceProxyHelper(null);
        AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl adapterXDRResponseWebServiceProxy = createWebServiceProxy(proxyHelper);

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRResponseWebServiceProxy.provideAndRegisterDocumentSetBResponse(body,
                assertion);
        assertNull(result);
    }

    @Test
    public void hasAdapterDelegationEvent() throws Exception {
        Class<AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl> clazz = AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBResponse", RegistryResponseType.class,
                AssertionType.class);
        AdapterDelegationEvent annotation = method.getAnnotation(AdapterDelegationEvent.class);
        assertNotNull(annotation);
        assertEquals(DeferredResponseDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DocSubmissionArgTransformerBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Response", annotation.serviceType());
        assertEquals("", annotation.version());
    }
}
