package gov.hhs.fha.nhinc.callback;

import com.sun.xml.wss.impl.callback.KeyStoreCallback;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class uses the truststore system properties as established in the 
 * domain.xml file to allow the configuration of the SAML Truststore policy 
 * statements.
 */
public class TrustStoreCallbackHandler implements CallbackHandler {

    private KeyStore keyStore = null;
    private String password;
    private static final String storeType = "JKS";
    private static Log log = LogFactory.getLog(TrustStoreCallbackHandler.class);

    /**
     * Creates the callback handler saving the truststore certificates 
     * information from the truststore file specified by the system properties:
     * javax.net.ssl.trustStore and javax.net.ssl.trustStorePassword.
     */
    public TrustStoreCallbackHandler() {
        log.debug("Entry TrustStoreCallbackHandler Constructor");
        InputStream is = null;
        String storeLoc = System.getProperty("javax.net.ssl.trustStore");
        if (storeLoc != null) {
            password = System.getProperty("javax.net.ssl.trustStorePassword");
            if (password != null) {
                try {
                    keyStore = KeyStore.getInstance(storeType);
                    is = new FileInputStream(storeLoc);
                    keyStore.load(is, password.toCharArray());
                } catch (IOException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    log.debug("TrustStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        log.debug("TrustStoreCallbackHandler " + ex);
                    }
                }
            } else {
                log.error("javax.net.ssl.trustStorePassword is not defined in domain.xml");
            }
        } else {
            log.error("javax.net.ssl.trustStore is not defined in domain.xml");
        }
        log.debug("Exit TrustStoreCallbackHandler Constructor");
    }

    /**
     * Implementing the callback, this method provides the truststore 
     * information to the input Callback object.
     * @param callbacks The Callback which needs to have truststore information 
     * set.
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.debug("Entry TrustStoreCallbackHandler handle callback");
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof KeyStoreCallback) {
                KeyStoreCallback cb = (KeyStoreCallback) callbacks[i];
                //print(cb.getRuntimeProperties());
                cb.setKeystore(keyStore);
                log.debug("KeyStoreCallback set keystore: " + keyStore);
            } else {
                log.error("Unsupported KeyStoreCallbackHandler Callback: " + callbacks[i]);
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
        log.debug("Exit TrustStoreCallbackHandler handle callback");
    }

    /*private void print(Map context) {
    Iterator it = context.keySet().iterator();
    while (it.hasNext()) {
    log.debug("Prop " + it.next());
    }
    }*/
}

