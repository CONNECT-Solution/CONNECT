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
package gov.hhs.fha.nhinc.exchangemgr.fhir;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.SSLSocketFactoryWrapper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelperProperties;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.transport.https.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

/**
 *
 * @author tjafri
 */
public class ConnectionUtil {

    private static final Long DEFAULT_TIMEOUT = 120000L;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionUtil.class);

    private ConnectionUtil() {
    }

    public static HttpURLConnection createHttpUrlConnection(String uri, HttpMethod httpMethod, String sniName,
        MimeType mimeType) throws FhirClientException {
        try {
            HttpURLConnection urlConnection = getHttpConnection(uri, getSSLContext(), sniName);
            Long timeout = PropertyAccessor.getInstance().getPropertyLongObject(NhincConstants.GATEWAY_PROPERTY_FILE,
                WebServiceProxyHelperProperties.CONFIG_KEY_TIMEOUT, DEFAULT_TIMEOUT);
            urlConnection.setReadTimeout(timeout.intValue());
            urlConnection.setConnectTimeout(timeout.intValue());
            urlConnection.setRequestMethod(httpMethod.toString());
            urlConnection.setRequestProperty(FhirConstants.ACCEPT_HEADER, mimeType.getMimeType());
            urlConnection.setRequestProperty(FhirConstants.CONTENT_TYPE_HEADER, mimeType.getMimeType()
                + FhirConstants.FHIR_CHARSET_HEADER);
            urlConnection.setRequestProperty(FhirConstants.ACCEPT_CHARSET_HEADER, FhirConstants.DEFAULT_CHARSET);
            return urlConnection;
        } catch (IOException | GeneralSecurityException anEx) {
            throw new FhirClientException("Error opening Connection: " + anEx.getLocalizedMessage(), anEx);
        }
    }

    private static HttpURLConnection getHttpConnection(String uri, SSLContext sslContext, String sniName) throws
        KeyManagementException, IOException, FhirClientException {
        if (StringUtils.isBlank(uri)) {
            throw new FhirClientException("URL not specified");
        }
        URL url = new URL(uri);
        if (FhirConstants.HTTPS.equals(url.getProtocol())) {
            return getHttpsConnection(url, sslContext, sniName);
        }
        return (HttpURLConnection) url.openConnection();
    }

    private static HttpsURLConnection getHttpsConnection(URL url, SSLContext sslContext, String sniName) throws
        KeyManagementException, IOException {
        SSLParameters sslParamters = sslContext.getDefaultSSLParameters();
        CertificateManager cm = CertificateManagerImpl.getInstance();
        sslContext.init(getKeyManager(cm), getTrustManager(cm), null);
        SSLSocketFactory factory;
        if (StringUtils.isNotBlank(sniName)) {
            List<SNIServerName> sniHostList = new ArrayList<>();
            sniHostList.add(new SNIHostName(sniName));
            sslParamters.setServerNames(sniHostList);
            factory = new SSLSocketFactoryWrapper(sslContext.getSocketFactory(), sslParamters);
        } else {
            factory = sslContext.getSocketFactory();
        }
        HttpsURLConnection httpsConnection = ((HttpsURLConnection) url.openConnection());
        httpsConnection.setSSLSocketFactory(factory);
        if (!isHostnameVerificationEnabled()) {
            httpsConnection.setHostnameVerifier(getAllHostnameVerifier());
        }
        return httpsConnection;
    }

    private static SSLContext getSSLContext() throws GeneralSecurityException {
        String protocols = getTLSVersionsFromProperties();
        TLSClientParameters tlsCP = new TLSClientParameters();
        tlsCP.setSecureSocketProtocol(protocols);

        return SSLUtils.getSSLContext(tlsCP);
    }

    private static String getTLSVersionsFromProperties() {
        try {
            return PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.FHIR_TLS);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to retrieve {} from {}: {}", NhincConstants.FHIR_TLS,
                NhincConstants.GATEWAY_PROPERTY_FILE, ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    private static KeyManager[] getKeyManager(CertificateManager cm) {
        try {
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(cm.getKeyStore(), getKeystorePassword());
            return keyFactory.getKeyManagers();
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            LOG.error("Could not initialize key and trust managers: {} ", e.getLocalizedMessage(), e);
        }
        return new KeyManager[0];
    }

    private static TrustManager[] getTrustManager(CertificateManager cm) {
        try {
            TrustManagerFactory trustFactory = TrustManagerFactory.
                getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(cm.getTrustStore());
            return trustFactory.getTrustManagers();
        } catch (KeyStoreException | NoSuchAlgorithmException e) {
            LOG.error("Could not initialize key and trust managers: {} ", e.getLocalizedMessage(), e);
        }
        return new TrustManager[0];
    }

    private static char[] getKeystorePassword() throws UnrecoverableKeyException {
        String keystorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        if (keystorePassword == null || keystorePassword.isEmpty()) {
            throw new UnrecoverableKeyException("Password for key is null or empty.");
        }

        return keystorePassword.toCharArray();
    }

    private static HostnameVerifier getAllHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private static boolean isHostnameVerificationEnabled() {
        return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
            NhincConstants.ENABLE_HOSTNAME_VERIFICATION, true);
    }
}
