/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

/**
 * @author mweaver
 * 
 */
public class CertificateManagerImpl implements CertificateManager {
    private static Logger log = Logger.getLogger(CertificateManagerImpl.class);

    private KeyStore keyStore = null;

    private KeyStore trustStore = null;

    private CertificateManagerImpl() {
        try {
            if (keyStore == null) {
                initKeyStore();
            }
            if (trustStore == null) {
                initTrustStore();
            }
        } catch (Exception e) {
            log.error("unable to initialize keystores", e);
            e.printStackTrace();
        }
    }

    public static CertificateManager getInstance() {
        return new CertificateManagerImpl();
    }
    
    /**
     * @return the keyStore
     */
    @Override
    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * @return the trustStore
     */
    @Override
    public KeyStore getTrustStore() {
        return trustStore;
    }

    /**
     * Initializes the keystore access using the system properties defined in the domain.xml javax.net.ssl.keyStore and
     * javax.net.ssl.keyStorePassword
     * 
     * @throws Exception
     */
    private void initKeyStore() throws Exception {
        log.debug("SamlCallbackHandler.initKeyStore() -- Begin");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.keyStoreType");
        String password = System.getProperty("javax.net.ssl.keyStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.keyStore");

        if (storeType == null) {
            log.error("javax.net.ssl.keyStoreType is not defined");
            log.warn("Default to JKS keyStoreType");
            storeType = "JKS";
        }
        if (password == null || storeLoc == null) {
            log.error("Store password or store location not defined");
            log.error("Please define javax.net.ssl.keyStorePassword and javax.net.ssl.keyStore");
        }

        if ("JKS".equals(storeType) && storeLoc == null) {
            log.error("javax.net.ssl.keyStore is not defined");
        } else {
            try {
                keyStore = KeyStore.getInstance(storeType);
                if ("JKS".equals(storeType)) {
                    is = new FileInputStream(storeLoc);
                }
                keyStore.load(is, password.toCharArray());
            } catch (NoSuchAlgorithmException ex) {
                log.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } catch (CertificateException ex) {
                log.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } catch (KeyStoreException ex) {
                log.error("Error initializing KeyStore: " + ex);
                throw new Exception(ex.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    log.debug("KeyStoreCallbackHandler " + ex);
                }
            }
        }

        log.debug("SamlCallbackHandler.initKeyStore() -- End");
    }

    /**
     * Initializes the truststore access using the system properties defined in the domain.xml javax.net.ssl.trustStore
     * and javax.net.ssl.trustStorePassword
     * 
     * @throws Exception
     */
    private void initTrustStore() throws Exception {
        log.debug("SamlCallbackHandler.initTrustStore() -- Begin");

        InputStream is = null;
        String storeType = System.getProperty("javax.net.ssl.trustStoreType");
        String password = System.getProperty("javax.net.ssl.trustStorePassword");
        String storeLoc = System.getProperty("javax.net.ssl.trustStore");

        if (storeType == null) {
            log.error("javax.net.ssl.trustStoreType is not defined in domain.xml");
            log.warn("Default to JKS trustStoreType");
            storeType = "JKS";
        }
        if (password != null) {
            if ("JKS".equals(storeType) && storeLoc == null) {
                log.error("javax.net.ssl.trustStore is not defined in domain.xml");
            } else {
                try {
                    trustStore = KeyStore.getInstance(storeType);
                    if ("JKS".equals(storeType)) {
                        is = new FileInputStream(storeLoc);
                    }
                    trustStore.load(is, password.toCharArray());
                } catch (NoSuchAlgorithmException ex) {
                    log.error("Error initializing TrustStore: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (CertificateException ex) {
                    log.error("Error initializing TrustStore: " + ex);
                    throw new IOException(ex.getMessage());
                } catch (KeyStoreException ex) {
                    log.error("Error initializing TrustStore: " + ex);
                    throw new IOException(ex.getMessage());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException ex) {
                        log.debug("KeyStoreCallbackHandler " + ex);
                    }
                }
            }
        } else {
            log.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
        }
        log.debug("SamlCallbackHandler.initTrustStore() -- End");
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultCertificate()
     */
    public X509Certificate getDefaultCertificate() throws Exception {
        return (X509Certificate) getPrivateKeyEntry().getCertificate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPrivateKey()
     */
    @Override
    public PrivateKey getDefaultPrivateKey() throws Exception {
        return getPrivateKeyEntry().getPrivateKey();
    }

    private PrivateKeyEntry getPrivateKeyEntry() throws Exception {
        log.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- Begin");
        X509Certificate cert = null;
        KeyStore.PrivateKeyEntry pkEntry = null;

        String client_key_alias = System.getProperty("CLIENT_KEY_ALIAS");
        if (client_key_alias != null) {
            String password = System.getProperty("javax.net.ssl.keyStorePassword");
            if (password != null) {
                try {
                    pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(client_key_alias,
                            new KeyStore.PasswordProtection(password.toCharArray()));

                } catch (NoSuchAlgorithmException ex) {
                    log.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (KeyStoreException ex) {
                    log.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                } catch (UnrecoverableEntryException ex) {
                    log.error("Error initializing Private Key: " + ex);
                    throw new Exception(ex.getMessage());
                }

            } else {
                log.error("javax.net.ssl.keyStorePassword is not defined in domain.xml");
            }

        } else {
            log.error("CLIENT_KEY_ALIAS is not defined in domain.xml");
        }

        log.debug("SamlCallbackHandler.getDefaultPrivKeyCert() -- End");
        return pkEntry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.callback.openSAML.CertificateManager#getDefaultPublicKey()
     */
    @Override
    public RSAPublicKey getDefaultPublicKey() {

        try {
            return (RSAPublicKey) getDefaultCertificate().getPublicKey();
        } catch (Exception e) {
            log.error(e);

        }
        return null;
    }

}
