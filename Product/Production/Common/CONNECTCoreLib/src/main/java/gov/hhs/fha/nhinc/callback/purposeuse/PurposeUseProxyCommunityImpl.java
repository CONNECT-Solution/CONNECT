/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.callback.purposeuse;

import gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author rhalfert
 */
public class PurposeUseProxyCommunityImpl implements PurposeUseProxy {

    private static Log log = LogFactory.getLog(PurposeUseProxyCommunityImpl.class);
    
    private static final String PURPOSE_FOR_USE_PROPERTY_FILE = "purposeUse";
    private final IPropertyAcessor propertyAccessor;
    
    /**
     * Constructor allows injection of the property accessor.
     * @param propertyAccessor used to pull properties.
     */
    public PurposeUseProxyCommunityImpl(IPropertyAcessor propertyAccessor) {
        super();
        this.propertyAccessor = propertyAccessor;
    }

    /**
     * Default constructor.
     */
    public PurposeUseProxyCommunityImpl() {
        super();
        this.propertyAccessor = PropertyAccessor.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPurposeForUseEnabled(CallbackProperties callbackProperties) {

        // check configured hcid value in purpose for use properties file
        String targetHomeCommunityId = callbackProperties.getTargetHomeCommunityId();
        if (targetHomeCommunityId != null) {
            return isPurposeForUseEnabled(targetHomeCommunityId);
        }

        return false;
    }
    
    /**
     * Returns boolean condition on whether PurposeForUse is enabled based on HCID.
     * 
     * @param homeCommunityId The home community for which to check the property setting
     * @return The PurposeForUse enabled setting
     */
    private boolean isPurposeForUseEnabled(String homeCommunityId) {
        boolean match = false;
        try {
            String purposeForUseEnabled = propertyAccessor.getProperty(PURPOSE_FOR_USE_PROPERTY_FILE, homeCommunityId);
            if (purposeForUseEnabled != null && purposeForUseEnabled.equalsIgnoreCase("true")) {
                match = true;
            }
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve (homeCommunityId) " + homeCommunityId + " from property file: "
                    + PURPOSE_FOR_USE_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return match;
    }
}
