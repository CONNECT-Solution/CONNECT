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
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.event.builder.PayloadSizeExtractor;
import gov.hhs.fha.nhinc.event.builder.PayloadTypeExtractor;
import gov.hhs.fha.nhinc.util.NhincCollections;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akong
 *
 */
public class ProvideAndRegisterDocumentSetDescriptionExtractor {

    private static final PayloadTypeExtractor PAYLOAD_TYPE_EXTRACTOR = new PayloadTypeExtractor();
    private static final PayloadSizeExtractor PAYLOAD_SIZE_EXTRACTOR = new PayloadSizeExtractor();

    /**
     * Extracts the payload types of a ProvideAndRegisterDocumentSetRequestType.
     *
     * @param request the ProvideAndRegisterDocumentSetRequestType whose values are to be extracted
     * @return a list of string containing the payload types
     */
    public List<String> getPayloadTypes(ProvideAndRegisterDocumentSetRequestType request) {
        List<String> payloadTypes = new ArrayList<>();
        if (hasObjectList(request)) {
            List<Optional<String>> listWithDups = Lists.transform(request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable(), PAYLOAD_TYPE_EXTRACTOR);

            payloadTypes = NhincCollections.fillAbsents(listWithDups, "");
        }

        return payloadTypes;
    }

    /**
     * Extracts the payload sizes of a ProvideAndRegisterDocumentSetRequestType.
     *
     * @param request the ProvideAndRegisterDocumentSetRequestType whose values are to be extracted
     * @return a list of string containing the payload sizes
     */
    public List<String> getPayloadSize(ProvideAndRegisterDocumentSetRequestType request) {
        List<String> payloadSizes = new ArrayList<>();

        if (hasObjectList(request)) {
            List<Optional<String>> listWithDups = Lists.transform(request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable(), PAYLOAD_SIZE_EXTRACTOR);

            payloadSizes = NhincCollections.fillAbsents(listWithDups, "");
        }

        return payloadSizes;
    }

    private boolean hasObjectList(ProvideAndRegisterDocumentSetRequestType request) {
        return request != null && request.getSubmitObjectsRequest() != null
                && request.getSubmitObjectsRequest().getRegistryObjectList() != null;
    }
}
