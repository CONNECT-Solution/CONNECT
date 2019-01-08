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
import gov.hhs.fha.nhinc.event.DocumentDescriptionBuilderTest;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.util.CollectionUtils;

/**
 * @author akong
 *
 */
public class ProvideAndRegisterDocumentSetDescriptionExtractorTest extends DocumentDescriptionBuilderTest {

    @Test
    public void noRequest() {
        ProvideAndRegisterDocumentSetDescriptionExtractor extractor = new ProvideAndRegisterDocumentSetDescriptionExtractor();

        assertTrue(CollectionUtils.isEmpty(extractor.getPayloadTypes(null)));
        assertTrue(CollectionUtils.isEmpty(extractor.getPayloadSize(null)));
    }

    @Test
    public void emptyRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();

        ProvideAndRegisterDocumentSetDescriptionExtractor extractor = new ProvideAndRegisterDocumentSetDescriptionExtractor();

        assertTrue(CollectionUtils.isEmpty(extractor.getPayloadTypes(request)));
        assertTrue(CollectionUtils.isEmpty(extractor.getPayloadSize(request)));
    }

    @Test
    public void basicRequest() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitRequest = new SubmitObjectsRequest();
        request.setSubmitObjectsRequest(submitRequest);

        addPayloadToRequest(submitRequest, Optional.of("payloadType1"), Optional.of(123));

        ProvideAndRegisterDocumentSetDescriptionExtractor extractor = new ProvideAndRegisterDocumentSetDescriptionExtractor();

        List<String> payloadTypes = extractor.getPayloadTypes(request);
        List<String> payloadSizes = extractor.getPayloadSize(request);

        assertEquals(1, payloadTypes.size());
        assertEquals(1, payloadSizes.size());
        assertEquals("payloadType1", payloadTypes.get(0));
        assertEquals("123", payloadSizes.get(0));
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

        ProvideAndRegisterDocumentSetDescriptionExtractor extractor = new ProvideAndRegisterDocumentSetDescriptionExtractor();

        List<String> payloadTypes = extractor.getPayloadTypes(request);
        List<String> payloadSizes = extractor.getPayloadSize(request);

        assertEquals(5, payloadTypes.size());
        assertEquals(5, payloadSizes.size());

        assertEquals("payloadType1", payloadTypes.get(0));
        assertEquals("1", payloadSizes.get(0));

        assertEquals("payloadType2", payloadTypes.get(1));
        assertEquals("", payloadSizes.get(1));

        assertEquals("", payloadTypes.get(2));
        assertEquals("3", payloadSizes.get(2));

        assertEquals("", payloadTypes.get(3));
        assertEquals("", payloadSizes.get(3));

        assertEquals("payloadType5", payloadTypes.get(4));
        assertEquals("5", payloadSizes.get(4));
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
        JAXBElement<ExtrinsicObjectType> jaxbWrapper = new JAXBElement<>(qName,
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
