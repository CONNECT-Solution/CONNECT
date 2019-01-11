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
package gov.hhs.fha.nhinc.admingui.managed.direct.helpers;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmay
 */
public class CertContainer {

    private X509Certificate cert;
    private Key key;
    private static final Logger LOG = LoggerFactory.getLogger(CertContainer.class);

    /**
     *
     * @param cert
     * @param key
     */
    public CertContainer(X509Certificate cert, Key key) {
        this.cert = cert;
        this.key = key;
    }

    /**
     *
     * @param data
     */
    public CertContainer(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        // Trying a as a PKCS12 data stream
        try {
            KeyStore localKeyStore = KeyStore.getInstance("PKCS12", "BC");

            localKeyStore.load(bais, "".toCharArray());
            Enumeration<String> aliases = localKeyStore.aliases();

            // we are really expecting only one alias
            if (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                X509Certificate cert = (X509Certificate) localKeyStore.getCertificate(alias);

                // check if there is private key
                Key key = localKeyStore.getKey(alias, "".toCharArray());
                if (key != null && key instanceof PrivateKey) {
                    this.cert = cert;
                    this.key = key;
                }
            }
        } catch (Exception e) {
            LOG.trace("Error during cert conversion: {}", e.getLocalizedMessage(), e);
            LOG.warn("Cert data is not a PKCS12 stream, trying next option...");
        }

        // Trying as an X.509 certificate
        if (cert == null) {
            bais.reset();
            bais = new ByteArrayInputStream(data);

            try {
                cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
                key = null;
            } catch (Exception e) {
                LOG.trace("Error during cert conversion: {}", e.getLocalizedMessage(), e);
                LOG.warn("Cert data cannot be converted to X.509, trying next option...");
            }
        }

        if (cert == null) {
            LOG.error("Could not parse cert data");
        }

        try {
            bais.close();
        } catch (Exception e) {
            LOG.error("Could not close input stream: {}", e.getLocalizedMessage(), e);
        }
    }

    /**
     *
     * @return
     */
    public X509Certificate getCert() {
        return cert;
    }

    /**
     *
     * @return
     */
    public Key getKey() {
        return key;
    }

    /**
     *
     * @return
     */
    public String getTrustedEntityName() {
        X500Principal prin = cert.getSubjectX500Principal();

        // check the CN attribute first
        // get the domain name
        Map<String, String> oidMap = new HashMap<>();
        oidMap.put("1.2.840.113549.1.9.1", "EMAILADDRESS"); // OID for email address
        String prinName = prin.getName(X500Principal.RFC1779, oidMap);

        String searchString = "CN=";
        int index = prinName.indexOf(searchString);
        if (index == -1) {
            searchString = "EMAILADDRESS=";
            // fall back to email
            index = prinName.indexOf(searchString);
            if (index == -1) {
                return ""; // no CN... nothing else that can be done from here
            }
        }

        // look for a "," to find the end of this attribute
        int endIndex = prinName.indexOf(",", index);
        String address;
        if (endIndex > -1) {
            address = prinName.substring(index + searchString.length(), endIndex);
        } else {
            address = prinName.substring(index + searchString.length());
        }

        return address;
    }

    /**
     *
     * @return
     */
    public String getThumbprint() {
        String thumbprint;

        try {
            thumbprint = DigestUtils.sha1Hex(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            LOG.error("Could not encode certificate: ", e.getLocalizedMessage(), e);
            thumbprint = null;
        }

        return thumbprint;
    }
}
