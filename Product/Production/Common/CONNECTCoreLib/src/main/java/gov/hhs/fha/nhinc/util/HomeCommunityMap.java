/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to map a home community ID to the
 * textual name of the home community.  The information
 * is stored in a properties file so that it can be tweaked
 * and changed without having to recompile...
 *
 * Added getCommunityIdFromXXX() methods for use in audit logging.
 * 
 * @author Les Westberg
 * @author venkat.keesara
 */
public class HomeCommunityMap {

    private static Log log = LogFactory.getLog(HomeCommunityMap.class);

    /**
     * This method retrieves the name of the home community baased on the
     * home community Id.
     * 
     * @param sHomeCommunityId The home community ID to be looked up.
     * @return The textual name of the home community.
     */
    public String getHomeCommunityName(String sHomeCommunityId) {
        String sHomeCommunityName = "";

        try {
            CMBusinessEntity oEntity = ConnectionManagerCache.getBusinessEntity(sHomeCommunityId);
            if ((oEntity != null) &&
                    (oEntity.getNames() != null) &&
                    (oEntity.getNames().getBusinessName() != null) &&
                    (oEntity.getNames().getBusinessName().size() > 0) &&
                    (oEntity.getNames().getBusinessName().get(0) != null) &&
                    (oEntity.getNames().getBusinessName().get(0).length() > 0)) {
                sHomeCommunityName = oEntity.getNames().getBusinessName().get(0);
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve textual name for home community ID: " + sHomeCommunityId, e);
        }

        return sHomeCommunityName;
    }

    /**
     * This method retrieves the first home community id from the target home
     * communities list.
     * @param target
     * @return The home community OID string
     */
    public static String getCommunityIdFromTargetCommunities(NhinTargetCommunitiesType target) {
        String responseCommunityId = null;
        if (target != null &&
                NullChecker.isNotNullish(target.getNhinTargetCommunity()) &&
                target.getNhinTargetCommunity().get(0) != null) {
            responseCommunityId = target.getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
        return formatHomeCommunityId(responseCommunityId);
    }

    /**
     * This method retrieves the home community id from the target home system.
     * @param target
     * @return The home community OID string
     */
    public static String getCommunityIdFromTargetSystem(NhinTargetSystemType target) {
        String responseCommunityId = null;
        if (target != null &&
                target.getHomeCommunity() != null &&
                target.getHomeCommunity().getHomeCommunityId() != null) {
            responseCommunityId = target.getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityId is " + responseCommunityId);
        return formatHomeCommunityId(responseCommunityId);
    }

    /**
     * This method retrieves the home community id from the user info found in
     * saml assertion.  If not defined in the user info, retrieve the id from
     * the homeCommunityId property of the assertion.
     * @param assertion
     * @return The home community OID string
     */
    public static String getCommunityIdFromAssertion(AssertionType assertion) {
        // Extract UserInfo from Message.Assertion
        String communityId = null;
        if (assertion != null &&
                assertion.getUserInfo() != null) {
            UserType userInfo = assertion.getUserInfo();

            if (userInfo != null &&
                    userInfo.getOrg() != null) {
                if (userInfo.getOrg().getHomeCommunityId() != null &&
                        userInfo.getOrg().getHomeCommunityId().length() > 0) {
                    communityId = userInfo.getOrg().getHomeCommunityId();
                }
            }

        } else if (assertion.getHomeCommunity() != null) {
            communityId = assertion.getHomeCommunity().getHomeCommunityId();
        }
        return formatHomeCommunityId(communityId);
    }

    /**
     * This method retrieves the community id from the deferred query
     * document response.
     * @param body
     * @return The home community OID string
     */
    public static String getCommunitIdForDeferredQDResponse(AdhocQueryResponse body) {
        String responseCommunityID = null;
        if (body != null &&
                body.getRegistryObjectList() != null &&
                body.getRegistryObjectList().getIdentifiable() != null &&
                body.getRegistryObjectList().getIdentifiable().size() > 0 &&
                body.getRegistryObjectList().getIdentifiable().get(0) != null) {
            responseCommunityID = body.getRegistryObjectList().getIdentifiable().get(0).getValue().getHome();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * This method retrieves the home community id from the retrieve document
     * request.
     * @param body
     * @return The home community OID string
     */
    public static String getCommunitIdForRDRequest(RetrieveDocumentSetRequestType body) {
        String responseCommunityID = null;
        if (body != null &&
                NullChecker.isNotNullish(body.getDocumentRequest()) &&
                body.getDocumentRequest().get(0) != null) {
            responseCommunityID = body.getDocumentRequest().get(0).getHomeCommunityId();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    /**
     * This method retrieves the home community id from the deferred retrieve
     * document response.
     * @param body
     * @return The home community OID string
     */
    public static String getCommunitIdForDeferredRDResponse(RetrieveDocumentSetResponseType body) {
        String responseCommunityID = null;
        if (body != null &&
                NullChecker.isNotNullish(body.getDocumentResponse()) &&
                body.getDocumentResponse().get(0) != null) {
            responseCommunityID = body.getDocumentResponse().get(0).getHomeCommunityId();
        }
        return formatHomeCommunityId(responseCommunityID);
    }

    private static String formatHomeCommunityId(String communityId) {
        // Set the Audit Source Id (community id)
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
            sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }

        return sHomeCommunity;
    }

}
