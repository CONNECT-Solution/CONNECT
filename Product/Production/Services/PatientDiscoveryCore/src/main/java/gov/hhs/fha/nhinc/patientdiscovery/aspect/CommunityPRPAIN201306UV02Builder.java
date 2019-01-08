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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

public class CommunityPRPAIN201306UV02Builder extends AssertionEventDescriptionBuilder {

    private PRPAIN201306UV02HCIDExtractor hcidExtractor = new PRPAIN201306UV02HCIDExtractor();
    private PRPAIN201306UV02StatusExtractor statusExtractor = new PRPAIN201306UV02StatusExtractor();
    private Optional<RespondingGatewayPRPAIN201306UV02ResponseType> response = Optional.absent();

    @Override
    public void buildTimeStamp() {
        // no time stamp here
    }

    @Override
    public void buildStatuses() {
        setStatuses(new ArrayList<>(applyExtractor(statusExtractor)));
    }

    @Override
    public void buildRespondingHCIDs() {
        setRespondingHCIDs(new ArrayList<>(applyExtractor(hcidExtractor)));
    }

    @Override
    public void buildPayloadTypes() {
        // no payload type here
    }

    @Override
    public void buildPayloadSizes() {
        // no payload size here
    }

    @Override
    public void buildErrorCodes() {
        // no error codes here
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue != null) {
            response = Optional.of((RespondingGatewayPRPAIN201306UV02ResponseType) returnValue);
        } else {
            response = Optional.absent();
        }
    }

    void setHCIDExtractor(PRPAIN201306UV02HCIDExtractor hcidExtractor) {
        this.hcidExtractor = hcidExtractor;
    }

    void setStatusExtractor(PRPAIN201306UV02StatusExtractor statusExtractor) {
        this.statusExtractor = statusExtractor;
    }

    private Set<String> applyExtractor(Function<PRPAIN201306UV02, Set<String>> function) {
        if (!response.isPresent()) {
            return Collections.EMPTY_SET;
        }
        Set<String> result = new HashSet<>();
        for (PRPAIN201306UV02 item : unwrap(response.get())) {
            result.addAll(function.apply(item));
        }
        return result;
    }

    private Iterable<PRPAIN201306UV02> unwrap(RespondingGatewayPRPAIN201306UV02ResponseType input) {
        List<PRPAIN201306UV02> result = new ArrayList<>();
        for (CommunityPRPAIN201306UV02ResponseType community : input.getCommunityResponse()) {
            if (community.getPRPAIN201306UV02() != null) {
                result.add(community.getPRPAIN201306UV02());
            }
        }
        return result;
    }

    Optional<RespondingGatewayPRPAIN201306UV02ResponseType> getReturnValue() {
        return response;
    }
}
