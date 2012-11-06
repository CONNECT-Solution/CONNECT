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

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.builder.PayloadSizeExtractor;
import gov.hhs.fha.nhinc.event.builder.PayloadTypeExtractor;
import gov.hhs.fha.nhinc.util.NhincCollections;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * @author akong
 * 
 */
public class ProvideAndRegisterDocumentSetDescriptionBuilder extends BaseEventDescriptionBuilder {

    private static final PayloadTypeExtractor PAYLOAD_TYPE_EXTRACTOR = new PayloadTypeExtractor();
    private static final PayloadSizeExtractor PAYLOAD_SIZE_EXTRACTOR = new PayloadSizeExtractor();

    private ProvideAndRegisterDocumentSetRequestType request;

    public ProvideAndRegisterDocumentSetDescriptionBuilder(ProvideAndRegisterDocumentSetRequestType request) {
        initialize(request);
    }

    private void initialize(ProvideAndRegisterDocumentSetRequestType request) {
        this.request = request;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildTimeStamp()
     */
    @Override
    public void buildTimeStamp() {
        // Not available on request
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildStatuses()
     */
    @Override
    public void buildStatuses() {
        // Not available on request
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildRespondingHCIDs()
     */
    @Override
    public void buildRespondingHCIDs() {
        // Not available on request
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadTypes()
     */
    @Override
    public void buildPayloadTypes() {
        // ProvideAndRegisterDocumentSetRequest/SubmitObjectRequest/RegistryObjectList/ExtrinsicObject[1...n]/Classification[@nodeRepresentation="formatCode"]/Slot[@name="codingScheme"]/ValueList/Value[1]
        if (hasObjectList()) {
            List<Optional<String>> listWithDups = Lists.transform(request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable(), PAYLOAD_TYPE_EXTRACTOR);

            setPayLoadTypes(NhincCollections.fillAbsents(listWithDups, ""));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadSize()
     */
    @Override
    public void buildPayloadSizes() {
        // ProvideAndRegisterDocumentSetRequest/SubmitObjectRequest/RegistryObjectList/ExtrinsicObject[1...n]/Slot[@name=size]/ValueList/Value
        if (hasObjectList()) {
            List<Optional<String>> listWithDups = Lists.transform(request.getSubmitObjectsRequest()
                    .getRegistryObjectList().getIdentifiable(), PAYLOAD_SIZE_EXTRACTOR);

            setPayloadSizes(NhincCollections.fillAbsents(listWithDups, ""));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildNPI()
     */
    @Override
    public void buildNPI() {
        // Not available on request
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildInitiatingHCID()
     */
    @Override
    public void buildInitiatingHCID() {
        // Not available on request
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildErrorCodes()
     */
    @Override
    public void buildErrorCodes() {
        // Not available on request
    }

    private boolean hasObjectList() {
        return request != null && request.getSubmitObjectsRequest() != null
                && request.getSubmitObjectsRequest().getRegistryObjectList() != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setArguments(java.lang.Object[])
     */
    @Override
    public void setArguments(Object... arguments) {
        if (arguments != null && arguments.length == 1
                && arguments[0] instanceof ProvideAndRegisterDocumentSetRequestType) {
            initialize((ProvideAndRegisterDocumentSetRequestType) arguments[0]);
        }
    }
  
}
