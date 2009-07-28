/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author rayj
 */
public class AssertionHelper {
    public static String extractUserName(AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getUserName() != null)) {
            username = assertion.getUserInfo().getUserName();
        }
        return username;
    }

    public static String extractUserHomeCommunity(AssertionType assertion) {
        String username = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getOrg() != null) && (assertion.getUserInfo().getOrg().getHomeCommunityId() != null)) {
            username = assertion.getUserInfo().getOrg().getHomeCommunityId();
        }
        return username;
    }

    static String extractPurpose(AssertionType assertion) {
        String purpose = null;
        if ((assertion != null) && (assertion.getPurposeOfDisclosureCoded() != null) && (assertion.getPurposeOfDisclosureCoded().getCode() != null)) {
            purpose = assertion.getPurposeOfDisclosureCoded().getCode();
        }
        return purpose;
    }

    static String extractUserRole(AssertionType assertion) {
        String userRole = null;
        if ((assertion != null) && (assertion.getUserInfo() != null) && (assertion.getUserInfo().getRoleCoded() != null) && (assertion.getUserInfo().getRoleCoded().getCode() != null)) {
            userRole = assertion.getUserInfo().getRoleCoded().getCode();
        }
        return userRole;
    }

}
