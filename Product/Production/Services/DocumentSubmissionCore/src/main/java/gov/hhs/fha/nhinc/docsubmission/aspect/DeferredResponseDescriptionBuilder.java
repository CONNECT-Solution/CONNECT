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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.event.TargetEventDescriptionBuilder;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Handles RegistryResponseType objects as <em>arguments</em> to the annotated methods. This is typical for deferred
 * response. Use the bare DocSubmissionBaseEventDescriptionBuilder when RegistryResponseType is the return value.
 *
 * @see DocSubmissionBaseEventDescriptionBuilder
 */
public class DeferredResponseDescriptionBuilder extends TargetEventDescriptionBuilder {

    private RegistryResponseDescriptionExtractor extractor = new RegistryResponseDescriptionExtractor();
    private Optional<RegistryResponseType> response;

    @Override
    public void buildTimeStamp() {
        // not extracted
    }

    @Override
    public void buildStatuses() {
        if(response.isPresent()){
            setStatuses(extractor.getStatuses(response.get()));
        }
    }

    @Override
    public void buildPayloadTypes() {
        // not extracted
    }

    @Override
    public void buildPayloadSizes() {
        // not extracted
    }

    @Override
    public void buildErrorCodes() {
        if(response.isPresent()){
            setErrorCodes(extractor.getErrorCodes(response.get()));
        }
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractResponse(arguments);
        extractTarget(arguments);
    }

    private void extractResponse(Object... arguments) {
        if (arguments == null) {
            return;
        }
        for (Object argument : arguments) {
            if (argument instanceof RegistryResponseType) {
                this.response = Optional.of((RegistryResponseType) argument);
                break;
            }
        }
    }

    @Override
    public void setReturnValue(Object returnValue) {

    }

    RegistryResponseDescriptionExtractor getResponseExtractor() {
        return extractor;
    }

    void setExtractor(RegistryResponseDescriptionExtractor extractor) {
        this.extractor = extractor;
    }
}
