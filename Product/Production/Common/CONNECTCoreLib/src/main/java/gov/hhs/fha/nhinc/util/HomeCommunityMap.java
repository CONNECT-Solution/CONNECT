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
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCacheHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessEntity;

/**
 * This class is used to map a home community ID to the textual name of the home community. The information is stored in
 * a properties file so that it can be tweaked and changed without having to recompile...
 * 
 * Added getCommunityIdFromXXX() methods for use in audit logging.
 * 
 * @author Les Westberg
 * @author venkat.keesara
 */
public class HomeCommunityMap {

    private static Log log = LogFactory.getLog(HomeCommunityMap.class);

    protected ConnectionManagerCache getConnectionManagerCache() {
        return ConnectionManagerCache.getInstance();
    }
    

	protected ConnectionManagerCacheHelper getConnectionManagerCacheHelper() {
		return new ConnectionManagerCacheHelper();
	}

    /**
     * This method retrieves the name of the home community baased on the home community Id.
     * 
     * @param sHomeCommunityId The home community ID to be looked up.
     * @return The textual name of the home community.
     */
    public String getHomeCommunityName(String sHomeCommunityId) {
        String sHomeCommunityName = "";
        ConnectionManagerCacheHelper helper = getConnectionManagerCacheHelper();
        try {
            ConnectionManagerCache connectionManagerCache = getConnectionManagerCache();

            BusinessEntity oEntity = connectionManagerCache.getBusinessEntity(sHomeCommunityId);
            if ((oEntity != null) && (oEntity.getName() != null) && (oEntity.getName().size() > 0)
                    && (oEntity.getName().get(0) != null) && (oEntity.getName().get(0).getValue().length() > 0)
                    && (oEntity.getBusinessKey().length() > 0)) {
                sHomeCommunityName = helper.getCommunityId(oEntity);
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve textual name for home community ID: " + sHomeCommunityId, e);
        }

        return sHomeCommunityName;
    }

    /**
     * This method retrieves the first home community id from the target home communities list.
     * 
     * @param target
     * @return The home community OID string
     */
    public static String getCommunityIdFromTargetCommunities(NhinTargetCommunitiesType target) {
        String responseCommunityId = null;
        if (target != null && NullChecker.isNotNullish(target.getNhinTargetCommunity())
                && target.getNhinTargetCommunity().get(0) != null) {
            responseCommunityId = target.getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
        return formatHomeCommunityId(responseCommunityId);
    }

    /**
     * This method retrieves the home community id from the target home system.
     * 
     * @param target
     * @return The home community OID string
     */
    public static String getCommunityIdFromTargetSystem(NhinTargetSystemType target) {
        String responseCommunityId = null;
        if (target != null && target.getHomeCommunity() != null
                && target.getHomeCommunity().getHomeCommunityId() != null) {
            responseCommunityId = target.getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
        return formatHomeCommunityId(responseCommunityId);
    }

    /**
     * This method retrieves the home community id from the user info found in saml assertion. If not defined in the
     * user info, retrieve the id from the homeCommunityId property of the assertion.
     * 
     * @param assertion
     * @return The home community OID string
     */
    public static String getCommunityIdFromAssertion(AssertionType assertion) {
        // Extract UserInfo from Message.Assertion
        String communityId = null;
        if (assertion != null && assertion.getUserInfo() != null) {
            UserType userInfo = assertion.getUserInfo();

            if (userInfo != null && userInfo.getOrg() != null) {
                if (userInfo.getOrg().getHomeCommunityId() != null
                        && userInfo.getOrg().getHomeCommunityId().length() > 0) {
                    communityId = userInfo.getOrg().getHomeCommunityId();
                }
            }

        } else if (assertion != null && assertion.getHomeCommunity() != null) {
            communityId = assertion.getHomeCommunity().getHomeCommunityId();
        }
        return formatHomeCommunityId(communityId);
    }

    /**
     * This method retrieves the community id from the deferred query document request.
     * 
     * @param body
     * @return The home community OID string
     */
    public static String getCommunityIdForDeferredQDRequest(AdhocQueryType body) {
        String responseCommunityID = null;
        if (body != null && body.getHome() != null) {
            responseCommunityID = body.getHome();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * This method retrieves the community id from the query.
     * @param body The body of the message to extract the community id from.
     * @return a string representing the community ID
     */
    public static String getCommunityIdForQDRequest(AdhocQueryType body) {
        return getCommunityIdForDeferredQDRequest(body);
    }

    /**
     * This method retrieves the community id from the deferred query document response.
     * 
     * @param body
     * @return The home community OID string
     */
    public static String getCommunityIdForDeferredQDResponse(AdhocQueryResponse body) {
        String responseCommunityID = null;
        if (body != null && body.getRegistryObjectList() != null
                && body.getRegistryObjectList().getIdentifiable() != null
                && body.getRegistryObjectList().getIdentifiable().size() > 0
                && body.getRegistryObjectList().getIdentifiable().get(0) != null) {
            responseCommunityID = body.getRegistryObjectList().getIdentifiable().get(0).getValue().getHome();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * This method retrieves the home community id from the retrieve document request.
     * 
     * @param body
     * @return The home community OID string
     */
    public static String getCommunityIdForRDRequest(RetrieveDocumentSetRequestType body) {
        String responseCommunityID = null;
        if (body != null && NullChecker.isNotNullish(body.getDocumentRequest())
                && body.getDocumentRequest().get(0) != null) {
            responseCommunityID = body.getDocumentRequest().get(0).getHomeCommunityId();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * This method retrieves the home community id from the deferred retrieve document response.
     * 
     * @param body
     * @return The home community OID string
     */
    public static String getCommunityIdForDeferredRDResponse(RetrieveDocumentSetResponseType body) {
        String responseCommunityID = null;
        if (body != null && NullChecker.isNotNullish(body.getDocumentResponse())
                && body.getDocumentResponse().get(0) != null) {
            responseCommunityID = body.getDocumentResponse().get(0).getHomeCommunityId();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * Formats the home community id by parsing out the 'urn:oid:' prefix if it exists.
     * 
     * @param communityId the community id string to format
     * @return the formatted community id
     */
    private static String formatHomeCommunityId(String communityId) {
        if (communityId != null) {
            log.debug("communityId prior to remove urn:oid" + communityId);
            if (communityId.startsWith("urn:oid:")) {
                communityId = communityId.substring(8);
            }
        }
        return communityId;
    }

    /**
     * Return this gateway's home community id
     * 
     * @return
     */
    public static String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        try {
            sHomeCommunity = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }

        return sHomeCommunity;
    }

}
