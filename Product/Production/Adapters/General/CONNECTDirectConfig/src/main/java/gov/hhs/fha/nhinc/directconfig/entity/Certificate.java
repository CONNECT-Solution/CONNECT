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
/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.directconfig.entity;

import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.Thumbprint;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The JPA Certificate class
 */
public class Certificate {

    private static final String DEFAULT_JCE_PROVIDER_STRING = "BC";
    private static final String JCE_PROVIDER_STRING_SYS_PARAM = "org.nhindirect.config.JCEProviderName";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Gets the configured JCE crypto provider string for crypto operations. This is configured using the
     * -Dorg.nhindirect.config.JCEProviderName JVM parameters. If the parameter is not set or is empty, then the default
     * string "BC" (BouncyCastle provider) is returned. By default the agent installs the BouncyCastle provider.
     *
     * @return The name of the JCE provider string.
     */
    public static String getJCEProviderName() {
        String retVal = System.getProperty(JCE_PROVIDER_STRING_SYS_PARAM);

        if (retVal == null || retVal.isEmpty()) {
            retVal = DEFAULT_JCE_PROVIDER_STRING;
        }

        return retVal;
    }

    /**
     * Overrides the configured JCE crypto provider string. If the name is empty or null, the default string "BC"
     * (BouncyCastle provider) is used.
     *
     * @param name The name of the JCE provider.
     */
    public static void setJCEProviderName(String name) {
        if (name == null || name.isEmpty()) {
            System.setProperty(JCE_PROVIDER_STRING_SYS_PARAM, DEFAULT_JCE_PROVIDER_STRING);
        } else {
            System.setProperty(JCE_PROVIDER_STRING_SYS_PARAM, name);
        }
    }

    private static final Log LOG = LogFactory.getLog(Certificate.class);

    private static final byte[] NULL_CERT = new byte[] {};

    /**
     * Getter to encapsulate NULL_CERT constant array
     *
     * @return null cert (empty byte array)
     */
    public static byte[] getNullCert() {
        return NULL_CERT;
    }

    private String owner;
    private String thumbprint;
    private Long id;
    private byte[] data;
    private Calendar createTime;
    private Calendar validStartDate;
    private Calendar validEndDate;
    private EntityStatus status;
    private boolean privateKey;

    /**
     * Construct a Certificate.
     */
    public Certificate() {
    }

    /**
     * Get the value of owner.
     *
     * @return the value of owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the value of owner.
     *
     * @param owner The value of owner.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the value of data.
     *
     * @return the value of data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Set the value of data.
     *
     * @param data The value of data.
     * @throws CertificateException
     */
    public void setData(byte[] data) throws CertificateException {
        this.data = data;

        if (data == NULL_CERT) {
            setThumbprint("");
        } else {
            loadCertFromData();
        }
    }

    /**
     * Indicates if the certificate has a private key
     *
     * @return
     */
    public boolean isPrivateKey() {
        return privateKey;
    }

    /**
     * Indicates if the certificate has a private key
     *
     * @param data
     *
     * @throws CertificateException
     */
    public void setPrivateKey(boolean b) throws CertificateException {
        privateKey = b;
    }

    private void setThumbprint(String aThumbprint) {
        thumbprint = aThumbprint;
    }

    /**
     * Get the value of thumbprint.
     *
     * @return the value of thumbprint.
     */
    public String getThumbprint() {
        return thumbprint;
    }

    /**
     * Get the value of id.
     *
     * @return the value of id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the value of id.
     *
     * @param id The value of id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the value of createTime.
     *
     * @return the value of createTime.
     */
    public Calendar getCreateTime() {
        return createTime;
    }

    /**
     * Set the value of createTime.
     *
     * @param timestamp The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) {
        createTime = timestamp;
    }

    /**
     * Get the value of status.
     *
     * @return the value of status.
     */
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status.
     *
     * @param status The value of status.
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    /**
     * Get the value of validStartDate.
     *
     * @return the value of validStartDate.
     */
    public Calendar getValidStartDate() {
        return validStartDate;
    }

    /**
     * Set the value of validStartDate.
     *
     * @param validStartDate The value of validStartDate.
     */
    public void setValidStartDate(Calendar validStartDate) {
        this.validStartDate = validStartDate;
    }

    /**
     * Get the value of validEndDate.
     *
     * @return the value of validEndDate.
     */
    public Calendar getValidEndDate() {
        return validEndDate;
    }

    /**
     * Set the value of validEndDate.
     *
     * @param validEndDate The value of validEndDate.
     */
    public void setValidEndDate(Calendar validEndDate) {
        this.validEndDate = validEndDate;
    }

    /**
     * Validate the Certificate for the existance of data.
     *
     * @throws CertificateException
     */
    public void validate() throws CertificateException {
        if (!hasData()) {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    }

    private boolean hasData() {
        return data != null && !Arrays.equals(data, Certificate.NULL_CERT);
    }

    /**
     * Clear the data of a Certificate.
     */
    public void clearData() {
        try {
            setData(NULL_CERT);
        } catch (CertificateException e) {
            LOG.warn("Could not clear certificate data: " + e.getLocalizedMessage(), e);
        }
    }

    private void loadCertFromData() throws CertificateException {
        X509Certificate cert = null;
        CertContainer container = null;
        Key key = null;

        try {
            validate();

            try {
                container = toCredential();
                cert = container.getCert();
                key = container.getKey();
            } catch (CertificateException e) {
                LOG.warn("Cert Container conversion failed: " + e.getLocalizedMessage(), e);
            }

            if (cert == null) {
                setThumbprint("");
            } else {
                setThumbprint(Thumbprint.toThumbprint(cert).toString());
                setPrivateKey(key != null);
            }
        } catch (Exception e) {
            setData(NULL_CERT);
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate or IPKIX URL", e);
        }
    }

    public CertContainer toCredential() throws CertificateException {
        CertContainer certContainer = null;

        try {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            // lets try this a as a PKCS12 data stream first
            try {
                KeyStore localKeyStore = KeyStore.getInstance("PKCS12", getJCEProviderName());

                localKeyStore.load(bais, "".toCharArray());
                Enumeration<String> aliases = localKeyStore.aliases();

                // we are really expecting only one alias
                if (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    X509Certificate cert = (X509Certificate) localKeyStore.getCertificate(alias);

                    // check if there is private key
                    Key key = localKeyStore.getKey(alias, "".toCharArray());
                    if (key != null && key instanceof PrivateKey) {
                        certContainer = new CertContainer(cert, key);
                    }
                }
            } catch (Exception e) {
                // must not be a PKCS12 stream, go on to next step
                LOG.warn("Not a PKCS12 stream: " + e.getLocalizedMessage(), e);
            }

            if (certContainer == null) {
                // try X509 certificate factory next
                bais.reset();
                bais = new ByteArrayInputStream(data);

                X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509")
                        .generateCertificate(bais);
                certContainer = new CertContainer(cert, null);
            }
            bais.close();
        } catch (CertificateException | java.security.cert.CertificateException | IOException e) {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }

        return certContainer;
    }

    public static class CertContainer {

        private final X509Certificate cert;
        private final Key key;

        public CertContainer() {
            cert = null;
            key = null;
        }

        public CertContainer(X509Certificate cert, Key key) {
            this.cert = cert;
            this.key = key;
        }

        public X509Certificate getCert() {
            return cert;
        }

        public Key getKey() {
            return key;
        }
    }
}
