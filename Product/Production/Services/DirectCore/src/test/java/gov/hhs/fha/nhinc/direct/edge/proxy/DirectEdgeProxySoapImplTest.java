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
package gov.hhs.fha.nhinc.direct.edge.proxy;

import gov.hhs.fha.nhinc.direct.DirectBaseTest;
import gov.hhs.fha.nhinc.direct.DirectException;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author mweaver
 *
 */
public class DirectEdgeProxySoapImplTest extends DirectBaseTest {

    private static final String ENDPOINT_URL = "http://localhost:8080/DirectEdgeProxySoapImpl";

    private DirectEdgeProxy objectUnderTest = null;
    private DirectEdgeProxyObjectFactory factory = null;
    private CONNECTClient<DocumentRepositoryPortType> mockClient;
    private final WebServiceProxyHelper mockWebServiceProxyHelper = mock(WebServiceProxyHelper.class);

    /**
     * Setup for tests.
     */
    @Before
    public void setUp() {

        factory = new DirectEdgeProxyObjectFactory() {
            @Override
            public DirectEdgeProxy getDirectEdgeProxy() {
                return new DirectEdgeProxySoapImpl(mockWebServiceProxyHelper);
            }
        };

        objectUnderTest = factory.getDirectEdgeProxy();

    }

    /**
     * Tests if the instance is actually a soap impl as opposed to a java, smtp,
     * etc.
     */
    @Test
    public void testIsSoapEdge() {
        assert (objectUnderTest instanceof DirectEdgeProxySoapImpl);
    }

    /**
     * This test requires a mock or otherwise endpoint stood up at the url
     * defined for the directedgesoap entry in internalConnectionInfo.xml.
     *
     * @throws MessagingException on failure.
     */
    @Test(expected = DirectException.class)
    public void testSendWithNoURL() throws MessagingException {
        MimeMessage message = new MimeMessage(null, IOUtils.toInputStream(DirectUnitTestUtil
                .getFileAsString("Example_A.txt")));
        RegistryResponseType resp = objectUnderTest.provideAndRegisterDocumentSetB(message);
    }

    /**
     * Tests the DirectEdgeProxySoapImpl class with a mocked up CONNECTClient.
     * This test will ensure that the transformation from XDM to XDR was
     * successful.
     *
     * @throws Exception on failure.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSendWithMockedObjects() throws Exception {
        mockClient = mock(CONNECTClient.class);
        when(mockClient.invokePort(eq(DocumentRepositoryPortType.class),
                eq("documentRepositoryProvideAndRegisterDocumentSetB"),
                any(ProvideAndRegisterDocumentSetRequestType.class))).thenReturn(getSuccessResponse());
        when(mockWebServiceProxyHelper.getAdapterEndPointFromConnectionManager(any(String.class))).thenReturn(
                ENDPOINT_URL);

        MimeMessage message = new MimeMessage(null, IOUtils.toInputStream(DirectUnitTestUtil
                .getFileAsString("Example_A.txt")));
        objectUnderTest = getProxyWithMockClient();
        RegistryResponseType resp = objectUnderTest.provideAndRegisterDocumentSetB(message);

        assertTrue(resp.getStatus().contains("Success"));
    }

    /**
     * @return an instance of DirectEdgeProxySoapImpl with a mocked up
     * CONNECTClient.
     */
    private DirectEdgeProxySoapImpl getProxyWithMockClient() {
        return new DirectEdgeProxySoapImpl(mockWebServiceProxyHelper) {
            @Override
            protected CONNECTClient<DocumentRepositoryPortType> getClient(
                    ServicePortDescriptor<DocumentRepositoryPortType> portDescriptor, String url) {
                return mockClient;
            }

        };
    }

    /**
     * @return a RegistryResponseType with a status of success.
     */
    private RegistryResponseType getSuccessResponse() {
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
        return resp;
    }

}
