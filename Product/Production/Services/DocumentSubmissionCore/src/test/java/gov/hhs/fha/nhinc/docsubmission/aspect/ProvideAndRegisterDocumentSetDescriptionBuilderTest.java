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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import gov.hhs.fha.nhinc.event.DocumentDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * @author akong
 * 
 */
public class ProvideAndRegisterDocumentSetDescriptionBuilderTest extends DocumentDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        ProvideAndRegisterDocumentSetDescriptionBuilder builder = new ProvideAndRegisterDocumentSetDescriptionBuilder(
                null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void emptyRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();

        ProvideAndRegisterDocumentSetDescriptionBuilder builder = new ProvideAndRegisterDocumentSetDescriptionBuilder(
                request);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(null, eventDescription.getPayloadTypes());
        assertEquals(null, eventDescription.getPayloadSizes());
    }

    @Test
    public void basicRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitRequest = new SubmitObjectsRequest();
        request.setSubmitObjectsRequest(submitRequest);
        
        addPayloadToRequest(submitRequest, Optional.of("payloadType1"), Optional.of(123));
        
        ProvideAndRegisterDocumentSetDescriptionBuilder builder = new ProvideAndRegisterDocumentSetDescriptionBuilder(
                request);
        EventDescription eventDescription = getEventDescription(builder);
        
        assertEquals(1, eventDescription.getPayloadTypes().size());
        assertEquals(1, eventDescription.getPayloadSizes().size());
        assertEquals("payloadType1", eventDescription.getPayloadTypes().get(0));
        assertEquals("123", eventDescription.getPayloadSizes().get(0));
    }
    
    @Test
    public void unevenPayloadRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitRequest = new SubmitObjectsRequest();
        request.setSubmitObjectsRequest(submitRequest);
        
        addPayloadToRequest(submitRequest, Optional.of("payloadType1"), Optional.of(1));
        addPayloadToRequest(submitRequest, Optional.of("payloadType2"), Optional.<Integer> absent());
        addPayloadToRequest(submitRequest, Optional.<String> absent(), Optional.of(3));
        addPayloadToRequest(submitRequest, Optional.<String> absent(), Optional.<Integer> absent());
        addPayloadToRequest(submitRequest, Optional.of("payloadType5"), Optional.of(5));
                
        ProvideAndRegisterDocumentSetDescriptionBuilder builder = new ProvideAndRegisterDocumentSetDescriptionBuilder(
                request);
        EventDescription eventDescription = getEventDescription(builder);
        
        assertEquals(5, eventDescription.getPayloadTypes().size());
        assertEquals(5, eventDescription.getPayloadSizes().size());
        
        assertEquals("payloadType1", eventDescription.getPayloadTypes().get(0));
        assertEquals("1", eventDescription.getPayloadSizes().get(0));
        
        assertEquals("payloadType2", eventDescription.getPayloadTypes().get(1));
        assertEquals("", eventDescription.getPayloadSizes().get(1));
        
        assertEquals("", eventDescription.getPayloadTypes().get(2));
        assertEquals("3", eventDescription.getPayloadSizes().get(2));
        
        assertEquals("", eventDescription.getPayloadTypes().get(3));
        assertEquals("", eventDescription.getPayloadSizes().get(3));
        
        assertEquals("payloadType5", eventDescription.getPayloadTypes().get(4));
        assertEquals("5", eventDescription.getPayloadSizes().get(4));
    }
    
    private void addPayloadToRequest(SubmitObjectsRequest response, Optional<String> payloadType, Optional<Integer> size) {
        ExtrinsicObjectType extrinsicObject = new ExtrinsicObjectType();

        if (payloadType.isPresent()) {
            addPayloadType(extrinsicObject, payloadType.get());
        }
        if (size.isPresent()) {
            addPayloadSize(extrinsicObject, size.get());
        }

        addExtrinsicObjectToRequest(response, extrinsicObject);
    }
    
    private void addExtrinsicObjectToRequest(SubmitObjectsRequest request, ExtrinsicObjectType extrinsicObject) {
        QName qName = mock(QName.class);
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = new JAXBElement<ExtrinsicObjectType>(qName,
                ExtrinsicObjectType.class, extrinsicObject);

        RegistryObjectListType registryObjectList = request.getRegistryObjectList();
        if (registryObjectList == null) {
            registryObjectList = new RegistryObjectListType();
            request.setRegistryObjectList(registryObjectList);
        }

        List<JAXBElement<? extends IdentifiableType>> identifiable = request.getRegistryObjectList().getIdentifiable();
        identifiable.add(jaxbWrapper);
    }
}
