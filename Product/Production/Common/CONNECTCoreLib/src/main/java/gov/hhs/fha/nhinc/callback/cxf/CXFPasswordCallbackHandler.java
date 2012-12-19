/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.ws.security.WSPasswordCallback;

/**
 * @author mweaver
 * 
 */
public class CXFPasswordCallbackHandler implements CallbackHandler {

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(CXFPasswordCallbackHandler.class);
    private static final String SYSTEM_PROPERTY_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";

    /** Keystore Password property. **/
    private String keystorePassword;

    /**
     * One default constructor, free of charge.
     */
    public CXFPasswordCallbackHandler() {
        keystorePassword = System.getProperty(SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
    }

    /**
     * It attempts to get the password from the private alias/passwords map.
     * 
     * @param callbacks callback array
     * 
     * @throws IOException exception encountered in executing callbacks
     * @throws UnsupportedCallbackException exception encountered in executing callbacks
     */
    public final void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        LOG.debug("Executing CXFPasswordCallbackHandler.handle() ... ");
        for (Callback callback : callbacks) {
            WSPasswordCallback pc = (WSPasswordCallback) callback;

            if (keystorePassword == null || keystorePassword.isEmpty()) {
                throw new IOException("keystore password was not provided. Please provide the system property: "
                        + SYSTEM_PROPERTY_KEYSTORE_PASSWORD);
            }
            pc.setPassword(keystorePassword);

        }
        LOG.debug("end CXFPasswordCallbackHandler.handle() ... ");
    }

}
