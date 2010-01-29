/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class TestHelper {
    public static NhinTargetCommunityType createTargetCommunity (String hcid, String list, String region) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        
        if (NullChecker.isNotNullish(hcid)) {
            HomeCommunityType community = new HomeCommunityType();
            community.setHomeCommunityId(hcid);
            target.setHomeCommunity(community);
        }

        if (NullChecker.isNotNullish(region)) {
            target.setRegion(region);
        }

        if (NullChecker.isNotNullish(list)) {
            target.setList(list);
        }

        if (NullChecker.isNullish(hcid) && NullChecker.isNullish(region) && NullChecker.isNullish(list)) {
            target = null;
        }
        
        return target;
    }

}
