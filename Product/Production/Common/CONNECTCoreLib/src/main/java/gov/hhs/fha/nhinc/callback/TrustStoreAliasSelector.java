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
 * This class uses the truststore alias system properties as established in the 
 * domain.xml file to allow the configuration of the SAML Truststore policy 
 * statements.
 */
public class TrustStoreAliasSelector implements com.sun.xml.wss.AliasSelector {

    private static Log log = LogFactory.getLog(TrustStoreAliasSelector.class);

    /**
     * Implementation of com.sun.xml.wss.AliasSelector returns the value of the 
     * SERVER_KEY_ALIAS system property.
     * @param map Currently unused
     * @return The value of the SERVER_KEY_ALIAS system property.
     */
    public String select(Map map) {

        String retSelection = System.getProperty("SERVER_KEY_ALIAS");
        if (retSelection == null) {
            log.error("SERVER_KEY_ALIAS is not definedt in domain.xml");
        }
        log.debug("TrustStoreAliasSelector selects: " + retSelection);
        return retSelection;
    }
}
