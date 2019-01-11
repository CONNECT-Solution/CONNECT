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
package gov.hhs.fha.nhinc.directconfig.service.impl;

import gov.hhs.fha.nhinc.directconfig.dao.CertificateDao;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.EntityStatus;
import gov.hhs.fha.nhinc.directconfig.service.CertificateService;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.helpers.CertificateGetOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Service class for methods related to a Certificate object.
 */
@Service("certSvc")
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.CertificateService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
public class CertificateServiceImpl extends SpringBeanAutowiringSupport implements CertificateService {

    private static final int RFC822Name_TYPE = 1; // name type constant for Subject Alternative name email address
    private static final int DNSName_TYPE = 2; // name type constant for Subject Alternative name domain name

    private static final Log LOG = LogFactory.getLog(CertificateServiceImpl.class);

    @Autowired
    private CertificateDao dao;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Initialization method.
     */
    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        LOG.info("CertificateService initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCertificates(Collection<Certificate> certs) throws ConfigurationServiceException {
        if (CollectionUtils.isNotEmpty(certs)) {
            for (Certificate cert : certs) {
                if (StringUtils.isEmpty(cert.getOwner()) && cert.getData() != null) {
                    // get the owner from the certificate information
                    // first transform into a certificate
                    CertContainer cont = toCertContainer(cert.getData());
                    if (cont != null && cont.getCert() != null) {
                        // now get the owner info from the cert
                        String theOwner = getOwner(cont.getCert());

                        if (StringUtils.isNotEmpty(theOwner)) {
                            cert.setOwner(theOwner);
                        }
                    }

                }

                dao.save(cert);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Certificate getCertificate(String owner, String thumbprint, CertificateGetOptions options)
        throws ConfigurationServiceException {

        return dao.load(owner, thumbprint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Certificate> getCertificates(Collection<Long> certIds, CertificateGetOptions options)
        throws ConfigurationServiceException {

        return dao.list(new ArrayList<>(certIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Certificate> getCertificatesForOwner(String owner, CertificateGetOptions options)
        throws ConfigurationServiceException {

        return dao.list(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCertificateStatus(Collection<Long> certificateIDs, EntityStatus status)
        throws ConfigurationServiceException {

        dao.setStatus(new ArrayList<>(certificateIDs), status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCertificateStatusForOwner(String owner, EntityStatus status) throws ConfigurationServiceException {
        dao.setStatus(owner, status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCertificates(Collection<Long> certificateIds) throws ConfigurationServiceException {
        dao.delete(new ArrayList<>(certificateIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCertificatesForOwner(String owner) throws ConfigurationServiceException {
        dao.delete(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Certificate> listCertificates(long lastCertificateID, int maxResults,
        CertificateGetOptions options) throws ConfigurationServiceException {

        // Direct RI comment: just return all for now
        return dao.list((String) null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Certificate cert) {
        return dao.load(cert.getOwner(), cert.getThumbprint()) != null;
    }

    public CertContainer toCertContainer(byte[] data) throws ConfigurationServiceException {
        CertContainer certContainer = null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            // lets try this a as a PKCS12 data stream first
            try {
                KeyStore localKeyStore = KeyStore.getInstance("PKCS12", Certificate.getJCEProviderName());

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
                LOG.warn("Not a PKCS12 stream: " + e.getLocalizedMessage());
                LOG.trace("Not a PKCS12 stream: " + e.getLocalizedMessage(), e);
            }

            if (certContainer == null) {
                // try X509 certificate factory next
                bais.reset();
                bais = new ByteArrayInputStream(data);

                X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
                    bais);
                certContainer = new CertContainer(cert, null);
            }
            bais.close();
        } catch (CertificateException | IOException e) {
            throw new ConfigurationServiceException("Data cannot be converted to a valid X.509 Certificate", e);
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

    private String getOwner(X509Certificate certificate) {
        String address = "";
        // check alternative names first
        Collection<List<?>> altNames = null;

        try {
            altNames = certificate.getSubjectAlternativeNames();
        } catch (CertificateParsingException ex) {
            /* no -op */
            LOG.warn("Could not get Subject Alternative Names: " + ex.getLocalizedMessage());
            LOG.trace("Could not get Subject Alternative Names: " + ex.getLocalizedMessage(), ex);
        }

        if (altNames != null) {
            for (List<?> entries : altNames) {
                // should always be the case according the altNames spec, but checking to be defensive
                if (entries.size() >= 2) {
                    Integer nameType = (Integer) entries.get(0);

                    // prefer email over over domain?
                    if (nameType == RFC822Name_TYPE) {
                        address = (String) entries.get(1);
                    } else if (nameType == DNSName_TYPE && address.isEmpty()) {
                        address = (String) entries.get(1);
                    }
                }
            }
        }

        if (!address.isEmpty()) {
            return address;
        }

        // can't find subject address in alt names... try the principal
        X500Principal issuerPrin = certificate.getSubjectX500Principal();

        // get the domain name
        Map<String, String> oidMap = new HashMap<>();
        oidMap.put("1.2.840.113549.1.9.1", "EMAILADDRESS"); // OID for email address
        String prinName = issuerPrin.getName(X500Principal.RFC1779, oidMap);

        // see if there is an email address first in the DN
        String searchString = "EMAILADDRESS=";
        int index = prinName.indexOf(searchString);

        if (index == -1) {
            searchString = "CN=";

            // no Email.. check the CN
            index = prinName.indexOf(searchString);

            if (index == -1) {
                return ""; // no CN... nothing else that can be done from here
            }
        }

        // look for a "," to find the end of this attribute
        int endIndex = prinName.indexOf(",", index);

        if (endIndex > -1) {
            address = prinName.substring(index + searchString.length(), endIndex);
        } else {
            address = prinName.substring(index + searchString.length());
        }

        return address;
    }
}
