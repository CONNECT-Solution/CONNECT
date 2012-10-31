/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
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
package gov.hhs.fha.nhinc.docquery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

import java.util.List;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class AdhocQueryResponseDescriptionBuilder extends BaseEventDescriptionBuilder {

    private static final HCIDExtractor HCID_EXTRACTOR = new HCIDExtractor();
    private static final ErrorExtractor ERROR_EXTRACTOR = new ErrorExtractor();
    private static final PayloadTypeExtractor PAYLOAD_TYPE_EXTRACTOR = new PayloadTypeExtractor();
    private static final StatusExtractor STATUS_EXTRACTOR = new StatusExtractor();

    private final AdhocQueryResponse response;

    public AdhocQueryResponseDescriptionBuilder(AdhocQueryResponse response) {
        this.response = response;
    }

    @Override
    public void buildTimeStamp() {
        // timestamp not extracted from response object
    }

    @Override
    public void buildStatuses() {
        if (hasObjectList()) {
            List<String> listWithDups = Lists.transform(response.getRegistryObjectList().getIdentifiable(),
                    STATUS_EXTRACTOR);
            setStatuses(ImmutableSet.copyOf(listWithDups).asList());
        }
    }

    @Override
    public void buildRespondingHCIDs() {
        if (hasObjectList()) {
            List<String> listWithDups = Lists.transform(response.getRegistryObjectList().getIdentifiable(),
                    HCID_EXTRACTOR);
            setRespondingHCIDs(ImmutableSet.copyOf(listWithDups).asList());
        }
    }

    @Override
    public void buildPayloadTypes() {
        if (hasObjectList()) {
            List<String> listWithDups = Lists.transform(response.getRegistryObjectList().getIdentifiable(),
                    PAYLOAD_TYPE_EXTRACTOR);
            setPayLoadTypes(ImmutableSet.copyOf(listWithDups).asList());
        }
    }

    @Override
    public void buildPayloadSize() {
        // payload size not available in response
    }

    @Override
    public void buildNPI() {
        // NPI not available in response
    }

    @Override
    public void buildInitiatingHCID() {
        // Initiating HCIDs not available in response
    }

    @Override
    public void buildErrorCodes() {
        if (hasErrorList()) {
            List<String> listWithDups = Lists.transform(response.getRegistryErrorList().getRegistryError(),
                    ERROR_EXTRACTOR);
            setErrorCodes(ImmutableSet.copyOf(listWithDups).asList());
        }
    }

    private boolean hasObjectList() {
        return response != null && response.getRegistryObjectList() != null;
    }

    private boolean hasErrorList() {
        return response != null && response.getRegistryErrorList() != null;
    }

    private static class HCIDExtractor implements Function<JAXBElement<? extends IdentifiableType>, String> {

        @Override
        public String apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
            IdentifiableType value = jaxbElement.getValue();
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;
            return extrinsicObjectType.getHome();
        }
    }

    private static class ErrorExtractor implements Function<RegistryError, String> {

        @Override
        public String apply(RegistryError error) {
            return error.getErrorCode();
        }
    }

    private static class PayloadTypeExtractor implements Function<JAXBElement<? extends IdentifiableType>, String> {

        @Override
        public String apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
            IdentifiableType value = jaxbElement.getValue();
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;
            return extrinsicObjectType.getObjectType();
        }
    }

    private static class StatusExtractor implements Function<JAXBElement<? extends IdentifiableType>, String> {

        @Override
        public String apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
            IdentifiableType value = jaxbElement.getValue();
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;
            return extrinsicObjectType.getStatus();
        }
    }
}
