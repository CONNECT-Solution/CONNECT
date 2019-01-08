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

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.event.TargetEventDescriptionBuilder;
import java.util.ArrayList;
import org.hl7.v3.PRPAIN201306UV02;

public class PRPAIN201306UV02EventDescriptionBuilder extends TargetEventDescriptionBuilder {

    private static final PRPAIN201306UV02HCIDExtractor HCID_EXTRACTOR = new PRPAIN201306UV02HCIDExtractor();
    private static final PRPAIN201306UV02StatusExtractor STATUS_EXTRACTOR = new PRPAIN201306UV02StatusExtractor();

    private Optional<PRPAIN201306UV02> body = Optional.absent();

    @Override
    public void buildErrorCodes() {
    }

    @Override
    public void buildPayloadSizes() {
    }

    @Override
    public void buildPayloadTypes() {
    }

    @Override
    public void buildRespondingHCIDs() {
        if (body.isPresent()) {
            setRespondingHCIDs(new ArrayList<>(HCID_EXTRACTOR.apply(body.get())));
        } else {
            super.buildRespondingHCIDs();
        }
    }

    @Override
    public void buildStatuses() {
        if (!body.isPresent()) {
            return;
        }

        setStatuses(new ArrayList<>(STATUS_EXTRACTOR.apply(body.get())));
    }

    @Override
    public void buildTimeStamp() {
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractTarget(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue == null || !(returnValue instanceof PRPAIN201306UV02)) {
            body = Optional.absent();
        } else {
            body = Optional.of((PRPAIN201306UV02) returnValue);
        }
    }

}
