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
package gov.hhs.fha.nhinc.directconfig.processor.impl;

import gov.hhs.fha.nhinc.directconfig.dao.TrustBundleDao;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleThumbprint;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import gov.hhs.fha.nhinc.directconfig.processor.BundleRefreshProcessor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.nhindirect.stagent.CryptoExtensions;
import org.nhindirect.stagent.options.OptionsManager;
import org.nhindirect.stagent.options.OptionsParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Camel based implementation of the {@linkplain BundleRefreshProcessor} interface.
 * <p>
 * The implementation allows for bundles to be downloaded from SSL protected sites that may not chain back to a trust
 * CA. This is useful in developement environments and is not recommended in a production environment. By default, this
 * feature is disable, but can be enabled using the
 * {@link DefaultBundleRefreshProcessorImpl#BUNDLE_REFRESH_PROCESSOR_ALLOW_DOWNLOAD_FROM_UNTRUSTED} options parameter.
 *
 * @author Greg Meyer
 * @since 1.3
 */
@Service
public class DefaultBundleRefreshProcessorImpl implements BundleRefreshProcessor {

    /**
     * Boolean value that specifies if bundles can be downloaded from non verified or untrusted SSL URLs. The default
     * value is false.
     * <p>
     * <b>JVM Parameter/Options Name:</b>
     * gov.hhs.fha.nhinc.directconfig.processor.impl.bundlerefresh.AllowNonVerifiedSSL
     */
    public final static String BUNDLE_REFRESH_PROCESSOR_ALLOW_DOWNLOAD_FROM_UNTRUSTED
        = "BUNDLE_REFRESH_PROCESSOR_ALLOW_DOWNLOAD_FROM_UNTRUSTED";

    protected static final int DEFAULT_URL_CONNECTION_TIMEOUT = 10000; // 10 seconds
    protected static final int DEFAULT_URL_READ_TIMEOUT = 10000; // 10 hour seconds

    private static final Log LOG = LogFactory.getLog(DefaultBundleRefreshProcessorImpl.class);

    @Autowired
    protected TrustBundleDao dao;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        initJVMParams();
    }

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        LOG.info("DefaultBundleRefreshProcessorImpl initialized");
    }

    /**
     * Initializes system preferences using the Direct {@link OptionsManager} pattern.
     */
    public synchronized static void initJVMParams() {

        final Map<String, String> JVM_PARAMS = new HashMap<>();
        JVM_PARAMS.put(BUNDLE_REFRESH_PROCESSOR_ALLOW_DOWNLOAD_FROM_UNTRUSTED,
            "gov.hhs.fha.nhinc.directconfig.processor.impl.bundlerefresh.AllowNonVerifiedSSL");

        OptionsManager.addInitParameters(JVM_PARAMS);
    }

    /**
     * Default constructor.
     */
    public DefaultBundleRefreshProcessorImpl() {
        OptionsParameter allowNonVerSSLParam = OptionsManager.getInstance().getParameter(
            BUNDLE_REFRESH_PROCESSOR_ALLOW_DOWNLOAD_FROM_UNTRUSTED);
        if (OptionsParameter.getParamValueAsBoolean(allowNonVerSSLParam, false)) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};

                // Install the all-trusting trust manager
                final SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };

                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                LOG.trace("DefaultBundleRefreshProcessorImpl initialization error: " + e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshBundle(TrustBundle bundle) {
        // track when the process started
        final Calendar processAttemptStart = Calendar.getInstance(Locale.getDefault());

        // get the bundle from the URL
        final byte[] rawBundle = downloadBundleToByteArray(bundle, processAttemptStart);

        if (rawBundle == null) {
            LOG.warn("Could not download bundle, or bundle file was null.");
            return;
        }

        LOG.debug("Bundle downloaded.");

        // check to see if there is a difference in the anchor sets (using checksums)
        boolean update = false;
        String checkSum = "";

        if (bundle.getCheckSum() == null) {
            // never got a check sum, flag for update
            update = true;
        } else {
            try {
                checkSum = BundleThumbprint.toThumbprint(rawBundle).toString();
                // if checksum mismatch, flag for update
                update = !bundle.getCheckSum().equals(BundleThumbprint.toThumbprint(rawBundle).toString());
            } catch (NoSuchAlgorithmException ex) {
                dao.updateLastUpdateError(bundle.getId(), processAttemptStart, BundleRefreshError.INVALID_BUNDLE_FORMAT);
                LOG.error("Failed to generate thumbprint for downloaded bundle: " + ex.getMessage(), ex);
            }
        }

        if (!update) {
            LOG.debug("No update needed.");
            dao.updateLastUpdateError(bundle.getId(), processAttemptStart, BundleRefreshError.SUCCESS);
            return;
        }

        final Collection<X509Certificate> bundleCerts = convertRawBundleToAnchorCollection(rawBundle, bundle,
            processAttemptStart);

        if (bundleCerts == null) {
            LOG.warn("No bundle certs found.");
            return;
        }

        LOG.debug("Preparing to update bundle");

        final HashSet<X509Certificate> downloadedSet = new HashSet<>(bundleCerts);

        try {
            final Collection<TrustBundleAnchor> newAnchors = new ArrayList<>();

            for (X509Certificate downloadedAnchor : downloadedSet) {
                try {
                    final TrustBundleAnchor anchorToAdd = new TrustBundleAnchor();
                    anchorToAdd.setData(downloadedAnchor.getEncoded());
                    anchorToAdd.setTrustBundle(bundle);

                    newAnchors.add(anchorToAdd);
                } catch (CertificateEncodingException | CertificateException e) {
                    LOG.warn("Failed to convert downloaded anchor to byte array. ", e);
                }
            }

            LOG.debug("Found " + newAnchors.size() + " Trust Bundle Anchors.");

            bundle.setTrustBundleAnchors(newAnchors);

            dao.updateTrustBundleAnchors(bundle.getId(), processAttemptStart, newAnchors, checkSum);
            dao.updateLastUpdateError(bundle.getId(), processAttemptStart, BundleRefreshError.SUCCESS);
        } catch (ConfigurationStoreException e) {
            dao.updateLastUpdateError(bundle.getId(), processAttemptStart, BundleRefreshError.INVALID_BUNDLE_FORMAT);
            LOG.error("Failed to write updated bundle anchors to data store ", e);
        }
    }

    /**
     * Converts a trust raw trust bundle byte array into a collection of {@link X509Certificate} objects.
     *
     * @param rawBundle The raw representation of the bundle. This generally the raw byte string downloaded from the
     * bundle's URL.
     * @param existingBundle The configured bundle object in the DAO. This object may contain the signing certificate
     * used for bundle authenticity checking.
     * @param processAttemptStart The time that the update process started.
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Collection<X509Certificate> convertRawBundleToAnchorCollection(byte[] rawBundle,
        final TrustBundle existingBundle, final Calendar processAttemptStart) {
        Collection<? extends Certificate> bundleCerts = null;
        InputStream inStream = null;

        // check to see if its an unsigned PKCS7 container
        try {
            inStream = new ByteArrayInputStream(rawBundle);
            bundleCerts = CertificateFactory.getInstance("X.509").generateCertificates(inStream);

            // in Java 7, an invalid bundle may be returned as a null instead of throw an exception
            // if its null and has no anchors, then try again as a signed bundle
            if (bundleCerts != null && bundleCerts.isEmpty()) {
                bundleCerts = null;
            }
        } catch (Exception e) {
            /* no-op for now.... this may not be a p7b, so try it as a signed message */
            LOG.warn("Could not parse as an unsigned PKCS7 container, possibly a CMS signed message?");
            LOG.trace("PKCS7 container parsing exception: " + e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(inStream);
        }

        // didn't work... try again as a CMS signed message
        if (bundleCerts == null) {
            try {
                final CMSSignedData signed = new CMSSignedData(rawBundle);

                // if there is a signing certificate assigned to the bundle, verify the signature
                if (existingBundle.getSigningCertificateData() != null) {
                    boolean sigVerified = false;

                    final X509Certificate signingCert = existingBundle.toSigningCertificate();
                    for (SignerInformation sigInfo : (Collection<SignerInformation>) signed.getSignerInfos()
                        .getSigners()) {

                        try {
                            if (sigInfo.verify(signingCert, CryptoExtensions.getJCEProviderName())) {
                                sigVerified = true;
                                break;
                            }
                        } catch (Exception e) {
                            LOG.warn("Could not parse as a CMS signed message");
                            LOG.trace("Could not parse as a CMS signed message: " + e.getLocalizedMessage(), e);
                            /* no-op... can't verify */
                        }
                    }

                    if (!sigVerified) {
                        dao.updateLastUpdateError(existingBundle.getId(), processAttemptStart,
                            BundleRefreshError.UNMATCHED_SIGNATURE);
                        LOG.warn("Downloaded bundle signature did not match configured signing certificate.");
                        return null;
                    }
                }

                final CMSProcessableByteArray signedContent = (CMSProcessableByteArray) signed.getSignedContent();

                inStream = new ByteArrayInputStream((byte[]) signedContent.getContent());

                bundleCerts = CertificateFactory.getInstance("X.509").generateCertificates(inStream);
            } catch (Exception e) {
                dao.updateLastUpdateError(existingBundle.getId(), processAttemptStart,
                    BundleRefreshError.INVALID_BUNDLE_FORMAT);
                LOG.warn("Failed to extract anchors from downloaded bundle.");
                LOG.trace("Extract anchors from bundle exception: " + e.getLocalizedMessage(), e);
            } finally {
                IOUtils.closeQuietly(inStream);
            }
        }

        return (Collection<X509Certificate>) bundleCerts;
    }

    /**
     * Downloads a bundle from the bundle's URL and returns the result as a byte array.
     *
     * @param bundle The bundle that will be downloaded.
     * @param processAttempStart The time that the update process started.
     * @return A byte array representing the raw data of the bundle.
     */
    protected byte[] downloadBundleToByteArray(TrustBundle bundle, Calendar processAttempStart) {
        InputStream inputStream = null;

        byte[] retVal = null;
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            // in this case the cert is a binary representation
            // of the CERT URL... transform to a string
            final URL certURL = new URL(bundle.getBundleURL());

            final URLConnection connection = certURL.openConnection();

            // the connection is not actually made until the input stream
            // is open, so set the timeouts before getting the stream
            connection.setConnectTimeout(DEFAULT_URL_CONNECTION_TIMEOUT);
            connection.setReadTimeout(DEFAULT_URL_READ_TIMEOUT);

            // open the URL as in input stream
            inputStream = connection.getInputStream();

            int BUF_SIZE = 2048;
            int count;

            final byte buf[] = new byte[BUF_SIZE];

            while ((count = inputStream.read(buf)) > -1) {
                outStream.write(buf, 0, count);
            }

            retVal = outStream.toByteArray();
        } catch (SocketTimeoutException e) {
            dao.updateLastUpdateError(bundle.getId(), processAttempStart, BundleRefreshError.DOWNLOAD_TIMEOUT);
            LOG.warn("Failed to download bundle", e);
        } catch (Exception e) {
            dao.updateLastUpdateError(bundle.getId(), processAttempStart, BundleRefreshError.NOT_FOUND);
            LOG.warn("Failed to download bundle", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outStream);
        }

        return retVal;
    }
}
