/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.event.builder;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
public class TargetDescriptionExtractor {
    
    
    /**
     * Extracts responder HCIDs from target.
     * @param target
     * @return 
     */
    public List<String> getResponders(NhinTargetSystemType target) {
        List<String> responders = null;
        if(target.getHomeCommunity() != null
                && target.getHomeCommunity().getHomeCommunityId() != null){
            responders = new ArrayList<String>();
            responders.add(target.getHomeCommunity().getHomeCommunityId());
        }
        return responders;
    }
}
