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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.properties.PropertyAccessorFileUtilities;
import java.util.Map;
import java.util.Properties;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.junit.Before;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class WsSecurityConfigFactoryTest {

    Properties sigProperties;
    PropertyAccessorFileUtilities propFileUtil;
    StoreUtil cryptoStoreUtil;

    @Before
    public void setup() {
        sigProperties = new Properties();
        propFileUtil = mock(PropertyAccessorFileUtilities.class);
        cryptoStoreUtil = mock(StoreUtil.class);

        when(propFileUtil.loadPropertyFile("signature")).thenReturn(sigProperties);

    }

    @Test
    public void verifyProperties() {
        final WsSecurityConfigFactory configFactory = new WsSecurityConfigFactory(propFileUtil);

        final Map<String, Object> configMap = configFactory.getConfiguration(null);
        verifyWsSecurityProperties(configMap);
    }

    @Test
    public void verifyClone() {
        final WsSecurityConfigFactory configFactory = new WsSecurityConfigFactory(propFileUtil);

        final Map<String, Object> configMap1 = configFactory.getConfiguration(null);
        configMap1.remove(WSHandlerConstants.PASSWORD_TYPE);
        ((Properties) configMap1.get("cryptoProperties")).put("keyTest", "valueTest");

        final Map<String, Object> configMap2 = configFactory.getConfiguration(null);
        assertEquals("PasswordDigest", configMap2.get(WSHandlerConstants.PASSWORD_TYPE));
        assertNull(((Properties) configMap2.get("cryptoProperties")).get("keyTest"));
    }

    public void verifyWsSecurityProperties(final Map<String, Object> properties) {
        assertEquals("Timestamp SAMLTokenSigned", properties.get(WSHandlerConstants.ACTION));
        assertEquals("3600", properties.get(WSHandlerConstants.TTL_TIMESTAMP));
        assertEquals("gateway", properties.get(WSHandlerConstants.USER));
        assertEquals("gov.hhs.fha.nhinc.callback.cxf.CXFPasswordCallbackHandler",
            properties.get(WSHandlerConstants.PW_CALLBACK_CLASS));
        assertEquals("PasswordDigest", properties.get(WSHandlerConstants.PASSWORD_TYPE));
        assertNotNull("cryptoProperties", properties.get("cryptoProperties"));
        assertEquals("cryptoProperties", properties.get(WSHandlerConstants.SIG_PROP_REF_ID));
        assertEquals(
            "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;",
            properties.get(WSHandlerConstants.SIGNATURE_PARTS));
    }

}
