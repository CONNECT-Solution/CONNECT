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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import static org.junit.Assert.assertEquals;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.net.MalformedURLException;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import gov.hhs.healthit.nhin.XDRAcknowledgementType;

/**
 * 
 * @author patlollav
 */
public class AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImplTest {
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

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequest() {
        System.out.println("provideAndRegisterDocumentSetBRequest");

        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AdapterXDRRequestSecuredPortType mockPort = mockery.mock(AdapterXDRRequestSecuredPortType.class);
        final Service mockService = mockery.mock(Service.class);
        final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            public String getEndPointFromConnectionManagerByAdapterAPILevel(String sServiceName, ADAPTER_API_LEVEL level)
            {
                return "url";
            }

            @Override
            public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception {
                XDRAcknowledgementType response = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                response.setMessage(regResp);
                return response;
            }

            @Override
            public void initializeSecurePort(BindingProvider port, String url, String serviceAction,
                    String wsAddressingAction, AssertionType assertion) {

            }

            @Override
            public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart)
                    throws MalformedURLException {
                return mockService;
            }
        };

        AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl adapterXDRRequestWebServiceProxy = new AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return proxyHelper;
            }

            @Override
            protected AdapterXDRRequestSecuredPortType getPort(String url, String wsAddressingAction,
                    AssertionType assertion) {
                return mockPort;
            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        ProvideAndRegisterDocumentSetRequestType iheMsg = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(iheMsg,
                null, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestFailureCase() {
        System.out.println("testProvideAndRegisterDocumentSetBRequestFailureCase");

        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AdapterXDRRequestSecuredPortType mockPort = mockery.mock(AdapterXDRRequestSecuredPortType.class);
        final Service mockService = mockery.mock(Service.class);
        final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            public String getEndPointFromConnectionManagerByAdapterAPILevel(String sServiceName, ADAPTER_API_LEVEL level)
            {
                return "url";
            }

            @Override
            public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
                    throws Exception {
                XDRAcknowledgementType response = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                response.setMessage(regResp);
                return response;
            }

            @Override
            public void initializeSecurePort(BindingProvider port, String url, String serviceAction,
                    String wsAddressingAction, AssertionType assertion) {

            }

            @Override
            public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart)
                    throws MalformedURLException {
                return mockService;
            }
        };

        AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl adapterXDRRequestWebServiceProxy = new AdapterDocSubmissionDeferredRequestProxyWebServiceSecuredImpl() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return proxyHelper;
            }

            @Override
            protected AdapterXDRRequestSecuredPortType getPort(String url, String wsAddressingAction,
                    AssertionType assertion) {
                return mockPort;
            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        ProvideAndRegisterDocumentSetRequestType iheMsg = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(iheMsg,
                null, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }
}
