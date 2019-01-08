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
package gov.hhs.fha.nhinc.callback.opensaml;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SAMLUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SAMLUtils.class);

    private SAMLUtils() {

    }


    public static String getDigestAlgorithm() {
        String algo = PropertyAccessor.getInstance().getProperty(NhincConstants.SAML_PROPERTY_FILE, SamlConstants.DEFAULT_DIG_ALGO_PROPERTY,
            SignatureConstants.ALGO_ID_DIGEST_SHA1);

        return constructNamespace(algo);
    }

    public static String getSignatureAlgorithm() {
        String algo = PropertyAccessor.getInstance().getProperty(NhincConstants.SAML_PROPERTY_FILE, SamlConstants.DEFAULT_SIG_ALGO_PROPERTY,
            SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);

        return constructNamespace(algo);
    }


    private static String constructNamespace(String uri) {
        try {
            Field field = SignatureConstants.class.getDeclaredField(uri);
            return (String) field.get(null);
        } catch (NoSuchFieldException e) {
            // Not a constant value, is it a manual namespace?
            if (uri.indexOf("http://") == 0) {
                return uri;
            } else {
                LOG.error("Could not resove SAML algorithm: {}. Not a URL or constant field found in org.opensaml.xmlsec.signature.support.SignatureConstants",uri, e);
                throw new SAMLAssertionBuilderException(uri + " is not a valid algorithm");
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            LOG.error("Could not resolve SAML algorithm: {}",uri, e);
            throw new SAMLAssertionBuilderException(e.getMessage());
        }
    }

    private static List<String> constructNamespaceList(String[] uriArray) {
        LinkedList<String> list = new LinkedList<>();

        for (String uri : uriArray) {
            list.add(constructNamespace(uri));
        }
        return list;
    }

    /**
     * Gets the configuration for SAML Signature and Digest algorithms.
     *
     * The configuration takes in a comma separated list of either the constant string from SignatureConstants
     * or the full URI of the requested algorithm.
     */
    public static Map<String, List<String>> getConfigurableSHA() {
        String digestAlgorithms = PropertyAccessor.getInstance().getProperty(NhincConstants.SAML_PROPERTY_FILE,
            SamlConstants.DIG_ALGO_PROPERTY, SignatureConstants.ALGO_ID_DIGEST_SHA1);

        String signatureAlgorithms = PropertyAccessor.getInstance().getProperty(NhincConstants.SAML_PROPERTY_FILE,
            SamlConstants.SIG_ALGO_PROPERTY, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);

        HashMap<String, List<String>> configSHA = new HashMap<>();
        configSHA.put(SamlConstants.DIGEST_KEY, constructNamespaceList(StringUtils.split(digestAlgorithms, ",")));
        configSHA.put(SamlConstants.SIGNATURE_KEY, constructNamespaceList(StringUtils.split(signatureAlgorithms, ",")));

        return configSHA;

    }

    public static String extractSignatureFromAssertion(AssertionType assertion) {
        return assertion != null && StringUtils.isNotBlank(assertion.getSignatureAlgorithm()) ?  assertion.getSignatureAlgorithm() : getSignatureAlgorithm();
    }

    public static String extractDigestFromAssertion(AssertionType assertion) {
        return assertion != null && StringUtils.isNotBlank(assertion.getDigestAlgorithm()) ?  assertion.getDigestAlgorithm() : getDigestAlgorithm();
    }
}
