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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.document.DocumentConstants;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

/**
 * @author akong
 *
 */
public abstract class DocumentDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    protected void addPayloadType(ExtrinsicObjectType extrinsicObject, String payloadType) {
        ClassificationType classificatonType = addClassification(extrinsicObject);
        SlotType1 slotType = addSlot(classificatonType, DocumentConstants.EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME);
        addValue(payloadType, slotType);
    }

    protected void addPayloadSize(ExtrinsicObjectType extrinsicObject, int size) {
        SlotType1 slotType = new SlotType1();
        extrinsicObject.getSlot().add(slotType);

        slotType.setName(DocumentConstants.EBXML_RESPONSE_SIZE_SLOTNAME);
        addValue("" + size, slotType);
    }

    private ClassificationType addClassification(ExtrinsicObjectType extrinsicObject) {
        ClassificationType classificatonType = new ClassificationType();
        extrinsicObject.getClassification().add(classificatonType);
        classificatonType.setNodeRepresentation(DocumentConstants.EBXML_RESPONSE_NODE_REPRESENTATION_FORMAT_CODE);
        return classificatonType;
    }

    private SlotType1 addSlot(ClassificationType classificatonType, String slotName) {
        SlotType1 slotType = new SlotType1();
        classificatonType.getSlot().add(slotType);

        slotType.setName(slotName);
        return slotType;
    }

    private void addValue(String payloadType, SlotType1 slotType) {
        ValueListType valueListType = slotType.getValueList();
        if (valueListType == null) {
            valueListType = new ValueListType();
            slotType.setValueList(valueListType);
        }

        valueListType.getValue().add(payloadType);
    }
}
