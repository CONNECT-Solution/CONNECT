/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {
    public NhinTargetSystemType createNhinTargetSystemType(String homeCommunityId)
    {
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(homeCommunityId);

        return createNhinTargetSystemType(hc);
    }
    public NhinTargetSystemType createNhinTargetSystemType(HomeCommunityType hc)
    {
        NhinTargetSystemType result = new NhinTargetSystemType();
        result.setHomeCommunity(hc);

        return result;
    }
}
