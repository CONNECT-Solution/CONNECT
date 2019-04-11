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
package gov.hhs.fha.nhinc.cryptostore;

import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to help managing cryptographic key and trust stores.
 *
 * @author msw
 *
 */
public class StoreUtil {

    private static final Logger LOG = LoggerFactory.getLogger(StoreUtil.class);
    private static Map<String, String> gatewayAliasMapping = new HashMap<>();

    private StoreUtil() {

    }

    /**
     * Gets the private key alias used for digital signatures. The method will return the value of the system property
     * defined in CertificateManager.CLIENT_KEY_ALIAS or the default alias defined in
     * CertificateManager.DEFAULT_CLIENT_KEY_ALIAS.
     *
     * @return String containing the private key alias.
     */
    public static String getPrivateKeyAlias() {
        String alias = System.getProperty(NhincConstants.CLIENT_KEY_ALIAS);
        return StringUtils.isBlank(alias) ? NhincConstants.DEFAULT_CLIENT_KEY_ALIAS : alias;
    }

    public static String getGatewayAliasDefaultTo(String overrideAlias) {
        if (StringUtils.isNotBlank(overrideAlias)) {
            return overrideAlias;
        }

        return getPrivateKeyAlias();
    }

    public static void addGatewayCertificateAlias(String exchangeName, String alias) {
        if (!StringUtils.equalsIgnoreCase(InternalExchangeManager.getInstance().getDefaultExchange(), exchangeName)
            && !ArrayUtils.contains(new String[] { exchangeName, alias }, null)) {
            gatewayAliasMapping.put(exchangeName, alias);
        }
    }

    public static String getGatewayCertificateAlias(String exchangeName) {
        LOG.debug("Get Certification for exchange {}", exchangeName);
        if (!StringUtils.equalsIgnoreCase(InternalExchangeManager.getInstance().getDefaultExchange(), exchangeName)) {
            String gatewayAlias = gatewayAliasMapping.get(exchangeName);
            if(StringUtils.isNotBlank(gatewayAlias)){
                LOG.debug("found exchange mapping for '{}' to use '{}' JKS alias", exchangeName, gatewayAlias);
                return gatewayAlias;
            }
        }
        return getPrivateKeyAlias();
    }
}
