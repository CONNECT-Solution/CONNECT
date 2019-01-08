/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.messaging.builder.impl;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.messaging.builder.NhinTargetCommunitiesBuilder;
import java.util.Collections;
import java.util.List;

/**
 * This class builds the NhinTargetCommunities tag for PD-DQ-DR request
 *
 * @author tjafri
 */
public class NhinTargetCommunitiesBuilderImpl implements NhinTargetCommunitiesBuilder {

    /**
     * The targets.
     */
    private NhinTargetCommunitiesType targets = null;

    /**
     * The HCI ds.
     */
    private List<String> HCIDs = null;

    @Override
    public void setTarget(String targetHCID) {
        HCIDs = Collections.singletonList(targetHCID);
    }

    @Override
    public void setTargets(List<String> targetHCIDs) {
        HCIDs = targetHCIDs;
    }

    @Override
    public void build() {
        targets = new NhinTargetCommunitiesType();
        for (String s : HCIDs) {
            NhinTargetCommunityType target = new NhinTargetCommunityType();
            HomeCommunityType HCID = new HomeCommunityType();
            HCID.setHomeCommunityId(s);
            target.setHomeCommunity(HCID);
            targets.getNhinTargetCommunity().add(target);
        }
    }

    @Override
    public NhinTargetCommunitiesType getNhinTargetCommunities() {
        return targets;
    }

}
