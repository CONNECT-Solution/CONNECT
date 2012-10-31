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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.junit.Test;

public class AdhocQueryResponseDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicBuild() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
        addQueryResult(response, DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE);

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS, eventDescription.getStatuses().get(0));
        assertEquals(1, eventDescription.getRespondingHCIDs().size());
        assertEquals("home", eventDescription.getRespondingHCIDs().get(0));
        assertEquals(1, eventDescription.getPayloadTypes().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE, eventDescription
                .getPayloadTypes().get(0));

        assertNull(eventDescription.getTimeStamp());
        assertNull(eventDescription.getPayloadSize());
        assertNull(eventDescription.getNPI()); // TODO: check with BH - is that correct
        assertNull(eventDescription.getInitiatingHCID());
    }

    @Test
    public void error() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryError error = new RegistryError();
        error.setErrorCode(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);

        addError(response, error);

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getErrorCodes().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE, eventDescription.getErrorCodes().get(0));
    }

    @Test
    public void mixedPayloadTypes() {
        AdhocQueryResponse response = new AdhocQueryResponse();

        addQueryResult(response, DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE);
        addQueryResult(response, DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE + "alt");

        AdhocQueryResponseDescriptionBuilder builder = new AdhocQueryResponseDescriptionBuilder(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(2, eventDescription.getPayloadTypes().size());
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE, eventDescription
                .getPayloadTypes().get(0));
        assertEquals(DocumentConstants.XDS_QUERY_RESPONSE_EXTRINSIC_OBJCECT_OBJECT_TYPE + "alt", eventDescription
                .getPayloadTypes().get(1));
    }

    private void addError(AdhocQueryResponse response, RegistryError error) {
        RegistryErrorList registryErrorList = new RegistryErrorList();
        response.setRegistryErrorList(registryErrorList);
        List<RegistryError> registryError = registryErrorList.getRegistryError();
        registryError.add(error);
    }

    private void addQueryResult(AdhocQueryResponse response, String objectType) {
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();
        extrinsicObject.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        extrinsicObject.setHome("home");
        extrinsicObject.setObjectType(objectType);
        addQueryResult(response, extrinsicObject);
    }

    private void addQueryResult(AdhocQueryResponse response, ExtrinsicObjectType extrinsicObject) {
        QName qName = mock(QName.class);
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = new JAXBElement<ExtrinsicObjectType>(qName,
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
