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
package gov.hhs.fha.nhinc.callback.opensaml;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.SHA_TYPE;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SAMLUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SAMLUtils.class);
    private static final String DIG_ALGO = "saml.DigestAlgorithm";
    private static final String SIG_ALGO = "saml.SignatureAlgorithm";
    /**
     * @return
     */
    public static String getDigestAlgorithm() {
        return PropertyAccessor.getInstance().getProperty(NhincConstants.ASSERTION_INFO_PROPERTY_FILE, DIG_ALGO,
            SignatureConstants.ALGO_ID_DIGEST_SHA1);
    }
    /**
     * @return
     */
    public static String getSignatureAlgorithm() {
        return PropertyAccessor.getInstance().getProperty(NhincConstants.ASSERTION_INFO_PROPERTY_FILE, SIG_ALGO,
            SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
    }

    public String getResolvedNamespace(String uri) {
        String resolved = null;
        try {
            SignatureConstants.class.getDeclaredField(uri);
        } catch (NoSuchFieldException e) {
            // Not a constant value, is it a manual namespace?
            if (uri.indexOf("http://") == 0) {
                resolved = uri;
            } else {
                LOG.error("Could not resove SAML algorithm: {}. Not a URL or constant field found in org.opensaml.xmlsec.signature.support.SignatureConstants.class",uri, e);
                throw new SAMLAssertionBuilderException(e.getMessage());
            }
        } catch (SecurityException e) {
            LOG.error("Could not resolve SAML algorithm: {}",uri, e);
            throw new SAMLAssertionBuilderException(e.getMessage());
        }

        return resolved;
    }

    public List<SHA_TYPE> getConfigurableSHA() {
        String configurableProperty = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
            NhincConstants.CONFIG_SHA, SHA_TYPE.SHA1.name());
        List<String> configurablePropertyList = Arrays.asList(StringUtils.split(configurableProperty, ","));
        List<SHA_TYPE> SHATypeList = new ArrayList<>();
        for (String sha : configurablePropertyList) {
            SHA_TYPE shaType = SHA_TYPE.getSHAType(sha);
            if (shaType != null) {
                SHATypeList.add(shaType);
            }
        }
        return SHATypeList;

    }
}
