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
package gov.hhs.fha.nhinc.docretrieve.aspect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;

public class RetrieveDocumentSetResponseTypeDescriptionBuilder extends AssertionEventDescriptionBuilder {

    private static final ErrorCodeExtractor ERROR_CODE_EXTRACTOR = new ErrorCodeExtractor();
    private Optional<RetrieveDocumentSetResponseType> response = Optional.absent();

    @Override
    public void buildTimeStamp() {
    }

    @Override
    public void buildStatuses() {
        if (response.isPresent()) {
            setStatuses(ImmutableList.of(response.get().getRegistryResponse().getStatus()));
        }
    }

    @Override
    public void buildRespondingHCIDs() {
        if (response.isPresent() && response.get() != null
                && response.get().getDocumentResponse() != null) {
            setRespondingHCIDs(new ArrayList<>(extractHcids(response.get().getDocumentResponse())));
        }else {
            setLocalResponder();
        }
    }

    @Override
    public void buildPayloadTypes() {
        // payload type is not available in response object. The slots do exist but the type is not
        // one of the available options.
    }

    @Override
    public void buildPayloadSizes() {
        // payload size is not available in response object. The slots do exist but the size is not
        // one of the available options.
    }

    @Override
    public void buildErrorCodes() {
        if (response.isPresent() && response.get().getRegistryResponse().getRegistryErrorList() != null) {
            List<String> listWithDups = Lists.transform(response.get().getRegistryResponse().getRegistryErrorList()
                    .getRegistryError(), ERROR_CODE_EXTRACTOR);
            setErrorCodes(listWithDups);
        }
    }

    private Set<String> extractHcids(List<DocumentResponse> docResponses){
        Set<String> hcids = new HashSet<>();
        for(DocumentResponse docResponse : docResponses){
            hcids.add(docResponse.getHomeCommunityId());
        }
        return hcids;
    }

    private static class ErrorCodeExtractor implements Function<RegistryError, String> {

        @Override
        public String apply(RegistryError registryError) {
            return registryError.getErrorCode();
        }
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue != null && returnValue instanceof RetrieveDocumentSetResponseType) {
            response = Optional.of((RetrieveDocumentSetResponseType) returnValue);
        } else {
            response = Optional.absent();
        }
    }
}
