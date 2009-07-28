package gov.hhs.fha.nhinc.callback;

import com.sun.xml.wss.impl.callback.KeyStoreCallback;
import com.sun.xml.wss.impl.callback.PrivateKeyCallback;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * This class uses the keystore system properties as established in the 
 * domain.xml file to allow the configuration of the SAML Keystore policy 
 * statements.
 */
public class KeyStoreCallbackHandler implements CallbackHandler {

    private KeyStore keyStore = null;
    private String password;
    private static final String storeType = "JKS";
    //private static Log log = LogFactory.getLog(KeyStoreCallbackHandler.class);

    /**
     * Creates the callback handler saving the keystore certificates information 
     * from the keystore file specified by the system properties:
     * javax.net.ssl.keyStore and javax.net.ssl.keyStorePassword.
     */
    public KeyStoreCallbackHandler() {
        System.out.println("Entry KeyStoreCallbackHandler Constructor");
        InputStream is = null;
        String storeLoc = System.getProperty("javax.net.ssl.keyStore");
        if (storeLoc != null) {
            password = System.getProperty("javax.net.ssl.keyStorePassword");
            if (password != null) {
                try {
                    keyStore = KeyStore.getInstance(storeType);
                    is = new FileInputStream(storeLoc);
                    keyStore.load(is, password.toCharArray());
                } catch (IOException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (CertificateException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (KeyStoreException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        System.out.println("KeyStoreCallbackHandler " + ex);
                    }
                }
            } else {
                System.out.println("javax.net.ssl.keyStorePassword is not defined in domain.xml");
            }
        } else {
            System.out.println("javax.net.ssl.keyStore is not defined in domain.xml");
        }
        System.out.println("Exit KeyStoreCallbackHandler Constructor");
    }

    /**
     * Implementing the callback, this method provides the keystore information 
     * or the private key information depending on the type of callback desired 
     * to the input Callback object.
     * @param callbacks The Callback which needs to have keystore information 
     * set, should be either a KeyStoreCallback or a PrivateKeyCallback
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        System.out.println("Entry KeyStoreCallbackHandler handle callback");
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof KeyStoreCallback) {
                KeyStoreCallback cb = (KeyStoreCallback) callbacks[i];
                //print(cb.getRuntimeProperties());
                cb.setKeystore(keyStore);
                System.out.println("KeyStoreCallback set keystore");
            } else if (callbacks[i] instanceof PrivateKeyCallback) {
                try {
                    PrivateKeyCallback cb = (PrivateKeyCallback) callbacks[i];
                    //print(cb.getRuntimeProperties());
                    Key privkey = keyStore.getKey(cb.getAlias(), password.toCharArray());
                    cb.setKey((PrivateKey) privkey);
                    System.out.println("PrivateKeyCallback set private key");
                } catch (KeyStoreException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                } catch (UnrecoverableKeyException ex) {
                    System.out.println("KeyStoreCallbackHandler " + ex);
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("Unsupported KeyStoreCallbackHandler Callback: " + callbacks[i]);
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
        System.out.println("Exit KeyStoreCallbackHandler handle callback");
    }

    /*private void print(Map context) {
    Iterator it = context.keySet().iterator();
    while (it.hasNext()) {
    System.out.println("Prop " + it.next());
    }
    }*/
}

