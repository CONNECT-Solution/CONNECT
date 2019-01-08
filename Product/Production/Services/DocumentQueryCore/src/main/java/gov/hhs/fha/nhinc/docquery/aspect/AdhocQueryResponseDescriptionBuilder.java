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
package gov.hhs.fha.nhinc.docquery.aspect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.util.JaxbDocumentUtils;
import gov.hhs.fha.nhinc.util.NhincCollections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdhocQueryResponseDescriptionBuilder extends AssertionEventDescriptionBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AdhocQueryResponseDescriptionBuilder.class);
    private static final ErrorExtractor ERROR_EXTRACTOR = new ErrorExtractor();
    private static final PayloadTypeExtractor PAYLOAD_TYPE_EXTRACTOR = new PayloadTypeExtractor();
    private static final PayloadSizeExtractor PAYLOAD_SIZE_EXTRACTOR = new PayloadSizeExtractor();

    private AdhocQueryResponse response;

    /**
     * Constructor for aspects. Response should be set via <code>setArguments</code>.
     *
     * @see #setArguments(Object...)
     */
    public AdhocQueryResponseDescriptionBuilder() {
    }

    public AdhocQueryResponseDescriptionBuilder(AdhocQueryResponse response) {
        this.response = response;
    }

    @Override
    public void buildTimeStamp() {
        // timestamp not extracted from response object
    }

    @Override
    public void buildStatuses() {
        if (hasStatus()) {
            setStatuses(ImmutableList.of(response.getStatus()));
        }
    }

    @Override
    public void buildRespondingHCIDs() {
        if (hasObjectList()) {
            setRespondingHCIDs(new ArrayList<>(extractHcids(response.getRegistryObjectList())));
        } else {
            setLocalResponder();
        }
    }

    @Override
    public void buildPayloadTypes() {
        if (hasObjectList()) {
            List<Optional<String>> listWithDups = Lists.transform(response.getRegistryObjectList().getIdentifiable(),
                PAYLOAD_TYPE_EXTRACTOR);

            setPayLoadTypes(NhincCollections.fillAbsents(listWithDups, ""));
        }
    }

    @Override
    public void buildPayloadSizes() {
        if (hasObjectList()) {
            List<Optional<String>> listWithDups = Lists.transform(response.getRegistryObjectList().getIdentifiable(),
                PAYLOAD_SIZE_EXTRACTOR);

            setPayloadSizes(NhincCollections.fillAbsents(listWithDups, ""));
        }
    }

    @Override
    public void buildErrorCodes() {
        if (hasErrorList()) {
            List<String> listWithDups = Lists.transform(response.getRegistryErrorList().getRegistryError(),
                ERROR_EXTRACTOR);
            setErrorCodes(listWithDups);
        }
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue == null || !(returnValue instanceof AdhocQueryResponse)) {
            LOG.warn("Unexpected return value: " + returnValue);
        } else {
            response = (AdhocQueryResponse) returnValue;
        }
    }

    private boolean hasStatus() {
        return response != null && response.getStatus() != null;
    }

    private boolean hasObjectList() {
        return response != null && response.getRegistryObjectList() != null;
    }

    private boolean hasErrorList() {
        return response != null && response.getRegistryErrorList() != null;
    }

    private static Set<String> extractHcids(RegistryObjectListType registryObjectList) {
        Set<String> hcids = new HashSet<>();
        if(registryObjectList.getIdentifiable() != null){
            for(JAXBElement<? extends IdentifiableType> identifiable : registryObjectList.getIdentifiable()){
                if(identifiable.getValue() != null
                    && identifiable.getValue() instanceof ExtrinsicObjectType){
                    hcids.add(identifiable.getValue().getHome());
                }
            }
        }
        return hcids;
    }

    private static class ErrorExtractor implements Function<RegistryError, String> {

        @Override
        public String apply(RegistryError error) {
            // errorCode is required by the DTD, so no Optional<String> here
            return error.getErrorCode();
        }
    }

    /**
     * Finds this deep value, if it is present:
     *
     * <pre>
     * RegistryObjectList/ExtrinsicObject/Classification[@nodeRepresentation="formatCode"]/Slot[@name="codingScheme"]/ValueList/Value[1]
     * </Pre>
     */
    private static class PayloadTypeExtractor implements
    Function<JAXBElement<? extends IdentifiableType>, Optional<String>> {

        @Override
        public Optional<String> apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
            IdentifiableType value = jaxbElement.getValue();
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;

            Optional<ClassificationType> classificationType = findClassificationType(extrinsicObjectType,
                DocumentConstants.EBXML_RESPONSE_NODE_REPRESENTATION_FORMAT_CODE);
            if (!classificationType.isPresent()) {
                return Optional.absent();
            }

            return JaxbDocumentUtils.findSlotType(classificationType.get().getSlot(),
                DocumentConstants.EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME);
        }

        private static Optional<ClassificationType> findClassificationType(ExtrinsicObjectType extrinsicObjectType,
            final String expectedType) {
            Predicate<ClassificationType> typePredicate = new Predicate<ClassificationType>() {
                @Override
                public boolean apply(ClassificationType type) {
                    return expectedType.equals(type.getNodeRepresentation());
                }
            };
            return Iterables.tryFind(extrinsicObjectType.getClassification(), typePredicate);
        }
    }

    /**
     * Finds these deep values, if present:
     *
     * <pre>
     * RegistryObjectList/ExtrinsicObject/Slot[@name="size"]/ValueList/Value[1]
     * </pre>
     */
    private static class PayloadSizeExtractor implements
    Function<JAXBElement<? extends IdentifiableType>, Optional<String>> {

        @Override
        public Optional<String> apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
            IdentifiableType value = jaxbElement.getValue();
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;

            return JaxbDocumentUtils.findSlotType(extrinsicObjectType.getSlot(),
                DocumentConstants.EBXML_RESPONSE_SIZE_SLOTNAME);
        }
    }
}
