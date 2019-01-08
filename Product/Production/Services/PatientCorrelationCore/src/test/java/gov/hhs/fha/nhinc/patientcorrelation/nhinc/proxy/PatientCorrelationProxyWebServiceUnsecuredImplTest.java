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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import org.hl7.v3.AddPatientCorrelationPLQRequestType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.junit.Before;
import org.junit.Test;

public class PatientCorrelationProxyWebServiceUnsecuredImplTest {

    private CONNECTClientFactory mockCONNECTClientFactory = mock(CONNECTClientFactory.class);
    @SuppressWarnings("unchecked")
    private CONNECTClient<PatientCorrelationPortType> mockClient = mock(CONNECTClient.class);
    @SuppressWarnings("unchecked")
    private CONNECTClient<PatientCorrelationPortType> mockSecuredClient = mock(CONNECTClient.class);
    private final WebServiceProxyHelper mockWebProxy = mock(WebServiceProxyHelper.class);
    private AssertionType mockAssertion = mock(AssertionType.class);
    private PRPAIN201301UV02 mock301 = mock(PRPAIN201301UV02.class);
    private PRPAIN201309UV02 mock309 = mock(PRPAIN201309UV02.class);
    private PatientLocationQueryResponseType mockPLQrec = mock(PatientLocationQueryResponseType.class);

    @SuppressWarnings("unchecked")
    @Test
    public void testAddPatientCorrelation() throws Exception {

        PatientCorrelationProxyWebServiceUnsecuredImpl impl = getPatientCorrelationProxyWebServiceUnsecuredImpl();
        impl.addPatientCorrelation(mock301, mockAssertion);
        when(mockWebProxy.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME)).thenReturn("");
        verify(mockClient).invokePort(any(Class.class), any(String.class), any(Object.class));
        verify(mockCONNECTClientFactory).getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class),
            any(AssertionType.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testretrievePatientCorrelations() throws Exception {
        PatientCorrelationProxyWebServiceUnsecuredImpl impl = getPatientCorrelationProxyWebServiceUnsecuredImpl();
        impl.retrievePatientCorrelations(mock309, mockAssertion);
        verify(mockClient).invokePort(any(Class.class), any(String.class), any(Object.class));
        verify(mockCONNECTClientFactory).getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class),
            any(AssertionType.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddPatientCorrelationPLQ() throws Exception {

        PatientCorrelationProxyWebServiceUnsecuredImpl impl = getPatientCorrelationProxyWebServiceUnsecuredImpl();
        impl.addPatientCorrelationPLQ(mockPLQrec, mockAssertion);
        verify(mockClient).invokePort(any(Class.class), any(String.class),
            any(AddPatientCorrelationPLQRequestType.class));
        verify(mockCONNECTClientFactory).getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class),
            any(AssertionType.class));
    }

    protected PatientCorrelationProxyWebServiceUnsecuredImpl getPatientCorrelationProxyWebServiceUnsecuredImpl() {
        return new PatientCorrelationProxyWebServiceUnsecuredImpl() {

            @Override
            protected CONNECTClientFactory getCONNECTClientFactory() {
                return mockCONNECTClientFactory;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper() {
                return mockWebProxy;
            }
        };
    }

    @Before
    public void setup() {
        when(mockCONNECTClientFactory.getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class),
            any(AssertionType.class))).thenReturn(mockClient);
        when(mockCONNECTClientFactory.getCONNECTClientSecured(any(ServicePortDescriptor.class), any(String.class), any(
            AssertionType.class))).thenReturn(mockSecuredClient);
    }
}
