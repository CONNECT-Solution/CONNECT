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
package gov.hhs.fha.nhinc.event.builder;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.util.JaxbDocumentUtils;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts the payload type off an Extrinsic Object.
 *
 * ExtrinsicObject/Classification[@nodeRepresentation="formatCode"]/Slot[@name="codingScheme"]/ValueList/Value[0]
 *
 */
public class PayloadTypeExtractor implements Function<JAXBElement<? extends IdentifiableType>, Optional<String>> {

    private static final Logger LOG = LoggerFactory.getLogger(PayloadTypeExtractor.class);

    @Override
    public Optional<String> apply(JAXBElement<? extends IdentifiableType> jaxbElement) {
        Optional<String> payloadType = Optional.absent();

        IdentifiableType value = jaxbElement.getValue();
        if (value instanceof ExtrinsicObjectType) {
            ExtrinsicObjectType extrinsicObjectType = (ExtrinsicObjectType) value;

            Optional<ClassificationType> classificationType = findClassificationType(extrinsicObjectType,
                DocumentConstants.EBXML_RESPONSE_NODE_REPRESENTATION_FORMAT_CODE);
            if (classificationType.isPresent()) {
                payloadType = JaxbDocumentUtils.findSlotType(classificationType.get().getSlot(),
                    DocumentConstants.EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME);
            }
        } else {
            LOG.warn("Passed in element has an unexpected type.  Expecting ExtrinsicObjectType.  Returning as absent.");
        }

        return payloadType;
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
