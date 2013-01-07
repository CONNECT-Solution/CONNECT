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

import org.apache.ws.security.handler.WSHandlerConstants;
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
        when(cryptoStoreUtil.getPrivateKeyAlias()).thenReturn("gateway");
    }
    
    @Test
    public void verifyProperties() {
        WsSecurityConfigFactory configFactory = new WsSecurityConfigFactory(propFileUtil, cryptoStoreUtil);
        
        Map<String, Object> configMap = configFactory.getConfiguration();
        
        verifyWsSecurityProperties(configMap);
    }
    
    @Test
    public void verifyClone() {        
        WsSecurityConfigFactory configFactory = new WsSecurityConfigFactory(propFileUtil, cryptoStoreUtil);
        
        Map<String, Object> configMap1 = configFactory.getConfiguration();
        configMap1.remove(WSHandlerConstants.PASSWORD_TYPE);
        ((Properties) configMap1.get("cryptoProperties")).put("keyTest", "valueTest");
        
        Map<String, Object> configMap2 = configFactory.getConfiguration();
        assertEquals("PasswordDigest", configMap2.get(WSHandlerConstants.PASSWORD_TYPE));
        assertNull(((Properties) configMap2.get("cryptoProperties")).get("keyTest"));        
    }
    
    public void verifyWsSecurityProperties(Map<String, Object> properties) {
        assertEquals("Timestamp SAMLTokenSigned", properties.get(WSHandlerConstants.ACTION));
        assertEquals("3600", properties.get(WSHandlerConstants.TTL_TIMESTAMP));
        assertEquals("gateway", properties.get(WSHandlerConstants.USER));
        assertEquals("gov.hhs.fha.nhinc.callback.cxf.CXFPasswordCallbackHandler",
                properties.get(WSHandlerConstants.PW_CALLBACK_CLASS));
        assertEquals("PasswordDigest", properties.get(WSHandlerConstants.PASSWORD_TYPE));
        assertEquals("saml.properties", properties.get(WSHandlerConstants.SAML_PROP_FILE));
        assertNotNull("cryptoProperties", properties.get("cryptoProperties"));
        assertEquals("cryptoProperties", properties.get(WSHandlerConstants.SIG_PROP_REF_ID));
        assertEquals("http://www.w3.org/2000/09/xmldsig#rsa-sha1", properties.get(WSHandlerConstants.SIG_ALGO));
        assertEquals("http://www.w3.org/2000/09/xmldsig#sha1", properties.get(WSHandlerConstants.SIG_DIGEST_ALGO));
        assertEquals(
                "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;",
                properties.get(WSHandlerConstants.SIGNATURE_PARTS));
    }

}
