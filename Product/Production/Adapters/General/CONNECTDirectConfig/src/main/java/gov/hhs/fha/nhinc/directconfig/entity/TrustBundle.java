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

import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 * JPA entity object for a trust bundle
 *
 * @author Greg Meyer
 * @since 1.2
 */
public class TrustBundle {

    private Long id;
    private String bundleName;
    private String bundleURL;
    private byte[] signingCertificateData;
    private Collection<TrustBundleAnchor> trustBundleAnchors;
    private int refreshInterval;
    private Calendar lastRefreshAttempt;
    private BundleRefreshError lastRefreshError;
    private Calendar lastSuccessfulRefresh;
    private Calendar createTime;
    private String checkSum;
    private Collection<TrustBundleDomainReltn> relations;

    /**
     * Construct a TrustBundle.
     */
    public TrustBundle() {
        refreshInterval = 0;
        checkSum = "";
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
     * Gets the value of the bundle name. The bundle name must be unique
     *
     * @return the value of the bundle name
     */
    public String getBundleName() {
        return bundleName;
    }

    /**
     * Set the value of the bundle name.
     *
     * @param bundleName The value of the bundleName
     */
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    /**
     * Gets the value of the bundle URL. The URL specifies the location of the bundle
     *
     * @return the value of the bundle URL
     */
    public String getBundleURL() {
        return bundleURL;
    }

    /**
     * Set the value of the bundle url.
     *
     * @param bundleURL The value of the bundle URL
     */
    public void setBundleURL(String bundleURL) {
        this.bundleURL = bundleURL;
    }

    /**
     * Gets the value of the signing certificate as it DER encoded byte array. The signing certificate validates the
     * authenticity of a bundle. It is optional and only used with signed bundles.
     *
     * @return the value of the signing certificate
     */
    public byte[] getSigningCertificateData() {
        return signingCertificateData;
    }

    /**
     * Set the value of the signing certificate
     *
     * @param signingCertificateData The value of the signing certificate
     */
    public void setSigningCertificateData(byte[] signingCertificateData) throws CertificateException {
        this.signingCertificateData = signingCertificateData;
    }

    /**
     * Gets the value of the bundle refresh interval in seconds.
     *
     * @return the value of the bundle refresh interval
     */
    public int getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * Set the value of the bundle refresh interval in seconds
     *
     * @param refreshInterval The value of the bundle refresh interval
     */
    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
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
     * Get the value of the last successful refresh date time. This time represents the last time a successful refresh
     * operation was performed.
     *
     * @return the value of the last successful refresh date time.
     */
    public Calendar getLastSuccessfulRefresh() {
        return lastSuccessfulRefresh;
    }

    /**
     * Set the value of the last successful refresh date time
     *
     * @param lastSuccessfulRefresh The value of the last successful refresh date time
     */
    public void setLastSuccessfulRefresh(Calendar lastSuccessfulRefresh) {
        this.lastSuccessfulRefresh = lastSuccessfulRefresh;
    }

    /**
     * Get the value of the last refresh attempt date time. This time represents the last time a refresh operation was
     * attempted. It is updated regardless if the refresh operation is successful or not.
     *
     * @return the value of the last refresh attempt date time
     */
    public Calendar getLastRefreshAttempt() {
        return lastRefreshAttempt;
    }

    /**
     * Set the value of the last refresh attempt date time.
     *
     * @param lastRefreshAttempt The value of the last refresh attempt date time.
     */
    public void setLastRefreshAttempt(Calendar lastRefreshAttempt) {
        this.lastRefreshAttempt = lastRefreshAttempt;
    }

    /**
     * Get the value of the last refresh error.
     *
     * @return the value of the last refresh error.
     */
    public BundleRefreshError getLastRefreshError() {
        return lastRefreshError;
    }

    /**
     * Set the value of the last refresh error.
     *
     * @param lastRefreshError The value of the last refresh error.
     */
    public void setLastRefreshError(BundleRefreshError lastRefreshError) {
        this.lastRefreshError = lastRefreshError;
    }

    /**
     * Get the value of the collection of trust anchors contained within the bundle
     *
     * @return collection of trust anchors contained within the bundle
     */
    public Collection<TrustBundleAnchor> getTrustBundleAnchors() {
        if (trustBundleAnchors == null) {
            trustBundleAnchors = new ArrayList<>();
        }

        return trustBundleAnchors;
    }

    /**
     * Set the collection of Trust Bundle - Domain relations
     *
     * @param relations The collection of Trust Bundle - Domain relations
     */
    public void setRelations(Collection<TrustBundleDomainReltn> relations) {
        this.relations = relations;
    }

    /**
     * Get the collection of Trust Bundle - Domain relations
     *
     * @return The collection of Trust Bundle - Domain relations
     */
    @XmlTransient
    public Collection<TrustBundleDomainReltn> getRelations() {
        if (relations == null) {
            relations = new ArrayList<>();
        }

        return relations;
    }

    /**
     * Set the value of the collection of trust anchors contained within the bundle
     *
     * @param trustBundleAnchors The value of the collection of trust anchors contained within the bundle
     */
    public void setTrustBundleAnchors(Collection<TrustBundleAnchor> trustBundleAnchors) {
        this.trustBundleAnchors = trustBundleAnchors;
    }

    /**
     * Get the value of the bundle check sum.
     *
     * @return collection of the bundle check sum.
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * Set the value of the bundle check sum.
     *
     * @param checkSum The value of the bundle check sum.
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * Converts the signing data into an X509 certificate
     *
     * @return The signing data as an X509 certificate
     * @throws CertificateException
     */
    public X509Certificate toSigningCertificate() throws CertificateException {
        X509Certificate cert;

        try {
            validate();
            ByteArrayInputStream bais = new ByteArrayInputStream(signingCertificateData);
            cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(bais);
            bais.close();
        } catch (Exception e) {
            throw new CertificateException("Data cannot be converted to a valid X.509 Certificate", e);
        }

        return cert;
    }

    /**
     * Validates that the bundle has valid and complete data
     *
     * @throws CertificateException
     */
    public void validate() throws CertificateException {
        if (!hasData()) {
            throw new CertificateException("Invalid Certificate: no certificate data exists");
        }
    }

    private boolean hasData() {
        return signingCertificateData != null && !Arrays.equals(signingCertificateData, Certificate.getNullCert());
    }
}
