/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;

/**
 *
 * @author rayj
 */
public class CommunityHelper {

    public static String extractCommunityId(HomeCommunityType community) {
        String communityId = null;
        if (community != null) {
            communityId = community.getHomeCommunityId();
        }
        return communityId;
    }
}
