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
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.event.TargetEventDescriptionBuilder;
import java.util.List;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;

public class MCCIIN000002UV01EventDescriptionBuilder extends TargetEventDescriptionBuilder {

    private static final TypeCodeExtractor extractor = new TypeCodeExtractor();
    private Optional<MCCIIN000002UV01> returnValue = Optional.absent();

    @Override
    public void buildTimeStamp() {
        // time stamp not here
    }

    @Override
    public void buildStatuses() {
        if (returnValue.isPresent()) {
            List<Optional<String>> transform = Lists.transform(returnValue.get().getAcknowledgement(), extractor);
            setStatuses(Lists.newArrayList(Optional.presentInstances(transform)));
        }
    }

    @Override
    public void buildPayloadTypes() {
        // payload types not here
    }

    @Override
    public void buildPayloadSizes() {
        // payload sizes not here
    }

    @Override
    public void buildErrorCodes() {
        // error codes represented as statuses in this type
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractTarget(arguments);
    }

    @Override
    public void setReturnValue(Object input) {
        if (input != null && (input instanceof MCCIIN000002UV01)) {
            MCCIIN000002UV01 val = (MCCIIN000002UV01) input;
            returnValue = Optional.of(val);
        } else {
            returnValue = Optional.absent();
        }
    }

    private static class TypeCodeExtractor implements Function<MCCIMT000200UV01Acknowledgement, Optional<String>> {

        @Override
        public Optional<String> apply(MCCIMT000200UV01Acknowledgement acknowledgement) {
            CS typeCode = acknowledgement.getTypeCode();
            if (typeCodeHasCode(typeCode)) {
                return Optional.of(typeCode.getCode());
            }
            return Optional.absent();
        }

        private boolean typeCodeHasCode(CS typeCode) {
            return typeCode != null && typeCode.getCode() != null;
        }
    }
}
