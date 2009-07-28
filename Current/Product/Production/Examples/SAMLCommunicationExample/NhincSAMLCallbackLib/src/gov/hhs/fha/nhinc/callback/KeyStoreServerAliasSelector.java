package gov.hhs.fha.nhinc.callback;

import java.util.Map;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * This class uses the keystore alias system properties as established in the 
 * domain.xml file to allow the configuration of the SAML Keystore policy 
 * statements.
 */
public class KeyStoreServerAliasSelector implements com.sun.xml.wss.AliasSelector {

    //private static Log log = LogFactory.getLog(KeyStoreServerAliasSelector.class);

    /**
     * Implementation of com.sun.xml.wss.AliasSelector returns the value of the 
     * SERVER_KEY_ALIAS system property.
     * @param map Currently unused
     * @return The value of the SERVER_KEY_ALIAS system property.
     */
    public String select(Map map) {

        String retSelection = System.getProperty("SERVER_KEY_ALIAS");
        if (retSelection == null) {
            System.out.println("SERVER_KEY_ALIAS is not defined in domain.xml");
        }
        System.out.println("TrustStoreAliasSelector selects: " + retSelection);
        return retSelection;
    }
}
