/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.callback;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class uses the keystore alias system properties as established in the 
 * domain.xml file to allow the configuration of the SAML Keystore policy 
 * statements.
 */
public class KeyStoreClientAliasSelector implements com.sun.xml.wss.AliasSelector {

    private static Log log = LogFactory.getLog(KeyStoreClientAliasSelector.class);

    /**
     * Implementation of com.sun.xml.wss.AliasSelector returns the value of the 
     * CLIENT_KEY_ALIAS system property.
     * @param map Currently unused
     * @return The value of the CLIENT_KEY_ALIAS system property.
     */
    public String select(Map map) {

        String retSelection = System.getProperty("CLIENT_KEY_ALIAS");
        if (retSelection == null) {
            log.error("CLIENT_KEY_ALIAS is not defined in domain.xml");
        }
        log.debug("KeyStoreClientAliasSelector selects: " + retSelection);
        return retSelection;
    }
}
