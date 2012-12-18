/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Handles RegistryResponseType objects as <em>arguments</em> to the annotated methods. This is typical for deferred
 * response. Use the bare DocSubmissionBaseEventDescriptionBuilder when RegistryResponseType is the return value.
 * 
 * @see DocSubmissionBaseEventDescriptionBuilder
 */
public class DeferredResponseDescriptionBuilder extends AssertionEventDescriptionBuilder {

    private RegistryResponseDescriptionExtractor extractor = new RegistryResponseDescriptionExtractor();
    private RegistryResponseType response;

    @Override
    public void buildTimeStamp() {
        // not extracted
    }

    @Override
    public void buildStatuses() {
        setStatuses(extractor.getStatuses(response));
    }

    @Override
    public void buildRespondingHCIDs() {
        // not extracted
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
        setErrorCodes(extractor.getErrorCodes(response));
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractResponse(arguments);
    }

    private void extractResponse(Object... arguments) {
        if (arguments == null) {
            return;
        }
        for (Object argument : arguments) {
            if (argument instanceof RegistryResponseType) {
                this.response = (RegistryResponseType) argument;
                break;
            }
        }
    }

    @Override
    public void setReturnValue(Object returnValue) {
        // Because this builder is somewhat confusing, throw an exception rather than silently ignore.
        if (returnValue != null) {
            throw new IllegalArgumentException("This builder only handles input params. "
                    + "It is not for use as an afterReturning annotation.");
        }
    }

    RegistryResponseDescriptionExtractor getResponseExtractor() {
        return extractor;
    }

    void setExtractor(RegistryResponseDescriptionExtractor extractor) {
        this.extractor = extractor;
    }
}