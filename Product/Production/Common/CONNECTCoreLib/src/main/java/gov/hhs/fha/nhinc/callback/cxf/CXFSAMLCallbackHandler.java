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
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.callback.opensaml.CallbackMapProperties;
import gov.hhs.fha.nhinc.callback.opensaml.CallbackProperties;
import gov.hhs.fha.nhinc.callback.opensaml.HOKSAMLAssertionBuilder;
import gov.hhs.fha.nhinc.callback.opensaml.SAMLAssertionBuilderException;
import gov.hhs.fha.nhinc.callback.opensaml.SAMLUtils;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.io.IOException;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SAMLCallback;
import org.apache.wss4j.common.saml.bean.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mweaver
 *
 */
public class CXFSAMLCallbackHandler implements CallbackHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CXFSAMLCallbackHandler.class);
    private PropertyAccessor accessor;
    public static final String HOK_CONFIRM = "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
    private HOKSAMLAssertionBuilder builder = new HOKSAMLAssertionBuilder();
    private Crypto issuerCrypto = null;

    public CXFSAMLCallbackHandler() {
        accessor = PropertyAccessor.getInstance();
    }

    public CXFSAMLCallbackHandler(final HOKSAMLAssertionBuilder builder) {
        this();
        this.builder = builder;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        LOG.trace("CXFSAMLCallbackHandler.handle begin");

        for (final Callback callback : callbacks) {
            if (callback instanceof SAMLCallback) {

                try {

                    final Message message = getCurrentMessage();
                    AssertionType custAssertion = getCustAssertion(message.get("assertion"));
                    NhinTargetSystemType systemType = (NhinTargetSystemType) message.get("targetSystemType");
                    String exchangeName = systemType != null ? systemType.getExchangeName() : InternalExchangeManager
                        .getInstance().getDefaultExchange();
                    LOG.trace("Is this exchange {} default for building assertion? {}", exchangeName,
                        systemType != null);
                    final SAMLCallback oSAMLCallback = (SAMLCallback) callback;

                    oSAMLCallback.setIssuerKeyName(
                        accessor.getProperty(NhincConstants.SAML_PROPERTY_FILE, SamlConstants.ISSUER_KEY_STRING));
                    oSAMLCallback.setIssuerKeyPassword(
                        accessor.getProperty(NhincConstants.SAML_PROPERTY_FILE, SamlConstants.ISSUER_KEY_VALUE));
                    oSAMLCallback.setSignAssertion(accessor.getPropertyBoolean(NhincConstants.SAML_PROPERTY_FILE,
                        SamlConstants.SIGN_ASSERTION_BOOL));
                    oSAMLCallback.setSendKeyValue(accessor.getPropertyBoolean(NhincConstants.SAML_PROPERTY_FILE,
                        SamlConstants.SEND_KEYVALUE_BOOL));
                    issuerCrypto = CryptoFactory.getInstance(accessor.getProperty(NhincConstants.SAML_PROPERTY_FILE,
                        SamlConstants.SIGNATURE_PROPERTIES_STRING));
                    oSAMLCallback.setIssuerCrypto(issuerCrypto);
                    oSAMLCallback.setSamlVersion(Version.SAML_20);

                    oSAMLCallback.setSignatureAlgorithm(SAMLUtils.extractSignatureFromAssertion(custAssertion));
                    oSAMLCallback.setSignatureDigestAlgorithm(SAMLUtils.extractDigestFromAssertion(custAssertion));

                    final SamlTokenCreator creator = new SamlTokenCreator();
                    final CallbackProperties properties = new CallbackMapProperties(addMessageProperties(
                        creator.createRequestContext(custAssertion, getResource(message), null), message));
                    oSAMLCallback.setAssertionElement(builder.build(properties, StoreUtil.getGatewayCertificateAlias(exchangeName)));
                } catch (WSSecurityException | PropertyAccessException e) {
                    LOG.error("Failed to create saml: {}", e.getLocalizedMessage(), e);
                    throw new SAMLAssertionBuilderException(e.getMessage(), e);
                }
            }
        }

        LOG.trace("CXFSAMLCallbackHandler.handle end");


    }

    /**
     * Return custAssertion
     *
     * @param obj
     * @return
     */
    protected static AssertionType getCustAssertion(Object obj) {
        return obj != null ? (AssertionType) obj : null;
    }

    /**
     * Populate Callback Properties with additional properties set on the message.
     *
     * @param propertiesMap to be appended.
     * @param message source of additional properties.
     * @return map containing assertion data and additional properties.
     */
    private static Map<String, Object> addMessageProperties(final Map<String, Object> propertiesMap,
        final Message message) {

        addPropertyFromMessage(propertiesMap, message, NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID);
        addPropertyFromMessage(propertiesMap, message, SamlConstants.TARGET_API_LEVEL);
        addPropertyFromMessage(propertiesMap, message, SamlConstants.ACTION_PROP);
        return propertiesMap;
    }

    private static void addPropertyFromMessage(final Map<String, Object> propertiesMap, final Message message,
        final String key) {
        propertiesMap.put(key, message.get(key));
    }

    protected Message getCurrentMessage() {
        return PhaseInterceptorChain.getCurrentMessage();
    }

    protected String getResource(final Message message) {
        String resource = null;
        try {
            final boolean isInbound = (Boolean) message.get(Message.INBOUND_MESSAGE);
            if (!isInbound) {
                resource = (String) message.get(Message.ENDPOINT_ADDRESS);
            }
        } catch (final Exception e) {
            LOG.error("Get resource exception: {}", e.getLocalizedMessage(), e);
        }
        return resource;
    }
}
