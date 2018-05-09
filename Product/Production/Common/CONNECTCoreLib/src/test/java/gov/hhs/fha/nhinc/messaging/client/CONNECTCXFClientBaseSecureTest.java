/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.faults.ConfigurationException;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class CONNECTCXFClientBaseSecureTest {

    @Test
    public void testHttpsClientDisableCNCheck() {
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.DISABLE_CN_CHECK, "true");
        } catch (PropertyAccessException ex) {
            System.out.println("Unable to set in memory property for disable-CN-Check.");
        }

        String url = "https://url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> endpoint = null;
        try {
            endpoint = createClient(url, assertion);

            Client client = ClientProxy.getClient(endpoint.getPort());

            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            TLSClientParameters tlsPara = conduit.getTlsClientParameters();

            Assert.assertTrue("disableCNCheck==true", tlsPara.isDisableCNCheck());
        } catch (ConfigurationException ex) {
            System.out.println("catching ConfigurationException-https-client required https-protocol.");
        }
    }

    @Test
    public void testHttpsClientRequiredHttps() {
        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.DISABLE_CN_CHECK, "false");
        } catch (PropertyAccessException ex) {
            System.out.println("Unable to set in memory property for disable-CN-Check.");
        }

        String url = "http://url";
        AssertionType assertion = new AssertionType();
        assertion.setTransactionTimeout(-1);

        CONNECTClient<TestServicePortType> client = null;
        ConfigurationException expected = null;
        try {
            client = createClient(url, assertion);
        } catch (ConfigurationException ex) {
            expected = ex;
        }
        Assert.assertNotNull("https-required expected--ConfigurationException", expected);
    }

    private CONNECTClient<TestServicePortType> createClient(String url, AssertionType assertion)
        throws ConfigurationException {
        return CONNECTClientFactory.getInstance().getCONNECTClientHttps(new TestServicePortDescriptor(), url,
            assertion);
    }

}
