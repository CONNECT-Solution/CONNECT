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
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.EndpointReferenceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.w3._2005._08.addressing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author rayj
 */
public class ConnectSubscriptionReferenceHelper {

    public static final String REFERENCE_PARAMETER_SUBSCRIPTION_ID_NAMESPACE = "http://www.hhs.gov/healthit/nhin";
    public static final String REFERENCE_PARAMETER_SUBSCRIPTION_ID_ELEMENT_NAME = "SubscriptionId";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(HiemSubscriptionRepositoryService.class);

    public EndpointReferenceType createSubscriptionReference(String subscriptionId) {
        String url = getSubscriptionManagerEndpointAddress();
        EndpointReferenceHelper helper = new EndpointReferenceHelper();
        EndpointReferenceType subscriptionReference = helper.createEndpointReferenceAddressOnly(url);
        helper.attachSimpleReferenceParameter(subscriptionReference, REFERENCE_PARAMETER_SUBSCRIPTION_ID_NAMESPACE,
                REFERENCE_PARAMETER_SUBSCRIPTION_ID_ELEMENT_NAME, subscriptionId);
        return subscriptionReference;
    }

    // todo: move to common location
    private String getSubscriptionManagerEndpointAddress() {
        String subMgrUrl = null;
        String homeCommunityId = null;
        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            homeCommunityId = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + homeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY
                    + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(homeCommunityId)) {
            try {
                subMgrUrl = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(
                		NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: "
                        + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME + " for community id: "
                        + homeCommunityId);
                log.error(ex.getMessage());
            }
        }
        return subMgrUrl;
    }
}
