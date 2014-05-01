/*
 * Copyright (c) 2013, United States Government, as represented by the Secretary of Health and Human Services.
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

package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.event.builder.TargetDescriptionExtractor;

/**
 *
 * @author jasonasmith
 */
public abstract class TargetEventDescriptionBuilder extends AssertionEventDescriptionBuilder {

    protected Optional<NhinTargetSystemType> target = Optional.absent();
    private final TargetDescriptionExtractor targetExtractor = new TargetDescriptionExtractor();

    @Override
    public final void buildRespondingHCIDs() {
        if (target.isPresent()) {
            setRespondingHCIDs(targetExtractor.getResponders(target.get()));
        } else {
            setLocalResponder();
        }
    }

    protected final void extractTarget(Object... arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; ++i) {
                if (arguments[i] instanceof NhinTargetSystemType) {
                    target = Optional.of((NhinTargetSystemType) arguments[i]);
                    return;
                }else if(arguments[i] instanceof NhinTargetCommunitiesType){
                    NhinTargetCommunitiesType communities = (NhinTargetCommunitiesType) arguments[i];
                    if(communities != null && communities.getNhinTargetCommunity() != null
                        && !communities.getNhinTargetCommunity().isEmpty()
                        && communities.getNhinTargetCommunity().get(0) != null
                        && communities.getNhinTargetCommunity().get(0).getHomeCommunity() != null){
                        target = Optional.of(convertToTargetSystem(communities.getNhinTargetCommunity().get(0)));
                        return;
                    }
                }
            }
        }
        target = Optional.absent();
    }
    
    private NhinTargetSystemType convertToTargetSystem(NhinTargetCommunityType communityType){
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(communityType.getHomeCommunity());
        
        return targetSystem;
    }
}
