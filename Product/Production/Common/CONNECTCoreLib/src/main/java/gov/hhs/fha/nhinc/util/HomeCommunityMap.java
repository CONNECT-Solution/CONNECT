/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(HomeCommunityMap.class);
    private static ConnectionManagerCache connection = ConnectionManagerCache.getInstance();
    private static PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

    /**
     * This method retrieves the name of the home community baased on the home community Id.
     *
     * @param sHomeCommunityId The home community ID to be looked up.
     * @return The textual name of the home community.
     */
    public static String getHomeCommunityName(String sHomeCommunityId) {
        String sHomeCommunityName = "";

        try {

            BusinessEntity oEntity = connection.getBusinessEntity(sHomeCommunityId);
            if (oEntity != null && oEntity.getName() != null && oEntity.getName().size() > 0
                    && oEntity.getName().get(0) != null && oEntity.getName().get(0).getValue().length() > 0) {
                sHomeCommunityName = oEntity.getName().get(0).getValue();
            }
        } catch (Exception e) {
            LOG.warn("Failed to retrieve textual name for home community ID: {}", e.getLocalizedMessage(), e);
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
        LOG.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
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
        LOG.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
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
        }

        if (communityId == null) {
            communityId = getHomeCommunityIdFromAssertion(assertion);
        }
        return formatHomeCommunityId(communityId);
    }

    /**
     * This method retrieves the home community id from the homeCommunityId property of the assertion.
     *
     * @param assertion
     * @return
     */
    public static String getHomeCommunityIdFromAssertion(AssertionType assertion) {
        String homeCommunity = null;

        if (assertion != null && assertion.getHomeCommunity() != null
                && NullChecker.isNotNullish(assertion.getHomeCommunity().getHomeCommunityId())) {
            homeCommunity = assertion.getHomeCommunity().getHomeCommunityId();
        }

        return homeCommunity;
    }

    /**
     * This method retrieves the community id from the deferred query document request.
     *
     * @param body
     * @return The home community OID string
     */
    public static String getCommunityId(AdhocQueryType body) {
        String responseCommunityID = null;
        if (body != null && body.getHome() != null) {
            responseCommunityID = body.getHome();
        }
        return formatHomeCommunityId(responseCommunityID);
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
        return getHomeCommunityIdWithPrefix(responseCommunityID);
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
    public static String formatHomeCommunityId(String communityId) {
        if (communityId != null) {
            if (communityId.startsWith("urn:oid:")) {
                communityId = communityId.substring(8);
            }
        }
        return communityId;
    }

    /**
     * Return this gateway's home community id.
     *
     * @return
     */
    public static String getLocalHomeCommunityId() {
        return getHomeCommunityFromPropFile();
    }

    /**
     * @return
     */
    private static String getHomeCommunityFromPropFile() {
        String sHomeCommunity = null;
        try {
            sHomeCommunity = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Could not get HCID from prop file: {}", ex.getLocalizedMessage(), ex);
        }
        return sHomeCommunity;
    }

    /**
     * Returns the home community id by with 'urn:oid:' prefix if it doesn't exists.
     *
     * @param communityId the community id string to format
     * @return the formatted community id
     */
    public static String getHomeCommunityIdWithPrefix(String communityId) {
        if (communityId != null) {
            if (!communityId.startsWith(NhincConstants.HCID_PREFIX)) {
                LOG.trace("Prefixing communityId with urn:oid");
                communityId = NhincConstants.HCID_PREFIX + communityId;
            }
        }
        return communityId;
    }

    protected static void setPropertyAccessor(PropertyAccessor propAccessor) {
        propertyAccessor = propAccessor;
    }

    protected static void setConnectionManager(ConnectionManagerCache connectionManager) {
        connection = connectionManager;
    }

    public static String getHomeCommunityWithoutPrefix(String hcid) {
        if (NullChecker.isNotNullish(hcid)) {
            if (hcid.startsWith(NhincConstants.HCID_PREFIX)) {
                return hcid.substring(NhincConstants.HCID_PREFIX.length());
            }
        }
        return hcid;
    }
}
