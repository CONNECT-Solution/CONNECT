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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import com.google.common.base.Optional;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class PayloadTypeExtractorTest {

    @Test
    public void extract() {
        ExtrinsicObjectType extrinsicObject = createExtrinsicObject("formatCode", "codingScheme", "value");
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = wrapExtrinsicObject(extrinsicObject);

        PayloadTypeExtractor extractor = new PayloadTypeExtractor();
        Optional<String> payloadType = extractor.apply(jaxbWrapper);

        assertEquals("value", payloadType.get());
    }

    @Test
    public void missingClassfication() {
        ExtrinsicObjectType extrinsicObject = createExtrinsicObject("test", "codingScheme", "value");
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = wrapExtrinsicObject(extrinsicObject);

        PayloadTypeExtractor extractor = new PayloadTypeExtractor();
        Optional<String> payloadType = extractor.apply(jaxbWrapper);

        assertFalse(payloadType.isPresent());
    }

    @Test
    public void missingSlot() {
        ExtrinsicObjectType extrinsicObject = createExtrinsicObject("formatCode", "test", "value");
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = wrapExtrinsicObject(extrinsicObject);

        PayloadTypeExtractor extractor = new PayloadTypeExtractor();
        Optional<String> payloadType = extractor.apply(jaxbWrapper);

        assertFalse(payloadType.isPresent());
    }

    private JAXBElement<ExtrinsicObjectType> wrapExtrinsicObject(ExtrinsicObjectType extrinsicObject) {
        QName qName = mock(QName.class);
        return new JAXBElement<>(qName, ExtrinsicObjectType.class, extrinsicObject);
    }

    private ExtrinsicObjectType createExtrinsicObject(String nodeRepValue, String slotName, String slotValue) {
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();

        ClassificationType classification = new ClassificationType();
        classification.setNodeRepresentation(nodeRepValue);

        SlotType1 slot = new SlotType1();
        slot.setName(slotName);

        ValueListType valueList = new ValueListType();
        valueList.getValue().add(slotValue);

        slot.setValueList(valueList);
        classification.getSlot().add(slot);
        extrinsicObject.getClassification().add(classification);

        return extrinsicObject;
    }
}
