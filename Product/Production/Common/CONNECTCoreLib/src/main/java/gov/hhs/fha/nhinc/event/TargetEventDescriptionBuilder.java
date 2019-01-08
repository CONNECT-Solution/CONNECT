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
package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.event.builder.TargetDescriptionExtractor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jasonasmith
 */
public abstract class TargetEventDescriptionBuilder extends AssertionEventDescriptionBuilder {

    protected Optional<NhinTargetSystemType> target = Optional.absent();
    private final TargetDescriptionExtractor targetExtractor = new TargetDescriptionExtractor();
    private static final Logger LOG = LoggerFactory.getLogger(TargetEventDescriptionBuilder.class);

    @Override
    public void buildRespondingHCIDs() {
        if (target.isPresent()) {
            setRespondingHCIDs(targetExtractor.getResponders(target.get()));
        } else {
            setLocalResponder();
        }
    }

    protected final void extractTarget(Object... arguments) {
        if (arguments != null) {
            for (Object argument : arguments) {
                if (isNhinTargetPresent(argument)){
                    return;
                }
            }
        }
        target = Optional.absent();
    }
    private boolean isNhinTargetPresent(Object argument){
        if (argument != null){
            if (argument instanceof NhinTargetSystemType) {
                target = Optional.of((NhinTargetSystemType) argument);
            } else if (argument instanceof NhinTargetCommunitiesType) {
                final NhinTargetSystemType targetSystem = convertToTargetSystem((NhinTargetCommunitiesType) argument);
                target = Optional.fromNullable(targetSystem);
            } else {
                final NhinTargetSystemType targetSystem = convertToTargetSystem(getNhinTargetCommunitiesType(
                        argument.getClass(), argument));
                target = Optional.fromNullable(targetSystem);
            }
        }
        return target.isPresent();
    }

    private NhinTargetCommunitiesType getNhinTargetCommunitiesType(Class argClass, Object obj) {
        try {
            Method[] methods = argClass.getDeclaredMethods();
            for (Method m : methods) {
                if ("getNhinTargetCommunities".equals(m.getName())) {
                    return (NhinTargetCommunitiesType) m.invoke(obj);
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.info("Unable to extract NhinTargetCommunitiesType from Object", ex);
        }
        return null;
    }

    private static NhinTargetSystemType convertToTargetSystem(NhinTargetCommunitiesType communities) {
        if (communities != null && CollectionUtils.isNotEmpty(communities.getNhinTargetCommunity())
            && communities.getNhinTargetCommunity().get(0) != null
            && communities.getNhinTargetCommunity().get(0).getHomeCommunity() != null) {
            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            targetSystem.setHomeCommunity(communities.getNhinTargetCommunity().get(0).getHomeCommunity());
            targetSystem.setUseSpecVersion(communities.getUseSpecVersion());
            return targetSystem;
        }
        return null;
    }


    @Override
    public void buildVersion() {
        String version = msgContext.getVersion();
        if (StringUtils.isBlank(version) && target.isPresent()) {
            version = target.get().getUseSpecVersion();
        }

        // if version is still blank due to target not being populated, grab it from the AssertionType instead
        if (StringUtils.isBlank(version) && assertion.isPresent()) {
            version = assertion.get().getImplementsSpecVersion();
        }

        description.setVersion(version);
    }
}
