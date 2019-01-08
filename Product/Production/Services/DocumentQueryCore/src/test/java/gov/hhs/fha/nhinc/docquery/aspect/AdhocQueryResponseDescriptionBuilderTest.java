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

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Mockito.mock;

public class AdhocQueryResponseDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicBuild() {
        AdhocQueryResponse response = getBasicResponse();
        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        assertBasicResponseBuilt(builder);
    }

    @Test
    public void validRespnoseTypes() {
        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder();
        AdhocQueryResponse response = getBasicResponse();
        builder.setReturnValue(response);
        assertBasicResponseBuilt(builder);
    }

    @Test
    public void nullArguments() {
        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder();
        try {
            builder.setReturnValue(null);
        } catch (NullPointerException npe) {
            fail("Should accept null gracefully");
        }
    }

    @Test
    public void errors() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryError error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        addError(response, error);

        error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        addError(response, error);

        error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
        addError(response, error);

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(3, eventDescription.getErrorCodes().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, eventDescription.getErrorCodes().get(0));
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, eventDescription.getErrorCodes().get(1));
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS,
                eventDescription.getErrorCodes().get(2));
    }

    @Test
    public void mixedPayloadTypes() {
        AdhocQueryResponse response = new AdhocQueryResponse();

        addQueryResult(response, Optional.of("payloadType"), Optional.of(12345));
        addQueryResult(response, Optional.of("payloadType2"), Optional.of(1));

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(2, eventDescription.getPayloadTypes().size());
        assertEquals("payloadType", eventDescription.getPayloadTypes().get(0));
        assertEquals("payloadType2", eventDescription.getPayloadTypes().get(1));
        assertEquals(2, eventDescription.getPayloadSizes().size());
        assertEquals("" + 12345, eventDescription.getPayloadSizes().get(0));
        assertEquals("" + 1, eventDescription.getPayloadSizes().get(1));
    }

    @Test
    public void keepDuplicateDescriptionElements() {
        AdhocQueryResponse response = new AdhocQueryResponse();

        addQueryResult(response, Optional.of("payloadType"), Optional.of(12345));
        addQueryResult(response, Optional.of("payloadType"), Optional.of(12345));

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(2, eventDescription.getPayloadTypes().size());
        assertEquals("payloadType", eventDescription.getPayloadTypes().get(0));
        assertEquals("payloadType", eventDescription.getPayloadTypes().get(1));
        assertEquals(2, eventDescription.getPayloadSizes().size());
        assertEquals("" + 12345, eventDescription.getPayloadSizes().get(0));
    }

    @Test
    public void missingPayloadInfoCoercedToEmpty() {
        AdhocQueryResponse response = new AdhocQueryResponse();

        addQueryResult(response, Optional.<String> absent(), Optional.<Integer> absent());

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getPayloadTypes().size());
        assertEquals("", eventDescription.getPayloadTypes().get(0));
        assertEquals(1, eventDescription.getPayloadSizes().size());
        assertEquals("", eventDescription.getPayloadSizes().get(0));
    }

    private AdhocQueryResponse getBasicResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
        addQueryResult(response, Optional.of("payloadType"), Optional.of(12345));
        return response;
    }

    private void assertBasicResponseBuilt(AdhocQueryResponseDescriptionBuilder builder) {
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS, eventDescription.getStatuses().get(0));
        assertEquals(1, eventDescription.getRespondingHCIDs().size());
        assertEquals("home", eventDescription.getRespondingHCIDs().get(0));
        assertEquals(1, eventDescription.getPayloadTypes().size());
        assertEquals("payloadType", eventDescription.getPayloadTypes().get(0));
        assertEquals(1, eventDescription.getPayloadSizes().size());
        assertEquals("" + 12345, eventDescription.getPayloadSizes().get(0));

        assertNull(eventDescription.getTimeStamp());
        assertNull(eventDescription.getNPI()); // TODO: check with BH - is that correct
        assertNull(eventDescription.getInitiatingHCID());
    }

    private void addError(AdhocQueryResponse response, RegistryError error) {
        RegistryErrorList registryErrorList = response.getRegistryErrorList();
        if (registryErrorList == null) {
            registryErrorList = new RegistryErrorList();
            response.setRegistryErrorList(registryErrorList);
        }
        List<RegistryError> registryError = registryErrorList.getRegistryError();
        registryError.add(error);
    }

    private void addQueryResult(AdhocQueryResponse response, Optional<String> payloadType, Optional<Integer> size) {
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();
        extrinsicObject.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        extrinsicObject.setHome("home");

        if (payloadType.isPresent()) {
            addPayloadType(extrinsicObject, payloadType.get());
        }
        if (size.isPresent()) {
            addPayloadSize(extrinsicObject, size.get());
        }

        addQueryResult(response, extrinsicObject);
    }

    private void addPayloadType(ExtrinsicObjectType extrinsicObject, String payloadType) {
        ClassificationType classificatonType = addClassification(extrinsicObject);
        SlotType1 slotType = addSlot(classificatonType, DocumentConstants.EBXML_RESPONSE_CODE_CODESCHEME_SLOTNAME);
        addValue(payloadType, slotType);
    }

    private void addPayloadSize(ExtrinsicObjectType extrinsicObject, int size) {
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

    private void addQueryResult(AdhocQueryResponse response, ExtrinsicObjectType extrinsicObject) {
        QName qName = mock(QName.class);
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = new JAXBElement<>(qName,
                ExtrinsicObjectType.class, extrinsicObject);

        RegistryObjectListType registryObjectList = response.getRegistryObjectList();
        if (registryObjectList == null) {
            registryObjectList = new RegistryObjectListType();
            response.setRegistryObjectList(registryObjectList);
        }

        List<JAXBElement<? extends IdentifiableType>> identifiable = response.getRegistryObjectList().getIdentifiable();
        identifiable.add(jaxbWrapper);
    }
}
