/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wss4j.policy.SPConstants;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.properties.PropertyAccessorFileUtilities;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.wss4j.dom.handler.WSHandlerConstants;


/**
 * @author akong
 *
 */
public class WsSecurityConfigFactory {

    private static final String SIGNATURE_PROPERTIES_FILENAME = "signature";
    private static final Logger LOG = LoggerFactory.getLogger(WsSecurityConfigFactory.class);
    private static WsSecurityConfigFactory INSTANCE = null;

    private PropertyAccessorFileUtilities propFileUtil = null;
    private StoreUtil cryptoStoreUtil = null;
    private HashMap<String, Object> configuration = null;

    WsSecurityConfigFactory() {
        this(new PropertyAccessorFileUtilities(), StoreUtil.getInstance());
    }

    WsSecurityConfigFactory(final PropertyAccessorFileUtilities propFileUtil, final StoreUtil cryptoStoreUtil) {
        this.propFileUtil = propFileUtil;
        this.cryptoStoreUtil = cryptoStoreUtil;
        configuration = createWSSecurityConfiguration();
    }

    /**
     * Returns a singleton instance of this factory.
     *
     * @return a singleton instance of this factory.
     */
    public static WsSecurityConfigFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WsSecurityConfigFactory();
        }

        return INSTANCE;
    }

    /**
     * Returns a cloned copy of the map containing all the necessary security configuration. This includes all the
     * properties read from the signature.properties file.
     *
     * @return
     */
    public Map<String, Object> getConfiguration() {
        return deepCopy(configuration);
    }

    private HashMap<String, Object> createWSSecurityConfiguration() {
        final HashMap<String, Object> outProps = new HashMap<>();

        outProps.put(WSHandlerConstants.ACTION, "Timestamp SAMLTokenSigned");
        outProps.put(WSHandlerConstants.TTL_TIMESTAMP, "3600");
        outProps.put(WSHandlerConstants.USER, getIssuerKeyAlias());
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, "gov.hhs.fha.nhinc.callback.cxf.CXFPasswordCallbackHandler");
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordDigest");
        outProps.put(WSHandlerConstants.SAML_CALLBACK_CLASS, "gov.hhs.fha.nhinc.callback.cxf.CXFSAMLCallbackHandler");
        outProps.put("cryptoProperties", getSignatureProperties());
        outProps.put(WSHandlerConstants.SIG_PROP_REF_ID, "cryptoProperties");
        outProps.put(WSHandlerConstants.SIG_ALGO, SPConstants.RSA_SHA1);
        outProps.put(WSHandlerConstants.SIG_DIGEST_ALGO, SPConstants.SHA1);

        outProps.put(WSHandlerConstants.SIGNATURE_PARTS,
                "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;");

        return outProps;
    }

    private String getIssuerKeyAlias() {
        return cryptoStoreUtil.getPrivateKeyAlias();
    }

    private Properties getSignatureProperties() {
        return propFileUtil.loadPropertyFile(SIGNATURE_PROPERTIES_FILENAME);
    }

    private Map<String, Object> deepCopy(final HashMap<String, Object> configMap) {
        final HashMap<String, Object> clonedMap = new HashMap<>(configMap);

        final Properties cryptoProperties = (Properties) clonedMap.get("cryptoProperties");
        clonedMap.put("cryptoProperties", cryptoProperties.clone());

        return clonedMap;
    }

}
