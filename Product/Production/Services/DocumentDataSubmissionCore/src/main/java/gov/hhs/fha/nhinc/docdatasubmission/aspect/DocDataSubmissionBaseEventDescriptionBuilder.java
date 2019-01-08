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
package gov.hhs.fha.nhinc.docdatasubmission.aspect;

import static gov.hhs.fha.nhinc.docdatasubmission.aspect.RegistryResponseDescriptionExtractor.getErrorCodes;
import static gov.hhs.fha.nhinc.docdatasubmission.aspect.RegistryResponseDescriptionExtractor.getStatuses;
import gov.hhs.fha.nhinc.event.TargetEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import java.text.SimpleDateFormat;
import java.util.Date;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class DocDataSubmissionBaseEventDescriptionBuilder extends TargetEventDescriptionBuilder {

    private final RegisterDocumentSetDescriptionExtractor requestExtractor;
    private RegisterDocumentSetRequestType request;
    private RegistryResponseType response;

    public DocDataSubmissionBaseEventDescriptionBuilder() {
        requestExtractor = new RegisterDocumentSetDescriptionExtractor();
    }

    public DocDataSubmissionBaseEventDescriptionBuilder(final RegisterDocumentSetDescriptionExtractor requestExtractor,
        final AssertionDescriptionExtractor assertionExtractor) {
        this.requestExtractor = requestExtractor;
        super.setAssertionExtractor(assertionExtractor);
    }

    @Override
    public void buildTimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        setTimeStamp(timeStamp);
    }

    @Override
    public void buildStatuses() {
        setStatuses(getStatuses(response));
    }

    @Override
    public void buildPayloadTypes() {
        setPayLoadTypes(requestExtractor.getPayloadTypes(request));
    }

    @Override
    public void buildPayloadSizes() {
        setPayloadSizes(requestExtractor.getPayloadSize(request));
    }

    @Override
    public void buildErrorCodes() {
        setErrorCodes(getErrorCodes(response));
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractRequest(arguments);
        extractTarget(arguments);
    }

    private void extractRequest(Object[] arguments) {
        if (arguments != null) {
            for (Object argument : arguments) {
                if (argument instanceof RegisterDocumentSetRequestType) {
                    request = (RegisterDocumentSetRequestType) argument;
                    return;
                }
            }
        }
        request = null;
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue != null && returnValue instanceof RegistryResponseType) {
            response = (RegistryResponseType) returnValue;
        }
    }

    RegisterDocumentSetRequestType getRequest() {
        return request;
    }
}
